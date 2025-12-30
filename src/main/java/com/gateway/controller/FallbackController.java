package com.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/chatbook")
    public Mono<String> chatbookFallback() {
        return Mono.just("Chatbook service temporarily unavailable");
    }

    @GetMapping("/notification")
    public Mono<String> notificationFallback() {
        return Mono.just("Notification service temporarily unavailable");
    }
}

