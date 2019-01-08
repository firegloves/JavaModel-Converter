package it.caneserpente.javamodelconverter.converter.typescript;

import com.sun.istack.internal.Nullable;
import it.caneserpente.javamodelconverter.converter.base.AConstructorConverter;
import it.caneserpente.javamodelconverter.converter.base.ADatatypeConverter;
import it.caneserpente.javamodelconverter.model.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.Function;

public class TypescriptConstructorConverter extends AConstructorConverter {

    private final List<String> NO_CONSTRUCTOR_DATA_TYPES = Arrays.asList("number", "string", "String", "boolean", "Boolean");

    private ADatatypeConverter datatypeConverter;

    private Map<Class, Function> strategyMap;

    /**
     * constructor
     */
    public TypescriptConstructorConverter(ADatatypeConverter datatypeConverter) {
        super(datatypeConverter);
        this.datatypeConverter = datatypeConverter;

        // TODO pensare se mettere la strategy map nella classe padre per forzare ulteriori traduzioni a utilizzare questo approccio
        this.strategyMap = new HashMap<>();
        this.strategyMap.put(JMCFieldBasic.class, (Function<JMCFieldBasic, String>) jf -> this.createConstrJMCFieldBasic(jf));
        this.strategyMap.put(JMCFieldArray.class, (Function<JMCFieldArray, String>) jf -> this.createConstrJMCFieldArray(jf));
        this.strategyMap.put(JMCFieldCollection.class, (Function<JMCFieldCollection, String>) jf -> this.createConstrJMCFieldCollection(jf));
        this.strategyMap.put(JMCFieldMap.class, (Function<JMCFieldMap, String>) jf -> this.createConstrJMCFieldMap(jf));
    }


    @Override
    public JMCClass createConstructor(@Nullable JMCClass clz) {

        if (null != clz) {
            clz.setConvertedConstructorInit("\tconstructor()\n" +
                                            "\tconstructor(m: " + clz.getConvertedClassName() + ")\n" +
                                            "\tconstructor(m?: " + clz.getConvertedClassName() + ") {\n");

            clz.getFieldList().stream().forEach(f -> this.createConstructorFieldAssignment(f));
        }

        return clz;
    }


    @Override
    protected JMCField createConstructorFieldAssignment(@Nullable JMCField jf) {

        if (null != jf) {
            // create constructor assignment statement
            jf.setConvertedContructorFieldStm((String) this.strategyMap.get(jf.getClass()).apply(jf));
        }

        return jf;
    }


    /**
     * converts JMCFieldBasic
     * @param jf the JMCFieldBasic to convert
     * @return JMCField converted
     */
    private String createConstrJMCFieldBasic(JMCFieldBasic jf) {

        switch (jf.getJavaTypeName()) {

            case "java.util.Date":
            case "java.sql.Timestamp":
                return "\t\tthis." + jf.getJavaField().getName() + " = m && m." + jf.getJavaField().getName() + " ? new Date(m." + jf.getJavaField().getName() + ") : undefined;\n";

            default:
                return "\t\tthis." + jf.getJavaField().getName() + " = m && m." + jf.getJavaField().getName() + " || undefined;\n";
        }
    }

    /**
     * converts JMCFieldArray
     * @param jf the JMCFieldArray to convert
     * @return JMCField converted
     */
    private String createConstrJMCFieldArray(JMCFieldArray jf) {

        if (null == jf.getConvertedFieldType() || jf.getConvertedFieldType().isEmpty() || NO_CONSTRUCTOR_DATA_TYPES.contains(jf.getConvertedFieldType())) {
            return "\t\tthis." + jf.getJavaField().getName() + " = m && m." + jf.getJavaField().getName() + ";\n";
        } else {
            return "\t\tthis." + jf.getJavaField().getName() + " = m && m." + jf.getJavaField().getName() + " ? m." + jf.getJavaField().getName() + ".map(s => new " + jf.getConvertedFieldType() + "(s)) : [];\n";
        }
    }

