package com.example.roadmap.config;

import com.example.roadmap.config.jwt.JwtAuthenticationFilter;
import com.example.roadmap.config.jwt.JwtExceptionFilter;
import com.example.roadmap.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정을 위한 클래스
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors() // CORS 에러 방지

                // 세션을 사용하지 않을거라 세션 설정을 Stateless 로 설정
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 접근 권한 설정부
                .and().authorizeRequests()
                .requestMatchers(HttpMethod.GET, "/", "/tour/**", "/main/**").permitAll() // tour, main uri get요청 허용
                .requestMatchers("/login/**", "/signup", "/reissue").permitAll() // login, signup uri 접근 허용
                .requestMatchers("/user/**").hasRole("USER") // 해당 uri는 USER 역할이여야 함
                .anyRequest().authenticated() // 이외의 요청은 인증되어야 함

                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, new JwtAuthenticationFilter(jwtTokenProvider).getClass());
        return http.build();
    }

    // JWT를 사용하기 위해서는 기본적으로 password encoder가 필요
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
