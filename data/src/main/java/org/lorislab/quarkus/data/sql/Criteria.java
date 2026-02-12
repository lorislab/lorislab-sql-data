package org.lorislab.quarkus.data.sql;

import java.util.ArrayList;
import java.util.List;

public class Criteria {

    private final List<Op> ops = new ArrayList<>();

    private final List<Object> values = new ArrayList<>();

    public void addNotNull(Op op, Object value) {
        if (value == null) {
            return;
        }
        add(op, value);
    }

    public void add(Op op, Object value) {
        ops.add(op);
        values.add(value);
    }

    public List<Op> ops() {
        return ops;
    }

    public List<Object> values() {
        return values;
    }

}
