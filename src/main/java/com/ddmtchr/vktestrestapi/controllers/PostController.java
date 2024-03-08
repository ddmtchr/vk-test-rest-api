package com.ddmtchr.vktestrestapi.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    @GetMapping
//    @PreAuthorize("hasRole('ROLE_USERS')")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getPosts() {
        return ResponseEntity.ok("Got data");
    }
}
