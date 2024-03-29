package com.ddmtchr.vktestrestapi.services;

import com.ddmtchr.vktestrestapi.model.PostDTO;
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
public class ProxyPostsService {
    private final WebClient webClient;

    @Autowired
    public ProxyPostsService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://jsonplaceholder.typicode.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Cacheable("postsCollection")
    public List<PostDTO> fetchPosts() {
        return webClient.get().uri("/posts").retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<PostDTO>>() {
                }).block();
    }

    @Cacheable(value = "posts", key = "#id")
    public PostDTO fetchPostById(Long id) {
        return webClient.get().uri("/posts/{id}", id).retrieve()
                .bodyToMono(PostDTO.class).block();
    }

    @CachePut(value = "posts", key = "#result.id")
    public PostDTO addPost(PostDTO post) {
        return webClient.post().uri("/posts").bodyValue(post).retrieve()
                .bodyToMono(PostDTO.class).block();
    }

    @CachePut(value = "posts", key = "#id")
    public PostDTO updatePost(PostDTO post, Long id) {
        return webClient.put().uri("/posts/{id}", id)
                .bodyValue(post)
                .retrieve()
                .bodyToMono(PostDTO.class)
                .block();
    }

    @CacheEvict(value = "posts", key = "#id")
    public Void deletePostById(Long id) {
        return webClient.delete().uri("/posts/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
