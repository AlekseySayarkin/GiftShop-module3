package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.service.exception.ErrorCodeEnum;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.dao.request.TagSearchCriteria;
import com.epam.esm.service.util.PaginationValidator;
import com.epam.esm.service.util.TagValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;

@Service
public class TagServiceImp implements TagService {

    private static final Logger LOGGER = LogManager.getLogger(TagServiceImp.class);

    private final TagDao tagDao;
    private final TagValidator tagValidator;
    private final PaginationValidator paginationValidator;

    @Autowired
    public TagServiceImp(TagDao tagDao, TagValidator tagValidator, PaginationValidator paginationValidator) {
        this.tagDao = tagDao;
        this.tagValidator = tagValidator;
        this.paginationValidator = paginationValidator;
    }

    @Override
    public Tag getTagByName(String name) throws ServiceException {
        tagValidator.validateName(name);
        try {
            return tagDao.getTagByName(name);
        } catch (NoResultException ex) {
            throw new ServiceException(String.format("Failed to get tag with name = {%s}", name),
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public Tag getTagById(int tagId) throws ServiceException {
        tagValidator.validateId(tagId);
        try {
            Tag tag = tagDao.getTagById(tagId);
            if (tag == null) {
                LOGGER.error("Failed to get tag by it id: " + tagId);
                throw new ServiceException("Failed to get tag by it id: " + tagId,
                        ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
            }

            return tag;
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getTag(int id): " + e.getMessage());
            throw new ServiceException("Failed to get tag by it id: " + tagId,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public List<Tag> getAllTagsByPage(TagSearchCriteria searchCriteria, int page, int size,
                                      SortType sortType, SortBy sortBy) throws ServiceException {
        paginationValidator.validatePagination(page, size);

        if (searchCriteria == null) {
            searchCriteria = TagSearchCriteria.getDefaultTagRequestBody();
        }
        searchCriteria.setSortType(sortType);
        searchCriteria.setSortBy(sortBy);
        tagValidator.validateTagSearchCriteria(searchCriteria);

        try {
            return tagDao.getAllTagsByPage(searchCriteria, page, size);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllTagsByPage(): " + e.getMessage());
            throw new ServiceException("Failed to get tags", ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public int getLastPage(int size) throws ServiceException {
        paginationValidator.validateSize(size);
        try {
            return tagDao.getLastPage(size);
        } catch (DataAccessException | PersistenceException e) {
            LOGGER.error("Failed to get last page");
            throw new ServiceException("Failed to get last page", ErrorCodeEnum.FAILED_TO_RETRIEVE_PAGE);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Tag addTag(Tag tag) throws ServiceException {
        tagValidator.validateTag(tag);
        try {
            return tagDao.addTag(tag);
        } catch (PersistenceException | DataAccessException e) {
            LOGGER.error("Failed to add tag");
            throw new ServiceException("Failed to add tag", ErrorCodeEnum.FAILED_TO_ADD_TAG);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void deleteTag(int tagId) throws ServiceException {
        tagValidator.validateId(tagId);
        try {
            tagDao.deleteTagById(tagId);
        } catch (DataAccessException | NoResultException | IllegalArgumentException e) {
            LOGGER.error("Following exception was thrown in deleteTag(): " + e.getMessage());
            throw new ServiceException("Failed to delete tag by it id: " + tagId,
                    ErrorCodeEnum.FAILED_TO_DELETE_TAG);
        }
    }
}
