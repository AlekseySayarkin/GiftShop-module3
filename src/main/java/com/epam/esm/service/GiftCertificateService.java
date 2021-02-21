package com.epam.esm.service;

import com.epam.esm.service.criteria.sort.SortBy;
import com.epam.esm.service.criteria.sort.SortType;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.criteria.search.CertificateSearchCriteria;
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
     * persistence layer by it id
     * which equals to {@code int certificateId}.
     *
     * @param certificateId certificate id.
     * @throws ServiceException when failed to get {@code GiftCertificate}.
     * @return {@code GiftCertificate}.
     */
    GiftCertificate getGiftCertificateById(int certificateId) throws ServiceException;

    /**
     * Retrieves {@code GiftCertificate} from persistence layer.
     *
     * @param searchCriteria object containing search criteria.
     * @param page from which position to start.
     * @param size max amount of {@code GiftCertificate} to return.
     * @param sortType type of a sort.
     * @param sortBy by witch field to sort.
     * @throws ServiceException when failed to get {@code GiftCertificate}.
     * @return List<GiftCertificate> - certificates from persistence layer.
     */
    List<GiftCertificate> getGiftCertificatesByPage(CertificateSearchCriteria searchCriteria, int page, int size,
                                                    SortType sortType, SortBy sortBy) throws ServiceException;

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
     * @param giftCertificate {@code GiftCertificate} which to be added to persistence layer.
     * @throws ServiceException when failed to add {@code GiftCertificate} to persistence layer.
     * @return id of a {@code GiftCertificate} from persistence layer.
     */
    GiftCertificate addGiftCertificate(GiftCertificate giftCertificate) throws ServiceException;

    /**
     * Deletes {@code GiftCertificate} from persistence layer.
     *
     * @param certificateId id of a {@code GiftCertificate} which to delete from persistence layer.
     * @throws ServiceException when failed to delete {@code GiftCertificate} from persistence layer.
     */
    void deleteGiftCertificate(int certificateId) throws ServiceException;

    /**
     * Updates {@code GiftCertificate} in persistence layer.
     *
     * @param id id of giftCertificate  which to update in persistence layer.
     * @param giftCertificate {@code ServiceException} which to update persistence layer.
     * @return updated {@code GiftCertificate} from persistence layer.
     */
    GiftCertificate updateGiftCertificate(GiftCertificate giftCertificate, int id) throws ServiceException;
}
