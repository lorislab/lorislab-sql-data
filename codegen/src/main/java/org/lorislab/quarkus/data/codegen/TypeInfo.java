package org.lorislab.quarkus.data.codegen;

import static org.lorislab.quarkus.data.codegen.Utils.getTypeDefaultValue;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

public class TypeInfo {

    private Element element;

    private TypeElement typeElement;

    private PackageElement packageElement;

    public TypeInfo(Element element, TypeElement typeElement, PackageElement packageName) {
        this.element = element;
        this.typeElement = typeElement;
        this.packageElement = packageName;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public Element getElement() {
        return element;
    }

    public String getQualifiedName() {
        return element.asType().toString();
    }

    public boolean isPrimitive() {
        return element.asType().getKind().isPrimitive();
    }

    public String getSimpleName() {
        if (typeElement == null) {
            return element.asType().toString();
        }
        return typeElement.getSimpleName().toString();
    }

    public String getPackageName() {
        return packageElement.getQualifiedName().toString();
    }

    public boolean isEnum() {
        if (typeElement == null) {
            return false;
        }
        return typeElement.getKind() == ElementKind.ENUM;
    }

    public Class<?> getPrimitiveObjectClass() {
        return Utils.getTypeKindObject(element.asType().getKind());
    }

    public String getPrimitiveTypeDefault() {
        return getTypeDefaultValue(element.asType().getKind());
    }
}
