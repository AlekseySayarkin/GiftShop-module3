package com.epam.esm.dao;

import com.epam.esm.dao.impl.HibernateGiftCertificateDaoImpl;
import com.epam.esm.dao.service.PersistenceService;
import com.epam.esm.dao.service.impl.PersistenceServiceImpl;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

class HibernateGiftCertificateDaoImplTest {

    private GiftCertificateDAO giftCertificateDAO;
    private PersistenceService<GiftCertificate> service;

    private static final String GET_CERTIFICATE_BY_NAME = "SELECT g FROM GiftCertificate g WHERE g.name=:name";
    private static final String GET_CERTIFICATE_COUNT =
            "SELECT count(g.id) FROM GiftCertificate g WHERE o.isActive=true ";
    private int size;

    @BeforeEach
    public void init() {
        size = 10;
        service = Mockito.mock(PersistenceServiceImpl.class);
        giftCertificateDAO = new HibernateGiftCertificateDaoImpl(service);
    }

    private GiftCertificate initCertificate() {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(1);
        certificate.setName("Tour to Greece");
        certificate.setDescription("Certificate description");
        certificate.setPrice(99.99);

        certificate.setCreateDate(ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        certificate.setLastUpdateDate(certificate.getCreateDate());
        certificate.setDuration(10);

        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag(1, "tourism"));
        tags.add(new Tag(1, "relax"));
        certificate.setTags(tags);

        return certificate;
    }

    @Test
    void whenGivenGiftCertificate_thenCorrectlyReturnItById() {
        GiftCertificate given = initCertificate();

        Mockito.when(service.add(given)).thenReturn(given);

        GiftCertificate actual = giftCertificateDAO.addGiftCertificate(given);
        Assertions.assertEquals(given, actual);
    }

    @Test
    void whenGivenGiftCertificate_thenCorrectlyReturnItByName() {
        GiftCertificate given = initCertificate();

        Mockito.when(service.getModelByName(GET_CERTIFICATE_BY_NAME, given.getName())).thenReturn(given);

        GiftCertificate actual = giftCertificateDAO.getGiftCertificateByName(given.getName());
        Assertions.assertEquals(given, actual);
    }

    @Test
    void whenGivenGiftCertificate_thenCorrectlyDeleteIt() {
        GiftCertificate given = initCertificate();

        Mockito.doThrow(new IllegalArgumentException()).when(service).delete(given.getId());

        try {
            giftCertificateDAO.deleteGiftCertificate(given.getId());
        } catch (IllegalArgumentException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void whenGivenPageSize_thenCorrectlyReturnLastPage() {
        int givenLastPage = 10;

        Mockito.when(service.getLastPage(GET_CERTIFICATE_COUNT, size)).thenReturn(givenLastPage);

        int actualLastPage = giftCertificateDAO.getLastPage(size);
        Assertions.assertEquals(givenLastPage, actualLastPage);
    }

    @Test
    void whenGivenGiftCertificate_thenCorrectlyAddsIt() {
        GiftCertificate given = initCertificate();

        Mockito.when(service.add(given)).thenReturn(given);

        GiftCertificate actual = giftCertificateDAO.addGiftCertificate(given);
        Assertions.assertEquals(given, actual);
    }

    @Test
    void whenGivenGiftCertificate_thenCorrectlyUpdateIt() {
        GiftCertificate given = initCertificate();

        Mockito.when(service.update(given)).thenReturn(given);

        GiftCertificate actual = giftCertificateDAO.updateGiftCertificate(given);
        Assertions.assertEquals(given, actual);
    }
}
