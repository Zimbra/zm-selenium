package com.zimbra.qa.selenium.projects.universal.tests.preferences.sharing;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.TreePreferences.TreeItem;

public class ResendShareFromPreferences extends AjaxCommonTest {

	public ResendShareFromPreferences() {
		super.startingPage = app.zPagePreferences;
	}

	@Test( description = "Resend share folder request from preferences", groups = { "functional", "L2" })

	public void ResendShareFromPreferences_01() throws HarnessException {

		FolderItem inbox = FolderItem.importFromSOAP(ZimbraAccount.Account9(), FolderItem.SystemFolder.Inbox);
		String foldername = "folder" + ConfigProperties.getUniqueString();
		String activeAccount = app.zGetActiveAccount().DisplayName;

		// Create a subfolder in Inbox
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>"
						+		"<folder name='" + foldername +"' l='" + inbox.getId() +"'/>"
						+	"</CreateFolderRequest>");

		FolderItem subfolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);
		ZAssert.assertNotNull(subfolder, "Verify the new owner folder exists");

		// Share it with Account9
		app.zGetActiveAccount().soapSend(
				"<FolderActionRequest xmlns='urn:zimbraMail'>"
						+		"<action id='"+ subfolder.getId() +"' op='grant'>"
						+			"<grant d='" + ZimbraAccount.Account9().EmailAddress + "' gt='usr' perm='r'/>"
						+		"</action>"
						+	"</FolderActionRequest>");

		// Make sure that Account9 now has the share
		ZimbraAccount.Account9().soapSend(
				"<GetShareInfoRequest xmlns='urn:zimbraAccount'>"
						+		"<grantee type='usr'/>"
						+		"<owner by='name'>"+ app.zGetActiveAccount().EmailAddress +"</owner>"
						+	"</GetShareInfoRequest>");

		String ownerEmail = ZimbraAccount.Account9().soapSelectValue("//acct:GetShareInfoResponse//acct:share[@folderPath='/Inbox/"+ foldername +"']", "ownerEmail");
		ZAssert.assertEquals(ownerEmail, app.zGetActiveAccount().EmailAddress, "Verify the owner of the shared folder");

		// Navigate to preferences -> sharing
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Sharing);
		SleepUtil.sleepMedium();

		// Get row ID
		String optionLocator;
		int rows = app.zPagePreferences.sGetCssCount("css=div[id='Prefs_Pages_Sharing_sharesBy'] table tbody tr:nth-child(2) td div");
		String id;

		for (int i=0; i < rows+2; i++) {
			
			id = app.zPagePreferences.sGetEval("return window.document.getElementById('zl__SVG__rows').children[" + i + "].children[0].children[0].children[0].children[0].id");
			if (app.zPagePreferences.sGetText("css=td[id='" + id + "']").equals(ZimbraAccount.Account9().EmailAddress)) {
				optionLocator = "css=div[id ='zl__SVG__rows'] a[id='" + id.replace("wi", "resend") + "']";
				app.zPagePreferences.sClickAt(optionLocator, "0,0");
				SleepUtil.sleepMedium();
				break;
			}
		}

		// Login to Account9
		app.zPageLogin.zLogin(ZimbraAccount.Account9());

		// Select message
		String subject = "Share Created: "+  foldername;
		String body = activeAccount + " has shared " + "\"" + foldername + "\" (Mail Folder) with " + ZimbraAccount.Account9().EmailAddress;

		// Select the message so that it shows in the reading pane
		app.zPageMail.zIsActive();
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		app.zPageMail.sSelectFrame("css=iframe[id$='__body__iframe']");

		// UI verification
		String mailBody=app.zPageMail.sGetText("css=body[class='MsgBody MsgBody-html']");
		app.zPageMail.sSelectFrame("relative=top");

		ZAssert.assertTrue(mailBody.contains(body), "Verify that message body is present");
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent("css=td[id$='Shr__SHARE_ACCEPT_title']"),"Verify that Accept button is present");
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent("css=td[id$='Shr__SHARE_DECLINE_title']"),"Verify that Decline button is present");

	}
}