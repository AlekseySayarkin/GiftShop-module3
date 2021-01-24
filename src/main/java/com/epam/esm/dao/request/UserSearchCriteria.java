package com.epam.esm.dao.request;

import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;

public class UserSearchCriteria extends SortingSearchCriteria {

    private final static SortBy DEFAULT_SORT_BY = SortBy.LOGIN;
    private final static SortType DEFAULT_SORT_TYPE = SortType.ASC;

    private String login;

    public static UserSearchCriteria getDefaultUserRequestBody() {
        UserSearchCriteria userSearchCriteria = new UserSearchCriteria();
        userSearchCriteria.setSortBy(DEFAULT_SORT_BY);
        userSearchCriteria.setSortType(DEFAULT_SORT_TYPE);

        return userSearchCriteria;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
