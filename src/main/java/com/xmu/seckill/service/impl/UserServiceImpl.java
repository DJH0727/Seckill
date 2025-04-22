package com.xmu.seckill.service.impl;

import com.xmu.seckill.entity.User;
import com.xmu.seckill.mapper.UserMapper;
import com.xmu.seckill.service.UserService;
import com.xmu.seckill.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final StringRedisTemplate redisTemplate;
    @Autowired
    public UserServiceImpl(UserMapper userMapper, StringRedisTemplate redisTemplate) {
        this.userMapper = userMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public User findById(Long id) {
        return userMapper.findById(id);
    }

    @Override
    public User findByName(String name) {
        return userMapper.findByName(name);
    }

    @Override
    public void register(User user) {
        String encodedPassword = PasswordUtil.encode(user.getPassword());  // 加密密码
        user.setPassword(encodedPassword);
        userMapper.insertUser(user);
    }


    @Override
    public String login(String name, String password) {
        User user = userMapper.findByName(name);
        if (user != null && PasswordUtil.matches(password, user.getPassword())) {
            String token = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set("login:token:" + token, String.valueOf(user.getId()), 30, TimeUnit.MINUTES);
            return token;
        }
        return null;
    }
    @Override
    public List<User> getAll() {
        return userMapper.selectAll();
    }

}
