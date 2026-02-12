package org.lorislab.quarkus.data.test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.common.truth.Truth.assertThat;
import static com.google.testing.compile.CompilationSubject.compilations;
import static com.google.testing.compile.Compiler.javac;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import javax.tools.JavaFileObject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lorislab.quarkus.data.codegen.MapperProcessor;
import org.lorislab.quarkus.data.test.source.*;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;

public class MapperProcessorTest {

    private Compiler compiler;

    @BeforeEach
    public void init() {
        compiler = javac().withOptions("-Adebug=true")
                .withProcessors(new MapperProcessor());
    }

    @Test
    public void generateEntity() {
        assertDoesNotThrow(() -> {
            Compilation compilation = compiler.compile(source(Model2.class), source(Model.class));

            assertAbout(compilations()).that(compilation)
                    .generatedSourceFile("org/lorislab/quarkus/data/test/source/Model2_")
                    .hasSourceEquivalentTo(source(Model2_.class));
            assertThat(compilation.status()).isEqualTo(Compilation.Status.SUCCESS);

            assertAbout(compilations()).that(compilation)
                    .generatedSourceFile("org/lorislab/quarkus/data/test/source/Model_")
                    .hasSourceEquivalentTo(source(Model_.class));
            assertThat(compilation.status()).isEqualTo(Compilation.Status.SUCCESS);

        });
    }

    @Test
    public void generateSqlMapper() {
        assertDoesNotThrow(() -> {
            Compilation compilation = compiler
                    .compile(source(Model.class), source(SimpleMapper.class));

            assertAbout(compilations()).that(compilation)
                    .generatedSourceFile("org/lorislab/quarkus/data/test/source/Model_")
                    .hasSourceEquivalentTo(source(Model_.class));
            assertThat(compilation.status()).isEqualTo(Compilation.Status.SUCCESS);

            assertAbout(compilations()).that(compilation)
                    .generatedSourceFile("org/lorislab/quarkus/data/test/source/SimpleMapperImpl")
                    .hasSourceEquivalentTo(source(SimpleMapperImpl.class));

            assertThat(compilation.status()).isEqualTo(Compilation.Status.SUCCESS);

        });
    }

    @Test
    public void generateAbstractClassSqlMapper() {
        assertDoesNotThrow(() -> {
            Compilation compilation = compiler.compile(source(Model.class), source(SimpleClassMapper.class));

            assertAbout(compilations()).that(compilation)
                    .generatedSourceFile("org/lorislab/quarkus/data/test/source/SimpleClassMapperImpl")
                    .hasSourceEquivalentTo(source(SimpleClassMapperImpl.class));
            assertThat(compilation.status()).isEqualTo(Compilation.Status.SUCCESS);

        });
    }

    @Test
    public void generateCDITest() {
        assertDoesNotThrow(() -> {
            Compilation compilation = compiler.compile(source(Model.class), source(CDISimpleClassMapper.class));

            assertAbout(compilations()).that(compilation)
                    .generatedSourceFile("org/lorislab/quarkus/data/test/source/CDISimpleClassMapperImpl")
                    .hasSourceEquivalentTo(source(CDISimpleClassMapperImpl.class));
            assertThat(compilation.status()).isEqualTo(Compilation.Status.SUCCESS);

        });
    }

    private JavaFileObject source(Class<?> clazz) {
        return JavaFileObjects.forResource(clazz.getCanonicalName().replace('.', '/') + ".java");
    }
}
