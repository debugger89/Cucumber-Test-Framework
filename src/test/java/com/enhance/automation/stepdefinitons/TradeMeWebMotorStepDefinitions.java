package com.enhance.automation.stepdefinitons;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.openqa.selenium.WebDriver;

import com.enhance.automation.pageobjects.TradeMeHomePage;
import com.enhance.automation.pageobjects.TradeMeMotorsHomePage;
import com.enhance.automation.pageobjects.TradeMeSearchResultPage;
import com.enhance.automation.runtime.helpers.WebBrowserCommands;
import com.enhance.automation.runtime.helpers.WebDriverContainer;
import com.enhance.automation.runtime.utils.Constants;
import com.enhance.automation.runtime.utils.PropertyReader;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

/**
 * Contains the step definition files for the TradeMe WEB scenarios
 * 
 * @author cdushmantha
 *
 */
public class TradeMeWebMotorStepDefinitions extends WebBrowserCommands {

	public TradeMeWebMotorStepDefinitions() {
		super(WebDriverContainer.getInstance().getDriver());
	}

	@Given("User is on the home page")
	public void userOnHomePage() {

		// read the local property file for the base URL of the SUT
		PropertyReader propReader = new PropertyReader(Constants.LOCAL_CONFIG_FILENAME);
		String weburl = propReader.getProperty(Constants.PARAM_NAME_BASEURL_WEB);
		// navigate to the base web URL in the browser
		goTo(weburl);

	}

	@Given("navigated to the {string} tab")
	public void navigated_to_the_tab(String tabName) {

		WebDriver driver = WebDriverContainer.getInstance().getDriver();

		TradeMeHomePage home = new TradeMeHomePage(driver);
		// click to the "Motors" tab in the home page
		home.tab_navigationTab.withParams(tabName).click();
	}

	@Then("the user navigates to {string} tab and selects {string} option")
	public void the_user_navigates_to_tab_and_selects_option(String vehicleType, String vehicleState) {
		WebDriver driver = WebDriverContainer.getInstance().getDriver();

		TradeMeMotorsHomePage motorHome = new TradeMeMotorsHomePage(driver);
		// navigate to the "Cars" tab
		motorHome.tab_vehicleTypeNavigationTab.withParams(vehicleType).click();
		// navigate to the "Used" tab under Cars
		motorHome.tab_vehicleStateNavigationTab.withParams(vehicleState).click();

	}

	@Then("user provides {string}, {string}, {string}, {string}")
	public void user_provides_selection_criteria(String keyword, String make, String model, String body) {
		WebDriver driver = WebDriverContainer.getInstance().getDriver();

		TradeMeMotorsHomePage motorHome = new TradeMeMotorsHomePage(driver);
		// Enter the keyword, make, model and body type
		motorHome.text_searchByKeyword.sendKeys(keyword);
		motorHome.select_searchByMake.select(make);
		motorHome.select_searchByModel.select(model);
		// expand the body type drop down
		motorHome.select_searchByBodyStyle.click();
		// check the body type option
		motorHome.select_searchByBodyStyleOption.withParams(body).click();
	}

	@Then("clicks on Search button")
	public void clicks_on_search_button() {
		WebDriver driver = WebDriverContainer.getInstance().getDriver();

		TradeMeMotorsHomePage motorHome = new TradeMeMotorsHomePage(driver);
		// click on search button
		motorHome.buttn_search.click();
	}

	@Then("user click on the first seach result")
	public void user_click_on_the_first_seach_result() {
		WebDriver driver = WebDriverContainer.getInstance().getDriver();

		TradeMeSearchResultPage results = new TradeMeSearchResultPage(driver);
		// click on the first search result found in the results page
		results.link_firstSearchResult.click();

	}

	@Then("verify the {string}, {string}, {string}, {string}")
	public void verify_the_vloger_sedan(String numberPlate, String bodyStyle, String kilometers, String seats) {
		WebDriver driver = WebDriverContainer.getInstance().getDriver();

		TradeMeSearchResultPage results = new TradeMeSearchResultPage(driver);

		// wait until the header is loaded for 10 seconds
		results.text_resultHeader.waitUntilPresent(10);

		// Assert actual number plate badge element's text
		assertEquals(true, results.text_numberPlate.getText().trim().endsWith(numberPlate),
				"The actual number plate is not equal to expected : " + numberPlate);

		// Assert actual body type badge element's text
		assertEquals(true, results.text_bodyType.getText().trim().endsWith(bodyStyle),
				"The actual body type is not equal to expected : " + bodyStyle);

		// Assert actual odometer badge element's text
		assertEquals(true, results.text_odometer.getText().trim().endsWith(kilometers),
				"The actual odometer is not equal to expected : " + kilometers);

		// Assert actual seats badge element's value
		assertEquals(true, results.text_seats.getText().trim().endsWith(seats),
				"The actual seats count is not equal to expected : " + seats);

	}

}
