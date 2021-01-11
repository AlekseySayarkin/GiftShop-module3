package com.epam.esm.dao;

import com.epam.esm.dao.request.CertificateSearchCriteria;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.exception.ServiceException;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;

/**
 * This interface provides with ability to
 * transfer {@code GiftCertificate} in and out
 * of data source.
 *
 * @author Aleksey Sayarkin
 */
public interface GiftCertificateDAO {

    /**
     * Retrieves data of {@code GiftCertificate} from
     * data source by name
     * which equals to {@code String name}.
     *
     * @param name certificate name.
     * @return {@code GiftCertificate}.
     * @throws NoResultException if failed to get result.
     */
    GiftCertificate getGiftCertificateByName(String name) throws NoResultException;

    /**
     * Retrieves data of {@code GiftCertificate} from
     * data source by id
     * which equals to {@code int id}.
     *
     * @param id certificate id.
     * @return {@code GiftCertificate}.
     */
    GiftCertificate getGiftCertificateById(int id);

    /**
     * Retrieves all {@code GiftCertificate} from data source.
     *
     * @param requestBody object containing search criteria.
     * @param page from which position in a data source.
     * @param size max amount of {@code GiftCertificate} to return.
     * @return List<GiftCertificate> - all existing certificates in data source.
     */
    List<GiftCertificate> getGiftCertificatesByRequestBody(CertificateSearchCriteria requestBody, int page, int size) throws ServiceException;

    /**
     * Retrieves count of {@code GiftCertificate} from data source.
     *
     * @param size size of a page.
     * @return count of {@code GiftCertificate}.
     */
    int getLastPage(int size);

    /**
     * Adds new {@code GiftCertificate} to data source.
     *
     * @param giftCertificate {@code GiftCertificate} which to be added to data source.
     * @return id of a {@code GiftCertificate} from data source.
     * @throws PersistenceException when failed to add {@code GiftCertificate} to data source.
     */
    GiftCertificate addGiftCertificate(GiftCertificate giftCertificate) throws PersistenceException;

    /**
     * Deletes {@code GiftCertificate} from data source.
     *
     * @param id id of {@code GiftCertificate} which to deleted from data source.
     */
    void deleteGiftCertificate(int id);

    /**
     * Updates {@code GiftCertificate} in data source.
     *
     * @param giftCertificate {@code ServiceException} which to update in data source.
     * @return updated {@code GiftCertificate}.
     */
    GiftCertificate updateGiftCertificate(GiftCertificate giftCertificate);
}
