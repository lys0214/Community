package com.lys.community.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LogHandlerInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LogHandlerInterceptor.class);
//    controller执行前调用

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //logger.debug("controller前调用");
        System.out.println("controller前调用");
        return true;

    }

//    controller执行后页面渲染、

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //logger.debug("controller执行后进行的渲染");
        System.out.println("controller执行后进行的渲染");
    }


//    在DispatcherServlet 渲染了对应的视图之后执行，通常用它做资源清理工作

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //logger.debug("在模板渲染后的处理事件");
        System.out.println("在模板渲染后的处理事件");
    }

}
