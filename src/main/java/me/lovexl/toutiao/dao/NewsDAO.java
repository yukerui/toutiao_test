package me.lovexl.toutiao.dao;

import me.lovexl.toutiao.model.News;
import me.lovexl.toutiao.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface NewsDAO {
    String TABLE_NAME ="news";
    String INSERT_FIELDS = "title,link,image,like_count,comment_count,user_id,created_date";
    String SELECT_FIELDS = "id, "+ INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME, "(",INSERT_FIELDS,
            ")", "values", "(#{title},#{link},#{image},#{likeCount},#{commentCount},#{userId},#{createdDate})"})
    int addNews(News news);

    @Update({"update ",TABLE_NAME,"set comment_count=2"})
    void updateCommentCount(News news);

//    @Select({"select ",SELECT_FIELDS, "from ",TABLE_NAME,"where id=#{userId}" })
//    void selectNews(int user_id);
    @Select({"select ",SELECT_FIELDS," from",TABLE_NAME,
            " where user_id= #{userId}"})
    News selectByUserId(int userId);

    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offSet") int offSet,
                                        @Param("limit") int limit);
//     News selectById(int id);
}

