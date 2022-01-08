package com.lys.community;

import com.alibaba.fastjson.JSON;
import com.lys.community.dao.UserDao;
import com.lys.community.entity.User;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

@SpringBootTest
public class UserTest {
    @Autowired
    private UserDao userDao;
    @Test
    public void test1() {
        User user = userDao.selectById(1);
        System.out.println(user);

        /*User liming = userDao.selectByName("liming");
        System.out.println(liming);

        User user1 = userDao.selectByEmail("123@qq.com");
        System.out.println(user1);*/
    }

    @Test
    public void test2() {
        User user = new User();
        user.setUsername("小李");
        user.setPassword("123");
        user.setSalts("123");
        user.setEmail("yui");
        System.out.println(user);
        System.out.println(JSON.toJSONString(user));
        System.out.println(23);
    }

    @Test
    public void test3(){
        //System.out.println(userDao.updatePassword(1, "321"));
        //System.out.println(userDao.updateStatus(1, 1));
        System.out.println(userDao.updateHeader(1, "http://mywork.img"));
    }

    @Test
    public void test4(){
        ArrayList<String> time = new ArrayList<>();
        ArrayList<Long> count = new ArrayList<>();
        List<Map<String, Object>> list = userDao.getCount();
        for (Map<String, Object> stringObjectMap : list) {
            Timestamp time1 = (Timestamp) stringObjectMap.get("time");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String format1 = format.format(time1);
            time.add(format1);
            count.add((long) stringObjectMap.get("count"));
        }
        System.out.println(time);
        System.out.println(count);
    }

    /**
     * 测试获取用户表的所有信息
     */
    @Test
    public void test5(){
        List<User> users = userDao.selectUsers();
        for (User user : users) {
            System.out.println(user);
        }
    }






}
