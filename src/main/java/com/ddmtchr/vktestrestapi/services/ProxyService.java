package com.ddmtchr.vktestrestapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ProxyService {
    private final WebClient webClient;

    @Autowired
    public ProxyService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://jsonplaceholder.typicode.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Cacheable("posts")
    public String fetchPosts() {
        return webClient.get().uri("/posts").retrieve().bodyToMono(String.class).block();
    }

    @Cacheable("users")
    public String fetchUsers() {
        return webClient.get().uri("/users").retrieve().bodyToMono(String.class).block();
    }

    @Cacheable("albums")
    public String fetchAlbums() {
        return webClient.get().uri("/albums").retrieve().bodyToMono(String.class).block();
    }

    @Scheduled(fixedRateString = "${caching.TTL}")
    @CacheEvict(allEntries = true, value = {"posts", "users", "albums"})
    public void emptyCache() {}
}
