package it.caneserpente.javamodelconverter.model;

import java.lang.reflect.Field;

public class JMCField {

    private Field javaField;
    private String javaTypeName;

    // CONVERTED FIELDS
    private String convertedFieldType;
    private String convertedFieldStm;
    private String convertedContructorFieldStm;

    public JMCField(Field javaField) {
        this.javaField = javaField;
        this.javaTypeName = this.javaField.getGenericType().getTypeName();
    }

    public Field getJavaField() {
        return javaField;
    }

    public void setJavaField(Field javaField) {
        this.javaField = javaField;
    }

    public String getJavaTypeName() {
        return javaTypeName;
    }

    public String getConvertedFieldType() {
        return convertedFieldType;
    }

    public void setConvertedFieldType(String convertedFieldType) {
        this.convertedFieldType = convertedFieldType;
    }

    public String getConvertedFieldStm() {
        return convertedFieldStm;
    }

    public void setConvertedFieldStm(String convertedFieldStm) {
        this.convertedFieldStm = convertedFieldStm;
    }

    public String getConvertedContructorFieldStm() {
        return convertedContructorFieldStm;
    }

    public void setConvertedContructorFieldStm(String convertedContructorFieldStm) {
        this.convertedContructorFieldStm = convertedContructorFieldStm;
    }

    @Override
    public String toString() {
        return "JMCField{" +
                "javaField=" + javaField +
                ", javaTypeName='" + javaTypeName + '\'' +
                ", convertedFieldType='" + convertedFieldType + '\'' +
                ", convertedFieldStm='" + convertedFieldStm + '\'' +
                '}';
    }
}
