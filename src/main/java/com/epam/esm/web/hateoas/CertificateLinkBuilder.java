package com.epam.esm.web.hateoas;

import com.epam.esm.dao.request.CertificateRequestBody;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.web.controller.CertificateController;
import com.epam.esm.web.dto.GiftCertificateDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CertificateLinkBuilder implements ModelLinkBuilder<GiftCertificateDto> {

    private static final String ALL_CERTIFICATES = "certificates";
    private static final String CURRENT_CERTIFICATE = "certificate";

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final CertificateRequestBody defaultRequestBody =
            CertificateRequestBody.getDefaultCertificateRequestBody();

    @Override
    public void linkToModel(EntityModel<GiftCertificateDto> modelDto) throws ServiceException {
        modelDto.add(linkTo(methodOn(CertificateController.class).getGiftCertificate(
                Objects.requireNonNull(modelDto.getContent()).getId())).withRel(CURRENT_CERTIFICATE));
    }

    @Override
    public void linkToModelPage(CollectionModel<EntityModel<GiftCertificateDto>> collectionModel, int page, int size)
            throws ServiceException {
        collectionModel.add(getLinkToCertificatesPage(page, size, ALL_CERTIFICATES));
    }

    @Override
    public void linkToFirstModelPage(EntityModel<GiftCertificateDto> model) throws ServiceException {
        model.add(getLinkToCertificatesPage(DEFAULT_PAGE, DEFAULT_SIZE, ALL_CERTIFICATES));
    }

    @Override
    public void linkToNextModelPage(CollectionModel<EntityModel<GiftCertificateDto>> collectionModel, int page, int size)
            throws ServiceException {
        collectionModel.add(getLinkToCertificatesPage(page + 1, size, "next"));
    }

    @Override
    public void linkToPrevModelPage(CollectionModel<EntityModel<GiftCertificateDto>> collectionModel, int page, int size)
            throws ServiceException {
        collectionModel.add(getLinkToCertificatesPage(page - 1, size, "prev"));
    }

    private Link getLinkToCertificatesPage(int page, int size, String rel) throws ServiceException {
        return linkTo(methodOn(CertificateController.class)
                .getGiftCertificates(defaultRequestBody, page, size)).withRel(rel);
    }
}
