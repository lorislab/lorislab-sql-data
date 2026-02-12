package org.lorislab.quarkus.data.sql;

record Pagination(long page, int size, boolean requestTotal) implements PageRequest {

    Pagination {
        if (page < 1) {
            throw new IllegalArgumentException("pageNumber: " + page);
        } else if (size < 1) {
            throw new IllegalArgumentException("maxPageSize: " + size);
        }
    }
}
