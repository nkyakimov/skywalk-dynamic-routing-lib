package com.skywalk.dynamic.routing.config;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.util.StringUtils;

public class SkywalkDynamicRoutingEventListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    Logger log = LoggerFactory.getLogger(SkywalkDynamicRoutingEventListener.class);

    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        String dynamicRoutingHeader = environment.getProperty("skywalk.dynamic-routing.header");
        String dynamicRoutingValue = environment.getProperty("skywalk.dynamic-routing.value");

        if (!StringUtils.hasText(dynamicRoutingValue)) {
            return;
        }

        Properties eurekaMetadataProp = new Properties();
        eurekaMetadataProp.put("eureka.instance.metadata-map." + dynamicRoutingHeader, dynamicRoutingValue);
        environment.getPropertySources().addFirst(new PropertiesPropertySource("skywalkProps", eurekaMetadataProp));

        log.info("Registered for dynamic routing: Header {}, Value {}", dynamicRoutingHeader, dynamicRoutingValue);
    }

}
