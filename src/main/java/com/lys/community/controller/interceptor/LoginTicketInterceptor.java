package com.lys.community.controller.interceptor;

import com.lys.community.dao.LoginTicketDao;
import com.lys.community.entity.LoginTicket;
import com.lys.community.entity.User;
import com.lys.community.service.impl.UserServiceDaoImp;
import com.lys.community.utils.CookieUtil;
import com.lys.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Autowired
    private UserServiceDaoImp userServiceDaoImp;

    @Autowired
    private LoginTicketDao loginTicketDao;

    @Autowired
    private HostHolder hostHolder;

    /**
     * 在controller执行前从cookie中获取凭证
     * @param request 请求
     * @param response 回复
     * @param handler 处理程序
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("从Cookie中获取登录ticket");
    //   从cookie中获取凭证
        String ticket = CookieUtil.getValue(request, "ticket");
        System.out.println(ticket);
        if (ticket != null) {
        //    查询凭证
            LoginTicket loginTicket = loginTicketDao.selectByTicket(ticket);
            if (loginTicket != null && loginTicket.getStatus() == 0 ) {
                User user = userServiceDaoImp.selectById(loginTicket.getUserid());
                hostHolder.setUser(user);
                System.out.println(hostHolder);
            }
        }
        return true;
    }

    /**
     * 从持有用户发送给modelview
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println(hostHolder);
        User user = hostHolder.getUser();
        System.out.println("根据ticket查找的用户是:"+user);
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    /**
     * 在模板加载之后清理数据
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
