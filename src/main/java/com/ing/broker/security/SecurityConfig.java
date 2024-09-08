package com.ing.broker.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}admin12345*")
                .roles("ADMIN")
                .build();

        UserDetails employee = User.builder()
                .username("employee")
                .password("{noop}employee12345")
                .roles("EMPLOYEE")
                .build();
        return new InMemoryUserDetailsManager(admin, employee);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        .requestMatchers(HttpMethod.POST, "/order/new").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/order/list").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/order/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/order/all-pending").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/order/match").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/asset/deposit").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/asset/withdraw").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/asset/search").hasAnyRole("EMPLOYEE", "ADMIN")
        );

        httpSecurity.httpBasic(Customizer.withDefaults());
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.headers(configurer -> configurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        return httpSecurity.build();
    }
}
