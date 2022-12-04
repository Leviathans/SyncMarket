package org.example.huobi;

import cn.hutool.json.JSONUtil;
import lombok.extern.log4j.Log4j2;
import okhttp3.WebSocket;
import org.example.base.SyncMarketDataTemplate;
import org.example.huobi.protocol.HuobiRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("uhuobiSyncMarketDataTemplate")
@Log4j2
public class UHuobiSyncMarketDataTemplate extends SyncMarketDataTemplate {

    private WebSocket uHuobiWebSocketClient;

    @Override
    public List<String> mappingTradePairs4Exchange(List<String> tokens) {
        //BTC-USDT
        List<String> exchangeTradePairs = new ArrayList<>();
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
            String stream = String.format("market.%s.trade.detail", tradePair);
            streams.add(stream);
        }
        return streams;
    }

    @Override
    public void sub(List<String> streams) {
        for (String stream : streams) {
            HuobiRequestBody requestBody = new HuobiRequestBody();
            requestBody.setId(String.valueOf(System.currentTimeMillis()));
            requestBody.setSub(stream);
            uHuobiWebSocketClient.send(JSONUtil.toJsonStr(requestBody));
            log.info("huobi U sub : {}", JSONUtil.toJsonStr(requestBody));
        }
    }

    @Autowired
    public void setuHuobiWebSocketClient(WebSocket uHuobiWebSocketClient) {
        this.uHuobiWebSocketClient = uHuobiWebSocketClient;
    }
}
