package com.zimbra.qa.selenium.projects.ajax.tests.mail.folders;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.search.PageSearch;

public class OpenInTab extends PrefGroupMailByMessageTest {

	public OpenInTab() {
		logger.info("New " + OpenInTab.class.getCanonicalName());

	}

	@Test(	description = "Folder Context menu -> Open In Tab",
			groups = { "smoke" })
	public void OpenInTabFolder() throws HarnessException {
		
		FolderItem inbox = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Inbox);
		ZAssert.assertNotNull(inbox, "Verify the inbox is available");
				
		// Create the subfolder
		String name1 = "folder" + ZimbraSeleniumProperties.getUniqueString();
		
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ name1 +"' l='"+ inbox.getId() +"'/>" +
                "</CreateFolderRequest>");

		FolderItem subfolder1 = FolderItem.importFromSOAP(app.zGetActiveAccount(), name1);
		ZAssert.assertNotNull(subfolder1, "Verify the subfolder is available");
		
		
		// Click on Get Mail to refresh the folder list
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);
		
		//Click Open in Tab context menu
		app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_OPENTAB, subfolder1);
		ZAssert.assertTrue(app.zPageSearch.sIsElementPresent(PageSearch.Locators.zSearchTab), "Search tab should open");
}
}