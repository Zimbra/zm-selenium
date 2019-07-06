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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.contacts;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;

public class DragAndDropContact extends AjaxCore  {

	public DragAndDropContact() {
		logger.info("New "+ DragAndDropContact.class.getCanonicalName());
		super.startingPage = app.zPageContacts;
	}


	@Test (description = "Move a contact item to sub-addressbook",
			groups = { "bhr" })

	public void DragAndDropContact_01() throws HarnessException {

        // The Addressbook folder
		FolderItem root = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.UserRoot);

		// The addressbook
		String foldername = "ab"+ ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
						"<folder name='" + foldername +"' l='"+ root.getId() +"' view='contact'/>" +
					"</CreateFolderRequest>");
		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);

		// Create a contact item
		String firstName = "First" + ConfigProperties.getUniqueString();
		String lastName = "Last" + ConfigProperties.getUniqueString();
		String email = "email" + ConfigProperties.getUniqueString() + "@domain.com";

		app.zGetActiveAccount().soapSend(
	                "<CreateContactRequest xmlns='urn:zimbraMail'>" +
	                		"<cn >" +
	                			"<a n='firstName'>" + firstName +"</a>" +
	                			"<a n='lastName'>" + lastName +"</a>" +
	                			"<a n='email'>" + email + "</a>" +
                			"</cn>" +
	                "</CreateContactRequest>");

		ContactItem contact = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:" + firstName);

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// DND
		app.zPageContacts.zDragAndDrop(
				"css=div#zlif__CNS-main__" + contact.getId() + "__fileas:contains("+ contact.fileAs + ")",
				"css=td#zti__main_Contacts__" + folder.getId() + "_textCell:contains("+ folder.getName() + ")");

        // Verify contact deleted
        ContactItem actual = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:"+ contact.firstName);
        ZAssert.assertNotNull(actual, "Verify the contact is deleted from the addressbook");
        ZAssert.assertEquals(actual.getFolderId(), folder.getId(), "Verify the contact is in the Trash folder");
   	}


	@Test (description = "Move a contact item to trash folder by drag and drop",
			groups = { "sanity" })

	public void DnDToTrash_02() throws HarnessException {

        // The Addressbook and Trash folder
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);

		// Create a contact item
		String firstName = "First" + ConfigProperties.getUniqueString();
		String lastName = "Last" + ConfigProperties.getUniqueString();
		String email = "email" + ConfigProperties.getUniqueString() + "@domain.com";

		app.zGetActiveAccount().soapSend(
	                "<CreateContactRequest xmlns='urn:zimbraMail'>" +
	                		"<cn >" +
	                			"<a n='firstName'>" + firstName +"</a>" +
	                			"<a n='lastName'>" + lastName +"</a>" +
	                			"<a n='email'>" + email + "</a>" +
                			"</cn>" +
	                "</CreateContactRequest>");

		ContactItem contact = ContactItem.importFromSOAP(app.zGetActiveAccount(), "#firstname:" + firstName);

		// Refresh
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		// DND
		app.zPageContacts.zDragAndDrop(
				"css=div#zlif__CNS-main__" + contact.getId() + "__fileas:contains("+ contact.fileAs + ")",
				"css=td#zti__main_Contacts__" + trash.getId() + "_textCell:contains("+ trash.getName() + ")");

        // Verify contact deleted
        ContactItem actual = ContactItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere #firstname:"+ contact.firstName);
        ZAssert.assertNotNull(actual, "Verify the contact is deleted from the addressbook");
        ZAssert.assertEquals(actual.getFolderId(), trash.getId(), "Verify the contact is in the Trash folder");
   	}
}