package com.epam.esm.dao;

import com.epam.esm.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface provides with ability to
 * transfer {@code Tag} in and out
 * of data source.
 *
 * @author Aleksey Sayarkin
 */
public interface TagRepository extends JpaRepository<Tag, Integer>, CustomizedTagRepository {

    Tag getTagByName(String name);
}
