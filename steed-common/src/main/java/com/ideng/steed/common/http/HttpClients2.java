package com.ideng.steed.common.http;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Http operations
 * 
 * @author Administrator
 *
 */
public class HttpClients2 {

    public static void get(final URI url, long timeout, TimeUnit unit) {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<CloseableHttpResponse> future = es.submit(new Callable<CloseableHttpResponse>() {

            @Override
            public CloseableHttpResponse call() throws Exception {
                try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                    HttpGet httpget = new HttpGet(url);
                    return httpclient.execute(httpget);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

        });

        try {
            future.get(timeout, unit);
        } catch (Exception e) {}
    }
}
