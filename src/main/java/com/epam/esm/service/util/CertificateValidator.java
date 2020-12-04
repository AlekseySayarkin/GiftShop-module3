package com.epam.esm.service.util;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.dao.exception.PersistenceException;

public interface CertificateValidator {

    void validateCertificate(GiftCertificate giftCertificate) throws PersistenceException;
    void validateId(int id) throws PersistenceException;
    void validateName(String name) throws PersistenceException;
    void validateDescription(String description) throws PersistenceException;
}
