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
package com.zimbra.qa.selenium.projects.universal.tests.mail.folders.accounts;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.universal.pages.Toaster;
import com.zimbra.qa.selenium.projects.universal.pages.mail.PageMail.PageMailView;
import com.zimbra.qa.selenium.projects.universal.pages.mail.TreeMail.Locators;

public class GetGmailImap extends SetGroupMailByMessagePreference {

	public GetGmailImap() {
		logger.info("New "+ GetGmailImap.class.getCanonicalName());
	}

	/**
	 * Objective: View an Gmail folder - IMAP
	 *
	 * 1. Login to universal
	 * 2. Create a folder
	 * 3. Add external account as Gmail
	 * 4. Right click on the folder -> Get external mail
	 * 5. Verify the message from step 2 appears
	 *
	 * @throws HarnessException
	 */

	@Test (description = "View an external Gmail - IMAP",
			groups = { "smoke", "L1" })

	public void GetExternalGmailIMAP_01() throws HarnessException {

		// Create the external data source on the same server
		String external = "testzimbra123@gmail.com";
		String Password="zimbra@123";
		String subject = "Your account settings in one place at My Account" ;

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
		String datasourceHost = "imap.gmail.com";
		String datasourceImapPort = ConfigProperties.getStringProperty("server.imap.port");
		String datasourceImapType = ConfigProperties.getStringProperty("server.imap.type");

		app.zGetActiveAccount().soapSend(
			"<CreateDataSourceRequest xmlns='urn:zimbraMail'>"
					+		"<imap name='"+ datasourcename +"' l='"+ folder.getId() +"' isEnabled='true' "
					+			"port='"+ datasourceImapPort +"' host='"+ datasourceHost +"' connectionType='"+ datasourceImapType +"' leaveOnServer='true' "
					+			"username='"+ external +"' password='"+ Password +"' "
					+			"useAddressForForwardReply='true' replyToDisplay='Bar Foo' replyToAddress='"+ app.zGetActiveAccount().EmailAddress +"' "
					+			"fromDisplay='Foo Bar' fromAddress='"+ app.zGetActiveAccount().EmailAddress +"' />"
					+	"</CreateDataSourceRequest>");

		app.zPageMain.zRefreshMainUI();

		// If the datasource has never been synced, then an empty title bar appears
		app.zTreeMail.sRightClickAt("css=div[id='zov__main_Mail'] td[id$='_textCell']:contains("+ foldername +")", "");
		app.zTreeMail.zWaitForBusyOverlay();
		app.zTreeMail.sClickAt(Locators.ContextMenuTVFoldersCSS + " div[id^='SYNC'] td[id$='_title']", "");
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
		String externalInbox = app.zGetActiveAccount().soapSelectValue("//mail:folder[@name='"+ foldername +"']//mail:folder[@name='Inbox']", "id");

		// Click on the Inbox
		app.zTreeMail.sClickAt("css=td[id='zti__main_Mail__" + externalInbox +"_textCell']", "");

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

}
