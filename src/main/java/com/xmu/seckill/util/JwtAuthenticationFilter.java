package com.xmu.seckill.util;

import com.xmu.seckill.util.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT 认证过滤器，继承 OncePerRequestFilter 确保每个请求只过滤一次。
 * 用于从请求头中提取 JWT Token，校验后将用户信息注入 Spring Security 上下文。
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtUtils jwtUtils = new JwtUtils();

    /**
     * 过滤器主逻辑：解析 JWT、验证、设置认证上下文。
     */
    @Override
    protected void doFilterInternal( @NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull  FilterChain filterChain)
            throws ServletException, IOException {

        // 从请求头中解析出 JWT Token
        String token = getTokenFromRequest(request);

        if (StringUtils.hasText(token)) {
            Long userId = jwtUtils.parseToken(token); // 使用你的 JwtUtils 解析 Token
            if (userId != null) {
                // 将用户 ID 放入 Spring Security 上下文中，表示已登录状态
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                //logger.info("JWT 验证通过，用户ID：{}", userId);
            } else {
                logger.warn("无效的 JWT Token");
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 从请求头中提取 JWT Token。格式应为：Authorization: Bearer xxxxx
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // 通常 Token 放在 Authorization 头部，格式为 "Bearer token"
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // 去掉 "Bearer " 前缀
        }
        return null;
    }
}
