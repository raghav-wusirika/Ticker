package com.example.ticker;

import com.example.ticker.service.TickerCacheInterface;
import com.example.ticker.service.TickerObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Ticker")
public class TickerController {

    @Autowired
    private TickerCacheInterface tickerCacheInterface;

    @GetMapping(path="/GetData", produces = "application/json")
    public @ResponseBody Object getMostReqData() {
        return new ResponseEntity<>(tickerCacheInterface.getTopTen(), HttpStatus.OK);
    }

    @PostMapping(path = "/PushData", consumes = "application/json", produces = "application/json")
    public @ResponseBody Object pushData(@RequestBody TickerObject newData) {
        tickerCacheInterface.addToCache(newData.getSymbol());
        return new ResponseEntity<>(newData, HttpStatus.OK);
    }
}
