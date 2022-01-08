package com.lys.community.config;

import com.lys.community.controller.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebConfig implements WebMvcConfigurer {
    //    注册拦截器
    @Autowired
    private LogHandlerInterceptor logHandlerInterceptor;

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logHandlerInterceptor)
                .excludePathPatterns("/**/*.css",
                        "/**/*.js", "/**/*.jpg", "/**/*.png")
                .addPathPatterns("/hello");//配置要拦截的目标

        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css",
                        "/**/*.js", "/**/*.jpg", "/**/*.png");
    }
}
