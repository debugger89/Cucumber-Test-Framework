package com.enhance.automation.stepdefinitons;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

import org.json.JSONArray;
import org.json.JSONObject;

import com.enhance.automation.runtime.enums.LogLevel;
import com.enhance.automation.runtime.helpers.APICommands;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Contains the step definition files for the TradeMe API create listing
 * scenarios
 * 
 * @author cdushmantha
 *
 */
public class TradeMeAPICreateListingStepDefinitions extends APICommands {

	@Given("user is authenticated to add a new listing")
	public void user_is_authenticated_to_add_a_new_listing() {
		// This part is not completed due to the errors in the TradeMe sand box
		// environment user registration issue.

		// commenting out the exception throw to test the next steps

		// throw new io.cucumber.java.PendingException();
	}

	@Given("sends a {string} request to {string} api including {string}, {string}, {string}, {string}, {int}, {int}, {string}, {string}")
	public void sends_a_request_including(String httpMethod, String resourceURL, String category, String title,
			String subtitle, String description, Integer start_price, Integer buy_now_price, String duration,
			String shipping) throws Exception {

		// construct the payload JSON message
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("Category", category);
		jsonObj.put("Title", title);
		jsonObj.put("Subtitle", subtitle);
		jsonObj.put("Description", new JSONArray().put(description));
		jsonObj.put("StartPrice", start_price);
		jsonObj.put("BuyNowPrice", buy_now_price);
		jsonObj.put("Duration", duration);

		log(LogLevel.PASS, "Request payload \n : " + jsonObj.toString(2));

		// execute post request
		executePost(resourceURL + ".json", jsonObj);

	}

	@Then("the response will contain {string}")
	public void the_response_will_contain(String description) {
		response.then().body("Description", hasItem(description));
		log(LogLevel.PASS, "Respose json array contains the expected Description : " + description);
	}

	@When("user sends a GET request to {string} to retrieve the added listing")
	public void user_sends_a_request_to_retrieve_the_added_listing(String resourceURL) throws Exception {
		// get listing ID from first response
		String listingID = response.then().extract().path("ListingId");

		String constructedURL = resourceURL + "/" + listingID + ".json";
		executeGet(constructedURL);
		log(LogLevel.PASS, "Request for retrieve listing passed to : " + constructedURL);

	}

	@Then("the response will contain the {string}, {string}, {string}, {string}, {int}, {int}, {string}, {string}")
	public void the_response_will_contain_the(String category, String title, String subtitle, String description,
			Integer start_price, Integer buy_now_price, String duration, String shipping) {

		// Assert the response Category is equal to expected
		response.then().body("Category", equalTo(category));

		// Assert the response Title is equal to expected
		response.then().body("Title", equalTo(title));

		// Assert the response Description is equal to expected
		response.then().body("Description", hasItem(description));

		// Assert the response StartPrice is equal to expected
		response.then().body("StartPrice", equalTo(start_price));

		// Assert the response BuyNowPrice is equal to expected
		response.then().body("BuyNowPrice", equalTo(buy_now_price));

		// Assert the response Duration is equal to expected
		response.then().body("Duration", equalTo(duration));

		// Assert the response ShippingOptions is equal to expected
		response.then().body("ShippingOptions", hasItem(shipping));
	}
}
