package com.enhance.automation.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.enhance.automation.runtime.proxy.Element;
import com.enhance.automation.runtime.proxy.PageObjectFactory;

/**
 * TradeMe web application HomePage
 * 
 * @author cdushmantha
 *
 */
public class TradeMeHomePage {

	public TradeMeHomePage(WebDriver driver) {
		PageObjectFactory.initElements(driver, this);
	}

	@FindBy(how = How.XPATH, using = "//nav[@aria-label='Search and Verticals']//a[contains(text(),'%s')]")
	public Element tab_navigationTab;
}
