package com.lys.community.controller;

import com.lys.community.entity.DiscussPost;
import com.lys.community.entity.Page;
import com.lys.community.entity.User;
import com.lys.community.service.DiscussPostServiceDao;
import com.lys.community.service.UserServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
public class HomeController {
    @Autowired
    private UserServiceDao userServiceDao;

    @Autowired
    private DiscussPostServiceDao discussPostServiceDao;

    /**
     *  返回首页
     * @param model 传送体
     * @param page  页数
     * @return
     */
    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String index(Model model, Page page, HttpSession session) {
        page.setRows(discussPostServiceDao.selectDiscussPostRows(0));
        page.setPath("/index");
        //查找出一定的帖子
        List<DiscussPost> list = discussPostServiceDao.selectDiscussPosts(0, page.getOffset(), page.getLimit());
        //定义一个map的结合，将用户的帖子信息和用户信息，经过封装后，发送给主页面
        List<Map< String,Object>> discussPosts = new ArrayList<>();
        System.out.println("评论有：");
        if (list != null) {
            for (DiscussPost post : list) {
                System.out.println(post);
                HashMap<String, Object> map = new HashMap<>();
                map.put("post", post);
                User user = userServiceDao.selectById(post.getUserId());
                map.put("user", user);
                discussPosts.add(map);
            }
        }
/*        String user_id = (String) session.getAttribute("user_id");
        User user1 = userServiceDao.selectById(Integer.parseInt(user_id));
        model.addAttribute("user", user1);*/
        model.addAttribute("discussPost", discussPosts);
        model.addAttribute("page", page);
        return "index";
    }

    /**
     * 跳转设置页
     */
    @RequestMapping(path = "/toSetting",method = RequestMethod.GET)
    public String toSetting(){
        return "/html/setting";
    }

    /**
     * 跳转到小黑屋页面
     */
    @RequestMapping(path = "/toBalck",method = RequestMethod.GET)
    public String toBlack(){
        return "/html/blank_home";
    }
}
