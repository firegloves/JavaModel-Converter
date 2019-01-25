package it.caneserpente.javamodelconverter.model;

import java.lang.reflect.Field;

public class JMCFieldArray extends JMCFieldWithSubtype {

    public JMCFieldArray(Field javaField) {
        super(javaField);
        this.setJavaSubtypeName(this.getJavaField().getType().getComponentType().getTypeName());
    }
}
