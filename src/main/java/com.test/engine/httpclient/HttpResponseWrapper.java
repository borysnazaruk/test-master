package com.test.engine.httpclient;

import com.test.engine.exception.HttpRequestException;
import org.apache.commons.lang3.CharEncoding;
import org.apache.http.HttpResponse;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HttpResponseWrapper {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger("HttpClient");
    private final int statusCode;
    private String body;
    private String bodyEncoded;
    private HttpResponse rawResponse;
    private byte[] fileEntity;

    public HttpResponseWrapper(final HttpResponse httpResponse) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        rawResponse = httpResponse;
        statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == 204) {
            bodyEncoded = null;
        } else {
            try {
                httpResponse.getEntity().writeTo(byteArrayOutputStream);
                fileEntity = byteArrayOutputStream.toByteArray();
                bodyEncoded = new String(fileEntity, CharEncoding.UTF_8);
                body = new String(bodyEncoded.getBytes(CharEncoding.UTF_8));
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
                throw new HttpRequestException(e.getMessage());
            }
        }
    }

    public byte[] getFileEntity() {
        return fileEntity;
    }

    public String getBody() {
        return body;
    }

    public String getBodyEncoded() {
        return bodyEncoded;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public HttpResponse getRawResponse() {
        return rawResponse;
    }
}
