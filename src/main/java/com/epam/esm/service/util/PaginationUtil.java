package com.epam.esm.service.util;

public class PaginationUtil {

    public static int getLastPage(int count, int size) {
        int pages = count / size;
        if (count % size > 0) {
            pages++;
        }

        return pages;
    }
}
