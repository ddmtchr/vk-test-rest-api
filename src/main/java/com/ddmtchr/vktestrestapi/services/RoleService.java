package com.ddmtchr.vktestrestapi.services;

import com.ddmtchr.vktestrestapi.model.Role;
import com.ddmtchr.vktestrestapi.model.Roles;
import com.ddmtchr.vktestrestapi.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role findRoleByName(Roles name) {
        Optional<Role> role = roleRepository.findByName(name);
        return role.orElse(null);
    }
}
