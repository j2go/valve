package com.github.stiangao.valve.core;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ttgg on 2017/8/4.
 */
public class LimiterManager implements Limiter {

    private final String KEY_ALL = "ALL";

    private LimiterConfig config;

    private LimitRecorder recorder;
    private ConcurrentHashMap<String, RateLimiter> limiterMap = new ConcurrentHashMap<>();

    public LimiterManager(LimiterConfig config, LimitRecorder recorder) {
        this.config = config;
        this.recorder = recorder;
        limiterMap.put(KEY_ALL, RateLimiter.create(config.getQps(LimiterType.All, null)));
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
        boolean pass = rateLimiter.tryAcquire();
        recorder.record(type, key, pass);
        return pass;
    }

    private RateLimiter genLimiter(LimiterType type, String key) {

        return RateLimiter.create(config.getQps(type, key));
    }

    private String genKey(LimiterType type, String key) {
        if (type.equals(LimiterType.All)) {
            return KEY_ALL;
        }
        return type.toString() + "#" + key;
    }


    @Override
    public boolean visit(String ip, String cid, String uri) {
        if (!pass(LimiterType.All, null)) {
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

    public LimitRecorder getRecorder() {
        return recorder;
    }

}
