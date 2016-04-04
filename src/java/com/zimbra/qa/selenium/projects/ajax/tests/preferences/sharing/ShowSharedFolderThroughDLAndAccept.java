package com.zimbra.qa.selenium.projects.ajax.tests.preferences.sharing;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraDistributionList;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;

	public class ShowSharedFolderThroughDLAndAccept extends AjaxCommonTest {

		public ShowSharedFolderThroughDLAndAccept() {

			super.startingPage = app.zPagePreferences;
			super.startingAccountPreferences = null;	
		}

		@Test( description = "Accept share folder with user through a distribution list", groups = { "functional1" })

		public void ShowSharedFolderThroughDLAndAccept_01() throws HarnessException {

			// Create distribution list and add two accounts		
			ZimbraAccount AccountA = ZimbraAccount.AccountA();
			ZimbraAccount loggedInAccount = app.zGetActiveAccount();
			ZimbraDistributionList distribution = (new ZimbraDistributionList()).provision();
			distribution.addMember(AccountA);
			distribution.addMember(loggedInAccount);
			
			FolderItem inbox = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), FolderItem.SystemFolder.Inbox);
			String foldername = "folder" + ZimbraSeleniumProperties.getUniqueString();
		
			// Create a subfolder in Inbox of Account A
			AccountA.soapSend(
					"<CreateFolderRequest xmlns='urn:zimbraMail'>"
							+		"<folder name='" + foldername +"' l='" + inbox.getId() +"'/>"
							+	"</CreateFolderRequest>");

			FolderItem subfolder = FolderItem.importFromSOAP(AccountA, foldername);
			ZAssert.assertNotNull(subfolder, "Verify the new owner folder exists");

			// Share folder with DL from Account A
			AccountA.soapSend(
					"<FolderActionRequest xmlns='urn:zimbraMail'>"
							+		"<action id='"+ subfolder.getId() +"' op='grant'>"
							+			"<grant d='" + distribution.EmailAddress + "' gt='usr' perm='r'/>"
							+		"</action>"
							+	"</FolderActionRequest>");
			
			//Go to Sharing from preferences
			app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Sharing);
	
			// Click "show folders shared with me through a DL'
			app.zPagePreferences.zClick("css=label[id$='group_text_right']:contains('Show folders shared with me through a distribution list')"); //Select share with me through DL option

			// Accept the share from active account
			app.zPagePreferences.zWaitForBusyOverlay();
			app.zPagePreferences.sClick("css=div[id='zl__SVP__rows'] a[id$='_accept']"); //Accept locator
			SleepUtil.sleepSmall(); 
			app.zPagePreferences.sClick("css=td[id='ZmAcceptShare_button5_title']"); // 'Yes' button locator
			SleepUtil.sleepMedium(); 

			//Soap verification
			app.zGetActiveAccount().soapSend(
					"<GetFolderRequest xmlns='urn:zimbraMail'/>");
			
			Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//mail:link[@owner='"+ AccountA.EmailAddress +"']");
			ZAssert.assertGreaterThan(nodes.length, 0, "Verify the mountpoint is listed in the folder tree");

			//UI Verification
			//Make sure Active user name is present under 'Folder shares with me that I have accepted'
			ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent("css=div[id='Prefs_Pages_Sharing_mountedShares'] td[id$='_ow']:contains('" + AccountA.DisplayName + "')"), "Verify email id of owner exists");
			ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent("css=div[id='Prefs_Pages_Sharing_mountedShares'] td[id$='_wi']:contains('" + distribution.EmailAddress + "')"), "Verify dl exists in share with");
			ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent("css=div[id='Prefs_Pages_Sharing_mountedShares'] td[id$='_it']:contains('" + foldername  + "')"), "Verify shared folder name exists");

		}
	}
