/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.universal.tests.main.external.register;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;

public class BasicRegistration extends AjaxCommonTest {
	
	public BasicRegistration() {
		logger.info("New "+ BasicRegistration.class.getCanonicalName());
		super.startingPage = app.zPageLogin;
	}
	
	
	@Bugs(ids = "103011")
	@Test( description = "Register as and external user", priority=3, 
		groups = { "smoke", "L0"})
	
	public void BasicRegistration_01() throws HarnessException {
		
		try {
		
			ZimbraExternalAccount external = new ZimbraExternalAccount();
			external.setEmailAddress("external" + ConfigProperties.getUniqueString() + "@example.com");
			
			FolderItem inbox = FolderItem.importFromSOAP(ZimbraAccount.AccountZWC(), FolderItem.SystemFolder.Inbox);
			String foldername = "folder" + ConfigProperties.getUniqueString();
	
			// Create a subfolder in Inbox
			ZimbraAccount.AccountZWC().soapSend(
						"<CreateFolderRequest xmlns='urn:zimbraMail'>"
					+		"<folder name='" + foldername +"' l='" + inbox.getId() +"' view='message'/>"
					+	"</CreateFolderRequest>");
			String folderid = ZimbraAccount.AccountZWC().soapSelectValue("//mail:folder", "id");
	
			// Share the subfolder
			ZimbraAccount.AccountZWC().soapSend(
						"<FolderActionRequest xmlns='urn:zimbraMail'>"
					+		"<action id='"+ folderid +"' op='grant'>"
					+			"<grant d='"+ external.EmailAddress +"' inh='1' gt='guest' pw='' perm='r'/>"
					+		"</action>"
					+	"</FolderActionRequest>");
	
			// Send the notification
			ZimbraAccount.AccountZWC().soapSend(
						"<SendShareNotificationRequest xmlns='urn:zimbraMail'>"
					+		"<item id='"+ folderid +"'/>"
					+		"<e a='"+ external.EmailAddress +"'/>"
					+		"<notes/>"
					+	"</SendShareNotificationRequest>");
	
	
			// Parse the URL From the sent message
			ZimbraAccount.AccountZWC().soapSend(
						"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
					+		"<query>in:sent "+ external.EmailAddress +"</query>"
					+	"</SearchRequest>");
			String messageid = ZimbraAccount.AccountZWC().soapSelectValue("//mail:m", "id");
			
			ZimbraAccount.AccountZWC().soapSend(
						"<GetMsgRequest xmlns='urn:zimbraMail'>"
					+		"<m id='"+ messageid +"' html='1'/>"
					+	"</GetMsgRequest>");
			
			// Based on the content of the sent message, the URL's can be determined
			Element response = ZimbraAccount.AccountZWC().soapSelectNode("//mail:GetMsgResponse", 1);
			external.setURL(response);
			
			
			//-- GUI Actions
			
			// Navigate to the registration page
			app.zPageExternalRegistration.zSetURL(external.getRegistrationURL());
			app.zPageExternalRegistration.zNavigateTo();
			app.zPageExternalRegistration.zLogin(external);
	
			
			//-- Verification
			
			// After logging in, make sure the page appears correctly
			app.zPageExternalMain.zWaitForActive();
			boolean loaded = app.zPageExternalMain.zIsActive();
			ZAssert.assertTrue(loaded, "Verify that the main page became active");
			
		} finally {
			zFreshLogin();
			logger.info(app.zGetActiveAccount().EmailAddress);
		}
	}
}