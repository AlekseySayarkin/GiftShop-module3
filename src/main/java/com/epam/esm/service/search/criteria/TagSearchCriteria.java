package com.epam.esm.service.search.criteria;

import com.epam.esm.service.search.sort.SortBy;
import com.epam.esm.service.search.sort.SortType;

public class TagSearchCriteria extends SortingSearchCriteria {

    private final static SortBy DEFAULT_SORT_BY = SortBy.NAME;
    private final static SortType DEFAULT_SORT_TYPE = SortType.ASC;

    public static TagSearchCriteria getDefaultTagRequestBody() {
        TagSearchCriteria tagSearchCriteria = new TagSearchCriteria();
        tagSearchCriteria.setSortBy(DEFAULT_SORT_BY);
        tagSearchCriteria.setSortType(DEFAULT_SORT_TYPE);

        return tagSearchCriteria;
    }
}
