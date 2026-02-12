package org.lorislab.quarkus.data.codegen;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

import org.lorislab.quarkus.data.Column;
import org.lorislab.quarkus.data.EnumType;
import org.lorislab.quarkus.data.Id;
import org.lorislab.quarkus.data.Mapping;

public class FieldInfo {

    private static final String REGEX = "([a-z])([A-Z]+)";
    private static final String REPLACEMENT = "$1_$2";

    private final Element element;

    public static FieldInfo of(Element element) {
        return new FieldInfo(element);
    }

    public FieldInfo(Element element) {
        this.element = element;
    }

    public Element getElement() {
        return element;
    }

    public String getName() {
        return element.getSimpleName().toString();
    }

    public String getConstName() {
        return getName().replaceAll(REGEX, REPLACEMENT).toUpperCase();
    }

    public boolean hasId() {
        return element.getAnnotation(Id.class) != null;
    }

    public Column getColumn() {
        return element.getAnnotation(Column.class);
    }

    public TypeInfo getFieldInfo(ProcessingEnvironment processingEnv) {
        return Utils.typeInfoOf(processingEnv, element);
    }

    public String getColumnName() {
        var c = getColumn();
        if (c != null && !c.name().isEmpty()) {
            return c.name().toLowerCase();
        }
        return getName().toLowerCase();
    }

    public FieldMapping getFieldMapping(Mapping mapping, String columnConst) {
        var ignore = false;
        var enumType = EnumType.DEFAULT;
        String columnName = null;

        var column = getColumn();
        if (column != null) {
            ignore = column.ignore();
            enumType = column.enumType();
        }

        if (ignore) {
            return new FieldMapping(ignore, columnName, columnConst, enumType, getName().toLowerCase());
        }

        if (mapping != null) {
            ignore = mapping.ignore();
            if (!mapping.column().isBlank()) {
                columnName = mapping.column();
            }
            if (mapping.enumType() != EnumType.DEFAULT) {
                enumType = mapping.enumType();
            }
        }

        return new FieldMapping(ignore, columnName, columnConst, enumType, getName().toLowerCase());
    }

    public record FieldMapping(boolean ignore, String column, String columnConst, EnumType enumType, String fieldColumnName) {
        String getColumnSourceCode() {
            if (column != null) {
                return "\"" + column + "\"";
            }
            if (columnConst != null) {
                return columnConst;
            }
            return fieldColumnName;
        }
    }
}
