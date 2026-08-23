package com.lestcrm.deepseekcrmsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lestcrm.deepseekcrmsystem.entity.User;
import com.lestcrm.deepseekcrmsystem.mapper.UserMapper;
import com.lestcrm.deepseekcrmsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    // 【优化点 1】 摒弃 @Autowired 字段注入，使用构造器注入
    private final UserMapper userMapper;

    // 创建一个 BCrypt 编码器（在 Spring Security 配置未单独注入 Bean 的情况下，直接 new 是合法且安全的）
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            return null;
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(User::getUsername);
        return userMapper.selectList(wrapper);
    }

    @Override
    public boolean updatePassword(Long userId, String oldPassword, String newPassword) {
        // 1. 查询用户
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;
        }

        // 2. 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }

        // 3. 校验新密码（至少6位）
        if (!StringUtils.hasText(newPassword) || newPassword.length() < 6) {
            return false;
        }

        // 4. 加密新密码并更新
        user.setPassword(passwordEncoder.encode(newPassword));
        int rows = userMapper.updateById(user);
        return rows > 0;
    }
}