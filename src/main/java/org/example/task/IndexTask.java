package org.example.task;

import cn.hutool.core.util.NumberUtil;
import com.google.common.base.Strings;
import lombok.extern.log4j.Log4j2;
import org.example.base.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static org.example.base.Constants.TOKENS;

@Component
@Log4j2
public class IndexTask {

    private RedisTemplate<String, String> redisTemplate;

    @Scheduled(fixedRate = 5000)
    public void health() {

        for (String token : TOKENS) {
            String hbS = (String) redisTemplate.opsForHash().get(Constants.REDISKEY_HUOBI_S, token + "usd");
            String binanceS = (String) redisTemplate.opsForHash().get(Constants.REDISKEY_BINANCE_S, token + "usd");
            String hbU = (String) redisTemplate.opsForHash().get(Constants.REDISKEY_HUOBI_U, token + "usdt");
            String binanceU = (String) redisTemplate.opsForHash().get(Constants.REDISKEY_BINANCE_U, token + "usdt");

            BigDecimal hbsPrice = Strings.isNullOrEmpty(hbS) ? BigDecimal.ZERO : new BigDecimal(hbS);
            BigDecimal binancesPrice = Strings.isNullOrEmpty(binanceS) ? BigDecimal.ZERO : new BigDecimal(binanceS);
            BigDecimal hbuPrice = Strings.isNullOrEmpty(hbU) ? BigDecimal.ZERO : new BigDecimal(hbU);
            BigDecimal binanceuPrice = Strings.isNullOrEmpty(binanceU) ? BigDecimal.ZERO : new BigDecimal(binanceU);

            if (NumberUtil.isGreater(hbsPrice, BigDecimal.ZERO) && NumberUtil.isGreater(binancesPrice, BigDecimal.ZERO)) {
                //TODO send msg to kafka and redis

            }
            if (NumberUtil.isGreater(hbuPrice, BigDecimal.ZERO) && NumberUtil.isGreater(binanceuPrice, BigDecimal.ZERO)) {
                //TODO send msg to kafka and redis

            }

            //result
        }
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

//    @Autowired
//    public void setRedisScript(DefaultRedisScript<List<Object>> redisScript) {
//        this.redisScript = redisScript;
//    }
}
