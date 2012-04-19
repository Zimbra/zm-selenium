package com.zimbra.qa.selenium.projects.ajax.tests.zimlets.linkedin;

import java.util.List;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.ZimletItem;
import com.zimbra.qa.selenium.framework.items.ZimletItem.CoreZimletItem;
import com.zimbra.qa.selenium.framework.items.ZimletItem.CoreZimletItem.CoreZimletName;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.TreeMail.FolderSection;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.TreeMail.FolderSectionAction;


public class FolderTree extends AjaxCommonTest {

	public FolderTree() {
		logger.info("New "+ FolderTree.class.getCanonicalName());
		
		// All tests start at the login page
		super.startingPage = app.zPageMail;
		super.startingAccountPreferences = null;
		
	}
	
	@Bugs(ids = "50123")
	@Test(	description = "Verify the LinkedIn zimlet appears in the folder tree",
			groups = { "deprecated" })
	public void FolderTree_01() throws HarnessException {
		ZimletItem linkedin = CoreZimletItem.getCoreZimlet(CoreZimletName.com_zimbra_linkedin, app);
		
		// Expand the zimlets section
		app.zTreeMail.zSectionAction(FolderSectionAction.Expand, FolderSection.Zimlets);
		
		// Get the list of zimlets
		List<ZimletItem> zimlets = app.zTreeMail.zListGetZimlets();
		
		// Find out if LinkedIn is listed
		ZimletItem found = null;
		for (ZimletItem zimlet : zimlets) {
			if (linkedin.equals(zimlet) ) {
				found = zimlet;
			}
		}
		
		ZAssert.assertNotNull(found, "Verify the LinkedIn Zimlet was found");
				
	}


	// All these tests require the Folder tree to be fully loaded
	@BeforeMethod( groups = { "always" } )
	public void folderTreeBeforeMethod() throws HarnessException {
		logger.info("folderTreeBeforeMethod: start");
		
		for (int i = 0; i < 10; i++) {
			
			if ( app.zTreeMail.zIsActive() ) {
				return; // Done!
			}
			
			SleepUtil.sleep(1000);
			
		}
		logger.info("folderTreeBeforeMethod: finish");

	}


}
