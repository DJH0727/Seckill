package com.xmu.seckill.service;

import com.xmu.seckill.entity.User;

import java.util.List;

public interface UserService {
    User findById(Long id);  // 根据用户id查找用户
    User findByName(String name);  // 根据用户名查找用户
    void register(User user);  // 用户注册
    String login(String username, String password);
    List<User> getAll();
}
