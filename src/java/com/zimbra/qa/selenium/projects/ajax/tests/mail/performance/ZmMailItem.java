/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.performance.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.LmtpInject;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.*;

public class ZmMailItem extends AjaxCore {

	@SuppressWarnings("serial")
	public ZmMailItem() throws HarnessException {
		logger.info("New "+ ZmMailItem.class.getCanonicalName());

		super.startingPage = app.zPageMail;
		super.startingAccountPreferences = new HashMap<String, String>() {{
			put("zimbraPrefGroupMailBy", "message");
			put("zimbraPrefMessageViewHtmlPreferred", "FALSE");
		}};
	}


	@Test (description = "Measure the performance for preview pane, text message, initial load",
			groups = { "performance", "deprecated" })

	public void ZmMailItem_01() throws HarnessException {

		String mime = ConfigProperties.getBaseDirectory() + "/data/public/mime/email02/mime01.txt";
		String subject = "Subject13155016716713";

		LmtpInject.injectFile(app.zGetActiveAccount(), new File(mime));

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmMailItem, "Load preview pane, text message, initial load");
		app.zPageMail.sClickAt("css=ul[id='zl__TV-main__rows'] li[id^='zli__TV-main__']  div span[id$='__su']:contains('"+subject+"')","");
		PerfMetrics.waitTimestamp(token);
	}


	@Test (description = "Measure the performance for preview pane, text message, 1 message",
			groups = { "performance", "deprecated" })

	public void ZmMailItem_02() throws HarnessException {

		String mime = ConfigProperties.getBaseDirectory() + "/data/public/mime/email02/mime01.txt";
		String subject = "Subject13155016716713";

		LmtpInject.injectFile(app.zGetActiveAccount(), new File(mime));

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmMailItem, "Load preview pane, text message, 1 message");
		app.zPageMail.sClickAt("css=ul[id='zl__TV-main__rows'] li[id^='zli__TV-main__']  div span[id$='__su']:contains('"+subject+"')","");
		PerfMetrics.waitTimestamp(token);
	}


	@Test (description = "Measure the performance for preview pane,  message with 1 attachment",
			groups = { "performance", "deprecated" })

	public void ZmMailItem_03() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email05/mime01.txt";
		final String subject = "subject151615738";
		LmtpInject.injectFile(app.zGetActiveAccount(), new File(mimeFile));

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmMailItem, "Load preview pane, text message, 1 attachment");
		app.zPageMail.sClickAt("css=ul[id='zl__TV-main__rows'] li[id^='zli__TV-main__']  div span[id$='__su']:contains('"+subject+"')","");

		PerfMetrics.waitTimestamp(token);
	}


	@Test (description = "Measure the performance for preview pane,  message with 3 attachment",
			groups = { "performance", "deprecated" })

	public void ZmMailItem_04() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email05/mime02.txt";
		final String subject = "subject151111738";
		LmtpInject.injectFile(app.zGetActiveAccount(), new File(mimeFile));

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");
		PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmMailItem, "Load preview pane, text message, 3 attachments");
		app.zPageMail.sClickAt("css=ul[id='zl__TV-main__rows'] li[id^='zli__TV-main__']  div span[id$='__su']:contains('"+subject+"')","");

		PerfMetrics.waitTimestamp(token);
	}
}