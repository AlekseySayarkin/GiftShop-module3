package com.epam.esm.dao.request;

import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;

public class OrderRequestBody {

    private SortType sortType;
    private SortBy sortBy;

    public static OrderRequestBody getDefaultUserRequestBody() {
        OrderRequestBody orderRequestBody = new OrderRequestBody();
        orderRequestBody.setSortBy(SortBy.NAME);
        orderRequestBody.setSortType(SortType.ASC);

        return orderRequestBody;
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
