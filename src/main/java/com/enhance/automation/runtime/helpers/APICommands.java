package com.enhance.automation.runtime.helpers;

import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.enhance.automation.runtime.enums.LogLevel;
import com.enhance.automation.runtime.reports.ExtentReporterImpl;
import com.enhance.automation.runtime.reports.IResultReporter;
import com.enhance.automation.runtime.utils.Constants;
import com.enhance.automation.runtime.utils.PropertyReader;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * @author cdushmantha
 *
 */
public class APICommands {

	protected RequestSpecification request;
	protected Response response;
	private IResultReporter reporter;
	private static final Logger LOGGER = LogManager.getLogger();

	public APICommands() {
		reporter = ExtentReporterImpl.getInstance();
	}

	/**
	 * Executes a HTTP GET command and returns the Response object
	 * 
	 * @param resourceUrl The resource URL
	 * @return
	 * @throws Exception
	 */
	public Response executeGet(String resourceUrl) throws Exception {
		try {
			// get the base URL from the config file and construct the full URL
			PropertyReader propReader = new PropertyReader(Constants.LOCAL_CONFIG_FILENAME);
			String baseurl = propReader.getProperty(Constants.PARAM_NAME_BASEURL_API);
			String fullurl = baseurl + "/" + resourceUrl;

			// execute the GET command
			request = RestAssured.given();
			response = request.get(new URL(fullurl));

			LOGGER.info("GET command executed for : " + fullurl);
			reporter.log(LogLevel.PASS, "GET command executed for : " + fullurl);

			return response;
		} catch (Exception e) {
			LOGGER.error("GET command failed due to error : " + e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Executes a HTTP POST command and returns the Response object
	 * 
	 * @param resourceUrl The resource URL
	 * @return
	 * @throws Exception
	 */
	public Response executePost(String resourceUrl, JSONObject payload) throws Exception {
		try {
			// get the base URL from the config file and construct the full URL
			PropertyReader propReader = new PropertyReader(Constants.LOCAL_CONFIG_FILENAME);
			String baseurl = propReader.getProperty(Constants.PARAM_NAME_BASEURL_API);
			String fullurl = baseurl + "/" + resourceUrl;

			// execute the POST command with payload
			request = RestAssured.given();
			request.header("Content-Type", "application/json");
			request.body(payload.toString());
			response = request.post(new URL(fullurl));

			LOGGER.info("POST command executed for : " + fullurl);
			reporter.log(LogLevel.PASS, "POST command executed for : " + fullurl);

			return response;
		} catch (Exception e) {
			LOGGER.error("POST command failed due to error : " + e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * If the response is an array, count the number of elements in the array.
	 * 
	 * @param path The Json path for the array
	 * @return number of elements in array
	 */
	public int getResponseArraySize(String path) {
		try {
			int count = response.body().jsonPath().getList(path).size();

			LOGGER.info("Response body's path : " + path + " has : " + count + " items");
			reporter.log(LogLevel.PASS, "Response body has : " + count + " items");
			return count;
		} catch (Exception e) {
			LOGGER.error("Response body parsing failed due to error : " + e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Logs an event to the reporter.
	 * 
	 * @param status  Defines the severity of the event.
	 * @param message Event message to be locked.
	 */
	public void log(LogLevel status, String message) {
		ExtentReporterImpl.getInstance().log(status, message);
		LOGGER.info(message);
	}

}
