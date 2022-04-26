/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.framework.util;

import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A utility that delays execution
 * 
 * The class can sleep for a specified duration.
 * 
 * Preferably, test cases should use the defined Small, Medium, Long, and Very
 * Long delays.
 * 
 * @author Matt Rhoades
 *
 */
public class SleepUtil {
	private static Logger logger = LogManager.getLogger(SleepUtil.class);
	public static int SleepGranularity = 1000;

	public static void sleep(long millis) {
		Date start = new Date();

		try {

			long target = millis;
			long total = 0; // The total milliseconds slept in this method

			try {
				while (millis > 0) {
					if (millis >= SleepGranularity) {
						logger.info("Sleep: " + SleepGranularity + " milliseconds ... (" + total + "/" + target + ")");
						Thread.sleep(SleepGranularity);
						total += SleepGranularity;
					} else {
						logger.info("Sleep: " + millis + " milliseconds ... (" + millis + "/" + target + ")");
						Thread.sleep(millis);
						total += millis;
					}

					millis -= SleepGranularity;
				}

			} catch (InterruptedException e) {
				logger.warn("Sleep was interuppted", e);
			}

		} finally {
			SleepMetrics.RecordSleep((new Throwable()).getStackTrace(), millis, start, new Date());
		}
	}

	public static void sleepVerySmall() {
		if (ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".coverage.enabled",
				ConfigProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
			sleep(ConfigProperties.getIntProperty("very_small_wait", 800));
		} else {
			sleep(ConfigProperties.getIntProperty("very_small_wait", 500));
		}
	}

	public static void sleepSmall() {
		if (ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".coverage.enabled",
				ConfigProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
			sleep(ConfigProperties.getIntProperty("small_wait", 1200));
		} else {
			sleep(ConfigProperties.getIntProperty("small_wait", 1000));
		}
	}

	public static void sleepMedium() {
		if (ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".coverage.enabled",
				ConfigProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
			sleep(ConfigProperties.getIntProperty("medium_wait", 2500));
		} else {
			sleep(ConfigProperties.getIntProperty("medium_wait", 2000));
		}
	}
	
	public static void zimbraXSleepMedium() {
		if (ConfigProperties.getStringProperty("server.zimbrax").equals("true")) {
			sleep(ConfigProperties.getIntProperty("medium_wait", 2000));
		}
	}

	public static void sleepLong() {
		if (ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".coverage.enabled",
				ConfigProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
			sleep(ConfigProperties.getIntProperty("long_wait", 5000));
		} else {
			sleep(ConfigProperties.getIntProperty("long_wait", 4000));
		}
	}

	public static void sleepLongMedium() {
		if (ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".coverage.enabled",
				ConfigProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
			sleep(ConfigProperties.getIntProperty("long_medium_wait", 7000));
		} else {
			sleep(ConfigProperties.getIntProperty("long_medium_wait", 6000));
		}
	}

	public static void sleepVeryLong() {
		if (ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".coverage.enabled",
				ConfigProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
			sleep(ConfigProperties.getIntProperty("very_long_wait", 13000));
		} else {
			sleep(ConfigProperties.getIntProperty("very_long_wait", 10000));
		}
	}

	public static void sleepVeryVeryLong() {
		if (ConfigProperties.getStringProperty(ConfigProperties.getLocalHost() + ".coverage.enabled",
				ConfigProperties.getStringProperty("coverage.enabled")).contains("true") == true) {
			sleep(ConfigProperties.getIntProperty("very_long_wait", 18000));
		} else {
			sleep(ConfigProperties.getIntProperty("very_long_wait", 15000));
		}
	}
}