package com.lestcrm.deepseekcrmsystem.config;

import com.alibaba.fastjson.JSON;
import com.lestcrm.deepseekcrmsystem.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

/**
 * 登录访问控制拦截器
 *
 * 作用：对 /api/** 下的所有接口做统一的登录校验。
 * 未登录（Session 中没有 user 对象）的请求直接返回 401，
 * 不再进入 Controller，从而避免未授权访问业务数据。
 *
 * 白名单（登录接口、跨域预检请求）在 WebMvcConfig 中通过
 * excludePathPatterns 排除，不在这里判断。
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    /** Session 中保存登录用户信息的 key，与 LoginController 保持一致 */
    public static final String SESSION_USER_KEY = "user";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 跨域预检请求（OPTIONS）不含业务数据，直接放行，否则浏览器预检会失败
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        HttpSession session = request.getSession(false);
        Object user = (session == null) ? null : session.getAttribute(SESSION_USER_KEY);

        if (user != null) {
            // 已登录，放行
            return true;
        }

        // 未登录：返回 401 + 统一 JSON 结构，前端拦截后跳转登录页
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(JSON.toJSONString(Result.unauthorized("未登录或登录已过期，请重新登录")));
            writer.flush();
        }
        return false;
    }
}
