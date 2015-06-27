package com.mdeng.common.dataimport.excel;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.mdeng.common.dal.IEntity;
import com.mdeng.common.dataimport.AbstractImporter;

/**
 * 处理较大的Excel(>10MB)
 * 
 * @author Administrator
 *
 */
public class LargeExcelImporter extends AbstractImporter {

  private static final int MAX_QUEUE_SIZE = 1000;
  private static final int MAX_THREAD_SIZE = 5;
  private BlockingQueue<SimpleRow> queue;
  private ExecutorService es;
  private Function<SimpleRow, ? extends IEntity> function;
  private CountDownLatch cdl;

  public LargeExcelImporter(String path, Function<SimpleRow, ? extends IEntity> function) {
    super(path);
    queue = new ArrayBlockingQueue<SimpleRow>(MAX_QUEUE_SIZE);
    es = Executors.newFixedThreadPool(MAX_THREAD_SIZE);
    this.function = function;
    cdl = new CountDownLatch(files.length);
  }

  public void exec() {
    for (File file : files) {
      es.submit(new Scaner(file));
    }

    for (int i = 0; i < MAX_THREAD_SIZE; i++) {
      es.submit(new Consumer());
    }
  }

  public void waitForComplete() {
    try {
      es.shutdown();
      es.awaitTermination(5, TimeUnit.DAYS);
    } catch (InterruptedException e) {}
  }

  class Scaner implements Runnable {
    private final File file;

    public Scaner(File file) {
      this.file = file;
    }

    @Override
    public void run() {
      try {
        OPCPackage pkg = OPCPackage.open(file);
        XSSFReader r = new XSSFReader(pkg);

        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        ReadOnlySharedStringsTable rosst = new ReadOnlySharedStringsTable(pkg);
        SheetContentsRowHandler contentsHandler = new SheetContentsRowHandler();
        ContentHandler handler =
            new XSSFSheetXMLHandler(r.getStylesTable(), rosst, contentsHandler, true);
        parser.setContentHandler(handler);

        // rId2 found by processing the Workbook
        // Seems to either be rId# or rSheet#
        // InputStream sheet0 = r.getSheet("rId0");
        // InputSource sheetSource = new InputSource(sheet0);
        // parser.parse(sheetSource);
        // sheet0.close();
        Iterator<InputStream> sheets = r.getSheetsData();
        while (sheets.hasNext()) {
          InputStream sheet = sheets.next();
          InputSource sheetSource = new InputSource(sheet);
          parser.parse(sheetSource);
          sheet.close();
        }
        cdl.countDown();
        logger.info("{0} scaned.", file.getName());
      } catch (Exception e) {
        logger.error("{0} scaned failed: {1}", file.getName(), e.getMessage());
      }

    }

  }

  class Consumer implements Runnable {
    @Override
    public void run() {
      try {
        while (cdl.getCount() > 0 || queue.size() > 0) {
          SimpleRow row = queue.poll(5, TimeUnit.MILLISECONDS);
          if (row != null) function.apply(row);
        }
      } catch (InterruptedException e) {}
    }
  }

  class SheetContentsRowHandler implements SheetContentsHandler {
    private SimpleRow row = null;

    @Override
    public void startRow(int rowNum) {
      row = new SimpleRowImpl(rowNum);
    }

    @Override
    public void endRow() {
      try {
        queue.put(row);
      } catch (InterruptedException e) {
        logger.warn("row {0} interruptted.", row.getRowIndex());
      }
    }

    @Override
    public void cell(String cellReference, String formattedValue) {
      int cellIndex = cellReference.charAt(0) - 'A';
      row.setCellValue(cellIndex, formattedValue);
    }

    class SimpleRowImpl implements SimpleRow {
      private Map<String, String> innerRow = Maps.newHashMap();
      private int rowIndex;

      SimpleRowImpl(int rowIndex) {
        super();
        this.rowIndex = rowIndex;
      }

      @Override
      public String getCellValue(int cellIndex) {
        return innerRow.get(cellReference(cellIndex));
      }

      @Override
      public int getRowIndex() {
        return rowIndex;
      }

      @Override
      public void setCellValue(int cellIndex, String value) {
        innerRow.put(cellReference(cellIndex), value);
      }

      private String cellReference(int cellIndex) {
        return String.valueOf((char) ('A' + cellIndex)) + (getRowIndex() + 1);
      }
    }

    @Override
    public void headerFooter(String arg0, boolean arg1, String arg2) {
      // nothing
    }
  }

}
