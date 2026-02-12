package org.lorislab.quarkus.data.sql;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Update extends SqlOp {

    public static Update update(String table) {
        return new Update(table);
    }

    int count;

    String returning;

    String table;

    String columns;

    String where;

    Update(String table) {
        this.table = table;
    }

    @Override
    public String build() {
        return build(table);
    }

    @Override
    public String build(String table) {
        String sql = "UPDATE " + table + " SET " + columns;
        if (where != null) {
            sql += " WHERE " + where;
        }
        if (returning != null) {
            sql += " RETURNING " + returning;
        }
        return sql;
    }

    public Update set(String... columns) {
        this.count = columns.length;
        this.columns = IntStream.rangeClosed(1, count).boxed().map(x -> columns[x - 1] + "=$" + x)
                .collect(Collectors.joining(","));
        return this;
    }

    public Update where(Op... ops) {
        int begin = count + 1;
        where = IntStream.rangeClosed(begin, count + ops.length)
                .boxed()
                .map(x -> ops[x - begin].get() + "$" + x)
                .collect(Collectors.joining(" AND "));
        return this;
    }

    public Update returning(String... returning) {
        this.returning = String.join(",", returning);
        return this;
    }
}
