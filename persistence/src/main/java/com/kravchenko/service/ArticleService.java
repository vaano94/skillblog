package com.kravchenko.service;

import com.kravchenko.dao.ArticleDao;
import com.kravchenko.domain.Article;
import com.kravchenko.domain.Tag;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Created by john on 5/1/17.
 */
@Component
public class ArticleService {


    @Autowired
    private ArticleDao dao;

    @Autowired
    private SessionFactory sessionFactory;

//    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    @Transactional
    public void saveOrUpdateArticle(Article article) {
        dao.save(article);
    }

    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = true)
    public Article getArticleById(Long id) {
        return dao.findById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteArticle(long articleId) {
        dao.delete(articleId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteArticle(Article article) {
        dao.delete(article);
    }

    @Transactional
    public List getArticlePerPage(String tag, int offset, int pageSize) {
        Criteria criteria = sessionFactory.openSession().createCriteria(Article.class);
        if (tag != null) {
            Criteria alias = criteria.createAlias("tags", "tags");
            criteria.add(Restrictions.eq("tags.tagName", tag));
        }
        if (pageSize != 0) {
            criteria.setMaxResults(pageSize);
        }
        if (offset != 0) {
            criteria.setFirstResult(offset);
        }
        return criteria.list();
    }

}
