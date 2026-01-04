package com.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public JwtAuthConverter jwtAuthConverter() {
        return new JwtAuthConverter();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                .authorizeExchange(auth -> auth

                        // Public endpoints (NO JWT)
                        .pathMatchers(
                                "/api/auth/**",
                                "/actuator/**"
                        ).permitAll()

                        .pathMatchers("/api/chatbook/**").hasRole("USER")
                        .pathMatchers("/api/notifs/**").hasRole("USER")

                        // WebSocket handshake
                        .pathMatchers("/ws/**").authenticated()

                        // Everything else
                        .anyExchange().authenticated()
                )

                // JWT validation at Gateway
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthConverter())
                        )
                )

                .build();
    }
}
