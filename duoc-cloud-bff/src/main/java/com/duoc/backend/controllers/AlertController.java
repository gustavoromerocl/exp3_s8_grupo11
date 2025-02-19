package com.duoc.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {
    

    @Autowired
    private WebClient.Builder webClientBuilder;

    private String BASE_URL = "http://kafka-consumer:8085/api/alerts";

    @GetMapping
    public Mono<ResponseEntity<String>> findAll() {
        return webClientBuilder.build()
                .get()
                .uri(BASE_URL)
                .retrieve()
                .toEntity(String.class);
    }
}
