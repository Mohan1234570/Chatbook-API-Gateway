package com.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Component
public class RoleCheckFilter
        extends AbstractGatewayFilterFactory<RoleCheckFilter.Config> {

    public RoleCheckFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String rolesHeader = exchange.getRequest()
                    .getHeaders()
                    .getFirst("X-User-Roles");

            // ❌ No JWT / no roles → FORBIDDEN
            if (!StringUtils.hasText(rolesHeader)) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            List<String> userRoles = Arrays.stream(rolesHeader.split(","))
                    .map(String::trim)
                    .toList();

            if (!userRoles.contains(config.getRequiredRole())) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {
        private String requiredRole;

        public String getRequiredRole() {
            return requiredRole;
        }

        public void setRequiredRole(String requiredRole) {
            this.requiredRole = requiredRole;
        }
    }
}
