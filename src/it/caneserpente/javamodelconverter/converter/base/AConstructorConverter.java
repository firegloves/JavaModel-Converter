

package it.caneserpente.javamodelconverter.converter.base;

import com.sun.istack.internal.Nullable;
import it.caneserpente.javamodelconverter.model.JMCClass;
import it.caneserpente.javamodelconverter.model.JMCField;

import java.lang.reflect.Field;

public abstract class AConstructorConverter {

    protected ADatatypeConverter datatypeConverter;

    public AConstructorConverter(ADatatypeConverter datatypeConverter) {
        this.datatypeConverter = datatypeConverter;
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
    protected abstract JMCField createConstructorFieldAssignment(@Nullable JMCField jf);
}
