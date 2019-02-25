package me.lovexl.toutiao.controller;

import me.lovexl.toutiao.dao.NewsDAO;
import me.lovexl.toutiao.model.HostHolder;
import me.lovexl.toutiao.model.News;
import me.lovexl.toutiao.service.NewsService;
import me.lovexl.toutiao.service.QiniuService;
import me.lovexl.toutiao.service.UserService;
import me.lovexl.toutiao.util.toutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

@Controller
public class NewsController {
    @Autowired
    private NewsDAO newsDAO;
    @Autowired
    private NewsService newsService;
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private QiniuService qiniuService;

    private final Logger logger = LoggerFactory.getLogger(NewsController.class);
    @RequestMapping(path={"/uploadImage/"},method = RequestMethod.POST)
    @ResponseBody
    //request的param
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        logger.info("file={}",file);
        try{
            String fileUrl = qiniuService.saveImage(file);
            if(fileUrl == null){
                return toutiaoUtil.getJSONString(1,"上传图片失败");
            }
            return toutiaoUtil.getJSONString(0,fileUrl);
        }catch (Exception e){
            logger.error("上传图片失败"+e.getMessage());
            return toutiaoUtil.getJSONString(1,"上传失败");
        }
    }

    @RequestMapping(path = {"/news/{newsId}"},method ={RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model){
        News news = newsService.getById(newsId);
        model.addAttribute("news",news);
        model.addAttribute("owner",userService.getUser(news.getUserId()));
        return "detail";
    }

    @RequestMapping(path = {"/image"},method = RequestMethod.GET)
    @ResponseBody
    public void getImage(@RequestParam("name")String imageName,
                           HttpServletResponse response){
        try {
            response.setContentType("/image/jpg");
            StreamUtils.copy(new FileInputStream(new File(toutiaoUtil.IMAGE_DIR
                    +imageName)),response.getOutputStream());
        }
        catch (Exception e){
            logger.error("读取图片错误"+e.getMessage());
        }
    }

    //发布帖子
    @RequestMapping(path = {"/addNews/"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image")MultipartFile image,
                        @RequestParam("title") String title,
                          @RequestParam("link") String link){
        try {
            News news = new News();
 //           logger.info("user:{}",hostHolder.getUser().getId());
            if(hostHolder.getUser()!=null){
                news.setUserId(hostHolder.getUser().getId());
            }else{
                //匿名id
                news.setUserId(3);
            }
            String image_url= qiniuService.saveImage(image);
            news.setImage(image_url);
            news.setTitle(title);
            news.setCreatedDate(new Date());
            news.setLink(link);
            newsService.addNews(news);
            return toutiaoUtil.getJSONString(0);
        }
        catch (Exception e){
            logger.error("添加资讯出错"+e.getMessage());
            return toutiaoUtil.getJSONString(1,"发布失败");
        }


    }
}
