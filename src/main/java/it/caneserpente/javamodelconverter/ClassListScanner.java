/**
 * scans directory to load all present .java classes
 * parses all full qualified present classes's names and save it into a list
 */

package it.caneserpente.javamodelconverter;

import it.caneserpente.javamodelconverter.exception.JMCException;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.Nullable;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        // TODO check if I have a double / also on linux systems
        this.javacArgsFilePath = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "argsFile.txt";

        // init compiler
        compileFileList = new ArrayList<>();
        compiler = ToolProvider.getSystemJavaCompiler();

        if (null == compiler) {
            throw new JMCException("Can't get System Java compiler. A possible cause is that you are using JRE instead of JDK. Look at this: https://stackoverflow.com/questions/1388690/how-do-i-make-the-jdk-the-default-jre");
        }
    }


    /**
     * scans inputDir to load all present .java classes
     * for each java class construct its full qualified name and add it to the list to return
     *
     * @return the list of full qualified names identified classes
     */
    public List<String> scanForClasses() {

        List<String> classNameList = new ArrayList<>();

        // scans files
        classNameList = this.scanDir(inputDir.getAbsolutePath());

        if (classNameList.isEmpty()) {
            throw new JMCException(LogMessages.ERR_NO_FILES_TO_TRANSPILE + inputDir.getAbsolutePath());
        }

        // compile files
        this.compileClasses();

        return classNameList;
    }

    /**
     * scans received to load all present .java classes
     *
     * @return the list of full qualified names identified classes
     */
    private List<String> scanDir(String dirToScan) {

        List<String> foundClasses = new ArrayList<>();

        try (Stream<Path> stream = Files.list(Paths.get(dirToScan))) {

            stream.forEach(p -> {
                        if (Files.isDirectory(p)) {
                            // scan subfolder
                            foundClasses.addAll(this.scanDir(p.toString()));
                        } else if(FilenameUtils.getExtension(p.toString()).equalsIgnoreCase("java")) {
                            // read classes in this folder
                            String clsName = this.scanJavaFile(p.toString());
                            if (null != clsName && !clsName.isEmpty()) {
                                foundClasses.add(clsName);
                            }
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return foundClasses;
    }


    /**
     * scans a file to create java class full qualified name
     * if full qaulified name is found => compile class
     *
     * @param fileName the file name (with path) to be scanned
     * @return full qualified java class name
     */
    private String scanJavaFile(String fileName) {

        System.out.println("# SCANNING FILE " + fileName);

        String pkg = null, className = null, fullQName = null;

        // with Stream I should reuse them to search for package and then for class name.
        // because they are designed to be streamed only once I decided to go with dear old Scanner
        try (Scanner scanner = new Scanner(new File(fileName))) {

            // scan file searching for full qualified name
            while (scanner.hasNext()) {
                String l = scanner.nextLine();

                // TODO GESTIRE CASO IN CUI IL PACKAGE NON SIA PRESENTE???

                // search for pkg
                if (null == pkg) {
                    pkg = this.extractPackage(l);
                } else {
                    // search for class' full qualified name
                    className = this.extractClassName(l);
                    if (null != className && !className.isEmpty()) {
                        fullQName = pkg + "." + className;
                        break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new JMCException(e);
        }

        if (null == fullQName) {
            throw new JMCException("Error parsing file: " + fileName);
        }

        this.compileFileList.add(fileName);

        return fullQName;
    }


    /**
     * search for the package instruction into the lthisine that receives as param
     *
     * @param line
     * @return package name or null
     */
    private String extractPackage(String line) {
        return null != line && line.startsWith("package") ? line.replace("package ", "").replace(";", "") : null;
    }


    /**
     * search for the class name instruction into the line that receives as param
     *
     * @param line
     * @return class name or null
     */
    private String extractClassName(@Nullable String line) {

        String clsName = null;

        if (null != line && (line.contains("class") || line.contains("enum"))) {

            String[] tokens = line.split("class|enum");
            if (null != tokens && tokens.length >= 2) {
                clsName = tokens[1].replace("{", "").trim();
            }
        }

        return clsName;
    }


    /**
     * compile alla identified java files
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
     *
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
