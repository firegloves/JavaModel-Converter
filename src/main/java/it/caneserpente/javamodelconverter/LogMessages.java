package it.caneserpente.javamodelconverter;

import java.util.Arrays;
import java.util.stream.Collectors;

public class LogMessages {

    public static final String ERR_UNSUPPORTED_LANG = "Transpiling language unsupported or not recognized. Supported languages are: " + Arrays.stream(ESupportedLanguages.values()).map(Enum::name).collect(Collectors.joining(", "));
    public static final String ERR_SRC_FOLDER = " does not exists or it is not a directory. Otherwise check for permissions";
    public static final String ERR_NO_FILES_TO_TRANSPILE = "Can't find any java files into ";
}
