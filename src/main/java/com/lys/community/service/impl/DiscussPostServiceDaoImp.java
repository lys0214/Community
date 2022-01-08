package com.lys.community.service.impl;

import com.lys.community.dao.DiscussPostDao;
import com.lys.community.entity.DiscussPost;
import com.lys.community.service.DiscussPostServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class DiscussPostServiceDaoImp implements DiscussPostServiceDao {
    @Autowired
    private DiscussPostDao discussPostDao;
    @Override
    public List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit) {
        return discussPostDao.selectDiscussPosts(userId, offset, limit);
    }

    @Override
    public List<DiscussPost> getAll() {
        return discussPostDao.getAll();
    }
    @Override
    public int selectDiscussPostRows(int userId) {
        return discussPostDao.selectDiscussPostRows(userId);
    }

    @Override
    public int insertDiscussPost(DiscussPost discussPost) {
        if (discussPost == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        //过滤敏感词

        //转义
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
        return discussPostDao.insertDiscussPost(discussPost);
    }

    @Override
    public DiscussPost selectDiscussPostById(int id) {
        return discussPostDao.selectDiscussPostById(id);
    }

}
