package com.xmu.seckill.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtUtils {
    // 生成用于签名的密钥（HMAC-SHA256）
    private final Key key = Keys.hmacShaKeyFor("qwer_poiu_zxcv_mnbv_1234_098_wad".getBytes(StandardCharsets.UTF_8));

    // Token 过期时间：1小时（单位：毫秒）
    private final long expireTime = 3600_000;

    // 生成 Token，包含用户 ID 作为 Subject
    public  String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // 设置 Subject 为用户 ID
                .setIssuedAt(new Date()) // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + expireTime)) // 设置过期时间
                .signWith(key) // 使用私钥进行签名
                .compact(); // 构建 JWT 字符串
    }

    // 解析 Token，返回用户 ID，如果非法则返回 null
    public Long parseToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key) // 设置密钥用于校验签名
                    .build()
                    .parseClaimsJws(token) // 解析 JWT
                    .getBody(); // 获取载荷部分（Claims）

            return Long.parseLong(claims.getSubject()); // 从 Subject 中获取用户 ID
        } catch (JwtException e) {
            // 捕获各种 JWT 异常（如过期、篡改等）
            return null;
        }
    }
}
