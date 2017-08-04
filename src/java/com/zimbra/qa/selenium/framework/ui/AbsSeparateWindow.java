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
package com.zimbra.qa.selenium.framework.ui;

import java.util.*;

import org.apache.log4j.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import com.zimbra.qa.selenium.framework.util.*;

/**
 * The <code>AbsSeparateWindow</code> class is a base class that all "separate
 * window" objects can derive from. The main additional functionality is the
 * ability to switch focus between different windows (i.e. the 'main' window and
 * the 'separate' window) when executing Selenium method calls.
 * <p>
 * All selenium methods (e.g. sClick(), sType()) must be redefined in this
 * class, with a wrapper to switch windows.
 * <p>
 *
 * @author Matt Rhoades
 *
 */
public abstract class AbsSeparateWindow extends AbsPage {
	protected static Logger logger = LogManager.getLogger(AbsSeparateWindow.class);

	public static boolean IsDebugging = false;

	/**
	 * The Selenium ID for the separate window
	 */
	protected String DialogWindowID = null;

	/**
	 * The Selenium ID for the main window ("null" by default)
	 */
	protected String MainWindowID = "null";

	/**
	 * The title bar text
	 */
	protected String DialogWindowTitle = null;

	/**
	 * Whether or not to switch focus when working in the separate window
	 */
	protected boolean DoChangeWindowFocus = false;

	public AbsSeparateWindow(AbsApplication application) {
		super(application);

		logger.info("new " + AbsSeparateWindow.class.getCanonicalName());

		DoChangeWindowFocus = false;

	}

	protected void changeFocus() throws HarnessException {
		if (DoChangeWindowFocus) {
			super.sWindowFocus();
		}
	}
	public void sTypeNewWindow(String locator, String value) throws HarnessException {
		logger.info(myPageName() + " sType(" + locator + ", " + value + ")");

		super.sSelectWindow(this.DialogWindowID);
		super.sType(locator, value);
		SleepUtil.sleepSmall();
	} 

	public void sClickNewWindow(String locator) throws HarnessException {
		logger.info(myPageName() + " sClickInNewWindow(" + locator + ")");

		super.sSelectWindow(this.DialogWindowID);
		super.sClick(locator);
		SleepUtil.sleepSmall();
	} 

	public String sGetBodyContent(String windowTitle, String locator) throws HarnessException {
		logger.info(myPageName() + " sGetBodyContent()");
		
		String text;
		WebElement we = null;
		we = webDriver().findElement(By.cssSelector(locator.replace("css=", "")));
		text = we.getText();
		return (text);
	}
	
	public int sGetCssCountNewWindow(String css) throws HarnessException {
		logger.info(myPageName() + " sGetCssCount(" + css + ")");

		Integer count = null;

		try {
			super.sSelectWindow(this.DialogWindowID);
			changeFocus();
			count = super.sGetCssCount(css);

		} finally {

		}

		logger.info("getCssCount(" + css + ") = " + count);

		return (count);
	}
	
	public void sClick(List<String> locators) throws HarnessException {
		logger.info(myPageName() + " sClick(" + Arrays.toString(locators.toArray()) + ")");

		for (String locator : locators) {
			sClick(locator);
		}
	}
	public void zTypeCharacters(String characters) throws HarnessException {
		logger.info(myPageName() + " zTypeCharacters()");
		
		super.zKeyboard.zTypeCharacters(characters);
		}

	public void zCloseWindow() throws HarnessException {
		logger.info(myPageName() + " zCloseWindow()");

		try {

			Set<String> windows = webDriver().getWindowHandles();
			if(windows.size() == 1)
				return;
			String mainwindow = webDriver().getWindowHandle();

			for (String handle : windows) {
				webDriver().switchTo().window(handle);
				if (!handle.equals(mainwindow)) {
					webDriver().switchTo().window(handle).close();
				}
			}
			webDriver().switchTo().window(mainwindow);

		} finally {
			super.zSelectWindow(MainWindowID);
		}
	}

	public void zCloseWindow(String title) throws HarnessException {
		logger.info(myPageName() + " zCloseWindow(" + title + ")");

		try {

			if ((this.DialogWindowID == null || this.DialogWindowID.equals("null")) && (this.DialogWindowTitle.equals("") || this.DialogWindowTitle.equals("null")) && title.equals("")) {
				return;
			}

			try {
				super.sSelectWindow(title);

			} catch (WebDriverException e) {
				logger.warn("In zCloseWindow(), unable to locate DialogWindowID. Assume already closed.", e);
				return;
			}

			for (String winHandle : webDriver().getWindowHandles()) {
				WebDriver window = webDriver().switchTo().window(winHandle);
				if ((window.getTitle().contentEquals(title) || window.getCurrentUrl().contains("/" + title + "?")) && !window.getTitle().contentEquals("Zimbra: Inbox")
						&& !window.getTitle().contentEquals("Zimbra: Contacts")
						&& !window.getTitle().contentEquals("Zimbra: Calendar")
						&& !window.getTitle().contentEquals("Zimbra: Tasks")
						&& !window.getTitle().contentEquals("Zimbra: Briefcase")
						&& !window.getTitle().contentEquals("Zimbra: Preferences")) {
					webDriver().close();
				}
				if (title.equals("selenium_blank")) {
					if (window.getTitle().equals("")) {
						webDriver().close();
					}
				}
			}

		} finally {
			super.zSelectWindow(MainWindowID);
		}
	}
	
	public void zSetWindowTitle(String title) throws HarnessException {
		this.DialogWindowTitle = title;
		this.DialogWindowID = title;
	}

	public void zSetWindowID(String id) throws HarnessException {
		this.DialogWindowID = id;
		this.DialogWindowTitle = id;
	}

	protected boolean zSetWindowIdByTitle(String title) throws HarnessException {

		if (IsDebugging) {

			// Helpful for debugging, log all the names, titles, names
			for (String name : super.sGetAllWindowIds()) {
				logger.info("Window ID: " + name);
			}

			for (String name : super.sGetAllWindowNames()) {
				logger.info("Window name: " + name);
			}

			for (String t : super.sGetAllWindowTitles()) {
				logger.info("Window title: " + t);
			}

		}

		for (String t : super.sGetAllWindowTitles()) {
			logger.info("Window title: " + t);
			if (t.toLowerCase().contains(title.toLowerCase())) {
				DialogWindowID = title;
				return (true);
			}
		}

		return (false);

	}

	public boolean zIsClosed(String windowName) throws HarnessException {
		logger.info(myPageName() + " zIsClosed()");
		return zWaitForWindowClosed(windowName);
	}

	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		if (this.DialogWindowTitle == null)
			throw new HarnessException("Window Title is null.  Use zSetWindowTitle() first.");

		for(int i= 1; i < 5; i++ ) {
			logger.info("Attempt " + i + ": Looking for window: " + DialogWindowTitle);

			try {
				for (String title : super.sGetAllWindowTitles()) {
					logger.info("Window title: " + title);
					if (title.toLowerCase().contains(DialogWindowTitle.toLowerCase())) {
						DialogWindowID = title;
						logger.info("zIsActive() = true ... title = " + DialogWindowID);
						return (true);
					}
				}
				SleepUtil.sleepMedium();
			} catch(Exception ex) {
				logger.error(ex);
			}
		}

		logger.info("zIsActive() = false");
		return (false);

	}

} 
