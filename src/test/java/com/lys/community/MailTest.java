package com.lys.community;

import com.lys.community.utils.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
public class MailTest {
    @Autowired
    private MailClient mailClient;
    @Test
    public void test1() {
        mailClient.sendMail("1761724207@qq.com", "问候","你好啊");

    }

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testSend(){
        String[] qq = {"1761724207","864367467"};
        for (String s : qq) {
            Context context = new Context();
            String content = templateEngine.process("/mail/mail", context);
            mailClient.sendMail(s+"@qq.com", "测试SMTP服务", content);
        }
    }
}
