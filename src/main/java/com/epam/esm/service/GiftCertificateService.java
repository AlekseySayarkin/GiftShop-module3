package com.epam.esm.service;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.dao.exception.DaoException;
import com.epam.esm.service.request.CertificateRequestBody;

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
     * @throws DaoException when failed to get {@code GiftCertificate} from persistence layer.
     * @return {@code GiftCertificate}.
     */
    GiftCertificate getGiftCertificate(String name) throws DaoException;

    /**
     * Retrieves data of {@code GiftCertificate} from
     * persistence layer by id
     * which equals to {@code int id}.
     *
     * @param id certificate id.
     * @throws DaoException when failed to get {@code GiftCertificate} from persistence layer.
     * @return {@code GiftCertificate}.
     */
    GiftCertificate getGiftCertificate(int id) throws DaoException;

    /**
     * Retrieves all {@code GiftCertificate} from persistence layer.
     * @param limit max amount of {@code GiftCertificate} to return.
     * @param offset from which position in a data source to return.
     * @throws DaoException when failed to get {@code GiftCertificate} from persistence layer.
     * @return List<GiftCertificate> - all existing certificates in persistence layer.
     */
    List<GiftCertificate> getCertificatesByPage(int limit, int offset) throws DaoException;

    /**
     * Retrieves {@code GiftCertificate} from persistence layer
     * by content which this {@code GiftCertificate} contains
     * in it name or description.
     *
     * @param content {@code GiftCertificate} name or description.
     * @param limit max amount of {@code GiftCertificate} to return.
     * @param offset from which position in a data source to return.
     * @throws DaoException when failed to get {@code GiftCertificate} from persistence layer.
     * @return List<GiftCertificate> - existing certificates in persistence layer.
     */
    List<GiftCertificate> getGiftCertificatesByContent(
            String content,int limit, int offset) throws DaoException;

    /**
     * Retrieves {@code GiftCertificate} from persistence layer
     * by name of a {@code Tag} which this {@code GiftCertificate} has.
     *
     * @param limit max amount of {@code GiftCertificate} to return.
     * @param offset from which position in a data source to return.
     * @param tagName name of a {@code Tag}.
     * @throws DaoException when failed to get {@code GiftCertificate} from persistence layer.
     * @return List<GiftCertificate> - existing certificates in persistence layer.
     */
    List<GiftCertificate> getGiftCertificateByTagName(
            String tagName, int limit, int offset) throws DaoException;

    /**
     * Retrieves all {@code GiftCertificate} from persistence layer
     * and sorts it by name according to {@code isAscending}.
     *
     * @param limit max amount of {@code GiftCertificate} to return.
     * @param offset from which position in a data source to return.
     * @param isAscending asc or desc sort.
     * @throws DaoException when failed to get {@code GiftCertificate} from persistence layer.
     * @return List<GiftCertificate> - sorted certificates in persistence layer.
     */
    List<GiftCertificate> getAllGiftCertificatesSortedByName(
            boolean isAscending, int limit, int offset) throws DaoException;

    /**
     * Retrieves all {@code GiftCertificate} from persistence layer
     * and sorts it by date according to {@code isAscending}.
     *
     * @param limit max amount of {@code GiftCertificate} to return.
     * @param offset from which position in a data source to return.
     * @param isAscending asc or desc sort.
     * @throws DaoException when failed to get {@code GiftCertificate} from persistence layer.
     * @return List<GiftCertificate> - sorted certificates in persistence layer.
     */
    List<GiftCertificate> getAllGiftCertificatesSortedByDate(
            boolean isAscending, int limit, int offset) throws DaoException;

    /**
     * Retrieves {@code GiftCertificate} from persistence layer
     * using one of non null fields of {@code CertificateRequestBody}.
     *
     * @param requestBody max amount and from which position in a data source of {@code GiftCertificate} to return.
     * @throws DaoException when failed to get {@code GiftCertificate} from persistence layer.
     * @return List<GiftCertificate> - certificates from persistence layer.
     */
    List<GiftCertificate> getGiftCertificates(CertificateRequestBody requestBody) throws DaoException;

    /**
     * Adds new {@code GiftCertificate} to persistence layer.
     *
     * @param giftCertificate {@code GiftCertificate} which to add to persistence layer.
     * @throws DaoException when failed to add {@code GiftCertificate} to persistence layer.
     * @return {@code GiftCertificate} from persistence layer.
     */
    GiftCertificate addGiftCertificate(GiftCertificate giftCertificate) throws DaoException;

    /**
     * Deletes {@code GiftCertificate} from persistence layer.
     *
     * @param id id of {@code GiftCertificate} which to delete from persistence layer.
     * @throws DaoException when failed to delete {@code GiftCertificate} from persistence layer.
     */
    void deleteGiftCertificate(int id) throws DaoException;

    /**
     * Updates {@code GiftCertificate} in persistence layer.
     * Null or default values in {@code GiftCertificate} are not updated.
     *
     * @param giftCertificate {@code GiftCertificate} which to update in persistence layer.
     * @param id id of {@code GiftCertificate} which to update in persistence layer.
     * @throws DaoException when failed to update {@code GiftCertificate} in persistence layer.
     * @return updated {@code GiftCertificate}
     */
    GiftCertificate updateGiftCertificate(GiftCertificate giftCertificate, int id) throws DaoException;
}
