package com.epam.esm.dao;

import com.epam.esm.dao.exception.DaoException;
import com.epam.esm.model.GiftCertificate;

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
     */
    GiftCertificate getGiftCertificate(String name) throws DaoException;

    /**
     * Retrieves data of {@code GiftCertificate} from
     * data source by id
     * which equals to {@code int id}.
     *
     * @param id certificate id.
     * @return {@code GiftCertificate}.
     */
    GiftCertificate getGiftCertificate(int id);

    /**
     * Retrieves all {@code GiftCertificate} from data source.
     *
     * @param limit max amount of {@code GiftCertificate} to return.
     * @param offset from which position in a data source
     * @return List<GiftCertificate> - all existing certificates in data source.
     */
    List<GiftCertificate> getGiftCertificatesByPage(int limit, int offset);

    /**
     * Retrieves {@code GiftCertificate} from data source
     * by content which this {@code GiftCertificate} contains
     * in it name or description.
     *
     * @param limit max amount of {@code GiftCertificate} to return.
     * @param offset from which position in a data source
     * @param content {@code GiftCertificate} name or description.
     * @return List<GiftCertificate> - existing certificates in data source.
     */
    List<GiftCertificate> getGiftCertificatesByContent(String content, int limit, int offset);

    /**
     * Retrieves {@code GiftCertificate} from data source
     * by name of a {@code Tag} which this {@code GiftCertificate} has.
     *
     * @param limit max amount of {@code GiftCertificate} to return.
     * @param offset from which position in a data source
     * @param tagName name of a {@code Tag}.
     * @return List<GiftCertificate> - existing certificates in data source.
     */
    List<GiftCertificate> getGiftCertificateByTagName(String tagName, int limit, int offset);

    /**
     * Retrieves all {@code GiftCertificate} from data source
     * and sorts it by name according to {@code isAscending}.
     *
     * @param limit max amount of {@code GiftCertificate} to return.
     * @param offset from which position in a data source
     * @param isAscending asc or desc sort.
     * @return List<GiftCertificate> - sorted certificates in data source.
     */
    List<GiftCertificate> getAllGiftCertificatesSortedByName(boolean isAscending, int limit, int offset);

    /**
     * Retrieves all {@code GiftCertificate} from data source
     * and sorts it by date according to {@code isAscending}.
     *
     * @param limit max amount of {@code GiftCertificate} to return.
     * @param offset from which position in a data source
     * @param isAscending asc or desc sort.
     * @return List<GiftCertificate> - sorted certificates in data source.
     */
    List<GiftCertificate> getGiftCertificatesSortedByDate(boolean isAscending, int limit, int offset);

    /**
     * Adds new {@code GiftCertificate} to data source.
     *
     * @param giftCertificate {@code GiftCertificate} which to be added to data source.
     * @return id of a {@code GiftCertificate} from data source.
     * @throws DaoException when failed to add {@code GiftCertificate} to data source.
     */
    int addGiftCertificate(GiftCertificate giftCertificate) throws DaoException;

    /**
     * Deletes {@code GiftCertificate} from data source.
     *
     * @param id id of {@code GiftCertificate} which to deleted from data source.
     * @return whether transaction was successful.
     */
    boolean deleteGiftCertificate(int id);

    /**
     * Updates {@code GiftCertificate} in data source.
     * Null or default values in {@code GiftCertificate} are not updated.
     *
     * @param giftCertificate {@code ServiceException} which to update in data source.
     * @return whether transaction was successful.
     */
    GiftCertificate updateGiftCertificate(GiftCertificate giftCertificate) throws DaoException;

    /**
     * Creates many to many relation with {@code GiftCertificate} and {@code Tag}.
     *
     * @param certificateId {@code GiftCertificate} id which to create a many to many relation with.
     * @param tagId {@code Tag} id  which to create a many to many relation with.
     * @return whether transaction was successful.
     */
    boolean createCertificateTagRelation(int certificateId, int tagId);

    /**
     * Deletes many to many relation with {@code GiftCertificate} and {@code Tag}.
     *
     * @param certificateId {@code GiftCertificate} id which to delete a many to many relation with.
     * @return whether transaction was successful.
     */
    boolean deleteAllCertificateTagRelations(int certificateId);
}
