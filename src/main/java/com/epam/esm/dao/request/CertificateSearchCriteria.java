package com.epam.esm.dao.request;

import com.epam.esm.dao.sort.SortType;
import com.epam.esm.dao.sort.SortBy;

import java.util.List;

public class CertificateSearchCriteria {

    private String content;
    private SortType sortType;
    private SortBy sortBy;
    private List<String> tagNames;
    private int size;
    private int page;

    public static CertificateSearchCriteria getDefaultCertificateRequestBody() {
        CertificateSearchCriteria certificateSearchCriteria = new CertificateSearchCriteria();
        certificateSearchCriteria.setSortBy(SortBy.NAME);
        certificateSearchCriteria.setSortType(SortType.ASC);

        return certificateSearchCriteria;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public List<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}