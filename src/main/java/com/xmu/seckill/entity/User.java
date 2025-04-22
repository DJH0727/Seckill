package com.xmu.seckill.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private Long id;       // 用户ID
    private String name;   // 用户名
    private String password;  // 用户密码
}
