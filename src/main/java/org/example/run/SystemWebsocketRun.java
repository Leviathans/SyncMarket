package org.example.run;

import org.example.binance.SBinanceSyncMarketDataTemplate;
import org.example.binance.UBinanceSyncMarketDataTemplate;
import org.example.huobi.SHuobiSyncMarketDataTemplate;
import org.example.huobi.UHuobiSyncMarketDataTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class SystemWebsocketRun implements ApplicationRunner {

    private UBinanceSyncMarketDataTemplate ubinanceSyncMarketDataTemplate;
    private UHuobiSyncMarketDataTemplate uhuobiSyncMarketDataTemplate;
    private SBinanceSyncMarketDataTemplate sbinanceSyncMarketDataTemplate;
    private SHuobiSyncMarketDataTemplate shuobiSyncMarketDataTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ubinanceSyncMarketDataTemplate.syncMarket();
        uhuobiSyncMarketDataTemplate.syncMarket();
        sbinanceSyncMarketDataTemplate.syncMarket();
        shuobiSyncMarketDataTemplate.syncMarket();
    }

    @Autowired
    public void setUbinanceSyncMarketDataTemplate(UBinanceSyncMarketDataTemplate ubinanceSyncMarketDataTemplate) {
        this.ubinanceSyncMarketDataTemplate = ubinanceSyncMarketDataTemplate;
    }

    @Autowired
    public void setUhuobiSyncMarketDataTemplate(UHuobiSyncMarketDataTemplate uhuobiSyncMarketDataTemplate) {
        this.uhuobiSyncMarketDataTemplate = uhuobiSyncMarketDataTemplate;
    }

    @Autowired
    public void setSbinanceSyncMarketDataTemplate(SBinanceSyncMarketDataTemplate sbinanceSyncMarketDataTemplate) {
        this.sbinanceSyncMarketDataTemplate = sbinanceSyncMarketDataTemplate;
    }

    @Autowired
    public void setShuobiSyncMarketDataTemplate(SHuobiSyncMarketDataTemplate shuobiSyncMarketDataTemplate) {
        this.shuobiSyncMarketDataTemplate = shuobiSyncMarketDataTemplate;
    }
}
