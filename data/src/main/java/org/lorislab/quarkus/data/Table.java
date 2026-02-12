package org.lorislab.quarkus.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Table {

    /**
     * Column name. The field name is the default name.
     *
     * @return the new column name for the mapper.
     */
    String name() default "";
}
