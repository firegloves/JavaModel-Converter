package it.caneserpente.javamodelconverter.javafield;

import java.lang.reflect.Field;

public class JavaFieldMap extends JavaField {

    private String javaSubtypeKeyName;
    private String javaSubtypeValue;

    public JavaFieldMap(Field javaField) {
        super(javaField);
    }

    public String getJavaSubtypeKeyName() {
        return javaSubtypeKeyName;
    }

    public void setJavaSubtypeKeyName(String javaSubtypeKeyName) {
        this.javaSubtypeKeyName = javaSubtypeKeyName;
    }

    public String getJavaSubtypeValue() {
        return javaSubtypeValue;
    }

    public void setJavaSubtypeValue(String javaSubtypeValue) {
        this.javaSubtypeValue = javaSubtypeValue;
    }

    @Override
    public String toString() {
        return "JavaFieldMap{" +
                "super='" + super.toString() + '\'' +
                "javaSubtypeKeyName='" + javaSubtypeKeyName + '\'' +
                ", javaSubtypeValue='" + javaSubtypeValue + '\'' +
                '}';
    }
}
