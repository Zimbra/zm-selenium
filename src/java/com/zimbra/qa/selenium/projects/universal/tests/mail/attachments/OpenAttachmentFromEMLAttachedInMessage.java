/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.mail.attachments;

import java.io.File;
import java.io.IOException;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.SeparateWindowDisplayMail;

public class OpenAttachmentFromEMLAttachedInMessage extends PrefGroupMailByMessageTest {

	public OpenAttachmentFromEMLAttachedInMessage() throws HarnessException {
		logger.info("New "+ OpenAttachmentFromEMLAttachedInMessage.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraFeatureViewInHtmlEnabled", "TRUE");
	}

	@Test( description = "Bug 80447 - Open attachment from an eml attached in message", groups = { "functional", "L2" })

	public void OpenAttachmentInForwardEmail_01() throws HarnessException, IOException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email18/mime.txt";
		final String subject = "Print2PDF Error";
		final String subjecteml = "File conversion #27331";

		LmtpInject.injectFile(app.zGetActiveAccount(), new File(mimeFile));

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);	

		SeparateWindowDisplayMail window = null;
		String windowTitle = "Zimbra: " + subject;

		try {

			// Select eml attachment in mail			
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressButton(Button.B_EML_ATTACHEMENT);
			SleepUtil.sleepMedium();
			window.zSetWindowTitle(subjecteml);
			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");

			//Verify that doc file attachment is present under eml file attached in a mail
			ZAssert.assertTrue(window.sIsElementPresent("css=td a[id='zv__MSG__MSG-1_attLinks_2.1_main']:contains('fileconvE5cNFS.doc')"), "Verify .doc  present as attachment");
			ZAssert.assertTrue(window.sIsElementPresent("css=td a[id='zv__MSG__MSG-1_attLinks_2.1_html']:contains('Preview')"), "Verify preview button exist");

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}

	}

}
