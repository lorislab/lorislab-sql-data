package org.lorislab.quarkus.data.test.source;

import org.lorislab.quarkus.data.EnumType;
import org.lorislab.quarkus.data.Mapper;
import org.lorislab.quarkus.data.Mapping;

import io.vertx.mutiny.sqlclient.Row;

@Mapper(cdi = false, instanceField = true, staticMethod = true)
public interface SimpleMapper {

    Model map1(Row row);

    @Mapping(field = "id", column = "uid")
    @Mapping(field = "status", ignore = true)
    @Mapping(field = "statusInteger", ignore = true)
    @Mapping(field = "parent", column = "p")
    Model mapSqlMapping3(Row row);

    @Mapping(field = "id", column = "uid")
    @Mapping(field = "status", ignore = true)
    @Mapping(field = "statusInteger", enumType = EnumType.STRING)
    @Mapping(field = "parent", column = "p")
    Model mapSqlMapping4(Row row);
}
