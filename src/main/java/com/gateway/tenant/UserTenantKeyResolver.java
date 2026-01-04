package com.gateway.tenant;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Primary
public class UserTenantKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(org.springframework.web.server.ServerWebExchange exchange) {
        String tenantId = exchange.getRequest()
                .getHeaders()
                .getFirst("X-Tenant-Id");

        return Mono.justOrEmpty(tenantId)
                .switchIfEmpty(Mono.just("anonymous"));
    }
}
