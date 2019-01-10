package it.caneserpente.javamodelconverter.converter.typescript;

import com.sun.istack.internal.Nullable;
import it.caneserpente.javamodelconverter.converter.base.AConstructorConverter;
import it.caneserpente.javamodelconverter.converter.base.ADatatypeConverter;
import it.caneserpente.javamodelconverter.model.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.Function;

public class TypescriptConstructorConverter extends AConstructorConverter {

    private final List<String> NO_CONSTRUCTOR_DATA_TYPES = Arrays.asList("number", "string", "String", "boolean", "Boolean");

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
            clz.setConvertedConstructorInit("\tconstructor()\n" +
                    "\tconstructor(m: " + clz.getConvertedClassName() + ")\n" +
                    "\tconstructor(m?: " + clz.getConvertedClassName() + ") {\n");

            clz.getFieldList().stream().forEach(f -> this.createConstructorFieldAssignment(f));
        }

        return clz;
    }


//    @Override
//    protected JMCField createConstructorFieldAssignment(@Nullable JMCField jf) {
//
//        if (null != jf) {
//            // create constructor assignment statement
//            jf.setConvertedContructorFieldStm((String) this.strategyMap.get(jf.getClass()).apply(jf));
//        }
//
//        return jf;
//    }


    @Override
    protected String createConstrJMCFieldBasic(JMCFieldBasic jf) {

        switch (jf.getJavaTypeName()) {

            case "java.util.Date":
            case "java.sql.Timestamp":
                return "\t\tthis." + jf.getJavaField().getName() + " = m && m." + jf.getJavaField().getName() + " ? new Date(m." + jf.getJavaField().getName() + ") : undefined;\n";

            default:
                return "\t\tthis." + jf.getJavaField().getName() + " = m && m." + jf.getJavaField().getName() + " || undefined;\n";
        }
    }

    @Override
    protected String createConstrJMCFieldArray(JMCFieldArray jf) {

        if (null == jf.getConvertedSubtype() || jf.getConvertedSubtype().isEmpty() || NO_CONSTRUCTOR_DATA_TYPES.contains(jf.getConvertedSubtype())) {
            return "\t\tthis." + jf.getJavaField().getName() + " = m && m." + jf.getJavaField().getName() + ";\n";
        } else {
            return "\t\tthis." + jf.getJavaField().getName() + " = m && m." + jf.getJavaField().getName() + " ? m." + jf.getJavaField().getName() + ".map(s => new " + jf.getConvertedSubtype() + "(s)) : [];\n";
        }
    }

    @Override
    protected String createConstrJMCFieldCollection(JMCFieldCollection jf) {
        // TODO REFACTOR - EQUAL METHODS

        if (null == jf.getConvertedSubtype() || jf.getConvertedSubtype().isEmpty() || NO_CONSTRUCTOR_DATA_TYPES.contains(jf.getConvertedSubtype())) {
            return "\t\tthis." + jf.getJavaField().getName() + " = m && m." + jf.getJavaField().getName() + ";\n";
        } else {
            return "\t\tthis." + jf.getJavaField().getName() + " = m && m." + jf.getJavaField().getName() + " ? m." + jf.getJavaField().getName() + ".map(s => new " + jf.getConvertedSubtype() + "(s)) : [];\n";
        }
    }

    @Override
    protected String createConstrJMCFieldMap(JMCFieldMap jf) {

        String converted = "\t\tthis." + jf.getJavaField().getName() + " = ";
        String convertedKey;
        String convertedValue;

        if (! jf.isParametrized()) {
            converted += "m && m." + jf.getJavaField().getName() + ";\n";
        } else {
            converted += "new Map<" + jf.getConvertedFieldKeyType() + ", " + jf.getConvertedFieldValueType() + ">();\n";
            converted += "\t\tArray.from(m." + jf.getJavaField().getName() + ".keys()).forEach(k => {\n";

            if (! NO_CONSTRUCTOR_DATA_TYPES.contains(jf.getConvertedFieldKeyType())) {
                convertedKey = "new " + jf.getConvertedFieldKeyType() + "(k)";
            } else {
                convertedKey = "k";
            }
            if (! NO_CONSTRUCTOR_DATA_TYPES.contains(jf.getConvertedFieldValueType())) {
                convertedValue = "new " + jf.getConvertedFieldValueType() + "(m.get(k))";
            } else {
                convertedValue = "m.get(k)";
            }

            converted += "\t\t\tm.set(" + convertedValue + ", " + convertedValue + ");\n";
            converted += "\t\t};\n";
        }

        return converted;
    }

//    // Import stylesheets
//import './style.css';
//
//    class Dog {
//        id: number;
//        name: string;
//    }
//
//// Write TypeScript code!
//const appDiv: HTMLElement = document.getElementById('app');
//    appDiv.innerHTML = `<h1>TypeScript Starter</h1>`;
//
//    let rawMap = new Map();
//rawMap.set(1, 'ciao');
//rawMap.set(2, 'core');
//rawMap.set(3, 'pupazzo');
//
//    let rawMap2 = rawMap;
//
////console.log(rawMap2.get(3));
//
//
//    let mappone = new Map<Date, Dog>();
//
//    let data1 = new Date();
//    let gelsa = new Dog();
//    gelsa.id = 1;
//    gelsa.name = 'Gelsomina';
//
//    let data2 = new Date();
//data2.setFullYear(2020);
//    let alfredo = new Dog();
//    alfredo.id = 2;
//    alfredo.name = 'Alfredo';
//
//mappone.set(data1, gelsa);
//mappone.set(data2, alfredo);
//
//console.log("MAPPONA")
//
///*for (let [key, value] of mappone) {
//    console.log(key, value);
//}*/
//
////console.log(mappone.get(data1));
//
//
//    let mappone2 = new Map<Date, Dog>();
////console.log(mappone)
//for (let k of mappone.keys()) {
//        console.log('e: '  + k);
//    }
//
//
//    let map = new Map([
//    [ "APPLE", 1 ],
//            [ "ORANGE", 2 ],
//            [ "MANGO", 3 ]
//            ]);
//
//console.log("MAP")
//        for (let [key, value] of map) {
//        console.log(key, value);
//    }
//
//console.log('ENDs')

}
