package com.skywalk.dynamic.routing.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.skywalk.dynamic.routing.config.SkywalkDynamicRoutingConfigs;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class SkywalkDynamicRoutingFilter extends OncePerRequestFilter {

    private final SkywalkDynamicRoutingConfigs skywalkDynamicRoutingConfigs;

    public SkywalkDynamicRoutingFilter(SkywalkDynamicRoutingConfigs skywalkDynamicRoutingConfigs) {
        this.skywalkDynamicRoutingConfigs = skywalkDynamicRoutingConfigs;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String header = request.getHeader(skywalkDynamicRoutingConfigs.getHeader());
            if (StringUtils.hasText(header)) {
                MDC.put(skywalkDynamicRoutingConfigs.getHeader(), header);
            }
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(skywalkDynamicRoutingConfigs.getHeader());
        }
    }

}
