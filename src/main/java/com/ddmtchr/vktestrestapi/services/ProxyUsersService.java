package com.ddmtchr.vktestrestapi.services;

import com.ddmtchr.vktestrestapi.model.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(ProxyUsersService.class);

    @Autowired
    public ProxyUsersService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://jsonplaceholder.typicode.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Cacheable("usersCollection")
    public List<UserDTO> fetchUsers() {
        logger.info("request to api");
        return webClient.get().uri("/users").retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserDTO>>() {
                }).block();
    }

    @Cacheable(value = "users", key = "#id")
    public UserDTO fetchUserById(Long id) {
        logger.info("request to api");
        return webClient.get().uri("/users/{id}", id).retrieve()
                .bodyToMono(UserDTO.class).block();
    }

    @CachePut(value = "users", key = "#result.id")
    public UserDTO addUser(UserDTO user) {
        logger.info("request to api");
        return webClient.post().uri("/users").bodyValue(user).retrieve()
                .bodyToMono(UserDTO.class).block();
    }

    @CachePut(value = "users", key = "#id")
    public UserDTO updateUser(UserDTO user, Long id) {
        logger.info("request to api");
        return webClient.put().uri("/users/{id}", id)
                .bodyValue(user)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    }

    @CacheEvict(value = "users", key = "#id")
    public Void deleteUserById(Long id) {
        logger.info("request to delete user with id: {}", id);
        return webClient.delete().uri("/users/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
