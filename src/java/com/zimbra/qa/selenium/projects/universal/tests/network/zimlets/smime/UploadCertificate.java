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
package com.zimbra.qa.selenium.projects.universal.tests.network.zimlets.smime;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.PagePreferences.Locators;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.TreePreferences.TreeItem;
import com.zimbra.qa.selenium.projects.universal.ui.zimlet.DialogPasswordRequired;

public class UploadCertificate extends AjaxCommonTest {

	public UploadCertificate() {
		
		logger.info("New "+ UploadCertificate.class.getCanonicalName());		
	}

	@Test ( description = "Verify that certificate which requires password can be uploaded successfully", priority=2, 
			groups = { "sanity", "L0", "network"})
	
	public void UploadCertificateWithPassword_01() throws HarnessException  {
		
		//Create specific account due to certificate restrictions
		ZimbraAccount user1 = new ZimbraAccount("user1"+ "@" + ConfigProperties.getStringProperty("testdomain", "testdomain.com"), null);
		String certPassword = "test123";
		user1.provision();
		user1.authenticate();

		// Modify the test account and change zimbraFeatureSMIMEEnabled to TRUE
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user1.ZimbraId +"</id>"
			+		"<a n='zimbraFeatureSMIMEEnabled'>TRUE</a>"
			+	"</ModifyAccountRequest>");
		
        app.zPageMain.zLogout();
		app.zPageLogin.zLogin(user1);
		
		// Create file item
		final String fileName = "user1_digitalid.p12";
		final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\private\\certs\\" + fileName;
		
		//Navigate to preferences
		app.zPagePreferences.zNavigateTo();
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.SecureEmail);
		
		//Click on browse button
		app.zPagePreferences.zToolbarPressButton(Button.B_BROWSE_TO_CERTIFICATE);
		
		//Browse and Upload file
		zUploadFile(filePath);
		SleepUtil.sleepMedium();
		
		//Enter password for certificate
		DialogPasswordRequired dialog = (DialogPasswordRequired) new DialogPasswordRequired(app,app.zPagePreferences);
		dialog.zEnterPassword(certPassword);
		
		dialog.zClickButton(Button.B_SUBMIT);
		SleepUtil.sleepMedium();
		
		//UI verifications
        ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(Locators.zRemoveCertificateLink), "Verify that remove link is present after adding certificate.");
        ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(Locators.zViewCertificateLink), "Verify that view link is present after adding certificate.");
        
        //Soap verifications
        user1.soapSend("<GetSmimeCertificateInfoRequest xmlns='urn:zimbraAccount'/>");
        String publicCertId = user1.soapSelectValue("//acct:GetSmimeCertificateInfoResponse/acct:certificate", "pubCertId");
        String privateCertId = user1.soapSelectValue("//acct:GetSmimeCertificateInfoResponse/acct:certificate", "pvtKeyId");
        ZAssert.assertNotNull(publicCertId, "Public certificate Id returned");
        ZAssert.assertNotNull(privateCertId, "Private certificate Id returned");

        //Deleting the account created for the test-case
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<DeleteAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user1.ZimbraId + "</id>"
			+	"</DeleteAccountRequest>");
        
	}
	
	@Test ( description = "Verify that certificate which does not require password can be uploaded successfully", priority=2, 
			groups = { "smoke", "L1", "network"})	
	
	public void UploadCertificateWithoutPassword_02() throws HarnessException  {
		
		//Create specific account due to certificate restrictions
		ZimbraAccount user2 = new ZimbraAccount("user2"+ "@" + ConfigProperties.getStringProperty("testdomain", "testdomain.com"), null);
		user2.provision();
		user2.authenticate();

		// Modify the test account and change zimbraFeatureSMIMEEnabled to TRUE
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user2.ZimbraId +"</id>"
			+		"<a n='zimbraFeatureSMIMEEnabled'>TRUE</a>"
			+	"</ModifyAccountRequest>");
		
        app.zPageMain.zLogout();
		app.zPageLogin.zLogin(user2);
		
		// Create file item
		final String fileName = "user2_digitalid.p12";
		final String filePath = ConfigProperties.getBaseDirectory() + "\\data\\private\\certs\\" + fileName;
		
		//Navigate to preferences
		app.zPagePreferences.zNavigateTo();
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.SecureEmail);

		//Click on browse button
		app.zPagePreferences.zToolbarPressButton(Button.B_BROWSE_TO_CERTIFICATE);

		//Browse and Upload file
		zUploadFile(filePath);
		SleepUtil.sleepMedium();
		
		//UI verifications
        ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(Locators.zRemoveCertificateLink), "Verify that remove link is present after adding certificate.");
        ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(Locators.zViewCertificateLink), "Verify that view link is present after adding certificate.");
        
        //Soap verifications
        user2.soapSend("<GetSmimeCertificateInfoRequest xmlns='urn:zimbraAccount'/>");
        String publicCertId = user2.soapSelectValue("//acct:GetSmimeCertificateInfoResponse/acct:certificate", "pubCertId");
        String privateCertId = user2.soapSelectValue("//acct:GetSmimeCertificateInfoResponse/acct:certificate", "pvtKeyId");
        ZAssert.assertNotNull(publicCertId, "Public certificate Id returned");
        ZAssert.assertNotNull(privateCertId, "Private certificate Id returned");

	}
	
}
