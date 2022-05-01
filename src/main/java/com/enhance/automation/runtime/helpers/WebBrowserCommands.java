package com.enhance.automation.runtime.helpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import com.enhance.automation.runtime.enums.LogLevel;
import com.enhance.automation.runtime.reports.ExtentReporterImpl;
import com.enhance.automation.runtime.reports.IResultReporter;

/**
 * 
 * Contains Selenium browser related command wrappers
 * 
 * @author cdushmantha
 *
 */
public class WebBrowserCommands {

	protected WebDriver driver;
	private IResultReporter reporter;
	private static final Logger LOGGER = LogManager.getLogger();

	public WebBrowserCommands(WebDriver driver) {
		this.driver = driver;
		reporter = ExtentReporterImpl.getInstance();
	}

	/**
	 * Navigates to the provided URL in the broswer
	 * 
	 * @param url The URL to be navigated to
	 */
	public void goTo(String url) {

		if (url == null || url.isBlank()) {
			throw new WebDriverException("The goTo URL cannot be blank");
		}

		try {
			driver.get(url);
			LOGGER.info("Navigated to webpage : " + url);
			reporter.log(LogLevel.PASS, "Navigated to webpage : " + url);
		} catch (Exception e) {
			LOGGER.error("Page navigation failed due to error : " + e.getMessage(), e);
			throw e;
		}

	}
}
