package com.ddmtchr.vktestrestapi.services;

import com.ddmtchr.vktestrestapi.database.entities.AuditLog;
import com.ddmtchr.vktestrestapi.database.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditService {
    private final AuditRepository auditRepository;

    public void saveLog(LocalDateTime time, String username, String requestMethod,
            String requestUrl, Integer responseStatus, Boolean hasAccess) {
        AuditLog log = new AuditLog();
        log.setTimestamp(time);
        log.setUsername(username);
        log.setRequestMethod(requestMethod);
        log.setRequestUrl(requestUrl);
        log.setResponseStatus(responseStatus);
        log.setHasAccess(hasAccess);
        auditRepository.save(log);
    }
}
