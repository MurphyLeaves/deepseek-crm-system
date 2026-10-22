package com.lestcrm.deepseekcrmsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 单页应用（SPA）路由转发
 *
 * 打包部署时前端构建产物放在 src/main/resources/static 下，
 * 由 Spring Boot 直接托管，整个系统只需要一个端口。
 *
 * 前端使用 history 路由，直接访问 /customers 这类地址时，
 * 服务器上并不存在对应文件，因此统一转发到 index.html，
 * 交给前端路由接管；刷新页面才不会 404。
 *
 * 只列举前端实际存在的路由，避免误伤 /api/** 与静态资源。
 */
@Controller
public class SpaController {

    @GetMapping({"/", "/login", "/home", "/customers", "/labels", "/dashboard", "/profile"})
    public String forwardToIndex() {
        return "forward:/index.html";
    }
}
