package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.impl.HibernateGiftCertificateDaoImpl;
import com.epam.esm.dao.impl.HibernateTagDaoImpl;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.dao.exception.DaoException;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.service.request.CertificateRequestBody;
import com.epam.esm.service.request.SortParameter;
import com.epam.esm.service.request.SortType;
import com.epam.esm.service.util.impl.CertificateValidatorImpl;
import com.epam.esm.service.util.impl.PaginationValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class GiftCertificateServiceImplTest {

    private TagDao tagDao;
    private GiftCertificateDAO giftCertificateDAO;
    private GiftCertificateService giftCertificateService;
    private int limit;
    private int offset;

    @BeforeEach
    public void init() {
        limit = 10;
        offset = 0;
        tagDao = Mockito.mock(HibernateTagDaoImpl.class);
        giftCertificateDAO = Mockito.mock(HibernateGiftCertificateDaoImpl.class);

        giftCertificateService = new GiftCertificateServiceImpl(
                giftCertificateDAO, tagDao, new CertificateValidatorImpl(), new PaginationValidatorImpl());
    }

    private List<GiftCertificate> initCertificates() {
        List<GiftCertificate> certificates = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            GiftCertificate certificate = initCertificate();
            certificate.setName("name" + i);

            certificates.add(certificate);
        }

        return certificates;
    }

    private GiftCertificate initCertificate() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1);
        giftCertificate.setName("Tourism");
        giftCertificate.setDescription("Description");
        giftCertificate.setPrice(10);
        giftCertificate.setCreateDate(ZonedDateTime.now());
        giftCertificate.setLastUpdateDate(ZonedDateTime.now());
        giftCertificate.setDuration(10);
        Tag tag = new Tag(1, "spa");
        giftCertificate.getTags().add(tag);

        return giftCertificate;
    }

    @Test
    void whenGetCertificate_thenCorrectlyReturnsItById() throws DaoException {
        GiftCertificate given = new GiftCertificate();
        given.setId(1);
        given.setName("Tourism");

        Mockito.when(giftCertificateDAO.getGiftCertificate(given.getId())).thenReturn(given);

        GiftCertificate actual = giftCertificateService.getGiftCertificate(given.getId());
        Assertions.assertEquals(given, actual);
        Mockito.verify(giftCertificateDAO).getGiftCertificate(given.getId());
    }

    @Test
    void whenGetCertificate_thenCorrectlyReturnsItByName() throws DaoException {
        GiftCertificate given = new GiftCertificate();
        given.setId(1);
        given.setName("Tourism");

        Mockito.when(giftCertificateDAO.getGiftCertificate(given.getName())).thenReturn(given);

        GiftCertificate actual = giftCertificateService.getGiftCertificate(given.getName());
        Assertions.assertEquals(given, actual);
        Mockito.verify(giftCertificateDAO).getGiftCertificate(given.getName());
    }

    @Test
    void whenAddGiftCertificates_thenCorrectlyReturnThem() throws DaoException {
        List<GiftCertificate> given = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            GiftCertificate certificate = new GiftCertificate();
            certificate.setId(i);
            certificate.setName("Tag " + i);
            given.add(certificate);
        }

        Mockito.when(giftCertificateService.getCertificatesByPage(limit, offset)).thenReturn(given);

        List<GiftCertificate> actual = giftCertificateService.getCertificatesByPage(limit, offset);
        Assertions.assertEquals(given, actual);
        Mockito.verify(giftCertificateDAO).getGiftCertificatesByPage(limit, offset);
    }

    @Test
    void whenAddCertificate_thenReturnItId() throws DaoException {
        GiftCertificate givenCertificate = initCertificate();
        Tag givenTag = new Tag(1,"spa");
        int expectedId = 1;

        Mockito.when(tagDao.addTag(givenTag)).thenReturn(1);
        Mockito.when(giftCertificateDAO.createCertificateTagRelation(
                givenCertificate.getId(), givenTag.getId())).thenReturn(true);
        Mockito.when(giftCertificateDAO.addGiftCertificate(givenCertificate)).thenReturn(expectedId);

        givenCertificate = giftCertificateService.addGiftCertificate(givenCertificate);
        Assertions.assertEquals(expectedId, givenCertificate.getId());
        Mockito.verify(tagDao).addTag(givenTag);
        Mockito.verify(giftCertificateDAO)
                .createCertificateTagRelation(givenCertificate.getId(), givenCertificate.getId());
        Mockito.verify(giftCertificateDAO).addGiftCertificate(givenCertificate);
    }

    @Test
    void whenTryAddVoidCertificate_thenThrowException() {
        GiftCertificate giftCertificate = new GiftCertificate();

        try {
            giftCertificateService.addGiftCertificate(giftCertificate);
        } catch (DaoException e) {
            Assertions.assertEquals("Failed to validate: certificate name is empty", e.getMessage());
        }
    }

    @Test
    void whenTryDeleteCertificate_thenThrowException() {
        GiftCertificate givenCertificate = initCertificate();

        try {
            giftCertificateService.deleteGiftCertificate(givenCertificate.getId());
        } catch (DaoException e) {
            Assertions.assertEquals("Failed to delete certificate: certificate not found", e.getMessage());
        }
    }

    @Test
    void whenAddCertificate_thenReturnThemSortedByDateAsc() throws DaoException {
        CertificateRequestBody givenRequestBody = new CertificateRequestBody();
        givenRequestBody.setSortType(SortType.ASC);
        givenRequestBody.setSortBy(SortParameter.DATE);
        givenRequestBody.setLimit(10);
        givenRequestBody.setOffset(0);
        List<GiftCertificate> givenCertificates = initCertificates();

        Mockito.when(giftCertificateDAO.getGiftCertificatesSortedByDate(true, limit, offset))
                .thenReturn(givenCertificates);

        List<GiftCertificate> actual = giftCertificateService.getGiftCertificates(
                givenRequestBody);
        Assertions.assertEquals(givenCertificates, actual);
        Mockito.verify(giftCertificateDAO).getGiftCertificatesSortedByDate(true, limit, offset);
    }

    @Test
    void whenAddCertificate_thenReturnThemSortedByDateDesc() throws DaoException {
        CertificateRequestBody givenRequestBody = new CertificateRequestBody();
        givenRequestBody.setSortType(SortType.DESC);
        givenRequestBody.setSortBy(SortParameter.DATE);
        givenRequestBody.setLimit(10);
        givenRequestBody.setOffset(0);
        List<GiftCertificate> givenCertificates = initCertificates();
        Collections.reverse(givenCertificates);

        Mockito.when(giftCertificateDAO.getGiftCertificatesSortedByDate(false, limit, offset))
                .thenReturn(givenCertificates);

        List<GiftCertificate> actual = giftCertificateService.getGiftCertificates(
                givenRequestBody);
        Assertions.assertEquals(givenCertificates, actual);
        Mockito.verify(giftCertificateDAO).getGiftCertificatesSortedByDate(false, limit, offset);
    }
}
