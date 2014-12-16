package com.ideng.steed.common.http;

import java.io.IOException;
import java.net.URI;
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
     * Http get on specified uri with reties, return null if all failed.
     * 
     * @param uri
     * @param handler
     * @param reties
     * @return
     * @throws Exception
     */
    public static <T> T get(final URI uri, ResponseHandler<T> handler, int reties) throws Exception {
        Args.notNull(uri, "uri");
        Args.notNull(handler, "handler");

        for (int i = 0; i < reties; i++) {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                HttpGet httpget = new HttpGet(uri);
                CloseableHttpResponse response = httpclient.execute(httpget);
                return handler.handleResponse(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
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
    public static <T> T get(final URI uri, ResponseHandler<T> handler, long timeout, TimeUnit unit)
            throws Exception {
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

}
