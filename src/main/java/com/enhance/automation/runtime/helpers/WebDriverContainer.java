package com.enhance.automation.runtime.helpers;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;

/**
 * 
 * Contains the WebDriver objects against the java thread and provides an API to
 * access theWebDriver object in a thread safe manner during parallel execution.
 * 
 * @author cdushmantha
 *
 */
public class WebDriverContainer {

	private static WebDriverContainer instance;

	private Map<Long, WebDriver> driverMap;

	private WebDriverContainer() {
		driverMap = new HashMap<Long, WebDriver>();
	}

	/**
	 * Singleton get Instance.
	 * 
	 * @return instance
	 */
	public static WebDriverContainer getInstance() {
		if (instance == null) {
			instance = new WebDriverContainer();
		}
		return instance;
	}

	/**
	 * Returns the WebDriver object for the current thread.
	 * 
	 * @return WebDriver object for current thread
	 */
	public WebDriver getDriver() {
		return driverMap.get(Thread.currentThread().getId());
	}

	/**
	 * Adds a WebDriver object against the current thread id.
	 * 
	 * @param driver
	 */
	public void setDriver(WebDriver driver) {
		driverMap.put(Thread.currentThread().getId(), driver);
	}
}
