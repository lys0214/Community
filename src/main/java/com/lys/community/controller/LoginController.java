package com.lys.community.controller;

import com.google.code.kaptcha.Producer;
import com.lys.community.entity.User;
import com.lys.community.service.impl.DiscussPostServiceDaoImp;
import com.lys.community.service.impl.UserServiceDaoImp;
import com.lys.community.utils.CommunityConstant;
import com.lys.community.utils.CommunityUtils;
import com.lys.community.utils.MailClient;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {
    @Autowired
    private UserServiceDaoImp userServiceDaoImp;

    @Autowired
    private DiscussPostServiceDaoImp discussPostServiceDaoImp;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private MailClient mailClient;

    //跳转注册页
    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegister() {
        return "/html/register";
    }

    //提交注册数据
    @RequestMapping(path = "/register1", method = RequestMethod.POST)
    public String register(Model model, User user,String codes,HttpSession session) {
        String codes1 = (String) session.getAttribute("codes");
        Map<String, String> map = userServiceDaoImp.register(user);
        if (codes1.equals(codes)) {
            System.out.println(user);
            System.out.println(map);
            if (map == null || map.isEmpty()) {
                //    没有错误消息
                model.addAttribute("message", "");
                model.addAttribute("target", "/community/login");
                return "/html/login";
            }
        }
        else {
            //    提交注册过程中有错误的信息
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            model.addAttribute("user", user);
            model.addAttribute("codeMsg", "验证码错误");
            return "/html/register";
        }
        return "";

    }

    //获取激活验证码
    @RequestMapping(path = "/getCode/{email}", method = RequestMethod.GET)
    @ResponseBody
    public String getCode(
                          @PathVariable String email,HttpSession session) {
        System.out.println("获取到邮箱"+email);
        Map<String, Object> map = userServiceDaoImp.getCode(email);
        if (map == null) {
            //发送邮件
            String codes = CommunityUtils.getUUID().substring(0, 5);
            System.out.println("生成的验证码是;"+codes);
            session.setAttribute("codes", codes);
            Context context = new Context();
            context.setVariable("codes", codes);
            String process = templateEngine.process("/html/codes", context);
            System.out.println("发送的邮件如下:" + process);
            mailClient.sendMail(email, "注册验证码", process);
            return "验证码已经发送";
        }else {
            return "验证码发送失败";
        }
    }



    //    激活确认,点击跳转激活结果页
    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model,
                             @PathVariable("userId") int userId,
                             @PathVariable("code") String code) {
        int activation = userServiceDaoImp.activation(userId, code);
        if (activation == ACTIVATION_SUCCESSFUL) {
            System.out.println("激活成功，您的账号可以使用了");
            model.addAttribute("msg", "激活成功，您的账号可以使用了");
            model.addAttribute("target", "/community/index");
        } else if (activation == ACTIVATION_REPEAT) {
            System.out.println("无效操作，重复激活");
            model.addAttribute("msg", "无效操作，重复激活");
            model.addAttribute("target", "/community/index");
        } else {
            System.out.println("激活失败，您提供的激活码不正确");
            model.addAttribute("msg", "激活失败，您提供的激活码不正确");
            model.addAttribute("target", "/community/index");
        }
        return "/html/activation_result";
    }

    @RequestMapping(path = "/toLogin", method = RequestMethod.GET)
    public String toLogin(Model model) {
        model.addAttribute("msg", "欢迎回来！");
        return "/html/login";
    }

    //    登录页操作

    @Autowired
    private Producer kaptchaProducer;

    /**
     * 请求一个验证码
     */

    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        //    生成验证码
        System.out.println("访问了/kaptcha");
        String text = kaptchaProducer.createText();
        session.setAttribute("kaptcha", text);
        System.out.println("session中的验证码是"+text);
        //    生成验证码图片
        BufferedImage image = kaptchaProducer.createImage(text);
        //    将图片传入session
        //    将图片输出到前端(图片+格式)
        response.setContentType("image/png");
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("响应验证码失败");
        }
    }

    /**
     * 登录
     */
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, String code, boolean rememberMe,
                        Model model, HttpSession session, HttpServletResponse response) {
        String kaptcha = (String) session.getAttribute("kaptcha");
        if (StringUtils.isNullOrEmpty(kaptcha) || StringUtils.isNullOrEmpty(code) || !(kaptcha).equalsIgnoreCase(code)) {
            System.out.println("验证码不正确");
            model.addAttribute("codeMsg1", "验证码不正确");
            return "/html/login";
        }
        //    是否记住密码
        int expireSeconds = rememberMe ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        //    检查账号，密码
        Map<String, Object> login = userServiceDaoImp.login(username, password, expireSeconds);
        if (login.containsKey("ticket")) {
            //    检查登录凭证是否正确
            Cookie cookie = new Cookie("ticket", login.get("ticket").toString());
            //    设置Cookie生效的范围
            cookie.setPath(contextPath);
            //    设置cookie的生效时间
            cookie.setMaxAge(expireSeconds);
            //    将cookie发送到浏览器
            response.addCookie(cookie);
            System.out.println("登录成功！");
            //将用户信息带过去
            User user = userServiceDaoImp.selectByName(username);
            model.addAttribute("user", user);
            //保存账户名和头像信息
            session.setAttribute("user_id", user.getId());
            return "redirect:/index";
        } else {
            // 将错误消息重新反馈给登录界面
            System.out.println("账号或者密码不正确");
            model.addAttribute("usernameMsg", login.get("usernameMsg"));
            model.addAttribute("passwordMsg", login.get("passwordMsg"));
            return "/html/login";
        }
    }
    /**
     * 退出登录
     */
    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        System.out.println("已经退出!");
        userServiceDaoImp.logout(ticket);
        return "redirect:/toLogin";
    }
}
