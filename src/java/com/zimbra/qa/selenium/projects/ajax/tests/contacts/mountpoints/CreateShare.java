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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.mountpoints;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogShare;

public class CreateShare extends AjaxCore  {

	public CreateShare() {
		logger.info("New "+ CreateShare.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}


	@Test (description = "Share an addressbook - Viewer",
			groups = { "smoke", "L1" })

	public void CreateShare_01() throws HarnessException {

		String addressbookname = "addressbook" + ConfigProperties.getUniqueString();

		// Create an addressbook
		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + addressbookname +"' l='1' view='contact'/>"
				+	"</CreateFolderRequest>");

		// Make sure the folder was created on the server
		FolderItem addressbook = FolderItem.importFromSOAP(app.zGetActiveAccount(), addressbookname);
		ZAssert.assertNotNull(addressbook, "Verify the folder exists on the server");

		// Need to do Refresh to see folder in the list
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// Right click on folder, select "Share"
		DialogShare dialog = (DialogShare)app.zTreeContacts.zTreeItem(Action.A_RIGHTCLICK, Button.B_SHARE, addressbook);
		ZAssert.assertNotNull(dialog, "Verify the sharing dialog pops up");

		// Use defaults for all options
		dialog.zSetEmailAddress(ZimbraAccount.AccountA().EmailAddress);

		// Send it
		dialog.zPressButton(Button.B_OK);

		// Make sure that AccountA now has the share
		ZimbraAccount.AccountA().soapSend(
					"<GetShareInfoRequest xmlns='urn:zimbraAccount'>"
				+		"<grantee type='usr'/>"
				+		"<owner by='name'>"+ app.zGetActiveAccount().EmailAddress +"</owner>"
				+	"</GetShareInfoRequest>");

		String ownerEmail = ZimbraAccount.AccountA().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/"+ addressbookname +"']", "ownerEmail");
		ZAssert.assertEquals(ownerEmail, app.zGetActiveAccount().EmailAddress, "Verify the owner of the shared folder");

		String rights = ZimbraAccount.AccountA().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/"+ addressbookname +"']", "rights");
		ZAssert.assertEquals(rights, "r", "Verify the rights are 'read only'");

		String granteeType = ZimbraAccount.AccountA().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/"+ addressbookname +"']", "granteeType");
		ZAssert.assertEquals(granteeType, "usr", "Verify the grantee type is 'user'");
	}
}