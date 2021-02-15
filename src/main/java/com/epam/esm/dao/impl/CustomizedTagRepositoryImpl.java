package com.epam.esm.dao.impl;

import com.epam.esm.dao.CustomizedTagRepository;
import com.epam.esm.dao.request.TagSearchCriteria;
import com.epam.esm.dao.service.PersistenceService;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

@Repository
public class CustomizedTagRepositoryImpl implements CustomizedTagRepository {

    private final PersistenceService<Tag> persistenceService;

    private static final String GET_ALL_TAGS = "SELECT t FROM Tag t WHERE t.isActive=true ";

    @Autowired
    public CustomizedTagRepositoryImpl(PersistenceService<Tag> persistenceService) {
        this.persistenceService = persistenceService;
    }

    @PostConstruct
    private void init() {
        persistenceService.setType(Tag.class);
    }

    @Override
    public List<Tag> getAllTagsByPage(TagSearchCriteria searchCriteria, int page, int size) {
        return persistenceService.getAllModelsByPage(
                GET_ALL_TAGS, page, size, searchCriteria.getSortType(), searchCriteria.getSortBy());
    }

    @Override
    public void deleteById(int tagId) {
        Tag tag = persistenceService.getModelById(tagId);
        tag.setActive(false);
        persistenceService.update(tag);
    }
}
