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
package com.zimbra.qa.selenium.projects.ajax.tests.briefcase.file;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.regex.Pattern;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FileItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.HtmlElement;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.FeatureBriefcaseTest;
import com.zimbra.qa.selenium.projects.ajax.ui.briefcase.PageBriefcase;

public class EditFile extends FeatureBriefcaseTest {

	public EditFile() throws HarnessException {
		logger.info("New " + EditFile.class.getCanonicalName());
		super.startingPage = app.zPageBriefcase;
		super.startingAccountPreferences.put("zimbraPrefBriefcaseReadingPaneLocation", "bottom");
		super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox","TRUE");
	}


	@Test( description = "Upload file, edit name - verify the content remains the same",
			groups = { "smoke", "L1" })

	public void EditFile_01() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/other/testtextfile.txt";
		FileItem fileItem = new FileItem(filePath);

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>"
				+ "<doc l='" + briefcaseFolder.getId() + "'><upload id='"
				+ attachmentId + "'/>" + "</doc></SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Retrieve file text through RestUtil
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("fmt", PageBriefcase.Response.Format.NATIVE.getFormat());

		String text = app.zPageBriefcase.displayFile(fileItem.getName(), hm)
				.get(PageBriefcase.Response.ResponsePart.BODY);

		// Right click on File, select Rename
		app.zPageBriefcase.zListItem(Action.A_RIGHTCLICK, Button.B_RENAME, fileItem);

		String fileName2 = "renameFile" + ConfigProperties.getUniqueString();

		app.zPageBriefcase.rename(fileName2);
		app.zPageBriefcase.zClick("css=div[id='zl__BDLV-main__rows']");

		// Verify document name through GUI
		ZAssert.assertTrue(app.zPageBriefcase
				.waitForPresentInListView(fileName2),
				"Verify new file name through GUI");

		// Display file through RestUtil
		EnumMap<PageBriefcase.Response.ResponsePart, String> response = app.zPageBriefcase.displayFile(fileName2, hm);

		HtmlElement element = HtmlElement.clean(response.get(PageBriefcase.Response.ResponsePart.BODY));
		HtmlElement.evaluate(element, "//body", null, Pattern.compile(".*" + text + ".*"), 1);

