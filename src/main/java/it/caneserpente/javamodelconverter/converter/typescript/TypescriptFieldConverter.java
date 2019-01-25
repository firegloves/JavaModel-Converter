package it.caneserpente.javamodelconverter.converter.typescript;

import com.sun.istack.internal.NotNull;
import it.caneserpente.javamodelconverter.converter.base.ADatatypeConverter;
import it.caneserpente.javamodelconverter.converter.base.AFieldConverter;
import it.caneserpente.javamodelconverter.model.*;

public class TypescriptFieldConverter extends AFieldConverter {

    public TypescriptFieldConverter(ADatatypeConverter datatypeConverter) {
        super(datatypeConverter);
    }


    @Override
    protected JMCField setConvertedFieldStatement(@NotNull JMCField jf) {

        // set converted statement
        jf.setConvertedFieldStm("\t" + jf.getJavaField().getName() + ": " + jf.getConvertedFieldType() + ";\n");

        return jf;
    }


    @Override
    protected JMCField convertJMCFieldBasic(JMCFieldBasic jf) {
        jf.setConvertedFieldType(this.datatypeConverter.convertDataTypeName(jf.getJavaTypeName()));
        return jf;
    }

    @Override
    protected JMCField convertJMCFieldArray(JMCFieldArray jf) {
        return this.convertJMCFieldArrayOrCollection(jf);
    }

    @Override
    protected JMCField convertJMCFieldCollection(JMCFieldCollection jf) {
        return this.convertJMCFieldArrayOrCollection(jf);
    }

    @Override
    protected JMCField convertJMCFieldMap(JMCFieldMap jf) {

        String converted = "Map";

        jf.setConvertedFieldKeyType(this.datatypeConverter.convertDataTypeName(jf.getJavaSubtypeKeyName()));
        jf.setConvertedFieldValueType(this.datatypeConverter.convertDataTypeName(jf.getJavaSubtypeValueName()));
        converted += "<" + jf.getConvertedFieldKeyType() + ", " + jf.getConvertedFieldValueType() + ">";

        jf.setConvertedFieldType(converted);

        return jf;
    }

    @Override
    protected JMCField convertJMCFieldCustomType(JMCFieldCustomType jf) {
        jf.setConvertedFieldType(this.datatypeConverter.getTranspilingDataTypeSimpleName(jf.getJavaTypeName()));
        this.manageImportStatement(jf, jf.getJavaTypeName(), jf.getConvertedFieldType());
        return jf;
    }

    @Override
    protected void setImportDataType(JMCField jf, String convertedFieldType) {
        jf.setImportDataType(convertedFieldType);
        jf.setImportDataTypeStatement("import { " + convertedFieldType + " } from \"./" + convertedFieldType + "\";\n");
    }


    /**
     * converts JMCFieldWithSubtype
     * @param jf the JMCFieldWithSubtype to convert
     * @return JMCFieldWithSubtype converted
     */
    private JMCFieldWithSubtype convertJMCFieldArrayOrCollection(JMCFieldWithSubtype jf) {
        jf.setConvertedSubtype(this.datatypeConverter.convertDataTypeName(jf.getJavaSubtypeName()));
        jf.setConvertedFieldType(jf.getConvertedSubtype() + "[]");

        this.manageImportStatement(jf, jf.getJavaSubtypeName(), jf.getConvertedSubtype());

        return jf;
    }
}
