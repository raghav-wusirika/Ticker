package com.example.ticker.service;

import java.lang.ref.SoftReference;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class CacheObject implements Delayed {
    private final String key;
    private final long expiryTime;

    public CacheObject(String key, long expiryTime) {
        this.key = key;
        this.expiryTime = expiryTime;
    }

    public String getKey() {
        return key;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(expiryTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return Long.compare(expiryTime, ((CacheObject) o).expiryTime);
    }
}
