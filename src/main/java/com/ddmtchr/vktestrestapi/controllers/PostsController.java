package com.ddmtchr.vktestrestapi.controllers;

import com.ddmtchr.vktestrestapi.model.PostDTO;
import com.ddmtchr.vktestrestapi.services.ProxyPostsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostsController {
    private final ProxyPostsService proxyService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_POSTS')")
    public ResponseEntity<?> getPosts() {
        return ResponseEntity.ok(proxyService.fetchPosts());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_POSTS')")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(proxyService.fetchPostById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_POSTS')")
    public ResponseEntity<?> addPost(@RequestBody @Valid PostDTO post) {
        return new ResponseEntity<>(proxyService.addPost(post), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_POSTS')")
    public ResponseEntity<?> updatePost(@RequestBody @Valid PostDTO post, @PathVariable Long id) {
        return ResponseEntity.ok(proxyService.updatePost(post, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_POSTS')")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        return ResponseEntity.ok(proxyService.deletePostById(id));
    }
}
