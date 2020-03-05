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
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.EnableBriefcaseFeature;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogTag;

public class TagDocument extends EnableBriefcaseFeature {

	public TagDocument() {
		logger.info("New " + TagDocument.class.getCanonicalName());
		super.startingPage = app.zPageBriefcase;
		super.startingAccountPreferences.put("zimbraPrefBriefcaseReadingPaneLocation", "bottom");
	}


	@Test (description = "Tag a Document using Toolbar -> Tag -> New Tag",
			groups = { "bhr" })

	public void TagDocument_01() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create document item
		DocumentItem docItem = new DocumentItem();

		String docName = docItem.getName();
		String docText = docItem.getDocText();

		// Create document using SOAP
		String contentHTML = XmlStringUtil.escapeXml("<html>" + "<body>" + docText + "</body>" + "</html>");

		account.soapSend("<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>" + "<doc name='" + docName
				+ "' l='" + briefcaseFolder.getId() + "' ct='application/x-zimbra-doc'>" + "<content>" + contentHTML
				+ "</content>" + "</doc>" + "</SaveDocumentRequest>");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Click on created document
		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, docItem);

		// Create a tag using GUI
		String tagName = "tag" + ConfigProperties.getUniqueString();

		// Click on New Tag
		DialogTag dialogTag = (DialogTag) app.zPageBriefcase.zToolbarPressPulldown(Button.B_TAG, Button.O_TAG_NEWTAG,
				null);

		dialogTag.zSetTagName(tagName);
		dialogTag.zPressButton(Button.B_OK);

		// Make sure the tag was created on the server (get the tag ID)
		account.soapSend("<GetTagRequest xmlns='urn:zimbraMail'/>");

		String tagId = account.soapSelectValue("//mail:GetTagResponse//mail:tag[@name='" + tagName + "']", "id");

		// Verify tagged document name
		account.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='document'>" + "<query>tag:" + tagName
				+ "</query>" + "</SearchRequest>");

		String name = account.soapSelectValue("//mail:SearchResponse//mail:doc", "name");
		ZAssert.assertEquals(name, docName, "Verify tagged document name");
		account.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='document'>" + "<query>" + docName + "</query>"
				+ "</SearchRequest>");

		String id = account.soapSelectValue("//mail:SearchResponse//mail:doc", "t");
		ZAssert.assertNotNull(id, "Verify the search response returns the document tag id");
		ZAssert.assertEquals(id, tagId, "Verify the tag was attached to the document");

		// Delete Document upon test completion
		app.zPageBriefcase.deleteFileByName(docName);
	}


	@Test (description = "Tag a Document using pre-existing Tag",
			groups = { "sanity" })

	public void TagDocument_02() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create document item
		DocumentItem docItem = new DocumentItem();

		String docName = docItem.getName();
		String docText = docItem.getDocText();

		// Create document using SOAP
		String contentHTML = XmlStringUtil.escapeXml("<html>" + "<body>" + docText + "</body>" + "</html>");

		account.soapSend("<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>" + "<doc name='" + docName
				+ "' l='" + briefcaseFolder.getId() + "' ct='application/x-zimbra-doc'>" + "<content>" + contentHTML
				+ "</content>" + "</doc>" + "</SaveDocumentRequest>");

		// Create a tag
		String tagName = "tag" + ConfigProperties.getUniqueString();

		account.soapSend("<CreateTagRequest xmlns='urn:zimbraMail'>" + "<tag name='" + tagName + "' color='1' />"
				+ "</CreateTagRequest>");

		// Make sure the tag was created on the server
		TagItem tag = TagItem.importFromSOAP(app.zGetActiveAccount(), tagName);
		ZAssert.assertNotNull(tag, "Verify the new tag was created");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Click on created document
		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, docItem);

		// Tag document selecting pre-existing tag from Toolbar drop down list
		app.zPageBriefcase.zToolbarPressPulldown(Button.B_TAG, tag.getName());

		// Make sure the tag was applied to the document
		account.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='document'>" + "<query>" + docName + "</query>"
				+ "</SearchRequest>");

		String id = account.soapSelectValue("//mail:SearchResponse//mail:doc", "t");
		ZAssert.assertNotNull(id, "Verify the search response returns the document tag id");
		ZAssert.assertEquals(id, tag.getId(), "Verify the tag was attached to the document");

		// Delete Document upon test completion
		app.zPageBriefcase.deleteFileByName(docName);
	}


	@Test (description = "Tag a Document using Right Click context menu",
			groups = { "functional" })

	public void TagDocument_03() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create document item
		DocumentItem docItem = new DocumentItem();

		String docName = docItem.getName();
		String docText = docItem.getDocText();

		// Create document using SOAP
		String contentHTML = XmlStringUtil.escapeXml("<html>" + "<body>" + docText + "</body>" + "</html>");

		account.soapSend("<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>" + "<doc name='" + docName
				+ "' l='" + briefcaseFolder.getId() + "' ct='application/x-zimbra-doc'>" + "<content>" + contentHTML
				+ "</content>" + "</doc>" + "</SaveDocumentRequest>");

		// Create a tag
		String tagName = "tag" + ConfigProperties.getUniqueString();

		account.soapSend("<CreateTagRequest xmlns='urn:zimbraMail'>" + "<tag name='" + tagName + "' color='1' />"
				+ "</CreateTagRequest>");

		// Make sure the tag was created on the server
		TagItem tagItem = TagItem.importFromSOAP(app.zGetActiveAccount(), tagName);
		ZAssert.assertNotNull(tagItem, "Verify the new tag was created");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Click on created document
		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, docItem);

		// Tag document using Right Click context menu
		app.zPageBriefcase.zListItem(Action.A_RIGHTCLICK, Button.O_TAG_FILE, tagItem.getName(), docItem);

		// Make sure the tag was applied to the document
		account.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='document'>" + "<query>" + docName + "</query>"
				+ "</SearchRequest>");

		String id = account.soapSelectValue("//mail:SearchResponse//mail:doc", "t");
		ZAssert.assertNotNull(id, "Verify the search response returns the document tag id");
		ZAssert.assertEquals(id, tagItem.getId(), "Verify the tag was attached to the document");

		// Delete Document upon test completion
		app.zPageBriefcase.deleteFileByName(docName);
	}
}
