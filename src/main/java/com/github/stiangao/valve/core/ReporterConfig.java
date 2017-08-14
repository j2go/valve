package com.github.stiangao.valve.core;

/**
 * Created by ttgg on 2017/8/15.
 */
public interface ReporterConfig {

    int getNotifyTimes();

    int getPeriodMinutes();

    String getSender();

    String getSenderPassword();

    String getReceiver();
}
