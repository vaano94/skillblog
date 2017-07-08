package com.kravchenko.dao;

import com.kravchenko.domain.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by john on 5/2/17.
 */
@Component
@Transactional
public interface TagDao extends CrudRepository<Tag, Long> {

    @Query(value = "SELECT * FROM tag t INNER JOIN article a ON a.article_id = t.article_id WHERE tag_name LIKE %?1%", nativeQuery = true)
    public List<Tag> getTagsByArticleContaining(String tex);


}
