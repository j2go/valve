package com.github.stiangao.valve.local;

import com.github.stiangao.valve.core.LimitRecorder;
import com.github.stiangao.valve.core.LimiterType;

import java.util.Map;

/**
 * Created by shitiangao on 2017/8/7.
 */
public class LocalLimitRecorder implements LimitRecorder {

    @Override
    public void record(LimiterType type, String key, boolean pass) {

    }

    @Override
    public long getRecordNum(LimiterType type, String key, boolean pass) {
        return 0;
    }

    @Override
    public Map<LimiterType, Map<String, Long>> getAllPassRecord() {
        return null;
    }

    @Override
    public Map<LimiterType, Map<String, Long>> getAllRefuseRecord() {
        return null;
    }
}
