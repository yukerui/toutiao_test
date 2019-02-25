package me.lovexl.toutiao.controller;

import me.lovexl.toutiao.service.UserService;
import me.lovexl.toutiao.util.toutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    UserService userService;

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @RequestMapping(value = "/reg/",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "remember",defaultValue = "0") int remember,
                      HttpServletResponse response){
        try{
            Map<String,Object> map = userService.register(username, password);
            if (map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(remember>0){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                return toutiaoUtil.getJSONString(0,"注册成功");
            }
            else{
                return toutiaoUtil.getJSONString(1,map);
            }
        }
        catch (Exception e){
            logger.error("注册异常"+e.getMessage());
            return toutiaoUtil.getJSONString(1,"注册异常");
        }
    }


    @RequestMapping(value = {"/login"}, method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                         @RequestParam(value = "remember",defaultValue = "0") int remember,
                         HttpServletResponse response){
        try{
            Map<String,Object> map = userService.login(username,password);
            if (map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(remember>0){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                return toutiaoUtil.getJSONString(0,"登陆成功");
            }
            else{
                return toutiaoUtil.getJSONString(1,map);
            }
        }
        catch (Exception e){
            logger.error("登陆失败"+e.getMessage());
            return toutiaoUtil.getJSONString(1,"登陆失败");
        }
    }
    @RequestMapping(value = {"/logout"},method = {RequestMethod.GET,RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket,Model model){
//        HomeController homeController= new HomeController();
//        model.addAttribute("vos",homeController.getNews(0,0,10));
        userService.logout(ticket);
        return "redirect:/";
    }
    @RequestMapping(value = {"/isLogin"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String isLogin(){
        return userService.isLogin();
    }

}
