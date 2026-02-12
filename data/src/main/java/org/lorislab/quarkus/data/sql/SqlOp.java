package org.lorislab.quarkus.data.sql;

public abstract class SqlOp {

    public abstract String build();

    public abstract String build(String table);
}