    /**
     * converts JMCFieldCollection
     * @param jf the JMCFieldCollection to convert
     * @return JMCField converted
     */
    private String createConstrJMCFieldCollection(JMCFieldCollection jf) {
        // TODO REFACTOR - EQUAL METHODS

        if (null == jf.getConvertedFieldType() || jf.getConvertedFieldType().isEmpty() || NO_CONSTRUCTOR_DATA_TYPES.contains(jf.getConvertedFieldType())) {
            return "\t\tthis." + jf.getJavaField().getName() + " = m && m." + jf.getJavaField().getName() + ";\n";
        } else {
            return "\t\tthis." + jf.getJavaField().getName() + " = m && m." + jf.getJavaField().getName() + " ? m." + jf.getJavaField().getName() + ".map(s => new " + jf.getConvertedFieldType() + "(s)) : [];\n";
        }
    }

    /**
     * converts JMCFieldMap
     * @param jf the JMCFieldMap to convert
     * @return JMCField converted
     */
    private String createConstrJMCFieldMap(JMCFieldMap jf) {

        String converted = "Map";

        if (jf.isParametrized()) {
            converted += "<" + datatypeConverter.convertDataTypeName(jf.getJavaSubtypeKeyName()) + ", " + this.datatypeConverter.convertDataTypeName(jf.getJavaSubtypeValueName()) + ">";
        }

        return converted;
    }



//    /**
//     * generate the assignment constructor instruction of the received field (Collection's subtypes, arrays, etc)
//     *
//     * @param field the field of which to create assignment constructor instruction in desired language
//     * @return the assignment constructor instruction in desired language
//     */
//    protected String createCollectionOrMapFieldAssignment(@Nullable Field field) {
//
//        if (null == field) {
//            return null;
//        }
//
//        String fieldStr = null;
//
//        // check for Collection (and subtypes) and Arrays
//        fieldStr = this.createCollectionOrArrayFieldAssignment(field);
//
//        if (null == fieldStr) {
//            fieldStr = this.createMapFieldAssignment(field);
//        }
//
//        return fieldStr;
//    }
//
//
//    /**
//     * if param field is an Array or a subtype of Collection class this method manage the field and return constructor's field assignment statement
//     *
//     * @param field the field of which create constructor's field assignment statement
//     * @return constructor's param field assignment statement if field is of type Array or a subtype of Collection, null otherwise
//     */
//    private String createCollectionOrArrayFieldAssignment(Field field) {
//
//        boolean toArray = false;
//        String fieldName = field.getName();
//        String typescriptTypeName = "";
//
//        // Array
//        if (field.getType().isArray()) {
//            toArray = true;
//            typescriptTypeName = this.datatypeConverter.convertDataTypeName(field.getType().getComponentType().getTypeName());
//        }
//
//        // subtype of Collection
//        if (!toArray && Collection.class.isAssignableFrom(field.getType())) {
//            toArray = true;
//
//            if (field.getGenericType() instanceof ParameterizedType) {
//                typescriptTypeName = this.datatypeConverter.convertDataTypeName(((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getTypeName());
//            }
//        }
//
//        if (toArray) {
//            if (null == typescriptTypeName || typescriptTypeName.isEmpty() || NO_CONSTRUCTOR_DATA_TYPES.contains(typescriptTypeName)) {
//                return "this." + fieldName + " = m && m." + fieldName;
//            } else {
//                return "this." + fieldName + " = m && m." + fieldName + " ? m." + fieldName + ".map(s => new " + typescriptTypeName + "(s)) : []";
//            }
//
//        } else {
//            return null;
//        }
//
//    }
//
//
//    /**
//     * if param field is a Map or a subtype of Map class this method manage the field and return constructor's field assignment statement
//     *
//     * @param field the field of which create constructor's field assignment statement
//     * @return constructor's param field assignment statement if field is of type Map or a subtype of Map, null otherwise
//     */
//    private String createMapFieldAssignment(Field field) {
//
//        // subtype of map
//        if (Map.class.isAssignableFrom(field.getType())) {
////            return "\t" + field.getName() + ": {" + this.datatypeConverter.convertParametrizedMapTypes(field) + "}";
//        }
//
//        return null;
//    }

}
