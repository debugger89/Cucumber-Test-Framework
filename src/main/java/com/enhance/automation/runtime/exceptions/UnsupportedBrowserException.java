package com.enhance.automation.runtime.exceptions;

/**
 * Custom exception for un supported browser type for execution
 * 
 * @author cdushmantha
 *
 */
public class UnsupportedBrowserException extends RuntimeException {

	private static final long serialVersionUID = 6131647626252692619L;

	public UnsupportedBrowserException(String message) {
		super(message);
	}

}
