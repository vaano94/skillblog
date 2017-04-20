package com.kravchenko.controller;

import com.kravchenko.domain.Article;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by john on 4/16/17.
 */
@RestController
public class HelloCtrl {

    @GetMapping(value = "/show/{id}")
    public Article hello(@PathVariable Integer id) {

        Article article = new Article();
        article.setAuthor("Ivan");
        article.setContent(String.valueOf(id));
        article.setBeginDate(new Date());

        return article;
    }

}
