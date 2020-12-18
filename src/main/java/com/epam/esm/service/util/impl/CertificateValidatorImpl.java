package com.epam.esm.service.util.impl;

import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.dao.exception.DaoException;
import com.epam.esm.service.util.CertificateValidator;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class CertificateValidatorImpl implements CertificateValidator {

    public void validateCertificate(GiftCertificate giftCertificate) throws DaoException {
        if (giftCertificate == null) {
            throw new DaoException("Failed to validate: certificate is empty",
                    ErrorCodeEnum.CERTIFICATE_VALIDATION_ERROR);
        }

        validateId(giftCertificate.getId());
        validateName(giftCertificate.getName());
        validateDescription(giftCertificate.getDescription());
        validatePrice(giftCertificate.getPrice());
        validateCreateAndUpdateDates(giftCertificate.getCreateDate(), giftCertificate.getLastUpdateDate());
        validateDuration(giftCertificate.getDuration());
    }

    public void validateId(int id) throws DaoException {
        if (id < 0) {
            throw new DaoException("Failed to validate: certificate id is negative",
                    ErrorCodeEnum.CERTIFICATE_VALIDATION_ERROR);
        }
    }

    public void validateName(String name) throws DaoException {
        if (name == null || name.isEmpty()) {
            throw new DaoException("Failed to validate: certificate name is empty",
                    ErrorCodeEnum.CERTIFICATE_VALIDATION_ERROR);
        }
    }

    public void validateDescription(String description) throws DaoException {
        if (description == null || description.isEmpty()) {
            throw new DaoException("Failed to validate: certificate description is empty",
                    ErrorCodeEnum.CERTIFICATE_VALIDATION_ERROR);
        }
    }

    private void validatePrice(double price) throws DaoException {
        if (price < 0) {
            throw new DaoException("Failed to validate: certificate price is negative",
                    ErrorCodeEnum.CERTIFICATE_VALIDATION_ERROR);
        }
    }

    private void validateCreateAndUpdateDates(ZonedDateTime createDate, ZonedDateTime updateDate)
            throws DaoException {
        if (createDate == null && updateDate == null) {
            return;
        }
        if (createDate == null) {
            throw new DaoException("Failed to validate: certificate create date is null while update date isn't",
                    ErrorCodeEnum.CERTIFICATE_VALIDATION_ERROR);
        }
        if (updateDate.isBefore(createDate)) {
            throw new DaoException("Failed to validate: certificate update date is before create date",
                    ErrorCodeEnum.CERTIFICATE_VALIDATION_ERROR);
        }
    }

    private void validateDuration(int duration) throws DaoException {
        if (duration <= 0) {
            throw new DaoException("Failed to validate: certificate price is negative",
                    ErrorCodeEnum.CERTIFICATE_VALIDATION_ERROR);
        }
    }
}
