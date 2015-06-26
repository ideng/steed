package com.mdeng.common.dataimport.excel;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.base.Function;
import com.mdeng.common.dal.IEntity;
import com.mdeng.common.dataimport.AbstractImporter;

/**
 * 处理较小的Excel
 * 
 * @author Administrator
 *
 */
public class SmallExcelImporter extends AbstractImporter {

  private static final int MAX_QUEUE_SIZE = 1000;
  private static final int MAX_THREAD_SIZE = 5;
  private BlockingQueue<Row> queue;
  private ExecutorService es;
  private Function<Row, ? extends IEntity> function;

  public SmallExcelImporter(String path, Function<Row, ? extends IEntity> function) {
    super(path);
    queue = new ArrayBlockingQueue<Row>(MAX_QUEUE_SIZE);
    es = Executors.newCachedThreadPool();
    this.function = function;
  }

  public void exec() {
    es.submit(new Scaner());
    for (int i = 0; i < MAX_THREAD_SIZE; i++) {
      es.submit(new Runnable() {

        @Override
        public void run() {
          try {
            function.apply(queue.take());
          } catch (InterruptedException e) {}
        }
      });
    }
  }

  public void waitForComplete() {
    try {
      es.shutdown();
      es.awaitTermination(5, TimeUnit.DAYS);
    } catch (InterruptedException e) {}
  }

  class Scaner implements Runnable {

    @Override
    public void run() {
      for (final File file : files) {
        try {
          XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
          for (Row row : wb.getSheetAt(0)) {
            queue.put(row);
          }
          logger.info("{0} scaned.", file.getName());
        } catch (Exception e) {
          logger.error("{0} scaned failed: {1}", file.getName(), e.getMessage());
        }

      }
    }

  }

}
