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

	@Test (description = "Share folder from preferences/sharing", groups = { "functional" } )
	
	public void ShareFolderFromPreferences_01() throws HarnessException {
		
		// Navigate to preferences -> sharing
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Sharing);
				
		// Select share button and Share inbox
		app.zPagePreferences.zFolderPressPulldown(Button.O_SHARE_FOLDER_TYPE);
		app.zPagePreferences.zClick("css=td[id='zti__ZmChooseFolderDialog_Mail__2_textCell']"); //Locator for selecting inbox in share dialog
		SleepUtil.sleepSmall();
		app.zPagePreferences.zClick("css=td[id='ChooseFolderDialog_button2_title']:contains('OK')"); //Locator for selecting Ok button in share dialog
		SleepUtil.sleepMedium();
		
		//enter email and share with managers rights
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

		String ownerEmail = ZimbraAccount.AccountA().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/Inbox']", "ownerEmail");
		ZAssert.assertEquals(ownerEmail, app.zGetActiveAccount().EmailAddress, "Verify the owner of the shared folder");
		
		//UI Verification
		//Make sure AccountA user name is present under 'Folder shared by me'
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent("css=div[id='Prefs_Pages_Sharing_sharesBy'] td[id$='_wi']:contains('" + ZimbraAccount.AccountA().EmailAddress  + "')"), "Verify user email id on the list");
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent("css=div[id ='zl__SVG__rows'] a[id$='_edit' ]"), "Verify edit link exits"); 
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent("css=div[id ='zl__SVG__rows'] a[id$='_revoke' ]"), "Verify revoke button");
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent("css=div[id ='zl__SVG__rows'] a[id$='_resend' ]"), "Verify resend button");		
	}
}