package com.epam.esm.web.controller;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.service.request.CertificateRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public CollectionModel<GiftCertificate> getGiftCertificates(@RequestBody(required = false) CertificateRequestBody request,
             @RequestParam int limit, @RequestParam int offset) throws PersistenceException {
        CollectionModel<GiftCertificate> giftCertificates =
                CollectionModel.of(giftCertificateService.getGiftCertificates(request, limit, offset));
        giftCertificates.add(linkTo(methodOn(CertificateController.class)
                .getGiftCertificates(null, limit, offset)).withSelfRel());
       return giftCertificates;
    }

    @GetMapping("/certificates/{id}")
    public GiftCertificate getGiftCertificate(@PathVariable int id) throws PersistenceException {
        GiftCertificate giftCertificate = giftCertificateService.getGiftCertificate(id);
        giftCertificate.add(linkTo(CertificateController.class).slash(id).withSelfRel());
        giftCertificate.add(linkTo(methodOn(CertificateController.class)
                .getGiftCertificates(null, DEFAULT_LIMIT, DEFAULT_OFFSET)).withRel("GiftCertificates"));
        return giftCertificate;
    }

    @PostMapping("/certificates")
    public GiftCertificate addGiftCertificate(@RequestBody GiftCertificate giftCertificate)
            throws PersistenceException {
        giftCertificate = giftCertificateService.addGiftCertificate(giftCertificate);
        giftCertificate.add(linkTo(methodOn(CertificateController.class)
                .getGiftCertificate(giftCertificate.getId())).withRel("addedCertificate"));
        giftCertificate.add(linkTo(methodOn(CertificateController.class)
                .getGiftCertificate(giftCertificate.getId())).withRel("certificates"));
        return giftCertificate;
    }

    @DeleteMapping("/certificates/{id}")
    public HttpStatus deleteGiftCertificate(@PathVariable int id) throws PersistenceException {
        giftCertificateService.deleteGiftCertificate(id);
        return HttpStatus.OK;
    }

    @PutMapping("/certificates/{id}")
    public GiftCertificate updateGiftCertificate(
            @RequestBody GiftCertificate giftCertificate, @PathVariable int id) throws PersistenceException {
        giftCertificate = giftCertificateService.updateGiftCertificate(giftCertificate, id);
        giftCertificate.add(linkTo(methodOn(CertificateController.class)
                .getGiftCertificate(giftCertificate.getId())).withRel("updatedCertificate"));
        giftCertificate.add(linkTo(methodOn(CertificateController.class)
                .getGiftCertificate(giftCertificate.getId())).withRel("certificates"));
        return giftCertificate;
    }
}
