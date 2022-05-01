package com.enhance.automation.runtime.reports;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.enhance.automation.runtime.enums.LogLevel;

/**
 * Extent reporter implementation class. Contains the concrete methods for the
 * Extent reporting actions. Implements {@link IResultReporter}
 * 
 * @author cdushmantha
 *
 */
public final class ExtentReporterImpl implements IResultReporter {

	private static ExtentReports extent;
	private static ExtentReporterImpl instance;
	private static Map<Integer, ExtentTest> extentTestMap = new HashMap<Integer, ExtentTest>();

	private static File reportFolder;

	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * Private constructor for the Singleton class. Reads the extent-config file and
	 * and customizes the report file.
	 */
	private ExtentReporterImpl() {
		extent = new ExtentReports();

		reportFolder = new File("reports" + File.separator + getReportNameString());

		ExtentSparkReporter spark = new ExtentSparkReporter(reportFolder);
		try {

			InputStream is = ExtentReporterImpl.class.getResourceAsStream("/extent-config.xml");
			File tempConfigFile = File.createTempFile("_Extent_Config-", "");
			tempConfigFile.deleteOnExit();
			FileUtils.copyInputStreamToFile(is, tempConfigFile);
			spark.loadXMLConfig(tempConfigFile);
			tempConfigFile.deleteOnExit();

		} catch (IOException e) {
			LOGGER.error("Loading custom extent report template failed. Using the default template. Error : "
					+ e.getMessage(), e);
		}
		extent.attachReporter(spark);
	}

	/**
	 * Constructs the report name with the time stamp
	 * 
	 * @return
	 */
	private String getReportNameString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd @ HH-mm-ss");
		return dateFormat.format(new Date());
	}

	public static synchronized IResultReporter getInstance() {

		if (instance == null) {
			instance = new ExtentReporterImpl();
		}

		return instance;
	}

	/**
	 * Save the report to disk. synchronized to avoid complications during parallel
	 * execution
	 */
	public synchronized void saveReport() {
		extent.flush();
	}

	/**
	 * Logs a test message to report
	 */
	public void log(LogLevel logLevel, String message) {
		ExtentTest test = extentTestMap.get(getCurrentThreadId());
		test.log(Status.valueOf(logLevel.toString()), message);
	}

	/**
	 * Logs a test message to report
	 */
	public void log(LogLevel logLevel, String message, String screenshotPath, Throwable t) {
		ExtentTest test = extentTestMap.get(getCurrentThreadId());

		test.log(Status.valueOf(logLevel.toString()), message);

		Media m = MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build();

		// Added as a workaround. The message, throwable, screenshot combination is
		// hiding the message.
		test.log(Status.valueOf(logLevel.toString()), t, m);
	}

	/**
	 * Starts a new test and keeps the test against the current thread id.
	 * synchronized to avoid complications during parallel execution
	 */
	public synchronized void startTestCase(String testCaseName) {
		ExtentTest test = extent.createTest(testCaseName);
		extentTestMap.put(getCurrentThreadId(), test);

	}

	/**
	 * Stops the current test against the current thread id. synchronized to avoid
	 * complications during parallel execution
	 */
	public synchronized void endTestCase() {
		extent.flush();
	}

	private int getCurrentThreadId() {
		return (int) Thread.currentThread().getId();
	}

	public File getReportFolder() {
		return reportFolder;
	}

}
