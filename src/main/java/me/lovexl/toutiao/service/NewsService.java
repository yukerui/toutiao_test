package me.lovexl.toutiao.service;

import me.lovexl.toutiao.ToutiaoApplication;
import me.lovexl.toutiao.dao.NewsDAO;
import me.lovexl.toutiao.model.News;
import me.lovexl.toutiao.util.toutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.channels.MulticastChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;
    //获得最新的limit条新闻
    public List<News> getLastNews(int UserId,int offset,int limit) {
        return newsDAO.selectByUserIdAndOffset(UserId, offset, limit);
    }
    //
    public News getById(int newsId){
        return newsDAO.selectByUserId(newsId);
    }
    public int addNews(News news){
        newsDAO.addNews(news);
        return news.getId();
    }
    //将上传的图片保存到指定位置
    public String saveImage(MultipartFile file) throws IOException {
        int doPos = file.getOriginalFilename().lastIndexOf(".");
        if(doPos<0){
            return null;
        }
        String fileExt=file.getOriginalFilename().substring(doPos+1).toLowerCase();
        if(!toutiaoUtil.isFileAllowed(fileExt)){
            return null;
        }
        //将所有上传的图片随机生成一个名字
        String fileName = UUID.randomUUID().toString().replaceAll("-","")+"."+fileExt;
        //将图片保存到本地
        Files.copy(file.getInputStream(),new File(toutiaoUtil.IMAGE_DIR+fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        return toutiaoUtil.TOUTIAO_DOMAIN+"/image?name="+fileName;
    }
}
