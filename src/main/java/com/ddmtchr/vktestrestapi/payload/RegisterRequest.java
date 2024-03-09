package com.ddmtchr.vktestrestapi.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private Set<String> roles;
}
