package com.mdeng.serank.spider;

import java.net.URL;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.mdeng.common.http.BasicProxyProvider;
import com.mdeng.common.http.HttpProxyPool;
import com.mdeng.common.http.HttpRequestBuilder;
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
  //protected static HttpProxyPool pool = new HttpProxyPool(new BasicProxyProvider());
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
        //System.out.println("Result for keyword {}:{}"+ kr.getKeyword()+" "+ kr.getResult());
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

    keyword.setResult(GrabResult.SUCCESS);
    return keyword;
  }

  protected String getPageContent(String url) {
    // TODO:...
    String content = HttpRequestBuilder.create().get(url).execute(new HttpRequestBuilder.StringEntityHandler());
//    HttpHost host = pool.getProxy();
//    RequestConfig config = RequestConfig.custom().setProxy(host).build();
//    String content = HttpRequestBuilder.create().config(config).get(url).execute(new HttpRequestBuilder.StringEntityHandler());
    return content;
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

  protected String getMainHost(String url) {
    if (url.indexOf("...") >= 0) {
      url = serRegex.matchNthValue(url, "(.*?)\\.\\.\\.", 1);
    }

    if (url.indexOf("http://") < 0) {
      url = "http://" + url;
    }

    String host = "";
    try {
      URL ui = new URL(url);
      String tempHost = ui.getHost();
      if (!Strings.isNullOrEmpty(tempHost)) {
        String[] splits = tempHost.split("\\.");
        int len = splits.length;
        if (len < 2) {
          host = splits[0];
        } else if (tempHost.indexOf(".com.cn") >= 0 || tempHost.indexOf(".net.cn") >= 0
            || tempHost.indexOf(".org.cn") >= 0 || tempHost.indexOf(".gov.cn") >= 0
            || tempHost.indexOf(".cn.com") >= 0) {
          host = splits[len - 3] + "." + splits[len - 2] + "." + splits[len - 1];
        } else {
          host = splits[len - 2] + "." + splits[len - 1];
        }
      }
    } catch (Throwable t) {
      logger.error("HOST解析出错：" + url, t);
    }

    return host;
  }
}
