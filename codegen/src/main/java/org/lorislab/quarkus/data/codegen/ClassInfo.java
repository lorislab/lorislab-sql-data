package org.lorislab.quarkus.data.codegen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;

import org.lorislab.quarkus.data.Mapper;

public class ClassInfo {

    private final TypeElement typeElement;

    private Metamodel metamodel;

    public static ClassInfo of(Element element) {
        TypeElement typeElement = (TypeElement) element;
        return new ClassInfo(typeElement);
    }

    public ClassInfo(TypeElement typeElement) {
        this.typeElement = typeElement;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public String getName() {
        return typeElement.getSimpleName().toString();
    }

    public List<FieldInfo> getFields() {
        return typeElement.getEnclosedElements().stream()
                .filter(f -> f.getKind() == ElementKind.FIELD)
                .map(FieldInfo::of).toList();
    }

    public boolean isAbstractClass() {
        return typeElement.getKind() == ElementKind.CLASS && typeElement.getModifiers().contains(Modifier.ABSTRACT);
    }

    public boolean isInterface() {
        return typeElement.getKind() == ElementKind.INTERFACE;
    }

    public Mapper getMapper() {
        return typeElement.getAnnotation(Mapper.class);
    }

    public List<MethodInfo> getMethods() {
        return typeElement.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.METHOD)
                .map(e -> (ExecutableElement) e)
                .map(MethodInfo::of)
                .toList();
    }

    public List<MethodInfo> getMethodsToGenerate() {
        return getMethods().stream().filter(m -> (isInterface() && !m.isDefault()) || (!isInterface() && m.isAbstract()))
                .toList();
    }

    public List<String> getMapperCustomAnnotations(Elements elements) {
        var actionElement = elements.getTypeElement(Mapper.class.getName());
        return typeElement
                .getAnnotationMirrors().stream()
                .filter(x -> x.getAnnotationType().equals(actionElement.asType()))
                .findFirst().flatMap(mapperMirror -> mapperMirror.getElementValues().entrySet().stream()
                        .filter(e -> "anno".contentEquals(e.getKey().getSimpleName()))
                        .findFirst().map(Map.Entry::getValue)
                        .map(ClassInfo::convertList))
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    private static List<String> convertList(AnnotationValue a) {
        var value = a.getValue();
        if (!(value instanceof List<?> list)) {
            throw new IllegalArgumentException("Expected List<AnnotationValue> but got " + value);
        }

        List<AnnotationValue> tmp = (List<AnnotationValue>) a.getValue();
        return tmp.stream().map(av -> {
            DeclaredType tm = (DeclaredType) av.getValue();
            TypeElement te = (TypeElement) tm.asElement();
            return te.getQualifiedName().toString();
        }).toList();
    }

    public Metamodel getMetamodel() {
        return metamodel;
    }

    public void setMetamodel(Metamodel metamodel) {
        this.metamodel = metamodel;
    }

    public static class Metamodel {

        private final String qualifiedName;

        private final String simpleName;

        private final String packageName;

        private String tableConst;

        private String idConst;

        private final Map<String, String> fieldsConst = new HashMap<>();

        public Metamodel(String qualifiedName, String packageName, String simpleName) {
            this.qualifiedName = qualifiedName;
            this.packageName = packageName;
            this.simpleName = simpleName;
        }

        public String getQualifiedName() {
            return qualifiedName;
        }

        public String getSimpleName() {
            return simpleName;
        }

        public String getPackageName() {
            return packageName;
        }

        public String getTableConst() {
            return tableConst;
        }

        public String getIdConst() {
            return idConst;
        }

        public Map<String, String> getFieldsConst() {
            return fieldsConst;
        }

        public void setTableConst(String tableConst) {
            this.tableConst = tableConst;
        }

        public void setIdConst(String idConst) {
            this.idConst = idConst;
        }

        public String getColumnConst(String name) {
            return qualifiedName + "." + fieldsConst.get(name);
        }
    }
}
