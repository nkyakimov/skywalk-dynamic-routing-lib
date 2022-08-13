package com.skywalk.dynamic.routing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("skywalk.dynamic-routing")
public class SkywalkDynamicRoutingConfigs {

    private String header = "X-SKYWALK-DYNAMIC-ROUTING";
    private boolean excludeRegistered = true;
    private String value;

    public SkywalkDynamicRoutingConfigs() {
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public boolean isExcludeRegistered() {
        return excludeRegistered;
    }

    public void setExcludeRegistered(boolean excludeRegistered) {
        this.excludeRegistered = excludeRegistered;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
