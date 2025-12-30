package com.gateway.custom;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class RequestContextFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        Long userId = getLongHeader(request, "X-User-Id");
        Long tenantId = getLongHeader(request, "X-Tenant-Id");
        List<String> roles = getListHeader(request, "X-User-Roles");

        if (userId != null) {
            exchange.getAttributes().put("userId", userId);
        }

        if (tenantId != null) {
            exchange.getAttributes().put("tenantId", tenantId);
        }

        // roles is never null → safe
        exchange.getAttributes().put("roles", roles);

        return chain.filter(exchange);
    }


    /**
     * Safely parse Long header
     * - null if header missing
     * - null if empty
     * - null if invalid number
     */
    private Long getLongHeader(ServerHttpRequest request, String name) {
        String value = request.getHeaders().getFirst(name);

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException ex) {
            // Do NOT crash gateway for bad client input
            return null;
        }
    }

    /**
     * Safely parse comma-separated list header
     */
    private List<String> getListHeader(ServerHttpRequest request, String name) {
        String value = request.getHeaders().getFirst(name);

        if (value == null || value.trim().isEmpty()) {
            return List.of();
        }

        return List.of(value.split(","));
    }
}
