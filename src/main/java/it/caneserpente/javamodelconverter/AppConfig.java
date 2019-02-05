package it.caneserpente.javamodelconverter;


import org.jeasy.props.annotations.Property;

import static org.jeasy.props.PropertiesInjectorBuilder.aNewPropertiesInjector;

public class AppConfig {

    private static AppConfig instance = new AppConfig();

    @Property(source = "application.properties", key = "target.language")
    private String targetLanguage;

    @Property(source = "application.properties", key = "target.javaClassesDir")
    private String targetJavaClassesDir;

    @Property(source = "application.properties", key = "target.compiledDir")
    private String targetCompiledDir;

    @Property(source = "application.properties", key = "target.outputDir")
    private String targetOutputDir;

    @Property(source = "application.properties", key = "filename.prefix")
    private String filenamePrefix;

    @Property(source = "application.properties", key = "filename.suffix")
    private String filenameSuffix;

    @Property(source = "application.properties", key = "flattenPkg")
    private boolean flattenPkg;

    /******************************************************************************
     *                      TYPESCRIPT SPECIFIC CONFIG
     *****************************************************************************/

    @Property(source = "application.properties", key = "typescript.generateInterface")
    private boolean generateInterface;

    @Property(source = "application.properties", key = "typescript.angularCodingStyle")
    private boolean angularCodingStyle;


    private AppConfig() {
        // inject props
        aNewPropertiesInjector().injectProperties(this);

        if (null == this.getFilenamePrefix()) {
            this.setFilenamePrefix("");
        }
        if (null == this.getFilenameSuffix()) {
            this.setFilenameSuffix("");
        }
    }

    public static AppConfig getInstance() {
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

    public static void setInstance(AppConfig instance) {
        AppConfig.instance = instance;
    }

    public boolean isAngularCodingStyle() {
        return angularCodingStyle;
    }

    public void setAngularCodingStyle(boolean angularCodingStyle) {
        this.angularCodingStyle = angularCodingStyle;
    }

    public String getFilenamePrefix() {
        return filenamePrefix;
    }

    public void setFilenamePrefix(String filenamePrefix) {
        this.filenamePrefix = filenamePrefix;
    }

    public String getFilenameSuffix() {
        return filenameSuffix;
    }

    public void setFilenameSuffix(String filenameSuffix) {
        this.filenameSuffix = filenameSuffix;
    }

    public boolean isFlattenPkg() {
        return flattenPkg;
    }

    public void setFlattenPkg(boolean flattenPkg) {
        this.flattenPkg = flattenPkg;
    }
}
