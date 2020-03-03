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
package com.zimbra.qa.selenium.projects.ajax.tests.briefcase.document;

import java.util.List;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.DocumentItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.XmlStringUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.EnableBriefcaseFeature;
import com.zimbra.qa.selenium.projects.ajax.pages.briefcase.DocumentBriefcaseEdit;
import com.zimbra.qa.selenium.projects.ajax.pages.briefcase.DocumentBriefcaseNew;
import com.zimbra.qa.selenium.projects.ajax.pages.briefcase.DocumentBriefcaseOpen;
import com.zimbra.qa.selenium.projects.ajax.pages.briefcase.PageBriefcase;

public class EditDocument extends EnableBriefcaseFeature {

	public EditDocument() {
		logger.info("New " + EditDocument.class.getCanonicalName());
		super.startingPage = app.zPageBriefcase;
		super.startingAccountPreferences.put("zimbraPrefBriefcaseReadingPaneLocation", "bottom");
	}


	@Test (description = "Create document through SOAP - edit name & verify through GUI",
			groups = { "sanity" })

	public void EditDocument_01() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create document item
		DocumentItem docItem1 = new DocumentItem();
		DocumentItem docItem2 = new DocumentItem();

		// Create document using SOAP
		String contentHTML = XmlStringUtil
				.escapeXml("<html>" + "<body>" + docItem1.getDocText() + "</body>" + "</html>");

		account.soapSend("<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>" + "<doc name='"
				+ docItem1.getName() + "' l='" + briefcaseFolder.getId() + "' ct='application/x-zimbra-doc'>"
				+ "<content>" + contentHTML + "</content>" + "</doc>" + "</SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Click on created document
		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, docItem1);

		// Click on Edit document icon in toolbar
		DocumentBriefcaseEdit documentBriefcaseEdit = (DocumentBriefcaseEdit) app.zPageBriefcase
				.zToolbarPressButton(Button.B_EDIT_FILE, docItem1);

		app.zPageBriefcase.isEditDocLoaded(docItem1);

		// Select edit document window
		try {
			app.zPageBriefcase.zSelectWindow(docItem1.getName());

			// Fill out the document with the new data
			// documentBriefcaseEdit.typeDocumentName(docItem2.getName());
			documentBriefcaseEdit.zFillField(DocumentBriefcaseNew.Field.Name, docItem2.getName());

			// Save and close
			documentBriefcaseEdit.zSubmit();
		} catch (Exception ex) {
			throw new HarnessException("error in editing document " + docItem1.getName(), ex);
		} finally {
			app.zPageBriefcase.zSelectWindow(PageBriefcase.pageTitle);
		}

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// "Verify document name through GUI");
		ZAssert.assertTrue(app.zPageBriefcase.waitForPresentInListView(docItem2.getName()),
				"Verify document name through GUI");

