package com.test.engine.exception;

public class IncompatibleDataException extends ExtendedException {
    public IncompatibleDataException() {

    }

    public IncompatibleDataException(final String message) {
        super(message);

    }

    public IncompatibleDataException(final Throwable cause) {
        super(cause);

    }

    public IncompatibleDataException(final String message, final Throwable cause) {
        super(message, cause);

    }

    public IncompatibleDataException(final IncompatibleDataError error) {
        super(error.toString());

    }

    public IncompatibleDataException(final String msg, final String... args) {
        super(String.format(msg, args));
    }

    public enum IncompatibleDataError {
        INCORRECT_PARAMETER("Proper parameter should be used for this step");
        private final String message;

        IncompatibleDataError(final String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }
    }
}
