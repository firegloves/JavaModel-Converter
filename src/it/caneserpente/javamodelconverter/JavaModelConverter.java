package it.caneserpente.javamodelconverter;

import it.caneserpente.javamodelconverter.converter.typescript.TypescriptClassConverter;
import it.caneserpente.javamodelconverter.converter.typescript.TypescriptDatatypeConverter;

import java.util.List;

public class JavaModelConverter {

    public static void main(String[] args) {

        // scans directory for .java files and build it
        List<String> classList = new ClassListScanner(null, null).scanForClasses();
        classList.stream().forEach(c -> System.out.println(c));

        new TypescriptClassConverter(null, null, new TypescriptDatatypeConverter()).convertClassList(classList);
    }
}
