package com.enhance.automation.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.enhance.automation.runtime.proxy.Element;
import com.enhance.automation.runtime.proxy.PageObjectFactory;

/**
 * TradeMe "Motors" section HomePage
 * 
 * @author cdushmantha
 *
 */
public class TradeMeMotorsHomePage {

	public TradeMeMotorsHomePage(WebDriver driver) {
		PageObjectFactory.initElements(driver, this);
	}

	@FindBy(how = How.XPATH, using = "//div[@role='list']/a[@role='listitem']//span[contains(text(),'%s')]")
	public Element tab_vehicleTypeNavigationTab;

	@FindBy(how = How.XPATH, using = "//div[@role='list']/tg-tab[@role='listitem']//tg-tab-heading[contains(text(),'%s')]")
	public Element tab_vehicleStateNavigationTab;

	@FindBy(how = How.XPATH, using = "//input[@placeholder='Search using keywords']")
	public Element text_searchByKeyword;

	@FindBy(how = How.NAME, using = "selectedMake")
	public Element select_searchByMake;

	@FindBy(how = How.NAME, using = "searchParams.model")
	public Element select_searchByModel;

	@FindBy(how = How.CSS, using = "div.tm-motors-search-bar__dropdown-multi-select")
	public Element select_searchByBodyStyle;

	@FindBy(how = How.XPATH, using = "//tg-dropdown-content//span[contains(text(),'%s')]")
	public Element select_searchByBodyStyleOption;

	@FindBy(how = How.XPATH, using = "//button[@type='submit']")
	public Element buttn_search;

}
