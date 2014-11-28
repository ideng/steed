package com.ideng.steed.common.http;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.Args;

import com.ideng.steed.common.exceptions.SteedError;

/**
 * Http operations
 * 
 * @author Administrator
 *
 */
public class HttpClients2 {

    /**
     * Http get on specified uri.
     * 
     * @param uri
     * @param handler
     * @return
     * @throws Exception
     */
    public static <T> T get(final URI uri, ResponseHandler<T> handler) throws Exception {
        Args.notNull(uri, "uri");
        Args.notNull(handler, "handler");
        
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpget = new HttpGet(uri);
            CloseableHttpResponse response = httpclient.execute(httpget);
            return handler.handleResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
            throw SteedError.ExecutionError(e);
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
    public static <T> T get(final URI uri, ResponseHandler<T> handler, long timeout,
            TimeUnit unit) throws Exception {
        Args.notNull(uri, "uri");
        Args.notNull(handler, "handler");
        
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<CloseableHttpResponse> future = es.submit(new Callable<CloseableHttpResponse>() {

            @Override
            public CloseableHttpResponse call() throws Exception {
                try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                    HttpGet httpget = new HttpGet(uri);
                    return httpclient.execute(httpget);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw SteedError.ExecutionError(e);
                }
            }

        });

        CloseableHttpResponse response = future.get(timeout, unit);
        return handler.handleResponse(response);
    }
}
