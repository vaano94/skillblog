package com.kravchenko.service;

import com.kravchenko.dao.ArticleDao;
import com.kravchenko.domain.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by john on 5/1/17.
 */
@Component
public class ArticleService {


    @Autowired
    private ArticleDao dao;

    @Transactional
    public void saveArticle(Article article) {
        dao.save(article);
    }

    public void updateArticle(Article article) {
        dao.save(article);
    }

    public Article getArticleById(Long id) {
        return dao.findById(id);
    }

}
