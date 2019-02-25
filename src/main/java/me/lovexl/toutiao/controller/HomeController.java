package me.lovexl.toutiao.controller;

import me.lovexl.New;
import me.lovexl.toutiao.model.HostHolder;
import me.lovexl.toutiao.model.News;
import me.lovexl.toutiao.model.ViewObject;
import me.lovexl.toutiao.service.NewsService;
import me.lovexl.toutiao.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    NewsService newsService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;

    public List<ViewObject> getNews(int UserId,int offset,int limit){
        List<News> newsList = newsService.getLastNews(UserId,offset, limit);
        List<ViewObject> vos =new ArrayList<>();
        for(News news:newsList){
            ViewObject vo = new ViewObject();
            vo.set("news",news);
            vo.set("user",userService.getUser(news.getUserId()));
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path={"/","/index"},method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model){
        //return "index";
        model.addAttribute("vos",getNews(0,0,10));
//        return "test";
        return "index";
    }
    @RequestMapping(path={"/user/{userId}"},method = {RequestMethod.GET, RequestMethod.POST})
    public String  userIndex(Model model,
                                  @PathVariable("userId") int userId,
                                  @RequestParam(value = "pop",defaultValue = "0")int pop){
        //return "index";
        model.addAttribute("vos",getNews(userId,0,10));
//        model.addAttribute("pop",pop);
//        return "test";
        return "index";
    }


}
