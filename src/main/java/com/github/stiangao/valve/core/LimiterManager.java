package com.github.stiangao.valve.core;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ttgg on 2017/8/4.
 */
public class LimiterManager implements Limiter {

    private LimiterConfig config;
    private ConcurrentHashMap<String, RateLimiter> limiterMap = new ConcurrentHashMap<>();

    private RateLimiter globalRateLimiter;

    public LimiterManager(LimiterConfig config) {
        this.config = config;
        globalRateLimiter = RateLimiter.create(config.getGlobalLimitQps());
    }

    public boolean pass() {
        if (!config.enableLimit()) {
            return true;
        }
        return globalRateLimiter.tryAcquire();
    }

    public boolean pass(LimiterType type, String key) {
        if (!config.enableLimit(type)) {
            return true;
        }
        String mapKey = genKey(type, key);
        RateLimiter rateLimiter = limiterMap.get(mapKey);
        if (rateLimiter == null) {
            limiterMap.put(mapKey, genLimiter(type, key));
            return true;
        }
        return rateLimiter.tryAcquire();
    }

    private RateLimiter genLimiter(LimiterType type, String key) {

        return RateLimiter.create(config.getQps(type, key));
    }

    private String genKey(LimiterType type, String key) {

        return type.toString() + "#" + key;
    }


    @Override
    public boolean visit(String ip, String cid, String uri) {
        if (!pass()) {
            return false;
        }
        if (!pass(LimiterType.Address, ip)) {
            return false;
        }
        if (!pass(LimiterType.Client, cid)) {
            return false;
        }
        if (!pass(LimiterType.Target, uri)) {
            return false;
        }
        return true;
    }
}
