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
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Shortcut;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.XmlStringUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.EnableBriefcaseFeature;
import com.zimbra.qa.selenium.projects.ajax.pages.*;
import org.testng.annotations.AfterMethod;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;

public class MoveDocument extends EnableBriefcaseFeature {

	public MoveDocument() {
		logger.info("New " + MoveDocument.class.getCanonicalName());
		super.startingPage = app.zPageBriefcase;
		super.startingAccountPreferences.put("zimbraPrefBriefcaseReadingPaneLocation", "bottom");
	}


	@Test (description = "Create document through SOAP - move & verify through GUI",
			groups = { "smoke", "L0" })

	public void MoveDocument_01() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		String briefcaseFolderId = briefcaseFolder.getId();

		String name = "folder" + ConfigProperties.getUniqueString();

		// Create a subfolder to move the message into i.e. Briefcase/subfolder
		account.soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + name + "' l='"
				+ briefcaseFolderId + "'/>" + "</CreateFolderRequest>");

		FolderItem subFolderItem = FolderItem.importFromSOAP(account, name);

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Double Click on created subfolder
		app.zPageBriefcase.zListItem(Action.A_DOUBLECLICK, subFolderItem);

		// Create document item
		DocumentItem docItem = new DocumentItem();

		// Create document using SOAP
		String contentHTML = XmlStringUtil
				.escapeXml("<html>" + "<body>" + docItem.getDocText() + "</body>" + "</html>");

		account.soapSend("<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>" + "<doc name='"
				+ docItem.getName() + "' l='" + briefcaseFolderId + "' ct='application/x-zimbra-doc'>" + "<content>"
				+ contentHTML + "</content>" + "</doc>" + "</SaveDocumentRequest>");

		// document.importFromSOAP(account, document.getDocName());

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Click on created document
		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, docItem);

		// Click move -> subfolder
		app.zPageBriefcase.zToolbarPressPulldown(Button.B_MOVE, subFolderItem);

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		// Verify document was moved from the folder
		ZAssert.assertFalse(app.zPageBriefcase.isPresentInListView(docItem.getName()),
				"Verify document was moved from the folder");

		// Click on subfolder in tree view
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, subFolderItem, true);

		// Verify document was moved to the selected folder
		ZAssert.assertTrue(app.zPageBriefcase.isPresentInListView(docItem.getName()),
				"Verify document was moved to the selected folder");
	}


	@Test (description = "Move Document using 'm' keyboard shortcut",
			groups = { "functional", "L3" })

	public void MoveDocument_02() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseRootFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		String briefcaseRootFolderId = briefcaseRootFolder.getId();

		Shortcut shortcut = Shortcut.S_MOVE;

		String[] subFolderNames = { "subFolderName1" + ConfigProperties.getUniqueString(),
				"subFolderName2" + ConfigProperties.getUniqueString() };

		FolderItem[] subFolders = new FolderItem[subFolderNames.length];

		// Create folders to move the message from/to: Briefcase/sub-folder
		for (int i = 0; i < subFolderNames.length; i++) {
			account.soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + subFolderNames[i]
					+ "' l='" + briefcaseRootFolderId + "'/>" + "</CreateFolderRequest>");

			subFolders[i] = FolderItem.importFromSOAP(account, subFolderNames[i]);
		}

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseRootFolder, true);

		// Create document item
		DocumentItem docItem = new DocumentItem();

		// Create document in sub-folder1 using SOAP
		String contentHTML = XmlStringUtil
				.escapeXml("<html>" + "<body>" + docItem.getDocText() + "</body>" + "</html>");

		account.soapSend("<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>" + "<doc name='"
				+ docItem.getName() + "' l='" + subFolders[0].getId() + "' ct='application/x-zimbra-doc'>" + "<content>"
				+ contentHTML + "</content>" + "</doc>" + "</SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseRootFolder, true);

		// Double click on sub-folder1 in list view
		app.zPageBriefcase.zListItem(Action.A_DOUBLECLICK, subFolders[0]);

		// Click on created document in list view
		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, docItem);

		// Click the Move keyboard shortcut
		DialogMove chooseFolder = (DialogMove) app.zPageBriefcase.zKeyboardShortcut(shortcut);

		// Choose destination folder and Click OK on Confirmation dialog
		chooseFolder.sClickTreeFolder(subFolders[1]);

		// Click OK on Confirmation dialog
		app.zPageBriefcase
				.sClick("//div[@id='ChooseFolderDialog_buttons']//td[contains(@id,'OK_')]//td[contains(@id,'_title')]");

		// app.zPageBriefcase.sClickAt("css=div[id=ChooseFolderDialog_button2]","0,0");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseRootFolder, true);

		// Click on sub-folder1 in tree view
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, subFolders[0], false);

		// Verify document is no longer in the sub-folder1
		ZAssert.assertFalse(app.zPageBriefcase.isPresentInListView(docItem.getName()),
				"Verify document is no longer in the folder: " + subFolders[0].getName());

		// Click on sub-folder2 in tree view
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, subFolders[1], true);

		// Verify document was moved to sub-folder2
		ZAssert.assertTrue(app.zPageBriefcase.isPresentInListView(docItem.getName()),
				"Verify document was moved to the folder: " + subFolders[1].getName());
	}


	@Test (description = "Create document through SOAP - move using Right Click Context Menu & verify through GUI",
			groups = { "functional", "L3" })

	public void MoveDocument_03() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		String briefcaseFolderId = briefcaseFolder.getId();

		String name = "subFolder" + ConfigProperties.getUniqueString();

		// Create a subfolder to move the message into i.e. Briefcase/subfolder
		account.soapSend("<CreateFolderRequest xmlns='urn:zimbraMail'>" + "<folder name='" + name + "' l='"
				+ briefcaseFolderId + "'/>" + "</CreateFolderRequest>");

		FolderItem subFolderItem = FolderItem.importFromSOAP(account, name);

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Double Click on created subfolder
		app.zPageBriefcase.zListItem(Action.A_DOUBLECLICK, subFolderItem);

		// Create document item
		DocumentItem docItem = new DocumentItem();

		// Create document using SOAP
		String contentHTML = XmlStringUtil
				.escapeXml("<html>" + "<body>" + docItem.getDocText() + "</body>" + "</html>");

		account.soapSend("<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>" + "<doc name='"
				+ docItem.getName() + "' l='" + briefcaseFolderId + "' ct='application/x-zimbra-doc'>" + "<content>"
				+ contentHTML + "</content>" + "</doc>" + "</SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Click on created document
		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, docItem);

		// Move using Right Click Context Menu
		DialogMove chooseFolder = (DialogMove) app.zPageBriefcase.zListItem(Action.A_RIGHTCLICK, Button.O_MOVE,
				docItem);

		// Choose folder on Confirmation dialog
		chooseFolder.sClickTreeFolder(subFolderItem);

		// Click OK on Confirmation dialog
		app.zPageBriefcase
				.sClick("//div[@id='ChooseFolderDialog_buttons']//td[contains(@id,'OK_')]//td[contains(@id,'_title')]");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		// Verify document was moved from the folder
		ZAssert.assertFalse(app.zPageBriefcase.isPresentInListView(docItem.getName()),
				"Verify document was moved from the folder");

		// Click on subfolder in tree view
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, subFolderItem, true);

		// Verify document was moved to the selected folder
		ZAssert.assertTrue(app.zPageBriefcase.isPresentInListView(docItem.getName()),
				"Verify document was moved to the selected folder");
	}


	@AfterMethod(groups = { "always" })
	public void afterMethod() throws HarnessException {
		logger.info("Checking for the Move Dialog ...");

		// Check if the "Move Dialog is still open
		DialogMove dialog = new DialogMove(app, ((AjaxPages) app).zPageBriefcase);
		if (dialog.zIsActive()) {
			logger.warn(dialog.myPageName() + " was still active.  Cancelling ...");
			dialog.zPressButton(Button.B_CANCEL);
		}
	}
}