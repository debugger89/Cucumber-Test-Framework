package com.enhance.automation.runtime.proxy;

import org.openqa.selenium.WebElement;

/**
 * Proxy Element class extending the Selenium WebElement 
 * @author cdushmantha
 *
 */
public interface Element extends WebElement {

	Element withParams(String... locatorParams);

	void select(String text);

	void waitUntilPresent(int timeOutInSeconds);
}