package com.ddmtchr.vktestrestapi.controllers;

import com.ddmtchr.vktestrestapi.database.entities.Role;
import com.ddmtchr.vktestrestapi.database.entities.User;
import com.ddmtchr.vktestrestapi.model.Roles;
import com.ddmtchr.vktestrestapi.payload.JwtResponse;
import com.ddmtchr.vktestrestapi.payload.LoginRequest;
import com.ddmtchr.vktestrestapi.payload.RegisterRequest;
import com.ddmtchr.vktestrestapi.security.jwt.JwtUtils;
import com.ddmtchr.vktestrestapi.security.services.UserDetailsImpl;
import com.ddmtchr.vktestrestapi.security.services.UserDetailsServiceImpl;
import com.ddmtchr.vktestrestapi.security.services.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserDetailsServiceImpl userService;
    private final RoleService roleService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        User user = new User(request.getUsername(), encoder.encode(request.getPassword()));
        Set<String> rolesString = request.getRoles();
        Set<Role> roles = new HashSet<>();
        if (rolesString == null || rolesString.isEmpty()) {
            Role postsRole = roleService.findRoleByName(Roles.ROLE_POSTS); // by default
            roles.add(postsRole);
        } else {
            rolesString.forEach((role) -> {
                switch (role) {
                    case "ADMIN":
                        Role adminRole = roleService.findRoleByName(Roles.ROLE_ADMIN);
                        roles.add(adminRole);
                        break;
                    case "USERS":
                        Role usersRole = roleService.findRoleByName(Roles.ROLE_USERS);
                        roles.add(usersRole);
                        break;
                    case "ALBUMS":
                        Role albumsRole = roleService.findRoleByName(Roles.ROLE_ALBUMS);
                        roles.add(albumsRole);
                        break;
                    case "POSTS":
                    default:
                        Role postsRole = roleService.findRoleByName(Roles.ROLE_POSTS); // by default
                        roles.add(postsRole);
                }
            });
        }
        user.setRoles(roles);
        userService.addUser(user);
        return new ResponseEntity<>("Registered", HttpStatus.CREATED);
    }
}
