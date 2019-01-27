package it.caneserpente.javamodelconverter.builder;

import it.caneserpente.javamodelconverter.ApplicationConfig;
import it.caneserpente.javamodelconverter.ESupportedLanguages;
import it.caneserpente.javamodelconverter.JavaFieldReader;
import it.caneserpente.javamodelconverter.converter.base.AClassConverter;
import it.caneserpente.javamodelconverter.converter.base.ADatatypeConverter;
import it.caneserpente.javamodelconverter.converter.typescript.TypescriptClassConverter;
import it.caneserpente.javamodelconverter.converter.typescript.TypescriptConstructorConverter;
import it.caneserpente.javamodelconverter.converter.typescript.TypescriptDatatypeConverter;
import it.caneserpente.javamodelconverter.converter.typescript.TypescriptFieldConverter;
import it.caneserpente.javamodelconverter.exception.JMCException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ClassConverterDirector {

    /**
     * transpiling mapping
     */
    private Map<ESupportedLanguages, Supplier<AClassConverter>> map;

    private ApplicationConfig config;

    private ESupportedLanguages transpilingLang;
    private List<String> transpilingClassList;

    public ClassConverterDirector(List<String> transpilingClassList) {

        // load application properties
        this.config = ApplicationConfig.getInstance();

        // transpiling language
        this.transpilingLang = ESupportedLanguages.valueOf(config.getTargetLanguage());
        if (!Arrays.asList(ESupportedLanguages.values()).contains(this.transpilingLang)) {
            throw new JMCException("Transpiling language unsupported or not recognized");
        }

        // transpiling class list
        this.transpilingClassList = transpilingClassList;

        // configure functions map
        this.configureMap();
    }


    /**
     * configure strategy pattern's map
     */
    private void configureMap() {

        this.map = new HashMap<>();

        this.map.put(ESupportedLanguages.TYPESCRIPT, () -> this.constructTypescript());

        // ADD HERE NEW LANGUAGES MAPPINGS
    }


    /**
     * builds and returns a AClassConverter based on transpilingLang variable
     *
     * @return the subtype AClassConverter correctly instantiated and composed
     */
    public AClassConverter construct() {
        return map.get(this.transpilingLang).get();
    }


    /**
     * builds and returns a TypescriptClassConverter for Typescript transpiling
     *
     * @return a TypescriptClassConverter for Typescript transpiling
     */
    private AClassConverter constructTypescript() {

        ADatatypeConverter datatypeConverter = new TypescriptDatatypeConverter(this.transpilingClassList);

        return GenericBuilder.of(TypescriptClassConverter::new)
                .with(AClassConverter::setInputDirName, this.config.getTargetCompiledDir())
                .with(AClassConverter::setOutputDirName, this.config.getTargetOutputDir())
                .with(AClassConverter::setFieldReader, new JavaFieldReader(datatypeConverter))
                .with(AClassConverter::setConstructorConverter, new TypescriptConstructorConverter(datatypeConverter))
                .with(AClassConverter::setFieldConverter, new TypescriptFieldConverter(datatypeConverter))
                .build();
    }
}
