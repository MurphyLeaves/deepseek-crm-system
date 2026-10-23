package com.lestcrm.deepseekcrmsystem.controller;

import com.lestcrm.deepseekcrmsystem.dto.PasswordUpdateDTO;
import com.lestcrm.deepseekcrmsystem.entity.User;
import com.lestcrm.deepseekcrmsystem.service.UserService;
import com.lestcrm.deepseekcrmsystem.utils.AuthUtils;
import com.lestcrm.deepseekcrmsystem.utils.Result;
import com.lestcrm.deepseekcrmsystem.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;


//@Controller
@RestController
@RequestMapping("/api")

public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录接口
     * POST /api/login
     * 请求体：{ "username": "admin", "password": "123456" }
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody LoginRequest request, HttpSession session) {
        User user = userService.login(request.getUsername(), request.getPassword());
        if (user != null) {
            session.setAttribute("user", user);
            return Result.success(user);  // 登录成功
        } else {
            return Result.error("用户名或密码错误");
        }
    }

    /**
     * 获取当前登录用户信息
     * GET /api/user/info
     */
    @GetMapping("/user/info")
    public Result<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return Result.success(user);
        }
        return Result.error("未登录");
    }

    /**
     * 退出登录
     * POST /api/logout
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpSession session) {
        session.invalidate();
        return Result.success("退出成功");
    }

    // 在 LoginController 中新增以下两个方法

    /**
     * 获取所有用户列表（管理员功能）
     * GET /api/user/list
     * 仅管理员可调用，普通用户返回 403
     */
    @GetMapping("/user/list")
    public Result<List<User>> getUserList(HttpSession session) {
        if (!AuthUtils.isAdmin(session)) {
            return Result.forbidden("该功能仅管理员可用");
        }
        List<User> users = userService.getAllUsers();
        return Result.success(users);
    }

    /**
     * 修改当前用户密码
     * PUT /api/user/password
     */
    @PutMapping("/user/password")
    public Result<Boolean> updatePassword(@RequestBody PasswordUpdateDTO dto, HttpSession session) {
        // 获取当前登录用户
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.error("未登录，请先登录");
        }

        boolean success = userService.updatePassword(
                currentUser.getId(),
                dto.getOldPassword(),
                dto.getNewPassword()
        );

        if (success) {
            return Result.success(true);
        } else {
            return Result.error("原密码错误");
        }
    }
}