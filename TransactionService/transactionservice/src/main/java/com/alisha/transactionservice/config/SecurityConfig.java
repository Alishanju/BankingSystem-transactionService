package com.alisha.transactionservice.config;

import java.security.interfaces.RSAPublicKey;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import org.springframework.security.web.SecurityFilterChain;

import com.alisha.transactionservice.security.JwtProperties;

import java.nio.file.Files;

import java.security.KeyFactory;

import java.security.spec.X509EncodedKeySpec;

import java.util.Base64;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final ResourceLoader resourceLoader;

    private final JwtProperties jwtProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/actuator/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**")
                        .permitAll()

                        .anyRequest()
                        .authenticated())

                .oauth2ResourceServer(
                        oauth ->
                                oauth.jwt(jwt -> {
                                }));

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder()
            throws Exception {

        Resource resource =
                resourceLoader.getResource(
                        jwtProperties.getPublicKey());

        String key = Files.readString(
                resource.getFile().toPath());

        key = key
                .replace(
                        "-----BEGIN PUBLIC KEY-----",
                        "")
                .replace(
                        "-----END PUBLIC KEY-----",
                        "")
                .replaceAll("\\s", "");

        byte[] decoded =
                Base64.getDecoder()
                        .decode(key);

        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(decoded);

        KeyFactory keyFactory =
                KeyFactory.getInstance("RSA");

        RSAPublicKey publicKey =
                (RSAPublicKey)
                        keyFactory.generatePublic(spec);

        return NimbusJwtDecoder
                .withPublicKey(publicKey)
                .build();
    }
}