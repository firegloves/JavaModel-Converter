package it.caneserpente.javamodelconverter.converter.typescript;

import com.sun.istack.internal.Nullable;
import it.caneserpente.javamodelconverter.converter.base.AConstructorConverter;
import it.caneserpente.javamodelconverter.converter.base.ADatatypeConverter;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TypescriptConstructorConverter extends AConstructorConverter {

    private final List<String> NO_CONSTRUCTOR_DATA_TYPES = Arrays.asList("number", "string", "String", "boolean", "Boolean");

    private ADatatypeConverter datatypeConverter;

    /**
     * constructor
     */
    public TypescriptConstructorConverter(ADatatypeConverter datatypeConverter) {
        super(datatypeConverter);
        this.datatypeConverter = datatypeConverter;
    }


    @Override
    public String createConstructor(String clzName, @Nullable Field[] fieldArray) {

        StringBuilder sb = new StringBuilder();
        sb.append("\tconstructor()\n")
                .append("\tconstructor(m: " + clzName + ")\n")
                .append("\tconstructor(m?: " + clzName + ") {\n");

        for (int i = 0; i < fieldArray.length; i++) {

            String typeName = fieldArray[i].getGenericType().getTypeName();
            String converted = this.datatypeConverter.convertDataTypeName(typeName);

            // generic data type
            if (null == converted || converted.isEmpty()) {
                sb.append("\t\t" + this.createConstructorBaseClassFieldAssignment(fieldArray[i]) + ";\n");
            } else {
                sb.append("\t\t" + this.createConstructorFieldAssignment(typeName, fieldArray[i].getName()) + ";\n");
            }
        }

        sb.append("\n\t}");

        return sb.toString();
    }


    @Override
    protected String createConstructorFieldAssignment(@Nullable String typeName, @Nullable String fieldName) {

        if (null == typeName || typeName.isEmpty() || null == fieldName || fieldName.isEmpty()) {
            return null;
        }

        switch (typeName) {

            case "java.util.Date":
            case "java.sql.Timestamp":
                return "this." + fieldName + " = m && m." + fieldName + " ? new Date(m." + fieldName + ") : undefined";

            default:
                return "this." + fieldName + " = m && m." + fieldName + " || undefined";
        }
    }


    /**
     * generate the assignment constructor instruction of the received field (Collection's subtypes, arrays, etc)
     *
     * @param field the field of which to create assignment constructor instruction in desired language
     * @return the assignment constructor instruction in desired language
     */
    protected String createConstructorBaseClassFieldAssignment(@Nullable Field field) {

        if (null == field) {
            return null;
        }

        String fieldStr = null;

        // check for Collection (and subtypes) and Arrays
        fieldStr = this.createConstructorCollectionOrArrayFieldAssignment(field);

        if (null == fieldStr) {
            fieldStr = this.createConstructorMapFieldAssignment(field);
        }

        return fieldStr;
    }


    /**
     * if param field is an Array or a subtype of Collection class this method manage the field and return constructor's field assignment statement
     *
     * @param field the field of which create constructor's field assignment statement
     * @return constructor's param field assignment statement if field is of type Array or a subtype of Collection, null otherwise
     */
    private String createConstructorCollectionOrArrayFieldAssignment(Field field) {

        boolean toArray = false;
        String fieldName = field.getName();
        String typescriptTypeName = "";

        // Array
        if (field.getType().isArray()) {
            toArray = true;
            typescriptTypeName = this.datatypeConverter.convertDataTypeName(field.getType().getComponentType().getTypeName());
        }

        // subtype of Collection
        if (! toArray && Collection.class.isAssignableFrom(field.getType())) {
            toArray = true;

            if (field.getGenericType() instanceof ParameterizedType) {
                typescriptTypeName = this.datatypeConverter.convertDataTypeName(((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getTypeName());
            }
        }

        if (toArray) {
            if (null == typescriptTypeName || typescriptTypeName.isEmpty() || NO_CONSTRUCTOR_DATA_TYPES.contains(typescriptTypeName)) {
                return "this." + fieldName + " = m && m." + fieldName;
            } else {
                return "this." + fieldName + " = m && m." + fieldName + " ? m." + fieldName + ".map(s => new " + typescriptTypeName + "(s)) : []";
            }

        } else {
            return null;
        }

    }


    /**
     * if param field is a Map or a subtype of Map class this method manage the field and return constructor's field assignment statement
     *
     * @param field the field of which create constructor's field assignment statement
     * @return constructor's param field assignment statement if field is of type Map or a subtype of Map, null otherwise
     */
    private String createConstructorMapFieldAssignment(Field field) {

        String parametrizedStr = "";

        // subtype of map
        if (Map.class.isAssignableFrom(field.getType())) {

            String[] paramTypes = this.datatypeConverter.convertParametrizedMapTypes(field);
            if (null != paramTypes && paramTypes.length == 2) {
                parametrizedStr = " [name: " + paramTypes[ADatatypeConverter.MAP_KEY] + "]: " + paramTypes[ADatatypeConverter.MAP_KEY];
            }
        }

        return "\t" + field.getName() + ": {" + parametrizedStr + "}";
    }

}
