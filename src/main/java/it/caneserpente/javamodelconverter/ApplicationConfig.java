package it.caneserpente.javamodelconverter;


import org.jeasy.props.annotations.Property;

import static org.jeasy.props.PropertiesInjectorBuilder.aNewPropertiesInjector;

public class ApplicationConfig {

    private static ApplicationConfig instance;

    @Property(source = "application.properties", key = "target.language")
    private String targetLanguage;

    @Property(source = "application.properties", key = "target.javaClassesDir")
    private String targetJavaClassesDir;

    @Property(source = "application.properties", key = "target.compiledDir")
    private String targetCompiledDir;

    @Property(source = "application.properties", key = "target.outputDir")
    private String targetOutputDir;


    /******************************************************************************
     *                      TYPESCRIPT SPECIFIC CONFIG
     *****************************************************************************/

    @Property(source = "application.properties", key = "typescript.generateInterface")
    private boolean generateInterface;

    @Property(source = "application.properties", key = "typescript.angularCodingStyle")
    private boolean angularCodingStyle;


    private ApplicationConfig() {
        aNewPropertiesInjector().injectProperties(this);
    }

    public static ApplicationConfig getInstance() {
        if (null == instance) {
            instance = new ApplicationConfig();
        }
        return instance;
    }


    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public String getTargetJavaClassesDir() {
        return targetJavaClassesDir;
    }

    public void setTargetJavaClassesDir(String targetJavaClassesDir) {
        this.targetJavaClassesDir = targetJavaClassesDir;
    }

    public String getTargetCompiledDir() {
        return targetCompiledDir;
    }

    public void setTargetCompiledDir(String targetCompiledDir) {
        this.targetCompiledDir = targetCompiledDir;
    }

    public String getTargetOutputDir() {
        return targetOutputDir;
    }

    public void setTargetOutputDir(String targetOutputDir) {
        this.targetOutputDir = targetOutputDir;
    }

    public boolean isGenerateInterface() {
        return generateInterface;
    }

    public void setGenerateInterface(boolean generateInterface) {
        this.generateInterface = generateInterface;
    }

    public static void setInstance(ApplicationConfig instance) {
        ApplicationConfig.instance = instance;
    }

    public boolean isAngularCodingStyle() {
        return angularCodingStyle;
    }

    public void setAngularCodingStyle(boolean angularCodingStyle) {
        this.angularCodingStyle = angularCodingStyle;
    }
}
