package it.caneserpente.javamodelconverter;


import org.jeasy.props.annotations.Property;

public class ApplicationConfig {

    @Property(source = "application.properties", key = "target.language")
    private String targetLanguage;

    @Property(source = "application.properties", key = "target.inputDir")
    private String targetInputDir;

    @Property(source = "application.properties", key = "target.outputDir")
    private String targetOutputDir;


    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public String getTargetInputDir() {
        return targetInputDir;
    }

    public void setTargetInputDir(String targetInputDir) {
        this.targetInputDir = targetInputDir;
    }

    public String getTargetOutputDir() {
        return targetOutputDir;
    }

    public void setTargetOutputDir(String targetOutputDir) {
        this.targetOutputDir = targetOutputDir;
    }
}
