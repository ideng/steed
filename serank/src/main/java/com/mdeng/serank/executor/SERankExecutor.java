package com.mdeng.serank.executor;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.mdeng.serank.spider.AbstractSERankSpider;

/**
 * Rank executor using multi-thread.
 * 
 * @author Administrator
 *
 */
public class SERankExecutor {
  private static final int THREAD_COUNT = 5;
  private List<AbstractSERankSpider> spiders;

  public List<AbstractSERankSpider> getSpiders() {
    return spiders;
  }

  public void setSpiders(List<AbstractSERankSpider> spiders) {
    this.spiders = spiders;
  }

  public void execute() {
    if (spiders == null) return;

    ExecutorService es = Executors.newCachedThreadPool();
    for (AbstractSERankSpider spider : spiders) {
      for (int i = 0; i < THREAD_COUNT; i++) {
        es.submit(spider);
      }
    }

    es.shutdown();
  }
}
