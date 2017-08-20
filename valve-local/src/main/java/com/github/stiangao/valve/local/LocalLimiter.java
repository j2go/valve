package com.github.stiangao.valve.local;

import com.github.sitangao.valve.core.LimitRecorder;
import com.github.sitangao.valve.core.Limiter;
import com.github.sitangao.valve.core.LimiterManager;

/**
 * Created by ttgg on 2017/8/4.
 */
public class LocalLimiter implements Limiter {

    LocalLimiterConfig localLimiterConfig;
    LimiterManager<LocalLimitRecorder> limiterManager;

    public LocalLimiter() {
        localLimiterConfig = new LocalLimiterConfig();
        localLimiterConfig.load();

        LocalLimitRecorder recorder = new LocalLimitRecorder(null);
        limiterManager = new LimiterManager<>(localLimiterConfig, recorder);
    }

    @Override
    public boolean visit(String ip, String cid, String uri) {

        return limiterManager.visit(ip, cid, uri);
    }

}
