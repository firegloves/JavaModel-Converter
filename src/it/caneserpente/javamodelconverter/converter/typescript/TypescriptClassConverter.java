package it.caneserpente.javamodelconverter.converter.typescript;

import com.sun.istack.internal.Nullable;
import it.caneserpente.javamodelconverter.converter.base.AClassConverter;
import it.caneserpente.javamodelconverter.converter.base.ADatatypeConverter;

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
        super(inputDir, outputDir, new TypescriptConstructorConverter(datatypeConverter), new TypescriptFieldConverter(datatypeConverter));
        this.datatypeConverter = datatypeConverter;
    }

    @Override
    protected String convertClassName(@Nullable Class clz) {
        return null != clz ? clz.getSimpleName() : null;
    }


    @Override
    protected void writeGeneratedClass(String clzName, String fields, String constructor) {

        StringBuilder sb = new StringBuilder();
        sb.append("export class " + clzName + "{ ").append("\n\n")
                .append(fields).append("\n\n")
                .append(constructor).append("\n\n")
                .append("}");

        try (PrintWriter writer = new PrintWriter(new File(super.outputDir.getAbsolutePath() + System.getProperty("file.separator") + clzName + ".ts"))) {
            writer.println(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


}
