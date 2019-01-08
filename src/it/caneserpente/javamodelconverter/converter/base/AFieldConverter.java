package it.caneserpente.javamodelconverter.converter.base;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import it.caneserpente.javamodelconverter.JavaModelConverter;
import it.caneserpente.javamodelconverter.model.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class AFieldConverter {

    protected Map<Class, Function> strategyMap;

    public AFieldConverter() {
        this.strategyMap = new HashMap<>();
        this.strategyMap.put(JMCFieldBasic.class, (Function<JMCFieldBasic, String>) jf -> this.convertJMCFieldBasic(jf));
        this.strategyMap.put(JMCFieldArray.class, (Function<JMCFieldArray, String>) jf -> this.convertJMCFieldArray(jf));
        this.strategyMap.put(JMCFieldCollection.class, (Function<JMCFieldCollection, String>) jf -> this.convertJMCFieldCollection(jf));
        this.strategyMap.put(JMCFieldMap.class, (Function<JMCFieldMap, String>) jf -> this.convertJMCFieldMap(jf));
    }

    /**
     * converts List<JMCField> received into desired language and save result into dedicated variable
     *
     * @param fieldList the list of JMCField to convert to desired language
     * @return received list of JMCField with converted variable populated with corresponding value into desired language
     */
    public List<JMCField> convertFieldList(@Nullable List<JMCField> fieldList) {

//        StringBuilder fieldsBuilder = new StringBuilder();

        if (null != fieldList) {
            fieldList.stream().forEach(f -> this.convertField(f));
//            for (int i=0; i<fieldArray.length; i++) {
//                fieldsBuilder.append(this.convertField(fieldArray[i])).append("\n");
//            }
        }

        return fieldList;
    }



    /**
     * converts one JMCField received into desired language and save result into dedicated variable
     *
     * @param jf the JMCField to convert to desired language
     * @return received JMCField with converted variable populated with corresponding value into desired language
     */
    protected JMCField convertField(@Nullable JMCField jf) {

        if (null != jf) {

            // converts typename
            jf.setConvertedFieldType((String) this.strategyMap.get(jf.getClass()).apply(jf));

            // set converted filed statement
            this.setConvertedFieldStatement(jf);

            if (JavaModelConverter.DEBUG) {
                System.out.println("CONVERTED: " + jf.getConvertedFieldStm());
            }
        }

        return jf;
    }


    /**
     * sets JMCField convertedFieldStm variable converting it into desired language
     * @param jf the JMCField of which set convertedFieldStm variable into desired language
     * @return updated
     */
    protected abstract JMCField setConvertedFieldStatement(@NotNull JMCField jf);


    /**
     * converts JMCFieldBasic
     * @param jf the JMCFieldBasic to convert
     * @return JMCField converted
     */
    protected abstract String convertJMCFieldBasic(JMCFieldBasic jf);

    /**
     * converts JMCFieldArray
     * @param jf the JMCFieldArray to convert
     * @return JMCField converted
     */
    protected abstract String convertJMCFieldArray(JMCFieldArray jf);

    /**
     * converts JMCFieldCollection
     * @param jf the JMCFieldCollection to convert
     * @return JMCField converted
     */
    protected abstract String convertJMCFieldCollection(JMCFieldCollection jf);

    /**
     * converts JMCFieldMap
     * @param jf the JMCFieldMap to convert
     * @return JMCField converted
     */
    protected abstract String convertJMCFieldMap(JMCFieldMap jf);

}
