package it.caneserpente.javamodelconverter;

import it.caneserpente.javamodelconverter.converter.base.ADatatypeConverter;
import it.caneserpente.javamodelconverter.exception.JMCException;
import it.caneserpente.javamodelconverter.javafield.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.Function;

public class JavaFieldReader {

    private ADatatypeConverter datatypeConverter;

    private Map<Class, Function> strategyMap;

    public JavaFieldReader(ADatatypeConverter datatypeConverter) {
        this.datatypeConverter = datatypeConverter;

        // TODO SPOSTARE TUTTO NEI COSTRUTTORI? O DIRETTAMENTE NEI METODI DI CREAZIONE E RISPARMIO TEMPO
        this.strategyMap = new HashMap<>();
        this.strategyMap.put(JavaFieldBasic.class, (Function<JavaFieldBasic, JavaField>) jf -> this.populateJavaFieldBasic(jf));
        this.strategyMap.put(JavaFieldArray.class, (Function<JavaFieldArray, JavaField>) jf -> this.populateJavaFieldArray(jf));
        this.strategyMap.put(JavaFieldCollection.class, (Function<JavaFieldCollection, JavaField>) jf -> this.populateJavaFieldCollection(jf));
        this.strategyMap.put(JavaFieldMap.class, (Function<JavaFieldMap, JavaField>) jf -> this.populateJavaFieldMap(jf));
    }

    /**
     * get a Class, accesses all its fields, populates a list of corresponding JavaField and returns it
     *
     * @param clz the class of which read field list
     * @return list of received class' corresponding JavaField
     */
    public List<JavaField> readClassFields(Class clz) {

        List<JavaField> fieldList = new ArrayList<>();

        Field[] fieldArray = clz.getDeclaredFields();
        for (int i = 0; i < fieldArray.length; i++) {
            fieldList.add(this.createJavaField(fieldArray[i]));
        }

        fieldList.stream().forEach(jf -> System.out.println(jf.toString()));

        return fieldList;
    }


    /**
     * receives a Field, instantiates the appropriate JavaField subtype, populates its data and returns it
     *
     * @param f Field of which craete JavaField subtype
     * @return JavaField subtype corresponding to the received Field
     */
    private JavaField createJavaField(Field f) {

        // instantiate java field
        JavaField jf = this.instantiateJavaField(f);

        // populate java field data
        return (JavaField) this.strategyMap.get(jf.getClass()).apply(jf);
    }


    /**
     * depending on received Field type instantiates a JavaField subtype and returns it
     *
     * @param f Field of which instantiates JavaField subtype depending on field type
     * @return instantiated JavaField corresponding to received Field type
     * @throws JMCException if Field type is not supported
     */
    private JavaField instantiateJavaField(Field f) {

        Class type = f.getType();

        // array
        if (type.isArray()) {
            return new JavaFieldArray(f);
        }

        // collection
        if (Collection.class.isAssignableFrom(type)) {
            return new JavaFieldCollection(f);
        }

        // map
        if (Map.class.isAssignableFrom(type)) {
            return new JavaFieldMap(f);
        }

        // generic supported type
        if (null != datatypeConverter.convertDataTypeName(f.getGenericType().getTypeName())) {
            return new JavaFieldBasic(f);
        }

        throw new JMCException("TYPE NOT SUPPORTED: " + f.getGenericType().getTypeName());
    }

    /**
     * populates JavaFieldBasic
     * @param jf the JavaFieldBasic to populate
     * @return JavaField populated
     */
    private JavaField populateJavaFieldBasic(JavaFieldBasic jf) {
        System.out.println("populateJavaField " + jf.getJavaField().getGenericType().getTypeName());
        return jf;
    }

    /**
     * populates JavaFieldArray
     * @param jf the JavaFieldArray to populate
     * @return JavaField populated
     */
    private JavaField populateJavaFieldArray(JavaFieldArray jf) {
        jf.setJavaSubtypeName(jf.getJavaField().getType().getComponentType().getTypeName());
        return jf;
    }

    /**
     * populates JavaFieldCollection
     * @param jf the JavaFieldCollection to populate
     * @return JavaField populated
     */
    private JavaField populateJavaFieldCollection(JavaFieldCollection jf) {
        if (jf.getJavaField().getGenericType() instanceof ParameterizedType) {
            jf.setJavaSubtypeName(((ParameterizedType) jf.getJavaField().getGenericType()).getActualTypeArguments()[0].getTypeName());
        }
        return jf;
    }

    /**
     * populates JavaFieldMap
     * @param jf the JavaFieldMap to populate
     * @return JavaField populated
     */
    private JavaField populateJavaFieldMap(JavaFieldMap jf) {
        if (jf.getJavaField().getGenericType() instanceof ParameterizedType) {
            jf.setJavaSubtypeKeyName(((ParameterizedType) jf.getJavaField().getGenericType()).getActualTypeArguments()[0].getTypeName());
            jf.setJavaSubtypeValue(((ParameterizedType) jf.getJavaField().getGenericType()).getActualTypeArguments()[1].getTypeName());
        }
        return jf;
    }

}
