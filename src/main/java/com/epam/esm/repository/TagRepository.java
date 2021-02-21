package com.epam.esm.repository;

import com.epam.esm.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * This interface provides with ability to
 * transfer {@code Tag} in and out
 * of data source.
 *
 * @author Aleksey Sayarkin
 */
public interface TagRepository extends JpaRepository<Tag, Integer> {

    @Override
    @Modifying
    @Query("update Tag t set t.isActive = false where t.id = :tagId")
    void deleteById(Integer tagId);
}
