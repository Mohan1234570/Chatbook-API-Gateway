package com.gateway.cache;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ResponseCacheFilter implements GlobalFilter, Ordered {

    private final ReactiveStringRedisTemplate redis;

    public ResponseCacheFilter(ReactiveStringRedisTemplate redis) {
        this.redis = redis;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        if (exchange.getRequest().getMethod() != HttpMethod.GET) {
            return chain.filter(exchange);
        }

        String key = "cache:" + exchange.getRequest().getURI();

        return redis.opsForValue().get(key)
                .flatMap(cached -> {

                    exchange.getResponse().beforeCommit(() -> {
                        exchange.getResponse()
                                .getHeaders()
                                .set("X-Cache", "HIT");
                        return Mono.empty();
                    });

                    return exchange.getResponse().writeWith(
                            Mono.just(
                                    exchange.getResponse()
                                            .bufferFactory()
                                            .wrap(cached.getBytes())
                            )
                    );
                })
                .switchIfEmpty(chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        return -2; // 🔥 FIX
    }
}
