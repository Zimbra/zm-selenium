/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.folders.accounts;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.Toaster;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.PageMail.PageMailView;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.TreeMail.Locators;

public class GetExternalIMAP extends PrefGroupMailByMessageTest {

	public GetExternalIMAP() {
		logger.info("New "+ GetExternalIMAP.class.getCanonicalName());
	}

	/**
	 * Objective: View an external folder - POP
	 *
	 * 1. Create an account on the server
	 * 2. Put a message in the inbox
	 * 3. Login to ajax
	 * 4. Create a folder
	 * 5. Add a data source to the account from step 1, associate with the folder in step 4
	 * 6. Right click on the folder -> Get external mail
	 * 7. Verify the message from step 2 appears
	 *
	 * @throws HarnessException
	 */

	@Test( description = "View an external folder - IMAP",
			groups = { "smoke", "L1" })

	public void GetExternalIMAP_01() throws HarnessException {

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
		String datasourceImapPort = ConfigProperties.getStringProperty("server.imap.port");
		String datasourceImapType = ConfigProperties.getStringProperty("server.imap.type");

		app.zGetActiveAccount().soapSend(
				"<CreateDataSourceRequest xmlns='urn:zimbraMail'>"
			+		"<imap name='"+ datasourcename +"' l='"+ folder.getId() +"' isEnabled='true' "
			+			"port='"+ datasourceImapPort +"' host='"+ app.zGetActiveAccount().zGetAccountStoreHost() +"' connectionType='"+ datasourceImapType +"' leaveOnServer='true' "
			+			"username='"+ external.EmailAddress +"' password='"+ external.Password +"' "
			+			"useAddressForForwardReply='true' replyToDisplay='Bar Foo' replyToAddress='"+ app.zGetActiveAccount().EmailAddress +"' "
			+			"fromDisplay='Foo Bar' fromAddress='"+ app.zGetActiveAccount().EmailAddress +"' />"
			+	"</CreateDataSourceRequest>");

		app.zPageMain.sRefresh();

		// If the datasource has never been synced, then an empty title bar appears
		app.zTreeMail.zRightClickAt("css=div[id='zov__main_Mail'] td[id$='_textCell']:contains("+ foldername +")", "");
		app.zTreeMail.zWaitForBusyOverlay();
		app.zTreeMail.zClickAt(Locators.ContextMenuTVFoldersCSS + " div[id^='SYNC'] td[id$='_title']", "");
		app.zTreeMail.zWaitForBusyOverlay();

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

		// See: https://bugzilla.zimbra.com/show_bug.cgi?id=66447
		// Get the folder from the server
		app.zGetActiveAccount().soapSend("<GetFolderRequest xmlns='urn:zimbraMail'/>");
		String externalInbox = app.zGetActiveAccount().soapSelectValue("//mail:folder[@name='"+ foldername +"']//mail:folder[@name='INBOX']", "id");

		String locator = "css=td[id='zti__main_Mail__" + externalInbox +"_textCell']";
		app.zPageMail.zWaitForElementPresent(locator);

		// Click on the Inbox
		app.zTreeMail.zClickAt(locator, "");
		String listLocator = null;
		String rowLocator = null;
		if (app.zPageMail.zGetPropMailView() == PageMailView.BY_MESSAGE) {
		    	listLocator = "css=div[id='zl__TV-main__rows']";
			rowLocator = "div[id^='zli__TV-main__']";
		} else {
			listLocator = "css=div[id='zl__CLV-main__rows']";
			rowLocator = "div[id^='zli__CLV-main__']";
		}

		// Make sure the list exists
		app.zPageMail.zWaitForElementPresent(listLocator+ " " + rowLocator);

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


	@Test( description = "IMAP: get updates from the external account - 'refresh' button",
			groups = { "functional", "L2" })

	public void GetExternalIMAP_02() throws HarnessException {

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
		String datasourceImapPort = ConfigProperties.getStringProperty("server.imap.port");
		String datasourceImapType = ConfigProperties.getStringProperty("server.imap.type");

		app.zGetActiveAccount().soapSend(
				"<CreateDataSourceRequest xmlns='urn:zimbraMail'>"
			+		"<imap name='"+ datasourcename +"' l='"+ folder.getId() +"' isEnabled='true' "
			+			"port='"+ datasourceImapPort +"' host='"+ app.zGetActiveAccount().zGetAccountStoreHost() +"' connectionType='"+ datasourceImapType +"' leaveOnServer='true' "
			+			"username='"+ external.EmailAddress +"' password='"+ external.Password +"' "
			+			"useAddressForForwardReply='true' replyToDisplay='Bar Foo' replyToAddress='"+ app.zGetActiveAccount().EmailAddress +"' "
			+			"fromDisplay='Foo Bar' fromAddress='"+ app.zGetActiveAccount().EmailAddress +"' />"
			+	"</CreateDataSourceRequest>");

		app.zPageMain.sRefresh();

		// If the datasource has never been synced, then an empty title bar appears
		app.zTreeMail.zRightClickAt("css=div[id='zov__main_Mail'] td[id$='_textCell']:contains("+ foldername +")", "");
		app.zTreeMail.zWaitForBusyOverlay();
		app.zTreeMail.zClickAt(Locators.ContextMenuTVFoldersCSS + " div[id^='SYNC'] td[id$='_title']", "");
		app.zTreeMail.zWaitForBusyOverlay();

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

		// See: https://bugzilla.zimbra.com/show_bug.cgi?id=66447
		// Get the folder from the server
		app.zGetActiveAccount().soapSend("<GetFolderRequest xmlns='urn:zimbraMail'/>");
		String externalInbox = app.zGetActiveAccount().soapSelectValue("//mail:folder[@name='"+ foldername +"']//mail:folder[@name='INBOX']", "id");

		// Click on the Inbox
		app.zTreeMail.zClickAt("css=td[id='zti__main_Mail__" + externalInbox +"_textCell']", "");

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

		SleepUtil.sleepMedium();

		// Sync is asynchronous, so we have to wait for the toaster
		toaster = app.zPageMain.zGetToaster();
		toaster.zWaitForActive();
		SleepUtil.sleepMedium();
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zWaitForBusyOverlay();
		SleepUtil.sleepMedium();
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zWaitForBusyOverlay();
		SleepUtil.sleepMedium();

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


	@Test( description = "IMAP: get updates from the external account - right click -> sync",
			groups = { "functional", "L2" })

	public void GetExternalIMAP_03() throws HarnessException {

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
		String datasourceImapPort = ConfigProperties.getStringProperty("server.imap.port");
		String datasourceImapType = ConfigProperties.getStringProperty("server.imap.type");

		app.zGetActiveAccount().soapSend(
				"<CreateDataSourceRequest xmlns='urn:zimbraMail'>"
			+		"<imap name='"+ datasourcename +"' l='"+ folder.getId() +"' isEnabled='true' "
			+			"port='"+ datasourceImapPort +"' host='"+ app.zGetActiveAccount().zGetAccountStoreHost() +"' connectionType='"+ datasourceImapType +"' leaveOnServer='true' "
			+			"username='"+ external.EmailAddress +"' password='"+ external.Password +"' "
			+			"useAddressForForwardReply='true' replyToDisplay='Bar Foo' replyToAddress='"+ app.zGetActiveAccount().EmailAddress +"' "
			+			"fromDisplay='Foo Bar' fromAddress='"+ app.zGetActiveAccount().EmailAddress +"' />"
			+	"</CreateDataSourceRequest>");

		app.zPageMain.sRefresh();

		// If the datasource has never been synced, then an empty title bar appears
		app.zTreeMail.zRightClickAt("css=div[id='zov__main_Mail'] td[id$='_textCell']:contains("+ foldername +")", "");
		app.zTreeMail.zWaitForBusyOverlay();
		app.zTreeMail.zClickAt(Locators.ContextMenuTVFoldersCSS + " div[id^='SYNC'] td[id$='_title']", "");
		app.zTreeMail.zWaitForBusyOverlay();

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

		// See: https://bugzilla.zimbra.com/show_bug.cgi?id=66447
		// Get the folder from the server
		app.zGetActiveAccount().soapSend("<GetFolderRequest xmlns='urn:zimbraMail'/>");
		String externalInbox = app.zGetActiveAccount().soapSelectValue("//mail:folder[@name='"+ foldername +"']//mail:folder[@name='INBOX']", "id");

		// Click on the Inbox
		app.zTreeMail.zClickAt("css=td[id='zti__main_Mail__" + externalInbox +"_textCell']", "");

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

		// Right click on account - > Sync
		app.zTreeMail.zRightClickAt("css=div[id='zov__main_Mail'] td[id$='_textCell']:contains("+ foldername +")", "");
		app.zTreeMail.zWaitForBusyOverlay();
		app.zTreeMail.zClickAt(Locators.ContextMenuTVFoldersCSS + " div[id^='SYNC'] td[id$='_title']", "");
		app.zTreeMail.zWaitForBusyOverlay();

		// Sync is asynchronous, so we have to wait for the toaster
		toaster = app.zPageMain.zGetToaster();
		toaster.zWaitForActive();
		SleepUtil.sleepVeryLong();

		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		app.zTreeMail.zWaitForBusyOverlay();

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
