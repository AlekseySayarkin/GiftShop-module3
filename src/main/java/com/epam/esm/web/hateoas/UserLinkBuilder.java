package com.epam.esm.web.hateoas;

import com.epam.esm.dao.request.UserRequestBody;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.web.controller.TagController;
import com.epam.esm.web.controller.UserController;
import com.epam.esm.web.dto.UserDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class UserLinkBuilder implements ModelLinkBuilder<UserDto> {

    private static final String ALL_USERS = "users";
    private static final String CURRENT_USERS = "user";

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final UserRequestBody defaultRequestBody = UserRequestBody.getDefaultUserRequestBody();

    @Override
    public void linkToModel(EntityModel<UserDto> modelDto) throws ServiceException {
        modelDto.add(linkTo(methodOn(TagController.class).getTag(
                Objects.requireNonNull(modelDto.getContent()).getId())).withRel(CURRENT_USERS));
    }

    @Override
    public void linkToModelPage(CollectionModel<EntityModel<UserDto>> collectionModel, int page, int size)
            throws ServiceException {
        collectionModel.add(getLinkToTagsPage(page, size, ALL_USERS));
    }

    @Override
    public void linkToFirstModelPage(EntityModel<UserDto> tagDto) throws ServiceException {
        tagDto.add(getLinkToTagsPage(DEFAULT_PAGE, DEFAULT_SIZE, ALL_USERS));
    }

    @Override
    public void linkToNextModelPage(CollectionModel<EntityModel<UserDto>> collectionModel, int page, int size)
            throws ServiceException {
        collectionModel.add(getLinkToTagsPage(page + 1, size, "next"));
    }

    @Override
    public void linkToPrevModelPage(CollectionModel<EntityModel<UserDto>> collectionModel, int page, int size)
            throws ServiceException {
        collectionModel.add(getLinkToTagsPage(page - 1, size, "prev"));
    }

    private Link getLinkToTagsPage(int page, int size, String rel) throws ServiceException {
        return linkTo(methodOn(UserController.class).getUsers(defaultRequestBody, page, size)).withRel(rel);
    }
}
