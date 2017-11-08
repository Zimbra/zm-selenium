/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.mountpoints.viewer;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogShare;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogShare.ShareMessageType;

public class CreateShare extends AjaxCommonTest {

	public CreateShare() {
		logger.info("New " + CreateShare.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Test (description = "Share calendar folder with viewer rights",
			groups = { "smoke", "L1" })

	public void CreateShare_01() throws HarnessException {

		String calendarname = "calendar" + ConfigProperties.getUniqueString();

		// Create a calendar
		app.zGetActiveAccount().soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='"
				+ calendarname + "' l='1' view='appointment'/>" + "</CreateFolderRequest>");

		// Make sure the folder was created on the server
		FolderItem calendar = FolderItem.importFromSOAP(app.zGetActiveAccount(), calendarname);
		ZAssert.assertNotNull(calendar, "Verify the folder exists on the server");

		// Need to do Refresh to see folder in the list
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Right click on folder, select "Share"
		DialogShare dialog = (DialogShare) app.zTreeCalendar.zTreeItem(Action.A_RIGHTCLICK, Button.B_SHARE, calendar);
		ZAssert.assertNotNull(dialog, "Verify the sharing dialog pops up");

		// Use defaults for all options
		dialog.zSetEmailAddress(ZimbraAccount.Account1().EmailAddress);

		// Send it
		dialog.zPressButton(Button.B_OK);

		// Make sure that AccountA now has the share
		ZimbraAccount.Account1().soapSend("<GetShareInfoRequest xmlns='urn:zimbraAccount'>" + "<grantee type='usr'/>"
				+ "<owner by='name'>" + app.zGetActiveAccount().EmailAddress + "</owner>" + "</GetShareInfoRequest>");

		String ownerEmail = ZimbraAccount.Account1().soapSelectValue(
				"//acct:GetShareInfoResponse//acct:share[@folderPath='/" + calendarname + "']", "ownerEmail");
		ZAssert.assertEquals(ownerEmail, app.zGetActiveAccount().EmailAddress, "Verify the owner of the shared folder");

		String rights = ZimbraAccount.Account1().soapSelectValue(
				"//acct:GetShareInfoResponse//acct:share[@folderPath='/" + calendarname + "']", "rights");
		ZAssert.assertEquals(rights, "r", "Verify the rights are 'read only'");

		String granteeType = ZimbraAccount.Account1().soapSelectValue(
				"//acct:GetShareInfoResponse//acct:share[@folderPath='/" + calendarname + "']", "granteeType");
		ZAssert.assertEquals(granteeType, "usr", "Verify the grantee type is 'user'");

	}


	@Test (description = "Share folder with viewer rights and Do not send mail about the share",
			groups = { "functional", "L2" })

	public void CreateShare_02() throws HarnessException {

		String calendarname = "calendar" + ConfigProperties.getUniqueString();

		// Create a calendar
		app.zGetActiveAccount().soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='"
				+ calendarname + "' l='1' view='appointment'/>" + "</CreateFolderRequest>");

		// Make sure the folder was created on the server
		FolderItem calendar = FolderItem.importFromSOAP(app.zGetActiveAccount(), calendarname);
		ZAssert.assertNotNull(calendar, "Verify the folder exists on the server");

		// Need to do Refresh to see folder in the list
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		DialogShare dialog = (DialogShare) app.zTreeCalendar.zTreeItem(Action.A_RIGHTCLICK, Button.B_SHARE, calendar);
		ZAssert.assertNotNull(dialog, "Verify the dialog opened");

		dialog.zSetEmailAddress(ZimbraAccount.Account10().EmailAddress);
		dialog.zSetMessageType(ShareMessageType.DoNotSendMsg, null);
		dialog.zPressButton(Button.B_OK);

		// Search for the mail in recepients inbox
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.Account10(),
				"subject:(" + (char) 34 + "Share Created" + (char) 58 + " " + calendarname + (char) 34 + ")");
		ZAssert.assertNull(received, "Verify no mail is received");
	}


	@Test (description = "Share folder with viewer rights and add a multiline note to it",
			groups = { "functional", "L2" })

	public void CreateShare_03() throws HarnessException {

		app.zPageMain.zRefreshMainUI();
		app.zPageCalendar.zNavigateTo();

		// Create a folder
		String firstLine = "First Line " + ConfigProperties.getUniqueString();
		String secondLine = "Second Line " + ConfigProperties.getUniqueString();
		String thirdLine = "Third Line " + ConfigProperties.getUniqueString();
		String calendarname = "calendar" + ConfigProperties.getUniqueString();

		// Create a calendar
		app.zGetActiveAccount().soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='"
				+ calendarname + "' l='1' view='appointment'/>" + "</CreateFolderRequest>");

		// Make sure the folder was created on the server
		FolderItem calendar = FolderItem.importFromSOAP(app.zGetActiveAccount(), calendarname);
		ZAssert.assertNotNull(calendar, "Verify the folder exists on the server");

		// Need to do Refresh to see folder in the list
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Rename the folder using context menu
		DialogShare dialog = (DialogShare) app.zTreeCalendar.zTreeItem(Action.A_RIGHTCLICK, Button.B_SHARE, calendar);
		ZAssert.assertNotNull(dialog, "Verify the dialog opened");

		// Add note to standard message, click OK
		dialog.zSetEmailAddress(ZimbraAccount.Account9().EmailAddress);

		dialog.zSetMessageType(ShareMessageType.AddNoteToStandardMsg, firstLine +"\n" + secondLine +"\n"+thirdLine);
		dialog.zPressButton(Button.B_OK);

		// Search for the mail in recepients inbox
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.Account9(),
				"subject:(" + (char) 34 + "Share Created" + (char) 58 + " " + calendarname + (char) 34 + ")");
		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, app.zGetActiveAccount().EmailAddress,
				"Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, ZimbraAccount.Account9().EmailAddress,
				"Verify the to field is correct");
		ZAssert.assertStringContains(received.dBodyText, firstLine + "\n" + secondLine + "\n" + thirdLine,
				"Verify the body field is correct");
	}
}