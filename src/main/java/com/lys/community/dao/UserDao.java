package com.lys.community.dao;

import com.lys.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserDao {
    //    通过id查询用户
    User selectById(int id);

    //    用过用户名查询用户
    User selectByName(String userName);

    // 通过邮箱查询用户
    User selectByEmail(String email);

    //   添加用户
    int insertUser(User user);

    //  更新激活状态
    int updateStatus(int id, int status);

    //  更新头像
    int updateHeader(int id, String headerUrl);

    //  修改用户密码
    int updatePassword(int id, String password);

//    返回每个时间段增长人数
    List<Map<String,Object>> getCount();

//    返回所有的用户信息
    List<User> selectUsers();

}
