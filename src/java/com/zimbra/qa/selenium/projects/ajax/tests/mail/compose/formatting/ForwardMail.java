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
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;

public class ForwardMail extends PrefGroupMailByMessageTest {

	public ForwardMail() {
		logger.info("New "+ ForwardMail.class.getCanonicalName());		
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
	}
	
	@Test(	description = "Forward an Excel formatting data message  and verify its formatting",
			groups = { "smoke" })
	public void forwardHtmlMail() throws HarnessException {
		
		final String mimeFile = ZimbraSeleniumProperties.getBaseDirectory()
				+ "/data/public/mime/Excel_Data_Formatting_Mime.txt";
		final String subject = "Test Excel Data Formatting";

		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mimeFile));

		// Refresh current view
		app.zPageMail.zVerifyMailExists(subject);

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent("css=div[id='zimbraEditorContainer'] div table[border='1']"), "Verify Excel Table border ");			
		
		// Forward the item
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_FORWARD);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");
		
		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.AccountB().EmailAddress);
		
		// Send the message
		mailform.zSubmit();
		
		// From the receiving end, verify the message details
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:("+subject +")");
		ZAssert.assertStringContains(received.dSubject, "Fwd", "Verify the subject field contains the 'Fwd' prefix");
		
		// Logout and login to pick up the changes
		app.zPageLogin.zNavigateTo();
		this.startingPage.zNavigateTo();
		
		// Click in sent
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Sent));
		
		// Verify Excel Table border
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent("css=div[id='zimbraEditorContainer'] div table[border='1']"), "Verify Excel Table border ");
		
		//Verify Excel Table border and its contents
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent("css=div[id='zimbraEditorContainer'] div table[border='1']"), "Verify Excel Table border ");
		ZAssert.assertStringContains(actual.zGetMailProperty(com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail.Field.Body), "ID", "Verify the body content matches");
		ZAssert.assertStringContains(actual.zGetMailProperty(com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail.Field.Body), "Fname", "Verify the body content matches");
		ZAssert.assertStringContains(actual.zGetMailProperty(com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail.Field.Body), "Lname", "Verify the body content matches");
		ZAssert.assertStringContains(actual.zGetMailProperty(com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail.Field.Body), "Test1", "Verify the body content matches");
		ZAssert.assertStringContains(actual.zGetMailProperty(com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail.Field.Body), "Test2", "Verify the body content matches");
		ZAssert.assertStringContains(actual.zGetMailProperty(com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail.Field.Body), "Test3", "Verify the body content matches");
		ZAssert.assertStringContains(actual.zGetMailProperty(com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail.Field.Body), "Test4", "Verify the body content matches");
	}
}