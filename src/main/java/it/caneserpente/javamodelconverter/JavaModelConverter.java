package it.caneserpente.javamodelconverter;

import it.caneserpente.javamodelconverter.builder.ClassConverterDirector;
import it.caneserpente.javamodelconverter.converter.base.AClassConverter;

import java.util.List;

public class JavaModelConverter {

    public static final boolean DEBUG = true;

    /**
     * app entry point
     * @param args
     */
    public static void main(String[] args) {

        // scans directory for .java files and build it
        List<String> classList = new ClassListScanner().scanForClasses();
        classList.stream().forEach(c -> System.out.println(c));

//        ClassConverterBuilder builder = new ClassConverterBuilder()
//                .with($ -> {
//                    $.setClassConverter();
//                })

        ClassConverterDirector director = new ClassConverterDirector(classList);
        AClassConverter classConverter = director.construct();

        // init TypescriptDatatypeConverter
//        ADatatypeConverter datatypeConverter = new TypescriptDatatypeConverter(classList);

        classConverter.convertClassList(classList);
    }

//    /**
//     * instantiates converters
//     */
//    private static void createConverters() {
//
//    }
}
