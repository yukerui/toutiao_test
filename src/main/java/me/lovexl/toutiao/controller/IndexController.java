package me.lovexl.toutiao.controller;

import me.lovexl.toutiao.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
//    @RequestMapping(path = {"/","/index"})
//    @ResponseBody
//    public String index(HttpSession session){
//        logger.info("Visit Index");
//        return "hello world"+session.getAttribute("msg");
//    }
    @RequestMapping(value = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value="type",defaultValue = "1") int type,
                          @RequestParam(value="key",defaultValue = "lovexl") String key){
        return String.format("GID{%s},UID{%d},TYPE{%d},KEY{%s}",groupId,userId,type,key);
    }

    @RequestMapping(path = {"/name"})
    public String news(Model model){
        model.addAttribute("name","vv1");
        List<String> colors = Arrays.asList(new String[] {"RED","GREEN","BLUE"});
        Map<String,String> map = new HashMap<String, String>();
        for(int i=0;i<4;i++){
            map.put(String.valueOf(i),String.valueOf(i*i));
        }
        model.addAttribute("colors",colors);
        model.addAttribute("map",map);
        model.addAttribute("user", new User("Jim"));
        return "news";
    }
    @RequestMapping("/request")
    @ResponseBody
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session)
    {
        StringBuilder stringBuilder = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            stringBuilder.append(name+ ":" +request.getHeader(name)+"<br>");
        }
        for(Cookie cookie : request.getCookies()){
            stringBuilder.append("Cookie:");
            stringBuilder.append(cookie.getName());
            stringBuilder.append(",");
            stringBuilder.append(cookie.getValue());
            stringBuilder.append("<br>");
        }
        return stringBuilder.toString();
    }
    @RequestMapping("/redirect/{code}")
    public String redirect(@PathVariable("code") int code,
                           HttpSession session){
        /*
        RedirectView red = new RedirectView("/",true);
        if(code == 301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return red;*/
        session.setAttribute("msg","Jump from lovexl");
        return "redirect:/";
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value="key",required = false) String key){
        if("admin".equals(key)){
            return "hello admin";
        }
        throw new IllegalArgumentException("Wrong key");
    }
    //错误处理
    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){
        return "error:" + e.getMessage();
    }
}
