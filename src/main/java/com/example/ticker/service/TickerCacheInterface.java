package com.example.ticker.service;

public interface TickerCacheInterface {

    public String getTopTen();

    public void addToCache(String ticker);

}
