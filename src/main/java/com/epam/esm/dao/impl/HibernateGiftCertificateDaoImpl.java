package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.request.CertificateSearchCriteria;
import com.epam.esm.dao.service.PersistenceService;
import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.ErrorCodeEnum;
import com.epam.esm.service.exception.ServiceException;
import com.google.common.base.CaseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@EntityScan(basePackages = "com.epam.esm.model")
public class HibernateGiftCertificateDaoImpl implements GiftCertificateDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private static final boolean ACTIVE_CERTIFICATE = true;
    private static final boolean DELETED_CERTIFICATE = false;

    private final PersistenceService<GiftCertificate> persistenceService;

    private static final String GET_CERTIFICATE_BY_NAME =
            "SELECT g FROM GiftCertificate g WHERE g.name=:name AND g.isActive=true ";
    private static final String GET_CERTIFICATE_COUNT =
            "SELECT count(g.id) FROM GiftCertificate g WHERE g.isActive=true ";

    @Autowired
    public HibernateGiftCertificateDaoImpl(PersistenceService<GiftCertificate> persistenceService) {
        this.persistenceService = persistenceService;
    }

    @PostConstruct
    private void init() {
        persistenceService.setType(GiftCertificate.class);
    }

    @Override
    public GiftCertificate getGiftCertificateByName(String name) throws NoResultException {
        return persistenceService.getModelByName(GET_CERTIFICATE_BY_NAME, name);
    }

    @Override
    public GiftCertificate getGiftCertificateById(int id) {
        return persistenceService.getModelById(id);
    }

    @Override
    public List<GiftCertificate> getGiftCertificatesByRequestBody(
            CertificateSearchCriteria requestBody, int page, int size) throws ServiceException {
        if (requestBody == null) {
            requestBody = CertificateSearchCriteria.getDefaultCertificateRequestBody();
        }
        if (!requestBody.getSortBy().equals(SortBy.NAME) && !requestBody.getSortBy().equals(SortBy.CREATE_DATE)) {
            throw new ServiceException("Cant sort users by " + requestBody.getSortBy(),
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }

        List<GiftCertificate> giftCertificates =  getGiftCertificatesFromQuery(requestBody, page, size);
        removeDeletedTags(giftCertificates);
        
        return giftCertificates;
    }

    private List<GiftCertificate> getGiftCertificatesFromQuery(
            CertificateSearchCriteria requestBody, int page, int size) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = builder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);

        query.select(root).distinct(true);
        buildQuery(root, query, requestBody);

        CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, requestBody.getSortBy().toString());
        if (requestBody.getSortType().equals(SortType.ASC)) {
            query.orderBy(builder.asc(root.get(CaseFormat.LOWER_UNDERSCORE
                    .to(CaseFormat.LOWER_CAMEL, requestBody.getSortBy().toString()))));
        } else {
            query.orderBy(builder.desc(root.get(CaseFormat.LOWER_UNDERSCORE
                    .to(CaseFormat.LOWER_CAMEL, requestBody.getSortBy().toString()))));
        }

        TypedQuery<GiftCertificate> typedQuery = entityManager.createQuery(query);

        typedQuery.setFirstResult((page - 1) * size);
        typedQuery.setMaxResults(size);

        return typedQuery.getResultList();
    }

    private <T extends AbstractQuery<GiftCertificate>> void buildQuery(
            Root<GiftCertificate> root, T query, CertificateSearchCriteria requestBody) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        Join<GiftCertificate, Tag> tags = root.join("tags");
        List<Predicate> predicates = new ArrayList<>();

        Predicate predicateForActive = builder.isTrue(root.get("isActive"));
        predicates.add(predicateForActive);
        if (requestBody.getContent() != null) {
            Predicate predicateForDescription =
                    builder.like(root.get("name"), "%" + requestBody.getContent() + "%");
            predicates.add(predicateForDescription);
            predicateForDescription =
                    builder.like(root.get("description"), "%" + requestBody.getContent() + "%");
            predicates.add(predicateForDescription);
        }

        if (requestBody.getTagNames() != null) {
            CriteriaBuilder.In<String> inTags = builder.in(tags.get("name"));
            for (String tagName : requestBody.getTagNames()) {
                inTags.value(tagName);
            }
            predicates.add(inTags);
            query.groupBy(root.get("id"));
            query.having(builder.equal(builder.count(root.get("id")), requestBody.getTagNames().size()));
        }
        Predicate[] predArray = new Predicate[predicates.size()];
        predicates.toArray(predArray);
        query.where(predArray);
    }

    private void removeDeletedTags(List<GiftCertificate> giftCertificates) {
        giftCertificates.forEach(g -> g.getTags().removeIf(tag -> !tag.isActive()));
    }

    @Override
    public int getLastPage(int size) {
        return persistenceService.getLastPage(GET_CERTIFICATE_COUNT, size);
    }

    @Override
    public GiftCertificate addGiftCertificate(GiftCertificate giftCertificate) throws PersistenceException {
        giftCertificate.setActive(ACTIVE_CERTIFICATE);
        return persistenceService.add(giftCertificate);
    }

    @Override
    public void deleteGiftCertificate(int id) {
        GiftCertificate giftCertificate = persistenceService.getModelById(id);
        if (giftCertificate == null) {
            throw new NoResultException("Failed to find certificate to delete by id:" + id);
        }
        giftCertificate.setActive(DELETED_CERTIFICATE);
        updateGiftCertificate(giftCertificate);
    }

    @Override
    public GiftCertificate updateGiftCertificate(GiftCertificate giftCertificate) {
        giftCertificate.setActive(ACTIVE_CERTIFICATE);
        return persistenceService.update(giftCertificate);
    }
}
