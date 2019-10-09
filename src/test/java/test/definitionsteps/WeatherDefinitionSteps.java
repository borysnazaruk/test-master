package test.definitionsteps;

import com.test.steps.WSUserSteps;
import net.thucydides.core.annotations.Steps;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

public class WeatherDefinitionSteps {

    @Steps
    WSUserSteps wsUser;

    @When("WS-User send GET request to endpoint '<city>'")
    public void whenWSUserSendGETRequestToEndpointcity(String city) {
        wsUser.get_request(city);
    }

    @Then("WS-User receive HTTP response with status code '<statusCode>'")
    public void thenWSUserReceiveHTTPResponseWithStatusCodestatusCode(int statusCode) {
        wsUser.verify_status_code(statusCode);
    }

    @Then("in Response body attribute city should be equal '<value>'")
    public void thenInResponseBodyAttributeCityShouldBeEqualvalue(String value) {
        wsUser.verify_weather_response_body(value);
    }

    @When("WS-User send POST request to endpoint '$register'")
    public void whenWSUserSendPOSTRequestToEndpointregister(String endpoint) {
        wsUser.customer_registration(endpoint);
    }

    @Then("Response body should contains '<value>'")
    public void thenResponseBodyShouldContainsvalue(String value) {
        wsUser.verify_customer_response_body(value);
    }




}
