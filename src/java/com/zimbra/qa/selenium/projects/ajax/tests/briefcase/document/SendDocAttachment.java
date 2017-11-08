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
package com.zimbra.qa.selenium.projects.ajax.tests.briefcase.document;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.DocumentItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.XmlStringUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.core.FeatureBriefcaseTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.ui.briefcase.PageBriefcase;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;

public class SendDocAttachment extends FeatureBriefcaseTest {

	public SendDocAttachment() {
		logger.info("New " + SendDocAttachment.class.getCanonicalName());
		super.startingPage = app.zPageBriefcase;
		super.startingAccountPreferences.put("zimbraPrefBriefcaseReadingPaneLocation", "bottom");
	}


	@Test (description = "Create document through SOAP - click Send as attachment, Cancel & verify through GUI",
			groups = { "functional", "L2" })

	public void SendDocAttachment_01() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create document item
		DocumentItem docItem = new DocumentItem();

		String docName = docItem.getName();
		String docText = docItem.getDocText();

		// Create document using SOAP
		String contentHTML = XmlStringUtil.escapeXml("<html>" + "<body>" + docText + "</body>" + "</html>");

		account.soapSend("<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>"
						+ "<doc name='"
						+ docName
						+ "' l='"
						+ briefcaseFolder.getId()
						+ "' ct='application/x-zimbra-doc'>"
						+ "<content>"
						+ contentHTML
						+ "</content>"
						+ "</doc>"
						+ "</SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Click on created document
		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, docItem);

		// Click on Send as attachment
		FormMailNew mailform;
		mailform = (FormMailNew) app.zPageBriefcase.zToolbarPressPulldown(
					Button.B_ACTIONS, Button.O_SEND_AS_ATTACHMENT, docItem);

		// Verify the new mail form has attachment
		ZAssert.assertTrue(app.zPageBriefcase.zWaitForElementPresent(
				PageBriefcase.Locators.zAttachmentText.locator + ":contains(" + docName + ")", "3000"),
				"Verify the attachment text");

		// Cancel the message
		// A warning dialog should appear regarding losing changes
		DialogWarning warningDlg = (DialogWarning) mailform
				.zToolbarPressButton(Button.B_CANCEL);

		ZAssert.assertNotNull(warningDlg, "Verify the dialog is returned");

		// Dismiss the dialog
		//warningDlg.zPressButton(Button.B_NO);
		// Click No on warning dialog
		app.zPageBriefcase.sClick("//div[@id='YesNoCancel']//td[contains(@id,'No_')]//td[contains(@id,'_title')]");


		// Make sure the dialog is dismissed
		warningDlg.zWaitForClose();

		// Delete document upon test completion
		app.zPageBriefcase.deleteFileByName(docItem.getName());
	}


	@Test (description = "Send document as attachment using Right Click Context Menu & verify through GUI",
			groups = { "functional", "L2" })

	public void SendDocAttachment_02() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create document item
		DocumentItem docItem = new DocumentItem();

		String docName = docItem.getName();
		String docText = docItem.getDocText();

		// Create document using SOAP
		String contentHTML = XmlStringUtil.escapeXml("<html>" + "<body>" + docText + "</body>" + "</html>");

		account.soapSend("<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>"
						+ "<doc name='"
						+ docName
						+ "' l='"
						+ briefcaseFolder.getId()
						+ "' ct='application/x-zimbra-doc'>"
						+ "<content>"
						+ contentHTML
						+ "</content>"
						+ "</doc>"
						+ "</SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Click on created document
		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, docItem);

		// Click on Send as attachment using Right Click Context Menu
		FormMailNew mailform = (FormMailNew) app.zPageBriefcase.zListItem(
				Action.A_RIGHTCLICK, Button.O_SEND_AS_ATTACHMENT, docItem);

		// Verify the new mail form has attachment
		ZAssert.assertTrue(app.zPageBriefcase.zWaitForElementPresent(
				PageBriefcase.Locators.zAttachmentText.locator + ":contains(" + docName + ")", "3000"),
				"Verify the attachment text");

		// Cancel the message
		// A warning dialog should appear regarding losing changes
		DialogWarning warningDlg = (DialogWarning) mailform.zToolbarPressButton(Button.B_CANCEL);

		ZAssert.assertNotNull(warningDlg, "Verify the dialog is returned");

		// Dismiss the dialog
		//warningDlg.zPressButton(Button.B_NO);
		// Click No on warning dialog
		app.zPageBriefcase.sClick("//div[@id='YesNoCancel']//td[contains(@id,'No_')]//td[contains(@id,'_title')]");

		warningDlg.zWaitForClose(); // Make sure the dialog is dismissed

		// Delete document upon test completion
		app.zPageBriefcase.deleteFileByName(docItem.getName());
	}
}
