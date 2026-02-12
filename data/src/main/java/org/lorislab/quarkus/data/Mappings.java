package org.lorislab.quarkus.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Repeatable annotation for the {@code SqlMapping}
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface Mappings {

    Mapping[] value();
}
