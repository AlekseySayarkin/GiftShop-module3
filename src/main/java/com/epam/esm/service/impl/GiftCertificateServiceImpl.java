package com.epam.esm.service.impl;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.ErrorCodeEnum;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.search.criteria.CertificateSearchCriteria;
import com.epam.esm.service.search.sort.SortBy;
import com.epam.esm.service.search.sort.SortType;
import com.epam.esm.service.util.CertificateValidator;
import com.epam.esm.service.util.PaginationUtil;
import com.epam.esm.service.util.PaginationValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final static Logger log = LogManager.getLogger(GiftCertificateServiceImpl.class);

    private final GiftCertificateRepository certificateRepository;
    private final CertificateValidator certificateValidator;
    private final PaginationValidator paginationValidator;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository certificateRepository,
                                      CertificateValidator certificateValidator,
                                      PaginationValidator paginationValidator) {
        this.certificateRepository = certificateRepository;
        this.certificateValidator = certificateValidator;
        this.paginationValidator = paginationValidator;
    }

    @Override
    public GiftCertificate getGiftCertificateById(int certificateId) throws ServiceException {
        certificateValidator.validateId(certificateId);
        try {
            return certificateRepository.findById(certificateId).orElseThrow(() -> {
                log.error("Failed to get certificate by it id: " + certificateId);
                return new ServiceException(
                        "Failed to get certificate by it id: " + certificateId,
                        ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE
                );
            });
        } catch (DataAccessException e) {
            log.error("Following exception was thrown in getGiftCertificate(int id): " + e.getMessage());
            throw new ServiceException(
                    "Failed to get certificate by it id: " + certificateId, ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE
            );
        }
    }

    @Override
    public List<GiftCertificate> getGiftCertificatesByPage(CertificateSearchCriteria searchCriteria, int page, int size,
                                                           SortType sortType, SortBy sortBy) throws ServiceException {
        paginationValidator.validatePagination(page, size);

        if (searchCriteria == null) {
            searchCriteria = CertificateSearchCriteria.getDefaultCertificateRequestBody();
        }
        searchCriteria.setSortType(sortType);
        searchCriteria.setSortBy(sortBy);
        certificateValidator.validateCertificateSearchCriteria(searchCriteria);

        return certificateRepository.findAll(
                getSpecification(searchCriteria), PageRequest.of(--page, size, searchCriteria.getSort())
        ).getContent();
    }

    private Specification<GiftCertificate> getSpecification(CertificateSearchCriteria searchCriteria) {
        return Specification
                .where(containsTextInNameOrDescription(searchCriteria.getContent()))
                .or(containsTextInTagName(searchCriteria.getTagNames()));
    }

    private Specification<GiftCertificate> containsTextInNameOrDescription(String text) {
        return text == null || text.isEmpty() ? null : (root, query, builder) -> {
            final var finalText = "%" + text + "%";
            return builder.or(
                    builder.like(root.get("name"), finalText),
                    builder.like(root.get("description"), finalText)
            );
        };
    }

    private Specification<GiftCertificate> containsTextInTagName(List<String> tagNames) {
        return tagNames == null || tagNames.isEmpty() ? null : (root, query, builder) -> {
            var tags = root.join("tags");
            var inTags = builder.in(tags.get("name"));
            tagNames.forEach(inTags::value);
            return inTags;
        };
    }

    @Override
    public int getLastPage(int size) throws ServiceException {
        paginationValidator.validateSize(size);
        try {
            return PaginationUtil.getLastPage((int) certificateRepository.count(), size);
        } catch (DataAccessException | PersistenceException e) {
            log.error("Failed to get last page");
            throw new ServiceException("Failed to get last page", ErrorCodeEnum.FAILED_TO_RETRIEVE_PAGE);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public GiftCertificate addGiftCertificate(GiftCertificate giftCertificate) throws ServiceException {
        certificateValidator.validateCertificate(giftCertificate);
        try {
            giftCertificate.setActive(true);
            return certificateRepository.save(giftCertificate);
        } catch (DataAccessException | PersistenceException e) {
            log.error("Following exception was thrown in addGiftCertificate(): " + e.getMessage());
            throw new ServiceException(
                    "Failed to add certificate: certificate already exist", ErrorCodeEnum.FAILED_TO_ADD_CERTIFICATE
            );
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void deleteGiftCertificate(int certificateId) throws ServiceException {
        GiftCertificate giftCertificate = getGiftCertificateById(certificateId);
        if (giftCertificate == null) {
            log.error("Failed to delete certificate: certificate not found");
            throw new ServiceException(
                    "Failed to delete certificate: certificate not found", ErrorCodeEnum.FAILED_TO_DELETE_CERTIFICATE
            );
        }
        try {
            certificateRepository.deleteById(giftCertificate.getId());
        } catch (DataAccessException | NoResultException | IllegalArgumentException e) {
            log.error("Following exception was thrown in deleteGiftCertificate(): " + e.getMessage());
            throw new ServiceException("Failed to delete certificate", ErrorCodeEnum.FAILED_TO_DELETE_CERTIFICATE);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public GiftCertificate updateGiftCertificate(GiftCertificate giftCertificate, int id) throws ServiceException {
        giftCertificate.setId(id);
        certificateValidator.validateCertificate(giftCertificate);
        try {
            return updateExistingCertificate(giftCertificate);
        } catch (DataAccessException e) {
            log.error("Following exception was thrown in updateGiftCertificate(): " + e.getMessage());
            throw new ServiceException("Failed to update certificate", ErrorCodeEnum.FAILED_TO_UPDATE_CERTIFICATE);
        }
    }

    private GiftCertificate updateExistingCertificate(GiftCertificate updated) throws ServiceException {
        GiftCertificate existing = getGiftCertificateById(updated.getId());
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setDuration(updated.getDuration());
        existing.setTags(updated.getTags());
        existing.setActive(true);
        return certificateRepository.save(existing);
    }
}
