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
package com.zimbra.qa.selenium.projects.universal.tests.briefcase.document;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.DocumentItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Shortcut;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.XmlStringUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.universal.core.FeatureBriefcaseTest;
import com.zimbra.qa.selenium.projects.universal.ui.briefcase.DialogConfirm;

public class DeleteDocument extends FeatureBriefcaseTest {

	public DeleteDocument() {
		logger.info("New " + DeleteDocument.class.getCanonicalName());

		super.startingPage = app.zPageBriefcase;
		
		super.startingAccountPreferences.put("zimbraPrefBriefcaseReadingPaneLocation", "bottom");
		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox","TRUE");
	}

	@Test( description = "Create document through SOAP - delete & check trash", 
			groups = { "smoke", "L0" })
	public void DeleteDocument_01() throws HarnessException {
		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account,
				SystemFolder.Briefcase);

		FolderItem trashFolder = FolderItem.importFromSOAP(account,
				SystemFolder.Trash);

		// Create document item
		DocumentItem docItem = new DocumentItem();

		String docName = docItem.getName();
		String docText = docItem.getDocText();

		// Create document using SOAP
		String contentHTML = XmlStringUtil.escapeXml("<html>" + "<body>"
				+ docText + "</body>" + "</html>");

		account
				.soapSend("<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>"
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

		String docId = account.soapSelectValue(
				"//mail:SaveDocumentResponse//mail:doc", "id");

		// refresh briefcase page
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		SleepUtil.sleepVerySmall();

		// Click on created document
		
		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, docItem);

		// Click on Delete document icon in toolbar
		DialogConfirm deleteConfirm = (DialogConfirm) app.zPageBriefcase
				.zToolbarPressButton(Button.B_DELETE, docItem);

		// Click OK on Confirmation dialog
		deleteConfirm.zClickButton(Button.B_YES);

		// refresh briefcase page
		app.zTreeBriefcase
				.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		// Verify document was deleted from the list
		boolean isDeleted = app.zPageBriefcase
				.waitForDeletedFromListView(docName);

		ZAssert
				.assertTrue(isDeleted,
						"Verify document was deleted through GUI");

