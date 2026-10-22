package com.lestcrm.deepseekcrmsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置：注册登录访问控制拦截器
 *
 * /api/** 下的所有接口默认都需要登录后才能访问，
 * 登录与登出接口列入白名单放行；
 * 修改密码等接口在 Controller 内部还会再取一次 Session 做兜底判断。
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebMvcConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/login",   // 登录接口，必须放行
                        "/api/logout"   // 登出接口，未登录时调用也应当正常返回
                );
    }
}
