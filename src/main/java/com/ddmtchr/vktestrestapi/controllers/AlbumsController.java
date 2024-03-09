package com.ddmtchr.vktestrestapi.controllers;

import com.ddmtchr.vktestrestapi.services.ProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/albums")
public class AlbumsController {
    private final ProxyService proxyService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ALBUMS')")
    public ResponseEntity<?> getAlbums() {
        return ResponseEntity.ok(proxyService.fetchAlbums());
    }

}
