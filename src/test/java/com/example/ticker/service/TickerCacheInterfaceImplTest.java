package com.example.ticker.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
class TickerCacheInterfaceImplTest {

    @Autowired
    TickerCacheInterface tickerCacheInterface;

    static ArrayList<String> addDataList1 = new ArrayList<>();
    static ArrayList<String> addDataList2 = new ArrayList<>();
    static {

        addDataList1.add("ticker1");
        addDataList1.add("ticker2");
        addDataList1.add("ticker3");
        addDataList1.add("ticker4");
        addDataList1.add("ticker5");
        addDataList1.add("ticker6");
        addDataList1.add("ticker7");
        addDataList1.add("ticker8");
        addDataList1.add("ticker9");
        addDataList1.add("ticker10");

        addDataList2.add("ticker10");
        addDataList2.add("ticker9");
        addDataList2.add("ticker8");
        addDataList2.add("ticker7");
        addDataList2.add("ticker6");
        addDataList2.add("ticker5");
        addDataList2.add("ticker4");
        addDataList2.add("ticker3");
        addDataList2.add("ticker2");
        addDataList2.add("ticker1");
    }

    @Test
    void addToCache() {
        int t = 100;
        Thread[] threads = new Thread[t];
        for (int i = 0; i < t/2; i++) {
            threads[i] = new Thread(() -> {
                addDataList1.forEach((k) -> {
                    tickerCacheInterface.addToCache(k);
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            });
        }

        for (int i = t/2; i < t; i++) {
            threads[i] = new Thread(() -> {
                addDataList2.forEach((k) -> {
                    tickerCacheInterface.addToCache(k);
                    try {
                        Thread.sleep(1900);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            });
        }

        for (Thread thread : threads){
            thread.start();
        }

        for (Thread thread : threads){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /*Thread sleepThread = new Thread(() -> {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        sleepThread.start();

        try {
            sleepThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        Assertions.assertTrue(true);

    }
}