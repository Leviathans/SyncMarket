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

@Service("shuobiSyncMarketDataTemplate")
@Log4j2
public class SHuobiSyncMarketDataTemplate extends SyncMarketDataTemplate {

    private WebSocket sHuobiWebSocketClient;

    @Override
    public List<String> mappingTradePairs4Exchange(List<String> tokens) {
        List<String> exchangeTradePairs = new ArrayList<>();
        for (String token : tokens) {
            StringBuilder sb = new StringBuilder(token.toUpperCase());
            exchangeTradePairs.add(sb.append("-").append("USD").toString());
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
            HuobiRequestBody huobiRequestBody = new HuobiRequestBody();
            huobiRequestBody.setId(String.valueOf(System.currentTimeMillis()));
            huobiRequestBody.setSub(stream);
            sHuobiWebSocketClient.send(JSONUtil.toJsonStr(huobiRequestBody));
            log.info("huobi swap sub : {}", JSONUtil.toJsonStr(huobiRequestBody));
        }
    }

    @Autowired
    public void setsHuobiWebSocketClient(WebSocket sHuobiWebSocketClient) {
        this.sHuobiWebSocketClient = sHuobiWebSocketClient;
    }
}
