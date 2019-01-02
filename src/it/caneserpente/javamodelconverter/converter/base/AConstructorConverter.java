

package it.caneserpente.javamodelconverter.converter.base;

import com.sun.istack.internal.Nullable;

import java.lang.reflect.Field;

public abstract class AConstructorConverter {

    protected ADatatypeConverter datatypeConverter;

    public AConstructorConverter(ADatatypeConverter datatypeConverter) {
        this.datatypeConverter = datatypeConverter;
    }


    /**
     * generate constructor code and return it as string
     *
     * @param clzName the class name of which create constructor code
     * @param fieldArray array of class Fields
     * @return constructor generated code in desired language
     */
    public abstract String createConstructor(@Nullable String clzName, @Nullable Field[] fieldArray);


    /**
     * generate the assignment constructor instruction of the received field
     *
     * @param typeName the field's typename of which to create assignment constructor instruction in desired language
     * @param fieldName the java field name
     * @return the assignment constructor instruction in desired language
     */
    protected abstract String createConstructorFieldAssignment(@Nullable String typeName, @Nullable String fieldName);
}
