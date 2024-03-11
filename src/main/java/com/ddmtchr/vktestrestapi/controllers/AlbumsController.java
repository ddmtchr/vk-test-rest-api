package com.ddmtchr.vktestrestapi.controllers;

import com.ddmtchr.vktestrestapi.model.AlbumDTO;
import com.ddmtchr.vktestrestapi.services.ProxyAlbumsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/albums")
public class AlbumsController {
    private final ProxyAlbumsService proxyService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ALBUMS')")
    public ResponseEntity<?> getAlbums() {
        return ResponseEntity.ok(proxyService.fetchAlbums());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ALBUMS')")
    public ResponseEntity<?> getAlbumById(@PathVariable Long id) {
        return ResponseEntity.ok(proxyService.fetchAlbumById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ALBUMS')")
    public ResponseEntity<?> addAlbum(@RequestBody @Valid AlbumDTO album) {
        return new ResponseEntity<>(proxyService.addAlbum(album), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ALBUMS')")
    public ResponseEntity<?> updateAlbum(@RequestBody @Valid AlbumDTO album, @PathVariable Long id) {
        return ResponseEntity.ok(proxyService.updateAlbum(album, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ALBUMS')")
    public ResponseEntity<?> deleteAlbum(@PathVariable Long id) {
        return ResponseEntity.ok(proxyService.deleteAlbumById(id));
    }
}
