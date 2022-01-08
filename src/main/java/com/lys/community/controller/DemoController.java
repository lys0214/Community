package com.lys.community.controller;

import com.alibaba.fastjson.JSON;
import com.lys.community.entity.DiscussPost;
import com.lys.community.entity.User;
import com.lys.community.service.DiscussPostServiceDao;
import com.lys.community.service.UserServiceDao;
import com.lys.community.utils.CommunityUtils;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.*;

//注意：这里如果是要转换视图，就是用@controller,如果是返回数据，是用@resController
@Controller
//解决跨域请求
@CrossOrigin
public class DemoController {
    @Autowired
    private UserServiceDao userServiceDao;
    @Autowired
    private DiscussPostServiceDao discussPostServiceDao;

    //测试ajax使用
    @RequestMapping(path = "/ajax", method = RequestMethod.POST)
    @ResponseBody
    public String testAjax(String name, int age) {
        System.out.println(name);
        System.out.println(age);
        return CommunityUtils.getJSONString(0, "操作成功");
    }

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(Request request) {
        System.out.println(request.getRemoteUser());
        System.out.println(request.remoteAddr());
        return "hello,123";
    }

    @RequestMapping("/demo1")
    public String demo1() {
        return "poiu";
    }
    
    @RequestMapping("/ajax")
    public String ajax(){
        return "/demo/ajax";
    }

    //    /s?current=1&limit=10
    @RequestMapping(path = "/s", method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
            @RequestParam(name = "current", required = false, defaultValue = "1") int current,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit
    ) {
        System.out.println(current);
        System.out.println(limit);
        User user = new User();
        user.setUsername("小李");
        user.setPassword("123");
        user.setSalts("123");
        user.setEmail("yui");
        System.out.println("收到前端发来请求！！！");
        String s = JSON.toJSONString(user);
        return s;
    }

    //

    //    请求方式2
//    /ss/765
    @RequestMapping(path = "/ss/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String demo2(@PathVariable("id") int id) {
        System.out.println(id);
        return "a student";
    }

    //    post请求
    @RequestMapping(path = "/student", method = RequestMethod.POST)
    public String saveStudent(String name, String age) {
        System.out.println("====有人访问了！！！=========");
        System.out.println("name:" + name + ",age" + age);
        return "/demo/success";
    }

    //    响应HTML数据
    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name", "liming");
        modelAndView.addObject("age", "23");
        modelAndView.setViewName("/demo/view");
        return modelAndView;
    }

    //    简写方式
    @RequestMapping("/v2")
    public String toV2(Model model) {
        model.addAttribute("fruit", "apple");
        return "/demo/v2";
    }

    @RequestMapping("/demo2")
    public String demo2(HashMap<String, Object> map) {
        String[] names = {"小白", "小李", "小红"};
        String name = "liming";
        //HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("names", names);
        //model.addAttribute("map", map);
        return "/demo/demo2";
    }

    @RequestMapping("/demo3")
    public String demo3(HashMap<String, Object> map, Model model) {
        User user = userServiceDao.selectById(2);
        map.put("user", user);
        //查找出一定的帖子
        List<DiscussPost> list = discussPostServiceDao.selectDiscussPosts(0, 0, 5);
        //定义一个map的结合，将用户的帖子信息和用户信息，经过封装后，发送给主页面
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post : list) {
                HashMap<String, Object> map1 = new HashMap<>();
                map1.put("post", post);
                User use1 = userServiceDao.selectById(post.getUserId());
                map1.put("user", user);
                discussPosts.add(map1);
                System.out.println(map);
            }
        }
        System.out.println(discussPosts);
        model.addAttribute("discussPost", discussPosts);
        return "/demo/demo2";

    }

    /**
     * 测试cookie的使用
     */
    @RequestMapping(path = "/cookie/set", method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response) {
    //    创建Cookie
        Cookie name = new Cookie("name", "liming)");
    //    设置cookie生效范围
        name.setPath("/community/cookie/get");
    //    创建cookie声明周期
        name.setMaxAge(60 * 10);
    //    发送Cookie
        response.addCookie(name);
        return "set cookie ok!!!";
    }

    @RequestMapping(path = "/cookie/get", method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("name") String name) {
        String s = "获取到Cookie的值是" + name + "";
        return s;
    }

    /**
     * 测试session作用
     */
    @RequestMapping(path = "/session/set",method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session){
        session.setAttribute("name", "缘分锝天空");
        return "session has set!";
    }
    @RequestMapping(path = "/session/get",method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session){
        String str = "拿到session中的name：" + session.getAttribute("name");
        return str;
    }

}
