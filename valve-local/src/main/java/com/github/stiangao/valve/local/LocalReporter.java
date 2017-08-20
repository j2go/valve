package com.github.stiangao.valve.local;

import com.github.sitangao.valve.core.LimiterType;
import io.github.biezhi.ome.OhMyEmail;
import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
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
@Slf4j
public class LocalReporter implements Runnable {

    private int limitTimes;
    private Map<String, Integer> notPassMap = new HashMap<>();
    private Set<String> tempRecordSet = new HashSet<>();
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private String sender;
    private String receiver;

    public LocalReporter(ReporterConfig config) {
        limitTimes = config.getNotifyTimes();
        int period = config.getPeriodMinutes();
        executorService.scheduleAtFixedRate(this, period, period, TimeUnit.MINUTES);

        sender = config.getSender();
        receiver = config.getReceiver();

        OhMyEmail.config(OhMyEmail.SMTP_163(true), config.getSender(), config.getSenderPassword());
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
                report(k);
                needClearKeys.add(k);
            }
        });
        needClearKeys.forEach(e -> notPassMap.remove(e));
    }

    private void report(String key) {
        try {
            OhMyEmail.subject("notify").from(sender).to(receiver).html("<h1>" + key + "</h1>").send();
        } catch (MessagingException e) {
            log.error("LocalReporter send email error.", e);
        }
    }
}
