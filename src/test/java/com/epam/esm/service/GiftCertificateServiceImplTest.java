package com.epam.esm.service;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.service.search.criteria.CertificateSearchCriteria;
import com.epam.esm.service.util.impl.CertificateValidatorImpl;
import com.epam.esm.service.util.impl.PaginationValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
class GiftCertificateServiceImplTest {

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    @Mock
    private GiftCertificateRepository certificateRepository;

    private final static int PAGE = 1;
    private final static int SIZE = 10;

    @BeforeEach
    public void setUp() {
        var certificateValidator = new CertificateValidatorImpl();
        var paginationValidator = new PaginationValidatorImpl();
        giftCertificateService = new GiftCertificateServiceImpl(
                certificateRepository, certificateValidator, paginationValidator
        );
    }

    private List<GiftCertificate> initCertificates() {
        var certificates = new ArrayList<GiftCertificate>();
        IntStream.rangeClosed(1, 10).forEach(i -> {
            var certificate = initCertificate();
            certificate.setName("name" + i);
            certificates.add(certificate);
        });

        return certificates;
    }

    private GiftCertificate initCertificate() {
        var giftCertificate = new GiftCertificate();
        giftCertificate.setId(1);
        giftCertificate.setName("Tourism");
        giftCertificate.setDescription("Description");
        giftCertificate.setPrice(10);
        giftCertificate.setCreateDate(ZonedDateTime.now());
        giftCertificate.setLastUpdateDate(ZonedDateTime.now());
        giftCertificate.setDuration(10);
        var tag = new Tag(1, "spa");
        giftCertificate.getTags().add(tag);

        return giftCertificate;
    }

    @Test
    void whenGetCertificate_thenCorrectlyReturnsItById() throws ServiceException {
        var given = initCertificate();

        Mockito.when(certificateRepository.findById(given.getId())).thenReturn(java.util.Optional.of(given));

        var actual = giftCertificateService.getGiftCertificateById(given.getId());
        Assertions.assertEquals(given, actual);
        Mockito.verify(certificateRepository).findById(given.getId());
    }

    @Test
    @SuppressWarnings("unchecked")
    void whenAddGiftCertificates_thenCorrectlyReturnThem() throws ServiceException {
        var given = initCertificates();
        var givenSearchCriteria = CertificateSearchCriteria.getDefaultCertificateRequestBody();
        givenSearchCriteria.setContent("content");

        Mockito.when(
                certificateRepository.findAll(Mockito.isA(Specification.class), Mockito.isA(Pageable.class))
        ).thenReturn(new PageImpl<>(given));

        var actual = giftCertificateService.getGiftCertificatesByPage(
                givenSearchCriteria, PAGE, SIZE, givenSearchCriteria.getSortType(), givenSearchCriteria.getSortBy()
        );
        Assertions.assertEquals(given, actual);
        Mockito.verify(certificateRepository).findAll(Mockito.isA(Specification.class), Mockito.isA(Pageable.class));
    }

    @Test
    void whenAddCertificate_thenReturnItId() throws ServiceException {
        var givenCertificate = initCertificate();

        Mockito.when(certificateRepository.save(givenCertificate)).thenReturn(givenCertificate);

        var actual = giftCertificateService.addGiftCertificate(givenCertificate);
        Assertions.assertEquals(actual, givenCertificate);
        Mockito.verify(certificateRepository).save(givenCertificate);
    }

    @Test
    void whenTryAddEmptyCertificate_thenThrowException() {
        var giftCertificate = new GiftCertificate();

        try {
            giftCertificateService.addGiftCertificate(giftCertificate);
        } catch (ServiceException e) {
            Assertions.assertEquals("Failed to validate: certificate name is empty", e.getMessage());
        }
    }

    @Test
    void whenTryDeleteCertificate_thenThrowException() {
        var givenCertificate = initCertificate();

        try {
            giftCertificateService.deleteGiftCertificate(givenCertificate.getId());
        } catch (ServiceException e) {
            Assertions.assertEquals(
                    "Failed to get certificate by it id: " + givenCertificate.getId(), e.getMessage()
            );
        }
    }
}
