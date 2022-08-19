package com.skywalk.dynamic.routing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.skywalk.dynamic.routing.config.SkywalkDynamicRoutingConfig;
import com.skywalk.dynamic.routing.config.SkywalkDynamicRoutingConfigs;
import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({SkywalkDynamicRoutingConfig.class, SkywalkDynamicRoutingConfigs.class})
public @interface EnableSkywalkDynamicRouting {

}
