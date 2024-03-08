package com.ddmtchr.vktestrestapi.repository;

import com.ddmtchr.vktestrestapi.model.Role;
import com.ddmtchr.vktestrestapi.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(Roles name);
}
