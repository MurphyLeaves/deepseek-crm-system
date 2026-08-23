package com.lestcrm.deepseekcrmsystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data  // Lombok自动生成getter/setter/toString
public class User {
    private Long id;
    private String username;
    private String password;
    private String role;
    private String realName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
