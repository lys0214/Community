package com.lys.community.controller;

import com.alibaba.fastjson.JSON;
import com.lys.community.entity.User;
import com.lys.community.service.DiscussPostServiceDao;
import com.lys.community.service.UserServiceDao;
import com.lys.community.service.impl.UserServiceDaoImp;
import com.lys.community.utils.CommunityUtils;
import com.lys.community.utils.HostHolder;
import com.mysql.cj.util.StringUtils;
import org.apache.coyote.Request;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;


@Controller
@CrossOrigin
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserServiceDao userServiceDao;

    @Autowired
    private UserServiceDaoImp userServiceDaoImp;

    @Autowired
    private DiscussPostServiceDao discussPostServiceDao;

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/upload",method = RequestMethod.POST)
    public String uploadHeader(MultipartFile file,Model model){
        System.out.println(file);
        //判断文件是否为空
        if (file == null) {
            model.addAttribute("error", "您还没有选择图片");
            return "/html/setting";
        }
    //    判断文件后缀名是否正确
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isNullOrEmpty(suffix)) {
            model.addAttribute("error", "文件格式不对!");
            return "/html/setting";
        }
    //    生成随机文件名
        fileName = CommunityUtils.getUUID() + suffix;
    //    确定文件存放路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            // 存储文件
            file.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败",e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常");
        }

    //    更新当前用户头像的访问路径(web访问路径)
        User user = hostHolder.getUser();
        String headUrl = domain + contextPath + "/header/" + fileName;
        userServiceDao.updateHeader(user.getId(), headUrl);

        return "redirect:/index";
    }

    @RequestMapping(path = "/header/{fileName}",method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName")String fileName, HttpServletResponse response) {
    //    服务器存放地址
        fileName=uploadPath+"/"+fileName;
    //    文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
    //  响应照片
        response.setContentType("image/" + suffix);
        try {
            FileInputStream fis = new FileInputStream(fileName);
            OutputStream os=response.getOutputStream();
        //    缓冲区
            byte[] buffer = new byte[1024];
            int b=0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("读取头像失败！");
        }
    }

    
    @RequestMapping(path = "/allUsers",method = RequestMethod.GET)
    @ResponseBody
    public String selectUsers(){
        List<User> users = userServiceDaoImp.selectUsers();
        return JSON.toJSONString(users);
    }



























    /**
     * 跳转添加帖子页
     *
     * @return
     */
    @RequestMapping(path = "/toAdd")
    public String toAdd() {
        return "/html/addDiscuss";
    }


    /**
     * 返回各个时间段的注册人数增长情况
     */
    @ResponseBody
    @RequestMapping(path = "/get_time_count", method = RequestMethod.GET)
    public String getCountByTime(){
        Map<String, Object> map = userServiceDaoImp.getCount();
        return JSON.toJSONString(map);
    }
}
