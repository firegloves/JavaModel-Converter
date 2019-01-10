package it.caneserpente.javamodelconverter.converter.typescript;

import com.sun.istack.internal.Nullable;
import it.caneserpente.javamodelconverter.converter.base.ADatatypeConverter;

public class TypescriptDatatypeConverter extends ADatatypeConverter {


    @Override
    public String convertDataTypeName(@Nullable String typeName) {

        if (null == typeName) {
            return "";
        }

        switch (typeName) {

            case "int":
            case "java.lang.Integer":
            case "float":
            case "java.lang.Float":
            case "long":
            case "java.lang.Long":
            case "java.math.BigDecimal":
                return "number";

            case "java.lang.String":
                return "string";

            case "java.util.Date":
            case "java.sql.Date":
            case "java.sql.Timestamp":
                return "Date";

            default:
                return null;
        }
    }

}
