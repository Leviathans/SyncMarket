package org.example.listener;

import lombok.extern.log4j.Log4j2;
import org.example.binance.SBinanceSyncMarketDataTemplate;
import org.example.binance.UBinanceSyncMarketDataTemplate;
import org.example.huobi.SHuobiSyncMarketDataTemplate;
import org.example.huobi.UHuobiSyncMarketDataTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 处理重新订阅事件
 */
@Component
@Log4j2
public class ReSyncData {

    private UBinanceSyncMarketDataTemplate ubinanceSyncMarketDataTemplate;
    private UHuobiSyncMarketDataTemplate uhuobiSyncMarketDataTemplate;
    private SBinanceSyncMarketDataTemplate sbinanceSyncMarketDataTemplate;
    private SHuobiSyncMarketDataTemplate shuobiSyncMarketDataTemplate;

    @EventListener
    public void onApplicationEvent(ReSubEvent reSubEvent) {
        log.info("reconnct websocket,channel is {}", reSubEvent.getFuture());
        Future future = reSubEvent.getFuture();
        switch (future) {
            case UHUOBI:
                uhuobiSyncMarketDataTemplate.syncMarket();
            case SHUOBI:
                shuobiSyncMarketDataTemplate.syncMarket();
            case UBINANCE:
                ubinanceSyncMarketDataTemplate.syncMarket();
            case SBINANCE:
                sbinanceSyncMarketDataTemplate.syncMarket();
        }
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
