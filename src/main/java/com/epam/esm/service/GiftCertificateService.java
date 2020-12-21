package com.epam.esm.service;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.dao.request.CertificateSearchCriteria;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;

/**
 * This interface provides with ability to create
 * transactions with {@code GiftCertificate} in and out
 * of persistence layer.
 *
 * @author Aleksey Sayarkin
 */
public interface GiftCertificateService {

    /**
     * Retrieves data of {@code GiftCertificate} from
     * persistence layer by name
     * which equals to {@code String name}.
     *
     * @param name certificate name.
     * @throws ServiceException when failed to get {@code GiftCertificate} from persistence layer.
     * @return {@code GiftCertificate}.
     */
    GiftCertificate getGiftCertificateByName(String name) throws ServiceException;

    /**
     * Retrieves data of {@code GiftCertificate} from
     * persistence layer by id
     * which equals to {@code int id}.
     *
     * @param id certificate id.
     * @throws ServiceException when failed to get {@code GiftCertificate} from persistence layer.
     * @return {@code GiftCertificate}.
     */
    GiftCertificate getGiftCertificateById(int id) throws ServiceException;

    /**
     * Retrieves {@code GiftCertificate} from persistence layer
     * using one of non null fields of {@code CertificateRequestBody}.
     *
     * @param requestBody max amount and from which position in a data source of {@code GiftCertificate} to return.
     * @throws ServiceException when failed to get {@code GiftCertificate} from persistence layer.
     * @return List<GiftCertificate> - certificates from persistence layer.
     */
    List<GiftCertificate> getGiftCertificatesByPage(CertificateSearchCriteria requestBody, int page, int size)
            throws ServiceException;

    /**
     * Retrieves number of pages from persistence layer if every page
     * contains certain number of {@code GiftCertificate}.
     *
     * @param size size of a page.
     * @return number of pages.
     */
    int getLastPage(int size) throws ServiceException;

    /**
     * Adds new {@code GiftCertificate} to persistence layer.
     *
     * @param giftCertificate {@code GiftCertificate} which to add to persistence layer.
     * @throws ServiceException when failed to add {@code GiftCertificate} to persistence layer.
     * @return {@code GiftCertificate} from persistence layer.
     */
    GiftCertificate addGiftCertificate(GiftCertificate giftCertificate) throws ServiceException;

    /**
     * Deletes {@code GiftCertificate} from persistence layer.
     *
     * @param id id of {@code GiftCertificate} which to delete from persistence layer.
     * @throws ServiceException when failed to delete {@code GiftCertificate} from persistence layer.
     */
    void deleteGiftCertificate(int id) throws ServiceException;

    /**
     * Updates {@code GiftCertificate} in persistence layer.
     * Null or default values in {@code GiftCertificate} are not updated.
     *
     * @param giftCertificate {@code GiftCertificate} which to update in persistence layer.
     * @param id id of {@code GiftCertificate} which to update in persistence layer.
     * @throws ServiceException when failed to update {@code GiftCertificate} in persistence layer.
     * @return updated {@code GiftCertificate}
     */
    GiftCertificate updateGiftCertificate(GiftCertificate giftCertificate, int id) throws ServiceException;
}
