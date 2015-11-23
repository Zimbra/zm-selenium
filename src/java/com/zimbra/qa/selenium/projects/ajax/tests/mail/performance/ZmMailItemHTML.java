/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.performance;

import java.io.File;
import java.util.HashMap;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.performance.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.LmtpInject;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.ajax.core.*;


public class ZmMailItemHTML extends AjaxCommonTest {

	
	@SuppressWarnings("serial")
	public ZmMailItemHTML() throws HarnessException {
		logger.info("New "+ ZmMailItemHTML.class.getCanonicalName());

		super.startingPage = app.zPageMail;


		super.startingAccountPreferences = new HashMap<String, String>() {{
			put("zimbraPrefGroupMailBy", "message");
			put("zimbraPrefMessageViewHtmlPreferred", "TRUE");
		}};


	}
	
	
	@Test(	description = "Measure the performance for preview pane, html message, initial load",
			groups = { "performance" })
	public void ZmMailItem_01() throws HarnessException {
		
		String mime = ZimbraSeleniumProperties.getBaseDirectory() + "/data/public/mime/email02/mime01.txt";
		String subject = "Subject13155016716713";

		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mime));

		
		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmMailItem, "Load preview pane, html message, initial load");

		// Select the message so that it shows in the reading pane
		//app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		app.zPageMail.zClickAt("css=ul[id='zl__TV-main__rows'] li[id^='zli__TV-main__']  div span[id$='__su']:contains('"+subject+"')","");

		PerfMetrics.waitTimestamp(token);

	}

	@Test(	description = "Measure the performance for preview pane, html message, 1 message",
			groups = { "performance" })
	public void ZmMailItem_02() throws HarnessException {
		
		String mime = ZimbraSeleniumProperties.getBaseDirectory() + "/data/public/mime/email02/mime01.txt";
		String subject = "Subject13155016716713";

		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mime));
		
		// Refresh current view
		app.zPageMail.zVerifyMailExists(subject);

		PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmMailItem, "Load preview pane, html message, 1 message");

		// Select the message so that it shows in the reading pane
		//app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		app.zPageMail.zClickAt("css=ul[id='zl__TV-main__rows'] li[id^='zli__TV-main__']  div span[id$='__su']:contains('"+subject+"')","");

		PerfMetrics.waitTimestamp(token);

	}

}
