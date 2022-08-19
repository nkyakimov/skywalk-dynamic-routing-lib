package com.skywalk.dynamic.routing.config;

import javax.annotation.PostConstruct;

import com.skywalk.dynamic.routing.filter.SkywalkDynamicRoutingFilter;
import com.skywalk.dynamic.routing.supplier.SkywalkDynamicRoutingServiceInstanceSupplier;
import feign.RequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientSpecification;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
@ConditionalOnProperty(value = "skywalk.dynamic-routing.enabled", havingValue = "true", matchIfMissing = true)
public class SkywalkDynamicRoutingConfig {

    private final SkywalkDynamicRoutingConfigs skywalkDynamicRoutingConfigs;

    Logger log = LoggerFactory.getLogger(SkywalkDynamicRoutingConfig.class);

    public SkywalkDynamicRoutingConfig(SkywalkDynamicRoutingConfigs skywalkDynamicRoutingConfigs) {
        this.skywalkDynamicRoutingConfigs = skywalkDynamicRoutingConfigs;
    }

    @PostConstruct
    public void init() {
        log.info("Dynamic Routing header detected: {}", skywalkDynamicRoutingConfigs.getHeader());
        if (StringUtils.hasText(skywalkDynamicRoutingConfigs.getValue())) {
            log.info("Registered for dynamic routing with value: {}", skywalkDynamicRoutingConfigs.getValue());
        }
    }

    @Bean
    public SkywalkDynamicRoutingFilter skywalkDynamicRoutingFilter() {
        return new SkywalkDynamicRoutingFilter(skywalkDynamicRoutingConfigs);
    }

    @Bean
    public RequestInterceptor skywalkDynamicRoutingRequestInterceptor() {
        return template -> {
            String header = MDC.get(skywalkDynamicRoutingConfigs.getHeader());

            if (StringUtils.hasText(header)) {
                template.header(skywalkDynamicRoutingConfigs.getHeader(), header);
            }
        };
    }

    @Bean
    public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(
            ConfigurableApplicationContext context) {
        return ServiceInstanceListSupplier.builder()
                .withBase(
                        ServiceInstanceListSupplier.builder()
                                .withBlockingDiscoveryClient()
                                .withCaching()
                                .build(context)
                )
                .with((a, delegate) ->
                        new SkywalkDynamicRoutingServiceInstanceSupplier(delegate, skywalkDynamicRoutingConfigs))
                .build(context);
    }

    @Bean
    public LoadBalancerClientSpecification loadBalancerClientSpecification() {
        return new LoadBalancerClientSpecification("default.customLoadBalancerConfiguration",
                new Class[]{SkywalkDynamicRoutingConfig.class});
    }

}
