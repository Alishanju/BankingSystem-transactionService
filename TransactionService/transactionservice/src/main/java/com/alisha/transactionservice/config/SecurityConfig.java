package com.alisha.transactionservice.config;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

        @Value("${jwt.public-key}")
        private Resource publicKeyResource;

        @Bean
        public SecurityFilterChain securityFilterChain(
                        HttpSecurity http)
                        throws Exception {

                http
                                .csrf(csrf -> csrf.disable())

                                .sessionManagement(session -> session.sessionCreationPolicy(
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
                                                oauth -> oauth.jwt(jwt -> {
                                                }));

                return http.build();
        }

        @Bean
        public JwtDecoder jwtDecoder() throws Exception {

                String publicKeyContent = new String(
                                publicKeyResource.getInputStream()
                                                .readAllBytes());

                publicKeyContent = publicKeyContent
                                .replace("-----BEGIN PUBLIC KEY-----", "")
                                .replace("-----END PUBLIC KEY-----", "")
                                .replaceAll("\\s+", "");

                byte[] decodedKey = Base64.getDecoder().decode(publicKeyContent);

                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);

                KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);

                return NimbusJwtDecoder
                                .withPublicKey(publicKey)
                                .build();
        }
}