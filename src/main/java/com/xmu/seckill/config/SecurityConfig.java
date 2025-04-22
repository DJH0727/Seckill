package com.xmu.seckill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
/**
 * @author 2724
 * @date 2025/4/21
 * @description 配置 Spring Security，禁止 CSRF
 */
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 配置 HTTP 请求授权
                .formLogin(formLogin -> formLogin.loginPage("/login").permitAll())
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .anyRequest().permitAll()  // 其他请求无需认证
                )
                .csrf(AbstractHttpConfigurer::disable);
                ;

        return http.build();
    }
}
