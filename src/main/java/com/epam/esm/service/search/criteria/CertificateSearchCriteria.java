package com.epam.esm.service.search.criteria;

import com.epam.esm.service.search.sort.SortBy;
import com.epam.esm.service.search.sort.SortType;

import java.util.List;

public class CertificateSearchCriteria extends SortingSearchCriteria {

    private final static SortBy DEFAULT_SORT_BY = SortBy.NAME;
    private final static SortType DEFAULT_SORT_TYPE = SortType.ASC;

    private String content;
    private List<String> tagNames;

    public static CertificateSearchCriteria getDefaultCertificateRequestBody() {
        CertificateSearchCriteria certificateSearchCriteria = new CertificateSearchCriteria();
        certificateSearchCriteria.setSortBy(DEFAULT_SORT_BY);
        certificateSearchCriteria.setSortType(DEFAULT_SORT_TYPE);

        return certificateSearchCriteria;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }
}
