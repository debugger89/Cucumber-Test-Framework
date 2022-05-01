package com.enhance.automation.runtime.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import io.cucumber.core.backend.TestCaseState;
import io.cucumber.java.Scenario;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestCase;

/**
 * Provides the utility methods to interact with low level cucumber APIs via
 * reflection
 * 
 * @author cdushmantha
 *
 */
public class CucumberReflectionUtils {

	/**
	 * Identifies and returns the exception causing the test failure.
	 * 
	 * @param scenario
	 * @return throwable error
	 */
	public static Throwable getExecutionError(Scenario scenario) {
		try {
			Field field = FieldUtils.getField(Scenario.class, "delegate", true);
			final TestCaseState testCase = (TestCaseState) field.get(scenario);

			Method getError = MethodUtils.getMatchingMethod(testCase.getClass(), "getError");
			getError.setAccessible(true);

			return (Throwable) getError.invoke(testCase);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Reads the step definitions of the .feature file and returns the step text for the given step index.
	 * 
	 * @param scenario
	 * @param stepIndex
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static String getExecutionScenarioStep(Scenario scenario, int stepIndex)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field testCaseField = scenario.getClass().getDeclaredField("delegate");
		testCaseField.setAccessible(true);

		TestCaseState tcs = (TestCaseState) testCaseField.get(scenario);
		Field f2 = tcs.getClass().getDeclaredField("testCase");
		f2.setAccessible(true);

		TestCase r = (TestCase) f2.get(tcs);

		List<PickleStepTestStep> stepDefs = r.getTestSteps().stream().filter(x -> x instanceof PickleStepTestStep)
				.map(x -> (PickleStepTestStep) x).collect(Collectors.toList());

		PickleStepTestStep currentStepDef = stepDefs.get(stepIndex);
		String currentStepDescr = currentStepDef.getStep().getText();
		return currentStepDescr;
	}

}
