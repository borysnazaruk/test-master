package com.test.ws.restclient;

import com.test.common.dto.PostBatchResponseDTO;
import com.test.engine.httpclient.HttpRequest;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.RequestSpecification;
import net.thucydides.core.pages.PageObject;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


public class BatchRestClient extends PageObject implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger("HttpClient");
    private final String bsTangerineBaseSecure = System.getProperty("bs.tangerine.rest.secure");
    private final String bsTangerineRestVersion= System.getProperty("bs.tangerine.rest.version");
    private final String locationId = System.getProperty("locationId");
    private final String BATCH_URL = "%s/%s/%s";
    private static ResponseOptions<io.restassured.response.Response> response;
    private static RequestSpecification request;
    private static JSONObject requestParams = new JSONObject();




    public void getBatch(String endpoint) {
        RestAssured.replaceFiltersWith(ResponseLoggingFilter.responseLogger(), new RequestLoggingFilter());
        String restUrl = String.format(BATCH_URL+endpoint, bsTangerineBaseSecure, bsTangerineRestVersion,
                locationId);
        response = RestAssured.given().get(restUrl);
    }

    public void statusCodeVerification(int statusCode) {
        assertThat(response.getStatusCode()).as("wrong status code").isEqualTo(statusCode);
    }

    public void verifyResponseBody() {
        assertThat(response.getBody().toString().contains("2019190421")).as("body is empty");
    }

    public void postBatchOrder(String endpoint) {
        String restUrl = String.format(BATCH_URL, bsTangerineBaseSecure, bsTangerineRestVersion, endpoint);
        request = RestAssured.given();
        request.header("Bearer Token", HttpRequest.ContentType.BEARER_TOKEN.toString());
        request.contentType(HttpRequest.ContentType.APPLICATION_JSON.toString());
        request.body(body().toJSONString());
        response = request.post(restUrl);

    }

    public void verifyPostBatchBody(String status) throws IOException {
        PostBatchResponseDTO responseBody = response.getBody().as(PostBatchResponseDTO.class);
        assertThat(responseBody.getStatus()).as("wrong order status").isEqualTo(status);
        assertThat(responseBody.getOrder()).as("wrong order id").isEqualTo(requestParams.get("id"));
    }

    public static String setDate() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return dateTime.format(formatter);
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public JSONObject body() {
        requestParams.put("cutoff_time", setDate());
        requestParams.put("id", String.valueOf(getRandomNumberInRange(2019100200, 2019999999)));
        requestParams.put("location_id", locationId);
        HashMap<String, Serializable> item1 = new HashMap<>();
        item1.put("id", "00041196415063");
        item1.put("qty", 1);
        HashMap<String, Serializable> item2 = new HashMap<>();
        item2.put("id", "00039978002228");
        item2.put("qty", 1);
        ArrayList<HashMap<String, Serializable>> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        requestParams.put("items", items);
        return requestParams;
    }

}
