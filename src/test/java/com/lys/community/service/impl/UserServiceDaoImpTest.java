package com.lys.community.service.impl;

import com.lys.community.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;


@SpringBootTest
class UserServiceDaoImpTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserServiceDaoImp userServiceDaoImp;

    @Test
    void activation() {
        int activation = userServiceDaoImp.activation(26236, "5a6e5434bb51476dae09dd305e324b6e");
        System.out.println(activation);
    }

    /**
     *测试登录凭证
     */
    @Test
    public void test1() {
        Map<String, Object> map = userServiceDaoImp.login("liming", "123", 30);
        System.out.println(map);
    }


}