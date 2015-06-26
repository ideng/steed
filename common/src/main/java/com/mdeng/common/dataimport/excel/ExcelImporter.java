package com.mdeng.common.dataimport.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.google.common.base.Function;
import com.mdeng.common.dal.IEntity;

/**
 * 从Excel文件导入数据至数据库，只支持.xlsx文件
 * 
 * @author hui.deng
 *
 * @param <T>
 */
public class ExcelImporter {

  private Logger logger = LoggerFactory.getLogger(this.getClass());
  private File[] files;

  /**
   * 指定输入文件或文件夹路径
   * 
   * @param path
   */
  public ExcelImporter(String path) {
    File file = new File(path);
    if (file.isDirectory()) {
      files = file.listFiles();
    } else if (file.isFile()) {
      files = new File[1];
      files[0] = file;
    }
  }

  /**
   * 处理小文件
   * 
   * @param function
   * @throws Exception
   */
  public void exec(final Function<Row, ? extends IEntity> function) {
    ExecutorService es = Executors.newCachedThreadPool();
    for (final File file : files) {
      es.submit(new Runnable() {

        @Override
        public void run() {
          try {
            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
            for (Row row : wb.getSheetAt(0)) {
              function.apply(row);
            }
            logger.info("{0} completed.", file.getName());
          } catch (Exception e) {
            logger.error("{0} import failed: {1}", file.getName(), e.getMessage());
          }
        }

      });
    }


  }


  /**
   * 处理大文件
   * 
   * @param contentsHandler
   * @throws Exception
   */
  public void exec(final SheetContentsHandler contentsHandler) {
    ExecutorService es = Executors.newCachedThreadPool();
    for (final File file : files) {
      es.submit(new Runnable() {

        @Override
        public void run() {
          try {
            OPCPackage pkg = OPCPackage.open(file);
            XSSFReader r = new XSSFReader(pkg);

            XMLReader parser =
                XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
            ReadOnlySharedStringsTable rosst = new ReadOnlySharedStringsTable(pkg);
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

            logger.info("{0} completed.", file.getName());
          } catch (Exception e) {
            logger.error("{0} import failed: {1}", file.getName(), e.getMessage());
          }
        }

      });
    }
  }
}
