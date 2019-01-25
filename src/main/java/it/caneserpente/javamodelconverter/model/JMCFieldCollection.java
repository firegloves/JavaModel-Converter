package it.caneserpente.javamodelconverter.model;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class JMCFieldCollection extends JMCFieldWithSubtype {

    public JMCFieldCollection(Field javaField) {
        super(javaField);

        if (this.getJavaField().getGenericType() instanceof ParameterizedType) {
            this.setJavaSubtypeName(((ParameterizedType) this.getJavaField().getGenericType()).getActualTypeArguments()[0].getTypeName());
        }
    }
}
