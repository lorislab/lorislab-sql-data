package org.lorislab.quarkus.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define new column name for the mapper.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface Column {

    /**
     * Column name. The field name is the default name.
     *
     * @return the new column name for the mapper.
     */
    String name() default "";

    /**
     * Ignore field for the mapping
     *
     * @return ignore field flag.
     */
    boolean ignore() default false;

    /**
     * Only for enumeration. Define the enumeration mapping type.
     *
     * @return the enumeration mapping type.
     */
    EnumType enumType() default EnumType.DEFAULT;
}
