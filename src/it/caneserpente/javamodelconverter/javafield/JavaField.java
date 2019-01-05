package it.caneserpente.javamodelconverter.javafield;

import java.lang.reflect.Field;

public class JavaField {

    private Field javaField;
    private String javaTypeName;
    private String convertedFieldType;      // TODO METTERE NEL DTO DI USCITA?

    public Field getJavaField() {
        return javaField;
    }

    public void setJavaField(Field javaField) {
        this.javaField = javaField;
    }

    public String getJavaTypeName() {
        return javaTypeName;
    }

    public void setJavaTypeName(String javaTypeName) {
        this.javaTypeName = javaTypeName;
    }

    public String getConvertedFieldType() {
        return convertedFieldType;
    }

    public void setConvertedFieldType(String convertedFieldType) {
        this.convertedFieldType = convertedFieldType;
    }
}
