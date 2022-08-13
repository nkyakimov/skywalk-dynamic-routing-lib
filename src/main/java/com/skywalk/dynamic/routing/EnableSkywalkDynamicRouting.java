package com.skywalk.dynamic.routing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.skywalk.dynamic.routing.config.SkywalkDynamicRoutingConfig;
import com.skywalk.dynamic.routing.config.SkywalkDynamicRoutingConfigs;
import com.skywalk.dynamic.routing.filter.SkywalkDynamicRoutingFilter;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableFeignClients
@EnableDiscoveryClient
@Import({SkywalkDynamicRoutingConfig.class, SkywalkDynamicRoutingConfigs.class, SkywalkDynamicRoutingFilter.class})
public @interface EnableSkywalkDynamicRouting {

}
