package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.epam.esm.dao.exception.DaoException;
import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.dao.util.PersistenceUtil;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.util.List;

@Repository
public class HibernateTagDaoImpl implements TagDao {

    private final PersistenceUtil<Tag> persistenceUtil;

    private static final String GET_TAG_BY_NAME = "SELECT t FROM Tag t WHERE t.name=:name";
    private static final String GET_ALL_TAGS = "SELECT t FROM Tag t ";
    public static final String GET_TAG_COUNT = "SELECT count(t.id) FROM Tag t";

    @Autowired
    public HibernateTagDaoImpl(PersistenceUtil<Tag> persistenceUtil) {
        this.persistenceUtil = persistenceUtil;
    }

    @PostConstruct
    private void init() {
        persistenceUtil.setType(Tag.class);
    }

    @Override
    public Tag getTag(String name) throws DaoException {
        try {
            return persistenceUtil.getModelByName(GET_TAG_BY_NAME, name);
        } catch (NoResultException ex) {
            throw new DaoException(String.format("Failed to get tag with name = {%s}", name),
                ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public Tag getTag(int id) {
        return persistenceUtil.getModelById(id);
    }

    @Override
    public List<Tag> getAllTags() {
        return persistenceUtil.getAllModels(GET_ALL_TAGS);
    }

    @Override
    public List<Tag> getAllTagsByPage(int page, int size, SortType type, SortBy by) throws DaoException {
        if (!by.equals(SortBy.NAME)) {
            throw new DaoException(String.format("Failed to sort tags with {%s}.", by),
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
        return persistenceUtil.getAllModelsByPage(GET_ALL_TAGS, page, size, type, by);
    }

    @Override
    public int getLastPage(int size) {
        return persistenceUtil.getLastPage(GET_TAG_COUNT, size);
    }

    @Override
    public int addTag(Tag tag) throws DaoException {
        try{
            return persistenceUtil.addModel(tag);
        } catch (PersistenceException e) {
            throw new DaoException(String.format("Failed to add tag with name = {%s}.", tag.getName()),
                    ErrorCodeEnum.FAILED_TO_DELETE_TAG);
        }
    }

    @Override
    public boolean deleteTag(int tagId) {
        try {
            persistenceUtil.deleteModel(tagId);
            return true;
        } catch (NoResultException | IllegalArgumentException e) {
            return false;
        }
    }
}
