package org.lorislab.quarkus.data.sql;

import java.util.Iterator;
import java.util.List;

public record PageRecord<T>(PageRequest pageRequest, List<T> content, long totalElements,
        boolean moreResults) implements Page<T> {

    public static <E> PageRecord<E> of(PageRequest pageRequest, List<E> content, long totalElements) {
        return new PageRecord<E>(pageRequest, content, totalElements);
    }

    public PageRecord(PageRequest pageRequest, List<T> content, long totalElements) {
        this(pageRequest, content, totalElements,
                content.size() == pageRequest.size()
                        && (totalElements < 0
                                || totalElements > pageRequest.size() * pageRequest.page()));
    }

    @Override
    public boolean hasContent() {
        return !content.isEmpty();
    }

    @Override
    public int numberOfElements() {
        return content.size();
    }

    @Override
    public boolean hasNext() {
        return moreResults;
    }

    @Override
    public boolean hasPrevious() {
        return pageRequest.page() > 1;
    }

    @Override
    public long totalPages() {
        int size = pageRequest.size();
        return (totalElements + size - 1) / size;
    }

    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }

    @Override
    public boolean hasTotals() {
        return totalElements >= 0;
    }
}
