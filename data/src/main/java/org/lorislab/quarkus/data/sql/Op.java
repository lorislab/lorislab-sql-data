package org.lorislab.quarkus.data.sql;

import java.util.function.Supplier;

public interface Op extends Supplier<String> {

    static Op equal(String column) {
        return () -> column + "=";
    }

    static Op notEqual(String column) {
        return () -> column + "<>";
    }

    static Op lessThan(String column) {
        return () -> column + "<";
    }

    static Op lessThanOrEqualTo(String column) {
        return () -> column + "<=";
    }

}
