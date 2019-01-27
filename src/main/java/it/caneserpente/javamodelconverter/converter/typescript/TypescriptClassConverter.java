package it.caneserpente.javamodelconverter.converter.typescript;

import it.caneserpente.javamodelconverter.ApplicationConfig;
import it.caneserpente.javamodelconverter.converter.base.AClassConverter;
import it.caneserpente.javamodelconverter.converter.base.ADatatypeConverter;
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

    public TypescriptClassConverter() {
        super();
        this.loadConfig();
    }

    @Override
    protected void loadConfig() {
        this.generateInterfaces = ApplicationConfig.getInstance().isGenerateInterface();
        this.typeToGenerate = this.generateInterfaces ? "interface" : "class";
    }


    @Override
    protected JMCClass convertClassName(@Nullable JMCClass clz) {
        if (null != clz) {
            clz.setConvertedClassName(clz.getClazz().getSimpleName());
        }
        return clz;
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

        try (PrintWriter writer = new PrintWriter(new File(super.outputDir.getAbsolutePath() + System.getProperty("file.separator") + clz.getConvertedClassName() + ".ts"))) {
            writer.println(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
