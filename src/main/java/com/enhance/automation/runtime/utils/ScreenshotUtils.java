package com.enhance.automation.runtime.utils;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Provides a utility for taking screenshots for the WebDriver sessions.
 * 
 * @author cdushmantha
 *
 */
public class ScreenshotUtils {

	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * Takes a Selenium WebDriver screenshot and save the file to the given folder.
	 * 
	 * @param driver
	 * @param reportFolderAbsolutePath
	 * @return
	 */
	public static String takeWebOrNativeScreenshot(WebDriver driver, String reportFolderAbsolutePath) {

		final String screenshotFullPath = constructScreenshotName(reportFolderAbsolutePath);

		// try to get the Selenium based screenshot first.
		try {
			File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.moveFile(sourceFile, new File(screenshotFullPath));
		} catch (Exception e) {
			// If error occurred with Selenium, take native Java screenshot.

			try {
				doTakeNativeScreenshot(new File(screenshotFullPath));
			} catch (Exception e1) {
				// both screenshot attempts failed. Report the issue and do nothing.
				LOGGER.error("Error occured while taking screenshot. Both Selenium and Java screenshots failed.", e,
						e1);
				return null;
			}
		}

		return screenshotFullPath;
	}

	/**
	 * If the Selenium screenshot command failed for any reason use native
	 * {@link java.awt.Robot} class for taking a screenshot.
	 * 
	 * @param screenshotFile
	 * @return
	 * @throws HeadlessException
	 * @throws AWTException
	 * @throws IOException
	 */
	private static boolean doTakeNativeScreenshot(File screenshotFile)
			throws HeadlessException, AWTException, IOException {
		// get native java screenshot

		screenshotFile.getParentFile().mkdirs();
		BufferedImage image = new Robot()
				.createScreenCapture(new java.awt.Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		return ImageIO.write(image, "png", screenshotFile);

	}

	/**
	 * constructs the screenshot file name
	 * 
	 * @param reportFolderAbsolutePath
	 * @return
	 */
	private static String constructScreenshotName(String reportFolderAbsolutePath) {
		final SimpleDateFormat screenshotDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
		final String screenshotFolderPath = reportFolderAbsolutePath + File.separator + "screenshots";
		return screenshotFolderPath + File.separator + screenshotDateFormat.format(new Date()) + ".png";

	}
}
