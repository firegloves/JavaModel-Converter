package it.caneserpente.javamodelconverter;

import it.caneserpente.javamodelconverter.converter.base.ADatatypeConverter;
import it.caneserpente.javamodelconverter.exception.JMCException;
import it.caneserpente.javamodelconverter.model.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class JavaFieldReader {

    private ADatatypeConverter datatypeConverter;

    public JavaFieldReader(ADatatypeConverter datatypeConverter) {
        this.datatypeConverter = datatypeConverter;
    }

    /**
     * get a JMCClass, accesses all its class' fields, populates a list of corresponding JMCField and returns received JMCClass with populated fields
     *
     * @param clz the class of which read field list
     * @return the received JMCClass with JMCField populated
     */
    public JMCClass readClassFields(JMCClass clz) {

        List<JMCField> fieldList = new ArrayList<>();

        Field[] fieldArray = clz.getClazz().getDeclaredFields();
        for (int i = 0; i < fieldArray.length; i++) {
            fieldList.add(this.createJavaField(fieldArray[i]));
        }

        if (JavaModelConverter.DEBUG) {
            fieldList.stream().forEach(jf -> System.out.println("READED: " + jf.toString()));
        }

        clz.setFieldList(fieldList);

        return clz;
    }



    /**
     * depending on received Field type instantiates a JMCField subtype and returns it
     *
     * @param f Field of which instantiates JMCField subtype depending on field type
     * @return instantiated JMCField corresponding to received Field type
     * @throws JMCException if Field type is not supported
     */
    private JMCField createJavaField(Field f) {

        Class type = f.getType();

        // array
        if (type.isArray()) {
            return new JMCFieldArray(f);
        }

        // collection
        if (Collection.class.isAssignableFrom(type)) {
            return new JMCFieldCollection(f);
        }

        // map
        if (Map.class.isAssignableFrom(type)) {
            return new JMCFieldMap(f);
        }

        // generic supported type
        if (null != datatypeConverter.convertDataTypeName(f.getGenericType().getTypeName())) {
            return new JMCFieldBasic(f);
        }

        throw new JMCException("TYPE NOT SUPPORTED: " + f.getGenericType().getTypeName());
    }

}
