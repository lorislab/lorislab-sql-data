package org.lorislab.quarkus.data.sql;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Select extends SqlOp {

    public static String all() {
        return "*";
    }

    public static String count(String... columns) {
        return "COUNT(" + String.join(",", columns) + ")";
    }

    public static String count() {
        return count(all());
    }

    public static Select select() {
        return new Select(all());
    }

    public static Select select(String col, String... cols) {
        return new Select(col, cols);
    }

    private String columns;

    private String table;

    private String where;

    private String orderBy;

    private String groupBy;

    private String extend;

    private String limit;

    private String offset;

    Select(String col, String... cols) {
        if (cols.length > 0) {
            this.columns = col + "," + String.join(",", Arrays.asList(cols));
        } else {
            this.columns = col;
        }
    }

    public Select selectCount(String col, String... cols) {
        return selectCount().groupBy(col, cols);
    }

    public Select selectCount() {
        var s = new Select(count());
        s.table = table;
        s.where = where;
        s.orderBy = orderBy;
        s.groupBy = groupBy;
        s.extend = extend;
        s.limit = limit;
        s.offset = offset;
        return s;
    }

    @Override
    public String build() {
        return build(table);
    }

    @Override
    public String build(String table) {
        String sql = "SELECT " + columns + " FROM " + table;
        if (where != null) {
            sql += " WHERE " + where;
        }
        if (groupBy != null) {
            sql += " GROUP BY " + groupBy;
        }
        if (orderBy != null) {
            sql += " ORDER BY " + orderBy;
        }
        if (extend != null) {
            sql += " FOR " + extend;
        }
        if (limit != null) {
            sql += " LIMIT " + limit;
        }
        if (offset != null) {
            sql += " OFFSET " + offset;
        }
        return sql;
    }

    public Select from(String table) {
        this.table = table;
        return this;
    }

    public Select where(Op... ops) {
        where = IntStream.rangeClosed(1, ops.length)
                .boxed()
                .map(x -> ops[x - 1].get() + "$" + x)
                .collect(Collectors.joining(" AND "));
        return this;
    }

    public Select where(List<Op> ops) {
        where(ops.toArray(new Op[0]));
        return this;
    }

    public Select orderBy(Order... orders) {
        this.orderBy = Arrays.stream(orders).map(Supplier::get).collect(Collectors.joining(","));
        return this;
    }

    public Select groupBy(String col, String... cols) {
        if (cols.length > 0) {
            this.groupBy = col + "," + String.join(",", Arrays.asList(cols));
        } else {
            this.groupBy = col;
        }
        return this;
    }

    public Select extend(For... items) {
        this.extend = Arrays.stream(items).map(Supplier::get).collect(Collectors.joining(" "));
        return this;
    }

    public Select limit(String limit) {
        this.limit = limit;
        return this;
    }

    public Select limit(int limit) {
        return limit("" + limit);
    }

    public Select offset(String offset) {
        this.offset = offset;
        return this;
    }

    public Select offset(int offset) {
        return offset("" + offset);
    }
}
