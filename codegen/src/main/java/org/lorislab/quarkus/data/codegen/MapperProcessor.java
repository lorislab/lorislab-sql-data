package org.lorislab.quarkus.data.codegen;

import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import org.lorislab.quarkus.data.Entity;
import org.lorislab.quarkus.data.Mapper;

@SupportedAnnotationTypes({ "org.lorislab.quarkus.data.Mapper", "org.lorislab.quarkus.data.Entity" })
public class MapperProcessor extends AbstractProcessor {

    public static final String OPTION_DEBUG = "debug";

    private static final int MAX_ROUND = 20;
    private int round;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        if (!roundEnv.processingOver() && round > MAX_ROUND) {
            messager().printMessage(Diagnostic.Kind.ERROR,
                    "Processor possible processing loop detected (" + (MAX_ROUND + 1) + ")");
        }
        round++;

        if (annotations.isEmpty()) {
            return false;
        }
        var inputs = annotations.stream().map(TypeElement::getQualifiedName).map(Object::toString).toList();
        if (!inputs.contains(Entity.class.getName()) && !inputs.contains(Mapper.class.getName())) {
            return false;
        }

        // find all entities
        var entities = roundEnv.getElementsAnnotatedWith(Entity.class)
                .stream()
                .filter(x -> x.getKind() == ElementKind.CLASS)
                .map(ClassInfo::of)
                .collect(Collectors.toMap(classInfo -> classInfo.getTypeElement().getQualifiedName().toString(), x -> x));

        // generate entity metamodel
        entities.forEach((name, clazz) -> {
            try {
                EntityMetamodelWriter.generate(processingEnv, clazz);
            } catch (Exception e) {
                e.printStackTrace();
                messager().printMessage(Diagnostic.Kind.ERROR, "Writing entity metamodel class failed", clazz.getTypeElement());
            }
        });

        // find all mappers
        var mappers = roundEnv.getElementsAnnotatedWith(Mapper.class)
                .stream()
                .filter(x -> x.getKind() == ElementKind.CLASS || x.getKind() == ElementKind.INTERFACE)
                .map(ClassInfo::of)
                .toList();

        // generate mapper implementation
        for (var clazz : mappers) {
            var element = clazz.getTypeElement();
            if (!clazz.isInterface() && !clazz.isAbstractClass()) {
                messager().printMessage(Diagnostic.Kind.ERROR, "Mapper is not interface or abstract class. The element: "
                        + element + " kind: " + element.getKind() + " will be ignored.");
                continue;
            }

            messager().printMessage(Diagnostic.Kind.NOTE, "Mapper processing interface: " + element);
            try {
                MapperClassWriter.generate(entities, processingEnv, clazz);
            } catch (Exception e) {
                e.printStackTrace();
                messager().printMessage(Diagnostic.Kind.ERROR, "Writing mapper implementation class failed",
                        clazz.getTypeElement());
            }
        }
        ;

        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(Mapper.class.getCanonicalName(), Entity.class.getCanonicalName());
    }

    @Override
    public Set<String> getSupportedOptions() {
        return Set.of(OPTION_DEBUG);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_25;
    }

    private Messager messager() {
        return processingEnv.getMessager();
    }

}
