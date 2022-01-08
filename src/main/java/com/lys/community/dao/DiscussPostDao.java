package com.lys.community.dao;

import com.lys.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DiscussPostDao {
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    List<DiscussPost> getAll();

    int selectDiscussPostRows(int userId);


    int insertDiscussPost(DiscussPost discus);

    @Select("select * from discuss_post where id=#{id}")
    DiscussPost selectDiscussPostById(int id);

}
