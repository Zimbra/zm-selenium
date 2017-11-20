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

import java.util.List;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.EnableBriefcaseFeature;
import com.zimbra.qa.selenium.projects.universal.pages.DialogTag;
import com.zimbra.qa.selenium.projects.universal.pages.briefcase.PageBriefcase;

public class TagFile extends EnableBriefcaseFeature {

	public TagFile() throws HarnessException {
		logger.info("New " + TagFile.class.getCanonicalName());

		// All tests start at the Briefcase page
		super.startingPage = app.zPageBriefcase;

		//if (ConfigProperties.zimbraGetVersionString().contains("FOSS")) {
		    super.startingAccountPreferences.put("zimbraPrefShowSelectionCheckbox","TRUE");
		//}
			    
		super.startingAccountPreferences.put("zimbraPrefBriefcaseReadingPaneLocation", "bottom");
	}

	@Test (description = "Tag a File using Toolbar -> Tag -> New Tag", 
			groups = { "smoke", "L0" })
	public void TagFile_01() throws HarnessException {
		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account,
				SystemFolder.Briefcase);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory()
				+ "/data/public/other/testtextfile.txt";

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

		// Click on created File
		app.zPageBriefcase.zListItem(Action.A_BRIEFCASE_CHECKBOX, fileItem);
		/*
		if (ConfigProperties.zimbraGetVersionString().contains(
    			"FOSS")) {
		    app.zPageBriefcase.zListItem(Action.A_BRIEFCASE_CHECKBOX, fileItem);

		} else {
		    app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, fileItem);
		}
		*/
		// Click on header check box
		// app.zPageBriefcase.zHeader(Action.A_BRIEFCASE_HEADER_CHECKBOX);

		// Create a tag using GUI
		String tagName = "tag" + ConfigProperties.getUniqueString();

		// Click on New Tag
		DialogTag dialogTag = (DialogTag) app.zPageBriefcase
				.zToolbarPressPulldown(Button.B_TAG, Button.O_TAG_NEWTAG, null);

		SleepUtil.sleepSmall();
		dialogTag.zSetTagName(tagName);
		dialogTag.zPressButton(Button.B_OK);

		

		// Make sure the tag was created on the server (get the tag ID)
		account.soapSend("<GetTagRequest xmlns='urn:zimbraMail'/>");

		String tagId = account.soapSelectValue(
				"//mail:GetTagResponse//mail:tag[@name='" + tagName + "']",
				"id");

