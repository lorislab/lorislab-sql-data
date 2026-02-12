package org.lorislab.quarkus.data.sql;

import java.util.List;

public interface Page<T> extends Iterable<T> {

    static <E> Page<E> ofPage(Page<?> page, List<E> content) {
        var tmp = (PageRecord<?>) page;
        return PageRecord.of(tmp.pageRequest(), content, page.totalElements());
    }

    List<T> content();

    boolean hasContent();

    int numberOfElements();

    boolean hasNext();

    boolean hasPrevious();

    boolean hasTotals();

    long totalElements();

    long totalPages();
}
