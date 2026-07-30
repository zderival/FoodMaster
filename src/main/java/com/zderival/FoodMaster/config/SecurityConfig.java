package com.zderival.FoodMaster.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Marks this class as a source of Spring configuration and bean definitions
@Configuration
// Activates Spring Security's web security support
@EnableWebSecurity
// Generates a constructor for all final fields (constructor injection)
@RequiredArgsConstructor
public class SecurityConfig {

    // JwtFilter injected by Spring via the constructor Lombok generates
    private final JwtFilter jwtFilter;

    // Registers BCryptPasswordEncoder as a Spring bean so it can be
    // injected anywhere passwords need to be hashed or verified
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Defines the security rules for all incoming HTTP requests
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF — not needed since we use JWT in headers, not cookies
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // These endpoints are public — no token required
                        .requestMatchers("/auth/**", "/recipes/**").permitAll()
                        // Everything else requires a valid JWT
                        .anyRequest().authenticated()
                )

                // Tell Spring Security never to create an HTTP session
                // JWT is stateless — no server-side session storage needed
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Insert our JwtFilter before Spring's default auth filter
                // so every request is checked for a JWT token first
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                // Overrides Spring Security's default 403 response for unauthenticated requests.
                // When no valid JWT is present, this AuthenticationEntryPoint runs and sends
                // back 401 Unauthorized instead — the semantically correct status for
                // "you didn't prove who you are" (vs. 403, which means "I know you, but you can't do this").
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(((request, response, authException) ->
                        {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized: missing or invalid JWT");
                        })));

        return http.build();
    }


}