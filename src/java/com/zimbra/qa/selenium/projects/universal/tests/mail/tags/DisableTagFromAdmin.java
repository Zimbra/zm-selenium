package com.zimbra.qa.selenium.projects.universal.tests.mail.tags;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;

public class DisableTagFromAdmin extends PrefGroupMailByMessageTest {

	public DisableTagFromAdmin() {
		logger.info("New "+ CreateTag.class.getCanonicalName());		
	}
	
	@Test( description = "Bug # 80892 - Disable tag from admin and check in UNIVERSAL client",groups = { "functional", "L2" })
	public void DisableTagFromAdmin_01() throws HarnessException {	
		
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
						+		"<id>"+ZimbraAccount.Account10().ZimbraId +"</id>"
						+		"<a n='zimbraFeatureTaggingEnabled'>FALSE</a>"
						+	"</ModifyAccountRequest>");
		// Login to active account
		app.zPageLogin.zLogin(ZimbraAccount.Account10());
		
		// Verify that Tag is not present under tree
		ZAssert.assertFalse(app.zTreeMail.sIsElementPresent("css=td[id='ztih__main_Mail__TAG_textCell']") , "Tag is not present");
		
		// Open context menu
		app.zTreeMail.sClick("css=td[id='zb__NEW_MENU_dropdown'] ");
		SleepUtil.sleepSmall();
		
		// Verify tag not present under context menu
		ZAssert.assertFalse(app.zTreeMail.sIsElementPresent("css=td[id='zb__NEW_MENU_NEW_TAG_title']"), "Tag is not present under context menu");		
		
	}
}