package it.caneserpente.javamodelconverter.converter.typescript;

import it.caneserpente.javamodelconverter.AppConfig;
import it.caneserpente.javamodelconverter.converter.base.AClassConverter;
import it.caneserpente.javamodelconverter.model.JMCClass;
import org.apache.commons.text.WordUtils;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class TypescriptClassConverter extends AClassConverter {


    /**
     * if true interfaces are generated instead of classes
     */
    private boolean generateInterfaces;


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
    protected void setTypeToGenerate(JMCClass clz) {
        if (clz.isEnum()) {
            clz.setTypeToGenerate("enum");
        } else {
            clz.setTypeToGenerate(this.generateInterfaces ? "interface" : "class");
        }
    }

    @Override
    protected void setConstructorNeeded(JMCClass clz) {
        clz.setConstructorNeeded(!clz.isEnum() && !this.generateInterfaces);
    }

    @Override
    protected String writeEnumConstants(JMCClass clz) {

        return "\t" + clz.getEnumConstants().stream().
                map(enumConst -> {

                    // get enum constants names
                    String constants = WordUtils.capitalizeFully(enumConst.toString(), new char[]{'_'}).replaceAll("_", "");

                    // if needed add constants values
                    Optional<String> constValOpt = Arrays.asList(enumConst.getClass().getDeclaredFields()).stream()
                            .filter(f -> !f.isEnumConstant() && !f.isSynthetic())
                            .findFirst()
                            .map(f -> this.readEnumConstantsValues(f, enumConst));

                    // concatenates and returns
                    return constants + (constValOpt.isPresent() ? constValOpt.get() : "");
                })
                .collect(Collectors.joining(",\n\t"));
    }


    /**
     * eventually reads enum's field constant value and return it
     * @param enumField enum's field of which get value
     * @param enumConst the enum constant of which invoke enumField getter method
     * @return
     */
    private String readEnumConstantsValues(Field enumField, Object enumConst) {

        String metName = null, val = null;
        Method method;

        try {
            // creates getter name
            metName = "get" + (enumField.getName().substring(0, 1).toUpperCase() + enumField.getName().substring(1));
            // gets getter method
            method = enumConst.getClass().getDeclaredMethod(metName);
            // requests value
            val = (String) method.invoke(enumConst);

            // if string adds quotes
            if (null != val && method.getReturnType().equals(String.class)) {
                val = "\"" + val + "\"";
            }

        } catch (Exception e) {
            System.err.println("ERROR invoking enum's method " + metName);
        } finally {
            return null == val ? null : " = " + val;
        }
    }


    @Override
    protected void writeGeneratedClass(@Nullable JMCClass clz) {

        StringBuilder sb = new StringBuilder();

        // import
        this.writeImports(clz, sb);

        // open class block
        sb.append("export " + clz.getTypeToGenerate() + " " + clz.getConvertedClassName() + " {").append("\n\n");

        // fields or enum contants
        if (clz.isEnum()) {
            sb.append(this.writeEnumConstants(clz)).append("\n");
        } else {
            clz.getFieldList().stream().forEach(f -> sb.append(f.getConvertedFieldStm()));
        }

        // constructor (only if class generation)
        if (clz.isConstructorNeeded()) {

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
