package com.xmu.seckill.config;

import com.xmu.seckill.util.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
/**
 * @author 2724
 * @date 2025/4/21
 * @description 配置 Spring Security，禁止 CSRF
 */
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 配置 HTTP 请求授权
                .formLogin(AbstractHttpConfigurer::disable)// 禁用表单登录
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))// 禁用 session
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                                .anyRequest().permitAll()  // 所有请求都无需认证
                        /* //如果接口需要认证，则取消注释
                                .requestMatchers("/user/login", "/user/register",
                                        "/swagger-ui/**","/v3/api-docs/**","/webjars/**",
                                        "/swagger-resources/**").permitAll()  // 允许登录和注册页面访问
                                .anyRequest().authenticated()// 其他请求需要认证
                         */
                )
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // 添加 JWT 过滤器;

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
