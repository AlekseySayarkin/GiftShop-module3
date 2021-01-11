package com.epam.esm.dao.request;

import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;

public class OrderSearchCriteria extends SortingSearchCriteria {

    public static OrderSearchCriteria getDefaultUserRequestBody() {
        OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
        orderSearchCriteria.setSortBy(SortBy.COST);
        orderSearchCriteria.setSortType(SortType.ASC);

        return orderSearchCriteria;
    }
}
