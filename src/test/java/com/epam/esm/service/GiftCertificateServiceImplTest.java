package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.impl.HibernateGiftCertificateDaoImpl;
import com.epam.esm.dao.request.CertificateSearchCriteria;
import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.service.util.impl.CertificateValidatorImpl;
import com.epam.esm.service.util.impl.PaginationValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

class GiftCertificateServiceImplTest {

    private GiftCertificateDAO giftCertificateDAO;
    private GiftCertificateService giftCertificateService;
    private int page;
    private int size;

    @BeforeEach
    public void init() {
        page = 1;
        size = 10;
        giftCertificateDAO = Mockito.mock(HibernateGiftCertificateDaoImpl.class);

        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateDAO,
                new CertificateValidatorImpl(), new PaginationValidatorImpl());
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
    void whenGetCertificate_thenCorrectlyReturnsItById() throws ServiceException {
        GiftCertificate given = initCertificate();

        Mockito.when(giftCertificateDAO.getGiftCertificateById(given.getId())).thenReturn(given);

        GiftCertificate actual = giftCertificateService.getGiftCertificateById(given.getId());
        Assertions.assertEquals(given, actual);
        Mockito.verify(giftCertificateDAO).getGiftCertificateById(given.getId());
    }

    @Test
    void whenGetCertificate_thenCorrectlyReturnsItByName() throws ServiceException {
        GiftCertificate given = initCertificate();

        Mockito.when(giftCertificateDAO.getGiftCertificateByName(given.getName())).thenReturn(given);

        GiftCertificate actual = giftCertificateService.getGiftCertificateByName(given.getName());
        Assertions.assertEquals(given, actual);
        Mockito.verify(giftCertificateDAO).getGiftCertificateByName(given.getName());
    }

    @Test
    void whenAddGiftCertificates_thenCorrectlyReturnThem() throws ServiceException {
        List<GiftCertificate> given = initCertificates();
        CertificateSearchCriteria givenSearchCriteria = CertificateSearchCriteria.getDefaultCertificateRequestBody();

        Mockito.when(giftCertificateDAO.getGiftCertificatesByRequestBody(givenSearchCriteria, size, page))
                .thenReturn(given);

        List<GiftCertificate> actual = giftCertificateService.getGiftCertificatesByPage(givenSearchCriteria, size, page,
                givenSearchCriteria.getSortType(), givenSearchCriteria.getSortBy());
        Assertions.assertEquals(given, actual);
        Mockito.verify(giftCertificateDAO).getGiftCertificatesByRequestBody(givenSearchCriteria, size, page);
    }

    @Test
    void whenAddCertificate_thenReturnItId() throws ServiceException {
        GiftCertificate givenCertificate = initCertificate();

        Mockito.when(giftCertificateDAO.addGiftCertificate(givenCertificate)).thenReturn(givenCertificate);

        GiftCertificate actual = giftCertificateService.addGiftCertificate(givenCertificate);
        Assertions.assertEquals(actual, givenCertificate);
        Mockito.verify(giftCertificateDAO).addGiftCertificate(givenCertificate);
    }

    @Test
    void whenTryAddEmptyCertificate_thenThrowException() {
        GiftCertificate giftCertificate = new GiftCertificate();

        try {
            giftCertificateService.addGiftCertificate(giftCertificate);
        } catch (ServiceException e) {
            Assertions.assertEquals("Failed to validate: certificate name is empty", e.getMessage());
        }
    }

    @Test
    void whenTryDeleteCertificate_thenThrowException() {
        GiftCertificate givenCertificate = initCertificate();

        try {
            giftCertificateService.deleteGiftCertificate(givenCertificate.getId());
        } catch (ServiceException e) {
            Assertions.assertEquals(
                    "Failed to get certificate by it id: " + givenCertificate.getId(), e.getMessage());
        }
    }

    @Test
    void whenAddCertificate_thenReturnThemSortedByDateAsc() throws ServiceException {
        CertificateSearchCriteria givenSearchCriteria = new CertificateSearchCriteria();
        givenSearchCriteria.setSortType(SortType.ASC);
        givenSearchCriteria.setSortBy(SortBy.CREATE_DATE);
        List<GiftCertificate> givenCertificates = initCertificates();

        Mockito.when(giftCertificateDAO.getGiftCertificatesByRequestBody(givenSearchCriteria, page, size))
                .thenReturn(givenCertificates);

        List<GiftCertificate> actual = giftCertificateService.getGiftCertificatesByPage(
                givenSearchCriteria, page, size, givenSearchCriteria.getSortType(), givenSearchCriteria.getSortBy());
        Assertions.assertEquals(givenCertificates, actual);
        Mockito.verify(giftCertificateDAO).getGiftCertificatesByRequestBody(givenSearchCriteria, page, size);
    }

    @Test
    void whenAddCertificate_thenReturnThemSortedByDateDesc() throws ServiceException {
        CertificateSearchCriteria givenSearchCriteria = new CertificateSearchCriteria();
        givenSearchCriteria.setSortType(SortType.DESC);
        givenSearchCriteria.setSortBy(SortBy.CREATE_DATE);
        List<GiftCertificate> givenCertificates = initCertificates();

        Mockito.when(giftCertificateDAO.getGiftCertificatesByRequestBody(givenSearchCriteria, page, size))
                .thenReturn(givenCertificates);

        List<GiftCertificate> actual = giftCertificateService.getGiftCertificatesByPage(
                givenSearchCriteria, page, size, givenSearchCriteria.getSortType(), givenSearchCriteria.getSortBy());
        Assertions.assertEquals(givenCertificates, actual);
        Mockito.verify(giftCertificateDAO).getGiftCertificatesByRequestBody(givenSearchCriteria, page, size);
    }
}
