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

package com.zimbra.qa.selenium.projects.universal.tests.mail.compose.formatting;

import java.io.File;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.universal.ui.mail.DisplayMail.Field;

public class ViewMail extends PrefGroupMailByMessageTest {

	public ViewMail() throws HarnessException {
		logger.info("New " + ViewMail.class.getCanonicalName());
	}

	@Test (description = "View a message with Excel data formatting", 
			groups = { "smoke", "L1" })
	
	public void ViewMail_01() throws HarnessException {

		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Excel_Data_Formatting_Mime.txt";
		final String subject = "Test Excel Data Formatting";

		LmtpInject.injectFile(app.zGetActiveAccount(), new File(mimeFile));

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Verify Excel Table border and its contents
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		ZAssert.assertTrue(app.zPageMail.zVerifyDisplayMailElement("div[id='zimbraEditorContainer'] div table[border='1']"), "Verify Excel Table border ");
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), "ID", "Verify the body content matches");
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), "Fname", "Verify the body content matches");
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), "Lname", "Verify the body content matches");
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), "Test1", "Verify the body content matches");
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), "Test2", "Verify the body content matches");
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), "Test3", "Verify the body content matches");
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), "Test4", "Verify the body content matches");
	}
}