package com.along.common.constants;

import lombok.Getter;

/**
 * @Description: 返回状态枚举类
 * @Author along
 * @Date 2020/4/6 1:29
 */
@Getter
public enum ResultCodeEnum {

    // 未来在这里会有十分多的状态码！
    SUCCESS(true,20000,"成功"),
    UNKNOW_REASON(false,20001,"未知错误"),
    BAD_SQL_GRAMMAR(false,21001,"sql语法错误"),
    JSON_PARSE_ERROR(false,21002,"json 解析错误"),
    PARAM_ERROR(false,21003,"参数不正确");

    private Boolean success; // 是否响应成功
    private Integer code;    // 响应的状态码
    private String message;  // 响应的消息


    ResultCodeEnum(Boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }
}
