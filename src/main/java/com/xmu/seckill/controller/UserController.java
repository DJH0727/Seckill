package com.xmu.seckill.controller;

import com.xmu.seckill.common.Result;
import com.xmu.seckill.common.ResultCode;
import com.xmu.seckill.dto.UserRequest;
import com.xmu.seckill.entity.User;
import com.xmu.seckill.service.UserService;
import com.xmu.seckill.util.Dto2Entity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "用户接口", description = "用户的注册、登录、查询操作")
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
        User user = Dto2Entity.UserDto2Entity(userRequest);
        String token = userService.login(user.getName(), user.getPassword());
        return token != null ? Result.success(token)   : Result.failed(ResultCode.PASSWORD_ERROR);
    }
}
