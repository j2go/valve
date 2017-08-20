package com.github.sitangao.valve.core;

/**
 * Created by shitiangao on 2017/8/7.
 */
public interface LimitRecorder {

    void record(LimiterType type, String key, boolean pass);

}
