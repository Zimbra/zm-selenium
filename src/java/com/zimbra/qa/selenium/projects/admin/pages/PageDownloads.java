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
package com.zimbra.qa.selenium.projects.admin.pages;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

/**
 * This class defines the Downloads page (click on "Downloads" in the header)
 *
 * @author Matt Rhoades
 *
 */
public class PageDownloads extends AbsTab {

	public static class Locators {

		public static final String TOOLS_AND_MIGRATION_ICON = "css=div.ImgToolsAndMigration";
		public static final String DOWNLOADS = "css=div[id^='zti__AppAdmin__magHV__download'][id$='div']";
		public static final String HOME = "Home";
		public static final String TOOLS_AND_MIGRATION = "Tools and Migration";
		public static final String DOWNLOAD = "Downloads";
		public static final String IndexHtmlTitleLocator = "css=title:contains('Downloads')";
		public static final String GoBackLink = "css=a:contains('Go back')";
	}

	public PageDownloads(AbsApplication application) {
		super(application);

		logger.info("new " + myPageName());

	}

	@Override
	public void zNavigateTo() throws HarnessException {

		if (zIsActive()) {

			return;
		}

		// Click on Tools and Migration -> Downloads
		sClickAt(Locators.TOOLS_AND_MIGRATION_ICON, "");
		zWaitForWorkInProgressDialogInVisible();
		if (sIsElementPresent(Locators.DOWNLOADS))
			;
		sClickAt(Locators.DOWNLOADS, "");
		zWaitForWorkInProgressDialogInVisible();

		zWaitForActive();
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the Admin Console is loaded in the browser
		if (!MyApplication.zIsLoaded())
			throw new HarnessException("Admin Console application is not active!");

		boolean present = sIsElementPresent("css=span:contains('" + Locators.TOOLS_AND_MIGRATION + "')");
		if (!present) {
			return (false);
		}

		boolean visible = zIsVisiblePerPosition("css=span:contains('" + Locators.TOOLS_AND_MIGRATION + "')", 0, 0);
		if (!visible) {
			logger.debug("isActive() visible = " + visible);
			return (false);
		}

		return (true);

	}

	@Override
	public AbsPage zListItem(Action action, String item) throws HarnessException {
		throw new HarnessException(myPageName() + " does not contain lists");
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String item) throws HarnessException {
		throw new HarnessException(myPageName() + " does not contain lists");
	}

	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption, String item) throws HarnessException {
		throw new HarnessException(myPageName() + " does not contain lists");
	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		throw new HarnessException(myPageName() + " does not contain a toolbar");
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		throw new HarnessException(myPageName() + " does not contain a toolbar");
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	/**
	 * Open http://server.com/zimbra/downloads/index.html
	 *
	 * @throws HarnessException
	 */
	public boolean zOpenIndexHTML(String title) throws HarnessException {
		boolean opened = true;
		String base = ConfigProperties.getBaseURL();
		String path = "/downloads/index.html";
		String id = ConfigProperties.getUniqueString();
		this.sOpenWindow(base + path, id);
		SleepUtil.sleepSmall();

		// Check for the presence of 404 - Not Found page
		if (sGetTitle().contains("404 - Not Found")) {
			opened = false;
			zGoBack();
		} else if (sGetTitle().contains(title)) {
			opened = true;
		}
		return opened;
	}

	public boolean zVerifyHeader(String header) throws HarnessException {
		if (this.sIsElementPresent("css=span:contains('" + header + "')"))
			return true;
		return false;
	}

	public int getAuthResponse(URL url) throws IOException {
		String userpassword = "admin" + ":" + "test123";
		String encodedAuthorization = new String(Base64.getEncoder().encode(userpassword.getBytes()));
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
		int code = connection.getResponseCode();
		return code;
	}

	public void zGoBack() throws HarnessException {
		logger.info("Download page is not opened. Navigating to previous page");
		this.sClick(Locators.GoBackLink);
		SleepUtil.sleepLong();
	}

}
