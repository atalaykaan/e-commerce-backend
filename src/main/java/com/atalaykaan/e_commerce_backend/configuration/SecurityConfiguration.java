package com.atalaykaan.e_commerce_backend.configuration;

import com.atalaykaan.e_commerce_backend.model.enums.Role;
import com.atalaykaan.e_commerce_backend.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.atalaykaan.e_commerce_backend.constants.ApiConstants.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserDetailsConfiguration userDetailsConfiguration;

    private final JwtAuthFilter jwtAuthFilter;

    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request ->
                                request
                                        .requestMatchers(API_PREFIX + API_VERSION + API_USERS + "/register", API_PREFIX + API_VERSION + API_USERS + "/login").permitAll()
                                        .requestMatchers(HttpMethod.GET, API_PREFIX + API_VERSION + API_USERS).hasRole("USER")
                                        .requestMatchers(HttpMethod.GET, API_PREFIX + API_VERSION + API_PRODUCTS).hasRole("USER")
                                        .requestMatchers(HttpMethod.GET, API_PREFIX + API_VERSION + API_PRODUCTS + "/*").hasRole("USER")
                                        .requestMatchers(API_PREFIX + API_VERSION + API_CARTS).hasRole("USER")
                                        .requestMatchers(HttpMethod.DELETE, API_PREFIX + API_VERSION + API_CARTS + "/cartItem/*").hasRole("USER")
                                        .requestMatchers(HttpMethod.GET, API_PREFIX + API_VERSION + API_ORDERS).hasRole("USER")
                                        .requestMatchers(HttpMethod.POST, API_PREFIX + API_VERSION + API_ORDERS).hasRole("USER")
                                        .requestMatchers(API_PREFIX + "/**").hasRole("ADMIN")
                                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsConfiguration);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }
}
