package com.ddmtchr.vktestrestapi.security;

import com.ddmtchr.vktestrestapi.security.jwt.AccessDeniedHandlerJwt;
import com.ddmtchr.vktestrestapi.security.jwt.AuthenticationEntryPointJwt;
import com.ddmtchr.vktestrestapi.security.jwt.JwtAuthenticationFilter;
import com.ddmtchr.vktestrestapi.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableAspectJAutoProxy
public class WebSecurityConfig {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationEntryPointJwt unauthorizedHandler;

    @Autowired
    private AccessDeniedHandlerJwt accessDeniedHandlerJwt;

    @Bean
    public JwtAuthenticationFilter authenticationJwtTokenFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unauthorizedHandler)
                        .accessDeniedHandler(accessDeniedHandlerJwt))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(HttpMethod.GET, "/api/posts/**").hasAnyRole("ADMIN", "POSTS", "POSTS_VIEWER")
                                .requestMatchers(HttpMethod.POST, "/api/posts/**").hasAnyRole("ADMIN", "POSTS", "POSTS_EDITOR")
                                .requestMatchers(HttpMethod.PUT, "/api/posts/**").hasAnyRole("ADMIN", "POSTS", "POSTS_EDITOR")
                                .requestMatchers(HttpMethod.DELETE, "/api/posts/**").hasAnyRole("ADMIN", "POSTS", "POSTS_EDITOR")
                                .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("ADMIN", "USERS", "USERS_VIEWER")
                                .requestMatchers(HttpMethod.POST, "/api/users/**").hasAnyRole("ADMIN", "USERS", "USERS_EDITOR")
                                .requestMatchers(HttpMethod.PUT, "/api/users/**").hasAnyRole("ADMIN", "USERS", "USERS_EDITOR")
                                .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasAnyRole("ADMIN", "USERS", "USERS_EDITOR")
                                .requestMatchers(HttpMethod.GET, "/api/albums/**").hasAnyRole("ADMIN", "ALBUMS", "ALBUMS_VIEWER")
                                .requestMatchers(HttpMethod.POST, "/api/albums/**").hasAnyRole("ADMIN", "ALBUMS", "ALBUMS_EDITOR")
                                .requestMatchers(HttpMethod.PUT, "/api/albums/**").hasAnyRole("ADMIN", "ALBUMS", "ALBUMS_EDITOR")
                                .requestMatchers(HttpMethod.DELETE, "/api/albums/**").hasAnyRole("ADMIN", "ALBUMS", "ALBUMS_EDITOR")
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/ws/**").hasAnyRole("ADMIN", "WEBSOCKET")
                                .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
