package com.lys.community;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StringTest {
    /**
     * 字符串大小写转换
     */
    @Test
    public void test1(){
        String str = "abc123";
        System.out.println(str.toUpperCase());

    }
}
