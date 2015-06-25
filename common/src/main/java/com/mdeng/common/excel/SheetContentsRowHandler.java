package com.mdeng.common.excel;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
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
  }

  @Override
  public void startRow(int rowNum) {
    super.startRow(rowNum);
    row = new SimpleRowImpl(rowNum);
  }

  @Override
  public void endRow() {
    function.apply(row);
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
}
