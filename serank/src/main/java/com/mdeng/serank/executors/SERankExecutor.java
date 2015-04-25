package com.mdeng.serank.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.mdeng.serank.keywords.KeywordProvider;
import com.mdeng.serank.keywords.KeywordRank;

public class SERankExecutor {
  private KeywordProvider keywordProvider;

  public KeywordProvider getKeywordProvider() {
    return keywordProvider;
  }

  public void setKeywordProvider(KeywordProvider keywordProvider) {
    this.keywordProvider = keywordProvider;
  }

  public void execute() {
    ExecutorService es = Executors.newCachedThreadPool();
    while (keywordProvider.hasNext()) {
      KeywordRank kr = keywordProvider.next();
      // es.submit(task);
    }

    es.shutdown();
  }
}
