package com.mdeng.serank.executor;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;
import com.mdeng.common.utils.Stopwatch;
import com.mdeng.serank.keyword.provider.BasicKeywordProvider;
import com.mdeng.serank.spider.AbstractSERankSpider;
import com.mdeng.serank.spider.BaiduRankSpider;

/**
 * Rank executor using multi-thread.
 * 
 * @author Administrator
 *
 */
public class SERankExecutor {
  private int threadCount = 5;
  private List<AbstractSERankSpider> spiders;

  public List<AbstractSERankSpider> getSpiders() {
    return spiders;
  }

  public void setSpiders(List<AbstractSERankSpider> spiders) {
    this.spiders = spiders;
  }

  public int getThreadCount() {
    return threadCount;
  }

  public void setThreadCount(int threadCount) {
    this.threadCount = threadCount;
  }

  public void execute() {
    if (spiders == null) return;

    ExecutorService es = Executors.newCachedThreadPool();
    for (AbstractSERankSpider spider : spiders) {
      for (int i = 0; i < threadCount; i++) {
        es.submit(spider);
      }
    }

    es.shutdown();
    try {
      es.awaitTermination(1, TimeUnit.HOURS);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public static void main(String[] args) {
    SERankExecutor executor = new SERankExecutor();
    List<AbstractSERankSpider> lst = Lists.newArrayList();
    BaiduRankSpider spider = new BaiduRankSpider();
    spider.setKeywordProvider(new BasicKeywordProvider());
    lst.add(spider);
    executor.setSpiders(lst);
    
    Stopwatch watch = new Stopwatch();
    executor.setThreadCount(1);
    watch.start();
    executor.execute();
    watch.mark();
    System.out.println("1 thread:"+watch.getDuration(TimeUnit.SECONDS).get(0));
    
    spider.setKeywordProvider(new BasicKeywordProvider());
    watch = new Stopwatch();
    executor.setThreadCount(3);
    watch.start();
    executor.execute();
    watch.mark();
    System.out.println("3 thread:"+watch.getDuration(TimeUnit.SECONDS).get(0));
    
    spider.setKeywordProvider(new BasicKeywordProvider());
    watch = new Stopwatch();
    executor.setThreadCount(5);
    watch.start();
    executor.execute();
    watch.mark();
    System.out.println("5 thread:"+watch.getDuration(TimeUnit.SECONDS).get(0));
    System.exit(0);
  }
}
