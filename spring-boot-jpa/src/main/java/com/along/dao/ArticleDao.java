package com.along.dao;

import com.along.model.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author along
 * @Date 2019/1/9 14:13
 */
public interface ArticleDao extends JpaRepository<Article, String> {

}
