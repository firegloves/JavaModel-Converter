package it.caneserpente.javamodelconverter.argsmanagement;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import it.caneserpente.javamodelconverter.ESupportedLanguages;
import it.caneserpente.javamodelconverter.LogMessages;

import java.io.File;
import java.util.Arrays;

public class ArgsDirExistsValidator implements IParameterValidator {

    @Override
    public void validate(String name, String value) throws ParameterException {
        File dir = new File(value);
        if (! dir.exists() || ! dir.isDirectory()) {
            throw new ParameterException(name + " folder at " + value + LogMessages.ERR_SRC_FOLDER);
        }
    }
}
