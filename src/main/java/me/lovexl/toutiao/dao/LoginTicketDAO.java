package me.lovexl.toutiao.dao;

import me.lovexl.toutiao.model.LoginTicket;
import me.lovexl.toutiao.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface LoginTicketDAO {
    String TABLE_NAME = "login_ticket";
    String INSERT_FIELDS = "user_id,expired,ticket,status";
    String SELECT_FIELDS = "id,"+ INSERT_FIELDS;
    @Insert({"insert into "+TABLE_NAME+" ("+ INSERT_FIELDS+" )"+
            "values"+ "(#{userId},#{expired},#{ticket},#{status})"})
    int addTicket(LoginTicket loginTicket);

    @Select({"select "+SELECT_FIELDS+" from "+TABLE_NAME
            +" where ticket= #{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Update({"update ",TABLE_NAME, " set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket") String ticket,@Param("status") int status);
}
