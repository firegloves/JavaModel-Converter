package it.caneserpente.javamodelconverter.converter.base;

import com.sun.istack.internal.Nullable;

public abstract class ADatatypeConverter {

    /**
     * converts field data type into desired language and returns it
     *
     * @param typeName the Field typename to convert to desired language
     * @return received field data type as string coded in desired language, null otherwise
     */
    public abstract String convertDataTypeName(@Nullable String typeName);

}
