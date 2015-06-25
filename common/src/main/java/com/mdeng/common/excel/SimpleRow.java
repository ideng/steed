package com.mdeng.common.excel;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Excel的一行
 * 
 * @author hui.deng
 *
 */
public class SimpleRow {

  private Map<String, String> innerRow = Maps.newHashMap();
  private int rowIndex;
  
  /**
   * 获取单元格数据
   * 
   * @param cellIndex zero based
   * @return
   */
  public String getCellValue(int cellIndex) {
    return innerRow.get(String.valueOf((char)('A'+cellIndex))+(getRowIndex()+1));
  }

  /**
   * 获取行号, 从0开始
   * 
   * @return
   */
  public int getRowIndex() {
    return rowIndex;
  }

  void setRowIndex(int rowIndex) {
    this.rowIndex = rowIndex;
  }
  
  void setCellValue(String key, String value) {
    innerRow.put(key, value);
  }
}
