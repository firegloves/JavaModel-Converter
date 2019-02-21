package it.caneserpente.javamodelconverter.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JMCClass {

    private Class clazz;
//    private String containingFolders;
    private String fileName;

    /**
     * contains type name to generate
     */
    private String typeToGenerate;

    private String convertedClassName;
    private String convertedConstructorStart;
    private List<JMCField> fieldList;
    private String convertedConstructorEnd;

    private boolean constructorNeeded;

    public JMCClass(Class clazz) {
        this.clazz = clazz;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public String getTypeToGenerate() {
        return typeToGenerate;
    }

    public void setTypeToGenerate(String typeToGenerate) {
        this.typeToGenerate = typeToGenerate;
    }

    public boolean isEnum() {
        return this.clazz.isEnum();
    }

    /**
     * @return enum constants as List<Object> if this.clazz is enum, an empty List otherwise
     */
    public List<Object> getEnumConstants() {
        return this.isEnum() ? Arrays.asList(this.clazz.getEnumConstants()) : new ArrayList<>();
    }

    public void setConstructorNeeded(boolean constructorNeeded) {
        this.constructorNeeded = constructorNeeded;
    }

    public boolean isConstructorNeeded() {
        return constructorNeeded;
    }
}
