package com.enhance.automation.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.enhance.automation.runtime.proxy.Element;
import com.enhance.automation.runtime.proxy.PageObjectFactory;

/**
 * TradeMe web application 'Motors' search result page
 * 
 * @author cdushmantha
 *
 */
public class TradeMeSearchResultPage {

	public TradeMeSearchResultPage(WebDriver driver) {
		PageObjectFactory.initElements(driver, this);
	}

	@FindBy(how = How.XPATH, using = "//div[@tmid='title']")
	public Element link_firstSearchResult;

	@FindBy(how = How.CSS, using = "h1.tm-motors-listing__title")
	public Element text_resultHeader;

	@FindBy(how = How.XPATH, using = "//tg-icon[@name='vehicle-odometer']/../..")
	public Element text_odometer;

	@FindBy(how = How.XPATH, using = "//tg-icon[@name='vehicle-car']/../..")
	public Element text_bodyType;

	@FindBy(how = How.XPATH, using = "//tg-icon[@name='vehicle-seat']/../..")
	public Element text_seats;

	@FindBy(how = How.XPATH, using = "//span[contains(text(),'Number plate')]/..")
	public Element text_numberPlate;

}
