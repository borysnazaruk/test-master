package com.test.engine.exception;

public class FileOperationsException extends ExtendedException {

    public FileOperationsException() {

    }

    public FileOperationsException(final String message) {
        super(message);

    }

    public FileOperationsException(final Throwable cause) {
        super(cause);

    }

    public FileOperationsException(final String message, final Throwable cause) {
        super(message, cause);

    }

    public FileOperationsException(final String msg, final String... args) {
        super(String.format(msg, args));
    }
}
