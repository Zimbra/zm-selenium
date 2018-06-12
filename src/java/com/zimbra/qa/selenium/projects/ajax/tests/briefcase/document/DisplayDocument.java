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
import com.zimbra.qa.selenium.projects.ajax.core.EnableBriefcaseFeature;
import com.zimbra.qa.selenium.projects.ajax.pages.briefcase.DocumentBriefcaseOpen;

public class DisplayDocument extends EnableBriefcaseFeature {

	public DisplayDocument() {
		logger.info("New " + DisplayDocument.class.getCanonicalName());
		super.startingPage = app.zPageBriefcase;
		super.startingAccountPreferences.put("zimbraPrefBriefcaseReadingPaneLocation", "bottom");
		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox","TRUE");
	}


	@Test (description = "Create document through SOAP - verify through GUI",
			groups = { "smoke", "L1" })

	public void DisplayDocument_01() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account,	SystemFolder.Briefcase);

		// Create document item
		DocumentItem document = new DocumentItem();

		String docName = document.getName();
		String docText = document.getDocText();

		// Create document using SOAP
		String contentHTML = XmlStringUtil.escapeXml("<html>" + "<body>"
				+ docText + "</body>" + "</html>");

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

		boolean present = app.zPageBriefcase.waitForPresentInListView(docName);
		ZAssert.assertTrue(present, "Verify document name through GUI");
	}


	@Bugs (ids = "79994")
	@Test (description = "german umlauts breaks briefcase ",
			groups = { "functional", "L3" })

	public void DisplayDocument_02() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account,	SystemFolder.Briefcase);

		// Create document item
		DocumentItem document = new DocumentItem();
		String docName = "testäöütest";
		document.setDocName(docName);
		String docText = document.getDocText();

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
		boolean present = app.zPageBriefcase.waitForPresentInListView(docName);

		ZAssert.assertTrue(present, "Verify document name through GUI");
		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, document);

		// Click on open in a separate window icon in toolbar
		DocumentBriefcaseOpen documentBriefcaseOpen;
		documentBriefcaseOpen = (DocumentBriefcaseOpen) app.zPageBriefcase.zToolbarPressPulldown(Button.B_ACTIONS,
					Button.B_LAUNCH_IN_SEPARATE_WINDOW, document);
		app.zPageBriefcase.isOpenDocLoaded(document);

		String text = "";

		// Select document opened in a separate window
		try {
			app.zPageBriefcase.zSelectWindow(docName);

			text = documentBriefcaseOpen.retriveDocumentText();

			// Close
			app.zPageBriefcase.zSelectWindow(docName);

			app.zPageBriefcase.closeWindow();
		} finally {
			app.zPageBriefcase.zSelectWindow(": Briefcase");
		}

		ZAssert.assertStringContains(text, docText,	"Verify document text through GUI");

		// Delete file upon test completion
		app.zPageBriefcase.deleteFileByName(docName);
	}
}