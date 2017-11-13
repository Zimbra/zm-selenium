/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.mail.folders.accounts;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.universal.ui.Toaster;

public class GetExternalPOP extends PrefGroupMailByMessageTest {

	public GetExternalPOP() {
		logger.info("New "+ GetExternalPOP.class.getCanonicalName());
	}

	/**
	 * Objective: View an external folder - POP
	 *
	 * 1. Create an account on the server
	 * 2. Put a message in the inbox
	 * 3. Login to universal
	 * 4. Create a folder
	 * 5. Add a data source to the account from step 1, associate with the folder in step 4
	 * 6. Right click on the folder -> Get external mail
	 * 7. Verify the message from step 2 appears
	 *
	 * @throws HarnessException
	 */

	@Test (description = "View an external folder - POP",
			groups = { "smoke", "L1" })

	public void GetExternalPOP_01() throws HarnessException {

		// Create the external data source on the same server
		ZimbraAccount external = new ZimbraAccount();
		external.provision();
		external.authenticate();

		// Add a message to the inbox
		String subject = "subject" + ConfigProperties.getUniqueString();

		external.soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
    		+		"<m l='"+ FolderItem.importFromSOAP(external, SystemFolder.Inbox).getId() +"' f='u'>"
        	+			"<content>From: foo@foo.com\n"
        	+				"To: foo@foo.com \n"
        	+				"Subject: "+ subject +"\n"
        	+				"MIME-Version: 1.0 \n"
        	+				"Content-Type: text/plain; charset=utf-8 \n"
        	+				"Content-Transfer-Encoding: 7bit\n"
        	+				"\n"
        	+				"simple text string in the body\n"
        	+			"</content>"
        	+		"</m>"
			+	"</AddMsgRequest>");

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
		String datasourcePopPort = ConfigProperties.getStringProperty("server.pop.port");
		String datasourcePopType = ConfigProperties.getStringProperty("server.pop.type");

		app.zGetActiveAccount().soapSend(
				"<CreateDataSourceRequest xmlns='urn:zimbraMail'>"
			+		"<pop3 name='"+ datasourcename +"' l='"+ folder.getId() +"' isEnabled='true' "
			+			"port='"+ datasourcePopPort +"' host='"+ app.zGetActiveAccount().zGetAccountStoreHost() +"' connectionType='"+ datasourcePopType +"' leaveOnServer='true' "
			+			"username='"+ external.EmailAddress +"' password='"+ external.Password +"' "
			+			"useAddressForForwardReply='true' replyToDisplay='Bar Foo' replyToAddress='"+ app.zGetActiveAccount().EmailAddress +"' "
			+			"fromDisplay='Foo Bar' fromAddress='"+ app.zGetActiveAccount().EmailAddress +"' />"
			+	"</CreateDataSourceRequest>");

		app.zPageMain.zRefreshMainUI();

