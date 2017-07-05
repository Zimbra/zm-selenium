package com.zimbra.qa.selenium.projects.universal.tests.preferences.sharing;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.TreePreferences.TreeItem;

public class RevokeShareFromPreferences extends AjaxCommonTest {

	public RevokeShareFromPreferences() {			
	}

	@Test( description = "Revoke share folder from preferences", groups = { "functional", "L2" })

	public void RevokeShareFromPreferences_01() throws HarnessException {

		FolderItem inbox = FolderItem.importFromSOAP(ZimbraAccount.AccountB(), FolderItem.SystemFolder.Inbox);
		String foldername = "folder" + ConfigProperties.getUniqueString();

		// Create a subfolder in Inbox
		ZimbraAccount.Account10().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>"
						+		"<folder name='" + foldername +"' l='" + inbox.getId() +"'/>"
						+	"</CreateFolderRequest>");

		FolderItem subfolder = FolderItem.importFromSOAP(ZimbraAccount.Account10(), foldername);
		ZAssert.assertNotNull(subfolder, "Verify the new owner folder exists");

		// Share it with AccountB
		ZimbraAccount.Account10().soapSend(
				"<FolderActionRequest xmlns='urn:zimbraMail'>"
						+		"<action id='"+ subfolder.getId() +"' op='grant'>"
						+			"<grant d='" + ZimbraAccount.AccountB().EmailAddress + "' gt='usr' perm='r'/>"
						+		"</action>"
						+	"</FolderActionRequest>");

		// Make sure that AccountB now has the share
		ZimbraAccount.AccountB().soapSend(
				"<GetShareInfoRequest xmlns='urn:zimbraAccount'>"
						+		"<grantee type='usr'/>"
						+		"<owner by='name'>"+ ZimbraAccount.Account10().EmailAddress +"</owner>"
						+	"</GetShareInfoRequest>");
		String ownerEmail = ZimbraAccount.AccountB().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/Inbox/"+ foldername +"']", "ownerEmail");
		ZAssert.assertEquals(ownerEmail, ZimbraAccount.Account10().EmailAddress, "Verify the owner of the shared folder");
		
		// Login with Account10 and go to preferences
		app.zPageLogin.zLogin(ZimbraAccount.Account10());
		app.zPagePreferences.zNavigateTo();

		// Navigate to preferences -> sharing
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Sharing);
		
		// Get row ID
		String optionLocator;
		int rows = app.zPagePreferences.sGetCssCount("css=div[id='Prefs_Pages_Sharing_sharesBy'] table tbody tr:nth-child(2) td div");
		String id;

		for (int i=0; i < rows+2; i++) {
			
			id = app.zPagePreferences.sGetEval("return window.document.getElementById('zl__SVG__rows').children[" + i + "].children[0].children[0].children[0].children[0].id");
			if (app.zPagePreferences.sGetText("css=td[id='" + id + "']").equals(ZimbraAccount.AccountB().EmailAddress)) {
				optionLocator = "css=div[id ='zl__SVG__rows'] a[id='" + id.replace("wi", "revoke") + "']";
				app.zPagePreferences.sClickAt(optionLocator, "0,0");
				SleepUtil.sleepMedium();
				break;
			}
		}

		app.zPagePreferences.sClick("css=td[id^='Yes_DWT'] td[id$='_button5_title']:contains('Yes')"); // Yes button locator
		SleepUtil.sleepMedium(); 

		//Soap verification
		// Make sure that share has been revoked
		ZimbraAccount.Account10().soapSend(
				"<GetFolderRequest xmlns='urn:zimbraMail'>"
						+		"<folder l='" + subfolder + "'/>"
						+	"</GetFolderRequest>");

		Element[] nodes = ZimbraAccount.Account10().soapSelectNodes("//mail:grant");
		ZAssert.assertEquals(nodes.length, 0, "Verify the folder is not granted");

		// UI verification
		ZAssert.assertFalse(app.zPagePreferences.sIsElementPresent("css=div[id='Prefs_Pages_Sharing_sharesBy'] td[id$='_wi']:contains('" + ZimbraAccount.AccountB().EmailAddress  + "')"), "Verify user email id is not on the list");
		ZAssert.assertFalse(app.zPagePreferences.sIsElementPresent("css=div[id='Prefs_Pages_Sharing_sharesBy'] td[id$='_wi']:contains('" + ZimbraAccount.AccountB().EmailAddress  +"') ~ td a[id$='_edit' ]"), "Verify edit link does not exists"); 
		ZAssert.assertFalse(app.zPagePreferences.sIsElementPresent("css=div[id='Prefs_Pages_Sharing_sharesBy'] td[id$='_wi']:contains('" + ZimbraAccount.AccountB().EmailAddress  +"') ~ td a[id$='_revoke']"), "Verify revoke button does not exists");
		ZAssert.assertFalse(app.zPagePreferences.sIsElementPresent("css=div[id='Prefs_Pages_Sharing_sharesBy'] td[id$='_wi']:contains('" + ZimbraAccount.AccountB().EmailAddress  +"') ~ td a[id$='_resend']"), "Verify resend button does not exists");	

	}		
}