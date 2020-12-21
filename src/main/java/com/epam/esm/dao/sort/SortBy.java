package com.epam.esm.dao.sort;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum SortBy {
    @JsonProperty("name")
    @JsonAlias("Name")
    NAME,

    @JsonProperty("date")
    @JsonAlias("DATE")
    DATE,

    @JsonProperty("cost")
    @JsonAlias("COST")
    COST
}
