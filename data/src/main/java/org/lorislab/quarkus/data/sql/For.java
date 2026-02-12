package org.lorislab.quarkus.data.sql;

import java.util.function.Supplier;

public interface For extends Supplier<String> {

    static For update() {
        return () -> "UPDATE";
    }

    static For skipLocked() {
        return () -> "SKIP LOCKED";
    }
}
