package com.ymss.steed.common.cache;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Key-value local cache
 * 
 * @author Administrator
 *
 * @param <K>
 * @param <V>
 */
public class LocalCache<K, V> {

    private LoadingCache<K, V> cache;

    public LocalCache(String spec, CacheLoader<K, V> loader) {
        cache = CacheBuilder.from(spec).build(loader);
    }

    public LocalCache(long expir, long maxSize, CacheLoader<K, V> loader) {
        cache =
                CacheBuilder.newBuilder().expireAfterWrite(expir, TimeUnit.MILLISECONDS)
                        .maximumSize(maxSize).build(loader);
    }

    /**
     * Get underline cache
     * 
     * @return
     */
    public LoadingCache<K, V> getCache() {
        return cache;
    }

    /**
     * Initialize cache using keys
     * 
     * @param keys
     */
    public LocalCache<K, V> load(Collection<K> keys) {
        if (keys == null || keys.isEmpty()) return this;

        for (K k : keys) {
            try {
                cache.get(k);
            } catch (ExecutionException e) {}
        }

        return this;
    }

    /**
     * Refresh keys at fixed rate
     * 
     * @param keys
     * @param period
     * @param unit
     * @return
     */
    public LocalCache<K, V> refreshAtFixedRate(final Collection<K> keys, long period, TimeUnit unit) {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                load(keys);

            }
        }, 0, period, unit);

        return this;
    }
}
