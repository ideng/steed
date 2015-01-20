package com.ideng.common.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.Args;

import com.ideng.common.utils.Jsons;

/**
 * Http operations
 * 
 * @author Administrator
 *
 */
public class HttpTools {

    private ExecutorService es = null;

    public HttpTools() {
        es = Executors.newCachedThreadPool();
    }

    /**
     * Http get on specified uri.
     * 
     * @param uri
     * @return
     * @throws Exception
     */
    public static <T> T get(final URI uri, ResponseHandler<T> handler) throws IOException {
        Args.notNull(uri, "uri");
        Args.notNull(handler, "handler");

        CloseableHttpClient httpclient = null;
        try {
            httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(uri);
            CloseableHttpResponse response = httpclient.execute(httpget);
            return handler.handleResponse(response);
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
    }

    /**
     * Http get on specified uri with timeout.
     * 
     * @param uri
     * @param handler
     * @param timeout
     * @param unit
     * @return
     * @throws Exception
     */
    public <T> T get(final URI uri, ResponseHandler<T> handler, long timeout, TimeUnit unit)
            throws Exception {
        Args.notNull(uri, "uri");
        Args.notNull(handler, "handler");

        final CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            final HttpGet httpget = new HttpGet(uri);
            Future<CloseableHttpResponse> future = es.submit(new Callable<CloseableHttpResponse>() {

                public CloseableHttpResponse call() throws Exception {
                    return httpclient.execute(httpget);
                }

            });

            return handler.handleResponse(future.get(timeout, unit));
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
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
        public String handleResponse(HttpResponse response) throws ClientProtocolException,
                IOException {
            try {
                InputStream in = response.getEntity().getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line = null;
                StringBuilder builder = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }
                return builder.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

    }
}
