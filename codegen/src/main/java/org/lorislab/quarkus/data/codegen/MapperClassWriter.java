package org.lorislab.quarkus.data.codegen;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeKind;
import javax.tools.Diagnostic;

public class MapperClassWriter {

    public static void generate(Map<String, ClassInfo> entities, ProcessingEnvironment processingEnv, ClassInfo clazz)
            throws Exception {

        var packageName = processingEnv.getElementUtils()
                .getPackageOf(clazz.getTypeElement())
                .getQualifiedName()
                .toString();

        var mapper = clazz.getMapper();
        var className = clazz.getName() + mapper.suffix();
        var fileName = packageName.isEmpty() ? className : packageName + "." + className;

        var sb = new StringBuilder();
        if (!packageName.isEmpty()) {
            sb.append(String.format("package %s;\n\n", packageName));
        }

        // add custom annotation
        var ca = clazz.getMapperCustomAnnotations(processingEnv.getElementUtils());
        if (ca != null) {
            ca.forEach(item -> sb.append(String.format("@%s\n", item)));
        }

        // add CDI scope annotation
        if (mapper.cdi()) {
            sb.append(String.format("@%s\n", mapper.cdiScoped()));
        }

        // create class
        if (clazz.isInterface()) {
            sb.append(String.format("public class %s implements %s {\n\n", className, clazz.getName()));
        } else {
            sb.append(String.format("public class %s extends %s {\n\n", className, clazz.getName()));
        }

        // create instance of the class
        if (mapper.instanceField()) {
            sb.append(
                    String.format("  public static final %s %s = new %s();\n\n", className, mapper.instanceName(), className));
        }

        // create methods
        var methods = clazz.getMethodsToGenerate();
        methods.forEach(method -> {

            var returnClass = method.getReturnClass();
            var params = method.getParameters(processingEnv);
            var inputs = params.stream().map(MethodInfo.Param::inputs).collect(Collectors.joining(","));

            sb.append("  @Override\n");
            sb.append(String.format("  public %s %s(%s) {\n", returnClass, method.getName(), inputs));

            // check if null
            var ifValues = params.stream().map(p -> String.format("%s == null", p.name())).collect(Collectors.joining(" || "));
            sb.append(String.format("    if (%s) {\n      return null;\n    }\n\n", ifValues));

            // create instance
            sb.append(String.format("    %s result = new %s();\n\n", returnClass, returnClass));

            // idx
            sb.append("    int idx;\n\n");

            // create mapping

            var entity = entities.get(returnClass);
            var metamodel = entity.getMetamodel();
            var mappings = method.getMappings();

            for (var field : entity.getFields()) {

                var name = field.getName();

                // create mapping for the field
                var mapping = field.getFieldMapping(mappings.get(name), metamodel.getColumnConst(name));
                if (mapping.ignore()) {
                    continue;
                }

                var setter = Utils.findSetter(entity, field, processingEnv.getTypeUtils());

                var typeInfo = field.getFieldInfo(processingEnv);
                var type = typeInfo.getQualifiedName();

                if (typeInfo.isEnum()) {
                    switch (mapping.enumType()) {
                        case STRING -> sb.append(String.format("""
                                    java.lang.String %s;
                                    if ((idx = row.getColumnIndex(%s)) != -1 && (%s = row.getString(idx)) != null) {
                                        %s
                                    }

                                """, name, mapping.getColumnSourceCode(), name,
                                setResultValue(name, setter, getEnumValue(type, name))));
                        case INTEGER -> sb.append(String.format("""
                                    java.lang.Integer %s;
                                    if ((idx = row.getColumnIndex(%s)) != -1 && (%s = row.getInteger(idx)) != null) {
                                        if (0 <= %s && %s < %s.values().length) {
                                            %s
                                        }
                                    }

                                """, name, mapping.getColumnSourceCode(), name, name, name, type,
                                setResultValue(name, setter, getEnumIntValue(type, name))));
                    }
                    continue;
                } else {
                    var collection = Utils.createCollection(processingEnv, field);
                    if (collection != null) {
                        if (collection.isArray()) {

                            var itemSimpleName = collection.getItemTypeSimpleName();
                            var itemKind = collection.getItemTypeKind();

                            if (itemKind == TypeKind.BYTE) {
                                sb.append(String.format("""
                                            %s %s;
                                            if ((idx = row.getColumnIndex(%s)) != -1 && (%s = row.getBuffer(idx)) != null) {
                                                %s
                                            }

                                        """, mapper.bufferClass(), name, mapping.getColumnSourceCode(), name,
                                        setResultValue(name, setter, name + ".getBytes()")));
                            } else {
                                var typeName = collection.getPrimitiveObjectClass();
                                var defaultValue = collection.getPrimitiveTypeDefault();
                                sb.append(String.format("""
                                            %s[] %s;
                                            if ((idx = row.getColumnIndex(%s)) != -1 && (%s = row.getArrayOf%ss(idx)) != null) {
                                                %s[] tmp = new %s[%s.length];
                                                for (int i = 0; i < %s.length; i++) {
                                                    tmp[i] = %s[i] != null ? %s[i] : %s;
                                                }
                                                %s
                                            }

                                        """, typeName.getName(), name, mapping.getColumnSourceCode(), name,
                                        typeName.getSimpleName(), itemSimpleName, itemSimpleName, name, name, name, name,
                                        defaultValue, setResultValue(name, setter, "tmp")));
                            }
                            continue;
                        } else if (collection.getKind() == CollectionInfo.CollectionKind.LIST) {
                            var itemType = collection.getItemTypeFqn();

                            sb.append(String.format("""
                                        %s[] %s;
                                        if ((idx = row.getColumnIndex(%s)) != -1 && (%s = row.getArrayOf%ss(idx)) != null) {
                                          %s
                                        }

                                    """, itemType.fqn(), name, mapping.getColumnSourceCode(), name, itemType.simpleName(),
                                    setResultValue(name, setter, getArraysAsList(name))));

                            continue;
                        } else if (collection.getKind() == CollectionInfo.CollectionKind.SET) {
                            var itemType = collection.getItemTypeFqn();

                            sb.append(String.format("""
                                        %s[] %s;
                                        if ((idx = row.getColumnIndex(%s)) != -1 && (%s = row.getArrayOf%ss(idx)) != null) {
                                          %s
                                        }

                                    """, itemType.fqn(), name, mapping.getColumnSourceCode(), name, itemType.simpleName(),
                                    setResultValue(name, setter, getArraysAsSet(name))));
                            continue;
                        }
                    } else {
                        if (typeInfo.isPrimitive()) {
                            var typeName = typeInfo.getPrimitiveObjectClass();
                            var defaultValue = typeInfo.getPrimitiveTypeDefault();
                            sb.append(String.format("""
                                        %s %s%s;
                                        if ((idx = row.getColumnIndex(%s)) != -1 && (%s = row.get%s(idx)) != null) {
                                            %s
                                        }
                                        %s
                                    """, typeName.getName(), name, initVariable(field.hasId()), mapping.getColumnSourceCode(),
                                    name, typeName.getSimpleName(),
                                    setResultValue(name, setter, getOptionalValue(name, defaultValue)),
                                    checkNullVariable(field.hasId(), name)));
                            continue;
                        } else {
                            var objectName = typeInfo.getQualifiedName();
                            sb.append(String.format("""
                                        %s %s%s;
                                        if ((idx = row.getColumnIndex(%s)) != -1 && (%s = row.get%s(idx)) != null) {
                                            %s
                                        }
                                        %s
                                    """, objectName, name, initVariable(field.hasId()), mapping.getColumnSourceCode(), name,
                                    typeInfo.getSimpleName(), setResultValue(name, setter, name),
                                    checkNullVariable(field.hasId(), name)));
                            continue;
                        }
                    }

                }
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        "Not supported type " + clazz.getName() + " for the field " + field.getName(), field.getElement());
            }

            // return statement
            sb.append("    return result;\n");
            sb.append("  }\n\n");

            // static method wrapper for instance field
            if (mapper.instanceField() && mapper.staticMethod()) {
                var staticInputs = params.stream().map(MethodInfo.Param::name).collect(Collectors.joining(","));
                sb.append(String.format("  public static %s %s(%s) {\n", returnClass,
                        method.getName() + mapper.staticMethodSuffix(), inputs));
                sb.append(String.format("    return %s.%s(%s);\n", mapper.instanceName(), method.getName(), staticInputs));
                sb.append("  }\n\n");
            }
        });

        sb.append("}\n");

        Utils.createJavaFile(processingEnv, fileName, sb.toString());
    }

    private static String setResultValue(String property, MethodInfo setter, String value) {
        if (setter != null) {
            return String.format("result.%s(%s);", setter.getName(), value);
        }
        return String.format("result.%s = %s;", property, value);
    }

    private static String getEnumValue(String type, String value) {
        return String.format("%s.valueOf(%s)", type, value);
    }

    private static String getEnumIntValue(String type, String value) {
        return String.format("%s.values()[%s]", type, value);
    }

    private static String getOptionalValue(String value, String defaultValue) {
        return String.format("java.util.Optional.of(%s).orElse(%s)", value, defaultValue);
    }

    private static String getArraysAsList(String value) {
        return String.format("java.util.Arrays.asList(%s)", value);
    }

    private static String getArraysAsSet(String value) {
        return String.format("new %s<>(%s.asList(%s))", HashSet.class.getName(), Arrays.class.getName(), value);
    }

    private static String initVariable(boolean init) {
        return init ? " = null" : "";
    }

    private static String checkNullVariable(boolean id, String name) {
        return id ? String.format("""
                if ( %s == null) { return null; }

                """, name) : "";
    }
}
