package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.formatting;



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


import java.io.File;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail.Field;



public class ReplyMail extends PrefGroupMailByMessageTest {

	public ReplyMail() {
		logger.info("New "+ ReplyMail.class.getCanonicalName());		

		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");

	}

	@Test(	description = "Reply an Excel formatting data message  and verify its formatting",
			groups = { "smoke" })
	public void ReplyHtmlMail() throws HarnessException {

		///
		final String mimeFile = ZimbraSeleniumProperties.getBaseDirectory()
				+ "/data/public/mime/Excel_Data_Formatting_Mime.txt";
		final String subject = "Test Excel Data Formatting";

		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(
				mimeFile));

		// Refresh current view
		app.zPageMail.zVerifyMailExists(subject);

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent("css=div[id='zimbraEditorContainer'] div table[border='1']"), "Verify Excel Table border ");		


		// Reply the item
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_REPLY);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");
		// Send the message
		mailform.zSubmit();


		// From the receiving end, verify the message details
		// Need 'in:sent' to seprate the message from the inbox message
		MailItem received = MailItem.importFromSOAP(app.zGetActiveAccount(),"in:sent subject:("+subject +")");
		ZAssert.assertStringContains(received.dSubject, "Re", "Verify the subject field contains the 'Re' prefix");

		// Logout and login to pick up the changes
		app.zPageLogin.zNavigateTo();
		this.startingPage.zNavigateTo();

		// Refresh current view
		app.zPageMail.zVerifyMailExists(subject);

		// Click in sent
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Sent));

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		//Verify Excel Table border

		ZAssert.assertTrue(app.zPageMail.sIsElementPresent("css=div[id='zimbraEditorContainer'] div table[border='1']"), "Verify Excel Table border ");

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