		// Click on the folder and select Sync
		app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_FOLDER_GET_EXTERNAL, folder);

		// Sync is asynchronous, so we have to wait for the toaster
		Toaster toaster = app.zPageMain.zGetToaster();
		toaster.zWaitForActive();
		SleepUtil.sleepMedium();
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zWaitForBusyOverlay();
		SleepUtil.sleepMedium();
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zWaitForBusyOverlay();
		SleepUtil.sleepMedium();

		// Click on the folder, and verify the message appears
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, folder);

		// Get the messages
		List<MailItem> messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		// Make sure the message appears in the list
		MailItem found = null;
		for (MailItem m : messages) {
			logger.info("Subject: looking for "+ subject +" found: "+ m.gSubject);
			if ( subject.equals(m.gSubject) ) {
				found = m;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the message is in the external folder");

	}


	/**
	 * Objective: View an external folder - POP
	 *
	 * 1. Create an account on the server
	 * 2. Put a message in the inbox
	 * 3. Login to universal
	 * 4. Create a folder
	 * 5. Add a data source to the account from step 1, associate with the folder in step 4
	 * 6. Select the folder (http://bugzilla.zimbra.com/show_bug.cgi?id=66528#c5)
	 * 7. Click "Refresh"
	 * 7. Verify the message from step 2 appears
	 *
	 * @throws HarnessException
	 */

	@Test (description = "POP: get updates from the external account - 'refresh' button",
			groups = { "functional", "L2" })

	public void GetExternalPOP_02() throws HarnessException {

		// Create the external data source on the same server
		ZimbraAccount external = new ZimbraAccount();
		external.provision();
		external.authenticate();

		// Add a message to the inbox
		String subject = "subject" + ConfigProperties.getUniqueString();

		external.soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
    		+		"<m l='"+ FolderItem.importFromSOAP(external, SystemFolder.Inbox).getId() +"' f='u'>"
        	+			"<content>From: foo@foo.com\n"
        	+				"To: foo@foo.com \n"
        	+				"Subject: "+ subject +"\n"
        	+				"MIME-Version: 1.0 \n"
        	+				"Content-Type: text/plain; charset=utf-8 \n"
        	+				"Content-Transfer-Encoding: 7bit\n"
        	+				"\n"
        	+				"simple text string in the body\n"
        	+			"</content>"
        	+		"</m>"
			+	"</AddMsgRequest>");

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
		String datasourcePopPort = ConfigProperties.getStringProperty("server.pop.port");
		String datasourcePopType = ConfigProperties.getStringProperty("server.pop.type");

		app.zGetActiveAccount().soapSend(
				"<CreateDataSourceRequest xmlns='urn:zimbraMail'>"
			+		"<pop3 name='"+ datasourcename +"' l='"+ folder.getId() +"' isEnabled='true' "
			+			"port='"+ datasourcePopPort +"' host='"+ app.zGetActiveAccount().zGetAccountStoreHost() +"' connectionType='"+ datasourcePopType +"' leaveOnServer='true' "
			+			"username='"+ external.EmailAddress +"' password='"+ external.Password +"' "
			+			"useAddressForForwardReply='true' replyToDisplay='Bar Foo' replyToAddress='"+ app.zGetActiveAccount().EmailAddress +"' "
			+			"fromDisplay='Foo Bar' fromAddress='"+ app.zGetActiveAccount().EmailAddress +"' />"
			+	"</CreateDataSourceRequest>");

		app.zPageMain.zRefreshMainUI();

		// Click on the folder and select Sync
		app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_FOLDER_GET_EXTERNAL, folder);

		// Sync is asynchronous, so we have to wait for the toaster
		Toaster toaster = app.zPageMain.zGetToaster();
		toaster.zWaitForActive();
		SleepUtil.sleepMedium();
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zWaitForBusyOverlay();
		SleepUtil.sleepMedium();
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zWaitForBusyOverlay();
		SleepUtil.sleepMedium();

		// Add another message
		String subject2 = "subject" + ConfigProperties.getUniqueString();
		external.soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
    		+		"<m l='"+ FolderItem.importFromSOAP(external, SystemFolder.Inbox).getId() +"' f='u'>"
        	+			"<content>From: foo@foo.com\n"
        	+				"To: foo@foo.com \n"
        	+				"Subject: "+ subject2 +"\n"
        	+				"MIME-Version: 1.0 \n"
        	+				"Content-Type: text/plain; charset=utf-8 \n"
        	+				"Content-Transfer-Encoding: 7bit\n"
        	+				"\n"
        	+				"simple text string in the body\n"
        	+			"</content>"
        	+		"</m>"
			+	"</AddMsgRequest>");

		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, folder);

		// Click to Refresh
		app.zPageMain.zToolbarPressButton(Button.B_REFRESH);

		// Sync is asynchronous, so we have to wait for the toaster
		toaster = app.zPageMain.zGetToaster();
		toaster.zWaitForActive();

		// Click on the folder, and verify the message appears
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, folder);

		// Get the messages
		List<MailItem> messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		// Make sure the message appears in the list
		MailItem found = null;
		for (MailItem m : messages) {
			logger.info("Subject: looking for "+ subject2 +" found: "+ m.gSubject);
			if ( subject2.equals(m.gSubject) ) {
				found = m;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the message is in the external folder");

	}


	@Test (description = "POP: get updates from the external account - right click -> sync",
			groups = { "functional", "L2" })

	public void GetExternalPOP_03() throws HarnessException {

		// Create the external data source on the same server
		ZimbraAccount external = new ZimbraAccount();
		external.provision();
		external.authenticate();

		// Add a message to the inbox
		String subject = "subject" + ConfigProperties.getUniqueString();

		external.soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
    		+		"<m l='"+ FolderItem.importFromSOAP(external, SystemFolder.Inbox).getId() +"' f='u'>"
        	+			"<content>From: foo@foo.com\n"
        	+				"To: foo@foo.com \n"
        	+				"Subject: "+ subject +"\n"
        	+				"MIME-Version: 1.0 \n"
        	+				"Content-Type: text/plain; charset=utf-8 \n"
        	+				"Content-Transfer-Encoding: 7bit\n"
        	+				"\n"
        	+				"simple text string in the body\n"
        	+			"</content>"
        	+		"</m>"
			+	"</AddMsgRequest>");

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
		String datasourcePopPort = ConfigProperties.getStringProperty("server.pop.port");
		String datasourcePopType = ConfigProperties.getStringProperty("server.pop.type");

		app.zGetActiveAccount().soapSend(
				"<CreateDataSourceRequest xmlns='urn:zimbraMail'>"
			+		"<pop3 name='"+ datasourcename +"' l='"+ folder.getId() +"' isEnabled='true' "
			+			"port='"+ datasourcePopPort +"' host='"+ app.zGetActiveAccount().zGetAccountStoreHost() +"' connectionType='"+ datasourcePopType +"' leaveOnServer='true' "
			+			"username='"+ external.EmailAddress +"' password='"+ external.Password +"' "
			+			"useAddressForForwardReply='true' replyToDisplay='Bar Foo' replyToAddress='"+ app.zGetActiveAccount().EmailAddress +"' "
			+			"fromDisplay='Foo Bar' fromAddress='"+ app.zGetActiveAccount().EmailAddress +"' />"
			+	"</CreateDataSourceRequest>");

		app.zPageMain.zRefreshMainUI();

		// Click on the folder and select Sync
		app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_FOLDER_GET_EXTERNAL, folder);

		// Sync is asynchronous, so we have to wait for the toaster
		Toaster toaster = app.zPageMain.zGetToaster();
		toaster.zWaitForActive();
		SleepUtil.sleepMedium();
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zWaitForBusyOverlay();
		SleepUtil.sleepMedium();
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zWaitForBusyOverlay();
		SleepUtil.sleepMedium();

		// Add another message
		String subject2 = "subject" + ConfigProperties.getUniqueString();
		external.soapSend(
				"<AddMsgRequest xmlns='urn:zimbraMail'>"
    		+		"<m l='"+ FolderItem.importFromSOAP(external, SystemFolder.Inbox).getId() +"' f='u'>"
        	+			"<content>From: foo@foo.com\n"
        	+				"To: foo@foo.com \n"
        	+				"Subject: "+ subject2 +"\n"
        	+				"MIME-Version: 1.0 \n"
        	+				"Content-Type: text/plain; charset=utf-8 \n"
        	+				"Content-Transfer-Encoding: 7bit\n"
        	+				"\n"
        	+				"simple text string in the body\n"
        	+			"</content>"
        	+		"</m>"
			+	"</AddMsgRequest>");

		// Right click folder -> Sync
		app.zTreeMail.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_FOLDER_GET_EXTERNAL, folder);

		// Sync is asynchronous, so we have to wait for the toaster
		toaster = app.zPageMain.zGetToaster();
		toaster.zWaitForActive();

		// Click on the folder, and verify the message appears
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, folder);

		// Get the messages
		List<MailItem> messages = app.zPageMail.zListGetMessages();
		ZAssert.assertNotNull(messages, "Verify the message list exists");

		// Make sure the message appears in the list
		MailItem found = null;
		for (MailItem m : messages) {
			logger.info("Subject: looking for "+ subject2 +" found: "+ m.gSubject);
			if ( subject2.equals(m.gSubject) ) {
				found = m;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the message is in the external folder");

	}

}
