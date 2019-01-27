package it.caneserpente.javamodelconverter.argsmanagement;


import com.beust.jcommander.Parameter;
import it.caneserpente.javamodelconverter.ESupportedLanguages;

public class ArgsConfig {


    @Parameter(names = { "-l", "--lang" }, description = "Target transpiling languages", validateWith = ArgsSupportedLangValidator.class)
    private ESupportedLanguages lang;

    @Parameter(names = {"-s", "--src"}, description = "Java model classes folder path", validateWith = ArgsDirExistsValidator.class)
    private String srcFolder;

    @Parameter(names = {"-c", "--compiled"}, description = "Java compiled classes folder path", validateWith = ArgsDirExistsValidator.class)
    private String compiledFolder;

    @Parameter(names = {"-g", "--generated"}, description = "Transpiled model classes folder path", validateWith = ArgsDirExistsValidator.class)
    private String generatedFolder;


    public ESupportedLanguages getLang() {
        return lang;
    }

    public void setLang(ESupportedLanguages lang) {
        this.lang = lang;
    }

    public String getSrcFolder() {
        return srcFolder;
    }

    public void setSrcFolder(String srcFolder) {
        this.srcFolder = srcFolder;
    }

    public String getCompiledFolder() {
        return compiledFolder;
    }

    public void setCompiledFolder(String compiledFolder) {
        this.compiledFolder = compiledFolder;
    }

    public String getGeneratedFolder() {
        return generatedFolder;
    }

    public void setGeneratedFolder(String generatedFolder) {
        this.generatedFolder = generatedFolder;
    }
}
