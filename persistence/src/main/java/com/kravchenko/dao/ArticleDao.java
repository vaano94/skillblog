package com.kravchenko.dao;

import com.kravchenko.domain.Article;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by john on 4/20/17.
 */
@Component
@Transactional
public interface ArticleDao extends CrudRepository<Article, Long> {

    public Article findById(long id);

    @Async
    @Query(value = "SELECT a from ARTICLE a WHERE a.author=:author")
    public List<Article> getAuthor(@Param("author") String author);


}