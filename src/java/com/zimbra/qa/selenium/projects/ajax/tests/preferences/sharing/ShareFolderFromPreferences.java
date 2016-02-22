package com.zimbra.qa.selenium.projects.ajax.tests.preferences.sharing;


import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogShare.ShareRole;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;

public class ShareFolderFromPreferences extends AjaxCommonTest {
	
	public ShareFolderFromPreferences() {
		
		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences = null;	
	}

	@Test(
			description = "Share Inbox from preferences/sharing",
			groups = { "functional" }
			)
	
	public void ShareFolderFromPreferences_01() throws HarnessException {

		
		// Navigate to preferences -> sharing
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Sharing);
		SleepUtil.sleepMedium();
		
		// Select share button and Share inbox
		app.zPagePreferences.zFolderPressPulldown(Button.O_SHARE_FOLDER_TYPE);
		SleepUtil.sleepSmall();
		app.zPagePreferences.zClick("css=td[id='zti__ZmChooseFolderDialog_Mail__2_textCell']");
		SleepUtil.sleepSmall();
		app.zPagePreferences.zClick("css=td[id='ChooseFolderDialog_button2_title']:contains('OK')");
		SleepUtil.sleepMedium();
		
		// enter email and share with managers rights
		app.zPagePreferences.zSetEmailAddress(ZimbraAccount.AccountA().EmailAddress);
		app.zPagePreferences.zSetRole(ShareRole.Manager);		
		app.zPagePreferences.zClickButton(Button.B_OK);
		
		//SOAP verification
		// Make sure that AccountA now has the share
		ZimbraAccount.AccountA().soapSend(
							"<GetShareInfoRequest xmlns='urn:zimbraAccount'>"
						+		"<grantee type='usr'/>"
						+		"<owner by='name'>"+ app.zGetActiveAccount().EmailAddress +"</owner>"
						+	"</GetShareInfoRequest>");

		String ownerEmail = app.zGetActiveAccount().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/Inbox']", "ownerEmail");
		ZAssert.assertEquals(ownerEmail, ZimbraAccount.AccountA().EmailAddress, "Verify the owner of the shared folder");
			
	}
}