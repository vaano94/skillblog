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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Tag(String tagName) {
        this.tagName = tagName;
    }
}
