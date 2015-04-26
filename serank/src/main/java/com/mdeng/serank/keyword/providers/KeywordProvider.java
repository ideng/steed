package com.mdeng.serank.keyword.providers;

import com.mdeng.serank.keyword.KeywordRank;

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
