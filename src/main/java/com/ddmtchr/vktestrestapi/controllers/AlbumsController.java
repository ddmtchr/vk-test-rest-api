package com.ddmtchr.vktestrestapi.controllers;

import com.ddmtchr.vktestrestapi.model.AlbumDTO;
import com.ddmtchr.vktestrestapi.services.ProxyAlbumsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/albums")
public class AlbumsController {
    private final ProxyAlbumsService proxyService;

    @GetMapping
    public ResponseEntity<?> getAlbums() {
        return ResponseEntity.ok(proxyService.fetchAlbums());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAlbumById(@PathVariable Long id) {
        return ResponseEntity.ok(proxyService.fetchAlbumById(id));
    }

    @PostMapping
    public ResponseEntity<?> addAlbum(@RequestBody @Valid AlbumDTO album) {
        return new ResponseEntity<>(proxyService.addAlbum(album), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAlbum(@RequestBody @Valid AlbumDTO album, @PathVariable Long id) {
        return ResponseEntity.ok(proxyService.updateAlbum(album, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAlbum(@PathVariable Long id) {
        return ResponseEntity.ok(proxyService.deleteAlbumById(id));
    }
}
