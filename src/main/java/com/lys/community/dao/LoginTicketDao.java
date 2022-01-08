package com.lys.community.dao;

import com.lys.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LoginTicketDao {
    @Select({
            "select * from loginticket "
    })
    List<LoginTicket> selectAll();

    @Select({
            "select id,userid,ticket,status,expired ",
            "from loginticket where ticket=#{ticket}"
    })
    LoginTicket selectByTicket(String ticket);

    @Update({
            "update loginticket ",
            "set status=#{status} ",
            "where ticket=#{ticket}"
    })
    int updateTicket(String ticket, int status);

    @Insert({
            "insert into loginticket(id, userid, ticket, status, expired) ",
            "values (0,#{userid},#{ticket},#{status},current_date)"
    })
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertTicket(LoginTicket lTicket);
}
