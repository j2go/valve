package com.github.stiangao.valve.autoconfigure;

import com.github.stiangao.valve.autoconfigure.filter.LimiterFilter;
import com.github.stiangao.valve.core.LimiterConfig;
import com.github.stiangao.valve.core.LimiterType;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;
import java.util.Map;

/**
 * Created by ttgg on 2017/8/5.
 */
@Configuration
@EnableConfigurationProperties
public class AutoConfiguration {

    @Autowired
    ValveConfig valveConfig;

    @Bean
    public FilterRegistrationBean limiterFilterRegister() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new LimiterFilter(valveConfig));
        registrationBean.setName("valve");
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Configuration
    @ConfigurationProperties(prefix = "limit")
    @PropertySource("classpath:valve.yml")
    @Data
    public static class ValveConfig implements LimiterConfig {
        private boolean enable = false;
        private int qps = 99999;

        private Detail address;
        private Detail target;
        private Detail client;

        @Data
        public static class Detail {
            private boolean enable = false;
            private int qps = 9999;
            private List<Map<String, String>> list;

            public int getQps(String key) {
                for (Map<String, String> map : list) {
                    if (key.equals(map.get("key"))) {
                        return Integer.parseInt(map.get("qps"));
                    }
                }
                return qps;
            }
        }

        @Override
        public int getQps(LimiterType type, String key) {
            switch (type) {
                case ALL:
                    return qps;
                case ADDRESS:
                    return address.getQps(key);
                case TARGET:
                    return target.getQps(key);
                case CLIENT:
                    return client.getQps(key);
                default:
                    return 0;
            }
        }

        @Override
        public boolean enableLimit(LimiterType type) {
            switch (type) {
                case ALL:
                    return enable;
                case ADDRESS:
                    return address.isEnable();
                case TARGET:
                    return target.isEnable();
                case CLIENT:
                    return client.isEnable();
                default:
                    return false;
            }
        }
    }
}
