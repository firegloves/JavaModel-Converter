package it.caneserpente.javamodelconverter.builder;

import it.caneserpente.javamodelconverter.ESupporterLanguages;
import it.caneserpente.javamodelconverter.ApplicationConfig;
import it.caneserpente.javamodelconverter.converter.base.AClassConverter;
import it.caneserpente.javamodelconverter.converter.base.ADatatypeConverter;
import it.caneserpente.javamodelconverter.converter.typescript.TypescriptClassConverter;
import it.caneserpente.javamodelconverter.converter.typescript.TypescriptConstructorConverter;
import it.caneserpente.javamodelconverter.converter.typescript.TypescriptDatatypeConverter;
import it.caneserpente.javamodelconverter.converter.typescript.TypescriptFieldConverter;
import it.caneserpente.javamodelconverter.exception.JMCException;
import org.jeasy.props.api.PropertiesInjector;

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
        this.config = new ApplicationConfig();
        aNewPropertiesInjector().injectProperties(config);

        this.transpilingLang = ESupporterLanguages.valueOf(config.getTargetLanguage());
        this.transpilingClassList = transpilingClassList;

        this.map = new HashMap<>();
        this.map.put(ESupporterLanguages.TYPESCRIPT, () -> this.constructTypescript(this.transpilingClassList));
    }

    /**
     * builds and returns a AClassConverter based on transpilingLang variable
     *
     * @return the subtype AClassConverter correctly instantiated and composed
     */
    public AClassConverter construct() {
        try {
            return this.map.get(this.transpilingLang).get();
        } catch (Exception e) {
            throw new JMCException("Transpiling language unsupported or not recognized");
        }
    }


    /**
     * builds and returns a TypescriptClassConverter for Typescript transpiling
     *
     * @return a TypescriptClassConverter for Typescript transpiling
     */
    public AClassConverter constructTypescript(List<String> transpilingClassList) {

        ADatatypeConverter datatypeConverter = new TypescriptDatatypeConverter(transpilingClassList);

        return GenericBuilder.of(TypescriptClassConverter::new)
                .with(AClassConverter::setInputDirName, null)
                .with(AClassConverter::setOutputDir, null)
                .with(AClassConverter::setConstructorConverter, new TypescriptConstructorConverter(datatypeConverter))
                .with(AClassConverter::setFieldConverter, new TypescriptFieldConverter(datatypeConverter))
                .build();
    }
}
