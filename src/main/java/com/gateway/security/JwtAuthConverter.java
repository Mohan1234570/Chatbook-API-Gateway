package com.gateway.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

public class JwtAuthConverter
        implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {

        Collection<String> roles = extractRoles(jwt);

        Collection<? extends GrantedAuthority> authorities =
                roles.stream()
                        .map(r -> "ROLE_" + r)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        System.out.println("RESOLVED ROLES = " + roles);
        System.out.println("AUTHORITIES   = " + authorities);

        return Mono.just(new JwtAuthenticationToken(jwt, authorities));
    }

    private Collection<String> extractRoles(Jwt jwt) {

        // 1️⃣ roles
        List<String> roles = jwt.getClaimAsStringList("roles");
        if (roles != null) return roles;

        // 2️⃣ role
        String role = jwt.getClaimAsString("role");
        if (role != null) return List.of(role);

        // 3️⃣ scope
        String scope = jwt.getClaimAsString("scope");
        if (scope != null) return Arrays.asList(scope.split(" "));

        // 4️⃣ Keycloak realm_access.roles
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.get("roles") instanceof List<?> list) {
            return list.stream().map(String::valueOf).toList();
        }

        return List.of();
    }
}
