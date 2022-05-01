package com.enhance.automation.runtime.exceptions;

/**
 * Custom exception for any page initialization failures.
 * 
 * @author cdushmantha
 *
 */
public class PageInitializationException extends RuntimeException {

	private static final long serialVersionUID = 1552103792184523896L;

	public PageInitializationException(final String message) {
		super(message);
	}

	public PageInitializationException(final Throwable throwable) {
		super(throwable);
	}

}
