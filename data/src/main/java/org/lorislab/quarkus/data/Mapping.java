package org.lorislab.quarkus.data;

import java.lang.annotation.*;

/**
 * SQL mapping optional configuration.
 */
@Repeatable(Mappings.class)
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface Mapping {

    /**
     * The field name.
     *
     * @return the field name.
     */
    String field();

    /**
     * The column name. Default is field or {@code SqlColumn} value.
     *
     * @return the column name.
     */
    String column() default "";

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
