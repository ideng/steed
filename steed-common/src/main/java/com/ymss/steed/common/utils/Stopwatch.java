package com.ymss.steed.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Simple stop watch
 * 
 * @author Administrator
 *
 */
public class Stopwatch {
    private long startTime;
    private List<Long> markedTimes = new ArrayList<Long>();

    /**
     * Start watch
     */
    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void mark() {
        markedTimes.add(System.currentTimeMillis());
    }

    /**
     * Get time passed
     * 
     * @param unit
     * @return null if not started or marked
     */
    public List<Long> getDuration(TimeUnit unit) {
        if (startTime == 0 || markedTimes.isEmpty()) return null;

        List<Long> durations = new ArrayList<Long>();
        for (TimeUnit u : TimeUnit.values()) {
            if (u.equals(unit)) {
                for (long l : markedTimes) {
                    long duration = l - startTime;
                    durations.add(u.convert(duration, TimeUnit.MILLISECONDS));
                }
            }
        }

        return durations;
    }
}
