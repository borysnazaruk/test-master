package com.test.steps;

import com.test.ws.restclient.RestClient;
import net.thucydides.core.annotations.Step;

public class WSUserSteps {

    RestClient restClient;

    @Step
    public void get_request(String endpoint) {
        restClient.getWeatherDetails(endpoint);
    }

    @Step
    public void verify_status_code(int statusCode) {
        restClient.statusCodeVerification(statusCode);
    }

    @Step
    public void verify_weather_response_body(String value) {
        restClient.weatherResponseVerification(value);
    }

    @Step
    public void customer_registration(String endpoint) {
        restClient.customerRegistration(endpoint);
    }

    @Step
    public void  verify_customer_response_body(String value) {
        restClient.customerResponseVerification(value);
    }

}
