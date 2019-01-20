package it.caneserpente.javamodelconverter.converter.base;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.List;

public abstract class ADatatypeConverter {


    /**
     * list of data types in transpiling
     */
    protected List<String> transpilingDataTypes;


    public ADatatypeConverter(List<String> transpilingDataTypes) {
        this.transpilingDataTypes = transpilingDataTypes;
    }


    /**
     * converts field data type into desired language and returns it
     *
     * @param typeName the Field typename to convert to desired language
     * @return received field data type as string coded in desired language, null otherwise
     */
    public abstract String convertDataTypeName(@Nullable String typeName);


    /**
     * check is received type belongs to the current transpiling class list
     *
     * @param typeName the Field typename to convert to desired language
     * @return true if typeName belongs to the current transpiling class list, false otherwise
     */
    public boolean isTranspilingDataType(String typeName) {
        return null != this.transpilingDataTypes && this.transpilingDataTypes.contains(typeName);
    }

    /**
     * receive a full qualified name of a current traspiling data type and return its simple name
     *
     * @param typeName the Field typename to convert to desired language
     * @return the simple name of the received data type
     */
    public String getTranspilingDataTypeSimpleName(@NotNull String typeName) {
        return typeName.substring(typeName.lastIndexOf('.') + 1);
    }
}
