package com.enhance.automation.runtime.proxy;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Element decorator class for the custom Element type.
 * 
 * @author cdushmantha
 *
 */
public class ElementFieldDecorator {

	public Object decorate(ClassLoader loader, Class<?>[] interfaces, Field field, FindBy findBy, WebDriver driver) {

		if (WebElement.class.isAssignableFrom(field.getType())) {
			return Proxy.newProxyInstance(loader, interfaces, new ElementInvocationHandler(driver, findBy));
		}
		return null;

	}
}
