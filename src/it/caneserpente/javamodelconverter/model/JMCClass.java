package it.caneserpente.javamodelconverter.model;

import java.util.List;

public class JMCClass {

    private Class clazz;
    private String convertedClassName;
    private String convertedConstructorInit;
    private List<JMCField> fieldList;

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

    public String getConvertedConstructorInit() {
        return convertedConstructorInit;
    }

    public void setConvertedConstructorInit(String convertedConstructorInit) {
        this.convertedConstructorInit = convertedConstructorInit;
    }

    public List<JMCField> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<JMCField> fieldList) {
        this.fieldList = fieldList;
    }
}
