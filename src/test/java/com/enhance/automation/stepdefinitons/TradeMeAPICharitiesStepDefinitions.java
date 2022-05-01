package com.enhance.automation.stepdefinitons;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.enhance.automation.runtime.enums.LogLevel;
import com.enhance.automation.runtime.helpers.APICommands;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

/**
 * Contains the step definition files for the TradeMe API scenarios
 * 
 * @author cdushmantha
 *
 */
public class TradeMeAPICharitiesStepDefinitions extends APICommands {

	@Given("user retrieves a list of {string}")
	public void user_retrieves_a_list_of(String catalogueType) throws Exception {

		// construct the resource URL based on the parameter
		String resourceURL = catalogueType + ".json";
		// execute the GET request
		executeGet(resourceURL);
		// Print the successful response as a pretty string to report & log
		log(LogLevel.PASS, response.getBody().asPrettyString());
	}

	@Then("the response will contain a list of {string}")
	public void the_response_will_contain_a_list_of(String catalogueType) {

		// Get the array size of the response
		int responseArrSize = getResponseArraySize("");
		// assert the response array size is greater than 0
		assertEquals(true, responseArrSize > 0, "The response charities array is empty");
	}

	@Then("the response will contain {string} charity")
	public void the_response_will_contain_charity_with_tagline(String charityName) {

		// Assert the response array has a charity item with the given Description
		response.then().body("Description", hasItem(charityName));
		log(LogLevel.PASS, "Respose json array contains the expected item : " + charityName);
	}
}
