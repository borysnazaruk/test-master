
package com.test.ws.restclient.parser;

public class ResponseError {

    private final String errorCode;
    private final String errorMessage;

    public ResponseError(final String errorCode, final String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }


}
