/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.conversation.performance;

import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.performance.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.*;

public class ZmConv extends AjaxCore {

	@SuppressWarnings("serial")
	public ZmConv() throws HarnessException {
		logger.info("New "+ ZmConv.class.getCanonicalName());

		super.startingPage = app.zPageMail;
		super.startingAccountPreferences = new HashMap<String, String>() {{
			put("zimbraPrefGroupMailBy", "conversation");
			put("zimbraPrefMessageViewHtmlPreferred", "FALSE");
		}};
	}


	@Test (description = "Measure the performance for conversation view, preview pane, text message, initial load",
			groups = { "performance","deprecated" })

	public void ZmMailItem_01() throws HarnessException {

		String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/conversation02";
		String subject = "Conversation13155016716714";

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message present in current view");

		PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmConv, "Load preview pane, text message, initial load");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		PerfMetrics.waitTimestamp(token);
	}
}