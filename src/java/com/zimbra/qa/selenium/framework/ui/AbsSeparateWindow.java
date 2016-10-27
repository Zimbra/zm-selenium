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

	public void sClick(String locator) throws HarnessException {
		logger.info(myPageName() + " sClick(" + locator + ")");

		try {
			super.sSelectWindow(this.DialogWindowID);
			changeFocus();

			super.sClick(locator);
			SleepUtil.sleepMedium();

		} finally {
			super.sSelectWindow(MainWindowID);
			super.sWindowFocus();
		}

	}

	protected void changeFocus() throws HarnessException {
		if (DoChangeWindowFocus) {
			super.sWindowFocus();
		}
	}

	public void sType(String locator, String value) throws HarnessException {
		logger.info(myPageName() + " sType(" + locator + ", " + value + ")");

		try {
			super.sSelectWindow(this.DialogWindowID);
			changeFocus();

			super.sType(locator, value);

		} finally {
			super.sSelectWindow(MainWindowID);
			super.sWindowFocus();
		}

	}

	public void sTypeNewWindow(String locator, String value) throws HarnessException {
		logger.info(myPageName() + " sType(" + locator + ", " + value + ")");

		try {
			super.sSelectWindow(this.DialogWindowID);
			super.sType(locator, value);

		} finally {

		}

	}

	public String sGetText(String locator) throws HarnessException {
		logger.info(myPageName() + " sGetText(" + locator + ")");

		String text = "";

		try {
			super.sSelectWindow(this.DialogWindowID);
			changeFocus();

			text = super.sGetText(locator);

		} finally {
			super.sSelectWindow(MainWindowID);
			super.sWindowFocus();
		}

		return (text);
	}

	public String sGetBodyText() throws HarnessException {
		logger.info(myPageName() + " sGetBodyText()");

		String text;

		try {
			super.sSelectWindow(this.DialogWindowID);
			text = super.sGetBodyText();

		} finally {
			super.sSelectWindow(MainWindowID);
			super.sWindowFocus();
		}

		return (text);
	}

	public String sGetBodyContent(String windowTitle, String locator) throws HarnessException {
		logger.info(myPageName() + " sGetBodyContent()");

		String text;

		try {
			webDriver().switchTo().defaultContent();
			super.sSelectWindow(windowTitle);

			WebElement we = null;
			we = webDriver().findElement(By.cssSelector(locator.replace("css=", "")));
			text = we.getText();

		} finally {
			super.sSelectWindow(MainWindowID);
			super.sWindowFocus();
		}

		return (text);
	}

	public void sSelectFrame(String locator) throws HarnessException {
		logger.info(myPageName() + " sSelectFrame(" + locator + ")");

		try {
			super.sSelectWindow(this.DialogWindowID);
			changeFocus();

			super.sSelectFrame(locator);

		} finally {
			super.sSelectWindow(MainWindowID);
			super.sWindowFocus();
		}

	}

	public int sGetCssCount(String css) throws HarnessException {
		logger.info(myPageName() + " sGetCssCount(" + css + ")");

		Integer count = null;

		try {
			super.sSelectWindow(this.DialogWindowID);
			changeFocus();

			count = super.sGetCssCount(css);

		} finally {
			super.sSelectWindow(MainWindowID);
			super.sWindowFocus();
		}

		logger.info("getCssCount(" + css + ") = " + count);
		return (count);
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

	public void sType(String iframelocator, String locator, String value) throws HarnessException {

		try {
			super.sSelectWindow(this.DialogWindowID);
			changeFocus();
			try {

				super.sSelectFrame(iframelocator);
				super.sType(locator, value);

			} finally {
				this.sSelectFrame("relative=top");
			}

		} finally {
			super.sSelectWindow(MainWindowID);
			super.sWindowFocus();
		}

	}

	public String sGetText(String iframelocator, String locator) throws HarnessException {

		String text = "";

		try {
			super.sSelectWindow(this.DialogWindowID);
			changeFocus();
			try {

				super.sSelectFrame(iframelocator);
				text = super.zGetHtml(locator);

				logger.info("DisplayMail.zGetBody(" + iframelocator + ", " + locator + ") = " + text);

			} finally {
				this.sSelectFrame("relative=top");
			}

		} finally {
			super.sSelectWindow(MainWindowID);
			super.sWindowFocus();
		}

		return (text);

	}

	public boolean sIsElementPresent(String locator) throws HarnessException {
		logger.info(myPageName() + " sIsElementPresent(" + locator + ")");

		boolean present = false;

		try {
			super.sSelectWindow(this.DialogWindowID);
			changeFocus();

			present = super.sIsElementPresent(locator);

		} finally {
			super.sSelectWindow(MainWindowID);
			super.sWindowFocus();
		}

		return (present);
	}

	public boolean zIsVisiblePerPosition(String locator, int leftLimit, int topLimit) throws HarnessException {

		logger.info(myPageName() + " zIsVisiblePerPosition(" + locator + ", " + leftLimit + ", " + topLimit + ")");

		boolean present = false;

		try {
			super.sSelectWindow(this.DialogWindowID);
			changeFocus();

			present = super.zIsVisiblePerPosition(locator, leftLimit, topLimit);

		} finally {
			super.sSelectWindow(MainWindowID);
			super.sWindowFocus();
		}

		return (present);
	}

	public int sGetElementPositionLeft(String locator) throws HarnessException {
		logger.info(myPageName() + " sGetElementPositionLeft(" + locator + ")");

		try {
			super.sSelectWindow(this.DialogWindowID);
			changeFocus();

			int n = super.sGetElementPositionLeft(locator);
			return (n);

		} finally {
			super.sSelectWindow(MainWindowID);
			super.sWindowFocus();
		}

	}

	public int sGetElementPositionTop(String locator) throws HarnessException {
		logger.info(myPageName() + " sGetElementPositionTop(" + locator + ")");

		try {
			super.sSelectWindow(this.DialogWindowID);
			changeFocus();

			int n = super.sGetElementPositionTop(locator);
			return (n);

		} finally {
			super.sSelectWindow(MainWindowID);
			super.sWindowFocus();
		}

	}

	public void sFocus(String locator) throws HarnessException {
		logger.info(myPageName() + " sFocus(" + locator + ")");

		try {
			super.sSelectWindow(this.DialogWindowID);
			DoChangeWindowFocus = true;
			changeFocus();
			super.sFocus(locator);

		} finally {
			super.sSelectWindow(MainWindowID);
			super.sWindowFocus();
		}

	}

	public void sMouseDown(String locator) throws HarnessException {
		logger.info(myPageName() + " sMouseDown(" + locator + ")");

		try {
			super.sSelectWindow(this.DialogWindowID);
			changeFocus();
			super.sMouseDown(locator);

		} finally {
			super.sSelectWindow(MainWindowID);
			super.sWindowFocus();
		}

	}

	public void sMouseUp(String locator) throws HarnessException {
		logger.info(myPageName() + " sMouseUp(" + locator + ")");

		try {
			super.sSelectWindow(this.DialogWindowID);
			changeFocus();
			super.sMouseUp(locator);

		} finally {
			super.sSelectWindow(MainWindowID);
			super.sWindowFocus();
		}

	}

	public void sMouseDownAt(String locator, String coord) throws HarnessException {
		logger.info(myPageName() + " sMouseDownAt(" + locator + ", " + coord + ")");

		try {
			super.sSelectWindow(this.DialogWindowID);
			changeFocus();
			super.sMouseDownAt(locator, coord);

		} finally {
			super.sSelectWindow(MainWindowID);
			super.sWindowFocus();
		}

	}

	public void sMouseUpAt(String locator, String coord) throws HarnessException {
		logger.info(myPageName() + " sMouseUpAt(" + locator + ", " + coord + ")");

		try {
			super.sSelectWindow(this.DialogWindowID);
			changeFocus();
			super.sMouseUpAt(locator, coord);

		} finally {
			super.sSelectWindow(MainWindowID);
			super.sWindowFocus();
		}

	}

	public void sClick(List<String> locators) throws HarnessException {
		logger.info(myPageName() + " sClick(" + Arrays.toString(locators.toArray()) + ")");

		try {
			super.sSelectWindow(this.DialogWindowID);
			changeFocus();

			for (String locator : locators) {
				sClick(locator);
			}

		} finally {
			super.sSelectWindow(MainWindowID);
			super.sWindowFocus();
		}

	}

	public void zClickAt(String locator, String coord) throws HarnessException {
		logger.info(myPageName() + " zClickAt(" + locator + ", " + coord + ")");

		try {
			super.sSelectWindow(this.DialogWindowID);

			if (!super.sIsElementPresent(locator))
				throw new HarnessException("locator not present: " + locator);

			try {
				logger.info("...WebDriver...moveToElement:click()");
				final WebElement we = getElement(locator);
				final Actions builder = new Actions(webDriver());
				Action action = builder.moveToElement(we).click(we).build();
				action.perform();
			} catch (Exception ex) {
				throw new HarnessException("Unable to clickAt on locator " + locator, ex);
			}

		} finally {
			super.sSelectWindow(MainWindowID);
			super.sWindowFocus();
		}
	}

	public void zTypeCharacters(String characters) throws HarnessException {
		logger.info(myPageName() + " zTypeCharacters()");

		try {

			super.sSelectWindow(this.DialogWindowID);
			super.zKeyboard.zTypeCharacters(characters);

		} finally {
			super.zSelectWindow(MainWindowID);
			super.sWindowFocus();
		}

	}

	public void zKeyDown(String keyCode) throws HarnessException {
		logger.info(myPageName() + " zKeyDown()");

		try {

			super.sSelectWindow(this.DialogWindowID);
			super.sWindowFocus();

			super.zKeyDown(keyCode);

		} finally {
			super.zSelectWindow(MainWindowID);
			super.sWindowFocus();
		}

	}

	public void zCloseWindow() throws HarnessException {
		logger.info(myPageName() + " zCloseWindow()");

		try {

			Set<String> windows = webDriver().getWindowHandles();
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

			if (this.DialogWindowID == null || this.DialogWindowID.equals("null")) {
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
				if (window.getTitle().contains(title) && !window.getTitle().contains("Zimbra: Inbox")
						&& !window.getTitle().contains("Zimbra: Contacts")
						&& !window.getTitle().contains("Zimbra: Calendar")
						&& !window.getTitle().contains("Zimbra: Tasks")
						&& !window.getTitle().contains("Zimbra: Briefcase")
						&& !window.getTitle().contains("Zimbra: Preferences")) {
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

	public void zWaitForBusyOverlay() throws HarnessException {
		logger.info(myPageName() + " zWaitForBusyOverlay()");

		try {

			super.sSelectWindow(this.DialogWindowID);
			super.sWaitForCondition("selenium.browserbot.getUserWindow().top.appCtxt.getShell().getBusy()==false");

		} finally {
			super.zSelectWindow(MainWindowID);
		}

	}

	public void zSetWindowTitle(String title) throws HarnessException {
		DialogWindowTitle = title;
	}

	public void zSetWindowID(String id) throws HarnessException {
		this.DialogWindowID = id;
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

		for (String title : super.sGetAllWindowTitles()) {
			logger.info("Window title: " + title);
			if (title.toLowerCase().contains(DialogWindowTitle.toLowerCase())) {
				DialogWindowID = title;
				logger.info("zIsActive() = true ... title = " + DialogWindowID);
				return (true);
			}
		}

		logger.info("zIsActive() = false");
		return (false);

	}

}
