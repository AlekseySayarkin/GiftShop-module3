package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.epam.esm.dao.exception.DaoException;
import com.epam.esm.dao.util.PersistenceUtil;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.persistence.criteria.*;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
@EntityScan(basePackages = "com.epam.esm.model")
public class HibernateGiftCertificateDaoImpl implements GiftCertificateDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private final PersistenceUtil<GiftCertificate> persistenceUtil;

    private static final String GET_CERTIFICATE_BY_NAME = "SELECT g FROM GiftCertificate g WHERE g.name=:name";
    private static final String GET_ALL_CERTIFICATES = "SELECT g FROM GiftCertificate g ";
    public static final String GET_CERTIFICATE_COUNT = "SELECT count(g.id) FROM GiftCertificate g ";

    @Autowired
    public HibernateGiftCertificateDaoImpl(PersistenceUtil<GiftCertificate> persistenceUtil) {
        this.persistenceUtil = persistenceUtil;
    }

    @PostConstruct
    private void init() {
        persistenceUtil.setType(GiftCertificate.class);
    }

    @Override
    public GiftCertificate getGiftCertificate(String name) throws DaoException {
        try {
            return persistenceUtil.getModelByName(GET_CERTIFICATE_BY_NAME, name);
        } catch (NoResultException ex) {
            throw new DaoException(String.format("Failed to get certificate with name = {%s}", name),
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public GiftCertificate getGiftCertificate(int id) {
        return persistenceUtil.getModelById(id);
    }

    @Override
    public List<GiftCertificate> getGiftCertificatesByPage(int limit, int offset) {
        return jdbcTemplate.query(SQL_GET_CERTIFICATES_BY_PAGE, extractor, limit, offset);
    }

    @Override
    public List<GiftCertificate> getGiftCertificatesByContent(String content, int limit, int offset) {
        String param = "%" + content + "%";
        return jdbcTemplate.query(
                SQL_GET_ALL_CERTIFICATES_BY_CONTENT, extractor, limit, offset, param, param);
    }

    @Override
    public List<GiftCertificate> getGiftCertificateByTagName(String tagName, int limit, int offset) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> cq = cb.createQuery(GiftCertificate.class);
        Root<Tag> tagRoot = cq.from(Tag.class);

        Join<Tag, GiftCertificate> tagCertificateJoin = tagRoot.join("certificates", JoinType.INNER);

        final List<Predicate> andPredicates = new ArrayList<>();

        cq.select(tagCertificateJoin).distinct(true);

        TypedQuery<GiftCertificate> query = entityManager.createQuery(cq);
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificatesSortedByName(boolean isAscending,  int limit, int offset) {
        return getAllGiftCertificatesSortedByParameter("GiftCertificates.Name", isAscending, limit, offset);
    }

    @Override
    public List<GiftCertificate> getGiftCertificatesSortedByDate(boolean isAscending, int limit, int offset) {
        return getAllGiftCertificatesSortedByParameter("CreateDate", isAscending, limit, offset);
    }

    private List<GiftCertificate> getAllGiftCertificatesSortedByParameter(
            String parameter, boolean isAscending, int limit, int offset) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> cq = cb.createQuery(GiftCertificate.class);
        Root<Tag> tagRoot = cq.from(Tag.class);

        Join<Tag, GiftCertificate> tagCertificateJoin = tagRoot.join("certificates", JoinType.INNER);

        if (isAscending) {
            cq.orderBy(cb.asc(tagCertificateJoin.get(parameter)));
        } else {
            cq.orderBy(cb.desc(tagCertificateJoin.get(parameter));
        }

        cq.select(tagCertificateJoin).distinct(true);

        TypedQuery<GiftCertificate> query = entityManager.createQuery(cq);
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public int addGiftCertificate(GiftCertificate giftCertificate) throws DaoException {
        try{
            return persistenceUtil.addModel(giftCertificate);
        } catch (PersistenceException e) {
            throw new DaoException(String.format("Failed to add certificate with name = {%s}.",
                    giftCertificate.getName()), ErrorCodeEnum.FAILED_TO_DELETE_TAG);
        }
    }

    @Override
    public boolean deleteGiftCertificate(int id) {
        try {
            persistenceUtil.deleteModel(id);
            return true;
        } catch (NoResultException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public GiftCertificate updateGiftCertificate(GiftCertificate giftCertificate) {
        return persistenceUtil.updateModel(giftCertificate);
    }

    @Override//todelete
    public boolean createCertificateTagRelation(int certificateId, int tagId) {
       // return jdbcTemplate.update(SQL_CREATE_JOIN, certificateId, tagId) == 1;
        return  true;
    }

    @Override//todelete
    public boolean deleteAllCertificateTagRelations(int certificateId) {
       // return jdbcTemplate.update(SQL_DELETE_JOIN, certificateId) == 1;
        return true;
    }
}
