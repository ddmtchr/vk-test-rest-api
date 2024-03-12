package com.ddmtchr.vktestrestapi.controllers;

import com.ddmtchr.vktestrestapi.model.PostDTO;
import com.ddmtchr.vktestrestapi.services.ProxyPostsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostsController {
    private final ProxyPostsService proxyService;

    @GetMapping
    public ResponseEntity<?> getPosts() {
        return ResponseEntity.ok(proxyService.fetchPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(proxyService.fetchPostById(id));
    }

    @PostMapping
    public ResponseEntity<?> addPost(@RequestBody @Valid PostDTO post) {
        return new ResponseEntity<>(proxyService.addPost(post), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@RequestBody @Valid PostDTO post, @PathVariable Long id) {
        return ResponseEntity.ok(proxyService.updatePost(post, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        return ResponseEntity.ok(proxyService.deletePostById(id));
    }
}
