package com.kravchenko.database.database;

import com.kravchenko.dao.TagDao;
import com.kravchenko.domain.Article;
import com.kravchenko.domain.Tag;
import com.kravchenko.service.ArticleService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 6/4/17.
 */
@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
@RunWith(SpringRunner.class)
public class ArticleFetchTest {

    @Autowired
    ArticleService articleService;

    @Autowired
    TagDao tagDao;

    @Before
    public void fillMockData() {
        for (int i = 0; i < 20; i++) {
            Article article = new Article();
            article.setAuthor("abc");
            article.setBeginDate(new Date());
            article.setContent("laksdfoisdfjlke");
            article.setUpdateDate(new Date());
            List<Tag> tags = new ArrayList<Tag>(){{
                add(new Tag("hi",article));
                add(new Tag("hello",article));
                add(new Tag("ivan",article));
            }};
            article.setTags(tags);

            articleService.saveOrUpdateArticle(article);
        }
    }


    @Test
    public void testFetchArticleListDefault() {
        List amount = articleService.getArticlePerPage(null, 0, 0);
        Assert.assertEquals(amount.size(), 20);
    }

    @Test
    public void testFetchArticleListWithTag() {
        List amount = articleService.getArticlePerPage("hi", 0, 0);
        Assert.assertEquals(amount.size(), 20);
    }

    @Test
    public void testFetchArticleListWithOffset() {
        int ten = 10;
        List amount = articleService.getArticlePerPage(null, ten, 0);
        Assert.assertEquals(amount.size(), ten);
    }

    @Test
    public void testFetchArticleListWithPageSize() {
        int three = 3;
        List amount = articleService.getArticlePerPage(null, 0, three);
        Assert.assertEquals(amount.size(), three);
    }

    @Test
    public void testFetchArticleWithAllParams() {
        int offset = 3;
        List<Article> amount = (List<Article>) articleService.getArticlePerPage(null, offset, 5);
        Assert.assertEquals(amount.size(), 5);
        Assert.assertEquals(amount.get(0).getId(), (Long.valueOf(offset+1)));
        Assert.assertEquals(amount.get(1).getId(), (Long.valueOf(offset+2)));
    }

}
