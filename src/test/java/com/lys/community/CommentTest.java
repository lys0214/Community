package com.lys.community;

import com.lys.community.dao.CommentDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommentTest {
    @Autowired
    private CommentDao commentDao;
    @Test
    void test1(){
        int i = commentDao.selectCountByEntity(2, 1);
        System.out.println(i);
        commentDao.selectCommentsByEntity(1, 1, 2, 1);
    }

    @Test
    void test2(){
        System.out.println(commentDao.selectCountByEntity(1, 1));
    }

    @Test
    void test3() {

    }



}

