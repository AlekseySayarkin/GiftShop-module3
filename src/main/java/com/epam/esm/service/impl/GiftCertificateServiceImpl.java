package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.service.exception.ErrorCodeEnum;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.dao.request.CertificateSearchCriteria;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.util.CertificateValidator;
import com.epam.esm.service.util.PaginationValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private static final Logger LOGGER = LogManager.getLogger(GiftCertificateServiceImpl.class);

    private final GiftCertificateDAO giftCertificateDAO;
    private final CertificateValidator certificateValidator;
    private final PaginationValidator paginationValidator;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDAO giftCertificateDAO,
            CertificateValidator certificateValidator, PaginationValidator paginationValidator) {
        this.giftCertificateDAO = giftCertificateDAO;
        this.certificateValidator = certificateValidator;
        this.paginationValidator = paginationValidator;
    }

    @Override
    public GiftCertificate getGiftCertificateByName(String name) throws ServiceException {
        certificateValidator.validateName(name);
        try {
            return giftCertificateDAO.getGiftCertificateByName(name);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getGiftCertificate(String name): " + e.getMessage());
            throw new ServiceException("Failed to get certificate by it name: " + name,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public GiftCertificate getGiftCertificateById(int id) throws ServiceException {
        certificateValidator.validateId(id);
        try {
            return giftCertificateDAO.getGiftCertificateById(id);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getGiftCertificate(int id): " + e.getMessage());
            throw new ServiceException("Failed to get certificate by it id: " + id,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public List<GiftCertificate> getGiftCertificatesByPage(CertificateSearchCriteria requestBody, int page, int size)
            throws ServiceException {
        paginationValidator.validatePagination(size, page);
        if (requestBody == null) {
            requestBody = CertificateSearchCriteria.getDefaultCertificateRequestBody();
        }
        return giftCertificateDAO.getGiftCertificatesByRequestBody(requestBody, page, size);
    }

    @Override
    public int getLastPage(int size) throws ServiceException {
        return giftCertificateDAO.getLastPage(size);
    }

    @Override
    @Transactional
    public GiftCertificate addGiftCertificate(GiftCertificate giftCertificate) throws ServiceException {
        certificateValidator.validateCertificate(giftCertificate);
        try {
            giftCertificate.setCreateDate(ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
            giftCertificate.setLastUpdateDate(giftCertificate.getCreateDate());

            return giftCertificateDAO.addGiftCertificate(giftCertificate);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in addGiftCertificate(): " + e.getMessage());
            throw new ServiceException("Failed to add certificate: certificate already exist",
                    ErrorCodeEnum.FAILED_TO_ADD_CERTIFICATE);
        }
    }

    @Override
    @Transactional
    public void deleteGiftCertificate(int id) throws ServiceException {
        GiftCertificate giftCertificate = getGiftCertificateById(id);
        if (giftCertificate == null) {
            throw new ServiceException("Failed to delete certificate: certificate not found",
                    ErrorCodeEnum.FAILED_TO_DELETE_CERTIFICATE);
        }
        try {
            giftCertificateDAO.deleteGiftCertificate(giftCertificate.getId());
        } catch (DataAccessException | NoResultException | IllegalArgumentException e) {
            LOGGER.error("Following exception was thrown in deleteGiftCertificate(): " + e.getMessage());
            throw new ServiceException("Failed to delete certificate", ErrorCodeEnum.FAILED_TO_DELETE_CERTIFICATE);
        }
    }

    @Override
    @Transactional
    public GiftCertificate updateGiftCertificate(GiftCertificate giftCertificate, int id) throws ServiceException {
        giftCertificate.setId(id);
        certificateValidator.validateCertificate(giftCertificate);
        try {
            giftCertificate.setLastUpdateDate(ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));

            giftCertificate = giftCertificateDAO.updateGiftCertificate(giftCertificate);
            if (giftCertificate == null) {
                LOGGER.error("Failed to update certificate");
                throw new ServiceException("Failed to update certificate",
                        ErrorCodeEnum.FAILED_TO_UPDATE_CERTIFICATE);
            }
            return giftCertificate;
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in updateGiftCertificate(): " + e.getMessage());
            throw new ServiceException("Failed to update certificate", ErrorCodeEnum.FAILED_TO_UPDATE_CERTIFICATE);
        }
    }
}
