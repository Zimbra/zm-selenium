package com.zimbra.qa.selenium.projects.universal.tests.preferences.sharing;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.DialogShare.ShareRole;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.TreePreferences.TreeItem;

public class EditFolderSharedByMeFromPreferences extends UniversalCommonTest {

	public EditFolderSharedByMeFromPreferences() {
		super.startingPage = app.zPagePreferences;
			
	}

	@Test( description = "Modify share folder rights from preferences", groups = { "functional", "L2" })

	public void EditFolderSharedByMeFromPreferences_01() throws HarnessException {

		FolderItem inbox = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), FolderItem.SystemFolder.Inbox);
		String foldername = "folder" + ConfigProperties.getUniqueString();

		// Create a subfolder in Inbox
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>"
						+		"<folder name='" + foldername +"' l='" + inbox.getId() +"'/>"
						+	"</CreateFolderRequest>");

		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);
		ZAssert.assertNotNull(subfolder, "Verify the new owner folder exists");

		// Share it with AccountA
		app.zGetActiveAccount().soapSend(
				"<FolderActionRequest xmlns='urn:zimbraMail'>"
						+		"<action id='"+ subfolder.getId() +"' op='grant'>"
						+			"<grant d='" + ZimbraAccount.AccountA().EmailAddress + "' gt='usr' perm='r'/>"
						+		"</action>"
						+	"</FolderActionRequest>");

		// Make sure that AccountA now has the share
		ZimbraAccount.AccountA().soapSend(
				"<GetShareInfoRequest xmlns='urn:zimbraAccount'>"
						+		"<grantee type='usr'/>"
						+		"<owner by='name'>"+ app.zGetActiveAccount().EmailAddress +"</owner>"
						+	"</GetShareInfoRequest>");

		// Browser refresh
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Navigate to preferences -> sharing
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Sharing);

		// Select Edit and then change the rights to manager. Select OK
		app.zPagePreferences.sClick("css=div[id ='zl__SVG__rows'] a[id$='_edit' ]"); //Edit link locator
		SleepUtil.sleepSmall(); 
		app.zPagePreferences.zSetRole(ShareRole.Manager);		
		app.zPagePreferences.zPressButton(Button.B_OK);
		SleepUtil.sleepMedium(); 

		//Soap verification
		// Make sure that AccountA now has the share with manager rights
		ZimbraAccount.AccountA().soapSend(
				"<GetShareInfoRequest xmlns='urn:zimbraAccount'>"
						+		"<grantee type='usr'/>"
						+		"<owner by='name'>"+ app.zGetActiveAccount().EmailAddress +"</owner>"
						+	"</GetShareInfoRequest>");
		
		String ownerEmail = ZimbraAccount.AccountA().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/Inbox/"+ foldername +"']", "ownerEmail");
		ZAssert.assertEquals(ownerEmail, app.zGetActiveAccount().EmailAddress, "Verify the owner of the shared folder");
		
		String managerrights = ZimbraAccount.AccountA().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/Inbox/"+ foldername +"']", "rights");
		ZAssert.assertEquals(managerrights, "rwidx", "Verify the rights are manager");
	}		
}