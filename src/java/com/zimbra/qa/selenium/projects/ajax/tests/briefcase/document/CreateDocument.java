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

import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.util.HtmlElement;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.DocumentItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Shortcut;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.ajax.core.FeatureBriefcaseTest;
import com.zimbra.qa.selenium.projects.ajax.ui.briefcase.DocumentBriefcaseNew;
import com.zimbra.qa.selenium.projects.ajax.ui.briefcase.DocumentBriefcaseOpen;
import com.zimbra.qa.selenium.projects.ajax.ui.briefcase.PageBriefcase;

public class CreateDocument extends FeatureBriefcaseTest {

	public CreateDocument() {
		logger.info("New " + CreateDocument.class.getCanonicalName());

		super.startingPage = app.zPageBriefcase;
		super.startingAccountPreferences.put("zimbraPrefBriefcaseReadingPaneLocation", "bottom");
		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox", "TRUE");
	}

	@Bugs(ids = "97124")
	@Test( description = "Create document through GUI - verify through GUI", 
		groups = { "sanity", "L0" })
	
	public void CreateDocument_01() throws HarnessException {
		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create document item
		DocumentItem docItem = new DocumentItem();

		String docName = docItem.getName();
		String docText = docItem.getDocText();

		// Open new document page
		DocumentBriefcaseNew documentBriefcaseNew = (DocumentBriefcaseNew) app.zPageBriefcase.zToolbarPressButton(Button.B_NEW, docItem);

		try {
			app.zPageBriefcase.zSelectWindow(DocumentBriefcaseNew.pageTitle);
			app.zPageBriefcase.sWindowFocus();

			// Fill out the document with the data
			documentBriefcaseNew.zFillField(DocumentBriefcaseNew.Field.Name, docName);
			documentBriefcaseNew.zFillField(DocumentBriefcaseNew.Field.Body, docText);

			documentBriefcaseNew.zSubmit();
		} finally {
			app.zPageBriefcase.sSelectWindow(null);
			app.zPageBriefcase.sWindowFocus();
		}

		// Refresh briefcase page
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Click on created document
		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, docItem);

