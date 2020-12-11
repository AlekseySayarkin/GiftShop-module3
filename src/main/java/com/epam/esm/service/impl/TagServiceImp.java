package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.dao.exception.DaoException;
import com.epam.esm.service.request.TagRequestBody;
import com.epam.esm.service.util.PaginationValidator;
import com.epam.esm.service.util.TagValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public Tag getTag(String name) throws DaoException {
        tagValidator.validateName(name);
        try {
            return tagDao.getTag(name);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getTag(String name): " + e.getMessage());
            throw new DaoException("Failed to get tag by it name: " + name,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public Tag getTag(int id) throws DaoException {
        tagValidator.validateId(id);
        try {
            return tagDao.getTag(id);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getTag(int id): " + e.getMessage());
            throw new DaoException("Failed to get tag by it id: " + id,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public List<Tag> getAllTags() throws DaoException {
        try {
            return tagDao.getAllTags();
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllTags(): " + e.getMessage());
            throw new DaoException("Failed to get all tags", ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public List<Tag> getAllTagsByPage(TagRequestBody requestBody, int page, int size) throws DaoException {
        paginationValidator.validatePagination(size, page);
        if (requestBody == null) {
            requestBody = TagRequestBody.getDefaultTagRequestBody();
        }
        try {
            return tagDao.getAllTagsByPage(page, size, requestBody.getSortType(), requestBody.getSortBy());
        } catch (DataAccessException e) {
            e.printStackTrace();
            LOGGER.error("Following exception was thrown in getAllTagsByPage(): " + e.getMessage());
            throw new DaoException("Failed to get specified number of tags",
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public int getLastPage(int size) throws DaoException {
        if (size <= 0 || size > MAX_PAGE_SIZE) {
            throw new DaoException("Failed to get last page: size is negative",
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_PAGE);
        }
        return tagDao.getLastPage(size);
    }

    @Override
    @Transactional
    public Tag addTag(Tag tag) throws DaoException {
        tagValidator.validateTag(tag);
        try {
            int id = tagDao.addTag(tag);
            tag.setId(id);
            return tag;
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in addTag(): " + e.getMessage());
            throw new DaoException("Failed to add tag: tag already exists", ErrorCodeEnum.FAILED_TO_ADD_TAG);
        }
    }

    @Override
    @Transactional
    public void deleteTag(int tagId) throws DaoException {
        tagValidator.validateId(tagId);
        try {
            if (!tagDao.deleteTag(tagId)) {
                LOGGER.error("Failed to delete tag");
                throw new DaoException("Failed to delete tag because it id (" + tagId + ") is not found",
                        ErrorCodeEnum.FAILED_TO_DELETE_TAG);
            }
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in deleteTag(): " + e.getMessage());
            throw new DaoException("Failed to delete tag by it id: " + tagId,
                    ErrorCodeEnum.FAILED_TO_DELETE_TAG);
        }
    }
}
