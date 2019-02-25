package me.lovexl.toutiao.interceptor;

import me.lovexl.toutiao.controller.LoginController;
import me.lovexl.toutiao.dao.LoginTicketDAO;
import me.lovexl.toutiao.dao.UserDAO;
import me.lovexl.toutiao.model.HostHolder;
import me.lovexl.toutiao.model.LoginTicket;
import me.lovexl.toutiao.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class PassportInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(PassportInterceptor.class);
    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket=null;
        if (httpServletRequest.getCookies()!=null){
            for(Cookie cookie:httpServletRequest.getCookies()){
                if(cookie.getName().equals("ticket")){
                    ticket=cookie.getValue();
                    break;
                }
            }
        }
        Date date=new Date();
        date.setTime(date.getTime());
        if(ticket!=null){
//            logger.info(ticket);
            LoginTicket loginTicket=loginTicketDAO.selectByTicket(ticket);
//            logger.info("ticket={}",loginTicket.getTicket());
//            logger.info("expired={}",loginTicket.getExpired());
//            logger.info("userId={}",loginTicket.getUserId());
//            logger.info("status={}",loginTicket.getStatus());
            if(loginTicket==null || loginTicket.getExpired().before(date) || loginTicket.getStatus()!=0){
                return true;
            }
            User user = userDAO.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if(modelAndView  !=null && hostHolder.getUser()!=null){
            //与model.addAttribute()类似
            modelAndView.addObject("user",hostHolder.getUser());
            modelAndView.setViewName("index");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
