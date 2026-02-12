package org.lorislab.quarkus.data.codegen;

import static org.lorislab.quarkus.data.codegen.Utils.getTypeDefaultValue;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.*;

public class CollectionInfo {

    private final TypeMirror itemType;

    private final FieldInfo field;

    private final CollectionKind kind;

    public CollectionInfo(FieldInfo field, TypeMirror itemType, CollectionKind kind) {
        this.itemType = itemType;
        this.field = field;
        this.kind = kind;
    }

    public CollectionKind getKind() {
        return kind;
    }

    public boolean isArray() {
        return kind == CollectionKind.ARRAY;
    }

    public FieldInfo getField() {
        return field;
    }

    public String getItemTypeSimpleName() {
        return itemType.toString();
    }

    public TypeKind getItemTypeKind() {
        return itemType.getKind();
    }

    public TypeMirror getItemType() {
        return itemType;
    }

    public ItemType getItemTypeFqn() {
        return fqn(itemType);
    }

    public Class<?> getPrimitiveObjectClass() {
        return Utils.getTypeKindObject(itemType.getKind());
    }

    public String getPrimitiveTypeDefault() {
        return getTypeDefaultValue(itemType.getKind());
    }

    public static ItemType fqn(TypeMirror typeMirror) {

        switch (typeMirror.getKind()) {

            case DECLARED:
                DeclaredType declared = (DeclaredType) typeMirror;
                TypeElement element = (TypeElement) declared.asElement();
                return new ItemType(typeMirror.getKind(), element.getSimpleName().toString(),
                        element.getQualifiedName().toString());

            case TYPEVAR:
                TypeVariable typeVar = (TypeVariable) typeMirror;
                return new ItemType(typeMirror.getKind(), typeVar.toString(), null);

            case ARRAY:
                ArrayType arrayType = (ArrayType) typeMirror;
                return new ItemType(typeMirror.getKind(), fqn(arrayType.getComponentType()) + "[]", null);

            case BOOLEAN:
            case BYTE:
            case SHORT:
            case INT:
            case LONG:
            case CHAR:
            case FLOAT:
            case DOUBLE:
                return new ItemType(typeMirror.getKind(), typeMirror.toString(), null);

            case WILDCARD:
                WildcardType wildcard = (WildcardType) typeMirror;
                if (wildcard.getExtendsBound() != null)
                    new ItemType(typeMirror.getKind(), "? super " + fqn(wildcard.getExtendsBound()), null);
                ;
                if (wildcard.getSuperBound() != null)
                    new ItemType(typeMirror.getKind(), "? super " + fqn(wildcard.getSuperBound()), null);
                ;
                return new ItemType(typeMirror.getKind(), "?", null);

            default:
                return new ItemType(null, typeMirror.toString(), null);
        }
    }

    public enum CollectionKind {
        ARRAY,
        LIST,
        SET,
        QUEUE,
        DEQUE,
        SORTED_SET,
        NAVIGABLE_SET,
        OTHER
    }

    public record ItemType(TypeKind type, String simpleName, String fqn) {
    }
}
