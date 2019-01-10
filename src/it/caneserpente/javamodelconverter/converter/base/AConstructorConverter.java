

package it.caneserpente.javamodelconverter.converter.base;

import com.sun.istack.internal.Nullable;
import it.caneserpente.javamodelconverter.model.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class AConstructorConverter {

    protected ADatatypeConverter datatypeConverter;

    protected Map<Class, Function> strategyMap;

    public AConstructorConverter(ADatatypeConverter datatypeConverter) {

        this.datatypeConverter = datatypeConverter;

        this.strategyMap = new HashMap<>();
        this.strategyMap.put(JMCFieldBasic.class, (Function<JMCFieldBasic, String>) jf -> this.createConstrJMCFieldBasic(jf));
        this.strategyMap.put(JMCFieldArray.class, (Function<JMCFieldArray, String>) jf -> this.createConstrJMCFieldArray(jf));
        this.strategyMap.put(JMCFieldCollection.class, (Function<JMCFieldCollection, String>) jf -> this.createConstrJMCFieldCollection(jf));
        this.strategyMap.put(JMCFieldMap.class, (Function<JMCFieldMap, String>) jf -> this.createConstrJMCFieldMap(jf));
    }


    /**
     * generate constructor code and return it as string
     *
     * @param clz the JMCClass of which create constructor code
     * @return JMCClass with constructor generated code in desired language
     */
    public abstract JMCClass createConstructor(@Nullable JMCClass clz);


    /**
     * generate the assignment constructor instruction of the received field
     *
     * @param jf the JMCField of which to create assignment constructor instruction in desired language
     * @return the JMCField populated with assignment constructor instruction in desired language
     */
    protected JMCField createConstructorFieldAssignment(@Nullable JMCField jf) {

        if (null != jf) {
            // create constructor assignment statement
            jf.setConvertedContructorFieldStm((String) this.strategyMap.get(jf.getClass()).apply(jf));
        }

        return jf;
    }


    /**
     * converts JMCFieldBasic
     *
     * @param jf the JMCFieldBasic to convert
     * @return JMCField converted
     */
    protected abstract String createConstrJMCFieldBasic(JMCFieldBasic jf);


    /**
     * converts JMCFieldArray
     *
     * @param jf the JMCFieldArray to convert
     * @return JMCField converted
     */
    protected abstract String createConstrJMCFieldArray(JMCFieldArray jf);


    /**
     * converts JMCFieldCollection
     *
     * @param jf the JMCFieldCollection to convert
     * @return JMCField converted
     */
    protected abstract String createConstrJMCFieldCollection(JMCFieldCollection jf);


    /**
     * converts JMCFieldMap
     *
     * @param jf the JMCFieldMap to convert
     * @return JMCField converted
     */
    protected abstract String createConstrJMCFieldMap(JMCFieldMap jf);
}
