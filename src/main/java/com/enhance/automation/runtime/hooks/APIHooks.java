package com.enhance.automation.runtime.hooks;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.enhance.automation.runtime.enums.LogLevel;
import com.enhance.automation.runtime.reports.ExtentReporterImpl;
import com.enhance.automation.runtime.reports.IResultReporter;
import com.enhance.automation.runtime.utils.CucumberReflectionUtils;

import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;

/**
 * @author cdushmantha
 *
 *         Contains API related cucumber hooks
 */
public class APIHooks {

	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * Cucumber @AfterStep Hook for API scenarios. Records the test step statuses
	 * for Extend reporting purposes
	 * 
	 * @param scenario
	 * @throws IOException
	 */
	@AfterStep(value = "@api and not @web")
	public void afterStep(Scenario scenario) throws IOException {
		if (scenario.isFailed()) {
			IResultReporter reporter = ExtentReporterImpl.getInstance();
			Throwable e = getExecutionError(scenario);
			LOGGER.error(e.getMessage(), e);
			reporter.log(LogLevel.FAIL, getExecutionError(scenario).getMessage(), null, e);
		}
	}

	/**
	 * If a certain feature step is failed, get the failure reason for reporting
	 * purposes.
	 * 
	 * @param scenario
	 * @return
	 */
	private Throwable getExecutionError(Scenario scenario) {
		return CucumberReflectionUtils.getExecutionError(scenario);
	}

}
