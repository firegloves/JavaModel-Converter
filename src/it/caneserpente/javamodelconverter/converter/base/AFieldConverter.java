package it.caneserpente.javamodelconverter.converter.base;

import com.sun.istack.internal.Nullable;

import java.lang.reflect.Field;

public abstract class AFieldConverter {


    /**
     * converts fields received into desired language and return them as String
     *
     * @param fieldArray the array of Field to convert to desired language
     * @return received fields as string coded in desired language
     */
    public String convertFieldList(@Nullable Field[] fieldArray) {

        StringBuilder fieldsBuilder = new StringBuilder();

        if (null != fieldArray) {
            for (int i=0; i<fieldArray.length; i++) {
                fieldsBuilder.append(this.convertField(fieldArray[i])).append("\n");
            }
        }

        return fieldsBuilder.toString();
    }



    /**
     * converts field received into desired language and return it as String
     *
     * @param field the Field to convert to desired language
     * @return received field as string coded in desired language
     */
    protected abstract String convertField(@Nullable Field field);


}
