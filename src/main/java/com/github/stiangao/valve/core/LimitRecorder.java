package com.github.stiangao.valve.core;

import java.util.Map;

/**
 * Created by shitiangao on 2017/8/7.
 */
public interface LimitRecorder {

    void record(LimiterType type, String key, boolean pass);

    long getRecordNum(LimiterType type, String key, boolean pass);

    Map<LimiterType, Map<String,Long>> getAllPassRecord();

    Map<LimiterType, Map<String,Long>> getAllRefuseRecord();

}
