package com.epam.esm.dao.request;

import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;

public class TagSearchCriteria {

    private SortType sortType;
    private SortBy sortBy;

    public static TagSearchCriteria getDefaultTagRequestBody() {
        TagSearchCriteria tagSearchCriteria = new TagSearchCriteria();
        tagSearchCriteria.setSortBy(SortBy.NAME);
        tagSearchCriteria.setSortType(SortType.ASC);

        return tagSearchCriteria;
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
