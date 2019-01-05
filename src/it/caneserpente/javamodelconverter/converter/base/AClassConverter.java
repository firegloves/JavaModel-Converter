

package it.caneserpente.javamodelconverter.converter.base;

import com.sun.istack.internal.Nullable;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public abstract class AClassConverter {

    protected String inputDirName = "resources/compiled";
    protected String outputDirName = "resources/generated";

    protected File inputDir = null;
    protected File outputDir = null;

    protected AConstructorConverter constructorConverter;
    protected AFieldConverter fieldConverter;

    public AClassConverter(String inputDir, String outputDir, AConstructorConverter constructorConverter, AFieldConverter fieldConverter) {

        // input dir
        if (null != inputDir && !inputDir.isEmpty()) {
            this.inputDirName = inputDir;
        }
        this.inputDir = new File(this.inputDirName);
        if (!this.inputDir.exists() || !this.inputDir.isDirectory()) {
            throw new RuntimeException("Input Dir " + this.inputDir.getAbsolutePath() + " does not exists or it is not a directory. Otherwise check for permissions");
        }

        // output dir
        if (null != outputDir && !outputDir.isEmpty()) {
            this.outputDirName = outputDir;
        }
        this.outputDir = new File(this.outputDirName);
        if (!this.outputDir.exists() || !this.outputDir.isDirectory()) {
            throw new RuntimeException("Input Dir " + this.outputDir.getAbsolutePath() + " does not exists or it is not a directory. Otherwise check for permissions");
        }

        // sub converters
        this.constructorConverter = constructorConverter;
        this.fieldConverter = fieldConverter;
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
                Class clz = this.loadClass(fqNameList.get(i));
                this.convertClass(clz);
            }
        }
    }

    /**
     * load one class from inputDirName and returns it
     *
     * @param className the class' full qualified name to load
     * @return loaded class
     */
    private Class loadClass(String className) {

        try {
            // Convert File to a URL
            URL url = this.inputDir.toURI().toURL();
            URL[] urls = new URL[]{url};

            // Create a new class loader with the directory
            ClassLoader cl = new URLClassLoader(urls);

            return cl.loadClass(className);

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
    private void convertClass(@Nullable Class clz) {

        if (null != clz) {

            // generate fields
            Field[] fieldArray = clz.getDeclaredFields();
            String fields = this.fieldConverter.convertFieldList(fieldArray);

            // generate class name
            String clzName = this.convertClassName(clz);

            // generate constructor
            String constructor = this.constructorConverter.createConstructor(clzName, fieldArray);

            // write class
            this.writeGeneratedClass(clzName, fields, constructor);
        }
    }


    /**
     * converts class name into desired language and return it as String
     *
     * @param clz the Class from which get name to convert
     * @return class name converted into desired language
     */
    protected abstract String convertClassName(@Nullable Class clz);



    /**
     * construct ang write class name into desired language
     *
     * @param clzName the name to assign to the new class
     * @param fields string representing fields list
     * @param constructor string representing class constructor
     */
    protected abstract void writeGeneratedClass(String clzName, String fields, String constructor);
}