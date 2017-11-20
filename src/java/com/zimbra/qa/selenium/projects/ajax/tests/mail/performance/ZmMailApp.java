/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.performance;

import java.io.File;
import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.framework.util.performance.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.PageLogin.Locators;

public class ZmMailApp extends AjaxCore {

	public ZmMailApp() {
		logger.info("New "+ ZmMailApp.class.getCanonicalName());

		super.startingPage = app.zPageLogin;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 7525760124523255182L; {
			put("zimbraPrefGroupMailBy", "message");
			put("zimbraPrefMessageViewHtmlPreferred", "TRUE");
		}};
	}


	@Test (description = "Measure the time to load the mail app, message view, initial load",
			groups = { "performance", "deprecated" })

	public void ZmMailApp_01() throws HarnessException {

		// Fill out the login page
		app.zPageLogin.zSetLoginName(ZimbraAccount.AccountZCS().EmailAddress);
		app.zPageLogin.zSetLoginPassword(ZimbraAccount.AccountZCS().Password);

		PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmMailApp, "Load the mail app, message view, initial load");

		// Click the Login button
		app.zPageLogin.sClick(Locators.zBtnLogin);
		PerfMetrics.waitTimestamp(token);

		// Wait for the app to load
		app.zPageMain.zWaitForActive();
	}


	@Test (description = "Measure the time to load the mail app, message view, 1 message",
			groups = { "performance", "deprecated" })

	public void ZmMailApp_02() throws HarnessException {

		String mime = ConfigProperties.getBaseDirectory() + "/data/public/mime/email02/mime01.txt";
		LmtpInject.injectFile(ZimbraAccount.AccountZCS(), new File(mime));

		// Fill out the login page
		app.zPageLogin.zSetLoginName(ZimbraAccount.AccountZCS().EmailAddress);
		app.zPageLogin.zSetLoginPassword(ZimbraAccount.AccountZCS().Password);

		PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmMailApp, "Load the mail app, message view, 1 message");

		// Click the Login button
		app.zPageLogin.sClick(Locators.zBtnLogin);
		PerfMetrics.waitTimestamp(token);

		// Wait for the app to load
		app.zPageMain.zWaitForActive();
	}


	@Test (description = "Measure the time to load the mail app, message view, 100 messages",
			groups = { "performance", "deprecated" })

	public void ZmMailApp_03() throws HarnessException {

		String mime = ConfigProperties.getBaseDirectory() + "/data/public/mime/email03";
		LmtpInject.injectFile(ZimbraAccount.AccountZCS(), new File(mime));

		// Fill out the login page
		app.zPageLogin.zSetLoginName(ZimbraAccount.AccountZCS().EmailAddress);
		app.zPageLogin.zSetLoginPassword(ZimbraAccount.AccountZCS().Password);

		PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmMailApp, "Load the mail app, message view, 100 messages");

		// Click the Login button
		app.zPageLogin.sClick(Locators.zBtnLogin);
		PerfMetrics.waitTimestamp(token);

		// Wait for the app to load
		app.zPageMain.zWaitForActive();
	}
}