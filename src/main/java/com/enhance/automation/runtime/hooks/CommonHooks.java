package com.enhance.automation.runtime.hooks;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.enhance.automation.runtime.enums.LogLevel;
import com.enhance.automation.runtime.reports.ExtentReporterImpl;
import com.enhance.automation.runtime.reports.IResultReporter;
import com.enhance.automation.runtime.utils.CucumberReflectionUtils;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;

/**
 * @author cdushmantha
 *
 *         Contains common hooks for executions regardless of the execution type
 */
public class CommonHooks {

	private static final Logger LOGGER = LogManager.getLogger();

	private Map<Long, Integer> stepCounter = new HashMap<Long, Integer>();

	/**
	 * Cucumber hook @Before. Start the test reporting for this scenario and starts
	 * of step count for reporting purposes
	 * 
	 * @param scenario
	 */
	@Before(order = 0)
	public void beforeScenario(Scenario scenario) {

		startTestReporting(scenario);

		startStepCounting();

	}

	/**
	 * Cucumber hook @BeforeStep. Keeps track of the scenario's steps and add them
	 * to the Extend report as a feature step.
	 * 
	 * @param scenario
	 */
	@BeforeStep
	public void beforeStep(Scenario scenario) {
		String stepText = getCurrentStepText(scenario);
		if (stepText != null) {
			IResultReporter reporter = ExtentReporterImpl.getInstance();
			reporter.log(LogLevel.INFO, stepText);
		}

	}

	/**
	 * Cucumber hook @After. Ends the reporting for this scenario and flushes the
	 * report to disk.
	 * 
	 * @param scenario
	 */
	@After(order = 0)
	public void afterScenario(Scenario scenario) {

		// generate the report
		try {
			endReporting();
		} catch (Exception e) {
			LOGGER.error("Error occured while generating the test report : " + e.getMessage(), e);
		}
	}

	/**
	 * Ends the Extend reporting and flushes the report file to disk.
	 */
	private void endReporting() {
		IResultReporter reporter = ExtentReporterImpl.getInstance();
		LOGGER.info("Generating the extent report for test run in " + reporter.getReportFolder().getAbsolutePath());
		reporter.endTestCase();
	}

	/**
	 * Increments the step index in the feature file for reporting purposes.
	 */
	private void incrementStepCount() {
		long tid = getCurrentThreadID();
		stepCounter.put(tid, (stepCounter.get(tid) + 1));
	}

	/**
	 * Starts the test reporting for this specific test scenario.
	 * 
	 * @param scenario
	 */
	private void startTestReporting(Scenario scenario) {

		String testName = scenario.getName();

		IResultReporter reporter = ExtentReporterImpl.getInstance();
		reporter.startTestCase(testName);

	}

	private void startStepCounting() {
		stepCounter.put(getCurrentThreadID(), 0);
	}

	private long getCurrentThreadID() {
		return Thread.currentThread().getId();
	}

	/**
	 * Extracts the current step text of the executed scenario as a text for
	 * reporting purposes.
	 * 
	 * @param scenario
	 * @return
	 */
	private String getCurrentStepText(Scenario scenario) {
		try {

			return CucumberReflectionUtils.getExecutionScenarioStep(scenario, stepCounter.get(getCurrentThreadID()));

		} catch (Exception e) {
			LOGGER.trace("Error occurred during getting step name. Skipping the action.", e);
		} finally {
			incrementStepCount();
		}
		return null;
	}
}
