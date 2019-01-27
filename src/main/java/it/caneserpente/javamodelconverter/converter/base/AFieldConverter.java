package it.caneserpente.javamodelconverter.converter.base;

import it.caneserpente.javamodelconverter.JavaModelConverter;
import it.caneserpente.javamodelconverter.model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class AFieldConverter {

    protected ADatatypeConverter datatypeConverter;
    protected Map<Class, Function> strategyMap;


    public AFieldConverter(ADatatypeConverter datatypeConverter) {

        this.datatypeConverter = datatypeConverter;

        this.strategyMap = new HashMap<>();
        this.strategyMap.put(JMCFieldBasic.class, (Function<JMCFieldBasic, JMCField>) jf -> this.convertJMCFieldBasic(jf));
        this.strategyMap.put(JMCFieldArray.class, (Function<JMCFieldArray, JMCField>) jf -> this.convertJMCFieldArray(jf));
        this.strategyMap.put(JMCFieldCollection.class, (Function<JMCFieldCollection, JMCField>) jf -> this.convertJMCFieldCollection(jf));
        this.strategyMap.put(JMCFieldMap.class, (Function<JMCFieldMap, JMCField>) jf -> this.convertJMCFieldMap(jf));
        this.strategyMap.put(JMCFieldCustomType.class, (Function<JMCFieldCustomType, JMCField>) jf -> this.convertJMCFieldCustomType(jf));
    }

    /**
     * converts List<JMCField> received into desired language and save result into dedicated variable
     *
     * @param fieldList the list of JMCField to convert to desired language
     * @return received list of JMCField with converted variable populated with corresponding value into desired language
     */
    public List<JMCField> convertFieldList(@Nullable List<JMCField> fieldList) {

        if (null != fieldList) {
            fieldList.stream().forEach(f -> this.convertField(f));
        }

        return fieldList;
    }

    // TODO adjust doc!!!!


    /**
     * converts one JMCField received into desired language and save result into dedicated variable
     *
     * @param jf the JMCField to convert to desired language
     * @return received JMCField with converted variable populated with corresponding value into desired language
     */
    protected JMCField convertField(@Nullable JMCField jf) {

        if (null != jf) {

            // converts typename
            this.strategyMap.get(jf.getClass()).apply(jf);

            // set converted filed statement
            this.setConvertedFieldStatement(jf);

            if (JavaModelConverter.DEBUG) {
                System.out.println("CONVERTED: " + jf.getConvertedFieldStm());
            }
        }

        return jf;
    }


    /**
     * converts
     * sets JMCField ConvertedFieldType variable converting it into desired language
     * @param jf the JMCField of which set convertedFieldStm variable into desired language
     * @return updated
     */
    protected abstract JMCField setConvertedFieldStatement(@NotNull JMCField jf);


    /**
     * converts JMCFieldBasic
     * @param jf the JMCFieldBasic to convert
     * @return JMCField converted
     */
    protected abstract JMCField convertJMCFieldBasic(JMCFieldBasic jf);

    /**
     * converts JMCFieldArray
     * @param jf the JMCFieldArray to convert
     * @return JMCField converted
     */
    protected abstract JMCField convertJMCFieldArray(JMCFieldArray jf);

    /**
     * converts JMCFieldCollection
     * @param jf the JMCFieldCollection to convert
     * @return JMCField converted
     */
    protected abstract JMCField convertJMCFieldCollection(JMCFieldCollection jf);

    /**
     * converts JMCFieldMap
     * @param jf the JMCFieldMap to convert
     * @return JMCField converted
     */
    protected abstract JMCField convertJMCFieldMap(JMCFieldMap jf);

    /**
     * converts JMCFieldCustomType
     * @param jf the JMCFieldCustomType to convert
     * @return JMCField converted
     */
    protected abstract JMCField convertJMCFieldCustomType(JMCFieldCustomType jf);

    /**
     * manage a possible import statement and set it into received JMCField importDataTypeStatement var
     * if typeName belongs to the list of current transpiling data types add the corresponding import statement
     *
     * @param jf
     * @param typeName
     * @param convertedFieldType
     */
    protected void manageImportStatement(JMCField jf, String typeName, String convertedFieldType) {

        // NOTE: we should check if typeName is the same of the owner class and eventually avoid to set important
        // ex. a Foo class containing a List<Foo> fooList variable. In this case is an error to import Foo into itself
        // to avoid to pass owner class variable into the whole process of fields conversion this check is demanded to the AClassConverter.writeGeneratedClass method

        if (this.datatypeConverter.isTranspilingDataType(typeName)) {
            this.setImportDataType(jf, convertedFieldType);
        }
    }

    /**
     * set the import data type and the corresponding statement transcoded into desired language into received JMCField importDataTypeStatement var
     *
     * @param jf
     * @param convertedFieldType
     */
    protected abstract void setImportDataType(JMCField jf, String convertedFieldType);
}
