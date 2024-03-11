package com.ddmtchr.vktestrestapi.controllers;

import com.ddmtchr.vktestrestapi.model.UserDTO;
import com.ddmtchr.vktestrestapi.services.ProxyUsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {
    private final ProxyUsersService proxyService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USERS')")
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(proxyService.fetchUsers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USERS')")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(proxyService.fetchUserById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USERS')")
    public ResponseEntity<?> addUser(@RequestBody @Valid UserDTO user) {
        return new ResponseEntity<>(proxyService.addUser(user), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USERS')")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserDTO user, @PathVariable Long id) {
        return ResponseEntity.ok(proxyService.updateUser(user, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USERS')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(proxyService.deleteUserById(id));
    }
}
