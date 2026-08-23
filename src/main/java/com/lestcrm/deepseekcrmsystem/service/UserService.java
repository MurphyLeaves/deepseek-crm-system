package com.lestcrm.deepseekcrmsystem.service;

import com.lestcrm.deepseekcrmsystem.entity.User;
import java.util.List;


public interface UserService {
    User login(String username, String password);

    /**
            * 获取所有用户
     */
    List<User> getAllUsers();

    /**
     * 修改用户密码
     * @param userId 用户ID
     * @param oldPassword 原密码（明文）
     * @param newPassword 新密码（明文）
     * @return true-成功，false-原密码错误
     */
    boolean updatePassword(Long userId, String oldPassword, String newPassword);
}
