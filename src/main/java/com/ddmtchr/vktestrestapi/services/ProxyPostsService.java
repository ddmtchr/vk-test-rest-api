package com.ddmtchr.vktestrestapi.services;

import com.ddmtchr.vktestrestapi.model.PostDTO;
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
public class ProxyPostsService {
    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(ProxyPostsService.class);

    @Autowired
    public ProxyPostsService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://jsonplaceholder.typicode.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Cacheable("postsCollection")
    public List<PostDTO> fetchPosts() {
        logger.info("request to api");
        return webClient.get().uri("/posts").retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<PostDTO>>() {
                }).block();
    }

    @Cacheable(value = "posts", key = "#id")
    public PostDTO fetchPostById(Long id) {
        logger.info("request to api");
        return webClient.get().uri("/posts/{id}", id).retrieve()
                .bodyToMono(PostDTO.class).block();
    }

    @CachePut(value = "posts", key = "#result.id")
    public PostDTO addPost(PostDTO post) {
        logger.info("request to api");
        return webClient.post().uri("/posts").bodyValue(post).retrieve()
                .bodyToMono(PostDTO.class).block();
    }

    @CachePut(value = "posts", key = "#id")
    public PostDTO updatePost(PostDTO post, Long id) {
        logger.info("request to api");
        return webClient.put().uri("/posts/{id}", id)
                .bodyValue(post)
                .retrieve()
                .bodyToMono(PostDTO.class)
                .block();
    }

    @CacheEvict(value = "posts", key = "#id")
    public Void deletePostById(Long id) {
        logger.info("request to delete post with id: {}", id);
        return webClient.delete().uri("/posts/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
