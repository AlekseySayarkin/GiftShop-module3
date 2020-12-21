package com.epam.esm.dao.sort;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum SortType {
    @JsonProperty("asc")
    @JsonAlias("ASC")
    ASC,

    @JsonProperty("desc")
    @JsonAlias("DESC")
    DESC
}
