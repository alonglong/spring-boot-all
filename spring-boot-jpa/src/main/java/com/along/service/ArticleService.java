package com.along.service;

import com.along.model.dto.ArticleDTO;
import com.along.model.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author along
 * @Date 2019/1/9 15:02
 */
public interface ArticleService {
    Article save(ArticleDTO articleDTO);

    List<Article> saveAll(List list);

    Page<Article> findAll(Pageable pageable);

    Article findByTitle(String name);

    Article findByTitleLike(String name);

    Article findByUserId(String userId);

    Article update(Article article);
}
