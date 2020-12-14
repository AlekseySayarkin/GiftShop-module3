package com.epam.esm.service.util;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.dao.exception.DaoException;

public interface CertificateValidator {

    void validateCertificate(GiftCertificate giftCertificate) throws DaoException;
    void validateId(int id) throws DaoException;
    void validateName(String name) throws DaoException;
    void validateDescription(String description) throws DaoException;
}
