package com.epam.esm.web.controller;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.dao.exception.DaoException;
import com.epam.esm.service.request.CertificateRequestBody;
import com.epam.esm.web.dto.GiftCertificateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class CertificateController {

    private final GiftCertificateService giftCertificateService;

    private static final int DEFAULT_LIMIT = 10;
    private static final int DEFAULT_OFFSET = 0;

    @Autowired
    public CertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping("/certificates")
    public CollectionModel<GiftCertificate> getGiftCertificates(
            @RequestBody CertificateRequestBody request) throws DaoException {
        CollectionModel<GiftCertificate> giftCertificates =
                CollectionModel.of(giftCertificateService.getGiftCertificates(request));
        giftCertificates.add(linkTo(methodOn(CertificateController.class)
                .getGiftCertificates(null)).withSelfRel());
       return giftCertificates;
    }

    @GetMapping("/certificates/{id}")
    public GiftCertificateDto getGiftCertificate(@PathVariable int id) throws DaoException {
        GiftCertificateDto giftCertificate = GiftCertificateDto.of(giftCertificateService.getGiftCertificate(id));
        giftCertificate.add(linkTo(CertificateController.class).slash(id).withSelfRel());
        giftCertificate.add(linkTo(methodOn(CertificateController.class)
                .getGiftCertificates(null)).withRel("GiftCertificates")); //set def limit and offset
        return giftCertificate;
    }

    @PostMapping("/certificates")
    public GiftCertificateDto addGiftCertificate(@RequestBody GiftCertificate giftCertificate)
            throws DaoException {
        GiftCertificateDto giftCertificateDto =
                GiftCertificateDto.of(giftCertificateService.addGiftCertificate(giftCertificate));
        giftCertificateDto.add(linkTo(methodOn(CertificateController.class)
                .getGiftCertificate(giftCertificate.getId())).withRel("addedCertificate"));
        giftCertificateDto.add(linkTo(methodOn(CertificateController.class)
                .getGiftCertificate(giftCertificate.getId())).withRel("certificates"));
        return giftCertificateDto;
    }

    @DeleteMapping("/certificates/{id}")
    public HttpStatus deleteGiftCertificate(@PathVariable int id) throws DaoException {
        giftCertificateService.deleteGiftCertificate(id);
        return HttpStatus.OK;
    }

    @PutMapping("/certificates/{id}")
    public GiftCertificateDto updateGiftCertificate(
            @RequestBody GiftCertificate giftCertificate, @PathVariable int id) throws DaoException {
        GiftCertificateDto giftCertificateDto =
                GiftCertificateDto.of(giftCertificateService.updateGiftCertificate(giftCertificate, id));
        giftCertificateDto.add(linkTo(methodOn(CertificateController.class)
                .getGiftCertificate(giftCertificate.getId())).withRel("updatedCertificate"));
        giftCertificateDto.add(linkTo(methodOn(CertificateController.class)
                .getGiftCertificate(giftCertificate.getId())).withRel("certificates"));
        return giftCertificateDto;
    }
}
