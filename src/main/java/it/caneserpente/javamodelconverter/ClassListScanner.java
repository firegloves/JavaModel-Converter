/**
 * scans directory to load all present .java classes
 * parses all full qualified present classes's names and save it into a list
 */

package it.caneserpente.javamodelconverter;

import it.caneserpente.javamodelconverter.exception.JMCException;
import org.jetbrains.annotations.Nullable;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ClassListScanner {

    private String inputDirName = "transpiling/";
    private String compiledDirName = "transpiling/compiled";

    private String javacArgsFilePath;

    private File inputDir;
    private File compiledDir;

    private JavaCompiler compiler;
    private List<String> compileFileList;


    public ClassListScanner() {

        AppConfig config = AppConfig.getInstance();

        // input dir
        if (null != config.getTargetJavaClassesDir() && !config.getTargetJavaClassesDir().isEmpty()) {
            this.inputDirName = config.getTargetJavaClassesDir();
        }
        this.inputDir = new File(this.inputDirName);
        if (!this.inputDir.exists() || !this.inputDir.isDirectory()) {
            throw new RuntimeException("Input Dir " + this.inputDir.getAbsolutePath() + LogMessages.ERR_SRC_FOLDER);
        }

        // compiled dir
        if (null != config.getTargetCompiledDir() && !config.getTargetCompiledDir().isEmpty()) {
            this.compiledDirName = config.getTargetCompiledDir();
        }
        this.compiledDir = new File(this.compiledDirName);
        if (!this.compiledDir.exists()) {
            this.compiledDir.mkdirs();
        }
        if (!this.compiledDir.exists() || !this.compiledDir.isDirectory()) {
            throw new RuntimeException("Compiled Dir " + this.compiledDir.getAbsolutePath() + LogMessages.ERR_SRC_FOLDER);
        }

        this.javacArgsFilePath = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "argsFile.txt";

        // init compiler
        compileFileList = new ArrayList<>();
        compiler = ToolProvider.getSystemJavaCompiler();
    }


    /**
     * scans inputDir to load all present .java classes
     * for each java class construct its full qualified name and add it to the list to return
     *
     * @return the list of full qualified names to return
     */
    public List<String> scanForClasses() {

        List<String> classNameList = new ArrayList<>();

        // scans files
        String[] fileList = inputDir.list();
        for (int i = 0; i < fileList.length; i++) {

            String clsName = this.scanJavaFile(fileList[i]);
            if (null != clsName && !clsName.isEmpty()) {
                classNameList.add(clsName);
            }
        }

        if (classNameList.isEmpty()) {
            throw new JMCException(LogMessages.ERR_NO_FILES_TO_TRANSPILE + inputDir.getAbsolutePath());
        }

        // compile files
        this.compileClasses();

        return classNameList;
    }


    /**
     * scans a file to create java class full qualified name
     * if full qaulified name is found => compile class
     *
     * @param fileName the file name to be scanned
     * @return full qualified java class name
     */
    private String scanJavaFile(String fileName) {

        File clsFile = new File(inputDir.getAbsolutePath() + System.getProperty("file.separator") + fileName);

        if (clsFile.isDirectory()) {
            return null;
        }

        System.out.println("# SCANNING FILE " + clsFile);

        try (Scanner scanner = new Scanner(clsFile)) {

            String pkg = null, className = null, retVal = null;

            // scan file searching for full qualified name
            while (scanner.hasNext()) {
                String l = scanner.nextLine();

                // TODO GESTIRE CASO IN CUI IL PACKAGE NON SIA PRESENTE???

                if (null == pkg) {
                    String p = this.findPackage(l);
                    if (null != p && !p.isEmpty()) {
                        pkg = p;
                    }
                }

                if (null != pkg) {
                    className = this.findClassName(l);
                    if (null != className && !className.isEmpty()) {
                        retVal = pkg + "." + className;
                    }
                }
            }

            this.compileFileList.add(clsFile.getAbsolutePath());

            return retVal;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * search for the package instruction into the lthisine that receives as param
     *
     * @param line
     * @return package name or null
     */
    private String findPackage(@Nullable String line) {
        return null != line && line.startsWith("package") ? line.replace("package ", "").replace(";", "") : null;
    }


    /**
     * search for the class name instruction into the line that receives as param
     *
     * @param line
     * @return class name or null
     */
    private String findClassName(@Nullable String line) {

        String clsName = null;

        if (null != line && line.contains("class")) {

            String[] tokens = line.split("class");
            if (null != tokens && tokens.length >= 2) {
                clsName = tokens[1].replace("{", "").trim();
            }
        }

        return clsName;
    }




    /**
     * compile alla identified java files
     *
     */
    private void compileClasses() {

        // write args file
        this.writeJavacArgsFile();

        // compile
        compiler.run(null, null, null,
                "-d", this.compiledDir.getAbsolutePath(),    // output folder
                "-cp", this.inputDir.getAbsolutePath(),                 // classpath
                "@" + javacArgsFilePath                                 // javac argsFile
        );

    }


    /**
     * create and save javac argsFile into system temp directory
     * @return
     */
    private void writeJavacArgsFile() {

        // Save file
        try (Writer writer = new FileWriter(this.javacArgsFilePath)) {
            writer.write(this.compileFileList.stream().collect(Collectors.joining("\n")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
