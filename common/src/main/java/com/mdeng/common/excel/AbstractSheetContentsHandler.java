package com.mdeng.common.excel;

import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;


public abstract class AbstractSheetContentsHandler implements SheetContentsHandler {

  protected int rowNum;

  @Override
  public void startRow(int rowNum) {
    this.rowNum = rowNum;
  }

  @Override
  public void endRow() {
    // nothing
  }

  @Override
  public void headerFooter(String text, boolean isHeader, String tagName) {
    // nothing
  }

}
