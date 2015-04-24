package com.mdeng.common.http;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;

import com.google.common.collect.Lists;

public class HttpProxyPool {
  private static final int DEFAULT_POOL_SIZE = 100;
  private static final String CHECK_PROXY_URL = "http://www.baidu.com/";

  /**
   * Proxy can be shared in multiple HTTP requests
   */
  private static List<HttpHost> pool = Lists.newArrayList();
  private ProxyProvider provider;

  public HttpProxyPool() {
    init();
    monitor();
  }

  private void monitor() {
    Executors.newScheduledThreadPool(1).schedule(new Runnable() {

      @Override
      public void run() {
        for (HttpHost httpHost : pool) {
          if (!checkProxy(httpHost)) {

          }
        }

      }
    }, 60, TimeUnit.SECONDS);

  }

  private void init() {
    List<HttpHost> proxies = provider.getProxies(DEFAULT_POOL_SIZE);
    for (HttpHost httpHost : proxies) {
      if (!pool.contains(httpHost) && checkProxy(httpHost)) {
        pool.add(httpHost);
      }
    }
  }

  public HttpHost getProxy() {
    if (pool.size() > 0) {
      // random a proxy

    }
    return null;
  }

  public boolean checkProxy(HttpHost proxy) {
    return false;
  }
}
