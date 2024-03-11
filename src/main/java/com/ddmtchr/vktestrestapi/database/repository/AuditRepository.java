package com.ddmtchr.vktestrestapi.database.repository;

import com.ddmtchr.vktestrestapi.database.entities.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends JpaRepository<AuditLog, Long> {
    AuditLog findFirstByOrderByTimestampDesc();
}
