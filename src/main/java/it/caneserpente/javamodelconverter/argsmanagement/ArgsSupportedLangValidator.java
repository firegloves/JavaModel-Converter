package it.caneserpente.javamodelconverter.argsmanagement;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import it.caneserpente.javamodelconverter.ESupportedLanguages;
import it.caneserpente.javamodelconverter.LogMessages;

import java.util.Arrays;

public class ArgsSupportedLangValidator implements IParameterValidator {

    @Override
    public void validate(String name, String value) throws ParameterException {
        if (! Arrays.stream(ESupportedLanguages.values()).anyMatch((t) -> t.name().equals(value.toUpperCase()))) {
            throw new ParameterException(LogMessages.ERR_UNSUPPORTED_LANG);
        }
    }
}
