package it.caneserpente.javamodelconverter.model;

import java.lang.reflect.Field;

public class JMCFieldWithSubtype extends JMCField {

    private String javaSubtypeName;

    public JMCFieldWithSubtype(Field javaField) {
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
        return "JMCFieldWithSubtype{" +
                "super='" + super.toString() + '\'' +
                "javaSubtypeName='" + javaSubtypeName + '\'' +
                '}';
    }
}
