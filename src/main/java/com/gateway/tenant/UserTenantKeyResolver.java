package com.gateway.tenant;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component("userTenantKeyResolver")
public class UserTenantKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        String tenantId =
                exchange.getRequest().getHeaders().getFirst("X-Tenant-Id");

        return Mono.just(tenantId == null ? "default" : tenantId);
    }
}
