package com.mdeng.serank.keywords;

public interface KeywordRankConsumer {

  /**
   * Consume a keyword rank information.
   * 
   * @param keywordRank
   */
  void consume(KeywordRank keywordRank);
}
