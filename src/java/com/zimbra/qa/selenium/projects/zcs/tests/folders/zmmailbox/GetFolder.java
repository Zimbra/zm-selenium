package com.zimbra.qa.selenium.projects.zcs.tests.folders.zmmailbox;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.framework.util.staf.Stafzmmailbox;
import com.zimbra.qa.selenium.projects.zcs.tests.CommonTest;
import com.zimbra.qa.selenium.projects.zcs.ui.MailApp;


public class GetFolder extends CommonTest {

	public GetFolder() {
		super.NAVIGATION_TAB="mail";
	}
	
	//--------------------------------------------------------------------------
	// SECTION 2: SETUP
	//--------------------------------------------------------------------------
	@BeforeClass(groups = { "always" })
	public void zLogin() throws Exception {
		super.zLogin();
	}

	
	@Test(
			description = "Create a mail folder using zmmailbox and verify it in ajax",
			groups = { "smoke", "test" }
	)
	public void BasicGetFolder() throws Exception {
		
		String myMailbox = ClientSessionFactory.session().currentUserName();
		String folderName = "folder" + ZimbraSeleniumProperties.getUniqueString();
		
		Stafzmmailbox zmmailbox = new Stafzmmailbox();
		
		// Use zmmailbox to create a new folder in USER_ROOT
		zmmailbox.execute("zmmailbox -z -m "+ myMailbox +" cf /"+ folderName);
		
		// For debugging, use zmmailbox to list the folders
		zmmailbox.execute("zmmailbox -z -m "+ myMailbox +" gaf");
		
		
		// TODO: Do we need to click on "get mail" to sync the change?
		obj.zButton.zClick(MailApp.zGetMailIconBtn);

		
		// Use ZWC to verify the folder exists
		obj.zFolder.zExists(folderName);

	}

}
