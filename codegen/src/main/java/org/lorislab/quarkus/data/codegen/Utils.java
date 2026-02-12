package org.lorislab.quarkus.data.codegen;

import static org.lorislab.quarkus.data.codegen.MapperProcessor.OPTION_DEBUG;

import java.io.Writer;
import java.util.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;

public class Utils {

    public static String getTypeDefaultValue(TypeKind kind) {
        return switch (kind) {
            case TypeKind.BOOLEAN -> "false";
            case TypeKind.INT, TypeKind.LONG, TypeKind.BYTE, TypeKind.SHORT, TypeKind.FLOAT, TypeKind.DOUBLE -> "0";
            default -> throw new IllegalStateException("Unexpected value: " + kind);
        };
    }

    public static Class<?> getTypeKindObject(TypeKind kind) {
        return switch (kind) {
            case TypeKind.INT -> Integer.class;
            case TypeKind.LONG -> Long.class;
            case TypeKind.BOOLEAN -> Boolean.class;
            case TypeKind.BYTE -> Byte.class;
            case TypeKind.SHORT -> Short.class;
            case TypeKind.FLOAT -> Float.class;
            case TypeKind.DOUBLE -> Double.class;
            default -> throw new IllegalStateException("Unexpected value: " + kind);
        };
    }

    public static CollectionInfo createCollection(ProcessingEnvironment processingEnv, FieldInfo field) {
        Types types = processingEnv.getTypeUtils();
        Elements elements = processingEnv.getElementUtils();

        TypeElement collectionElement = elements.getTypeElement(Collection.class.getName());

        var typeMirror = field.getElement().asType();
        if (types.isAssignable(types.erasure(typeMirror), types.erasure(collectionElement.asType()))) {

            var kind = getCollectionKind(types, elements, typeMirror);

            if (typeMirror.getKind() == TypeKind.DECLARED) {
                DeclaredType declared = (DeclaredType) typeMirror;
                List<? extends TypeMirror> args = declared.getTypeArguments();
                if (!args.isEmpty()) {
                    return new CollectionInfo(field, args.getFirst(), kind);
                }
            }
            return new CollectionInfo(field, null, kind);
        }

        if (typeMirror.getKind() == TypeKind.ARRAY) {
            ArrayType arrayType = (ArrayType) typeMirror;
            return new CollectionInfo(field, arrayType.getComponentType(), CollectionInfo.CollectionKind.ARRAY);
        }
        return null;
    }

    private static CollectionInfo.CollectionKind getCollectionKind(Types types, Elements elements, TypeMirror typeMirror) {
        TypeMirror erased = types.erasure(typeMirror);
        if (types.isAssignable(erased, elements.getTypeElement(List.class.getName()).asType()))
            return CollectionInfo.CollectionKind.LIST;

        if (types.isAssignable(erased, elements.getTypeElement(Set.class.getName()).asType()))
            return CollectionInfo.CollectionKind.SET;

        if (types.isAssignable(erased, elements.getTypeElement(Queue.class.getName()).asType()))
            return CollectionInfo.CollectionKind.QUEUE;

        if (types.isAssignable(erased, elements.getTypeElement(Deque.class.getName()).asType()))
            return CollectionInfo.CollectionKind.DEQUE;

        if (types.isAssignable(erased, elements.getTypeElement(SortedSet.class.getName()).asType()))
            return CollectionInfo.CollectionKind.SORTED_SET;

        if (types.isAssignable(erased, elements.getTypeElement(NavigableSet.class.getName()).asType()))
            return CollectionInfo.CollectionKind.NAVIGABLE_SET;

        return CollectionInfo.CollectionKind.OTHER;
    }

    public static MethodInfo findSetter(ClassInfo clazz, FieldInfo field, Types types) {
        String fieldName = field.getName();
        TypeMirror fieldType = field.getElement().asType();
        String setterName = "set" + capitalize(fieldName);

        for (var method : clazz.getMethods()) {
            if (!method.getName().equals(setterName))
                continue;
            var params = method.getElement().getParameters();
            if (params.size() != 1)
                continue;
            if (isTypeCompatible(params.getFirst().asType(), fieldType, types)) {
                return method;
            }
        }
        return null;
    }

    private static String capitalize(String name) {
        if (name == null || name.isEmpty())
            return name;
        if (name.length() > 1 &&
                Character.isUpperCase(name.charAt(0)) &&
                Character.isUpperCase(name.charAt(1))) {
            return name;
        }
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    private static boolean isTypeCompatible(TypeMirror a, TypeMirror b, Types types) {
        // direct match
        if (types.isSameType(a, b))
            return true;

        // safely box primitives
        TypeMirror boxedA = boxIfPrimitive(a, types);
        TypeMirror boxedB = boxIfPrimitive(b, types);

        if (types.isSameType(boxedA, boxedB))
            return true;

        // assignability
        return types.isAssignable(boxedA, boxedB) || types.isAssignable(boxedB, boxedA);
    }

    private static TypeMirror boxIfPrimitive(TypeMirror t, Types types) {
        if (t.getKind().isPrimitive() && t instanceof PrimitiveType) {
            return types.boxedClass((PrimitiveType) t).asType();
        }
        return t;
    }

    public static TypeInfo typeInfoOf(ProcessingEnvironment processingEnv, Element element) {
        TypeElement typeElement = (TypeElement) processingEnv.getTypeUtils().asElement(element.asType());
        var packageName = processingEnv.getElementUtils().getPackageOf(element);
        return new TypeInfo(element, typeElement, packageName);
    }

    public static String fqn(ProcessingEnvironment processingEnv, Element element) {
        Types types = processingEnv.getTypeUtils();
        Elements elements = processingEnv.getElementUtils();
        TypeMirror typeMirror = element.asType();
        TypeElement typeElement = (TypeElement) types.asElement(typeMirror);

        if (typeElement != null) {
            return elements.getBinaryName(typeElement).toString();
        }
        return typeMirror.toString();
    }

    public static void createJavaFile(ProcessingEnvironment processingEnv, String fileName, String source) throws Exception {

        var debug = processingEnv.getOptions().get(OPTION_DEBUG);
        if (Boolean.parseBoolean(debug)) {
            System.out.println("------------------------------------------------------------------------");
            System.out.println(source);
            System.out.println("------------------------------------------------------------------------");
            System.out.println();
        }

        JavaFileObject filerSourceFile = processingEnv.getFiler().createSourceFile(fileName);
        try (Writer writer = filerSourceFile.openWriter()) {
            writer.write(source);
            writer.flush();
        } catch (Exception e) {
            try {
                filerSourceFile.delete();
            } catch (Exception ignored) {
                // Ignored
            }
            throw e;
        }
    }
}
