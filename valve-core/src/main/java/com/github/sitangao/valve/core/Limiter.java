package com.github.sitangao.valve.core;

/**
 * Created by ttgg on 2017/8/4.
 */
public interface Limiter {

    boolean visit(String ip, String cid, String uri);

}
