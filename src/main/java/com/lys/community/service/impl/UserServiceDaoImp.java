package com.lys.community.service.impl;

import com.lys.community.dao.LoginTicketDao;
import com.lys.community.dao.UserDao;
import com.lys.community.entity.LoginTicket;
import com.lys.community.entity.User;
import com.lys.community.service.UserServiceDao;
import com.lys.community.utils.CommunityConstant;
import com.lys.community.utils.CommunityUtils;
import com.lys.community.utils.MailClient;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserServiceDaoImp implements UserServiceDao,CommunityConstant {

    @Autowired
     UserDao userDao;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LoginTicketDao loginTicketDao;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    public User selectById(int id) {
        return userDao.selectById(id);
    }

    @Override
    public User selectByName(String userName) {
        return userDao.selectByName(userName);
    }

    @Override
    public User selectByEmail(String email) {
        return userDao.selectByEmail(email);
    }

    @Override
    public int insertUser(User user) {
        return userDao.insertUser(user);
    }

    @Override
    public int updateStatus(int id, int status) {
        return userDao.updateStatus(id, status);
    }

    @Override
    public int updateHeader(int id, String headerUrl) {
        return userDao.updateHeader(id, headerUrl);
    }

    @Override
    public int updatePassword(int id, String password) {
        return userDao.updatePassword(id, password);
    }

    @Override
    public List<User> selectUsers() {
        return userDao.selectUsers();
    }

    //    注册用户信息
    public Map<String, String> register(User user) {
        HashMap<String, String> map = new HashMap<>();
        // 判断用户传来的信息是否为空
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }
        if (StringUtils.isNullOrEmpty(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isNullOrEmpty(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        if (StringUtils.isNullOrEmpty(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }

        //判断用户名是否可用
        User user1 = userDao.selectByName(user.getUsername());
        if (user1 != null) {
            map.put("usernameMsg", "该账号已经存在");
            return map;
        }

        //判断密码是否够8位
        if (user.getPassword().length() < 8) {
            map.put("passwordMsg", "密码长度不能小于8位");
            return map;
        }
        //判断邮箱是否可用
        user1 = userDao.selectByEmail(user.getEmail());
        if (user1 != null) {
            map.put("emailMsg", "邮箱已经被注册！");
            return map;
        }

        //注册用户
        //获取盐值，并截取5位
        user.setSalts(CommunityUtils.getUUID().substring(0, 5));
        //给用户密码加密
        user.setPassword(CommunityUtils.md5(user.getPassword()+user.getSalts()));
        //默认为普通用户
        user.setType(0);
        //默认未激活
        user.setStatus(1);
        //为新用户生成随机激活码
        user.setActivationCode(CommunityUtils.getUUID());
        System.out.println("激活码是："+user.getActivationCode());
        //获取随机头像
        int randomNum = new Random().nextInt(1000);
        String headerUrl = "http://images.nowcoder.com/head/" + randomNum + "t.png";
        user.setHeaderUrl(headerUrl);
        //保存用户到数据库
        userDao.insertUser(user);
        //发送激活邮件
        /*Context context=new Context();
        //获取用户邮箱号
        context.setVariable("email", user.getEmail());
        //激活链接
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        //利用thymeleaf模板引擎生成邮件
        String process = templateEngine.process("/html/activation", context);
        System.out.println("生成的邮件如下：");
        System.out.println(process);
        mailClient.sendMail(user.getEmail(), "激活账号", process);
        System.out.println("邮件已经发送给注册用户");*/
        return map;
    }

    //    激活
    public int activation(int userId, String code) {
        User user = userDao.selectById(userId);
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;//重复激活
        } else if (user.getActivationCode().equals(code)) {
            userDao.updateStatus(userId, 1);
            return ACTIVATION_SUCCESSFUL;
        } else {
            return ACTIVATION_FAILURE;
        }
    }

    /**
     * 登录验证
     * @param username  用户名
     * @param password  密码
     * @param expireSeconds 过期时间
     * @return
     */
    public Map<String, Object> login(String username, String password, int expireSeconds) {
        HashMap<String, Object> map = new HashMap<>();
    //    空值处理
        if (StringUtils.isNullOrEmpty(username)) {
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isNullOrEmpty(password)) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
    //    验证账号是否存在
        User user = userDao.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "该账号不存在");
            return map;
        }
    //    验证用户是否激活
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "该账号未激活");
            return map;
        }
    //    验证密码是否正确
        password = CommunityUtils.md5(password + user.getSalts());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码不正确");
            return map;
        }

    //    生成登录验证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserid((user.getId()));
        loginTicket.setTicket(CommunityUtils.getUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expireSeconds * 1000));
    //    保存登录凭证
        loginTicketDao.insertTicket(loginTicket);

    //    将登录凭证带走
        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    /**
     * 退出登录，即将登录凭据中的status设置为1，登录时将status设置为0
     */
    public void logout(String ticket) {
        loginTicketDao.updateTicket(ticket, 1);
    }

    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketDao.selectByTicket(ticket);
    }




    public Map<String,Object> getCode(String email) {
        Map<String, Object> map = new HashMap<>();
        if (email == null) {
            map.put("emailMsg", "请先填写邮箱");
            return map;
        }else {
        //    发送邮件
            System.out.println("邮件已经发送！");
        }
        return null;
    }

    /**
     * 获取各个时间段的增长人数
     */
    public Map<String ,Object> getCount(){
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
        HashMap<String, Object> map = new HashMap<>();
        map.put("time", time);
        map.put("count", count);
        return map;
    }
}
