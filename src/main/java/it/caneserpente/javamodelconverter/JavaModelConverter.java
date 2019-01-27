package it.caneserpente.javamodelconverter;

import com.beust.jcommander.JCommander;
import it.caneserpente.javamodelconverter.argsmanagement.ArgsConfig;
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

        // reads cmd line args and configs application
        readArgs(args);

        // scans directory for .java files and build it
        List<String> classList = new ClassListScanner().scanForClasses();

        if (DEBUG) {
            classList.stream().forEach(c -> System.out.println(c));
        }

        // create desired class converter
        AClassConverter classConverter = new ClassConverterDirector(classList).construct();

        // convert classes
        classConverter.convertClassList(classList);
    }

    private static void readArgs(String[] args) {

        // parse cmd line parameters
        ArgsConfig argsConfig = new ArgsConfig();
        JCommander.newBuilder()
                .addObject(argsConfig)
                .build()
                .parse(args);

        // init AppConfig
        AppConfig appConfig = AppConfig.getInstance();

        // target lang
        if (null != argsConfig.getLang()) {
            appConfig.setTargetLanguage(argsConfig.getLang().name());
        }

        // src dir
        if (null != argsConfig.getSrcFolder()) {
            appConfig.setTargetJavaClassesDir(argsConfig.getSrcFolder());
        }

        // compiled dir
        if (null != argsConfig.getCompiledFolder()) {
            appConfig.setTargetCompiledDir(argsConfig.getCompiledFolder());
        }

        // generated dir
        if (null != argsConfig.getGeneratedFolder()) {
            appConfig.setTargetOutputDir(argsConfig.getGeneratedFolder());
        }
    }
}
