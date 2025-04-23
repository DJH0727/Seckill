package com.xmu.seckill.common;

import lombok.Getter;

@Getter
public enum ResultCode {
    //通用错误码
    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),
    VALIDATE_FAILED(400, "参数校验失败"),
    UNAUTHORIZED(401, "未认证或token过期"),
    FORBIDDEN(403, "没有相关权限"),
    NOT_FOUND(404, "资源不存在"),
    //自定义错误码
    USER_NOT_EXIST(1001, "用户不存在"),
    PASSWORD_ERROR(1002, "用户名或密码错误"),
    USER_NAME_EXIST(1003, "用户已存在"),
    TOO_MANY_REQUESTS(1004, "失败次数过多，请10分钟后再试");
    
 
    

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
