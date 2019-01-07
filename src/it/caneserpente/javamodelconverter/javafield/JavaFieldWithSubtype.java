package it.caneserpente.javamodelconverter.javafield;

import java.lang.reflect.Field;

public class JavaFieldWithSubtype extends JavaField {

    private String javaSubtypeName;

    public JavaFieldWithSubtype(Field javaField) {
        super(javaField);
    }

    public String getJavaSubtypeName() {
        return javaSubtypeName;
    }

    public void setJavaSubtypeName(String javaSubtypeName) {
        this.javaSubtypeName = javaSubtypeName;
    }

    @Override
    public String toString() {
        return "JavaFieldWithSubtype{" +
                "super='" + super.toString() + '\'' +
                "javaSubtypeName='" + javaSubtypeName + '\'' +
                '}';
    }
}
