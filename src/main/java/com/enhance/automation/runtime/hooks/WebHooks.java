package com.enhance.automation.runtime.hooks;

import java.io.IOException;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.enhance.automation.runtime.enums.Browsers;
import com.enhance.automation.runtime.enums.LogLevel;
import com.enhance.automation.runtime.exceptions.UnsupportedBrowserException;
import com.enhance.automation.runtime.helpers.WebDriverContainer;
import com.enhance.automation.runtime.reports.ExtentReporterImpl;
import com.enhance.automation.runtime.reports.IResultReporter;
import com.enhance.automation.runtime.utils.Constants;
import com.enhance.automation.runtime.utils.CucumberReflectionUtils;
import com.enhance.automation.runtime.utils.PrimitiveConverter;
import com.enhance.automation.runtime.utils.PropertyReader;
import com.enhance.automation.runtime.utils.ScreenshotUtils;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * 
 * Contains Web related cucumber hooks
 * 
 * @author cdushmantha
 * 
 */
public class WebHooks {

	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * Cucumber hook @Before. Will start the browser session with Selenium for the
	 * scenario.
	 * 
	 * @param scenario
	 */
	@Before(order = 1, value = "@web and not @api")
	public void beforeScenario(Scenario scenario) {

		Browsers browser = getBrowserForExecution();
		WebDriver driver = createLocalWebdriver(browser);
		WebDriverContainer.getInstance().setDriver(driver);

		startOfBrowser(driver);

	}

	/**
	 * Cucumber hook @After. Kills the browser session after the scenario is
	 * executed.
	 * 
	 * @param scenario
	 */
	@After(order = 1, value = "@web and not @api")
	public void afterScenario(Scenario scenario) {
		WebDriver driver = WebDriverContainer.getInstance().getDriver();
		try {
			if (driver == null) {
				LOGGER.info("Current driver session is null. Skipped the driver.quit()");
			} else {
				LOGGER.info("Ending the browser session with close() and quit()");
				driver.close();
				driver.quit();
			}
		} catch (Exception e) {
			LOGGER.error("Error occured during browser quit : " + e.getMessage(), e);
		}
	}

	/**
	 * Cucumber hook @AfterStep. Records the test step statuses for Extend reporting
	 * purposes
	 * 
	 * @param scenario
	 * @throws IOException
	 */
	@AfterStep(value = "@web and not @api")
	public void afterStep(Scenario scenario) throws IOException {
		if (scenario.isFailed()) {
			IResultReporter reporter = ExtentReporterImpl.getInstance();
			String screenshotPath = ScreenshotUtils.takeWebOrNativeScreenshot(
					WebDriverContainer.getInstance().getDriver(), reporter.getReportFolder().getCanonicalPath());
			Throwable e = getExecutionError(scenario);
			LOGGER.error(e.getMessage(), e);
			reporter.log(LogLevel.FAIL, getExecutionError(scenario).getMessage(), screenshotPath, e);
		}
	}

	/**
	 * Performs startup procedures for the new browser session. Ex : Maximize,
	 * timeout set
	 * 
	 * @param driver
	 */
	private void startOfBrowser(WebDriver driver) {
		driver.manage().window().maximize();

		PropertyReader propReader = new PropertyReader(Constants.LOCAL_CONFIG_FILENAME);

		// retrieve the browser's configuration params from the property file.
		int implicitWait = PrimitiveConverter.parse(Integer.class,
				propReader.getProperty(Constants.PARAM_NAME_IMPLICITWAIT));
		int pageLoadTimeout = PrimitiveConverter.parse(Integer.class,
				propReader.getProperty(Constants.PARAM_NAME_PAGELOADTIMEOUT));

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		LOGGER.info("Session implicitWaitTime is set to : " + implicitWait + " seconds");
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
		LOGGER.info("Session pageLoadTimeout is set to : " + pageLoadTimeout + " seconds");
	}

	/**
	 * Read the local property file and get the browser name for execution.
	 * 
	 * @return browser
	 */
	private Browsers getBrowserForExecution() {
		PropertyReader propReader = new PropertyReader(Constants.LOCAL_CONFIG_FILENAME);
		String browserString = propReader.getProperty(Constants.PARAM_NAME_BROWSER);
		return Browsers.valueOf(browserString);
	}

	/**
	 * Creates a local webdriver session for the given browser type.
	 * 
	 * @param browser
	 * @return
	 */
	private WebDriver createLocalWebdriver(Browsers browser) {
		switch (browser) {
		case CHROME: {
			LOGGER.info("Starting up a Chrome browser session. Driver server managed by WebDriverManager");
			WebDriverManager.chromedriver().setup();
			LOGGER.info("WebDriver driver server setup completed");
			WebDriver driver = new ChromeDriver();
			LOGGER.info("Started up a new ChromeDriver");
			return driver;

		}
		case FIREFOX: {
			LOGGER.info("Starting up a Firefox browser session. Driver server managed by WebDriverManager");
			WebDriverManager.firefoxdriver().setup();
			LOGGER.info("WebDriver driver server setup completed");
			WebDriver driver = new FirefoxDriver();
			LOGGER.info("Started up a new FirefoxDriver");
			return driver;
		}
		case SAFARI: {
			LOGGER.info("Starting up a Safari browser session. Driver server managed by WebDriverManager");
			WebDriverManager.safaridriver().setup();
			LOGGER.info("WebDriver driver server setup completed");
			WebDriver driver = new SafariDriver();
			LOGGER.info("Started up a new SafariDriver");
			return driver;
		}
		default: {
			throw new UnsupportedBrowserException("Invalid browser type used. Supports : CHROME, FIREFOX, SAFARI");
		}
		}

	}

	private Throwable getExecutionError(Scenario scenario) {
		return CucumberReflectionUtils.getExecutionError(scenario);
	}

}
