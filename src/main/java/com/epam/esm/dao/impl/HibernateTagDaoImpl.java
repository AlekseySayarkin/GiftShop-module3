package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.request.TagRequestBody;
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

    private static final String GET_TAG_BY_NAME = "SELECT t FROM Tag t WHERE t.name=:name";
    private static final String GET_ALL_TAGS = "SELECT t FROM Tag t ";
    private static final String GET_TAG_COUNT = "SELECT count(t.id) FROM Tag t";

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
    public Tag getTagById(int id) {
        return persistenceService.getModelById(id);
    }

    @Override
    public List<Tag> getAllTags() {
        return persistenceService.getAllModels(GET_ALL_TAGS);
    }

    @Override
    public List<Tag> getAllTagsByPage(TagRequestBody requestBody, int page, int size) {
        return persistenceService.getAllModelsByPage(
                GET_ALL_TAGS, page, size, requestBody.getSortType(), requestBody.getSortBy());
    }

    @Override
    public int getLastPage(int size) {
        return persistenceService.getLastPage(GET_TAG_COUNT, size);
    }

    @Override
    public int addTag(Tag tag) throws PersistenceException {
        return persistenceService.addModel(tag);
    }

    @Override
    public boolean deleteTag(int tagId) {
        try {
            persistenceService.deleteModel(tagId);
            return true;
        } catch (NoResultException | IllegalArgumentException e) {
            return false;
        }
    }
}
