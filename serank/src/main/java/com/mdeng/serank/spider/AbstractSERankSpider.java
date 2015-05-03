package com.mdeng.serank.spider;

import java.util.List;

import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.mdeng.serank.SERankRegex;
import com.mdeng.serank.SEType;
import com.mdeng.serank.keyword.KeywordRank;
import com.mdeng.serank.keyword.Rank;
import com.mdeng.serank.keyword.consumer.KeywordRankConsumer;
import com.mdeng.serank.keyword.provider.KeywordProvider;

/**
 * Abstract spider for keyword rank in search engine.
 * 
 * @author Administrator
 *
 */
public abstract class AbstractSERankSpider implements Runnable {

  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * Max grab times for each keyword
   */
  protected int retries = 3;
  protected SERankRegex serRegex = new SERankRegex();
  protected KeywordProvider keywordProvider;
  protected KeywordRankConsumer keywordRankConsumer;

  protected abstract SEType getSEType();

  @Override
  public void run() {
    if (keywordProvider == null) {
      logger.error("keyword provider null");
      return;
    }

    while (keywordProvider.hasNext()) {
      KeywordRank kr = keywordProvider.next();
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

        if (keywordRankConsumer != null) {
          keywordRankConsumer.consume(kr);
        }
      }
    }
  }

  protected KeywordRank grab(KeywordRank keyword) {
    String url = getUrl(keyword.getKeyword());
    String content = getPageContent(url);
    
    if (Strings.isNullOrEmpty(content)) {
      keyword.setResult(GrabResult.EMPTY_PAGE);
      return keyword;
    }
    
    List<String> divs = getDivs(content);
    if (divs == null || divs.size() == 0) {
      keyword.setResult(GrabResult.EMPTY_FIELD);
      return keyword;
    }
    
    for (String div : divs) {
      Rank ri = extractRank(div);
      if (ri != null) {
        keyword.addRankInfo(ri);
      }
    }

    return keyword;
  }

  protected String getPageContent(String url) {
    //TODO:...
    return null;
  }
  
  public KeywordRankConsumer getKeywordRankConsumer() {
    return keywordRankConsumer;
  }

  public void setKeywordRankConsumer(KeywordRankConsumer keywordRankConsumer) {
    this.keywordRankConsumer = keywordRankConsumer;
  }

  public KeywordProvider getKeywordProvider() {
    return keywordProvider;
  }

  public void setKeywordProvider(KeywordProvider keywordProvider) {
    this.keywordProvider = keywordProvider;
  }

  protected abstract String getUrl(String keyword);
  
  /**
   * Extract a rank information
   * 
   * @param div
   * @return
   */
  protected abstract Rank extractRank(String div);

  /**
   * Get html div tags for keyword.
   * 
   * @param keyword
   * @return
   */
  protected abstract List<String> getDivs(String keyword);
}
