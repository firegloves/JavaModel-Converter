/**
 * scans directory to load all present .java classes
 * parses all full qualified present classes's names and save it into a list
 */

package it.caneserpente.javamodelconverter;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClassListScanner {

    // TODO pass to relative path
    private String inputDirName = "resources/";
    private String compiledDirName = "resources/compiled";

    private File inputDir;
    private File compiledDir;


    public ClassListScanner(String inputDirName, String compiledDirName) {

        // input dir
        if (null != inputDirName && !inputDirName.isEmpty()) {
            this.inputDirName = inputDirName;
        }
        this.inputDir = new File(this.inputDirName);
        if (!this.inputDir.exists() || !this.inputDir.isDirectory()) {
            throw new RuntimeException("Input Dir " + this.inputDir.getAbsolutePath() + " does not exists or it is not a directory. Otherwise check for permissions");
        }

        // compiled dir
        if (null != compiledDirName && !compiledDirName.isEmpty()) {
            this.compiledDirName = compiledDirName;
        }
        this.compiledDir = new File(this.compiledDirName);
        if (!this.compiledDir.exists() || !this.compiledDir.isDirectory()) {
            throw new RuntimeException("Compiled Dir " + this.compiledDir.getAbsolutePath() + " does not exists or it is not a directory. Otherwise check for permissions");
        }
    }


    /**
     * scans inputDir to load all present .java classes
     * for each java class construct its full qualified name and add it to the list to return
     *
     * @return the list of full qualified names to return
     */
    public List<String> scanForClasses() {

        List<String> classNameList = new ArrayList<>();

        String[] fileList = inputDir.list();
        for (int i = 0; i < fileList.length; i++) {

            String clsName = this.scanJavaFile(fileList[i]);
            if (null != clsName && !clsName.isEmpty()) {
                classNameList.add(clsName);
            }
        }

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

            // compile class
            if (null != retVal && !retVal.isEmpty()) {
                this.compileClass(fileName);
            }

            return retVal;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * search for the package instruction into the line that receives as param
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
     * compile java file received as param
     *
     * @param fileName
     */
    private void compileClass(@NotNull String fileName) {

        // Compile source file.
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        // javac does not permit to choose output folder without creating all package hierarchy
        // so i choose to craete .class in the same folder of the source file and then to copy in che compiled folder
        compiler.run(null, null, null,
                "-d", this.compiledDir.getAbsolutePath(), this.inputDir.getAbsolutePath() + System.getProperty("file.separator") + fileName);

//        compiler.run(null, null, null, this.inputDir.getAbsolutePath() + System.getProperty("file.separator") + fileName);
//
//        File compFile = new File(this.inputDir.getAbsolutePath() + System.getProperty("file.separator") + fileName.replace(".java", ".class"));
//        if (null != compFile && compFile.exists()) {
//            compFile.renameTo(new File(this.compiledDir.getAbsolutePath() + System.getProperty("file.separator") + fileName.replace(".java", ".class")));
//        }
    }
}
