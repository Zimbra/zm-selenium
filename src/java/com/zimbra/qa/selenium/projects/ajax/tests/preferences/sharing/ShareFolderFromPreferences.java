/*
 * ***** BEGIN LICENSE BLOCK *****
 *
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
 *
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.sharing;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogShare.ShareRole;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;

public class ShareFolderFromPreferences extends AjaxCore {

	public ShareFolderFromPreferences() {
		super.startingPage = app.zPagePreferences;
	}


	@Test (description = "Share folder from preferences/sharing",
			groups = { "functional", "L2" } )

	public void ShareFolderFromPreferences_01() throws HarnessException {

		// Navigate to preferences -> sharing
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Sharing);

		// Select share button and Share inbox
		app.zPagePreferences.zFolderPressPulldown(Button.O_SHARE_FOLDER_TYPE);
		app.zPagePreferences.sClick("css=td[id='zti__ZmChooseFolderDialog_Mail__2_textCell']"); //Locator for selecting inbox in share dialog
		SleepUtil.sleepSmall();
		app.zPagePreferences.sClick("css=td[id='ChooseFolderDialog_button2_title']:contains('OK')");
		SleepUtil.sleepMedium();

		// Enter email and share with managers rights
		app.zPagePreferences.zSetEmailAddress(ZimbraAccount.AccountA().EmailAddress);
		app.zPagePreferences.zSetRole(ShareRole.Manager);
		app.zPagePreferences.zPressButton(Button.B_OK);

		// Make sure that AccountA now has the share
		ZimbraAccount.AccountA().soapSend(
							"<GetShareInfoRequest xmlns='urn:zimbraAccount'>"
						+		"<grantee type='usr'/>"
						+		"<owner by='name'>"+ app.zGetActiveAccount().EmailAddress +"</owner>"
						+	"</GetShareInfoRequest>");

		String ownerEmail = ZimbraAccount.AccountA().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/Inbox']", "ownerEmail");
		ZAssert.assertEquals(ownerEmail, app.zGetActiveAccount().EmailAddress, "Verify the owner of the shared folder");

		// Make sure AccountA user name is present under 'Folder shared by me'
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent("css=div[id='Prefs_Pages_Sharing_sharesBy'] td[id$='_wi']:contains('" + ZimbraAccount.AccountA().EmailAddress  + "')"), "Verify user email id on the list");
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent("css=div[id ='zl__SVG__rows'] a[id$='_edit' ]"), "Verify edit link exits");
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent("css=div[id ='zl__SVG__rows'] a[id$='_revoke' ]"), "Verify revoke button");
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent("css=div[id ='zl__SVG__rows'] a[id$='_resend' ]"), "Verify resend button");
	}
}