package com.enhance.automation.runtime.reports;

import java.io.File;

import com.enhance.automation.runtime.enums.LogLevel;

/**
 * Result reporter interface to be implemented later
 * 
 * @author cdushmantha
 *
 */
public interface IResultReporter {

	public void startTestCase(String testCaseName);

	public void endTestCase();

	public void saveReport();

	public void log(LogLevel logLevel, String message);

	public void log(LogLevel logLevel, String message, String screenshotPath, Throwable t);

	public File getReportFolder();
}
