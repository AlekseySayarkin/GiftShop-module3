package com.epam.esm.dao.request;

import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;

public class UserSearchCriteria {

    private String login;
    private SortType sortType;
    private SortBy sortBy;

    public static UserSearchCriteria getDefaultUserRequestBody() {
        UserSearchCriteria userSearchCriteria = new UserSearchCriteria();
        userSearchCriteria.setSortBy(SortBy.NAME);
        userSearchCriteria.setSortType(SortType.ASC);

        return userSearchCriteria;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    public SortBy getSortBy() {
        return sortBy;
    }

    public void setSortBy(SortBy sortBy) {
        this.sortBy = sortBy;
    }
}
