package org.example.binance;

import cn.hutool.json.JSONUtil;
import lombok.extern.log4j.Log4j2;
import okhttp3.WebSocket;
import org.example.base.SyncMarketDataTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("ubinanceSyncMarketDataTemplate")
@Log4j2
public class UBinanceSyncMarketDataTemplate extends SyncMarketDataTemplate {

    private WebSocket uBinanceWebSocketClient;

    @Override
    public List<String> mappingTradePairs4Exchange(List<String> tokens) {
        List<String> exchangeTradePairs = new ArrayList<>();
        //BTCUSDT
        for (String token : tokens) {
            StringBuilder sb = new StringBuilder(token);
            exchangeTradePairs.add(sb.append("usdt").toString().toLowerCase());
        }
        return exchangeTradePairs;
    }

    @Override
    public List<String> createStreams(List<String> tradePairs) {
        List<String> streams = new ArrayList<>();
        for (String tradePair : tradePairs) {
            String steam = tradePair + "@aggTrade";
            streams.add(steam);
        }
        return streams;
    }

    @Override
    public void sub(List<String> streams) {
        RequestBody requestBody = new RequestBody();
        requestBody.setParams(streams);
        requestBody.setId(System.currentTimeMillis());
        uBinanceWebSocketClient.send(JSONUtil.toJsonStr(requestBody));
        log.info("binance U sub :{}", JSONUtil.toJsonStr(requestBody));
    }

    @Autowired
    public void setuBinanceWebSocketClient(WebSocket uBinanceWebSocketClient) {
        this.uBinanceWebSocketClient = uBinanceWebSocketClient;
    }
}
