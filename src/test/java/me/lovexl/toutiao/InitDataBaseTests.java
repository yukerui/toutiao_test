package me.lovexl.toutiao;

import me.lovexl.toutiao.dao.LoginTicketDAO;
import me.lovexl.toutiao.dao.NewsDAO;
import me.lovexl.toutiao.dao.UserDAO;
import me.lovexl.toutiao.model.LoginTicket;
import me.lovexl.toutiao.model.News;
import me.lovexl.toutiao.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ToutiaoApplication.class)
@Sql("/init-schema.sql")

public class InitDataBaseTests {
  @Autowired
  private UserDAO userDAO;

  @Autowired
  private NewsDAO newsDAO;

  @Autowired
  private LoginTicketDAO loginTicketDAO;
    @Test
    public void contextLoads(){
        Random random = new Random();
        for(int i=0;i<11;i++) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(100)));
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);
            user.setPassword("bbyt0113");
            userDAO.updatePassword(user);
            Date date = new Date();
            LoginTicket ticket = new LoginTicket();
            ticket.setStatus(0);
            ticket.setUserId(i+1);
            date.setTime(date.getTime()+1000*3600*24);
            ticket.setExpired(date);
            ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
            loginTicketDAO.addTicket(ticket);

 //           loginTicketDAO.updateStatus(ticket.getTicket(),2);

            News news = new News();
            news.setId(i+1);
            news.setTitle(String.format("TITLE{%d}",i));
            news.setLink(String.format("lovexl.me/%d.html",i));
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(100)));
            news.setLikeCount(i+1);
            news.setCommentCount(i + 1);
            news.setUserId(i+1);
            Date nowDate = new Date();
            date.setTime(nowDate.getTime()-1000*3600*5*i);
            news.setCreatedDate(date);
            newsDAO.addNews(news);

        }

    }
}
