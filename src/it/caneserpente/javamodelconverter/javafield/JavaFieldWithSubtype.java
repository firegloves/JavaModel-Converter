package it.caneserpente.javamodelconverter.javafield;

public class JavaFieldWithSubtype extends JavaField {

    private JavaField subType;

    public JavaField getSubType() {
        return subType;
    }

    public void setSubType(JavaField subType) {
        this.subType = subType;
    }
}