		// Delete file upon test completion
		app.zPageBriefcase.deleteFileByName(docItem2.getName());
	}


	@Bugs (ids = "97124")
	@Test (description = "Create document through SOAP - edit text & name & verify through GUI",
			groups = { "functional" })

	public void EditDocument_02() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create document item
		DocumentItem docItem1 = new DocumentItem();
		DocumentItem docItem2 = new DocumentItem();

		// Create document using SOAP
		String contentHTML = XmlStringUtil
				.escapeXml("<html>" + "<body>" + docItem1.getDocText() + "</body>" + "</html>");

		account.soapSend("<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>" + "<doc name='"
				+ docItem1.getName() + "' l='" + briefcaseFolder.getId() + "' ct='application/x-zimbra-doc'>"
				+ "<content>" + contentHTML + "</content>" + "</doc>" + "</SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Click on created document
		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, docItem1);

		// Click on Edit document icon in toolbar
		DocumentBriefcaseEdit documentBriefcaseEdit = (DocumentBriefcaseEdit) app.zPageBriefcase
				.zToolbarPressButton(Button.B_EDIT_FILE, docItem1);

		app.zPageBriefcase.isEditDocLoaded(docItem1);

		// Select document window opened for editing
		try {
			app.zPageBriefcase.zSelectWindow(docItem1.getName());

			// Fill out the document with the data
			documentBriefcaseEdit.zFillField(DocumentBriefcaseNew.Field.Name, docItem2.getName());
			documentBriefcaseEdit.zFillField(DocumentBriefcaseNew.Field.Body, docItem2.getDocText());

			// Save and close
			documentBriefcaseEdit.zSubmit();
		} catch (Exception ex) {
			throw new HarnessException("error in editing document " + docItem1.getName(), ex);
		} finally {
			app.zPageBriefcase.sSelectWindow(PageBriefcase.pageTitle);
		}

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Click on modified document
		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, docItem2);

		// Click on open in a separate window icon in toolbar
		DocumentBriefcaseOpen documentBriefcaseOpen;
		documentBriefcaseOpen = (DocumentBriefcaseOpen) app.zPageBriefcase.zToolbarPressPulldown(Button.B_ACTIONS,
					Button.B_LAUNCH_IN_SEPARATE_WINDOW, docItem2);
		app.zPageBriefcase.isOpenDocLoaded(docItem2);

		String name = "";
		String text = "";

		// Select document opened in a separate window
		try {
			app.zPageBriefcase.zSelectWindow(docItem2.getName());

			name = documentBriefcaseOpen.retriveDocumentName();
			text = documentBriefcaseOpen.retriveDocumentText();

			// Close
			app.zPageBriefcase.closeWindow();
		} finally {
			app.zPageBriefcase.zSelectWindow(PageBriefcase.pageTitle);
		}

		ZAssert.assertStringContains(name, docItem2.getName(), "Verify document name through GUI");

		ZAssert.assertStringContains(text, docItem2.getDocText(), "Verify document text through GUI");

		// Delete file upon test completion
		app.zPageBriefcase.deleteFileByName(docItem2.getName());
	}


	@Bugs (ids = "97124")
	@Test (description = "Create document & edit text through SOAP & verify through GUI",
			groups = { "functional" })

	public void EditDocument_03() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create document item
		DocumentItem docItem = new DocumentItem();

		// Create document using SOAP
		String contentHTML = XmlStringUtil
				.escapeXml("<html>" + "<body>" + docItem.getDocText() + "</body>" + "</html>");

		account.soapSend("<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>" + "<doc name='"
				+ docItem.getName() + "' l='" + briefcaseFolder.getId() + "' ct='application/x-zimbra-doc'>"
				+ "<content>" + contentHTML + "</content>" + "</doc>" + "</SaveDocumentRequest>");

		// Search for created document
		account.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='document'>" + "<query>" + docItem.getName()
				+ "</query>" + "</SearchRequest>");

		String docId = account.soapSelectValue("//mail:doc", "id");
		String version = account.soapSelectValue("//mail:doc", "ver");

		ZAssert.assertNotNull(docId, "Verify the search response returns the document id");
		ZAssert.assertNotNull(docId, "Verify the search response returns the document version");
		docItem.setDocText("editText" + ConfigProperties.getUniqueString());

		// Edit document through SOAP
		account.soapSend("<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>" + "<doc name='"
				+ docItem.getName() + "' l='" + briefcaseFolder.getId() + "' ver='" + version + "' id='" + docId
				+ "' ct='application/x-zimbra-doc'>" + "<content>&lt;html>&lt;body>" + docItem.getDocText()
				+ "&lt;/body>&lt;/html></content>" + "</doc>" + "</SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Click on modified document

		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, docItem);

		// Click on open in a separate window icon in toolbar
		DocumentBriefcaseOpen documentBriefcaseOpen;
		documentBriefcaseOpen = (DocumentBriefcaseOpen) app.zPageBriefcase.zToolbarPressPulldown(Button.B_ACTIONS,
					Button.B_LAUNCH_IN_SEPARATE_WINDOW, docItem);
		app.zPageBriefcase.isOpenDocLoaded(docItem);

		String text = "";

		// Select document opened in a separate window
		try {
			app.zPageBriefcase.zSelectWindow(docItem.getName());

			text = documentBriefcaseOpen.retriveDocumentText();

			// Close
			app.zPageBriefcase.closeWindow();
		} finally {
			app.zPageBriefcase.zSelectWindow(PageBriefcase.pageTitle);
		}

		ZAssert.assertStringContains(text, docItem.getDocText(), "Verify document text through GUI");

		// Delete file upon test completion
		app.zPageBriefcase.deleteFileByName(docItem.getName());
	}


	@Test (description = "Create document through SOAP - Edit Document using Right Click Context Menu & verify through GUI",
			groups = { "sanity" })

	public void EditDocument_04() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create document item
		DocumentItem docItem = new DocumentItem();

		// Create document using SOAP
		String contentHTML = XmlStringUtil
				.escapeXml("<html>" + "<body>" + docItem.getDocText() + "</body>" + "</html>");

		account.soapSend("<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>" + "<doc name='"
				+ docItem.getName() + "' l='" + briefcaseFolder.getId() + "' ct='application/x-zimbra-doc'>"
				+ "<content>" + contentHTML + "</content>" + "</doc>" + "</SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Click on created document
		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, docItem);

		// Edit Document using Right Click Context Menu
		DocumentBriefcaseEdit documentBriefcaseEdit = (DocumentBriefcaseEdit) app.zPageBriefcase
				.zListItem(Action.A_RIGHTCLICK, Button.O_EDIT, docItem);

		// app.zPageBriefcase.isEditDocLoaded(docName, docText);

		String editDocName = "";

		// Select edit document window
		try {
			app.zPageBriefcase.zSelectWindow(docItem.getName());

			// Fill out the document with the new data
			editDocName = "editDocName" + ConfigProperties.getUniqueString();

			// documentBriefcaseEdit.typeDocumentName(editDocName);
			documentBriefcaseEdit.zFillField(DocumentBriefcaseNew.Field.Name, editDocName);

			// Save and close
			documentBriefcaseEdit.zSubmit();
		} catch (Exception ex) {
			throw new HarnessException("error in editing document " + docItem.getName(), ex);
		} finally {
			app.zPageBriefcase.zSelectWindow(PageBriefcase.pageTitle);
		}

		docItem.setDocName(editDocName);

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// "Verify document name through GUI");
		boolean present = app.zPageBriefcase.waitForPresentInListView(docItem.getName());

		ZAssert.assertTrue(present, "Verify document name through GUI");

		// Delete file upon test completion
		app.zPageBriefcase.deleteFileByName(docItem.getName());
	}


	@AfterMethod(groups = { "always" })
	public void afterMethod() throws HarnessException {
		logger.info("Checking for the opened window ...");

		// Check if the window is still open
		List<String> windows = app.zPageBriefcase.sGetAllWindowNames();
		for (String window : windows) {
			if (!window.isEmpty() && !window.contains("null") && !window.contains(PageBriefcase.pageTitle)
					&& !window.contains("main_app_window") && !window.contains("undefined")) {
				logger.warn(window + " window was still active. Closing ...");
				app.zPageBriefcase.zSelectWindow(window);
				app.zPageBriefcase.closeWindow();
			}
		}
		app.zPageBriefcase.zSelectWindow(PageBriefcase.pageTitle);
	}
}