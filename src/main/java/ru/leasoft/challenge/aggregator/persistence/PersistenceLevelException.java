package ru.leasoft.challenge.aggregator.persistence;

public class PersistenceLevelException extends RuntimeException {

    public PersistenceLevelException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return "Persistence level exception occurred. Underlying exception: " + getCause().getMessage();
    }
}
