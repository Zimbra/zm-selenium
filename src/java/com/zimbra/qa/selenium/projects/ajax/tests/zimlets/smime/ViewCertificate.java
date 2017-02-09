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
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.PagePreferences.Locators;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;
import com.zimbra.qa.selenium.projects.ajax.ui.zimlet.DialogViewCertificate;

public class ViewCertificate extends AjaxCommonTest {

	public ViewCertificate() {
		
		logger.info("New "+ ViewCertificate.class.getCanonicalName());		
	}

	@Test ( description = "Verify that certificate can be viewwed from web-client", priority=4, 
			groups = { "smime", "L4"})
	
	public void ViewCertificate_01() throws HarnessException  {
		
		String issuedToOrganization = "Synacor";
		String issuedByOrganization = "Zimbra";
		String issuedByEmail = "admin@testdomain.com";
		String algorithm = "SHA256with RSA";
		
		ZimbraAccount user4 = new ZimbraAccount("user4"+ "@" + ConfigProperties.getStringProperty("testdomain", "testdomain.com"), null);
		user4.provision();
		user4.authenticate();

		// Modify the test account and change zimbraFeatureSMIMEEnabled to TRUE
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user4.ZimbraId +"</id>"
			+		"<a n='zimbraFeatureSMIMEEnabled'>TRUE</a>"
			+	"</ModifyAccountRequest>");
		
		// Create file item
		String filePath = ConfigProperties.getBaseDirectory()
				+ "/data/private/certs/user4_digitalid.p12";

		// Upload file to server through RestUtil
		String attachmentId = user4.uploadFile(filePath);

		user4.soapSend(
				"<SaveSmimeCertificateRequest xmlns='urn:zimbraAccount'>" +
				"<upload id='" + attachmentId + "'></upload>" +
                "<password>test123</password>" +
                "</SaveSmimeCertificateRequest>");
		
        app.zPageMain.zLogout();
		app.zPageLogin.zLogin(user4);
		
		app.zPagePreferences.zNavigateTo();
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.SecureEmail);
		
		//Click on view Certificate Link
		app.zPagePreferences.sClick(Locators.zViewCertificateLink);
		
		//View Certificate dialog opens up
		DialogViewCertificate dialog = (DialogViewCertificate) new DialogViewCertificate(app, app.zPagePreferences);
		
		//Verifying various certificate fields
		ZAssert.assertEquals(user4.EmailAddress, dialog.zGetDisplayedText(user4.EmailAddress),"Issued to email address matched");
		ZAssert.assertEquals(issuedToOrganization, dialog.zGetDisplayedTextIssuedToOrganization(),"Issued to Organisation matched");
		ZAssert.assertEquals(issuedByOrganization, dialog.zGetDisplayedTextIssuedByOrganization(),"Issued by Organisation matched");
		ZAssert.assertEquals(issuedByEmail, dialog.zGetDisplayedTextIssuedByEmail(),"Issued by email address matched");
		ZAssert.assertEquals(algorithm, dialog.zGetDisplayedTextAlgorithm(),"Algorithm matched");
		
        //Deleting the account created for the test-case
		ZimbraAdminAccount.GlobalAdmin().soapSend(
				"<DeleteAccountRequest xmlns='urn:zimbraAdmin'>"
			+		"<id>"+ user4.ZimbraId + "</id>"
			+	"</DeleteAccountRequest>");

	}
	
}
