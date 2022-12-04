package org.example.base;

import org.springframework.stereotype.Component;

import java.util.List;

import static org.example.base.Constants.TOKENS;

@Component
public abstract class SyncMarketDataTemplate {

    public final void syncMarket() {
        // 同步 trade pair
        List<String> tokens = getToken();
        // 将交易对映射成各个交易所能识别的交易对
        List<String> exchangeTradePairs = mappingTradePairs4Exchange(tokens);
        // create streams
        List<String> streams = createStreams(exchangeTradePairs);
        // sub streams
        sub(streams);
    }

    public List<String> getToken() {
        return TOKENS;
    }

    public abstract List<String> mappingTradePairs4Exchange(List<String> tokens);

    public abstract List<String> createStreams(List<String> tradePairs);

    public abstract void sub(List<String> streams);
}
