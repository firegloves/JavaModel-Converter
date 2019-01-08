package it.caneserpente.javamodelconverter.converter.typescript;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import it.caneserpente.javamodelconverter.JavaModelConverter;
import it.caneserpente.javamodelconverter.converter.base.ADatatypeConverter;
import it.caneserpente.javamodelconverter.converter.base.AFieldConverter;
import it.caneserpente.javamodelconverter.model.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.Function;

public class TypescriptFieldConverter extends AFieldConverter {

    private final List<String> NO_CONSTRUCTOR_DATA_TYPES = Arrays.asList("number", "string", "String", "boolean", "Boolean");

    private ADatatypeConverter datatypeConverter;

    public TypescriptFieldConverter(ADatatypeConverter datatypeConverter) {
        this.datatypeConverter = datatypeConverter;
    }


    @Override
    protected JMCField setConvertedFieldStatement(@NotNull JMCField jf) {

        // set converted statement
        jf.setConvertedFieldStm("\t" + jf.getJavaField().getName() + ": " + jf.getConvertedFieldType() + ";\n");

        return jf;
    }


    @Override
    protected String convertJMCFieldBasic(JMCFieldBasic jf) {
        return this.datatypeConverter.convertDataTypeName(jf.getJavaTypeName());
    }

    @Override
    protected String convertJMCFieldArray(JMCFieldArray jf) {
        return this.datatypeConverter.convertDataTypeName(jf.getJavaSubtypeName()) + "[]";
    }

    @Override
    protected String convertJMCFieldCollection(JMCFieldCollection jf) {
        return this.datatypeConverter.convertDataTypeName(jf.getJavaSubtypeName()) + "[]";
    }

    @Override
    protected String convertJMCFieldMap(JMCFieldMap jf) {

        String converted = "Map";

        if (jf.isParametrized()) {
            converted += "<" + this.datatypeConverter.convertDataTypeName(jf.getJavaSubtypeKeyName()) + ", " + this.datatypeConverter.convertDataTypeName(jf.getJavaSubtypeValueName()) + ">";
        }

        return converted;
    }
}