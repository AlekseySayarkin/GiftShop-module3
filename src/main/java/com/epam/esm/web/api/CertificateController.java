package com.epam.esm.web.api;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.search.criteria.CertificateSearchCriteria;
import com.epam.esm.service.search.sort.SortBy;
import com.epam.esm.service.search.sort.SortType;
import com.epam.esm.service.util.PaginationValidator;
import com.epam.esm.web.dto.GiftCertificateDto;
import com.epam.esm.web.hateoas.CertificateLinkBuilder;
import com.epam.esm.web.hateoas.ModelAssembler;
import com.epam.esm.web.hateoas.pagination.PaginationConfigurer;
import com.epam.esm.web.hateoas.pagination.impl.PaginationConfigurerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
public class CertificateController {

    private final GiftCertificateService giftCertificateService;
    private final ModelAssembler<GiftCertificateDto> modelAssembler;
    private final PaginationConfigurer paginationConfigurer;

    @Autowired
    public CertificateController(
            GiftCertificateService giftCertificateService, ModelAssembler<GiftCertificateDto> modelAssembler,
            PaginationValidator paginationValidator) {
        this.giftCertificateService = giftCertificateService;
        this.modelAssembler = modelAssembler;
        this.paginationConfigurer = new PaginationConfigurerImpl<>(modelAssembler, paginationValidator);
    }

    @PostConstruct
    public void init() {
        modelAssembler.setModelLinkBuilder(new CertificateLinkBuilder());
    }

    @GetMapping("/certificates")
    public CollectionModel<EntityModel<GiftCertificateDto>> getGiftCertificates(
            @RequestBody(required = false) CertificateSearchCriteria request,
            @RequestParam int page, @RequestParam int size,
            @RequestParam SortType sortType, @RequestParam SortBy sortBy) throws ServiceException {
        paginationConfigurer.configure(page, size, giftCertificateService.getLastPage(size), sortType, sortBy);

        return modelAssembler.toCollectionModel(
                GiftCertificateDto.of(
                        giftCertificateService.getGiftCertificatesByPage(request, page, size, sortType, sortBy)
                )
        );
    }

    @GetMapping("/certificates/{id}")
    public EntityModel<GiftCertificateDto> getGiftCertificate(@PathVariable int id) throws ServiceException {
        return modelAssembler.toModel(GiftCertificateDto.of(giftCertificateService.getGiftCertificateById(id)));
    }

    @PostMapping("/certificates")
    @PreAuthorize("hasAuthority('certificates:write')")
    public EntityModel<GiftCertificateDto> addGiftCertificate(@RequestBody GiftCertificate giftCertificate)
            throws ServiceException {
        return modelAssembler.toModel(GiftCertificateDto.of(giftCertificateService.addGiftCertificate(giftCertificate)));
    }

    @DeleteMapping("/certificates/{id}")
    @PreAuthorize("hasAuthority('certificates:write')")
    public HttpStatus deleteGiftCertificate(@PathVariable int id) throws ServiceException {
        giftCertificateService.deleteGiftCertificate(id);
        return HttpStatus.OK;
    }

    @PutMapping("/certificates/{id}")
    @PreAuthorize("hasAuthority('certificates:write')")
    public EntityModel<GiftCertificateDto> updateGiftCertificate(
            @RequestBody GiftCertificate giftCertificate, @PathVariable int id) throws ServiceException {
        return modelAssembler.toModel(
                GiftCertificateDto.of(giftCertificateService.updateGiftCertificate(giftCertificate, id))
        );
    }
}
