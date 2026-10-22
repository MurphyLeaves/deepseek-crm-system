package com.lestcrm.deepseekcrmsystem.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data  // Lombok自动生成getter/setter/toString
public class User {
    private Long id;
    private String username;

    /**
     * 密码（BCrypt 密文）。
     * WRITE_ONLY 表示该字段只在反序列化（接收请求体）时可用，
     * 序列化为 JSON 响应时会被忽略，防止接口把密码哈希返回给前端。
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String role;
    private String realName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
