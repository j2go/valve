package com.github.stiangao.valve.local;

import com.github.stiangao.valve.core.LimiterType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by ttgg on 2017/8/13.
 */
public class LocalAlarm implements Runnable {

    private int limitTimes;
    private Map<String, Integer> notPassMap = new HashMap<>();
    private Set<String> tempRecordSet = new HashSet<>();
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();


    public LocalAlarm() {
        this(5);
    }

    public LocalAlarm(int reportTimes) {
        this(reportTimes, 5);
    }

    public LocalAlarm(int limitTimes, int period) {
        this.limitTimes = limitTimes;
        executorService.scheduleAtFixedRate(this, period, period, TimeUnit.MINUTES);
    }

    public void commit(LimiterType type, String key) {
        tempRecordSet.add(type + "@" + key);
    }

    @Override
    public void run() {
        List<String> needHandleKeys = new ArrayList<>();
        tempRecordSet.forEach(e -> needHandleKeys.add(e));
        tempRecordSet.clear();

        needHandleKeys.forEach(e -> {
            if (notPassMap.get(e) == null) {
                notPassMap.put(e, 0);
            }
        });
        List<String> needClearKeys = new ArrayList<>();
        notPassMap.forEach((k, v) -> {
            if (needHandleKeys.contains(k)) {
                notPassMap.put(k, v + 1);
            } else {
                if (v > 1) {
                    notPassMap.put(k, v - 1);
                } else {
                    needClearKeys.add(k);
                }
            }
        });
        notPassMap.forEach((k, times) -> {
            if (times > limitTimes) {
                notify(k);
                needClearKeys.add(k);
            }
        });
        needClearKeys.forEach(e -> notPassMap.remove(e));
    }

    private void notify(String key) {

    }

    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 1);
        map.put("c", 1);
        map.put("d", 1);
        map.put("e", 1);

        map.forEach((k, v) -> {
            if (k == "a" || k == "c") {
                map.put(k, v + 1);
            } else {
                map.put(k, v - 1);
            }
        });
        map.remove("d");
        System.out.println(map);

    }
}
