package com.github.stiangao.valve.core;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ttgg on 2017/8/4.
 */
public class LimiterManager implements Limiter {

    private static final String KEY_ALL = "ALL";

    private LimiterConfig config;
    private LimitRecorder recorder;

    private ConcurrentHashMap<String, RateLimiter> limiterMap = new ConcurrentHashMap<>();

    public LimiterManager(LimiterConfig config, LimitRecorder recorder) {
        this.config = config;
        this.recorder = recorder;
        limiterMap.put(KEY_ALL, RateLimiter.create(config.getQps(LimiterType.ALL, null)));
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
        if (type.equals(LimiterType.ALL)) {
            return KEY_ALL;
        }
        return type.toString() + "#" + key;
    }


    @Override
    public boolean visit(String ip, String cid, String uri) {
        return pass(LimiterType.ALL, null) &&
                pass(LimiterType.ADDRESS, ip) &&
                pass(LimiterType.CLIENT, cid) &&
                pass(LimiterType.TARGET, uri);
    }

    public LimitRecorder getRecorder() {
        return recorder;
    }

}
