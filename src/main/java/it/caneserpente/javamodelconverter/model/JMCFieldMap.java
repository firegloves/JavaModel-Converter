package it.caneserpente.javamodelconverter.model;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class JMCFieldMap extends JMCField {

    private String javaSubtypeKeyName;
    private String javaSubtypeValueName;

    // CONVERTED
    private String convertedFieldKeyType;
    private String convertedFieldValueType;

    public JMCFieldMap(Field javaField) {
        super(javaField);
        if (this.getJavaField().getGenericType() instanceof ParameterizedType) {
            this.javaSubtypeKeyName = ((ParameterizedType) this.getJavaField().getGenericType()).getActualTypeArguments()[0].getTypeName();
            this.javaSubtypeValueName = ((ParameterizedType) this.getJavaField().getGenericType()).getActualTypeArguments()[1].getTypeName();
        }
    }

    public String getJavaSubtypeKeyName() {
        return javaSubtypeKeyName;
    }

    public void setJavaSubtypeKeyName(String javaSubtypeKeyName) {
        this.javaSubtypeKeyName = javaSubtypeKeyName;
    }

    public String getJavaSubtypeValueName() {
        return javaSubtypeValueName;
    }

    public void setJavaSubtypeValueName(String javaSubtypeValueName) {
        this.javaSubtypeValueName = javaSubtypeValueName;
    }

    public String getConvertedFieldKeyType() {
        return convertedFieldKeyType;
    }

    public void setConvertedFieldKeyType(String convertedFieldKeyType) {
        this.convertedFieldKeyType = convertedFieldKeyType;
    }

    public String getConvertedFieldValueType() {
        return convertedFieldValueType;
    }

    public void setConvertedFieldValueType(String convertedFieldValueType) {
        this.convertedFieldValueType = convertedFieldValueType;
    }

    public boolean isParametrized() {
        return null != javaSubtypeKeyName && ! javaSubtypeKeyName.isEmpty() && null != javaSubtypeValueName && ! javaSubtypeValueName.isEmpty();
    }

    @Override
    public String toString() {
        return "JMCFieldMap{" +
                "super='" + super.toString() + '\'' +
                "javaSubtypeKeyName='" + javaSubtypeKeyName + '\'' +
                ", javaSubtypeValueName='" + javaSubtypeValueName + '\'' +
                '}';
    }
}
