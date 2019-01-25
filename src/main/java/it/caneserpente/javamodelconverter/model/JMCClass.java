package it.caneserpente.javamodelconverter.model;

import java.util.List;

public class JMCClass {

    private Class clazz;
    private String convertedClassName;
    private String convertedConstructorStart;
    private List<JMCField> fieldList;
    private String convertedConstructorEnd;

    public JMCClass(Class clazz) {
        this.clazz = clazz;
    }

    public String getConvertedClassName() {
        return convertedClassName;
    }

    public void setConvertedClassName(String convertedClassName) {
        this.convertedClassName = convertedClassName;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getConvertedConstructorStart() {
        return convertedConstructorStart;
    }

    public void setConvertedConstructorStart(String convertedConstructorStart) {
        this.convertedConstructorStart = convertedConstructorStart;
    }

    public List<JMCField> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<JMCField> fieldList) {
        this.fieldList = fieldList;
    }

    public String getConvertedConstructorEnd() {
        return convertedConstructorEnd;
    }

    public void setConvertedConstructorEnd(String convertedConstructorEnd) {
        this.convertedConstructorEnd = convertedConstructorEnd;
    }
}
