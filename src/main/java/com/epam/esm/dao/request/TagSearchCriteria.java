package com.epam.esm.dao.request;

import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;

public class TagSearchCriteria extends SortingSearchCriteria {

    public static TagSearchCriteria getDefaultTagRequestBody() {
        TagSearchCriteria tagSearchCriteria = new TagSearchCriteria();
        tagSearchCriteria.setSortBy(SortBy.NAME);
        tagSearchCriteria.setSortType(SortType.ASC);

        return tagSearchCriteria;
    }
}
