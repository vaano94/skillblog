package com.kravchenko.dao;

import com.kravchenko.domain.Article;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * Created by john on 4/20/17.
 */
@Component
@Transactional
public interface ArticleDao extends CrudRepository<Article, Long> {

//    @PersistenceContext
//    private EntityManager entityManager;

//    public void save(Article article) {
//        entityManager.persist(article);
//    }
//
//    public void update(Article article) {
//        entityManager.merge(article);
//    }
//
//    public Article getById(long id) {
//        return entityManager.find(Article.class, id);
//

    public Article findById(long id);


}