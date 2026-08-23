package com.lestcrm.deepseekcrmsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;//增加生成新密码

@SpringBootApplication
@MapperScan("com.lestcrm.deepseekcrmsystem.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        //System.out.println("新加密密码: " + new BCryptPasswordEncoder().encode("123456"));
    }

}
