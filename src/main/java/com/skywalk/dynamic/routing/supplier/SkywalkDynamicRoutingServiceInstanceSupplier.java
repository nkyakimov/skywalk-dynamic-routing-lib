package com.skywalk.dynamic.routing.supplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.skywalk.dynamic.routing.config.SkywalkDynamicRoutingConfigs;
import org.slf4j.MDC;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.loadbalancer.core.DelegatingServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

public class SkywalkDynamicRoutingServiceInstanceSupplier extends DelegatingServiceInstanceListSupplier {

    private final SkywalkDynamicRoutingConfigs skywalkDynamicRoutingConfigs;

    public SkywalkDynamicRoutingServiceInstanceSupplier(ServiceInstanceListSupplier delegate,
            SkywalkDynamicRoutingConfigs skywalkDynamicRoutingConfigs) {
        super(delegate);
        this.skywalkDynamicRoutingConfigs = skywalkDynamicRoutingConfigs;
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return delegate.get();
    }

    @Override
    public Flux<List<ServiceInstance>> get(Request request) {
        return delegate.get(request).map(instances -> filteredByHint(instances, getHintFromMDC()));
    }

    private String getHintFromMDC() {
        return MDC.get(skywalkDynamicRoutingConfigs.getHeader());
    }

    private List<ServiceInstance> filteredByHint(List<ServiceInstance> instances, String hint) {
        if (!StringUtils.hasText(hint)) {
            return filterServices(instances);
        }

        List<ServiceInstance> filteredInstances = new ArrayList<>();
        for (ServiceInstance instance : instances) {
            if (instance.getMetadata().getOrDefault(skywalkDynamicRoutingConfigs.getHeader(), "").equals(hint)) {
                filteredInstances.add(instance);
            }
        }

        return filteredInstances.size() > 0 ? filteredInstances : filterServices(instances);
    }

    private List<ServiceInstance> filterServices(List<ServiceInstance> instances) {
        if (!skywalkDynamicRoutingConfigs.isExcludeRegistered()) {
            return instances;
        }

        return instances.stream()
                .filter(Predicate.not(this::isServiceRegisteredForDynamicRouting))
                .collect(Collectors.toList());
    }

    private boolean isServiceRegisteredForDynamicRouting(ServiceInstance serviceInstance) {
        return StringUtils.hasText(serviceInstance.getMetadata().get(skywalkDynamicRoutingConfigs.getHeader()));
    }

}
