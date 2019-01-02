package it.caneserpente.javamodelconverter.converter.base;

import com.sun.istack.internal.Nullable;

import java.lang.reflect.Field;

public abstract class ADatatypeConverter {

    public static final int MAP_KEY = 0;
    public static final int MAP_VALUE = 1;

    /**
     * converts field data type into desired language and returns it
     *
     * @param typeName the Field typename to convert to desired language
     * @return received field data type as string coded in desired language
     */
    public abstract String convertDataTypeName(@Nullable String typeName);


    /**
     * converts map field parametrized types into desired language types and return them as an array of length 2.
     * resulting array indices are
     *
     * @param mapField the Field of which convert parametrized types to desired language, null if field is of raw type
     */
    public abstract String[] convertParametrizedMapTypes(Field mapField);

}
