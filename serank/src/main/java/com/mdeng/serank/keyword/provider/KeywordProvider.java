package com.mdeng.serank.keyword.provider;

import com.mdeng.serank.keyword.KeywordRank;

/**
 * Keyword is grouped
 * 
 * @author Administrator
 *
 */
public interface KeywordProvider {

  boolean hasNextGroup();

  int nextGroup();

  /**
   * Whether has next.
   * 
   * @return
   */
  boolean hasNextKeyword(int groupId);

  /**
   * To get next keyword.
   * 
   * @return
   */
  KeywordRank nextKeyword(int groupId);
}
