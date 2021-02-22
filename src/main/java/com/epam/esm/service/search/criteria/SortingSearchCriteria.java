package com.epam.esm.service.search.criteria;

import com.epam.esm.service.search.sort.SortBy;
import com.epam.esm.service.search.sort.SortType;
import org.springframework.data.domain.Sort;

public abstract class SortingSearchCriteria {

    private SortType sortType;
    private SortBy sortBy;

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

    public Sort getSort() {
        return sortType.equals(SortType.ASC) ?
                Sort.by(sortBy.getName()).ascending() :
                Sort.by(sortBy.getName()).descending();
    }
}
