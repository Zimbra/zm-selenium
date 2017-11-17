/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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

import java.util.Collection;
import java.util.regex.*;
import org.apache.log4j.*;
import org.testng.Assert;
import com.zimbra.qa.selenium.framework.core.ExecuteHarnessMain;

public class ZAssert {
	private static Logger logger = LogManager.getLogger(ZAssert.class);

	protected static final Logger tracer = LogManager.getLogger(ExecuteHarnessMain.TraceLoggerName);

	private static void trace(String message) {
		tracer.trace(message);
	}

	public static void resetCounts() {
		tracer.trace("Reseting counts");
	}

	public static void assertTrue(boolean condition, String message) {
		trace(message);

		message = "assertTrue: " + message;
		String details = String.format("%s -- (%s == %s) [%s]", "assertTrue", condition, true, message);
		logger.info(details);

		try {
			Assert.assertTrue(condition, details);

		} catch (AssertionError e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	public static void assertFalse(boolean condition, String message) {
		trace(message);

		message = "assertFalse: " + message;
		String details = String.format("%s -- (%s == %s) [%s]", "assertFalse", condition, false, message);
		logger.info(details);

		try {
			Assert.assertFalse(condition, details);

		} catch (AssertionError e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	public static void assertEquals(Object actual, Object expected, String message) {
		trace(message);

		message = "assertEquals: " + message;
		String details = String.format("%s -- (%s == %s) [%s]", "assertEquals", actual, expected, message);
		logger.info(details);

		try {
			Assert.assertEquals(actual, expected, details);

		} catch (AssertionError e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	public static void assertNotEqual(Object actual, Object expected, String message) {
		trace(message);

		message = "assertNotEqual: " + message;
		String details = String.format("%s -- (!(%s).equals(%s)) [%s]", "assertNotEqual", actual, expected, message);
		logger.info(details);

		try {
			Assert.assertTrue(!actual.equals(expected), details);

		} catch (AssertionError e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	public static void assertGreaterThan(int actual, int expected, String message) {
		trace(message);

		message = "assertGreaterThan: " + message;
		String details = String.format("%s -- (%s > %s) [%s]", "assertGreaterThan", actual, expected, message);
		logger.info(details);

		try {
			if (actual <= expected) {
				throw new AssertionError(details);
			}
		} catch (AssertionError e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	public static void assertGreaterThanEqualTo(int actual, int expected, String message) {
		trace(message);

		message = "assertGreaterThanEqualTo: " + message;
		String details = String.format("%s -- (%s >= %s) [%s]", "assertGreaterThanEqualTo", actual, expected, message);
		logger.info(details);

		try {
			if (actual < expected) {
				throw new AssertionError(details);
			}

		} catch (AssertionError e) {

			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	public static void assertLessThan(int actual, int expected, String message) {
		trace(message);

		message = "assertLessThan: " + message;
		String details = String.format("%s -- (%s < %s) [%s]", "assertLessThan", actual, expected, message);
		logger.info(details);

		try {
			if (actual >= expected) {
				throw new AssertionError(details);
			}

		} catch (AssertionError e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	public static void assertLessThanEqualTo(int actual, int expected, String message) {
		trace(message);

		message = "assertLessThanEqualTo: " + message;
		String details = String.format("%s -- (%s < %s) [%s]", "assertLessThanEqualTo", actual, expected, message);
		logger.info(details);

		try {
			if (actual > expected) {
				throw new AssertionError(details);
			}

		} catch (AssertionError e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	public static void assertNull(Object object, String message) {
		trace(message);

		message = "assertNull: " + message;
		String details = String.format("%s -- (%s == null) [%s]", "assertNull", object, message);
		logger.info(details);

		try {
			Assert.assertNull(object, details);

		} catch (AssertionError e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	public static void assertNotNull(Object object, String message) {
		trace(message);

		message = "assertNotNull: " + message;
		String details = String.format("%s -- (%s != null) [%s]", "assertNotNull", object, message);
		logger.info(details);

		try {
			Assert.assertNotNull(object, details);

		} catch (AssertionError e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	public static void assertContains(Collection<?> collection, Object object, String message) {
		trace(message);

		message = "assertContains: " + message;
		String details = String.format("%s -- (collection contains %s) [%s]", "assertContains", object, message);
		logger.info(details);

		try {
			boolean contains = collection.contains(object);
			Assert.assertTrue(contains, details);

		} catch (AssertionError e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	public static void assertMatches(String pattern, String input, String message) {
		assertMatches(Pattern.compile(pattern), input, message);
	}

	public static void assertMatches(Pattern pattern, String input, String message) {
		trace(message);

		message = "assertMatches" + message;
		String details = String.format("%s -- (%s matches %s) [%s]", "assertMatches", pattern.toString(), input,
				message);
		logger.info(details);

		try {
			Matcher m = pattern.matcher(input);
			Assert.assertTrue(m.matches(), details);

		} catch (AssertionError e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	public static void assertNotMatches(String pattern, String input, String message) {
		assertNotMatches(Pattern.compile(pattern), input, message);
	}

	public static void assertNotMatches(Pattern pattern, String input, String message) {
		trace(message);

		message = "assertNotMatches" + message;
		String details = String.format("%s -- (%s not matches %s) [%s]", "assertNotMatches", pattern.toString(), input,
				message);
		logger.info(details);

		try {
			Matcher m = pattern.matcher(input);
			Assert.assertFalse(m.matches(), details);

		} catch (AssertionError e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Verify that "actual" contains "substring"
	 * 
	 * @param actual
	 *            the actual text
	 * @param substring
	 *            the substring that should be cotained in actual
	 * @param message
	 *            the logging message
	 */
	public static void assertStringContains(String actual, String substring, String message) {
		trace(message);

		message = "assertStringContains: " + message;
		String details = String.format("%s -- (%s contains %s) [%s]", "assertStringContains", actual, substring,
				message);
		logger.info(details);

		try {
			boolean contains = actual.contains(substring);
			Assert.assertTrue(contains, details);

		} catch (AssertionError e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Verify that "actual" does not contain "substring"
	 * 
	 * @param actual
	 *            the actual text
	 * @param substring
	 *            the substring that should be cotained in actual
	 * @param message
	 *            the logging message
	 */
	public static void assertStringDoesNotContain(String actual, String substring, String message) {
		trace(message);

		message = "assertStringDoesNotContain: " + message;
		String details = String.format("%s -- (%s does not contain %s) [%s]", "assertStringDoesNotContain", actual,
				substring, message);
		logger.info(details);

		try {
			boolean contains = actual.contains(substring);
			Assert.assertFalse(contains, details);

		} catch (AssertionError e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
}