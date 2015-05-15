package com.mdeng.common.http;

import java.util.List;

import org.apache.http.HttpHost;

public interface ProxyProvider {
  List<HttpHost> getProxies(int size);
}
