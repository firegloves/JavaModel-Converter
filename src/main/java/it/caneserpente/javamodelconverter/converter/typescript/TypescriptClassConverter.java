package it.caneserpente.javamodelconverter.converter.typescript;

import it.caneserpente.javamodelconverter.AppConfig;
import it.caneserpente.javamodelconverter.converter.base.AClassConverter;
import it.caneserpente.javamodelconverter.model.JMCClass;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.PrintWriter;

public class TypescriptClassConverter extends AClassConverter {


    /**
     * if true interfaces are generated instead of classes
     */
    private boolean generateInterfaces;

    /**
     * contains type name to generate
     */
    private String typeToGenerate;

    /**
     * if true follows angular coding style
     */
    private boolean angularCodingStyle;

    public TypescriptClassConverter() {
        super();
        this.loadConfig();
    }

    @Override
    protected void loadConfig() {
        this.generateInterfaces = AppConfig.getInstance().isGenerateInterface();
        this.typeToGenerate = this.generateInterfaces ? "interface" : "class";
        this.angularCodingStyle = AppConfig.getInstance().isAngularCodingStyle();
    }

    @Override
    protected JMCClass convertFileName(@Nullable JMCClass clz) {
        if (null != clz) {

            String fileName;

            if (this.angularCodingStyle) {
                // split each found uppercase
                String[] clzNameToken = clz.getConvertedClassName().split("(?=\\p{Lu})");
                // example: pojo-test.model.ts
                fileName = String.join("-", clzNameToken).toLowerCase() + ".model";
            } else {
                fileName = clz.getConvertedClassName();
            }

            clz.setFileName(fileName);
        }
        return clz;
    }


    @Override
    protected JMCClass convertClassName(@Nullable JMCClass clz) {
        if (null != clz) {
            clz.setConvertedClassName(clz.getClazz().getSimpleName());
        }
        return clz;
    }

    @Override
    protected String getTranspilingLangExtension() {
        return ".ts";
    }


    @Override
    protected void writeGeneratedClass(@Nullable JMCClass clz) {

        StringBuilder sb = new StringBuilder();

        // import
        this.writeImports(clz, sb);

        // open class block
        sb.append("export " + this.typeToGenerate + " " + clz.getConvertedClassName() + " {").append("\n\n");

        // fields
        clz.getFieldList().stream().forEach(f -> sb.append(f.getConvertedFieldStm()));

        // constructor (only if class generation)
        if (! this.generateInterfaces) {

            sb.append("\n\n" + clz.getConvertedConstructorStart());
            clz.getFieldList().stream().forEach(f -> sb.append(f.getConvertedContructorFieldStm()));
            sb.append(clz.getConvertedConstructorEnd());
        }

        // close class block
        sb.append("}");

        // write on fs
        this.writeClassFile(clz, sb);
    }




}
