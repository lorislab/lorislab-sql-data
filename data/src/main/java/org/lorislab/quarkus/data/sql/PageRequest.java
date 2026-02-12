package org.lorislab.quarkus.data.sql;

public interface PageRequest {

    static PageRequest ofPage(long pageNumber, int maxPageSize, boolean requestTotal) {
        return new Pagination(pageNumber, maxPageSize, requestTotal);
    }

    static PageRequest ofSize(int maxPageSize) {
        return ofSize(maxPageSize, true);
    }

    static PageRequest ofSize(int maxPageSize, boolean requestTotal) {
        return ofPage(1, maxPageSize, requestTotal);
    }

    long page();

    int size();

    boolean requestTotal();
}
