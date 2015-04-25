package com.mdeng.serank.keywords;

public interface KeywordProvider {

  /**
   * Whether has next keyword.
   * 
   * @return
   */
  boolean hasNext();

  /**
   * To get next keyword.
   * 
   * @return
   */
  KeywordRank next();
}
