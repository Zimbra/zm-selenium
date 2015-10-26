/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.framework.util;

import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * A utility that delays execution
 * 
 * The class can sleep for a specified duration.
 * 
 * Preferably, test cases should use the defined Small, Medium, Long, 
 * and Very Long delays.
 * 
 * @author Matt Rhoades
 *
 */
public class SleepUtil {
	private static Logger logger = LogManager.getLogger(SleepUtil.class);

	/// Public methods
	public static int SleepGranularity = 1000;

	public static void sleep(long millis) {

		Date start = new Date();

		try {

			long target = millis;
			long total = 0; // The total milliseconds slept in this method

			try {

				while (millis > 0) {

					if ( millis >= SleepGranularity) {
						logger.info("Sleep: "+ SleepGranularity +" milliseconds ... ("+ total +"/"+ target +")");
						Thread.sleep(SleepGranularity);
						total += SleepGranularity;
					} else {
						logger.info("Sleep: "+ millis +" milliseconds ... ("+ millis +"/"+ target +")");
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

	/**
	 * Sleep a 500 msec
	 */
	public static void sleepVerySmall() {

		if(ZimbraSeleniumProperties.getStringProperty("coverage.enabled").contains("true")){

			sleep(ZimbraSeleniumProperties.getIntProperty("very_small_wait", 1500));

		}
		sleep(ZimbraSeleniumProperties.getIntProperty("very_small_wait", 500));
	}

	/**
	 * Sleep a 1000 msec
	 */
	public static void sleepSmall() {

		if(ZimbraSeleniumProperties.getStringProperty("coverage.enabled").contains("true")){

			sleep(ZimbraSeleniumProperties.getIntProperty("very_small_wait", 2500));

		}
		sleep(ZimbraSeleniumProperties.getIntProperty("small_wait", 1000));
	}

	/**
	 * Sleep a 2000 msec
	 */
	public static void sleepMedium() {
		if(ZimbraSeleniumProperties.getStringProperty("coverage.enabled").contains("true")){

			sleep(ZimbraSeleniumProperties.getIntProperty("very_small_wait", 4500));

		}
		sleep(ZimbraSeleniumProperties.getIntProperty("medium_wait", 2000));
	}

	/**
	 * Sleep a 4000 msec
	 */
	public static void sleepLong() {
		if(ZimbraSeleniumProperties.getStringProperty("coverage.enabled").contains("true")){

			sleep(ZimbraSeleniumProperties.getIntProperty("very_small_wait", 8000));

		}
		sleep(ZimbraSeleniumProperties.getIntProperty("long_wait", 4000));
	}

	/**
	 * Sleep a 10,000 msec
	 */
	public static void sleepVeryLong() {
		if(ZimbraSeleniumProperties.getStringProperty("coverage.enabled").contains("true")){

			sleep(ZimbraSeleniumProperties.getIntProperty("very_small_wait", 25000));

		}
		sleep(ZimbraSeleniumProperties.getIntProperty("very_long_wait", 10000));
	}


}
