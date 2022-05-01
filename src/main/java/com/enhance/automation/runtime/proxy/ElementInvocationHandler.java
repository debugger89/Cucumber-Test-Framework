package com.enhance.automation.runtime.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * Handles the invocation of the WebElement's action commands
 * 
 * @author cdushmantha
 *
 */
public class ElementInvocationHandler implements InvocationHandler {

	private WebDriver driver;
	private FindBy findBy;

	/**
	 * Constructor
	 * 
	 * @param driver
	 * @param findBy
	 * @param proxyElementClassName
	 */
	public ElementInvocationHandler(WebDriver driver, FindBy findBy) {
		this.driver = driver;
		this.findBy = findBy;
	}

	/**
	 * Invoke the proxie'd action and override the default action of the WebElement.
	 * Will invoke the wrapped commands in {@link ElementImpl}
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		ElementImpl impl = new ElementImpl(driver, findBy);

		try {
			method.setAccessible(true);
			return method.invoke(impl, args);
		} catch (InvocationTargetException | IllegalAccessException e) {
			throw e.getCause();
		}

	}

}