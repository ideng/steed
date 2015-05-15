package com.mdeng.common.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.apache.http.HttpHost;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class BasicProxyProvider implements ProxyProvider {

  private static final String urlTemplate =
      "http://115.29.136.51/get.php?tid=A3969657691719&num=%d";

  @Override
  public List<HttpHost> getProxies(int size) {
    List<HttpHost> ret = Lists.newArrayList();

    String url = String.format(urlTemplate, size);
    String content =
        HttpRequestBuilder.create().get(url).execute(new HttpRequestBuilder.StringEntityHandler());
    BufferedReader bufferedReader = new BufferedReader(new StringReader(content));

    String line = null;
    try {
      while ((line = bufferedReader.readLine()) != null) {
        try {
          String[] splits = line.split(":");
          if (splits.length != 2) {
            continue;
          } else if (Strings.isNullOrEmpty(splits[0]) || Strings.isNullOrEmpty(splits[0].trim())
              || Strings.isNullOrEmpty(splits[1]) || Strings.isNullOrEmpty(splits[1].trim())) {
            continue;
          }

          HttpHost httpHost = new HttpHost(splits[0].trim(), Integer.parseInt(splits[1]));
          ret.add(httpHost);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return ret;
  }

}
