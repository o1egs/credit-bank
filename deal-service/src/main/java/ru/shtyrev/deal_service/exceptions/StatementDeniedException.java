package ru.shtyrev.deal_service.exceptions;

public class StatementDeniedException extends Exception {
    public StatementDeniedException() {
        super();
    }

    public StatementDeniedException(String message) {
        super(message);
    }

    public StatementDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public StatementDeniedException(Throwable cause) {
        super(cause);
    }

    protected StatementDeniedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
