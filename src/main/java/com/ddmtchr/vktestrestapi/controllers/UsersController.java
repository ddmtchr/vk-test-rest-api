package com.ddmtchr.vktestrestapi.controllers;

import com.ddmtchr.vktestrestapi.model.UserDTO;
import com.ddmtchr.vktestrestapi.services.ProxyUsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UsersController {
    private final ProxyUsersService proxyService;

    @GetMapping
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(proxyService.fetchUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(proxyService.fetchUserById(id));
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody @Valid UserDTO user) {
        return new ResponseEntity<>(proxyService.addUser(user), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserDTO user, @PathVariable Long id) {
        return ResponseEntity.ok(proxyService.updateUser(user, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(proxyService.deleteUserById(id));
    }
}
