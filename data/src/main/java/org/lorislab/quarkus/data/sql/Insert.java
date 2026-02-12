package org.lorislab.quarkus.data.sql;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Insert extends SqlOp {

    public static Insert insert(String... columns) {
        return new Insert(columns);
    }

    String columns;

    String values;

    String table;

    String returning;

    Insert(String... columns) {
        columns(columns);
    }

    @Override
    public String build(String table) {
        String sql = "INSERT INTO " + table + columns + " VALUES " + values;
        if (returning != null) {
            sql += " RETURNING " + returning;
        }
        return sql;
    }

    @Override
    public String build() {
        return build(table);
    }

    public Insert into(String table) {
        this.table = table;
        return this;
    }

    public Insert columns(String... columns) {
        this.columns = Arrays.stream(columns).collect(Collectors.joining(",", " (", ")"));
        this.values = IntStream.rangeClosed(1, columns.length).boxed().map(x -> "$" + x)
                .collect(Collectors.joining(",", "(", ")"));
        return this;
    }

    public Insert returning(String... returning) {
        this.returning = String.join(" ,", returning);
        return this;
    }

}
