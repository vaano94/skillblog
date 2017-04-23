package com.kravchenko.database;

import com.kravchenko.dao.ArticleDao;
import com.kravchenko.domain.Article;
import com.kravchenko.domain.Expertise;
import com.kravchenko.domain.Tag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
@RunWith(SpringRunner.class)
public class ArticleTest {

    @Autowired
    ArticleDao articleDao;

    @Test
    public void testSaveArticle() {
        Article article = new Article();
        article.setAuthor("ivan");
        article.setContent("21233");
        article.setBeginDate(new Date());
        article.setUpdateDate(new Date());

//        List<Tag> tags = new ArrayList<Tag>(){{
//            add(new Tag("hi"));
//            add(new Tag("hello"));
//            add(new Tag("ivan"));
//        }};
//        article.setTags(tags);

        List<Expertise> expertise = new ArrayList<Expertise>(){{
            add(new Expertise("hi"));
            add(new Expertise("hello"));
            add(new Expertise("ivan"));
        }};
        article.setArea(expertise);

        Article save = articleDao.save(article);

        Article found = articleDao.findById(save.getId());
        Assert.assertEquals(save.getId(), found.getId());

    }

    @Test
    @Sql("../sql_queries/article.sql")
    public void findByid() {

    }


}
