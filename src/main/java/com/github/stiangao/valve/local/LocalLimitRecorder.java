package com.github.stiangao.valve.local;

import com.github.stiangao.valve.core.LimitRecorder;
import com.github.stiangao.valve.core.Limiter;
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
    public Map<String, Long> getPassRecord(LimiterType type) {
        return null;
    }

    @Override
    public Map<String, Long> getRefuseRecord(LimiterType type) {
        return null;
    }


    private String getKey(LimiterType type, String key) {
        return type.toString() + "#" + key;
    }
}
