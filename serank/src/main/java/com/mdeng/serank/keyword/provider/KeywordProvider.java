package com.mdeng.serank.keyword.provider;

import com.mdeng.serank.keyword.KeywordRank;

/**
 * Keyword is grouped
 * 
 * @author Administrator
 *
 */
public interface KeywordProvider {

  /**
   * Whether has next group
   * 
   * @return
   */
  boolean hasNextGroup();

  /**
   * To get next group
   * 
   * @return group id
   */
  int nextGroup();

  /**
   * Whether has next keyword in current group.
   * 
   * @return
   */
  boolean hasNextKeyword();

  /**
   * To get next keyword in current group.
   * 
   * @return
   */
  KeywordRank nextKeyword();
}
