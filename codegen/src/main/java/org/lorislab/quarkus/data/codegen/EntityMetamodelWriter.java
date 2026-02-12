package org.lorislab.quarkus.data.codegen;

import javax.annotation.processing.ProcessingEnvironment;

import org.lorislab.quarkus.data.Table;

public class EntityMetamodelWriter {

    public static void generate(ProcessingEnvironment processingEnv, ClassInfo clazz) throws Exception {

        var packageName = processingEnv.getElementUtils()
                .getPackageOf(clazz.getTypeElement())
                .getQualifiedName()
                .toString();

        var className = clazz.getName() + "_";
        var qualifiedName = packageName.isEmpty() ? className : packageName + "." + className;

        var metamodel = new ClassInfo.Metamodel(qualifiedName, packageName, className);

        var sb = new StringBuilder();
        if (!packageName.isEmpty()) {
            sb.append(String.format("package %s;\n\n", packageName));
        }
        sb.append(String.format("public interface %s {\n\n", className));

        var tableAnno = clazz.getTypeElement().getAnnotation(Table.class);
        if (tableAnno != null) {
            metamodel.setTableConst("TABLE_");
            sb.append(String.format("  String TABLE_ = \"%s\";\n\n", tableAnno.name()));
        }

        var fields = clazz.getFields();

        fields.stream()
                .filter(x -> x.getColumn() == null || (x.getColumn() != null && !x.getColumn().ignore()))
                .filter(FieldInfo::hasId)
                .findFirst().ifPresent(f -> {
                    metamodel.setIdConst("ID_");
                    sb.append(String.format("  String ID_ = \"%s\";\n\n", f.getColumnName()));
                });

        fields.stream()
                .filter(x -> x.getColumn() == null || (x.getColumn() != null && !x.getColumn().ignore()))
                .forEach(f -> {
                    metamodel.getFieldsConst().put(f.getName(), f.getConstName());
                    sb.append(String.format("  String %s = \"%s\";\n\n", f.getConstName(), f.getColumnName()));
                });

        sb.append("}\n");

        Utils.createJavaFile(processingEnv, qualifiedName, sb.toString());

        clazz.setMetamodel(metamodel);
    }

}
