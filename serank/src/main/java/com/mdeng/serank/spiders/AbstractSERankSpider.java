package com.mdeng.serank.spiders;

import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mdeng.serank.KeywordRank;
import com.mdeng.serank.SEType;

public abstract class AbstractSERankSpider implements Callable<List<KeywordRank>> {

  protected Logger logger = LoggerFactory.getLogger(this.getClass());
  protected List<KeywordRank> krs = Lists.newArrayList();
  /**
   * Completed number of keywords / total number of keywords
   */
  protected float progress;

  /**
   * Max grab times for each keyword
   */
  protected int retries = 3;

  public AbstractSERankSpider() {}

  public AbstractSERankSpider(List<KeywordRank> krs) {
    this.krs = krs;
  }

  protected abstract KeywordRank grab(KeywordRank keyword);

  protected abstract SEType getSEType();

  @Override
  public List<KeywordRank> call() throws Exception {
    for (int i = 0; i < krs.size(); i++) {
      KeywordRank kr = krs.get(i);
      if (kr == null) {
        logger.warn("input Keyword rank null");
      } else {
        kr.setKeyword(kr.getKeyword() != null ? kr.getKeyword().trim() : null);
        if (Strings.isNullOrEmpty(kr.getKeyword())) {
          kr.setResult(GrabResult.SUCCESS);
        }

        int cur = 0;
        while (cur++ < retries && !GrabResult.SUCCESS.equals(kr.getResult())) {
          kr = grab(kr);
        }
        logger.info("Result for keyword {}:{}", kr.getKeyword(), kr.getResult());
      }

      progress = (i + 1) / krs.size();
    }
    return krs;
  }

  public final float getProgess() {
    return progress;
  }
}
