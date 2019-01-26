package it.caneserpente.javamodelconverter.builder;

import it.caneserpente.javamodelconverter.ESupporterLanguages;
import it.caneserpente.javamodelconverter.ApplicationConfig;
import it.caneserpente.javamodelconverter.JavaFieldReader;
import it.caneserpente.javamodelconverter.converter.base.AClassConverter;
import it.caneserpente.javamodelconverter.converter.base.ADatatypeConverter;
import it.caneserpente.javamodelconverter.converter.typescript.TypescriptClassConverter;
import it.caneserpente.javamodelconverter.converter.typescript.TypescriptConstructorConverter;
import it.caneserpente.javamodelconverter.converter.typescript.TypescriptDatatypeConverter;
import it.caneserpente.javamodelconverter.converter.typescript.TypescriptFieldConverter;
import it.caneserpente.javamodelconverter.exception.JMCException;
import org.jeasy.props.api.PropertiesInjector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.jeasy.props.PropertiesInjectorBuilder.aNewPropertiesInjector;
import static org.jeasy.props.PropertiesInjectorBuilder.aNewPropertiesInjectorBuilder;

public class ClassConverterDirector {

    private Map<ESupporterLanguages, Supplier<AClassConverter>> map;

    private ApplicationConfig config;

    private ESupporterLanguages transpilingLang;
    private List<String> transpilingClassList;

    public ClassConverterDirector(List<String> transpilingClassList) {

        // lead application properties
        this.config = ApplicationConfig.getInstance();

        // transpiling language
        this.transpilingLang = ESupporterLanguages.valueOf(config.getTargetLanguage());
        if (!Arrays.asList(ESupporterLanguages.values()).contains(this.transpilingLang)) {
            throw new JMCException("Transpiling language unsupported or not recognized");
        }

        // transpiling class list
        this.transpilingClassList = transpilingClassList;

        // configure functions map
        this.map = new HashMap<>();
        this.map.put(ESupporterLanguages.TYPESCRIPT, () -> this.constructTypescript(this.transpilingClassList));
    }

    /**
     * builds and returns a AClassConverter based on transpilingLang variable
     *
     * @return the subtype AClassConverter correctly instantiated and composed
     */
    public AClassConverter construct() {
        return this.map.get(this.transpilingLang).get();
    }


    /**
     * builds and returns a TypescriptClassConverter for Typescript transpiling
     *
     * @return a TypescriptClassConverter for Typescript transpiling
     */
    public AClassConverter constructTypescript(List<String> transpilingClassList) {

        ADatatypeConverter datatypeConverter = new TypescriptDatatypeConverter(transpilingClassList);

        return GenericBuilder.of(TypescriptClassConverter::new)
                .with(AClassConverter::setInputDirName, this.config.getTargetCompiledDir())
                .with(AClassConverter::setOutputDirName, this.config.getTargetOutputDir())
                .with(AClassConverter::setFieldReader, new JavaFieldReader(datatypeConverter))
                .with(AClassConverter::setConstructorConverter, new TypescriptConstructorConverter(datatypeConverter))
                .with(AClassConverter::setFieldConverter, new TypescriptFieldConverter(datatypeConverter))
                .build();
    }
}