		ZAssert.assertEquals(response.get(PageBriefcase.Response.ResponsePart.BODY), text,
				"Verify document content through GUI");
	}


	@Test( description = "Upload file through RestUtil - Verify 'Edit' toolbar button is disabled",
			groups = { "functional", "L2" })

	public void EditFile_03() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/other/testtextfile.txt";

		IItem fileItem = new FileItem(filePath);

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>" + "<doc l='" + briefcaseFolder.getId() + "'>"
				+ "<upload id='" + attachmentId + "'/>" + "</doc>" + "</SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		app.zPageBriefcase.zListItem(Action.A_BRIEFCASE_CHECKBOX, fileItem);

		// Verify 'Edit' tool-bar button is disabled
		ZAssert.assertTrue(app.zPageBriefcase.isToolbarButtonDisabled(PageBriefcase.Locators.zEditFileBtn),
				"Verify 'Edit' toolbar button is disabled");

		// Delete file upon test completion
		app.zPageBriefcase.deleteFileByName(fileItem.getName());
	}


	@Test( description = "Upload file through RestUtil - Verify 'Edit' context menu is disabled",
			groups = { "functional", "L2" })

	public void EditFile_04() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory() + "/data/public/other/testtextfile.txt";

		IItem fileItem = new FileItem(filePath);

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>" + "<doc l='" + briefcaseFolder.getId() + "'>"
				+ "<upload id='" + attachmentId + "'/>" + "</doc>" + "</SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Right Click on created File
		app.zPageBriefcase.zListItem(Action.A_RIGHTCLICK, fileItem);

		// Verify 'Edit' context menu is disabled
		ZAssert.assertTrue(app.zPageBriefcase.isOptionDisabled(PageBriefcase.Locators.zEditMenuDisabled),
				"Verify 'Edit' context menu is disabled");

		// Delete file upon test completion
		app.zPageBriefcase.deleteFileByName(fileItem.getName());
	}


	@Bugs(ids = "54706")
	@Test( description = "'Restore As Current Version' does not restore notes",
		groups = { "functional", "L3" })

	public void EditFile_05() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create file item
		String file1Path = ConfigProperties.getBaseDirectory() + "/data/public/other/restoreversion.txt";
		IItem fileItem = new FileItem(file1Path);

		String notesV1 = "notesVersion1" + ConfigProperties.getUniqueString();
		String notesV2 = "notesVersion2" + ConfigProperties.getUniqueString();
		String nodeCollapsed = "css=div[id^=zlif__BDLV-main__] div[class='ImgNodeCollapsed']";
		String nodeExpanded = "css=div[id^=zlif__BDLV-main__] div[class='ImgNodeExpanded']";
		String locator = "css=tr[id^='zlif__BDLV-main__'] div[id^='zlif__BDLV-main__']:contains('#1:')";

		// Upload file to server through RestUtil
		String attachment1Id = account.uploadFile(file1Path);
		String attachment2Id = account.uploadFile(file1Path);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>" + "<doc desc='" + notesV1 + "' l='"
				+ briefcaseFolder.getId() + "'>" + "<upload id='" + attachment1Id + "'/></doc>"
				+ "</SaveDocumentRequest>");
		String file1Id = account.soapSelectValue("//mail:doc", "id");

		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>" + "<doc desc='" + notesV2 + "' ver='1' l='"
				+ briefcaseFolder.getId() + "' id='" + file1Id + "'>" + "<upload id='" + attachment2Id + "'/>"
				+ "</doc>" + "</SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		if (!app.zPageBriefcase.sIsElementPresent(nodeExpanded)) {
			app.zPageBriefcase.zClickAt(nodeCollapsed, "");
		}

		app.zPageBriefcase.zListItem(Action.A_RIGHTCLICK, Button.O_RESTORE_AS_CURRENT_VERSION, locator);
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		if (!app.zPageBriefcase.sIsElementPresent(nodeExpanded)) {
			app.zPageBriefcase.zClickAt(nodeCollapsed, "");
		}

		ZAssert.assertTrue(
				app.zPageCalendar.sIsElementPresent(
						"css=tr[id^='zlif__BDLV-main__'] div[id^='zlif__BDLV-main__']:contains('#3: " + notesV1 + "')"),
				"'Notes' is restored");

		// Delete file upon test completion
		app.zPageBriefcase.deleteFileByName(fileItem.getName());
	}


	@Bugs(ids = "74644")
	@Test( description = "Cannot rename the file's latest version",
		groups = { "functional", "L3" })

	public void EditFile_06() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create file item
		String file1Path = ConfigProperties.getBaseDirectory() + "/data/public/other/filerename.txt";

		String notesV1 = "notesVersion1" + ConfigProperties.getUniqueString();
		String notesV2 = "notesVersion2" + ConfigProperties.getUniqueString();
		String nodeCollapsed = "css=div[id^=zlif__BDLV-main__] div[class='ImgNodeCollapsed']";
		String nodeExpanded = "css=div[id^=zlif__BDLV-main__] div[class='ImgNodeExpanded']";
		String locator = "css=tr[id^='zlif__BDLV-main__'] div[id^='zlif__BDLV-main__']:contains('#2:')";

		// Upload file to server through RestUtil
		String attachment1Id = account.uploadFile(file1Path);
		String attachment2Id = account.uploadFile(file1Path);

		// Save uploaded file to briefcase through SOAP
		account.soapSend(
				"<SaveDocumentRequest xmlns='urn:zimbraMail'>" +
					"<doc desc='" + notesV1 + "' l='" + briefcaseFolder.getId() + "'>" +
					"<upload id='" + attachment1Id + "'/></doc>" +
				"</SaveDocumentRequest>");
		String file1Id = account.soapSelectValue("//mail:doc", "id");

		account.soapSend(
				"<SaveDocumentRequest xmlns='urn:zimbraMail'>" +
					"<doc desc='" + notesV2 + "' ver='1' l='" + briefcaseFolder.getId() + "' id='" + file1Id + "'>" +
					"<upload id='" + attachment2Id + "'/>" +
					"</doc>" +
				"</SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		if (!app.zPageBriefcase.sIsElementPresent(nodeExpanded)) {
			app.zPageBriefcase.zClickAt(nodeCollapsed, "");
		}

		// Right click on File, select Rename
		app.zPageBriefcase.zListItem(Action.A_RIGHTCLICK, Button.B_RENAME, locator);

		String fileName2 = "renameFile" + ConfigProperties.getUniqueString();

		app.zPageBriefcase.rename(fileName2);
		app.zPageBriefcase.sClick("css=div[id='zl__BDLV-main__rows']");

		// Verify document name through GUI
		ZAssert.assertTrue(app.zPageBriefcase.waitForPresentInListView(fileName2), "Verify new file name through GUI");
	}
}