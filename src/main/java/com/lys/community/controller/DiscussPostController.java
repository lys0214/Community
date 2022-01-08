package com.lys.community.controller;

import com.lys.community.dao.UserDao;
import com.lys.community.entity.Comment;
import com.lys.community.entity.DiscussPost;
import com.lys.community.entity.Page;
import com.lys.community.entity.User;
import com.lys.community.service.DiscussPostServiceDao;
import com.lys.community.service.UserServiceDao;
import com.lys.community.service.impl.CommentService;
import com.lys.community.service.impl.UserServiceDaoImp;
import com.lys.community.utils.CommunityConstant;
import com.lys.community.utils.CommunityUtils;
import com.lys.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
public class DiscussPostController implements CommunityConstant {
    @Autowired
    private DiscussPostServiceDao discussPostServiceDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserServiceDaoImp userServiceDaoImp;

    @Autowired
    private CommentService commentService;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtils.getJSONString(403, "您还没有登录");
        }
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        discussPostServiceDao.insertDiscussPost(post);
        return CommunityUtils.getJSONString(0, "发布成功");
    }

    //    点击某一条帖子详情
    @RequestMapping(path = "/detaill/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page) {
    //    找帖子
        DiscussPost post = discussPostServiceDao.selectDiscussPostById(discussPostId);
        model.addAttribute("post", post);
    //    找帖子的作者
        User user = userDao.selectById(post.getUserId());
        model.addAttribute("user", user);

    //     评论分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(post.getConmentCount());

        //评论列表
        List<Comment> commentList=commentService.findCommentsByEntity(ENTITY_TYPE_POST,post.getId(),page.getOffset(),page.getLimit());
        //评论显示vo列表
        List<Map<String,Object>> commentVoList=new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                Map<String, Object> commentVo = new HashMap<>();
                commentVo.put("comment", comment);//评论
            //    评论的作者
                commentVo.put("user", userDao.selectById(comment.getUserId()));
            //   回复列表
                List<Map<String,Object>> replayVoList = new ArrayList<>();


            }
        }

        return "/html/discussDetail";
    }


}
