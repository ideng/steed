package com.mdeng.common.http;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mdeng.common.utils.Jsons;

/**
 * To build a HTTP request.
 * 
 * @author hui.deng
 *
 */
public class HttpRequestBuilder {
  private static Logger logger = LoggerFactory.getLogger(HttpRequestBuilder.class);
  private CloseableHttpClient httpclient;
  private HttpRequestBase method;
  private RequestConfig config;
  private int retries = 1;

  HttpRequestBuilder() {
    httpclient = HttpClients.createDefault();
  }

  public static HttpRequestBuilder create() {
    return new HttpRequestBuilder();
  }

  public HttpRequestBuilder get(String url) {
    method = new HttpGet(url);
    return this;
  }

  public HttpRequestBuilder post(String url) {
    method = new HttpPost(url);
    return this;
  }

  public HttpRequestBuilder method(HttpRequestBase method) {
    this.method = method;
    return this;
  }

  public HttpRequestBuilder config(RequestConfig config) {
    this.config = config;
    return this;
  }

  public HttpRequestBuilder retries(int retries) {
    this.retries = retries;
    return this;
  }

  public void execute() {
    execute(null);
  }

  public <T> T execute(ResponseHandler<T> handler) {
    for (int i = 0; i < retries; i++) {
      try {
        if (config != null) method.setConfig(config);
        CloseableHttpResponse response = httpclient.execute(method);
        if (handler != null)
          return handler.handleResponse(response);
        else
          EntityUtils.consume(response.getEntity());
      } catch (Exception ex) {
        logger.error("{} time for: {}", i + 1, ex.getMessage());
      } finally {
        if (method != null) method.releaseConnection();
      }
    }
    return null;
  }


  /**
   * Get status line from http response header.
   * 
   * @author hui.deng
   *
   */
  public static class StatusLineHandler implements ResponseHandler<StatusLine> {

    public StatusLine handleResponse(HttpResponse response) throws ClientProtocolException,
        IOException {
      return response.getStatusLine();
    }

  }

  /**
   * JsonEntityHandler
   * 
   * @author hui.deng
   *
   * @param <T>
   */
  public static class JsonEntityHandler<T> implements ResponseHandler<T> {

    private Class<T> clazz;

    public JsonEntityHandler(Class<T> clazz) {
      this.clazz = clazz;
    }

    @Override
    public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
      try {
        InputStream in = response.getEntity().getContent();
        return Jsons.json2Obj(in, clazz);
      } catch (Exception e) {
        e.printStackTrace();
      }

      return null;
    }

  }

  /**
   * String entity handler
   * 
   * @author hui.deng
   *
   */
  public static class StringEntityHandler implements ResponseHandler<String> {

    @Override
    public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
      return EntityUtils.toString(response.getEntity());
    }

  }
}
