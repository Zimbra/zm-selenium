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
package com.zimbra.qa.selenium.projects.ajax.tests.zimlets.smime;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.PagePreferences.Locators;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;


public class DeleteCertificate extends AjaxCommonTest {

	public DeleteCertificate() {
		
		logger.info("New "+ DeleteCertificate.class.getCanonicalName());		
	}

	@Test ( description = "Verify that certificate can be deleted from web-client", priority=4, 
			groups = { "smime"})
	
	public void DeleteCertificate_01() throws HarnessException  {
		ZimbraAccount user3 = new ZimbraAccount("user3"+ "@" + ConfigProperties.getStringProperty("testdomain", "testdomain.com"), null);
		user3.provision();
		user3.authenticate();

		// Modify the test account and change zimbraFeatureSMIMEEnabled to TRUE
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user3.ZimbraId +"</id>"
			+		"<a n='zimbraFeatureSMIMEEnabled'>TRUE</a>"
			+	"</ModifyAccountRequest>");
		
		// Create file item
		String filePath = ConfigProperties.getBaseDirectory()
				+ "/data/private/certs/user3_digitalid.p12";

		// Upload file to server through RestUtil
		String attachmentId = user3.uploadFile(filePath);

		user3.soapSend(
				"<SaveSmimeCertificateRequest xmlns='urn:zimbraAccount'>" +
				"<upload id='" + attachmentId + "'></upload>" +
                "<password>test123</password>" +
                "</SaveSmimeCertificateRequest>");
		
        app.zPageMain.zLogout();
		app.zPageLogin.zLogin(user3);
		
		app.zPagePreferences.zNavigateTo();
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.SecureEmail);
		
		//Click on delete certificate link
		app.zPagePreferences.sClick(Locators.zRemoveCertificateLink);
		DialogWarning dialog = (DialogWarning) new DialogWarning(DialogWarning.DialogWarningID.RemoveCertificate, app, app.zPagePreferences);
		dialog.zClickButton(Button.B_YES);
		
		//UI verification, verify that browse button is visible again
        ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(Locators.zBrowseToCertificate), "Verify that browse button is present after removing certificate.");
        
        //Soap Verification
        user3.soapSend("<GetSmimeCertificateInfoRequest xmlns='urn:zimbraAccount'/>");
        String publicCertId = user3.soapSelectValue("//acct:GetSmimeCertificateInfoResponse/acct:certificate", "pubCertId");
        String privateCertId = user3.soapSelectValue("//acct:GetSmimeCertificateInfoResponse/acct:certificate", "pvtKeyId");
        ZAssert.assertNull(publicCertId, "Public certificate not returned");
        ZAssert.assertNull(privateCertId, "Private certificate not returned");
        
        //Deleting the account created for the test-case
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<DeleteAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user3.ZimbraId + "</id>"
			+	"</DeleteAccountRequest>");

	}
	
}
