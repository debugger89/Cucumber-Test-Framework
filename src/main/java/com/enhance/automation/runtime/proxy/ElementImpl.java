package com.enhance.automation.runtime.proxy;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enhance.automation.runtime.enums.LogLevel;
import com.enhance.automation.runtime.exceptions.NotYetImplementedException;
import com.enhance.automation.runtime.reports.ExtentReporterImpl;
import com.enhance.automation.runtime.reports.IResultReporter;
import com.enhance.automation.runtime.utils.Constants;
import com.enhance.automation.runtime.utils.PrimitiveConverter;
import com.enhance.automation.runtime.utils.PropertyReader;

/**
 * Implementing class for the {@link Element} interface. Contains the
 * implemented concrete methods of the custom {@link Element} interface and
 * {@link Weblement} interface.
 * 
 * @author cdushmantha
 *
 */
public class ElementImpl implements Element {

	private static final Logger LOGGER = LogManager.getLogger();

	private WebDriver driver;
	private FindBy findBy;
	private Object[] locatorParams;
	private By byLocator;
	private int implicitWait;
	private IResultReporter reporter;

	public ElementImpl(WebDriver driver, FindBy findBy) {
		this.driver = driver;
		this.findBy = findBy;
		this.implicitWait = getImplicitWaitforSession();
		this.reporter = ExtentReporterImpl.getInstance();
	}

	/**
	 * Accepts a varargs parameter and applies the parameters for the element's
	 * locator parameters.
	 */
	@Override
	public Element withParams(String... locatorParams) {
		this.locatorParams = locatorParams;
		return this;
	}

	/**
	 * Provides a wrapper for the native Selenium {@link WebElement} click()
	 * command. Performs a click action for the WebElement
	 */
	@Override
	public void click() {
		try {
			WebElement element = doFindElement();

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(implicitWait));
			wait.until(ExpectedConditions.elementToBeClickable(element));

			element.click();
			// Action passed. Report now!

			String successMessage = "Click command passed | Element : " + byLocator;
			LOGGER.info(successMessage);
			reporter.log(LogLevel.PASS, successMessage);
		} catch (Exception e) {
			LOGGER.error("Click command failed. Error : " + e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Provides a wrapper for the native Selenium {@link WebElement} sendKeys()
	 * command. Performs a type action for the WebElement
	 */
	@Override
	public void sendKeys(CharSequence... keysToSend) {
		try {
			WebElement element = doFindElement();

			element.sendKeys(keysToSend);
			// Action passed. Report now!

			String successMessage = "Type command passed | Value : " + keysToSend + " | Element : " + byLocator;
			LOGGER.info(successMessage);
			reporter.log(LogLevel.PASS, successMessage);
		} catch (Exception e) {
			LOGGER.error("Type command failed. Error : " + e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Provides a wrapper for the native Selenium
	 * {@link org.openqa.selenium.support.ui.Select.Select} selectByVisibleText()
	 * command. Performs a select action for the WebElement
	 * 
	 * @param text option text to be selected
	 */
	@Override
	public void select(String text) {
		try {
			WebElement element = doFindElement();

			Select dropDown = new Select(element);
			dropDown.selectByVisibleText(text);
			// Action passed. Report now!

			String successMessage = "Select command passed | Value : " + text + " | Element : " + byLocator;
			LOGGER.info(successMessage);
			reporter.log(LogLevel.PASS, successMessage);
		} catch (Exception e) {
			LOGGER.error("Select command failed. Error : " + e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Provides a wrapper for the native Selenium {@link WebElement} getText()
	 * command. Performs a text extraction of the WebElement and returns the text.
	 */
	@Override
	public String getText() {
		try {
			WebElement element = doFindElement();

			String text = element.getText();
			// Action passed. Report now!

			String successMessage = "GetText command passed | Text : " + text + " | Element : " + byLocator;
			LOGGER.info(successMessage);
			reporter.log(LogLevel.PASS, successMessage);

			return text;
		} catch (Exception e) {
			LOGGER.error("GetText command failed. Error : " + e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Provides a wrapper for the native Selenium {@link WebElement} findElement()
	 * command. Performs a {@link WebDriverWait} for the element find until a
	 * timeout.
	 * 
	 * @param timeOutInSeconds maximum timeout for the wait
	 */
	@Override
	public void waitUntilPresent(int timeOutInSeconds) {
		try {

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds));
			wait.until(ExpectedConditions.presenceOfElementLocated(convertFindByToBy()));
			// Action passed. Report now!

			String successMessage = "WaitUntilPresent action passed. Found element within " + timeOutInSeconds
					+ " seconds.";
			LOGGER.info(successMessage);
			reporter.log(LogLevel.PASS, successMessage);

		} catch (Exception e) {
			String errorMessage = "WaitUntilPresent action failed. Could not find element within " + timeOutInSeconds
					+ " seconds.";
			LOGGER.error(errorMessage, e);
			throw e;
		}
	}

	/**
	 * Provides a wrapper for the native Selenium {@link WebElement} findElement()
	 * command.
	 */
	protected WebElement doFindElement() {
		// convert page factory FindBy to By object
		byLocator = convertFindByToBy();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(implicitWait));
		return wait.until(ExpectedConditions.presenceOfElementLocated(byLocator));

	}

	/**
	 * Applies the parameters for the locator and constructs the By locator
	 * 
	 * @return
	 */
	private By convertFindByToBy() {
		String locator = String.format(findBy.using(), locatorParams);
		return findBy.how().buildBy(locator);
	}

	/**
	 * get ImplicitWait for this Session
	 * 
	 * @return
	 */
	private int getImplicitWaitforSession() {
		PropertyReader propReader = new PropertyReader(Constants.LOCAL_CONFIG_FILENAME);

		return PrimitiveConverter.parse(Integer.class, propReader.getProperty(Constants.PARAM_NAME_IMPLICITWAIT));
	}

	// From here onwards, the methods are not yet implemented. will throw an
	// NotYetImplementedException if used.

	@Override
	public void submit() {
		// TODO Auto-generated method stub
		throw new NotYetImplementedException();
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		throw new NotYetImplementedException();
	}

	@Override
	public String getTagName() {
		// TODO Auto-generated method stub
		throw new NotYetImplementedException();
	}

	@Override
	public String getAttribute(String name) {
		// TODO Auto-generated method stub
		throw new NotYetImplementedException();
	}

	@Override
	public boolean isSelected() {
		// TODO Auto-generated method stub
		throw new NotYetImplementedException();
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		throw new NotYetImplementedException();
	}

	@Override
	public List<WebElement> findElements(By by) {
		// TODO Auto-generated method stub
		throw new NotYetImplementedException();
	}

	@Override
	public WebElement findElement(By by) {
		// TODO Auto-generated method stub
		throw new NotYetImplementedException();
	}

	@Override
	public boolean isDisplayed() {
		// TODO Auto-generated method stub
		throw new NotYetImplementedException();
	}

	@Override
	public Point getLocation() {
		// TODO Auto-generated method stub
		throw new NotYetImplementedException();
	}

	@Override
	public Dimension getSize() {
		// TODO Auto-generated method stub
		throw new NotYetImplementedException();
	}

	@Override
	public Rectangle getRect() {
		// TODO Auto-generated method stub
		throw new NotYetImplementedException();
	}

	@Override
	public String getCssValue(String propertyName) {
		// TODO Auto-generated method stub
		throw new NotYetImplementedException();
	}

	@Override
	public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
		// TODO Auto-generated method stub
		throw new NotYetImplementedException();
	}

}
