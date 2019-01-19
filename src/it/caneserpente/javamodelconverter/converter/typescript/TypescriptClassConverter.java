package it.caneserpente.javamodelconverter.converter.typescript;

import com.sun.istack.internal.Nullable;
import it.caneserpente.javamodelconverter.converter.base.AClassConverter;
import it.caneserpente.javamodelconverter.converter.base.ADatatypeConverter;
import it.caneserpente.javamodelconverter.model.JMCClass;

import java.io.File;
import java.io.PrintWriter;

public class TypescriptClassConverter extends AClassConverter {

    private ADatatypeConverter datatypeConverter;

    /**
     * constructor
     *
     * @param inputDir  : directory from where read class to generated. if null resources/compiled directory is choosen
     * @param outputDir : directory where to write generated files. if null resources/generated directory is choosen
     */
    public TypescriptClassConverter(String inputDir, String outputDir, ADatatypeConverter datatypeConverter) {
        super(inputDir, outputDir, new TypescriptConstructorConverter(datatypeConverter), new TypescriptFieldConverter(datatypeConverter), datatypeConverter);
        this.datatypeConverter = datatypeConverter;
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

        // open class block
        sb.append("export class " + clz.getConvertedClassName() + " {").append("\n\n");

        // fields
        clz.getFieldList().stream().forEach(f -> sb.append(f.getConvertedFieldStm()));

        // constructor
        sb.append("\n\n" + clz.getConvertedConstructorStart());
        clz.getFieldList().stream().forEach(f -> sb.append(f.getConvertedContructorFieldStm()));
//        sb.append("\t}\n\n");
        sb.append(clz.getConvertedConstructorEnd());

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
