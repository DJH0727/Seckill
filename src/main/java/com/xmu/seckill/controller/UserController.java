package com.xmu.seckill.controller;

import com.xmu.seckill.common.Result;
import com.xmu.seckill.common.ResultCode;
import com.xmu.seckill.constants.RedisKey;
import com.xmu.seckill.dto.UserRequest;
import com.xmu.seckill.entity.User;
import com.xmu.seckill.service.UserService;
import com.xmu.seckill.util.Dto2Entity;
import com.xmu.seckill.util.JwtUtils;
import com.xmu.seckill.util.PasswordUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Tag(name = "用户接口", description = "用户的注册、登录、查询操作")
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final StringRedisTemplate redisTemplate;
    private final JwtUtils jwtUtils = new JwtUtils();
    @Autowired
    public UserController(UserService userService, StringRedisTemplate redisTemplate) {
        this.userService = userService;
        this.redisTemplate = redisTemplate;
    }

    @Operation(summary = "心跳检测", description = "用于检测用户服务是否可用")
    @GetMapping("/ping")
    public Result<String> ping() {
        return Result.success("pong");
    }
    // 用户注册接口
    @Operation(summary = "用户注册", description = "根据用户名和密码注册新用户")
    @PostMapping("/register")
    public Result<String> register(@RequestBody UserRequest userRequest) {
        //logger.info("register user: {}", userRequest);
        // 注册时先检查用户名是否已存在
        User user = Dto2Entity.UserDto2Entity(userRequest);
        if (userService.findByName(user.getName()) != null) {
            return Result.failed(ResultCode.USER_NAME_EXIST);
        }
        // 注册用户
        userService.register(user);
        return Result.success("注册成功");
    }
    @Operation(summary = "获取用户列表", description = "获取所有注册用户信息")
    @GetMapping("/list")
    public Result<String> list() {
        logger.info("get user list");
        List<User> userList = userService.getAll();
        return Result.success("一共有"+userList.size()+"个用户"+userList);
    }


    @Operation(summary = "用户登录", description = "根据用户名和密码进行登录验证")
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserRequest userRequest) {
        logger.info("login user: {}", userRequest);

        User user = userService.findByName(userRequest.getName());
        // Redis 中记录失败次数的 Key
        String key = RedisKey.LOGIN_FAIL_PREFIX + userRequest.getName();
        String failCount = redisTemplate.opsForValue().get(key);
        if (failCount != null && Integer.parseInt(failCount) >= 5) {
            return Result.failed(ResultCode.TOO_MANY_REQUESTS);
        }
        // 验证用户存在且密码正确
        if (user == null || !PasswordUtil.matches(userRequest.getPassword(), user.getPassword())) {
            // 增加登录失败次数
            redisTemplate.opsForValue().increment(key);
            // 设置10分钟的过期时间
            redisTemplate.expire(key, 10, TimeUnit.MINUTES);
            return Result.failed(ResultCode.PASSWORD_ERROR);
        }
        // 登录成功，清除失败记录
        redisTemplate.delete(key);

        // 生成 JWT Token
        String token = jwtUtils.generateToken(user.getId());

        return Result.success("登录成功，token：" + token);
    }
}
