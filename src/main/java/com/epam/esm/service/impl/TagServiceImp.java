package com.epam.esm.service.impl;

import com.epam.esm.model.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.ErrorCodeEnum;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.search.criteria.TagSearchCriteria;
import com.epam.esm.service.search.sort.SortBy;
import com.epam.esm.service.search.sort.SortType;
import com.epam.esm.service.util.PaginationUtil;
import com.epam.esm.service.util.PaginationValidator;
import com.epam.esm.service.util.TagValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;

@Service
public class TagServiceImp implements TagService {

    private final static Logger log = LogManager.getLogger(OrderServiceImpl.class);

    private final TagRepository tagRepository;
    private final TagValidator tagValidator;
    private final PaginationValidator paginationValidator;

    @Autowired
    public TagServiceImp(TagRepository tagRepository, TagValidator tagValidator,
                         PaginationValidator paginationValidator) {
        this.tagRepository = tagRepository;
        this.tagValidator = tagValidator;
        this.paginationValidator = paginationValidator;
    }

    @Override
    public Tag getTagById(int tagId) throws ServiceException {
        tagValidator.validateId(tagId);
        try {
            return tagRepository.findById((tagId)).orElseThrow(() -> {
                log.error("Failed to get tag by it id: " + tagId);
                return new ServiceException(
                        "Failed to get tag by it id: " + tagId, ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG
                );
            });
        } catch (DataAccessException e) {
            log.error("Following exception was thrown in getTag(int id): " + e.getMessage());
            throw new ServiceException("Failed to get tag by it id: " + tagId, ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
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
            return tagRepository.findAll(PageRequest.of(--page, size, searchCriteria.getSort())).getContent();
        } catch (DataAccessException e) {
            log.error("Following exception was thrown in getAllTagsByPage(): " + e.getMessage());
            throw new ServiceException("Failed to get tags", ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public int getLastPage(int size) throws ServiceException {
        paginationValidator.validateSize(size);
        try {
            return PaginationUtil.getLastPage((int) tagRepository.count(), size);
        } catch (DataAccessException | PersistenceException e) {
            log.error("Failed to get last page");
            throw new ServiceException("Failed to get last page", ErrorCodeEnum.FAILED_TO_RETRIEVE_PAGE);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Tag addTag(Tag tag) throws ServiceException {
        tagValidator.validateTag(tag);
        try {
            tag.setActive(true);
            return tagRepository.save(tag);
        } catch (PersistenceException | DataAccessException e) {
            log.error("Failed to add tag");
            throw new ServiceException("Failed to add tag", ErrorCodeEnum.FAILED_TO_ADD_TAG);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void deleteTag(int tagId) throws ServiceException {
        tagValidator.validateId(tagId);
        try {
            tagRepository.deleteById(tagId);
        } catch (DataAccessException | NoResultException | IllegalArgumentException e) {
            log.error("Following exception was thrown in deleteTag(): " + e.getMessage());
            throw new ServiceException("Failed to delete tag by it id: " + tagId, ErrorCodeEnum.FAILED_TO_DELETE_TAG);
        }
    }
}
