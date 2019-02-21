package it.caneserpente.javamodelconverter;

import com.beust.jcommander.JCommander;
import it.caneserpente.javamodelconverter.argsmanagement.ArgsConfig;
import it.caneserpente.javamodelconverter.builder.ClassConverterDirector;
import it.caneserpente.javamodelconverter.converter.base.AClassConverter;

import java.util.Arrays;
import java.util.List;

public class JavaModelConverter {

    public static final boolean DEBUG = true;

    /**
     * app entry point
     *
     * @param args
     */
    public static void main(String[] args) {

        // reads cmd line args and configs application
        readArgs(args);

//        testEnumReflection();

        // scans directory for .java files and build it
        List<String> classList = new ClassListScanner().scanForClasses();

        if (DEBUG) {
            classList.stream().forEach(c -> System.out.println(c));
        }

        // create desired class converter
        AClassConverter classConverter = new ClassConverterDirector(classList).construct();

        // convert classes
        classConverter.convertClassList(classList);
    }

    /**
     * read args from cmd line or from application.properties
     *
     * @param args
     */
    private static void readArgs(String[] args) {

        // parse cmd line parameters
        ArgsConfig argsConfig = new ArgsConfig();
        JCommander.newBuilder()
                .addObject(argsConfig)
                .build()
                .parse(args);

        // init AppConfig
        AppConfig appConfig = AppConfig.getInstance();

        // target lang
        if (null != argsConfig.getLang()) {
            appConfig.setTargetLanguage(argsConfig.getLang().name());
        }

        // src dir
        if (null != argsConfig.getSrcFolder()) {
            appConfig.setTargetJavaClassesDir(argsConfig.getSrcFolder());
        }

        // compiled dir
        if (null != argsConfig.getCompiledFolder()) {
            appConfig.setTargetCompiledDir(argsConfig.getCompiledFolder());
        }

        // generated dir
        if (null != argsConfig.getGeneratedFolder()) {
            appConfig.setTargetOutputDir(argsConfig.getGeneratedFolder());
        }
    }


    private static void testEnumReflection() {
        try {

//            https://stackoverflow.com/questions/24260011/get-value-of-enum-by-reflection

            Class<?> superH = Class.forName("it.caneserpente.javamodelconverter.ESuperHeroe");
            System.out.println("SUPERHEROES: " + Arrays.asList(superH.getEnumConstants()));

            Class<?> superP = Class.forName("it.caneserpente.javamodelconverter.ESuperPower");
            System.out.println("SUPERPOWERS: " + Arrays.asList(superP.getEnumConstants()));

//            Arrays.asList(superH.getEnumConstants()).stream().
//                    forEach(ec -> {
//                        Class clz = ec.getClass();
//                        Arrays.asList(clz.getDeclaredFields()).stream()
//                                .filter(f -> ! f.isEnumConstant() && ! f.isSynthetic())
//                                .forEach(f -> {
//
//                                    try {
//                                        Class<?> clzz = ec.getClass();
//                                        Method method = clzz.getDeclaredMethod("get" + (f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1)));
//                                        String val = (String) method.invoke(ec);
//                                        System.out.println("value : " + val);
//
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                });
//                    });


//            Field[] flds = superP.getDeclaredFields();
//            List<Field> cst = new ArrayList<Field>();  // enum constants
//            List<Field> mbr = new ArrayList<Field>();  // member fields
//            for (Field f : flds) {
//                if (f.isEnumConstant())
//                    cst.add(f);
//                else
//                    mbr.add(f);
//            }
//            if (!cst.isEmpty())
//                System.out.println(cst);
//            if (!mbr.isEmpty())
//                System.out.println(mbr);

//            Constructor[] ctors = superP.getDeclaredConstructors();
//            for (Constructor ctor : ctors) {
//                out.format(fmt, "Constructor", ctor.toGenericString(),
//                        synthetic(ctor));
//            }

//            Method[] mths = c.getDeclaredMethods();
//            for (Method m : mths) {
//                out.format(fmt, "Method", m.toGenericString(),
//                        synthetic(m));
//            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
