package com.example.ticker.service;


/*
*
 "timestamp": "2019-05-05T18:25:43.515Z", // timestamp of the quote
 "symbol": "D05.SI", // ticker symbol
 "sharesTraded": "5k", // volume for the trade being quoted
 "priceTraded": 26.55, // bid price
 "changeDirection": "up", // indicates whether stock is trading higher or lower
 "changeAmount": 0.17 // difference in price from previous day
* */

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@JsonFormat
@JsonIgnoreProperties(ignoreUnknown = true)
public class TickerObject implements Cloneable{
    private String symbol;

    public TickerObject(String symbol) {
        this.symbol = symbol;
    }

    public Object clone() throws
            CloneNotSupportedException
    {
        return super.clone();
    }

    @Override
    public String toString() {
        return "TickerObject{" +
                "symbol='" + symbol +
                '}';
    }
}
