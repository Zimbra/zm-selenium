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

package com.zimbra.qa.selenium.projects.ajax.tests.mail.newwindow.mountpoints;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.XmlStringUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.ajax.pages.SeparateWindowDialog;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.SeparateWindowDisplayMail;

public class AcceptShareFolder extends SetGroupMailByMessagePreference {

	public AcceptShareFolder() {
		logger.info("New " + AcceptShareFolder.class.getCanonicalName());
		super.startingPage = app.zPageMail;
		super.startingAccountPreferences.put("zimbraPrefOpenMailInNewWindow", "TRUE");		
	}


	@Bugs (ids = "27579")
	@Test (description = "Receive an invitation for a shared folder, Accept it - in a separate window opened by double clicking on the mail",
			groups = { "functional", "L3" })

	public void AcceptShareFolder_01() throws HarnessException {

		ZimbraAccount Owner = (new ZimbraAccount()).provision().authenticate();

		// Owner creates a folder, shares it with current user, and sends invitation
		String ownerFoldername = "ownerfolder"+ ConfigProperties.getUniqueString();

		FolderItem ownerInbox = FolderItem.importFromSOAP(Owner, FolderItem.SystemFolder.Inbox);
		ZAssert.assertNotNull(ownerInbox, "Verify the new owner folder exists");

		Owner.soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>"
						+		"<folder name='" + ownerFoldername +"' l='" + ownerInbox.getId() +"'/>"
						+	"</CreateFolderRequest>");

		FolderItem ownerFolder = FolderItem.importFromSOAP(Owner, ownerFoldername);
		ZAssert.assertNotNull(ownerFolder, "Verify the new owner folder exists");

		Owner.soapSend(
				"<FolderActionRequest xmlns='urn:zimbraMail'>"
						+		"<action id='"+ ownerFolder.getId() +"' op='grant'>"
						+			"<grant d='" + app.zGetActiveAccount().EmailAddress + "' gt='usr' perm='r'/>"
						+		"</action>"
						+	"</FolderActionRequest>");

		String shareMessageSubject = "shared"+ ConfigProperties.getUniqueString();
		String shareElement = String.format(
				"<share xmlns='urn:zimbraShare' version='0.1' action='new' >"
						+		"<grantee id='%s' email='%s' name='%s' />"
						+		"<grantor id='%s' email='%s' name='%s' />"
						+		"<link id='%s' name='%s' view='message' perm='r' />"
						+		"<notes/>"
						+	"</share>",
						app.zGetActiveAccount().ZimbraId, app.zGetActiveAccount().EmailAddress, app.zGetActiveAccount().EmailAddress,
						Owner.ZimbraId, Owner.EmailAddress, Owner.EmailAddress,
						ownerFolder.getId(), ownerFolder.getName());

		Owner.soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>"
						+		"<m>"
						+			"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>"
						+			"<su>"+ shareMessageSubject +"</su>"
						+			"<mp ct='multipart/alternative'>"
						+				"<mp ct='text/plain'>"
						+					"<content>shared</content>"
						+				"</mp>"
						+				"<mp ct='xml/x-zimbra-share'>"
						+					"<content>"+ XmlStringUtil.escapeXml(shareElement) +"</content>"
						+				"</mp>"
						+			"</mp>"
						+		"</m>"
						+	"</SendMsgRequest>");

		// Refresh current view
		ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(shareMessageSubject), "Verify message displayed in current view");

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, shareMessageSubject);

		SeparateWindowDisplayMail window = null;
		String windowTitle = ": " + shareMessageSubject;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zListItem(Action.A_DOUBLECLICK_TO_NEWWINDOW, shareMessageSubject);

			window.zSetWindowTitle(windowTitle);
			ZAssert.assertTrue(window.zIsWindowOpen(windowTitle),"Verify the window is opened and switch to it");

			// Verify that the A/D buttons are displayed
			ZAssert.assertTrue(window.zHasShareADButtons(),"Verify that the Accept/Decline share buttons are present");

			// Accept the share, which opens a dialog
			SeparateWindowDialog dialog = (SeparateWindowDialog) window.zPressButton(Button.B_ACCEPT_SHARE);
			ZAssert.assertNotNull(dialog,"Verify that the accept share dialog opens");

			// Click Yes on the dialog
			dialog.zPressButton(Button.B_YES);

		} finally {
			app.zPageMain.zCloseWindow(window, windowTitle, app);
		}
		
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(),FolderItem.SystemFolder.Trash);

		// Verify UI for Checking Share Decline message
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, trash);
		
		List<MailItem> messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		app.zPageMail.zListItem(Action.A_LEFTCLICK, messages.get(0).gSubject);
		ZAssert.assertStringContains(messages.get(0).gSubject,shareMessageSubject, "Verify the message's subject matches");
	}
}