package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.request.TagSearchCriteria;
import com.epam.esm.dao.service.PersistenceService;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.util.List;

@Repository
public class HibernateTagDaoImpl implements TagDao {

    private final PersistenceService<Tag> persistenceService;

    private static final boolean ACTIVE_TAG = true;
    private static final boolean DELETED_TAG = false;

    private static final String GET_TAG_BY_NAME = "SELECT t FROM Tag t WHERE t.name=:name AND t.isActive=true ";
    private static final String GET_ALL_TAGS = "SELECT t FROM Tag t WHERE t.isActive=true ";
    private static final String GET_TAG_COUNT = "SELECT count(t.id) FROM Tag t WHERE t.isActive=true ";

    @Autowired
    public HibernateTagDaoImpl(PersistenceService<Tag> persistenceService) {
        this.persistenceService = persistenceService;
    }

    @PostConstruct
    private void init() {
        persistenceService.setType(Tag.class);
    }

    @Override
    public Tag getTagByName(String name) throws NoResultException {
        return persistenceService.getModelByName(GET_TAG_BY_NAME, name);
    }

    @Override
    public Tag getTagById(int tagId) {
        return persistenceService.getModelById(tagId);
    }

    @Override
    public List<Tag> getAllTagsByPage(TagSearchCriteria searchCriteria, int page, int size) {
        return persistenceService.getAllModelsByPage(
                GET_ALL_TAGS, page, size, searchCriteria.getSortType(), searchCriteria.getSortBy());
    }

    @Override
    public int getLastPage(int size) {
        return persistenceService.getLastPage(GET_TAG_COUNT, size);
    }

    @Override
    public Tag addTag(Tag tag) {
        tag.setActive(ACTIVE_TAG);
        return persistenceService.add(tag);
    }

    @Override
    public void deleteTagById(int tagId) {
        Tag tag = persistenceService.getModelById(tagId);
        if (tag == null) {
            throw new NoResultException("Failed to find tag to delete by id: " + tagId);
        }
        tag.setActive(DELETED_TAG);
        persistenceService.update(tag);
    }
}
