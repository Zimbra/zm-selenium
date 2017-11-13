/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.briefcase.file;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.FileItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.universal.core.FeatureBriefcaseTest;
import com.zimbra.qa.selenium.projects.universal.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.universal.ui.briefcase.PageBriefcase;
import com.zimbra.qa.selenium.projects.universal.ui.mail.FormMailNew;

public class SendFileAttachment extends FeatureBriefcaseTest {

	public SendFileAttachment() throws HarnessException {
		logger.info("New " + SendFileAttachment.class.getCanonicalName());

		super.startingPage = app.zPageBriefcase;

		//if (ConfigProperties.zimbraGetVersionString().contains("FOSS")) {
		    super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox","TRUE");
		//}
		
		super.startingAccountPreferences.put("zimbraPrefBriefcaseReadingPaneLocation", "bottom");				
	}

	@Test (description = "Upload file through RestUtil - click Send as attachment, Cancel & verify through GUI", 
			groups = { "functional", "L3" })
	public void SendFileAttachment_01() throws HarnessException {
		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account,
				SystemFolder.Briefcase);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory()
				+ "/data/public/other/structure.jpg";

		FileItem fileItem = new FileItem(filePath);

		String fileName = fileItem.getName();

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>"
				+ "<doc l='" + briefcaseFolder.getId() + "'><upload id='"
				+ attachmentId + "'/></doc></SaveDocumentRequest>");

		// refresh briefcase page
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		SleepUtil.sleepSmall();

		// Click on uploaded file
		app.zPageBriefcase.zListItem(Action.A_BRIEFCASE_CHECKBOX, fileItem);
		/*
		if (ConfigProperties.zimbraGetVersionString().contains(
    			"FOSS")) {
		    app.zPageBriefcase.zListItem(Action.A_BRIEFCASE_CHECKBOX, fileItem);

		} else {
		    app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, fileItem);
		}
		*/
		// Click on Send as attachment
		FormMailNew mailform;
		if (ConfigProperties.zimbraGetVersionString().contains("7.1."))
			mailform = (FormMailNew) app.zPageBriefcase.zToolbarPressPulldown(
					Button.B_SEND, Button.O_SEND_AS_ATTACHMENT, fileItem);
		else
			mailform = (FormMailNew) app.zPageBriefcase.zToolbarPressPulldown(
					Button.B_ACTIONS, Button.O_SEND_AS_ATTACHMENT, fileItem);

		// Verify the new mail form has attachment
		ZAssert.assertTrue(app.zPageBriefcase
				.sIsElementPresent(PageBriefcase.Locators.zAttachmentText.locator + ":contains("
						+ fileName + ")"), "Verify the attachment text");

		// Cancel the message
		// A warning dialog should appear regarding losing changes
		DialogWarning warningDlg = (DialogWarning) mailform
				.zToolbarPressButton(Button.B_CANCEL);

		ZAssert.assertNotNull(warningDlg, "Verify the dialog is returned");

		// Dismiss the dialog
		warningDlg.zPressButton(Button.B_NO);

		warningDlg.zWaitForClose(); // Make sure the dialog is dismissed

		// delete file upon test completion
		app.zPageBriefcase.deleteFileByName(fileItem.getName());
	}

	@Test (description = "Send File as attachment using Right Click Context Menu & verify through GUI", 
			groups = { "functional", "L2" })
	public void SendFileAttachment_02() throws HarnessException {
		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account,
				SystemFolder.Briefcase);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory()
				+ "/data/public/other/structure.jpg";

		FileItem fileItem = new FileItem(filePath);

		String fileName = fileItem.getName();

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>"
				+ "<doc l='" + briefcaseFolder.getId() + "'><upload id='"
				+ attachmentId + "'/></doc></SaveDocumentRequest>");

		// SleepUtil.sleepVerySmall();

		// refresh briefcase page
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		SleepUtil.sleepSmall();

		// Click on uploaded file
		app.zPageBriefcase.zListItem(Action.A_BRIEFCASE_CHECKBOX, fileItem);
		/*
		if (ConfigProperties.zimbraGetVersionString().contains(
    			"FOSS")) {
		    app.zPageBriefcase.zListItem(Action.A_BRIEFCASE_CHECKBOX, fileItem);

		} else {
		    app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, fileItem);
		}
		*/
		// Click on Send as attachment using Right Click Context Menu
		FormMailNew mailform = (FormMailNew) app.zPageBriefcase.zListItem(
				Action.A_RIGHTCLICK, Button.O_SEND_AS_ATTACHMENT, fileItem);

		// Verify the new mail form has attachment
		ZAssert.assertTrue(app.zPageBriefcase
				.zWaitForElementPresent(PageBriefcase.Locators.zAttachmentText.locator + ":contains("
						+ fileName + ")"), "Verify the attachment text");

		// Cancel the message
		// A warning dialog should appear regarding losing changes
		DialogWarning warningDlg = (DialogWarning) mailform
				.zToolbarPressButton(Button.B_CANCEL);

		ZAssert.assertNotNull(warningDlg, "Verify the dialog is returned");

		// Dismiss the dialog clicking No
		// warningDlg.zPressButton(Button.B_NO);
		app.zPageBriefcase.sClick("//div[@id='YesNoCancel']//td[contains(@id,'No_')]//td[contains(@id,'_title')]");


		warningDlg.zWaitForClose(); // Make sure the dialog is dismissed

		// delete file upon test completion
		app.zPageBriefcase.deleteFileByName(fileItem.getName());
	}
}
