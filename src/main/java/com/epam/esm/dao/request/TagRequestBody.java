package com.epam.esm.dao.request;

import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;

public class TagRequestBody {

    private SortType sortType;
    private SortBy sortBy;

    public static TagRequestBody getDefaultTagRequestBody() {
        TagRequestBody tagRequestBody = new TagRequestBody();
        tagRequestBody.setSortBy(SortBy.NAME);
        tagRequestBody.setSortType(SortType.ASC);

        return tagRequestBody;
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
