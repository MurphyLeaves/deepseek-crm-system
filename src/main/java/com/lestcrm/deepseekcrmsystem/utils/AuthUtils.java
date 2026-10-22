package com.lestcrm.deepseekcrmsystem.utils;

import com.lestcrm.deepseekcrmsystem.config.AuthInterceptor;
import com.lestcrm.deepseekcrmsystem.entity.User;
import jakarta.servlet.http.HttpSession;

/**
 * 登录用户与角色判断的工具类
 *
 * 系统中角色取值来自 user 表的 role 字段，数据库中实际存储为
 * 小写字符串 "admin" / "user"。这里统一用 equalsIgnoreCase 比较，
 * 避免前端或数据库大小写不一致时判断失效。
 */
public final class AuthUtils {

    /** 管理员角色标识 */
    public static final String ROLE_ADMIN = "admin";

    private AuthUtils() {
    }

    /**
     * 从 Session 中取出当前登录用户，未登录返回 null
     */
    public static User currentUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object user = session.getAttribute(AuthInterceptor.SESSION_USER_KEY);
        return (user instanceof User) ? (User) user : null;
    }

    /**
     * 判断指定用户是否为管理员
     */
    public static boolean isAdmin(User user) {
        return user != null && ROLE_ADMIN.equalsIgnoreCase(user.getRole());
    }

    /**
     * 判断当前会话的登录用户是否为管理员
     */
    public static boolean isAdmin(HttpSession session) {
        return isAdmin(currentUser(session));
    }
}
