package com.github.stiangao.valve.autoconfigure.filter;

import com.github.stiangao.valve.autoconfigure.BootLimiter;
import com.github.stiangao.valve.core.LimiterConfig;
import com.github.stiangao.valve.local.LocalLimitRecorder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ttgg on 2017/8/5.
 */
public class LimiterFilter implements Filter {

    BootLimiter bootLimiter;
    LocalLimitRecorder recorder;

    public LimiterFilter(LimiterConfig config) {
        recorder = new LocalLimitRecorder();
        bootLimiter = new BootLimiter(config, recorder);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    //none
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String host = request.getRemoteHost();

        HttpServletRequest req = (HttpServletRequest) request;
        String sid = req.getSession().getId();
        String target = req.getRequestURI();

        if (!bootLimiter.visit(host, sid, target)) {
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.sendError(403);
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    //none
    }
}
