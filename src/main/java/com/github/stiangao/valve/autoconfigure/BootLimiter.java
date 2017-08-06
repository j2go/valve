package com.github.stiangao.valve.autoconfigure;

import com.github.stiangao.valve.core.Limiter;
import com.github.stiangao.valve.core.LimiterConfig;
import com.github.stiangao.valve.core.LimiterManager;

/**
 * Created by ttgg on 2017/8/5.
 */
public class BootLimiter implements Limiter {

    LimiterManager limiterManager;

    public BootLimiter(LimiterConfig config) {
        limiterManager = new LimiterManager(config);
    }

    @Override
    public boolean visit(String ip, String cid, String uri) {
        return limiterManager.visit(ip, cid, uri);
    }
}
