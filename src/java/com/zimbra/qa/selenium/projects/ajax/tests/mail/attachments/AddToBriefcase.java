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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.attachments;

import java.util.List;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.*;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail;

public class AddToBriefcase extends SetGroupMailByMessagePreference {

	private String subject;
	private String filename;
	private ZimbraAccount account;

	public AddToBriefcase() throws HarnessException {
		logger.info("New "+ AddToBriefcase.class.getCanonicalName());
		super.startingPage =  app.zPageMail;
		super.startingAccountPreferences.put("zimbraPrefBriefcaseReadingPaneLocation", "bottom");
	}


	@Test (description = "Add JPG attachment to Briefcase when viewing email in the current window",
			groups = { "functional", "L2" })

	public void AddToBriefcase_01() throws HarnessException {

		// -- Data Setup
		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email09/mime.txt";
		subject = "subject03431362517016470";
		filename = "screenshot.JPG";
		account = app.zGetActiveAccount();

		FolderItem folder = FolderItem.importFromSOAP(account, FolderItem.SystemFolder.Briefcase);

		// Inject the sample mime
		injectMessage(app.zGetActiveAccount(), mimeFile);

		// Double check that there is an attachment
		account.soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ subject +")</query>"
			+	"</SearchRequest>");
		String id = account.soapSelectValue("//mail:m", "id");

		account.soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail' >"
			+		"<m id='"+ id +"'/>"
			+	"</GetMsgRequest>");
		Element[] nodes = account.soapSelectNodes("//mail:mp[@cd='attachment']");
		ZAssert.assertGreaterThan(nodes.length, 0, "Verify the message has the attachment");

		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		AttachmentItem item = null;
		List<AttachmentItem> items = display.zListGetAttachments();
		for (AttachmentItem i : items) {
			if ( i.getAttachmentName().equals(filename)) {
				item = i;
				break;
			}
		}
		ZAssert.assertNotNull(item, "Verify one attachment is in the message");

		// Click to "Briefcase"
		DialogAddToBriefcase dialog = (DialogAddToBriefcase)display.zListAttachmentItem(Button.B_BRIEFCASE, item);
		dialog.zChooseBriefcaseFolder(folder.getId());
		dialog.zPressButton(Button.B_OK);

		// Verification
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='document'>"
			+		"<query>"+ filename +"</query>"
			+	"</SearchRequest>");

		String name = account.soapSelectValue("//mail:doc", "name");

		// Verify the search response returns the file name
		ZAssert.assertNotNull(name,
			"Verify the search response returns the document name");

		// Verify saved to Briefcase file and mail attachment name are matched
		ZAssert.assertEquals(name, filename, "Verify saved to Briefcase mail attachment name through SOAP");
	}


	@Test (description = "Add txt attachment to Briefcase when viewing email in a separate window",
			groups = { "functional", "L3" })

	public void AddToBriefcase_02() throws HarnessException {

	    	// -- Data Setup
 		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/email05/mime01.txt";
 		subject = "subject151615738";
 		filename = "file.txt";
 		account = app.zGetActiveAccount();

 		FolderItem folder = FolderItem.importFromSOAP(account, FolderItem.SystemFolder.Briefcase);

 		// Inject the sample mime
 		injectMessage(app.zGetActiveAccount(), mimeFile);

 		// Double check that there is an attachment
 		account.soapSend(
 				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
 			+		"<query>subject:("+ subject +")</query>"
 			+	"</SearchRequest>");
 		String id = account.soapSelectValue("//mail:m", "id");

 		account.soapSend(
 				"<GetMsgRequest xmlns='urn:zimbraMail' >"
 			+		"<m id='"+ id +"'/>"
 			+	"</GetMsgRequest>");
 		Element[] nodes = account.soapSelectNodes("//mail:mp[@cd='attachment']");
 		ZAssert.assertGreaterThan(nodes.length, 0, "Verify the message has the attachment");

 		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

 		// Select the message so that it shows in the reading pane
 		DisplayMail display = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
	 	AttachmentItem item = null;
	 	List<AttachmentItem> items = display.zListGetAttachments();
	 	for (AttachmentItem i : items) {
	 		if ( i.getAttachmentName().equals(filename)) {
	 			item = i;
	 			break;
	 		}
	 	}
	 	ZAssert.assertNotNull(item, "Verify one attachment is in the message");

	 	//Open message in a separate window
		SeparateWindow window = (SeparateWindow)app.zPageMail.zToolbarPressButton(Button.B_LAUNCH_IN_SEPARATE_WINDOW);
		String windowTitle = "Zimbra: " + subject;

		try {

			window.zWaitForActive();
			app.zPageMail.zSelectWindow("_blank");
			SleepUtil.sleepMedium();
			app.zPageCalendar.zWaitForElementAppear("css=div[id*=zv__MSG__MSG][id*=_attLinks_]");

			DialogAddToBriefcase dialog = (DialogAddToBriefcase)app.zPageMail.zToolbarPressButton(Button.B_BRIEFCASE);
			dialog.zChooseBriefcaseFolder(folder.getId());
			dialog.zPressButton(Button.B_OK);
			SleepUtil.sleepLong(); //sometime client takes longer time to add the file

        } finally {
        	app.zPageMain.zCloseWindow(window, windowTitle, app);
       	}

		// Verification
		account.soapSend(
			"<SearchRequest xmlns='urn:zimbraMail' types='document'>"
			+		"<query>"+ filename +"</query>"
			+	"</SearchRequest>");

		String name = account.soapSelectValue("//mail:doc", "name");

		// Verify the search response returns the file name
		ZAssert.assertNotNull(name, "Verify the search response returns the document name");

		// Verify saved to Briefcase file and mail attachment name are matched
		ZAssert.assertEquals(name, filename, "Verify saved to Briefcase mail attachment name through SOAP");
	}


	@AfterMethod(groups = { "always" })
	public void afterMethod() throws HarnessException {
		logger.info("AfterMethod cleanup ...");

		// Delete file upon test completion
		app.zPageBriefcase.deleteFileByName(filename);

		// Delete message
		MailItem received = MailItem.importFromSOAP(account, "in:inbox subject:("+ subject +")");
		account.soapSend("<ItemActionRequest xmlns='urn:zimbraMail'>"
						+ "<action op='move' id='"+ received.getId() +"' l='"+ FolderItem.importFromSOAP(account, SystemFolder.Trash).getId() +"'/>"
						+ "</ItemActionRequest>");
	}
}