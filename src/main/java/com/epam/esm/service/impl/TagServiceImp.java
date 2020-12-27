package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
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
    private static final int MAX_PAGE_SIZE = 50;

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
    public Tag getTagById(int id) throws ServiceException {
        tagValidator.validateId(id);
        try {
            Tag tag = tagDao.getTagById(id);
            if (tag == null) {
                throw new ServiceException("Failed to get tag by it id: " + id,
                        ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
            }

            return tag;
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getTag(int id): " + e.getMessage());
            throw new ServiceException("Failed to get tag by it id: " + id,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public List<Tag> getAllTags() throws ServiceException {
        try {
            return tagDao.getAllTags();
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllTags(): " + e.getMessage());
            throw new ServiceException("Failed to get all tags", ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public List<Tag> getAllTagsByPage(TagSearchCriteria requestBody, int page, int size) throws ServiceException {
        paginationValidator.validatePagination(size, page);

        if (requestBody == null) {
            requestBody = TagSearchCriteria.getDefaultTagRequestBody();
        }
        tagValidator.validateTagSearchCriteria(requestBody);

        try {
            return tagDao.getAllTagsByPage(requestBody, page, size);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllTagsByPage(): " + e.getMessage());
            throw new ServiceException("Failed to get tags", ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public int getLastPage(int size) throws ServiceException {
        if (size <= 0 || size > MAX_PAGE_SIZE) {
            throw new ServiceException("Failed to get last page: size is negative",
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_PAGE);
        }
        return tagDao.getLastPage(size);
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
            tagDao.deleteTag(tagId);
        } catch (DataAccessException | NoResultException | IllegalArgumentException e) {
            LOGGER.error("Following exception was thrown in deleteTag(): " + e.getMessage());
            throw new ServiceException("Failed to delete tag by it id: " + tagId,
                    ErrorCodeEnum.FAILED_TO_DELETE_TAG);
        }
    }
}
