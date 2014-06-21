/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.addressbook.mountpoints;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogShare;


public class CreateShare extends AjaxCommonTest  {

	public CreateShare() {
		logger.info("New "+ CreateShare.class.getCanonicalName());
		
		// All tests start at the login page
		super.startingPage = app.zPageAddressbook;
		super.startingAccountPreferences = null;

	}
	
	@Test(	description = "Share an addressbook - Viewer",
			groups = { "smoke" })
	public void CreateShare_01() throws HarnessException {
		
		String addressbookname = "addressbook" + ZimbraSeleniumProperties.getUniqueString();


		// Create an addressbook
		app.zGetActiveAccount().soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
				+		"<folder name='" + addressbookname +"' l='1' view='contact'/>"
				+	"</CreateFolderRequest>");

		// Make sure the folder was created on the server
		FolderItem addressbook = FolderItem.importFromSOAP(app.zGetActiveAccount(), addressbookname);
		ZAssert.assertNotNull(addressbook, "Verify the folder exists on the server");

		
		
		//Need to do Refresh by clicking on getmail button to see folder in the list 
		app.zPageAddressbook.zToolbarPressButton(Button.B_REFRESH);
		

		// Right click on folder, select "Share"
		DialogShare dialog = (DialogShare)app.zTreeContacts.zTreeItem(Action.A_RIGHTCLICK, Button.B_SHARE, addressbook);
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

		String ownerEmail = ZimbraAccount.AccountA().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/"+ addressbookname +"']", "ownerEmail");
		ZAssert.assertEquals(ownerEmail, app.zGetActiveAccount().EmailAddress, "Verify the owner of the shared folder");
		
		String rights = ZimbraAccount.AccountA().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/"+ addressbookname +"']", "rights");
		ZAssert.assertEquals(rights, "r", "Verify the rights are 'read only'");

		String granteeType = ZimbraAccount.AccountA().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/"+ addressbookname +"']", "granteeType");
		ZAssert.assertEquals(granteeType, "usr", "Verify the grantee type is 'user'");

	}

	
	

}
