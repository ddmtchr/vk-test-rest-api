package com.ddmtchr.vktestrestapi.services;

import com.ddmtchr.vktestrestapi.model.AlbumDTO;
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
public class ProxyAlbumsService {
    private final WebClient webClient;

    @Autowired
    public ProxyAlbumsService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://jsonplaceholder.typicode.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Cacheable("albumsCollection")
    public List<AlbumDTO> fetchAlbums() {
        return webClient.get().uri("/albums").retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<AlbumDTO>>() {
                }).block();
    }

    @Cacheable(value = "albums", key = "#id")
    public AlbumDTO fetchAlbumById(Long id) {
        return webClient.get().uri("/albums/{id}", id).retrieve()
                .bodyToMono(AlbumDTO.class).block();
    }

    @CachePut(value = "albums", key = "#result.id")
    public AlbumDTO addAlbum(AlbumDTO album) {
        return webClient.post().uri("/albums").bodyValue(album).retrieve()
                .bodyToMono(AlbumDTO.class).block();
    }

    @CachePut(value = "albums", key = "#id")
    public AlbumDTO updateAlbum(AlbumDTO album, Long id) {
        return webClient.put().uri("/albums/{id}", id)
                .bodyValue(album)
                .retrieve()
                .bodyToMono(AlbumDTO.class)
                .block();
    }

    @CacheEvict(value = "albums", key = "#id")
    public Void deleteAlbumById(Long id) {
        return webClient.delete().uri("/albums/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}

