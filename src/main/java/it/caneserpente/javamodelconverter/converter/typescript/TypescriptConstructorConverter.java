package it.caneserpente.javamodelconverter.converter.typescript;

import it.caneserpente.javamodelconverter.converter.base.AConstructorConverter;
import it.caneserpente.javamodelconverter.converter.base.ADatatypeConverter;
import it.caneserpente.javamodelconverter.model.*;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class TypescriptConstructorConverter extends AConstructorConverter {

    private final List<String> NO_CONSTRUCTOR_DATA_TYPES = Arrays.asList("number", "string", "String", "boolean", "Boolean", "any");

    /**
     * constructor
     */
    public TypescriptConstructorConverter(ADatatypeConverter datatypeConverter) {
        super(datatypeConverter);
        this.datatypeConverter = datatypeConverter;
    }


    @Override
    public JMCClass createConstructor(@Nullable JMCClass clz) {

        if (null != clz) {
            clz.setConvertedConstructorStart("\tconstructor()\n" +
                    "\tconstructor(m: " + clz.getConvertedClassName() + ")\n" +
                    "\tconstructor(m?: " + clz.getConvertedClassName() + ") {\n\n" +
                    "\t\tif (m) {\n");

            clz.getFieldList().stream().forEach(f -> this.createConstructorFieldAssignment(f));

            clz.setConvertedConstructorEnd("\t\t}\n\t}\n");
        }

        return clz;
    }


    @Override
    protected String createConstrJMCFieldBasic(JMCFieldBasic jf) {

        switch (jf.getJavaTypeName()) {

            case "java.util.Date":
            case "java.sql.Timestamp":
                return "\t\t\tthis." + jf.getJavaField().getName() + " = m." + jf.getJavaField().getName() + " ? new Date(m." + jf.getJavaField().getName() + ") : undefined;\n";

            default:
                return "\t\t\tthis." + jf.getJavaField().getName() + " = m." + jf.getJavaField().getName() + ";\n";
        }
    }

    @Override
    protected String createConstrJMCFieldArray(JMCFieldArray jf) {
        return this.createConstrJMCFieldArrayOrCollection(jf);
    }

    @Override
    protected String createConstrJMCFieldCollection(JMCFieldCollection jf) {
        return this.createConstrJMCFieldArrayOrCollection(jf);
    }

    @Override
    protected String createConstrJMCFieldMap(JMCFieldMap jf) {

        String converted = "\t\t\tthis." + jf.getJavaField().getName() + " = ";
        String convertedKey = "";
        String convertedValue;
        String fName = jf.getJavaField().getName();

        if (! jf.isParametrized()) {
            converted += "m." + fName + ";\n";
        } else {
            converted += "new Map<" + jf.getConvertedFieldKeyType() + ", " + jf.getConvertedFieldValueType() + ">();\n";
            converted += "\t\t\tif (m." + fName + ") {\n";
            converted += "\t\t\t\tfor (let k in m." + fName + ") {\n";

            if (! NO_CONSTRUCTOR_DATA_TYPES.contains(jf.getConvertedFieldKeyType())) {
                convertedKey = "new " + jf.getConvertedFieldKeyType() + "(k)";
            } else {
                // if number => we need to parse it
                if (jf.getConvertedFieldKeyType().equalsIgnoreCase("number")) {
                    convertedKey = "+";
                }

                convertedKey += "k";
            }
            if (! NO_CONSTRUCTOR_DATA_TYPES.contains(jf.getConvertedFieldValueType())) {
                convertedValue = "new " + jf.getConvertedFieldValueType() + "(m." + fName + "[k])";
            } else {
                convertedValue = "m." + fName + "[k]";
            }

            converted += "\t\t\t\t\tthis." + fName + ".set(" + convertedKey + ", " + convertedValue + ");\n";
            converted += "\t\t\t\t}\n";
            converted += "\t\t\t}\n";
        }

        return converted;
    }

    @Override
    protected String createConstrJMCFieldCustomType(JMCFieldCustomType jf) {
        return "\t\t\tthis." + jf.getJavaField().getName() + " = new " + jf.getConvertedFieldType() + "(m." + jf.getJavaField().getName() + ");\n";
    }




    /**
     * converts JMCFieldWithSubtype
     *
     * @param jf the JMCFieldWithSubtype to convert
     * @return JMCField converted
     */
    private String createConstrJMCFieldArrayOrCollection(JMCFieldWithSubtype jf) {
        if (null == jf.getConvertedSubtype() || jf.getConvertedSubtype().isEmpty() || NO_CONSTRUCTOR_DATA_TYPES.contains(jf.getConvertedSubtype())) {
            return "\t\t\tthis." + jf.getJavaField().getName() + " = m." + jf.getJavaField().getName() + ";\n";
        } else {
            return "\t\t\tthis." + jf.getJavaField().getName() + " = m." + jf.getJavaField().getName() + " ? m." + jf.getJavaField().getName() + ".map(s => new " + jf.getConvertedSubtype() + "(s)) : [];\n";
        }
    }
}
