package com.enhance.automation.runtime.proxy;

import java.lang.reflect.Field;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.enhance.automation.runtime.exceptions.PageInitializationException;

/**
 * Custom {@link PageFactory} class for the page initialization of page factory
 * model
 * 
 * @author cdushmantha
 *
 */
public class PageObjectFactory {

	/**
	 * Custom initElements command for page object class. Proxies the fields of the
	 * page object class.
	 * 
	 * @param <T>
	 * @param driver
	 * @param page
	 * @return T
	 */
	public static <T> T initElements(WebDriver driver, Object page) {

		Class<?> proxyIn = page.getClass();
		T pageObject = null;
		while (proxyIn != Object.class) {

			proxyFields(page, proxyIn, page, driver);
			proxyIn = proxyIn.getSuperclass();
		}
		return pageObject;

	}

	/**
	 * Proxy each field in the page object class and decorates the each field in the
	 * page object class
	 * 
	 * @param page
	 * @param proxyIn
	 * @param pageObject
	 * @param driver
	 */
	private static void proxyFields(Object page, Class<?> proxyIn, Object pageObject, WebDriver driver) {
		Field[] fields = proxyIn.getDeclaredFields();
		for (Field field : fields) {

			FindBy findBy = field.getAnnotation(FindBy.class);

			Object value = new ElementFieldDecorator().decorate(proxyIn.getClassLoader(),
					new Class[] { field.getType() }, field, findBy, driver);

			if (value != null) {
				try {
					field.setAccessible(true);
					field.set(pageObject, value);
				} catch (IllegalAccessException e) {
					throw new PageInitializationException(e);
				}

			}
		}
	}

}
