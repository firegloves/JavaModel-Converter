package it.caneserpente.javamodelconverter.converter.typescript;

import com.sun.istack.internal.Nullable;
import it.caneserpente.javamodelconverter.converter.base.ADatatypeConverter;
import it.caneserpente.javamodelconverter.converter.base.AFieldConverter;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TypescriptFieldConverter extends AFieldConverter {

    private final List<String> NO_CONSTRUCTOR_DATA_TYPES = Arrays.asList("number", "string", "String", "boolean", "Boolean");

    private ADatatypeConverter datatypeConverter;

    public TypescriptFieldConverter(ADatatypeConverter datatypeConverter) {
        this.datatypeConverter = datatypeConverter;
    }


    @Override
    protected String convertField(@Nullable Field field) {

        if (null == field) return null;

        String typeName = field.getGenericType().getTypeName();
        String converted = this.datatypeConverter.convertDataTypeName(typeName);

        if (null != converted && ! converted.isEmpty()) {
            converted = "\t" + field.getName() + ": " + converted + ";";
        } else {
            converted = this.convertBaseClassField(field);
        }

        if (null == converted) {
            System.out.println("** TYPE UNRECOGNIZED: " + field.getGenericType().getTypeName());
        }

        return converted;
    }



    /**
     * converts field received into desired language and return it as String
     * this method check if field is a subtype of a base type
     *
     * @param field the Field to convert to desired language
     * @return received field as string coded in desired language
     */
    private String convertBaseClassField(Field field) {

        String res = this.convertToArray(field);

        if (null == res) {
            res = this.convertFromMap(field);
        }

        return res;
    }


    /**
     * try to converts received field into Typescript array if field type is compatible
     * @param field the Field to convert to desired language
     * @return received field as string coded in desired language, null if Field is not compatible with Typescript array
     */
    private String convertToArray(Field field) {

        boolean toArray = false;
        String typeName = null, typescriptTypeName = "";

        // Array
        if (field.getType().isArray()) {
            toArray = true;
            typeName = field.getType().getComponentType().getTypeName();
        }

        // subtype of Collection
        if (Collection.class.isAssignableFrom(field.getType())) {
            toArray = true;

            if (field.getGenericType() instanceof ParameterizedType) {
                typeName = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getTypeName();
            }
        }

        if (! toArray) {
            return null;
        } else {
            typescriptTypeName = this.datatypeConverter.convertDataTypeName(typeName);
            return "\t" + field.getName() + ": " + typescriptTypeName + "[];";
        }
    }


    /**
     * try to converts received field into Typescript Tuple (map) if field type is compatible
     * @param field the Field to convert to desired language
     * @return received field as string coded in desired language, null if Field is not compatible with Typescript Tuple
     */
    private String convertFromMap(Field field) {

        String parametrizedStr = "";

        // subtype of map
        if (Map.class.isAssignableFrom(field.getType())) {

            String[] paramTypes = this.datatypeConverter.convertParametrizedMapTypes(field);
            if (null != paramTypes && paramTypes.length == 2) {
                parametrizedStr = " [name: " + paramTypes[ADatatypeConverter.MAP_KEY] + "]: " + paramTypes[ADatatypeConverter.MAP_KEY];
            }

            return "\t" + field.getName() + ": {" + parametrizedStr + "};";
        }

        return null;
    }

}
