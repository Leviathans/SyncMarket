package org.example.binance;

import cn.hutool.json.JSONUtil;
import lombok.extern.log4j.Log4j2;
import okhttp3.WebSocket;
import org.example.base.SyncMarketDataTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("sbinanceSyncMarketDataTemplate")
@Log4j2
public class SBinanceSyncMarketDataTemplate extends SyncMarketDataTemplate {

    private WebSocket sBinanceWebSocketClient;

    @Override
    public List<String> mappingTradePairs4Exchange(List<String> tokens) {
        List<String> exchangeTradePairs = new ArrayList<>();
        //BTCUSDT
        for (String token : tokens) {
            StringBuilder sb = new StringBuilder(token);
            exchangeTradePairs.add(sb.append("usd").toString().toLowerCase());
        }
        return exchangeTradePairs;
    }

    @Override
    public List<String> createStreams(List<String> tradePairs) {
        List<String> streams = new ArrayList<>();
        for (String tradePair : tradePairs) {
            String stream = tradePair + "_perp@aggTrade";
            streams.add(stream);
        }
        return streams;
    }

    @Override
    public void sub(List<String> streams) {
        RequestBody requestBody = new RequestBody();
        requestBody.setParams(streams);
        requestBody.setId(System.currentTimeMillis());
        sBinanceWebSocketClient.send(JSONUtil.toJsonStr(requestBody));
        log.info("binance swap sub :{}", JSONUtil.toJsonStr(requestBody));
    }

    @Autowired
    public void setsBinanceWebSocketClient(WebSocket sBinanceWebSocketClient) {
        this.sBinanceWebSocketClient = sBinanceWebSocketClient;
    }
}
