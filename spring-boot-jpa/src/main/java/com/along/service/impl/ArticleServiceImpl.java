package com.along.service.impl;

import com.along.dao.ArticleDao;
import com.along.dao.UserDao;
import com.along.model.dto.ArticleDTO;
import com.along.model.entity.Article;
import com.along.model.entity.User;
import com.along.service.ArticleService;
import com.along.util.NullUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author along
 * @Date 2019/1/9 15:39
 */
@Service(value = "articleService")
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private ArticleDao articleDao;
    private UserDao userDao;

    @Autowired
    public ArticleServiceImpl(ArticleDao articleDao, UserDao userDao) {
        this.articleDao = articleDao;
        this.userDao = userDao;
    }

    @Override
    public Article save(ArticleDTO articleDTO) {
        Article article = new Article();
        //设置用户
        Optional<User> opt = userDao.findById(articleDTO.getUserId());
        if (!opt.isPresent()) {
            return null;
        }
        article.setUser(opt.get());
        //信息拷贝
        BeanUtils.copyProperties(articleDTO, article, NullUtils.getNullPropertyNames(articleDTO));
        //执行添加
        return articleDao.save(article);
    }

    @Override
    public List<Article> saveAll(List list) {
        return null;
    }

    @Override
    public Page<Article> findAll(Pageable pageable) {
        return articleDao.findAll(pageable);
    }

    @Override
    public Article findByTitle(String name) {
        return null;
    }

    @Override
    public Article findByTitleLike(String name) {
        return null;
    }

    @Override
    public Article findByUserId(String userId) {
        return null;
    }

    @Override
    public Article update(Article article) {
        return null;
    }
}
