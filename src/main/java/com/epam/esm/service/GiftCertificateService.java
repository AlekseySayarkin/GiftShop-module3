package com.epam.esm.service;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.dao.exception.PersistenceException;
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
     * @throws PersistenceException when failed to get {@code GiftCertificate} from persistence layer.
     * @return {@code GiftCertificate}.
     */
    GiftCertificate getGiftCertificate(String name) throws PersistenceException;

    /**
     * Retrieves data of {@code GiftCertificate} from
     * persistence layer by id
     * which equals to {@code int id}.
     *
     * @param id certificate id.
     * @throws PersistenceException when failed to get {@code GiftCertificate} from persistence layer.
     * @return {@code GiftCertificate}.
     */
    GiftCertificate getGiftCertificate(int id) throws PersistenceException;

    /**
     * Retrieves all {@code GiftCertificate} from persistence layer.
     * @throws PersistenceException when failed to get {@code GiftCertificate} from persistence layer.
     * @return List<GiftCertificate> - all existing certificates in persistence layer.
     */
    List<GiftCertificate> geAllCertificatesByContent() throws PersistenceException;

    /**
     * Retrieves {@code GiftCertificate} from persistence layer
     * by content which this {@code GiftCertificate} contains
     * in it name or description.
     *
     * @param content {@code GiftCertificate} name or description.
     * @throws PersistenceException when failed to get {@code GiftCertificate} from persistence layer.
     * @return List<GiftCertificate> - existing certificates in persistence layer.
     */
    List<GiftCertificate> getGiftCertificatesByContent(String content) throws PersistenceException;

    /**
     * Retrieves {@code GiftCertificate} from persistence layer
     * by name of a {@code Tag} which this {@code GiftCertificate} has.
     *
     * @param tagName name of a {@code Tag}.
     * @throws PersistenceException when failed to get {@code GiftCertificate} from persistence layer.
     * @return List<GiftCertificate> - existing certificates in persistence layer.
     */
    List<GiftCertificate> getGiftCertificateByTagName(String tagName) throws PersistenceException;

    /**
     * Retrieves all {@code GiftCertificate} from persistence layer
     * and sorts it by name according to {@code isAscending}.
     *
     * @param isAscending asc or desc sort.
     *@throws PersistenceException when failed to get {@code GiftCertificate} from persistence layer.
     * @return List<GiftCertificate> - sorted certificates in persistence layer.
     */
    List<GiftCertificate> getAllGiftCertificatesSortedByName(boolean isAscending) throws PersistenceException;

    /**
     * Retrieves all {@code GiftCertificate} from persistence layer
     * and sorts it by date according to {@code isAscending}.
     *
     * @param isAscending asc or desc sort.
     * @throws PersistenceException when failed to get {@code GiftCertificate} from persistence layer.
     * @return List<GiftCertificate> - sorted certificates in persistence layer.
     */
    List<GiftCertificate> getAllGiftCertificatesSortedByDate(boolean isAscending) throws PersistenceException;

    /**
     * Retrieves {@code GiftCertificate} from persistence layer
     * using one of non null fields of {@code CertificateRequestBody}.
     *
     * @param requestBody representation of http request body.
     * @throws PersistenceException when failed to get {@code GiftCertificate} from persistence layer.
     * @return List<GiftCertificate> - certificates from persistence layer.
     */
    List<GiftCertificate> getGiftCertificates(CertificateRequestBody requestBody) throws PersistenceException;

    /**
     * Adds new {@code GiftCertificate} to persistence layer.
     *
     * @param giftCertificate {@code GiftCertificate} which to add to persistence layer.
     * @throws PersistenceException when failed to add {@code GiftCertificate} to persistence layer.
     * @return {@code GiftCertificate} from persistence layer.
     */
    GiftCertificate addGiftCertificate(GiftCertificate giftCertificate) throws PersistenceException;

    /**
     * Deletes {@code GiftCertificate} from persistence layer.
     *
     * @param id id of {@code GiftCertificate} which to delete from persistence layer.
     * @throws PersistenceException when failed to delete {@code GiftCertificate} from persistence layer.
     */
    void deleteGiftCertificate(int id) throws PersistenceException;

    /**
     * Updates {@code GiftCertificate} in persistence layer.
     * Null or default values in {@code GiftCertificate} are not updated.
     *
     * @param giftCertificate {@code GiftCertificate} which to update in persistence layer.
     * @param id id of {@code GiftCertificate} which to update in persistence layer.
     * @throws PersistenceException when failed to update {@code GiftCertificate} in persistence layer.
     * @return updated {@code GiftCertificate}
     */
    GiftCertificate updateGiftCertificate(GiftCertificate giftCertificate, int id) throws PersistenceException;
}
