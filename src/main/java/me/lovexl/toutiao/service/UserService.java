package me.lovexl.toutiao.service;

import me.lovexl.toutiao.dao.LoginTicketDAO;
import me.lovexl.toutiao.dao.UserDAO;
import me.lovexl.toutiao.model.HostHolder;
import me.lovexl.toutiao.model.LoginTicket;
import me.lovexl.toutiao.model.User;
import me.lovexl.toutiao.util.toutiaoUtil;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    HostHolder hostHolder;

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    public Map<String,Object> register(String username,String password){
        Map<String,Object> map = new HashMap<String, Object>();
        if(StringUtils.isBlank(username)){
            map.put("msgname","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msgpasswd","密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if(user!=null){
            map.put("msg","用户名已经被注册");
            return map;
        }
        //以上是核心功能，可以添加其他
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(100)));
        user.setPassword(toutiaoUtil.getMd5(password+user.getSalt()));
        userDAO.addUser(user);

        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }


    //登陆方法
    public Map<String,Object> login(String username,String password){
        Map<String,Object> map = new HashMap<String, Object>();
        if(StringUtils.isBlank(username)){
            map.put("msgname","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msgpasswd","密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if(user.getName() == null){
            map.put("msg","用户名不存在");
            return map;
        }
        if(!toutiaoUtil.getMd5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msgpwd","密码不正确");
            return map;
        }
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        //以上是核心功能，可以添加其他
        return map;
    }

    public String isLogin(){
        if (hostHolder.getUser()==null){
            return "未登陆,为您跳转登陆界面";
        }
        else{
            return "user: "+ hostHolder.getUser().getName();
        }
    }

    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }

    public String addLoginTicket(int userId){
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime()+1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }
}
