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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.conversation.conversations;

import java.io.*;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;

public class ReadMore extends PrefGroupMailByConversationTest {

	public ReadMore() throws HarnessException {
		logger.info("New "+ ReadMore.class.getCanonicalName());
	}


	@Test( description = "Use the 'Read More' button to scroll through the conversation content",
			groups = { "functional", "L2" })

	public void ViewMail_01() throws HarnessException {

		final String subject = "ReadMore13674340693103";
		final String mimeFolder = ConfigProperties.getBaseDirectory() + "/data/public/mime/email11";

		LmtpInject.injectFile(app.zGetActiveAccount(), new File(mimeFolder));

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Click on the "Read More" button
		app.zPageMail.zToolbarPressButton(Button.B_READMORE);

		// TODO: not sure how to verify that the scrollbar has moved?
	}
}