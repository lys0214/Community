package com.lys.community;

import com.lys.community.dao.DiscussPostDao;
import com.lys.community.entity.DiscussPost;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DiscussPostTest {
    @Autowired
    private DiscussPostDao discussPostDao;

    @Test
    public void test1(){
        //全部通过
        //System.out.println(discussPostDao.selectDiscussPosts(0,1,10));
        //System.out.println(discussPostDao.getAll());
        //System.out.println(discussPostDao.selectDiscussPostRows(2));
        //DiscussPost post = new DiscussPost();
        //post.setUserId(2);
        //post.setTitle("nihao11");
        //post.setContent("这是测试内容");
        //discussPostDao.insertDiscussPost(post);
        DiscussPost post = discussPostDao.selectDiscussPostById(31);
        System.out.println(post);
    }
}
