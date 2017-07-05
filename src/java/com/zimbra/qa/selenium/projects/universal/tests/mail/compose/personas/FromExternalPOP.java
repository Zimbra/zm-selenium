/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.mail.compose.personas;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.universal.ui.mail.FormMailNew.Field;

public class FromExternalPOP extends PrefGroupMailByMessageTest {

	public FromExternalPOP() {
		logger.info("New "+ FromExternalPOP.class.getCanonicalName());
	}
	
	@Test( description = "Compose message from - External POP",
			groups = { "smoke", "L1" })
	
	public void FromExternalPOP_01() throws HarnessException {
		
		// Create the external data source on the same server
		ZimbraAccount external = new ZimbraAccount();
		external.provision();
		external.authenticate();

		// Create the folder to put the data source
		String foldername = "external" + ConfigProperties.getUniqueString();
		
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ foldername +"' l='1'/>" +
                "</CreateFolderRequest>");

		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);
		ZAssert.assertNotNull(folder, "Verify the subfolder is available");
		
		// Create the data source
		String datasourcename = "datasource" + ConfigProperties.getUniqueString();
		String datasourceHost = ConfigProperties.getStringProperty("server.host");
		String datasourcePopPort = ConfigProperties.getStringProperty("server.pop.port");
		String datasourcePopType = ConfigProperties.getStringProperty("server.pop.type");
		
		app.zGetActiveAccount().soapSend(
				"<CreateDataSourceRequest xmlns='urn:zimbraMail'>"
			+		"<pop3 name='"+ datasourcename +"' l='"+ folder.getId() +"' isEnabled='true' "
			+			"port='"+ datasourcePopPort +"' host='"+ datasourceHost +"' connectionType='"+ datasourcePopType +"' leaveOnServer='true' "
			+			"username='"+ external.EmailAddress +"' password='"+ external.Password +"' />"
			+	"</CreateDataSourceRequest>");

		// Refresh UI
		app.zPageMain.sRefresh();
		
		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		
		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");
		
		// Fill out the form with the data
		mailform.zFillField(Field.From, external.EmailAddress);
		mailform.zFillField(Field.To, ZimbraAccount.AccountA().EmailAddress);
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, "content" + ConfigProperties.getUniqueString());
		mailform.zSubmit();
		SleepUtil.sleepLong(); // search request doesn't find mail

		// Verify the message shows as from the alias
		ZimbraAccount.AccountA().soapSend(
					"<SearchRequest types='message' xmlns='urn:zimbraMail'>"
			+			"<query>subject:("+ subject +")</query>"
			+		"</SearchRequest>");
		String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");

		ZimbraAccount.AccountA().soapSend(
					"<GetMsgRequest xmlns='urn:zimbraMail'>"
			+			"<m id='"+ id +"' html='1'/>"
			+		"</GetMsgRequest>");

		/**
		 * Since we are using Zimbra to Zimbra to set up the external
		 * account, the MTA knows both accounts and allows the
		 * Zimbra user to send from the External account directly,
		 * without the OBO.
		 * 
		 * Due to limitations in the WDC (5/22/2012), external
		 * accounts cannot be set up with third party servers.
		 * (Maybe a stand alone Zimbra server may be used.)
		 * 
		 * If this test case is executed with a third party
		 * server, you should see:
		 * From: Zimbra
		 * OBO: External
		 * 
		 */
		
		// Verify From: alias
		String address = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='f']", "a");
		ZAssert.assertEquals(address, app.zGetActiveAccount().EmailAddress, "In the Zimbra-Zimbra config, verify the from is the Zimbra email address");
		
	}

}
