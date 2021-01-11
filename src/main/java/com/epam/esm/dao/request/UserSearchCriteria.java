package com.epam.esm.dao.request;

import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;

public class UserSearchCriteria extends SortingSearchCriteria {

    private String login;

    public static UserSearchCriteria getDefaultUserRequestBody() {
        UserSearchCriteria userSearchCriteria = new UserSearchCriteria();
        userSearchCriteria.setSortBy(SortBy.LOGIN);
        userSearchCriteria.setSortType(SortType.ASC);

        return userSearchCriteria;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
