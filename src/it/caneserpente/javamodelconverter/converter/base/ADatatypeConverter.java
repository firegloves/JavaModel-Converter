package it.caneserpente.javamodelconverter.converter.base;

import com.sun.istack.internal.Nullable;

import java.lang.reflect.Field;

public abstract class ADatatypeConverter {

    /**
     * converts field data type into desired language and returns it
     *
     * @param typeName the Field typename to convert to desired language
     * @return received field data type as string coded in desired language, null otherwise
     */
    public abstract String convertDataTypeName(@Nullable String typeName);


    /**
     * converts and returns map field parametrized types into desired language types
     *
     * @param mapField the Field of which convert parametrized types to desired language, empty string if field is of raw type
     */
    public abstract String convertParametrizedMapTypes(Field mapField);

}
