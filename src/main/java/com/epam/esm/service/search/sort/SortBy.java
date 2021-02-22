package com.epam.esm.service.search.sort;

public enum SortBy {

    NAME("Name"), CREATE_DATE("CreateDate"), COST("TotalCost"), LOGIN("Login");

    private final String name;

    SortBy(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

