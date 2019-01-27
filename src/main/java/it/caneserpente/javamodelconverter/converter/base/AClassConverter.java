

package it.caneserpente.javamodelconverter.converter.base;

import it.caneserpente.javamodelconverter.ApplicationConfig;
import it.caneserpente.javamodelconverter.JavaFieldReader;
import it.caneserpente.javamodelconverter.model.JMCClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public abstract class AClassConverter {

    protected String inputDirName = "transpiling/compiled";
    protected String outputDirName = "transpiling/generated";

    protected File inputDir = null;
    protected File outputDir = null;

    protected ADatatypeConverter datatypeConverter;

    protected AConstructorConverter constructorConverter;
    protected AFieldConverter fieldConverter;

    private JavaFieldReader fieldReader;

    public AClassConverter() {
        this.loadConfig();
    }

    /**
     * load some current transpiling language configuration from application.properties
     */
    protected void loadConfig() {
    }

    public String getInputDirName() {
        return inputDirName;
    }

    public void setInputDirName(String inputDirName) {
        this.inputDirName = inputDirName;
        this.setInputDir(new File(this.inputDirName));
    }

    public String getOutputDirName() {
        return outputDirName;
    }

    public void setOutputDirName(String outputDirName) {
        this.outputDirName = outputDirName;
        this.setOutputDir(new File(this.outputDirName));
    }

    public File getInputDir() {
        return inputDir;
    }

    private void setInputDir(File inputDir) {
        this.inputDir = inputDir;
        if (! this.inputDir.exists() || ! this.inputDir.isDirectory()) {
            throw new RuntimeException("Input Dir " + this.inputDir.getAbsolutePath() + " does not exists or it is not a directory. Otherwise check for permissions");
        }
    }

    public File getOutputDir() {
        return outputDir;
    }

    private void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
        if (! this.outputDir.exists()) {
            this.outputDir.mkdirs();
        }
        if (! this.outputDir.exists() || !this.outputDir.isDirectory()) {
            throw new RuntimeException("Output Dir " + this.outputDir.getAbsolutePath() + " does not exists or it is not a directory. Otherwise check for permissions");
        }
    }

    public AConstructorConverter getConstructorConverter() {
        return constructorConverter;
    }

    public void setConstructorConverter(AConstructorConverter constructorConverter) {
        this.constructorConverter = constructorConverter;
    }

    public AFieldConverter getFieldConverter() {
        return fieldConverter;
    }

    public void setFieldConverter(AFieldConverter fieldConverter) {
        this.fieldConverter = fieldConverter;
    }

    public JavaFieldReader getFieldReader() {
        return fieldReader;
    }

    public void setFieldReader(JavaFieldReader fieldReader) {
        this.fieldReader = fieldReader;
    }

    public ADatatypeConverter getDatatypeConverter() {
        return datatypeConverter;
    }

    public void setDatatypeConverter(ADatatypeConverter datatypeConverter) {
        this.datatypeConverter = datatypeConverter;
        this.setFieldReader(new JavaFieldReader(datatypeConverter));
    }

    /**
     * load classes received as params and convert them with reflection
     * resulting files are written into outputDirName
     *
     * @param fqNameList list of full qualified names classes to convert
     */
    public void convertClassList(List<String> fqNameList) {

        if (null != fqNameList) {
            for (int i = 0; i < fqNameList.size(); i++) {
                JMCClass clz = this.loadClass(fqNameList.get(i));
                clz = this.fieldReader.readClassFields(clz);
                this.convertClass(clz);
            }
        }
    }

    /**
     * load one class from inputDirName and returns it
     *
     * @param className the class' full qualified name to load
     * @return loaded class wrapped into JMCClass
     */
    private JMCClass loadClass(String className) {

        try {
            // Convert File to a URL
            URL url = this.inputDir.toURI().toURL();
            URL[] urls = new URL[]{url};

            // Create a new class loader with the directory
            ClassLoader cl = new URLClassLoader(urls);

            return new JMCClass(cl.loadClass(className));

        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * converts received class into desired language
     *
     * @param clz class to convert
     */
    private void convertClass(@Nullable JMCClass clz) {

        if (null != clz) {

            // generate fields
            this.fieldConverter.convertFieldList(clz.getFieldList());

            // generate class name
            this.convertClassName(clz);

            // generate constructor
            this.constructorConverter.createConstructor(clz);

            // write class
            this.writeGeneratedClass(clz);
        }
    }


    /**
     * converts class name into desired language and set it into received JMCClass
     *
     * @param clz the JMCClass from which get name to convert
     * @return JMCClass with name converted into desired language
     */
    protected abstract JMCClass convertClassName(@Nullable JMCClass clz);



    /**
     * construct ang write class name into desired language
     *
     * @param clz the JMCClass of which generate code
     */
    protected abstract void writeGeneratedClass(@Nullable JMCClass clz);

    /**
     * creates and resturns name to assign to transpiled file to save
     * @return name to assign to transpiled file to save
     */
    protected abstract String createClassFileName(JMCClass clz);

    /**
     * add JMCClass imports to the StringBuilder
     * @param clz
     * @param sb
     */
    protected void writeImports(@NotNull JMCClass clz, @NotNull StringBuilder sb) {

        // NOTE: look at AField.Converter.manageImportStatement method'note

        List<String> imports = new ArrayList<>();

        clz.getFieldList().stream().forEach(f -> {

            String importStmt = f.getImportDataTypeStatement();

            if (null != importStmt && ! importStmt.isEmpty() && ! imports.contains(importStmt) && ! clz.getConvertedClassName().equalsIgnoreCase(f.getImportDataType())) {
                imports.add(importStmt);
                sb.append(importStmt);
            }
        });

        sb.append("\n");
    }
}
