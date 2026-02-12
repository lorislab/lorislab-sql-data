package org.lorislab.quarkus.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SQL mapper annotation.
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.CLASS)
public @interface Mapper {

    /**
     * Generate mapper as CDI bean
     *
     * @return generate mapper as CDI bean.
     */
    boolean cdi() default true;

    /**
     * Mapper CDI bean scoped.
     *
     * @return the mapper CDI bean scoped.
     */
    String cdiScoped() default "jakarta.enterprise.context.ApplicationScoped";

    /**
     * Mapper CDI bean annotations.
     *
     * @return the mapper CDI bean annotations.
     */
    Class[] anno() default {};

    /**
     * Define the suffix of the mapper implementation class.
     *
     * @return the suffix of the mapper implementation class.
     */
    String suffix() default "Impl";

    /**
     * Define the suffix of the static method.
     *
     * @return the suffix of the static method.
     */
    String staticMethodSuffix() default "S";

    /**
     * Disable or enabled static method generator.
     *
     * @return the flag to disable or enable static method.
     */
    boolean staticMethod() default false;

    /**
     * Define the static field name of the mapper.
     *
     * @return the static field name.
     */
    String instanceName() default "INSTANCE";

    /**
     * Create implementation instance static field.
     *
     * @return implementation instance static field.
     */
    boolean instanceField() default false;

    /**
     * Buffer class for the mapper.
     */
    String bufferClass() default "io.vertx.mutiny.core.buffer.Buffer";
}
