package com.epam.esm.dao.request;

import com.epam.esm.dao.sort.SortType;
import com.epam.esm.dao.sort.SortBy;

import java.util.List;

public class CertificateSearchCriteria extends SortingSearchCriteria {

    private String content;
    private List<String> tagNames;

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

    public List<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }
}
