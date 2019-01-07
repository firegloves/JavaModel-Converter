package it.caneserpente.javamodelconverter.exception;

public class JMCException extends RuntimeException {

    public JMCException() {
    }

    public JMCException(String message) {
        super(message);
    }

    public JMCException(String message, Throwable cause) {
        super(message, cause);
    }

    public JMCException(Throwable cause) {
        super(cause);
    }

    public JMCException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
