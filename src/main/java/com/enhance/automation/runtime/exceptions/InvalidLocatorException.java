package com.enhance.automation.runtime.exceptions;

/**
 * Custom exception for any test configuration failures.
 * 
 * @author cdushmantha
 *
 */
public class InvalidLocatorException extends RuntimeException {

	private static final long serialVersionUID = -2953158721142336470L;

	public InvalidLocatorException(String message) {
		super(message);
	}
}
