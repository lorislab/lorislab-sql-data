package org.lorislab.quarkus.data.codegen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

import org.lorislab.quarkus.data.Mapping;

public class MethodInfo {

    private final ExecutableElement element;

    public MethodInfo(ExecutableElement element) {
        this.element = element;
    }

    public static MethodInfo of(ExecutableElement element) {
        return new MethodInfo(element);
    }

    public ExecutableElement getElement() {
        return element;
    }

    public boolean isDefault() {
        return element.isDefault();
    }

    public boolean isAbstract() {
        return element.getModifiers().contains(Modifier.ABSTRACT);
    }

    public String getName() {
        return element.getSimpleName().toString();
    }

    public String getReturnClass() {
        return ((TypeElement) ((DeclaredType) element.getReturnType()).asElement())
                .getQualifiedName()
                .toString();
    }

    public List<Param> getParameters(ProcessingEnvironment processingEnv) {
        List<Param> result = new ArrayList<>();
        for (var p : element.getParameters()) {
            String paramName = p.getSimpleName().toString();
            String fqn = Utils.fqn(processingEnv, p);
            result.add(new Param(paramName, fqn));
        }
        return result;
    }

    public Map<String, Mapping> getMappings() {
        Mapping[] am = element.getAnnotationsByType(Mapping.class);
        return Stream.of(am).collect(Collectors.toMap(Mapping::field, Function.identity()));
    }

    public record Param(String name, String clazz) {
        String inputs() {
            return clazz + " " + name;
        }
    }
}
