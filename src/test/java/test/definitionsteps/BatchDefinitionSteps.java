package test.definitionsteps;

import com.test.steps.BatchSteps;
import net.thucydides.core.annotations.Steps;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import java.io.IOException;

public class BatchDefinitionSteps {


    @Steps
    BatchSteps batch;



    @When("WS-User send GET request to endpoint '$endpoint'")
    public void whenWSUserSendGETRequestToEndpointbatch(String endpoint) {
        batch.get_batch(endpoint);
    }

    @Then("WS-User receive HTTP response with status code '$statusCode'")
    public void thenWSUserReceiveHTTPResponseWithStatusCode200(int statusCode) {
        batch.verify_status_code(statusCode);
    }

    @Then("Response body should not be NULL")
    public void thenInResponseBodyAttributeShouldBeEqualvalue() {
        batch.verify_response_body();
    }

    @When("WS-User send POST request to endpoint '$endpoint'")
    public void whenWSUserSendPOSTRequestToEndpointcustomerorder(String endpoint) {
        batch.post_batch_order(endpoint);
    }

    @Then("Response body should contains status '$status' and order id")
    public void thenInResponseBodyShouldContainsStatusAndOrder(String status) throws IOException {
        batch.verify_post_response_body(status);
    }

}
