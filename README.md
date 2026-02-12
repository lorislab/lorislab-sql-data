# lorislab-sql-data

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Maven Central](https://img.shields.io/maven-central/v/org.lorislab.lib/lorislab-sql-data?label=Maven%20Central)](https://search.maven.org/search?q=g:org.lorislab.lib%20AND%20a:lorislab-sql-data)
[![GitHub Releases](https://img.shields.io/github/v/release/lorislab/lorislab-sql-data?label=GitHub%20Release)](https://github.com/lorislab/lorislab-sql-data/releases)

A small set of annotations and utilities for mapping Java types to SQL structures and generating mapping/metamodel code.

### Modules
- `data` - runtime annotations and small utilities (contains annotation classes such as `@Entity`, `@Table`, `@Column`, `@Mapper`, etc.).
- `codegen` - annotation processor / code generator that creates metamodels and mapper classes at compile time.

## Quickstart

Add the dependency to your `pom.xml` (replace the version with a released version when available):

```xml
<dependency>
  <groupId>org.lorislab.lib</groupId>
  <artifactId>lorislab-sql-data</artifactId>
  <version>{VERSION}</version>
</dependency>
```

If you use the `codegen` processor, make sure annotation processing is enabled; in Maven it is automatic for `maven-compiler-plugin`.

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <annotationProcessorPaths>
            <path>
                <groupId>org.lorislab.quarkus</groupId>
                <artifactId>quarkus-data-codegen</artifactId>
                <version>{VERSION}</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

## License

This project is licensed under the Apache License, Version 2.0. See the [LICENSE](LICENSE) for details.

## Contributing

Contributions are welcome. See the repository on GitHub: https://github.com/lorislab/lorislab-sql-data
