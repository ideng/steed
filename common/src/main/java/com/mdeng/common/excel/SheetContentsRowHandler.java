package com.mdeng.common.excel;

import com.google.common.base.Function;
import com.mdeng.common.dal.IEntity;

/**
 * 按行处理
 * 
 * @author hui.deng
 *
 */
public class SheetContentsRowHandler extends AbstractSheetContentsHandler {

  private SimpleRow row = null;
  private Function<SimpleRow, ? extends IEntity> function;

  public SheetContentsRowHandler(Function<SimpleRow, ? extends IEntity> function) {
    this.function = function;
    row = new SimpleRow();
  }

  @Override
  public void startRow(int rowNum) {
    super.startRow(rowNum);
    row.setRowIndex(rowNum);
  }

  @Override
  public void endRow() {
    function.apply(row);
  }

  @Override
  public void cell(String cellReference, String formattedValue) {
    row.setCellValue(cellReference, formattedValue);
  }

}
