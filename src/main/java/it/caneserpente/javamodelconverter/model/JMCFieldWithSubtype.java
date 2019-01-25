package it.caneserpente.javamodelconverter.model;

import java.lang.reflect.Field;

public class JMCFieldWithSubtype extends JMCField {

    private String javaSubtypeName;

    // CONVERTED FIELDS
    private String convertedSubtype;

    public JMCFieldWithSubtype(Field javaField) {
        super(javaField);
    }

    public String getJavaSubtypeName() {
        return javaSubtypeName;
    }

    public void setJavaSubtypeName(String javaSubtypeName) {
        this.javaSubtypeName = javaSubtypeName;
    }

    public String getConvertedSubtype() {
        return convertedSubtype;
    }

    public void setConvertedSubtype(String convertedSubtype) {
        this.convertedSubtype = convertedSubtype;
    }

    @Override
    public String toString() {
        return "JMCFieldWithSubtype{" +
                "super='" + super.toString() + '\'' +
                "javaSubtypeName='" + javaSubtypeName + '\'' +
                "convertedSubtype='" + convertedSubtype + '\'' +
                '}';
    }
}
