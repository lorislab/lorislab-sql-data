package org.lorislab.quarkus.data.test.source;

import org.lorislab.quarkus.data.Mapper;

import io.vertx.mutiny.sqlclient.Row;

@Mapper(cdi = false, instanceField = true, staticMethod = true)
public abstract class SimpleClassMapper {

    public abstract Model map(Row row);

    public void test() {

    }

}
