package com.epam.esm.service.request;

public class CertificateRequestBody {

    private String content;
    private SortType sortType;
    private SortParameter sortBy;
    private String tagName;
    private int limit;
    private int offset;

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

    public SortParameter getSortBy() {
        return sortBy;
    }

    public void setSortBy(SortParameter sortBy) {
        this.sortBy = sortBy;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
