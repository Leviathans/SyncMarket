package org.example.config;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import okio.ByteString;
import org.example.base.Constants;
import org.example.huobi.protocol.Detail;
import org.example.huobi.protocol.HuobiResponseBody;
import org.example.utils.SpringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;


@Configuration
@AutoConfigureOrder(3)
@Log4j2
public class WebSocketConfiguration {

    private OkHttpClient okHttpClient;
    private SpringUtils springUtils;
    private RedisTemplate<String, String> redisTemplate;
    private ApplicationEventPublisher applicationEventPublisher;

    @Value("${wss.u_huobi_url}")
    private String uHuobiUrl;
    @Value("${wss.s_huobi_url}")
    private String sHuobiUrl;
    @Value("${wss.u_binance_url}")
    private String uBinanceUrl;
    @Value("${wss.s_binance_url}")
    private String sBinanceUrl;


    @Bean(name = "uHuobiWebSocketClient")
    public WebSocket huobiWebSocketClient() {
        Request request = new Request.Builder().url(uHuobiUrl).build();
        return okHttpClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                super.onClosed(webSocket, code, reason);
                log.warn("huobi U websocket is closed");
                springUtils.refreshBean("uHuobiWebSocketClient");
//                applicationEventPublisher.publishEvent(new ReSubEvent(this, Future.UHUOBI));
            }

