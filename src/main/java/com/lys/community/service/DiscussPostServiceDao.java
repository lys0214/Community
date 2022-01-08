package com.lys.community.service;

import com.lys.community.entity.DiscussPost;

import java.util.List;

public interface DiscussPostServiceDao {
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    List<DiscussPost> getAll();

    int selectDiscussPostRows(int userId);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);

}
