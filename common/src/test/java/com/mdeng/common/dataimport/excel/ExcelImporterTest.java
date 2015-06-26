package com.mdeng.common.dataimport.excel;

import java.io.File;
import java.net.URISyntaxException;

import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;

import com.google.common.base.Function;
import com.mdeng.common.dal.IEntity;

public class ExcelImporterTest {

  @Test
  public void testSmall() throws URISyntaxException {
    String path = new File(this.getClass().getClassLoader().getResource("small.xlsx").toURI()).getAbsolutePath();
    SmallExcelImporter sei = new SmallExcelImporter(path, new SmallFunction());
    sei.exec();
    sei.waitForComplete();
  }

  class SmallFunction implements Function<Row, TestEntity> {

    @Override
    public TestEntity apply(Row input) {
      System.out.println("row " + input.getRowNum());
      return null;
    }

  }

  class TestEntity implements IEntity {

  }
}
