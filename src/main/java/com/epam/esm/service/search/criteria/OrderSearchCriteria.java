package com.epam.esm.service.search.criteria;

import com.epam.esm.service.search.sort.SortBy;
import com.epam.esm.service.search.sort.SortType;

public class OrderSearchCriteria extends SortingSearchCriteria {

    private final static SortBy DEFAULT_SORT_BY = SortBy.COST;
    private final static SortType DEFAULT_SORT_TYPE = SortType.ASC;

    public static OrderSearchCriteria getDefaultOrderRequestBody() {
        OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
        orderSearchCriteria.setSortBy(DEFAULT_SORT_BY);
        orderSearchCriteria.setSortType(DEFAULT_SORT_TYPE);

        return orderSearchCriteria;
    }
}
