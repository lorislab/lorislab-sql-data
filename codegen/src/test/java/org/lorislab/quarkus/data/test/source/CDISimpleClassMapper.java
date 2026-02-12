package org.lorislab.quarkus.data.test.source;

import org.lorislab.quarkus.data.Mapper;
import org.lorislab.quarkus.data.test.data.CustomAnnotation;

import io.vertx.mutiny.sqlclient.Row;

@Mapper(anno = { CustomAnnotation.class })
public abstract class CDISimpleClassMapper {

    public abstract Model map(Row row);

    public void test() {

    }

}
