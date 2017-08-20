package com.github.stiangao.valve.local;

import com.github.sitangao.valve.core.LimitRecorder;
import com.github.sitangao.valve.core.LimiterType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by shitiangao on 2017/8/7.
 */
public class LocalLimitRecorder implements LimitRecorder {

    private Map<String, AtomicLong> countMap = new ConcurrentHashMap<>();

    private LocalReporter localReporter;

    public LocalLimitRecorder(LocalReporter localReporter) {
        this.localReporter = localReporter;
    }

    @Override
    public void record(LimiterType type, String key, boolean pass) {
        String mapKey = getKey(type, key, pass);

        AtomicLong counter = countMap.get(mapKey);
        if (counter == null) {
            countMap.put(mapKey, new AtomicLong(1));
        } else {
            counter.incrementAndGet();
        }
        if (!pass && localReporter != null) {
            localReporter.commit(type, key);
        }
    }

    public long getRecordNum(LimiterType type, String key, boolean pass) {
        String mapKey = getKey(type, key, pass);
        return countMap.get(mapKey) == null ? 0 : countMap.get(key).longValue();
    }

    public Map<String, Long> getRecordInfo() {
        Map<String, Long> map = new LinkedHashMap<>();
        countMap.entrySet().forEach(e -> map.put(e.getKey(), e.getValue().longValue()));
        return map;
    }

    public void clear() {
        countMap.clear();
    }

    private String getKey(LimiterType type, String key, boolean pass) {
        return type.toString() + "@" + key + "#" + pass;
    }

}
