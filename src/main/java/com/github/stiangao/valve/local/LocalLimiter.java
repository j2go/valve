package com.github.stiangao.valve.local;

import com.github.stiangao.valve.core.Limiter;
import com.github.stiangao.valve.core.LimiterManager;
import com.github.stiangao.valve.core.LimiterType;

/**
 * Created by ttgg on 2017/8/4.
 */
public class LocalLimiter implements Limiter {

    LocalLimiterConfig localLimiterConfig;
    LimiterManager limiterManager;

    public LocalLimiter() {
        localLimiterConfig = new LocalLimiterConfig();
        localLimiterConfig.load();
        limiterManager = new LimiterManager(localLimiterConfig);
    }

    @Override
    public boolean visit(String ip, String cid, String uri) {

        return limiterManager.visit(ip, cid, uri);
    }

}
