package com.epam.esm.dao.request;

import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;

public class UserRequestBody {

    private String login;
    private SortType sortType;
    private SortBy sortBy;

    public static UserRequestBody getDefaultUserRequestBody() {
        UserRequestBody userRequestBody = new UserRequestBody();
        userRequestBody.setSortBy(SortBy.NAME);
        userRequestBody.setSortType(SortType.ASC);

        return userRequestBody;
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
