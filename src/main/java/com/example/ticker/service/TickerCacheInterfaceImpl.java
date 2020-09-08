package com.example.ticker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TickerCacheInterfaceImpl implements TickerCacheInterface {

    @Autowired
    private RestTemplate restTemplate;

    private final ConcurrentHashMap<String, AtomicInteger> tickerCacheMap = new ConcurrentHashMap();

    private final DelayQueue<CacheObject> cleaningUpQueue = new DelayQueue<>();

    private String topTenRequested = null;

    public TickerCacheInterfaceImpl() {

        Thread postCache = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (tickerCacheMap){
                    if(tickerCacheMap != null && !tickerCacheMap.isEmpty()) {
                        org.json.JSONObject jsonObject = new org.json.JSONObject(tickerCacheMap);
                        System.out.println(jsonObject);


                        try {
                            URL url = new URL("http://localhost:8081/Counter/PushData");
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.setRequestMethod("POST");
                            con.setRequestProperty("Content-Type", "application/json; utf-8");
                            con.setRequestProperty("Accept", "application/json");
                            con.setDoOutput(true);

                            try (OutputStream os = con.getOutputStream()) {
                                byte[] input = jsonObject.toString().getBytes("utf-8");
                                os.write(input, 0, input.length);
                            }

                            try (BufferedReader br = new BufferedReader(
                                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                                StringBuilder response = new StringBuilder();
                                String responseLine = null;
                                while ((responseLine = br.readLine()) != null) {
                                    response.append(responseLine.trim());
                                }
                                topTenRequested = response.toString();
                                System.out.println("topTenRequested :: "+topTenRequested);
                            }
                            tickerCacheMap.clear();
                        }  catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            topTenRequested = null;
                        } catch (IOException e) {
                            e.printStackTrace();
                            topTenRequested = null;
                        }
                    }else{
                        try {
                            HttpHeaders headers = new HttpHeaders();
                            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

                            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8081/Counter/GetData")
                                    .queryParam("count", 5);

                            HttpEntity<?> entity = new HttpEntity<>(headers);

                            HttpEntity<String> response = restTemplate.exchange(
                                    builder.toUriString(),
                                    HttpMethod.GET,
                                    entity,
                                    String.class);

                            System.out.println("Get Response:" + response.getBody());
                            topTenRequested = response.getBody();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        postCache.setDaemon(true);
        postCache.start();
    }

    @Override
    public String getTopTen() {
        return topTenRequested;
    }

    @Override
    public void addToCache(String ticker){
        tickerCacheMap.compute(ticker, (k,v) -> {
            if(v == null) {
                v = new AtomicInteger(1);
            }else {
                v.getAndIncrement();
            }
            return v;
        });
    }

}
