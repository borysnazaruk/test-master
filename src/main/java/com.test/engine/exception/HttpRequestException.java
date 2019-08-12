package com.test.engine.exception;

import java.util.List;
import java.util.stream.Collectors;

public class HttpRequestException extends ExtendedException {
    public HttpRequestException() {

    }

    public HttpRequestException(final int expectedCode, final int errorCode,
                                final String errorDescription) {
        super(String.format("Expected status code: %s, but actual is %s. Response error message: [%s]",
                expectedCode, errorCode, errorDescription));
    }

    public HttpRequestException(final List<Integer> expectedCodes, final int errorCode,
                                final String errorDescription) {
        super(String.format(
                "Expected status code: %s, but actual is %s. Response error message: [%s]", expectedCodes
                        .stream().map(code -> String.valueOf(code)).collect(Collectors.joining(",")),
                errorCode, errorDescription));

    }

    public HttpRequestException(final String message) {
        super(message);

    }

    public HttpRequestException(final Throwable cause) {
        super(cause);

    }

    public HttpRequestException(final String message, final Throwable cause) {
        super(message, cause);

    }

    public HttpRequestException(final String msg, final String... args) {
        super(String.format(msg, args));
    }
}
