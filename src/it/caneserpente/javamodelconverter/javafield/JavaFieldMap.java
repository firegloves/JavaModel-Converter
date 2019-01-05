package it.caneserpente.javamodelconverter.javafield;

public class JavaFieldMap extends JavaField {

    private JavaField subTypeKey;
    private JavaField subTypeValue;

    public JavaField getSubTypeKey() {
        return subTypeKey;
    }

    public void setSubTypeKey(JavaField subTypeKey) {
        this.subTypeKey = subTypeKey;
    }

    public JavaField getSubTypeValue() {
        return subTypeValue;
    }

    public void setSubTypeValue(JavaField subTypeValue) {
        this.subTypeValue = subTypeValue;
    }
}
