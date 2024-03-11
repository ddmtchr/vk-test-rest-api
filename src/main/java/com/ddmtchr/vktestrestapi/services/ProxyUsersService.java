package com.ddmtchr.vktestrestapi.services;

import com.ddmtchr.vktestrestapi.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ProxyUsersService {
    private final WebClient webClient;

    @Autowired
    public ProxyUsersService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://jsonplaceholder.typicode.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Cacheable("usersCollection")
    public List<UserDTO> fetchUsers() {
        return webClient.get().uri("/users").retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserDTO>>() {
                }).block();
    }

    @Cacheable(value = "users", key = "#id")
    public UserDTO fetchUserById(Long id) {
        return webClient.get().uri("/users/{id}", id).retrieve()
                .bodyToMono(UserDTO.class).block();
    }

    @CachePut(value = "users", key = "#result.id")
    public UserDTO addUser(UserDTO user) {
        return webClient.post().uri("/users").bodyValue(user).retrieve()
                .bodyToMono(UserDTO.class).block();
    }

    @CachePut(value = "users", key = "#id")
    public UserDTO updateUser(UserDTO user, Long id) {
        return webClient.put().uri("/users/{id}", id)
                .bodyValue(user)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    }

    @CacheEvict(value = "users", key = "#id")
    public Void deleteUserById(Long id) {
        return webClient.delete().uri("/users/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
