package com.epam.esm.dao.request;

import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;

public class OrderSearchCriteria {

    private SortType sortType;
    private SortBy sortBy;

    public static OrderSearchCriteria getDefaultUserRequestBody() {
        OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
        orderSearchCriteria.setSortBy(SortBy.COST);
        orderSearchCriteria.setSortType(SortType.ASC);

        return orderSearchCriteria;
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
