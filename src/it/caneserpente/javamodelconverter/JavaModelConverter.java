package it.caneserpente.javamodelconverter;

import it.caneserpente.javamodelconverter.converter.base.ADatatypeConverter;
import it.caneserpente.javamodelconverter.converter.typescript.TypescriptClassConverter;
import it.caneserpente.javamodelconverter.converter.typescript.TypescriptDatatypeConverter;

import java.util.List;

public class JavaModelConverter {

    public static final boolean DEBUG = true;

    public static void main(String[] args) {

        // scans directory for .java files and build it
        List<String> classList = new ClassListScanner(null, null).scanForClasses();
        classList.stream().forEach(c -> System.out.println(c));

        // init TypescriptDatatypeConverter
        ADatatypeConverter datatypeConverter = new TypescriptDatatypeConverter(classList);

        new TypescriptClassConverter(null, null, datatypeConverter).convertClassList(classList);
    }
}
