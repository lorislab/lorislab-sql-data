package org.lorislab.quarkus.data.sql;

import java.util.function.Supplier;

public interface Order extends Supplier<String> {

    static Order asc(String column) {
        return () -> column + " ASC";
    }
}
