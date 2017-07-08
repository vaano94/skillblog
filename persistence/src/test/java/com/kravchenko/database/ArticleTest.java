package com.kravchenko.database;

import com.kravchenko.dao.ArticleDao;
import com.kravchenko.dao.TagDao;
import com.kravchenko.domain.Article;
import com.kravchenko.domain.Expertise;
import com.kravchenko.domain.Tag;
import com.kravchenko.service.ArticleService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Autowired
    TagDao tagDao;

    @Autowired
    ArticleService articleService;

    @Test
    public void testSaveArticle() {
        Article article = new Article();
        article.setAuthor("ivan");
        article.setContent("21233");
        article.setBeginDate(new Date());
        article.setUpdateDate(new Date());

        List<Tag> tags = new ArrayList<Tag>(){{
            add(new Tag("hi",article));
            add(new Tag("hello",article));
            add(new Tag("ivan",article));
        }};
        article.setTags(tags);

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

//    @Test
//    @Sql("../sql_queries/article.sql")
//    public void findByid() {
//
//    }

    @Test
    public void testGetArticleByAuthor() {
        Article article = new Article();
        article.setAuthor("ivan");
        article.setContent("21233");
        article.setBeginDate(new Date());
        article.setUpdateDate(new Date());
        articleDao.save(article);

        List<Article> ivan = articleDao.getAuthor("ivan");

        Assert.assertNotNull(ivan);
    }

    @Test
    public void testGetArticleByTags() {
        Article article = new Article();
        article.setAuthor("ivan");
        article.setContent("21233");
        article.setBeginDate(new Date());
        article.setUpdateDate(new Date());
        List<Tag> tags = new ArrayList<Tag>(){{
            add(new Tag("hi",article));
            add(new Tag("hello",article));
            add(new Tag("ivan",article));
        }};
        article.setTags(tags);

        List<Expertise> expertise = new ArrayList<Expertise>(){{
            add(new Expertise("hi"));
            add(new Expertise("hello"));
            add(new Expertise("ivan"));
        }};
        article.setArea(expertise);
        articleDao.save(article);


        Article article1 = new Article();
        article1.setAuthor("maiv");
        article1.setContent("33223");
        article1.setBeginDate(new Date());
        article1.setUpdateDate(new Date());
        List<Tag> tags1 = new ArrayList<Tag>(){{
            add(new Tag("woop",article1));
            add(new Tag("hello",article1));
            add(new Tag("bobo",article1));
        }};
        article.setTags(tags);
        article1.setTags(new ArrayList<>());
        articleDao.save(article1);


        Article byId = articleDao.findById(1);

//        List<Tag> list = tagDao.getabc();
//        Assert.assertNotNull(list);
//        Assert.assertEquals(list.size(), 3)
        ;

//        List<Article> articles = articleDao.getByTag("two");

//
//        Assert.assertNotNull(articles);
//
//        System.out.println( articles);
    }

    @Test
    public void testGetCustomRequet() {
        testGetArticleByAuthor();

        testGetArticleByTags();

        List<Tag> woop = tagDao.getTagsByArticleContaining("woop");
        woop.forEach((item) -> System.out.println(item.getTagName()));
    }


}
