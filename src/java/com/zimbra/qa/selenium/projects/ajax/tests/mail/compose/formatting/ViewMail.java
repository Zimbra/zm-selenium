package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.formatting;

/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
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

import java.io.File;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail.Field;

public class ViewMail extends PrefGroupMailByMessageTest {

	boolean injected = false;



	public ViewMail() throws HarnessException {
		logger.info("New " + ViewMail.class.getCanonicalName());

	}

	@Test(description = "View a message with Excel data formatting", groups = { "smoke" })
	public void ViewMail_01() throws HarnessException {

		final String mimeFile = ZimbraSeleniumProperties.getBaseDirectory()
				+ "/data/public/mime/Excel_Data_Formatting_Mime.txt";
		final String subject = "Test Excel Data Formatting";

		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(
				mimeFile));

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);

		// Select the message so that it shows in the reading pane
		//	app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);


		//Verify Excel Table border and its contents

		ZAssert.assertTrue(app.zPageMail.sIsElementPresent("css=div[id='zimbraEditorContainer'] div table[border='1']"), "Verify Excel Table border ");
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), "ID", "Verify the body content matches");
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), "Fname", "Verify the body content matches");
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), "Lname", "Verify the body content matches");
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), "Test1", "Verify the body content matches");
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), "Test2", "Verify the body content matches");
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), "Test3", "Verify the body content matches");
		ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), "Test4", "Verify the body content matches");




	}

}
