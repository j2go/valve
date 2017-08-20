package com.github.stiangao.valve.boot.autoconfigure;

import com.github.sitangao.valve.core.LimitRecorder;
import com.github.sitangao.valve.core.Limiter;
import com.github.sitangao.valve.core.LimiterConfig;
import com.github.sitangao.valve.core.LimiterManager;

/**
 * Created by ttgg on 2017/8/5.
 */
public class BootLimiter implements Limiter {

    LimiterManager limiterManager;

    public BootLimiter(LimiterConfig config, LimitRecorder recorder) {
        limiterManager = new LimiterManager(config, recorder);
    }

    @Override
    public boolean visit(String ip, String cid, String uri) {
        return limiterManager.visit(ip, cid, uri);
    }
}
