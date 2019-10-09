package com.test.steps;


import com.test.ws.restclient.BatchRestClient;
import com.test.ws.restclient.RestClient;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import net.thucydides.core.annotations.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class BatchSteps {
    private static Logger LOG = LoggerFactory.getLogger(BatchSteps.class.getSimpleName());
    BatchRestClient batchRestClient;

    @Step
    public void get_batch(String endpoint) {
        batchRestClient.getBatch(endpoint);
    }

    @Step
    public void verify_response_body() {
        batchRestClient.verifyResponseBody();
    }

    @Step
    public void verify_status_code(int statusCode) {
        batchRestClient.statusCodeVerification(statusCode);
    }

    @Step
    public void post_batch_order(String endpoint) {
        batchRestClient.postBatchOrder(endpoint);
    }

    @Step
    public void verify_post_response_body(String status) throws IOException {
        batchRestClient.verifyPostBatchBody(status);
    }
}
