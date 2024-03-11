package com.ddmtchr.vktestrestapi.security.services;

import com.ddmtchr.vktestrestapi.database.entities.Role;
import com.ddmtchr.vktestrestapi.database.repository.RoleRepository;
import com.ddmtchr.vktestrestapi.model.Roles;
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