            @Override
            public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                log.info("huobi U websocket is closing");
            }

            @Override
            public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
                log.error("huobi U websocket is failure,exception:{},reason:{}", t.getMessage(), response);
                super.onFailure(webSocket, t, response);
                springUtils.refreshBean("uHuobiWebSocketClient");
                // applicationEventPublisher.publishEvent(new ReSubEvent(this, Future.SBINANCE));
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
                String jsonBody = ZipUtil.unGzip(bytes.toByteArray(), CharsetUtil.CHARSET_UTF_8.toString());
                JSONObject jsonObject = JSONUtil.parseObj(jsonBody);
                if (jsonObject.containsKey("ping")) {
                    String s = String.format("{\"pong\": %d}", (Long) jsonObject.get("ping"));
                    //log.info(s);
                    webSocket.send(s);
                } else if (jsonObject.containsKey("tick")) {
                    HuobiResponseBody responseBody = JSONUtil.toBean(jsonBody, HuobiResponseBody.class);
                    String tradePair = responseBody.getCh().split("\\.")[1].toLowerCase();
                    List<Detail> detailList = responseBody.getTick().getData();
                    List<Detail> sortedList = detailList.stream().sorted(Comparator.comparingLong(Detail::getTs).reversed()).toList();
                    if (!sortedList.isEmpty()) {
                        Detail detail = sortedList.get(0);
                        BigDecimal currentPrice = detail.getPrice();
                        redisTemplate.opsForHash().put(Constants.REDISKEY_HUOBI_U, tradePair, currentPrice.toString());
                    }
                } else if (jsonObject.containsKey("op") && (jsonObject.containsValue("error") || jsonObject.containsValue("close"))) {
                    springUtils.refreshBean("uHuobiWebSocketClient");
                    // applicationEventPublisher.publishEvent(new ReSubEvent(this, Future.UHUOBI));
                } else {
                    log.info(jsonBody);
                }
            }

            @Override
            public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
                log.info("huobi U websocket is opened");
            }
        });
    }

    @Bean(name = "sHuobiWebSocketClient")
    public WebSocket sHuobiWebSocketClient() {
        Request request = new Request.Builder().url(sHuobiUrl).build();
        return okHttpClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                super.onClosed(webSocket, code, reason);
                log.warn("huobi swap websocket is closed");
                springUtils.refreshBean("sHuobiWebSocketClient");
                // applicationEventPublisher.publishEvent(new ReSubEvent(this, Future.SHUOBI));
            }

            @Override
            public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                log.info("huobi swap websocket is closing");
            }

            @Override
            public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
                log.error("huobi swap websocket is failure,exception:{},reason:{}", t.getMessage(), response);
                super.onFailure(webSocket, t, response);
                springUtils.refreshBean("sHuobiWebSocketClient");
                //applicationEventPublisher.publishEvent(new ReSubEvent(this, Future.SBINANCE));
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
                String jsonBody = ZipUtil.unGzip(bytes.toByteArray(), CharsetUtil.CHARSET_UTF_8.toString());
                JSONObject jsonObject = JSONUtil.parseObj(jsonBody);
                if (jsonObject.containsKey("ping")) {
                    String s = String.format("{\"pong\": %d}", (Long) jsonObject.get("ping"));
                    //log.info(s);
                    webSocket.send(s);
                } else if (jsonObject.containsKey("tick")) {
                    HuobiResponseBody responseBody = JSONUtil.toBean(jsonBody, HuobiResponseBody.class);
                    String tradePair = responseBody.getCh().split("\\.")[1].toLowerCase();
                    List<Detail> detailList = responseBody.getTick().getData();
                    List<Detail> sortedList = detailList.stream().sorted(Comparator.comparingLong(Detail::getTs)).toList();
                    if (!sortedList.isEmpty()) {
                        Detail detail = sortedList.get(0);
                        BigDecimal currentPrice = detail.getPrice();
                        redisTemplate.opsForHash().put(Constants.REDISKEY_HUOBI_S, tradePair.replace("-", ""), currentPrice.toString());
                    }
                } else if (jsonObject.containsKey("op") && (jsonObject.containsValue("error") || jsonObject.containsValue("close"))) {
                    springUtils.refreshBean("sHuobiWebSocketClient");
                    //applicationEventPublisher.publishEvent(new ReSubEvent(this, Future.SHUOBI));
                } else {
                    log.info(jsonBody);
                }
            }

            @Override
            public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
                log.info("huobi swap websocket is opened");
            }
        });
    }

    @Bean(name = "uBinanceWebSocketClient")
    public WebSocket binanceWebSocketClient() {
        var request = new Request.Builder().url(uBinanceUrl).build();
        return okHttpClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                super.onClosed(webSocket, code, reason);
                log.warn("binance U websocket is closed");
                springUtils.refreshBean("uBinanceWebSocketClient");
                //applicationEventPublisher.publishEvent(new ReSubEvent(this, Future.UBINANCE));
            }

            @Override
            public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                log.info("binance U websocket is closing");
            }

            @Override
            public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
                log.error("binance U websocket is failure,exception:{},reason:{}", t.getMessage(), response);
                super.onFailure(webSocket, t, response);
                springUtils.refreshBean("uBinanceWebSocketClient");
                //applicationEventPublisher.publishEvent(new ReSubEvent(this, Future.SBINANCE));
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
                //{"e":"aggTrade","E":1645758648956,"s":"ETHUSDT","a":625711911,"p":"2630.41000000","q":"1.00000000","f":766917917,"l":766917917,"T":1645758648956,"m":false,"M":true}
                if (text.contains("aggTrade")) {
                    JSONObject jsonObject = JSONUtil.parseObj(text);
                    String tradePair = jsonObject.get("s", String.class);
                    String price = jsonObject.get("p", String.class);
                    try {
                        redisTemplate.opsForHash().put(Constants.REDISKEY_BINANCE_U, tradePair.toLowerCase(), price);
                    } catch (Exception e) {
                        log.error("binance U error,reason:", e);
                    }
                }
            }

            @Override
            public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
                log.info("binance U websocket is opened");
            }
        });
    }

    @Bean(name = "sBinanceWebSocketClient")
    public WebSocket sBinanceWebSocketClient() {
        var request = new Request.Builder().url(sBinanceUrl).build();
        return okHttpClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                super.onClosed(webSocket, code, reason);
                log.warn("binance swap websocket is closed,reaseon is {}", reason);
                springUtils.refreshBean("sBinanceWebSocketClient");
                //  applicationEventPublisher.publishEvent(new ReSubEvent(this, Future.SBINANCE));
            }

            @Override
            public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                log.info("binance swap websocket is closing");
            }

            @Override
            public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
                log.error("binance swap websocket is failure,exception:{},reason:{}", t.getMessage(), response);
                super.onFailure(webSocket, t, response);
                springUtils.refreshBean("sBinanceWebSocketClient");
                //  applicationEventPublisher.publishEvent(new ReSubEvent(this, Future.SBINANCE));
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
                if (log.isDebugEnabled()) {
                    log.debug("binance swap message :{}", text);
                }
                if (text.contains("aggTrade")) {
                    JSONObject jsonObject = JSONUtil.parseObj(text);
                    String tradePair = jsonObject.get("s", String.class);
                    String price = jsonObject.get("p", String.class);
                    try {
                        redisTemplate.opsForHash().put(Constants.REDISKEY_BINANCE_S, tradePair.toLowerCase().replace("_perp", ""), price);
                    } catch (Exception e) {
                        log.error("binance swap error,reason:", e);
                    }
                }
            }

            @Override
            public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
                log.info("binance swap websocket is opened");
            }
        });
    }

    @Autowired
    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    @Autowired
    public void setSpringUtils(SpringUtils springUtils) {
        this.springUtils = springUtils;
    }

    @Autowired
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
