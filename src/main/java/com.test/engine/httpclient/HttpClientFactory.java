package com.test.engine.httpclient;

import org.apache.http.client.HttpClient;

public abstract class HttpClientFactory {

    /** Main instance for testing */
    public static synchronized HttpClient getDefaultSecureClient() {
        return DefaultSecureHttpClient.getSecureClient();
    }

}