		// Verify tagged File name
		account
				.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='document'>"
						+ "<query>tag:"
						+ tagName
						+ "</query>"
						+ "</SearchRequest>");

		String name = account.soapSelectValue(
				"//mail:SearchResponse//mail:doc", "name");

		ZAssert.assertNotNull(name,
				"Verify the search response returns the document name");

		ZAssert.assertEquals(name, fileName, "Verify tagged File name");

		// Make sure the tag was applied to the File
		// account.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='document'>"
		// + "<query>in:briefcase</query></SearchRequest>");

		// String id = account.soapSelectValue(
		// "//mail:SearchResponse//mail:doc[@name='" + docName + "']", "t");

		account
				.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='document'>"
						+ "<query>"
						+ fileName
						+ "</query>"
						+ "</SearchRequest>");

		String id = account.soapSelectValue("//mail:SearchResponse//mail:doc",
				"t");

		ZAssert.assertNotNull(id,
				"Verify the search response returns the document tag id");

		ZAssert.assertEquals(id, tagId,
				"Verify the tag was attached to the File");

		// delete file upon test completion
		app.zPageBriefcase.deleteFileByName(fileName);
	}

	@Test (description = "Tag uploaded File using pre-existing Tag", 
			groups = { "smoke", "L1" })
	public void TagFile_02() throws HarnessException {
		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account,
				SystemFolder.Briefcase);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory()
				+ "/data/public/other/testtextfile.txt";

		FileItem fileItem = new FileItem(filePath);

		String fileName = fileItem.getName();

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>"
				+ "<doc l='" + briefcaseFolder.getId() + "'><upload id='"
				+ attachmentId + "'/></doc></SaveDocumentRequest>");

		// Create a tag
		String tagName = "tag" + ConfigProperties.getUniqueString();

		account.soapSend("<CreateTagRequest xmlns='urn:zimbraMail'>"
				+ "<tag name='" + tagName + "' color='1' />"
				+ "</CreateTagRequest>");

		// Make sure the tag was created on the server
		TagItem tag = TagItem.importFromSOAP(app.zGetActiveAccount(), tagName);

		ZAssert.assertNotNull(tag, "Verify the new tag was created");

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
		// Click on header check box
		// app.zPageBriefcase.zHeader(Action.A_BRIEFCASE_HEADER_CHECKBOX);

		// Tag file selecting pre-existing tag from Toolbar drop down list
		app.zPageBriefcase.zToolbarPressPulldown(Button.B_TAG, tag.getName());

		// Make sure the tag was applied to the File
		account
				.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='document'>"
						+ "<query>"
						+ fileName
						+ "</query>"
						+ "</SearchRequest>");

		String id = account.soapSelectValue("//mail:SearchResponse//mail:doc",
				"t");

		ZAssert.assertNotNull(id,
				"Verify the search response returns the document tag id");

		ZAssert.assertStringContains(id, tag.getId(),
				"Verify the tag was attached to the File");

		// delete file upon test completion
		app.zPageBriefcase.deleteFileByName(fileName);
	}

	@Test (description = "Tag uploaded File using Right Click context menu", 
			groups = { "functional", "L2" })
	public void TagFile_03() throws HarnessException {
		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account,
				SystemFolder.Briefcase);

		// Create file item
		String filePath = ConfigProperties.getBaseDirectory()
				+ "/data/public/other/testtextfile.txt";

		FileItem fileItem = new FileItem(filePath);

		String fileName = fileItem.getName();

		// Upload file to server through RestUtil
		String attachmentId = account.uploadFile(filePath);

		// Save uploaded file to briefcase through SOAP
		account.soapSend("<SaveDocumentRequest xmlns='urn:zimbraMail'>"
				+ "<doc l='" + briefcaseFolder.getId() + "'><upload id='"
				+ attachmentId + "'/></doc></SaveDocumentRequest>");

		// Create a tag
		String tagName = "tag" + ConfigProperties.getUniqueString();

		account.soapSend("<CreateTagRequest xmlns='urn:zimbraMail'>"
				+ "<tag name='" + tagName + "' color='1' />"
				+ "</CreateTagRequest>");

		// Make sure the tag was created on the server
		TagItem tagItem = TagItem.importFromSOAP(app.zGetActiveAccount(),
				tagName);

		ZAssert.assertNotNull(tagItem, "Verify the new tag was created");

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

		// Click on header check box
		// app.zPageBriefcase.zHeader(Action.A_BRIEFCASE_HEADER_CHECKBOX);

		// Tag File using Right Click context menu
		app.zPageBriefcase.zListItem(Action.A_RIGHTCLICK, Button.O_TAG_FILE,
				tagItem.getName(), fileItem);

		// Make sure the tag was applied to the File
		account
				.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='document'>"
						+ "<query>"
						+ fileName
						+ "</query>"
						+ "</SearchRequest>");

		String id = account.soapSelectValue("//mail:SearchResponse//mail:doc",
				"t");

		ZAssert.assertNotNull(id,
				"Verify the search response returns the document tag id");

		ZAssert.assertStringContains(id, tagItem.getId(),
				"Verify the tag was attached to the File");

		// delete file upon test completion
		app.zPageBriefcase.deleteFileByName(fileName);
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
				app.zPageBriefcase.closeWindow();
			}
		}
		app.zPageBriefcase.zSelectWindow(PageBriefcase.pageTitle);
	}
}
