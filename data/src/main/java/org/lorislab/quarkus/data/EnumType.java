package org.lorislab.quarkus.data;

/**
 * Enumeration mapping types.
 */
public enum EnumType {

    /**
     * The default is equal to STRING.
     */
    DEFAULT,
    /**
     * Mapping from string column to enumeration type.
     */
    STRING,
    /**
     * Mapping from integer column to enumeration type.
     */
    INTEGER;
}
