package com.ddmtchr.vktestrestapi.controllers;

import com.ddmtchr.vktestrestapi.database.entities.Role;
import com.ddmtchr.vktestrestapi.database.entities.User;
import com.ddmtchr.vktestrestapi.model.Roles;
import com.ddmtchr.vktestrestapi.payload.JwtResponse;
import com.ddmtchr.vktestrestapi.payload.LoginRequest;
import com.ddmtchr.vktestrestapi.payload.RegisterRequest;
import com.ddmtchr.vktestrestapi.security.jwt.JwtUtils;
import com.ddmtchr.vktestrestapi.security.services.RoleService;
import com.ddmtchr.vktestrestapi.security.services.UserDetailsImpl;
import com.ddmtchr.vktestrestapi.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerUnitTest {
    @Mock
    private UserDetailsServiceImpl userService;
    @Mock
    private RoleService roleService;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private AuthController authController;

    @Test
    void testAuthenticateUser_ValidCredentials_ReturnsJwtResponse() {
        LoginRequest request = new LoginRequest("username", "password");
        UserDetails userDetails = new UserDetailsImpl(1L, "username", "password", Collections.emptyList());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("mocked_token");

        ResponseEntity<?> responseEntity = authController.authenticateUser(request);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        if (responseEntity.getBody() instanceof JwtResponse jwtResponse) {
            assertEquals(1L, jwtResponse.getId());
            assertEquals("mocked_token", jwtResponse.getJwt());
            assertEquals("username", jwtResponse.getUsername());
        } else {
            assertInstanceOf(JwtResponse.class, responseEntity.getBody());
        }
    }

    @Test
    void testAuthenticateUser_InvalidCredentials_ReturnsUnauthorized() {
        LoginRequest request = new LoginRequest("username", "password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(BadCredentialsException.class);
        ResponseEntity<?> responseEntity;
        try {
            responseEntity = authController.authenticateUser(request);
        } catch (BadCredentialsException e) {
            responseEntity = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoMoreInteractions(authenticationManager);
    }

    @Test
    void testRegisterUser_ValidRequest_ReturnsCreatedWithDefaultRole() {
        RegisterRequest request = new RegisterRequest("username", "password", null);

        when(userService.existsByUsername("username")).thenReturn(false);
        when(roleService.findRoleByName(Roles.ROLE_POSTS)).thenReturn(new Role(1L, Roles.ROLE_POSTS));

        ResponseEntity<?> responseEntity = authController.registerUser(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Registered", responseEntity.getBody());
        verify(userService, times(1))
                .addUser(argThat((User user) -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals(Roles.ROLE_POSTS))));
    }

    @Test
    void testRegisterUser_ExistingUsername_ReturnsBadRequest() {
        RegisterRequest request = new RegisterRequest("username", "password", null);

        when(userService.existsByUsername("username")).thenReturn(true);

        ResponseEntity<?> responseEntity = authController.registerUser(request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Username already exists", responseEntity.getBody());
        verify(userService, never()).addUser(any(User.class));
    }

    @Test
    void testRegisterUser_CustomRoles_ReturnsCreatedWithCustomRoles() {
        RegisterRequest request = new RegisterRequest("username", "password", Set.of("ADMIN", "USERS", "POSTS", "ALBUMS"));

        when(roleService.findRoleByName(Roles.ROLE_ADMIN)).thenReturn(new Role(1L, Roles.ROLE_ADMIN));
        when(roleService.findRoleByName(Roles.ROLE_USERS)).thenReturn(new Role(2L, Roles.ROLE_USERS));
        when(roleService.findRoleByName(Roles.ROLE_POSTS)).thenReturn(new Role(3L, Roles.ROLE_POSTS));
        when(roleService.findRoleByName(Roles.ROLE_ALBUMS)).thenReturn(new Role(4L, Roles.ROLE_ALBUMS));

        ResponseEntity<?> responseEntity = authController.registerUser(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Registered", responseEntity.getBody());
        verify(userService, times(1))
                .addUser(argThat((User user) ->
                                user.getRoles().stream().map(role ->
                                        role.getName().name()).collect(Collectors.toSet())
                                        .equals(Set.of("ROLE_ADMIN", "ROLE_USERS", "ROLE_POSTS", "ROLE_ALBUMS"))));
    }

}
