package it.caneserpente.javamodelconverter.converter.typescript;

import com.sun.istack.internal.Nullable;
import it.caneserpente.javamodelconverter.converter.base.ADatatypeConverter;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

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
                return "";
        }
    }


    @Override
    public String convertParametrizedMapTypes(Field mapField) {

        String typeName1 = null, typeName2 = null, res = "";

        // subtype of map
        if (Map.class.isAssignableFrom(mapField.getType()) && mapField.getGenericType() instanceof ParameterizedType) {

            typeName1 = ((ParameterizedType) mapField.getGenericType()).getActualTypeArguments()[0].getTypeName();
            typeName2 = ((ParameterizedType) mapField.getGenericType()).getActualTypeArguments()[1].getTypeName();

            if (null != typeName1 && ! typeName1.isEmpty() && null != typeName2 && ! typeName2.isEmpty()) {
                res = " [name: " + this.convertDataTypeName(typeName1) + "]: " + this.convertDataTypeName(typeName2);
            }
        }

        return res;
    }

}
