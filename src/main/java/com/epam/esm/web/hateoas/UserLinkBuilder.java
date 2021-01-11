package com.epam.esm.web.hateoas;

import com.epam.esm.dao.request.UserSearchCriteria;
import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.service.exception.ServiceException;
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
    private static final String CURRENT_USERS = "self";

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final UserSearchCriteria defaultRequestBody = UserSearchCriteria.getDefaultUserRequestBody();

    @Override
    public void linkToModel(EntityModel<UserDto> modelDto) throws ServiceException {
        modelDto.add(linkTo(methodOn(UserController.class).getUser(
                Objects.requireNonNull(modelDto.getContent()).getId())).withRel(CURRENT_USERS));
    }

    @Override
    public void linkToModelPage(CollectionModel<EntityModel<UserDto>> collectionModel, int page, int size,
                                SortType sortType, SortBy sortBy) throws ServiceException {
        collectionModel.add(getLinkToUsersPage(page, size, ALL_USERS, sortType, sortBy));
    }

    @Override
    public void linkToFirstModelPage(EntityModel<UserDto> tagDto, SortType sortType, SortBy sortBy)
            throws ServiceException {
        tagDto.add(getLinkToUsersPage(DEFAULT_PAGE, DEFAULT_SIZE, ALL_USERS, sortType, sortBy));
    }

    @Override
    public void linkToNextModelPage(CollectionModel<EntityModel<UserDto>> collectionModel, int page, int size,
                                    SortType sortType, SortBy sortBy) throws ServiceException {
        collectionModel.add(getLinkToUsersPage(page + 1, size, "next", sortType, sortBy));
    }

    @Override
    public void linkToPrevModelPage(CollectionModel<EntityModel<UserDto>> collectionModel, int page, int size,
                                    SortType sortType, SortBy sortBy) throws ServiceException {
        collectionModel.add(getLinkToUsersPage(page - 1, size, "prev", sortType, sortBy));
    }

    @Override
    public void linkToLastModelPage(CollectionModel<EntityModel<UserDto>> collectionModel, int lastPage, int size, SortType sortType, SortBy sortBy) throws ServiceException {
        collectionModel.add(getLinkToUsersPage(lastPage, size, "last", sortType, sortBy));
    }

    private Link getLinkToUsersPage(int page, int size, String rel,
                                    SortType sortType, SortBy sortBy) throws ServiceException {
        return linkTo(methodOn(UserController.class).getUsers(defaultRequestBody, page, size,
                sortType, sortBy)).withRel(rel);
    }
}
