package com.kravchenko.domain;

import javax.persistence.*;

/**
 * Created by john on 4/23/17.
 */
@Entity
@Table
public class Expertise {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String areOfExpertise;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    public Expertise(String areOfExpertise) {
        this.areOfExpertise = areOfExpertise;
    }

    public String getAreOfExpertise() {
        return areOfExpertise;
    }

    public void setAreOfExpertise(String areOfExpertise) {
        this.areOfExpertise = areOfExpertise;
    }
}
