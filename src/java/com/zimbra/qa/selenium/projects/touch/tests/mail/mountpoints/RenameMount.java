package com.zimbra.qa.selenium.projects.touch.tests.mail.mountpoints;


import org.testng.annotations.*;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.touch.ui.PageCreateFolder;

public class RenameMount extends PrefGroupMailByMessageTest{


	public RenameMount() {
		logger.info("New " + DeleteMount.class.getCanonicalName());
	}

	@Test( description = "Verify user can delete mount point with any rights",
			groups = { "functional" })

	public void RenameMount_01() throws HarnessException  {

		String foldername = "folder" + ZimbraSeleniumProperties.getUniqueString();
		String mountpointname = "mountpoint" + ZimbraSeleniumProperties.getUniqueString();
		String renameMountFolder = "folder" + ZimbraSeleniumProperties.getUniqueString();

		FolderItem inbox = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), FolderItem.SystemFolder.Inbox);

		// Create a folder to share
		ZimbraAccount.AccountA().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>"
						+		"<folder name='" + foldername + "' l='" + inbox.getId() + "'/>"
						+	"</CreateFolderRequest>");

		FolderItem folder = FolderItem.importFromSOAP(ZimbraAccount.AccountA(), foldername);

		// Share it
		ZimbraAccount.AccountA().soapSend(
				"<FolderActionRequest xmlns='urn:zimbraMail'>"
						+		"<action id='"+ folder.getId() +"' op='grant'>"
						+			"<grant d='"+ app.zGetActiveAccount().EmailAddress +"' gt='usr' perm='rwidx'/>"
						+		"</action>"
						+	"</FolderActionRequest>");

		// Mount it
		app.zGetActiveAccount().soapSend(
				"<CreateMountpointRequest xmlns='urn:zimbraMail'>"
						+		"<link l='1' name='"+ mountpointname +"'  rid='"+ folder.getId() +"' zid='"+ ZimbraAccount.AccountA().ZimbraId +"'/>"
						+	"</CreateMountpointRequest>");

		app.zPageMain.zRefresh();	

		// Select the folder from the list and Rename
		PageCreateFolder createFolderPage = new PageCreateFolder(app, startingPage);
		createFolderPage.zClickButton(Button.B_EDIT);
		createFolderPage.zSelectFolder(mountpointname);
		createFolderPage.zEnterFolderName(renameMountFolder);
		createFolderPage.zClickButton(Button.B_SAVE);
		app.zPageMain.zRefresh();
		createFolderPage.zSelectOrganizer();

		//-- Verification

		// SOAP
		// Get all the folders and verify the new name appears and the old name disappears
		app.zGetActiveAccount().soapSend("<GetFolderRequest xmlns = 'urn:zimbraMail'/>");
		
		Element[] eFolder1 = app.zGetActiveAccount().soapSelectNodes("//mail:link[@name='"+ mountpointname +"']");
		ZAssert.assertEquals(eFolder1.length, 0, "Verify the old folder name no longer exists");
		
		Element[] eFolder2 = app.zGetActiveAccount().soapSelectNodes("//mail:link[@name='"+ renameMountFolder +"']");
		ZAssert.assertEquals(eFolder2.length, 1, "Verify the new folder name exists");
		
		// UI verification
		ZAssert.assertTrue(createFolderPage.zVerifyFolderExists(renameMountFolder), "Verify renamed folder visible in folder pane");

	}
}