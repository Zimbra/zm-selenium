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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.mountpoints.external;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogShare;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogShare.ShareMessageType;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogShare.ShareWith;

public class CreateShare extends AjaxCommonTest {

	public CreateShare() {
		logger.info("New " + CreateShare.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}


	@Bugs (ids = "46674")
	@Test (description = "Share folder with external rights and add note to it",
			groups = { "functional", "L2" })

	public void CreateShare_01() throws HarnessException {

		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);
		ZAssert.assertNotNull(root, "Verify that root folder is available");

		// Create a folder
		String calendar = "calendar" + ConfigProperties.getUniqueString();
		String message = "message" + ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + calendar
				+ "' l='" + root.getId() + "' view='appointment'/>" + "</CreateFolderRequest>");

		FolderItem calendarFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), calendar);
		ZAssert.assertNotNull(calendarFolder, "Verify the subfolder is available");

		// Click on Get Mail to refresh the folder list
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);

		// Rename the folder using context menu
		DialogShare dialog = (DialogShare) app.zTreeCalendar.zTreeItem(Action.A_RIGHTCLICK, Button.B_SHARE, calendarFolder);
		ZAssert.assertNotNull(dialog, "Verify the dialog opened");

		// Change the color, click OK
		dialog.zSetShareWith(ShareWith.ExternalGuests);
		dialog.zSetEmailAddress(ZimbraAccount.Account10().EmailAddress);
		dialog.zSetMessageType(ShareMessageType.AddNoteToStandardMsg, message);
		dialog.zPressButton(Button.B_OK);

		// Search for the mail in recepients inbox
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.Account10(),
				"subject:(" + (char) 34 + "Share Created" + (char) 58 + " " + calendar + (char) 34 + ")");
		ZAssert.assertEquals(received.dFromRecipient.dEmailAddress, app.zGetActiveAccount().EmailAddress,
				"Verify the from field is correct");
		ZAssert.assertEquals(received.dToRecipients.get(0).dEmailAddress, ZimbraAccount.Account10().EmailAddress,
				"Verify the to field is correct");
		ZAssert.assertStringContains(received.dBodyText, message, "Verify the body field is correct");

	}
}