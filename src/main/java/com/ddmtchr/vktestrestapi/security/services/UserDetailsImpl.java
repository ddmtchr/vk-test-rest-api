package com.ddmtchr.vktestrestapi.security.services;

import com.ddmtchr.vktestrestapi.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    private Collection<GrantedAuthority> authorities;

    public static UserDetailsImpl userToUserDetails(User user) {
        List<GrantedAuthority> authorityList = user.getRoles().stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getPassword(), authorityList);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
