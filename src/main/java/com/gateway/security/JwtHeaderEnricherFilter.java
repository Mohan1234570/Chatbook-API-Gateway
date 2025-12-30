package com.gateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtHeaderEnricherFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication())
                .flatMap(auth -> enrichExchange(exchange, auth))
                .defaultIfEmpty(exchange)
                .flatMap(chain::filter);
    }

    private Mono<ServerWebExchange> enrichExchange(
            ServerWebExchange exchange,
            Authentication authentication
    ) {

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            return Mono.just(exchange);
        }

        String userId = resolveUserId(jwt);
        String tenantId = resolveTenantId(jwt);
        String roles = resolveRoles(jwt);

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(request -> request
                        .header("X-User-Id", userId)
                        .header("X-Tenant-Id", tenantId)
                        .header("X-User-Roles", roles)
                )
                .build();

        return Mono.just(mutatedExchange);
    }

    private String resolveUserId(Jwt jwt) {
        String userId = jwt.getClaimAsString("userId");
        return (userId == null || userId.isBlank())
                ? jwt.getSubject()
                : userId;
    }

    private String resolveTenantId(Jwt jwt) {
        String tenantId = jwt.getClaimAsString("tenantId");
        return tenantId == null ? "" : tenantId;
    }

    private String resolveRoles(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        return roles == null ? "" : String.join(",", roles);
    }

    /**
     * IMPORTANT:
     * Run AFTER:
     *  - Security filters
     *  - RateLimiter
     *  - Routing decision
     */
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
