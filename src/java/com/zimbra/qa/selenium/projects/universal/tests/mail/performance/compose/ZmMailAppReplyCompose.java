/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.mail.performance.compose;

import java.io.File;
import java.util.HashMap;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.LmtpInject;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.performance.PerfKey;
import com.zimbra.qa.selenium.framework.util.performance.PerfMetrics;
import com.zimbra.qa.selenium.framework.util.performance.PerfToken;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCore;
import com.zimbra.qa.selenium.projects.universal.pages.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.universal.pages.mail.FormMailNew;

public class ZmMailAppReplyCompose extends UniversalCore {

	public ZmMailAppReplyCompose() {
		logger.info("New " + ZmMailAppReplyCompose.class.getCanonicalName());

		super.startingPage = app.zPageMail;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 7525760124523255182L;
			{
				put("zimbraPrefComposeFormat", "text");
			}
		};

	}

	@Test (description = "Measure the time to load reply-compose  window for simple message", groups = { "performance", "L4" })
	public void ZmMailAppReplyCompose_01() throws HarnessException {

		String mime = ConfigProperties.getBaseDirectory() + "/data/public/mime/email02/mime01.txt";
		String subject = "Subject13155016716713";

		LmtpInject.injectFile(app.zGetActiveAccount(), new File(mime));
		
		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmMailAppCompose, "Load Reply-Compose window for simple conversation");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		//Click Reply from tool bar
		app.zPageMail.zToolbarPressButton(Button.B_REPLY);

		PerfMetrics.waitTimestamp(token);

	}
	
	
	@Test (description = "Measure the time to load reply-compose  window for large conversation", groups = { "performance", "L4" })
	public void ZmMailAppReplyCompose_02() throws HarnessException {

		String mime = ConfigProperties.getBaseDirectory() + "/data/public/mime/largeconversation_mime.txt";
		String subject = "RESOLVED BUGS";

		LmtpInject.injectFile(app.zGetActiveAccount(), new File(mime));
		
		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmMailAppCompose, "Load Reply-Compose window for large conversation");
		
		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		//Click Reply from tool bar
		app.zPageMail.zToolbarPressButton(Button.B_REPLY);

		PerfMetrics.waitTimestamp(token);
	}

	@Test (description = "Measure the time to load reply-compose window for invite conversation", groups = { "performance", "L4" })
	public void ZmMailAppReplyCompose_03() throws HarnessException {

		String mime = ConfigProperties.getBaseDirectory() + "/data/public/mime/Invite_Message.txt";
		String subject = "Test Edit Reply";

		LmtpInject.injectFile(app.zGetActiveAccount(), new File(mime));

		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmMailAppCompose, "Load Reply-Compose window for invite conversation");

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail)app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Click Accept -> Edit Reply , which will open a new reply compose
		@SuppressWarnings("unused")
		FormMailNew editReply = (FormMailNew)display.zPressButtonPulldown(Button.B_ACCEPT, Button.O_ACCEPT_EDIT_REPLY);
		
		PerfMetrics.waitTimestamp(token);
	}
	
	@Test (description = "Measure the time to load reply-compose window for invite conversation with 7mb attachment", groups = { "performance", "L4" })
	public void ZmMailAppReplyCompose_04() throws HarnessException {

		String mime = ConfigProperties.getBaseDirectory() + "/data/public/mime/inviteMessageWith7MBAttachment.txt";
		String subject = "Invite Message with 7 MB attachment";

		LmtpInject.injectFile(app.zGetActiveAccount(), new File(mime));

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmMailAppCompose, "Load Reply-Compose window for invite conversation with 7mb attachment");

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail)app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Click Accept -> Edit Reply , which will open a new reply compose
		@SuppressWarnings("unused")
		FormMailNew editReply = (FormMailNew)display.zPressButtonPulldown(Button.B_ACCEPT, Button.O_ACCEPT_EDIT_REPLY);

		PerfMetrics.waitTimestamp(token);
	}
	
}

