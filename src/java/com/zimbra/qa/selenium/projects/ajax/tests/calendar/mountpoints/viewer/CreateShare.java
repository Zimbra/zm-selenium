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

import java.awt.event.KeyEvent;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogShare;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogShare.ShareMessageType;

public class CreateShare extends CalendarWorkWeekTest  {

	public CreateShare() {
		logger.info("New "+ CreateShare.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	@Test( description = "Share calendar folder with viewer rights",
			groups = { "smoke" })
			
	public void CreateShare_01() throws HarnessException {
		
		String calendarname = "calendar" + ConfigProperties.getUniqueString();

		// Create a calendar
		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + calendarname +"' l='1' view='appointment'/>"
				+	"</CreateFolderRequest>");

		// Make sure the folder was created on the server
		FolderItem calendar = FolderItem.importFromSOAP(app.zGetActiveAccount(), calendarname);
		ZAssert.assertNotNull(calendar, "Verify the folder exists on the server");

		// Need to do Refresh to see folder in the list 
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		
		// Right click on folder, select "Share"
		DialogShare dialog = (DialogShare)app.zTreeCalendar.zTreeItem(Action.A_RIGHTCLICK, Button.B_SHARE, calendar);
		ZAssert.assertNotNull(dialog, "Verify the sharing dialog pops up");

		// Use defaults for all options
		dialog.zSetEmailAddress(ZimbraAccount.AccountA().EmailAddress);
		
		// Send it
		dialog.zClickButton(Button.B_OK);
		
		// Make sure that AccountA now has the share
		ZimbraAccount.AccountA().soapSend(
					"<GetShareInfoRequest xmlns='urn:zimbraAccount'>"
				+		"<grantee type='usr'/>"
				+		"<owner by='name'>"+ app.zGetActiveAccount().EmailAddress +"</owner>"
				+	"</GetShareInfoRequest>");
		
		// Example response:
		//    <GetShareInfoResponse xmlns="urn:zimbraAccount">
		//		<share granteeId="e3c083c5-102a-416e-bcf4-6d4c59197e20" ownerName="enus13191472607033" granteeDisplayName="enus13191472702505" ownerId="8d5589ff-0548-4562-8d1d-1a4f70e3ca7e" rights="r" folderPath="/folder13191472674374" view="contact" granteeType="usr" ownerEmail="enus13191472607033@testdomain.com" granteeName="enus13191472702505@testdomain.com" folderId="257"/>
	    //	  </GetShareInfoResponse>

		String ownerEmail = ZimbraAccount.AccountA().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/"+ calendarname +"']", "ownerEmail");
		ZAssert.assertEquals(ownerEmail, app.zGetActiveAccount().EmailAddress, "Verify the owner of the shared folder");
		
		String rights = ZimbraAccount.AccountA().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/"+ calendarname +"']", "rights");
		ZAssert.assertEquals(rights, "r", "Verify the rights are 'read only'");

		String granteeType = ZimbraAccount.AccountA().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/"+ calendarname +"']", "granteeType");
		ZAssert.assertEquals(granteeType, "usr", "Verify the grantee type is 'user'");

	}

	@Test( description = "Share folder with viewer rights and Do not send mail about the share", 
			groups = { "functional" })
	public void CreateShare_02() throws HarnessException {

		String calendarname = "calendar" + ConfigProperties.getUniqueString();

		// Create a calendar
		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + calendarname +"' l='1' view='appointment'/>"
				+	"</CreateFolderRequest>");

		// Make sure the folder was created on the server
		FolderItem calendar = FolderItem.importFromSOAP(app.zGetActiveAccount(), calendarname);
		ZAssert.assertNotNull(calendar, "Verify the folder exists on the server");

		// Need to do Refresh to see folder in the list 
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		DialogShare dialog = (DialogShare) app.zTreeCalendar.zTreeItem(Action.A_RIGHTCLICK, Button.B_SHARE, calendar);
		ZAssert.assertNotNull(dialog, "Verify the dialog opened");

		dialog.zSetEmailAddress(ZimbraAccount.Account10().EmailAddress);

		dialog.zSetMessageType(ShareMessageType.DoNotSendMsg, null);
		dialog.zClickButton(Button.B_OK);
		
		//Search for the mail in recepients inbox
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.Account10(), "subject:('Share Created')");
		ZAssert.assertNull(received, "Verify no mail is received");
	}

	@Test( description = "Share folder with viewer rights and add a multiline note to it.", 
			groups = { "functional" })
	public void CreateShare_03() throws HarnessException {
	
		// Create a folder
		String firstLine = "First Line " + ConfigProperties.getUniqueString();
		String secondLine = "Second Line " + ConfigProperties.getUniqueString();
		String thirdLine = "Third Line " + ConfigProperties.getUniqueString();		
		String calendarname = "calendar" + ConfigProperties.getUniqueString();

		// Create a calendar
		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + calendarname +"' l='1' view='appointment'/>"
				+	"</CreateFolderRequest>");

		// Make sure the folder was created on the server
		FolderItem calendar = FolderItem.importFromSOAP(app.zGetActiveAccount(), calendarname);
		ZAssert.assertNotNull(calendar, "Verify the folder exists on the server");

		// Need to do Refresh to see folder in the list 
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Rename the folder using context menu
		DialogShare dialog = (DialogShare) app.zTreeCalendar.zTreeItem(Action.A_RIGHTCLICK, Button.B_SHARE, calendar);
		ZAssert.assertNotNull(dialog, "Verify the dialog opened");

		// Change the color, click OK
		dialog.zSetEmailAddress(ZimbraAccount.Account9().EmailAddress);

		dialog.zSetMessageType(ShareMessageType.AddNoteToStandardMsg, firstLine);
		dialog.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
		dialog.zKeyboard.zTypeCharacters(secondLine);
		dialog.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
		dialog.zKeyboard.zTypeCharacters(thirdLine);
		dialog.zClickButton(Button.B_OK);
		
		//Search for the mail in recepients inbox
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.Account9(), "subject:('Share Created')");
		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, app.zGetActiveAccount().EmailAddress, "Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, ZimbraAccount.Account9().EmailAddress, "Verify the to field is correct");
		ZAssert.assertStringContains(received.dBodyText, firstLine+ "\n" + secondLine + "\n" + thirdLine, "Verify the body field is correct");
	}

}
