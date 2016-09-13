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
package com.zimbra.qa.selenium.projects.touch.tests.contacts.mountpoint.viewer;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderMountpointItem;
import com.zimbra.qa.selenium.framework.items.TagItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.TouchCommonTest;
import com.zimbra.qa.selenium.projects.touch.ui.PageCreateFolder;
import com.zimbra.qa.selenium.projects.touch.ui.contacts.TagContactView;

public class TagContact extends TouchCommonTest  {

	public TagContact() {
		logger.info("New "+ Delete.class.getCanonicalName());
		super.startingPage = app.zPageAddressbook;
	}

	@Test( description = "Tag a contact item in mount folder with viewer rights",
			groups = { "functional" })

	public void TagContact_01() throws HarnessException {

		// Owner creates a folder, shares it with current user
		String ownerFoldername = "ownerfolder"+ ConfigProperties.getUniqueString();


		ZimbraAccount.AccountA().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>"
						+		"<folder name='" + ownerFoldername +"' l='1' view='contact'/>"
						+	"</CreateFolderRequest>");

		FolderItem ownerFolder = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), ownerFoldername);
		ZAssert.assertNotNull(ownerFolder, "Verify the new owner folder exists");

		logger.info(ownerFolder.getId());


		ContactItem contact = new ContactItem();
		contact.firstName = "First" + ConfigProperties.getUniqueString();
		contact.lastName = "Last" + ConfigProperties.getUniqueString();
		contact.email = "email" + ConfigProperties.getUniqueString() + "@domain.com";

		ZimbraAccount.AccountA().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
						"<cn l='"+ ownerFolder.getId() +"'>" +
						"<a n='firstName'>" + contact.firstName +"</a>" +
						"<a n='lastName'>" + contact.lastName +"</a>" +
						"<a n='email'>" + contact.email + "</a>" +
						"</cn>" +
				"</CreateContactRequest>");

		//Share the folder
		ZimbraAccount.AccountA().soapSend(
				"<FolderActionRequest xmlns='urn:zimbraMail'>"
						+		"<action id='"+ ownerFolder.getId() +"' op='grant'>"
						+			"<grant d='" + app.zGetActiveAccount().EmailAddress + "' gt='usr' perm='r'/>"
						+		"</action>"
						+	"</FolderActionRequest>");


		// Current user creates the mountpoint that points to the share
		String mountpointname = "mountpoint"+ ConfigProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
						+		"<link l='1' name='"+ mountpointname +"' view='contact' rid='"+ ownerFolder.getId() +"' zid='"+ ZimbraAccount.AccountA().ZimbraId +"'/>"
						+	"</CreateMountpointRequest>");

		FolderMountpointItem mountpoint = FolderMountpointItem.importFromSOAP(app.zGetActiveAccount(), mountpointname);
		ZAssert.assertNotNull(mountpoint, "Verify the subfolder is available");

		app.zPageMain.zRefresh();

		// Create a tag
		TagItem tagItem = TagItem.CreateUsingSoap(app.zGetActiveAccount());	

		// Select the contact from contact list
		PageCreateFolder createFolderPage = new PageCreateFolder(app, startingPage);
		createFolderPage.zSelectMountFolder(mountpointname);

		String locator = contact.lastName + ", " + contact.firstName;
		app.zPageAddressbook.zSelectContact(locator);


		// Choose tag action from action menu
		TagContactView tcv = (TagContactView)app.zPageAddressbook.zToolbarPressPulldownMount(Button.B_ACTIONS,Button.B_TAG);

		// Choose tag which is assigned to the contact 
		tcv.zTreeItem(Action.A_LEFTCLICK, tagItem);

		//UI Verification - Verify error message is displayed 
		app.zPageMail.zVerifyErrorMessageExist();

		// Verify the contact is removed from Contacts folder
		ContactItem actual = ContactItem.importFromSOAP(ZimbraAccount.AccountA(), "tag:"+tagItem.getName()+" AND #firstname:"+ contact.firstName);
		ZAssert.assertNull(actual, "Verify the contact is not with the tag");

	}
}
