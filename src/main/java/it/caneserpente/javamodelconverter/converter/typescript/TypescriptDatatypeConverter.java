package it.caneserpente.javamodelconverter.converter.typescript;

import it.caneserpente.javamodelconverter.converter.base.ADatatypeConverter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TypescriptDatatypeConverter extends ADatatypeConverter {


    public TypescriptDatatypeConverter(List<String> transpilingDataTypes) {
        super(transpilingDataTypes);
    }

    @Override
    public String convertDataTypeName(@Nullable String typeName) {

        // if null returns any
        if (null == typeName) {
            return "any";
        }

        // current transpiling data type
        if (this.transpilingDataTypes.contains(typeName)) {
            return this.getTranspilingDataTypeSimpleName(typeName);
        }

        // managed java data types
        switch (typeName) {

            case "int":
            case "java.lang.Integer":
            case "float":
            case "java.lang.Float":
            case "long":
            case "java.lang.Long":
            case "double":
            case "java.lang.Double":
            case "java.math.BigDecimal":
            case "java.math.BigInteger":
            case "byte":
            case "java.lang.Byte":
            case "short":
            case "java.lang.Short":
                return "number";

            case "char":
            case "java.lang.Character":
            case "java.lang.String":
                return "string";

            case "java.util.Date":
            case "java.sql.Date":
            case "java.sql.Timestamp":
                return "Date";

            case "boolean":
            case "java.lang.Boolean":
                return "boolean";

            default:
                return "any";
        }
    }

}
