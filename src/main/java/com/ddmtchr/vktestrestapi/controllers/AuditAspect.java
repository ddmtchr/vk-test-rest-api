package com.ddmtchr.vktestrestapi.controllers;

import com.ddmtchr.vktestrestapi.services.AuditService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {
    private final AuditService auditService;

    @Pointcut("execution(* com.ddmtchr.vktestrestapi.controllers.PostsController.*(..)) || " +
            "execution(* com.ddmtchr.vktestrestapi.controllers.UsersController.*(..)) || " +
            "execution(* com.ddmtchr.vktestrestapi.controllers.AlbumsController.*(..))")
    private void controllerMethodAuthorized() {}

    @Pointcut("execution(* com.ddmtchr.vktestrestapi.security.jwt.AccessDeniedHandlerJwt.handle(..))")
    private void accessDeniedHandlerMethods() {}

    @Pointcut("execution(* com.ddmtchr.vktestrestapi.security.jwt.AuthenticationEntryPointJwt.commence(..))")
    private void unauthorizedHandlerMethods() {}

    @Pointcut("execution(* com.ddmtchr.vktestrestapi.controllers.GlobalExceptionHandler.*(..))")
    private void exceptionHandlerMethods() {}

    @AfterReturning(pointcut = "controllerMethodAuthorized()", returning = "result")
    public void afterControllerMethod(Object result) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String username = getCurrentUsernameFromAuth();
        String requestUrl = request.getRequestURI();
        String requestMethod = request.getMethod();
        if (result instanceof ResponseEntity<?> response) {
            Integer responseStatus = response.getStatusCode().value();

            auditService.saveLog(LocalDateTime.now(), username, requestMethod, requestUrl, responseStatus, true);
        }
    }

    @After("accessDeniedHandlerMethods()")
    public void afterAccessDeniedHandlerMethod() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        if (!request.getRequestURI().startsWith("/api/auth")) {
            auditService.saveLog(LocalDateTime.now(), getCurrentUsernameFromAuth(), request.getMethod(),
                    request.getRequestURI(), response.getStatus(), false);
        }
    }

    @After("unauthorizedHandlerMethods()")
    public void afterUnauthorizedHandlerMethod() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        if (!request.getRequestURI().startsWith("/api/auth")) {
            auditService.saveLog(LocalDateTime.now(), null, request.getMethod(),
                    request.getRequestURI(), response.getStatus(), false);
        }
    }

    @AfterReturning(pointcut = "exceptionHandlerMethods()", returning = "result")
    public void afterExceptionHandlerMethod(Object result) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        if (!request.getRequestURI().startsWith("/api/auth")) {
            if (result instanceof ResponseEntity<?> response) {
                auditService.saveLog(LocalDateTime.now(), getCurrentUsernameFromAuth(), request.getMethod(),
                        request.getRequestURI(), response.getStatusCode().value(), true);
            }
        }
    }

    private String getCurrentUsernameFromAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return ((UserDetails)auth.getPrincipal()).getUsername();
        }
        return null;
    }
}