		// Click on open in a separate window icon in toolbar
		DocumentBriefcaseOpen documentBriefcaseOpen;
		documentBriefcaseOpen = (DocumentBriefcaseOpen) app.zPageBriefcase.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW, docItem);

		app.zPageBriefcase.isOpenDocLoaded(docItem);

		String name = "";
		String text = "";

		// Select document opened in a separate window
		try {
			app.zPageBriefcase.zSelectWindow(docName);
			app.zPageBriefcase.sWindowFocus();

			name = documentBriefcaseOpen.retriveDocumentName();
			text = documentBriefcaseOpen.retriveDocumentText();

			// close
			app.zPageBriefcase.zSelectWindow(docName);
			app.zPageBriefcase.closeWindow();

		} finally {
			app.zPageBriefcase.zSelectWindow(PageBriefcase.pageTitle);
			app.zPageBriefcase.sWindowFocus();
		}

		ZAssert.assertStringContains(name, docName,	"Verify document name through GUI");
		ZAssert.assertStringContains(text, docText,	"Verify document text through GUI");
	}
	
	@Test( description = "Create document using keyboard shortcut - verify through SOAP & RestUtil", 
			groups = { "smoke", "L1" })
	
	public void CreateDocument_02() throws HarnessException {
		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create document item
		DocumentItem document = new DocumentItem();

		String docName = document.getName();
		String docText = document.getDocText();

		Shortcut shortcut = Shortcut.S_NEWDOCUMENT;

		// Open new document page using keyboard shortcut
		app.zPageBriefcase.zSelectWindow(PageBriefcase.pageTitle);
		app.zPageBriefcase.sWindowFocus();
		DocumentBriefcaseNew documentBriefcaseNew = (DocumentBriefcaseNew) app.zPageBriefcase.zKeyboardShortcut(shortcut);

		try {
			app.zPageBriefcase.zSelectWindow(DocumentBriefcaseNew.pageTitle);
			app.zPageBriefcase.sWindowFocus();

			// Fill out the document with the data
			documentBriefcaseNew.zFillField(DocumentBriefcaseNew.Field.Name, docName);
			documentBriefcaseNew.zFillField(DocumentBriefcaseNew.Field.Body, docText);
			documentBriefcaseNew.zSubmit();

		} finally {
			app.zPageBriefcase.zSelectWindow(PageBriefcase.pageTitle);
			app.zPageBriefcase.sWindowFocus();
		}

		app.zPageBriefcase.zWaitForWindowClosed(DocumentBriefcaseNew.pageTitle);

		// Refresh briefcase page
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Display file through RestUtil
		EnumMap<PageBriefcase.Response.ResponsePart, String> response = app.zPageBriefcase
				.displayFile(docName, new HashMap<String, String>() {
					private static final long serialVersionUID = 1L;
					{
						put("fmt", PageBriefcase.Response.Format.NATIVE
								.getFormat());
					}
				});

		// Search for created document
		account.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='document'>" + "<query>" + docName + "</query>" + "</SearchRequest>");

		String name = account.soapSelectValue("//mail:SearchResponse//mail:doc", "name");

		ZAssert.assertNotNull(name, "Verify the search response returns the document name");
		ZAssert.assertStringContains(name, docName, "Verify document name through SOAP");

		HtmlElement element = HtmlElement.clean(response.get(PageBriefcase.Response.ResponsePart.BODY));
		HtmlElement.evaluate(element, "//body", null, Pattern.compile(".*" + docText + ".*"), 1);

		ZAssert.assertStringContains(response.get(PageBriefcase.Response.ResponsePart.BODY), docText, "Verify document content through GUI");

		// delete file upon test completion
		app.zPageBriefcase.deleteFileByName(docName);
	}
	
	@Bugs(ids="81299")
	@Test( description = "Create document using New menu pulldown menu - verify through SOAP & RestUtil", 
			groups = { "smoke", "L1" })
	
	public void CreateDocument_03() throws HarnessException {
		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account,
				SystemFolder.Briefcase);

		// Create document item
		DocumentItem document = new DocumentItem();
		Date date = new Date();
		String docName = document.getName();
		String docText = document.getDocText();

		// Refresh briefcase page before creating a new document
		app.zTreeBriefcase
				.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		SleepUtil.sleepVerySmall();

		// Create a new document using New pull down menu
		DocumentBriefcaseNew documentBriefcaseNew = (DocumentBriefcaseNew) app.zPageBriefcase.zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_DOCUMENT, document);

		try {
			app.zPageBriefcase.zSelectWindow(DocumentBriefcaseNew.pageTitle);
			app.zPageBriefcase.sWindowFocus();

			// Fill out the document with the data
			documentBriefcaseNew.zFillField(DocumentBriefcaseNew.Field.Name, docName);
			documentBriefcaseNew.zFillField(DocumentBriefcaseNew.Field.Body, docText);
			documentBriefcaseNew.zSubmit();

		} finally {
			app.zPageBriefcase.zSelectWindow(PageBriefcase.pageTitle);
			app.zPageBriefcase.sWindowFocus();
		}

		app.zPageBriefcase.zWaitForWindowClosed(DocumentBriefcaseNew.pageTitle);

		// Refresh briefcase page
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Search for created document
		account.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='document'>" + "<query>" + docName + "</query>" + "</SearchRequest>");

		String name = account.soapSelectValue("//mail:doc", "name");
		String modifiedDate = account.soapSelectValue("//mail:doc", "cd");
		Date modDate = new Date(Long.parseLong(modifiedDate));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

		ZAssert.assertNotNull(name, "Verify the search response returns the document name");
		ZAssert.assertStringContains(docName, name, "Verify document name through SOAP");
		ZAssert.assertEquals(dateFormat.format(modDate), dateFormat.format(date), "modified date is displayed correctly");

		// delete file upon test completion
		app.zPageBriefcase.deleteFileByName(docName);
	}

	@AfterMethod(groups = { "always" })
	public void afterMethod() throws HarnessException {
		logger.info("Checking for the opened window ...");

		// Check if the window is still open
		List<String> windows = app.zPageBriefcase.sGetAllWindowNames();
		for (String window : windows) {
			if (!window.isEmpty() && !window.contains("null")
				&& !window.contains(PageBriefcase.pageTitle)
				&& !window.contains("main_app_window")
				&& !window.contains("undefined")) {
				logger.warn(window + " window was still active. Closing ...");
				app.zPageBriefcase.zSelectWindow(window);
				app.zPageBriefcase.sWindowFocus();
				app.zPageBriefcase.closeWindow();
			}
		}
		app.zPageBriefcase.zSelectWindow(PageBriefcase.pageTitle);
		app.zPageBriefcase.sWindowFocus();
	}
}
