package com.kravchenko.domain;

import javax.persistence.*;

/**
 * Created by john on 4/23/17.
 */
@Table
@Entity(name="tag")
public class Tag {


    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String tagName;

    @ManyToOne(fetch = FetchType.EAGER,cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "article_id")
    private Article article;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Tag(String tagName, Article article) {
        this.tagName = tagName;
        this.article = article;
    }

    public Tag(String name) {
        this.tagName = name;
    }

    public Tag() {
    }
}