		// Verify document moved to Trash
		account
				.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='document'>"
						+ "<query>in:"
						+ trashFolder.getName()
						+ " "
						+ docName
						+ "</query>" + "</SearchRequest>");

		String id = account.soapSelectValue("//mail:SearchResponse//mail:doc",
				"id");

		ZAssert.assertNotNull(id,
				"Verify the search response returns the document id");

		ZAssert.assertEquals(id, docId,
				"Verify the document was moved to the trash folder");
	}

	@Test( description = "Create document through SOAP - delete using Delete Key & verify through GUI", 
			groups = { "functional", "L2" })
	public void DeleteDocument_02() throws HarnessException {
		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account,
				SystemFolder.Briefcase);

		// Create document item
		DocumentItem docItem = new DocumentItem();

		String docName = docItem.getName();
		String docText = docItem.getDocText();

		Shortcut shortcut = Shortcut.S_DELETE;

		// Create document using SOAP
		String contentHTML = XmlStringUtil.escapeXml("<html>" + "<body>"
				+ docText + "</body>" + "</html>");

		account
				.soapSend("<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>"
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

		// refresh briefcase page
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		SleepUtil.sleepVerySmall();

		// Click on created document
		
		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, docItem);

		// Click the Delete keyboard shortcut
		// app.zPageBriefcase.zSelectWindow("Zimbra: Briefcase");
		DialogConfirm deleteConfirm = (DialogConfirm) app.zPageBriefcase
				.zKeyboardShortcut(shortcut);

		// Click OK on Confirmation dialog
		deleteConfirm.zClickButton(Button.B_YES);

		// refresh briefcase page
		app.zTreeBriefcase
				.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		// Verify document was deleted
		boolean isDeleted = app.zPageBriefcase
				.waitForDeletedFromListView(docName);

		ZAssert
				.assertTrue(isDeleted,
						"Verify document was deleted through GUI");
	}

	@Test( description = "Create document through SOAP - delete using Backspace Key & verify through GUI",
			groups = { "functional","L3" })
	public void DeleteDocument_03() throws HarnessException {
		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account,
				SystemFolder.Briefcase);

		// Create document item
		DocumentItem docItem = new DocumentItem();

		String docName = docItem.getName();
		String docText = docItem.getDocText();

		Shortcut shortcut = Shortcut.S_BACKSPACE;

		// Create document using SOAP
		String contentHTML = XmlStringUtil.escapeXml("<html>" + "<body>"
				+ docText + "</body>" + "</html>");

		account
				.soapSend("<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>"
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

		// refresh briefcase page
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		SleepUtil.sleepVerySmall();

		// Click on created document
		
		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, docItem);

		// Delete Document using Backspace keyboard shortcut
		// app.zPageBriefcase.zSelectWindow("Zimbra: Briefcase");
		DialogConfirm deleteConfirm = (DialogConfirm) app.zPageBriefcase
				.zKeyboardShortcut(shortcut);

		// Click OK on Confirmation dialog
		deleteConfirm.zClickButton(Button.B_YES);

		// refresh briefcase page
		app.zTreeBriefcase
				.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		// Verify document was deleted
		boolean isDeleted = app.zPageBriefcase
				.waitForDeletedFromListView(docName);

		ZAssert
				.assertTrue(isDeleted,
						"Verify document was deleted through GUI");
	}

	@Test( description = "Create document through SOAP - delete using Right Click context menu & verify through GUI", 
			groups = { "smoke", "L1" })
	public void DeleteDocument_04() throws HarnessException {
		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account,
				SystemFolder.Briefcase);

		// Create document item
		DocumentItem docItem = new DocumentItem();

		String docName = docItem.getName();
		String docText = docItem.getDocText();

		// Create document using SOAP
		String contentHTML = XmlStringUtil.escapeXml("<html>" + "<body>"
				+ docText + "</body>" + "</html>");

		account
				.soapSend("<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>"
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

		// refresh briefcase page
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Click on created document
		
		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, docItem);

		// Delete Document using Right Click Context Menu
		DialogConfirm deleteConfirm = (DialogConfirm) app.zPageBriefcase
				.zListItem(Action.A_RIGHTCLICK, Button.O_DELETE, docItem);

		// Click OK on Confirmation dialog
		deleteConfirm.zClickButton(Button.B_YES);

		// refresh briefcase page
		app.zTreeBriefcase
				.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		// Verify document was deleted
		boolean isDeleted = app.zPageBriefcase
				.waitForDeletedFromListView(docName);

		ZAssert
				.assertTrue(isDeleted,
						"Verify document was deleted through GUI");
	}

	@Test( description = "Delete multiple documents(3) by selecting check box and delete using toolbar", 
			groups = { "functional", "L2" })
	public void DeleteDocument_05() throws HarnessException {
		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account,
				SystemFolder.Briefcase);

		FolderItem trashFolder = FolderItem.importFromSOAP(account,
				SystemFolder.Trash);

		// Create documents using SOAP
		DocumentItem[] docItems = {
				new DocumentItem("docName1"
						+ ConfigProperties.getUniqueString()),
				new DocumentItem("docName2"
						+ ConfigProperties.getUniqueString()),
				new DocumentItem("docName3"
						+ ConfigProperties.getUniqueString()) };

		String contentHTML = XmlStringUtil.escapeXml("<html>" + "<body>"
				+ ConfigProperties.getUniqueString() + "</body>"
				+ "</html>");

		for (int i = 0; i < docItems.length; i++) {
			account
					.soapSend("<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>"
							+ "<doc name='"
							+ docItems[i].getName()
							+ "' l='"
							+ briefcaseFolder.getId()
							+ "' ct='application/x-zimbra-doc'>"
							+ "<content>"
							+ contentHTML
							+ "</content>"
							+ "</doc>"
							+ "</SaveDocumentRequest>");

			docItems[i].setDocId(account.soapSelectValue(
					"//mail:SaveDocumentResponse//mail:doc", "id"));

		}

		// refresh briefcase page
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Select all three items
		for (DocumentItem item : docItems) {
			app.zPageBriefcase.zListItem(Action.A_BRIEFCASE_CHECKBOX, item);
			SleepUtil.sleepVerySmall();
		}

		// Click toolbar delete button
		DialogConfirm deleteConfirm = (DialogConfirm) app.zPageBriefcase
				.zToolbarPressButton(Button.B_DELETE, docItems[0]);

		// Click OK on Confirmation dialog
		deleteConfirm.zClickButton(Button.B_YES);

		// refresh briefcase page
		app.zTreeBriefcase
				.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		SleepUtil.sleepVerySmall();

		// Verify items are deleted);
		for (DocumentItem item : docItems) {
			String name = item.getName();
			String docId = item.getId();

			// Verify document was deleted from the list
			ZAssert.assertFalse(app.zPageBriefcase.isPresentInListView(name),
					"Verify the document " + name
							+ " is no longer in the list view");

			// Verify document moved to Trash
			account
					.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='document'>"
							+ "<query>in:"
							+ trashFolder.getName()
							+ " "
							+ name
							+ "</query>" + "</SearchRequest>");

			String id = account.soapSelectValue(
					"//mail:SearchResponse//mail:doc", "id");

			ZAssert.assertNotNull(id,
					"Verify the search response returns the document id");

			ZAssert
					.assertEquals(docId, id, "Verify the deleted document: "
							+ name + " id: " + docId
							+ " was moved to the trash folder");
		}
	}

	@Bugs(ids = "43836")
	@Test( description = "can not delete documents in briefcase with the same file name",
	groups = { "functional", "L3" })
	public void DeleteDocument_06() throws HarnessException {
		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account,	SystemFolder.Briefcase);
		FolderItem trashFolder = FolderItem.importFromSOAP(account,	SystemFolder.Trash);

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

		String docId = account.soapSelectValue("//mail:SaveDocumentResponse//mail:doc", "id");

		// refresh briefcase page
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		SleepUtil.sleepVerySmall();

		// Click on created document
		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, docItem);

		// Click on Delete document icon in toolbar
		DialogConfirm deleteConfirm = (DialogConfirm) app.zPageBriefcase.zToolbarPressButton(Button.B_DELETE, docItem);

		// Click OK on Confirmation dialog
		deleteConfirm.zClickButton(Button.B_YES);

		// refresh briefcase page
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		// Verify document was deleted from the list
		boolean isDeleted = app.zPageBriefcase.waitForDeletedFromListView(docName);

		ZAssert.assertTrue(isDeleted, "Verify document was deleted through GUI");

		// Verify document moved to Trash
		account.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='document'>"
						+ "<query>in:"
						+ trashFolder.getName()
						+ " "
						+ docName
						+ "</query>" + "</SearchRequest>");

		String id = account.soapSelectValue("//mail:SearchResponse//mail:doc", "id");
		ZAssert.assertNotNull(id, "Verify the search response returns the document id");
		ZAssert.assertEquals(id, docId,	"Verify the document was moved to the trash folder");
		
		// Create document using SOAP
		String contentHTML1 = XmlStringUtil.escapeXml("<html>" + "<body>" + docText + "</body>" + "</html>");

		account.soapSend("<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>"
						+ "<doc name='"
						+ docName
						+ "' l='"
						+ briefcaseFolder.getId()
						+ "' ct='application/x-zimbra-doc'>"
						+ "<content>"
						+ contentHTML1
						+ "</content>"
						+ "</doc>"
						+ "</SaveDocumentRequest>");

		String docId1 = account.soapSelectValue("//mail:SaveDocumentResponse//mail:doc", "id");

		// refresh briefcase page
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		SleepUtil.sleepVerySmall();

		// Click on created document
		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, docItem);

		// Click on Delete document icon in toolbar
		deleteConfirm = (DialogConfirm) app.zPageBriefcase.zToolbarPressButton(Button.B_DELETE, docItem);

		// Click OK on Confirmation dialog
		deleteConfirm.zClickButton(Button.B_YES);

		// refresh briefcase page
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		// Verify document was deleted from the list
		isDeleted = app.zPageBriefcase.waitForDeletedFromListView(docName);

		ZAssert.assertTrue(isDeleted, "Verify document was deleted through GUI");

		// Verify document moved to Trash
		account.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='document'>"
						+ "<query>in:"
						+ trashFolder.getName()
						+ " "
						+ docName
						+ "</query>" + "</SearchRequest>");

		id = account.soapSelectValue("//mail:SearchResponse//mail:doc", "id");
		ZAssert.assertNotNull(id, "Verify the search response returns the document id");
		ZAssert.assertEquals(id, docId1,	"Verify the document was moved to the trash folder");
	}

	@Bugs(ids = "103343")
	@Test( description = "Create document with 3 versions through SOAP - delete using Right Click context menu & verify through GUI",
	groups = { "functional", "L3" })
	public void DeleteDocument_07() throws HarnessException {
		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account,
				SystemFolder.Briefcase);

		// Create document item
		DocumentItem docItem = new DocumentItem();

		String docName = docItem.getName();
		String docTextV1 = "textVersion1" + docItem.getDocText();
		String docTextV2 = "textVersion2" + docItem.getDocText();
		String docTextV3 = "textVersion3" + docItem.getDocText();
		String notesV1 = "notesVersion1" + ConfigProperties.getUniqueString();
		String notesV2 = "notesVersion2" + ConfigProperties.getUniqueString();
		String notesV3 = "notesVersion3" + ConfigProperties.getUniqueString();
		String nodeCollapsed = "css=div[id^=zlif__BDLV-main__] div[class='ImgNodeCollapsed']";
		String nodeExpanded = "css=div[id^=zlif__BDLV-main__] div[class='ImgNodeExpanded']";
		String locator = "css=tr[id^='zlif__BDLV-main__'] div[id^='zlif__BDLV-main__']:contains('#2:')";
		String dialogText = "css=div[id='CONFIRM_DIALOG_content'] div[class='DwtConfirmDialogQuestion']:contains('Are you sure you want to permanently delete \"" + docName + "\"?')";
		// Create document using SOAP
		String contentHTML1 = XmlStringUtil.escapeXml("<html>" + "<body>"
				+ docTextV1 + "</body>" + "</html>");
		
		String contentHTML2 = XmlStringUtil.escapeXml("<html>" + "<body>"
				+ docTextV2 + "</body>" + "</html>");
		
		String contentHTML3 = XmlStringUtil.escapeXml("<html>" + "<body>"
				+ docTextV3 + "</body>" + "</html>");
		
		account.soapSend(
				"<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>" +
				"<doc ct='application/x-zimbra-doc' name='"+ docName +"' descEnabled='true' l='"+ briefcaseFolder.getId() +"' desc='" + notesV1 +"'>" +
				"<content>" + contentHTML1 + "</content>" +
				"</doc>" +
				"</SaveDocumentRequest>");
		String documentId = account.soapSelectValue("//mail:doc", "id");
		
		account.soapSend(
				"<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>" +
				"<doc ct='application/x-zimbra-doc' name='"+ docName +"' descEnabled='true' ver='1' id='" + documentId + "' l='"+ briefcaseFolder.getId() +"' desc='" + notesV2 +"'>" +
				"<content>" + contentHTML2 + "</content>" +
				"</doc>" +
				"</SaveDocumentRequest>");

		account.soapSend(
				"<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>" +
				"<doc ct='application/x-zimbra-doc' name='"+ docName +"' descEnabled='true' ver='2' id='" + documentId + "' l='"+ briefcaseFolder.getId() +"' desc='" + notesV3 +"'>" +
				"<content>" + contentHTML3 + "</content>" +
				"</doc>" +
				"</SaveDocumentRequest>");
		
		// refresh briefcase page
		// refresh briefcase page
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);
		
		if (!app.zPageBriefcase.sIsElementPresent(nodeExpanded)) {
			app.zPageBriefcase.zClickAt(nodeCollapsed, "");
		}
		
		// Delete Document using Right Click Context Menu
		DialogConfirm deleteConfirm = (DialogConfirm) app.zPageBriefcase.zListItem(Action.A_RIGHTCLICK, Button.O_DELETE, locator);
		ZAssert.assertTrue(app.zPageBriefcase.sIsElementPresent(dialogText), "Verify that text is correct");
		// Click OK on Confirmation dialog
		deleteConfirm.zClickButton(Button.B_YES);

		// refresh briefcase page
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		// Verify document was deleted
		boolean isDeleted = app.zPageBriefcase.waitForDeletedFromListView(docName);

		ZAssert.assertTrue(isDeleted, "Verify document was deleted through GUI");
	}
	
}