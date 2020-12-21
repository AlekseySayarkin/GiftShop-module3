package com.epam.esm.web.controller;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.dao.request.CertificateSearchCriteria;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.web.dto.GiftCertificateDto;
import com.epam.esm.web.hateoas.CertificateLinkBuilder;
import com.epam.esm.web.hateoas.ModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
public class CertificateController {

    private final GiftCertificateService giftCertificateService;
    private final ModelAssembler<GiftCertificateDto> modelAssembler;

    @Autowired
    public CertificateController(
            GiftCertificateService giftCertificateService, ModelAssembler<GiftCertificateDto> modelAssembler) {
        this.giftCertificateService = giftCertificateService;
        this.modelAssembler = modelAssembler;
    }

    @PostConstruct
    public void init() {
        modelAssembler.setModelLinkBuilder(new CertificateLinkBuilder());
    }

    @GetMapping("/certificates")
    public CollectionModel<EntityModel<GiftCertificateDto>> getGiftCertificates(
            @RequestBody(required = false) CertificateSearchCriteria request,
            @RequestParam int page, @RequestParam int size) throws ServiceException {
        int lastPage = giftCertificateService.getLastPage(size);
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                size, page, (long) lastPage * size, lastPage);
        modelAssembler.setMetadata(pageMetadata);

        return modelAssembler.toCollectionModel(
                GiftCertificateDto.of(giftCertificateService.getGiftCertificatesByPage(request, page, size)));
    }

    @GetMapping("/certificates/{id}")
    public EntityModel<GiftCertificateDto> getGiftCertificate(@PathVariable int id) throws ServiceException {
        return modelAssembler.toModel(GiftCertificateDto.of(giftCertificateService.getGiftCertificateById(id)));
    }

    @PostMapping("/certificates")
    public EntityModel<GiftCertificateDto> addGiftCertificate(@RequestBody GiftCertificate giftCertificate)
            throws ServiceException {
        return modelAssembler.toModel(
                GiftCertificateDto.of(giftCertificateService.addGiftCertificate(giftCertificate)));
    }

    @DeleteMapping("/certificates/{id}")
    public HttpStatus deleteGiftCertificate(@PathVariable int id) throws ServiceException {
        giftCertificateService.deleteGiftCertificate(id);
        return HttpStatus.OK;
    }

    @PutMapping("/certificates/{id}")
    public EntityModel<GiftCertificateDto> updateGiftCertificate(
            @RequestBody GiftCertificate giftCertificate, @PathVariable int id) throws ServiceException {
        return modelAssembler.toModel(
                GiftCertificateDto.of(giftCertificateService.updateGiftCertificate(giftCertificate, id)));
    }
}
