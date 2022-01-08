package com.lys.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void test1(){
        String redisKey = "test:count";
        //redisTemplate.opsForValue().set(redisKey, 1);
        System.out.println(redisTemplate.opsForValue().get(redisKey));
        System.out.println(redisTemplate.opsForValue().get("test:count"));
        //System.out.println(redisTemplate.opsForValue().increment(redisKey));
        //System.out.println(redisTemplate.opsForValue().get(redisKey));
        //System.out.println(redisTemplate.opsForValue().get("name1"));
    }

    @Test
    public void testSets(){
        String rediskey = "test:roles";
        redisTemplate.opsForSet().add(rediskey, "a1", "a2", "a3");
        System.out.println(redisTemplate.opsForSet().size(rediskey));
        System.out.println(redisTemplate.opsForSet().pop(rediskey
        ));
        System.out.println(redisTemplate.opsForSet().members(rediskey));
    }

    @Test
    public void testTransactionTemplate(){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                //返回运算结果
                return operations.exec();
            }
        });
    }
}
