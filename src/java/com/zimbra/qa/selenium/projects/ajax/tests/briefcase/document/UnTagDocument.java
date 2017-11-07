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
import com.zimbra.qa.selenium.projects.ajax.core.FeatureBriefcaseTest;

public class UnTagDocument extends FeatureBriefcaseTest {

	public UnTagDocument() {
		logger.info("New " + UnTagDocument.class.getCanonicalName());
		super.startingPage = app.zPageBriefcase;
		super.startingAccountPreferences.put("zimbraPrefBriefcaseReadingPaneLocation", "bottom");
	}


	@Test (description = "Remove a tag from a Document using Toolbar -> Tag -> Remove Tag",
			groups = { "smoke", "L1" })

	public void UnTagDocument_01() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Create document item
		DocumentItem docItem = new DocumentItem();

		// Create document using SOAP
		String contentHTML = XmlStringUtil.escapeXml("<html>" + "<body>" + docItem.getDocText() + "</body>" + "</html>");

		account.soapSend("<SaveDocumentRequest requestId='0' xmlns='urn:zimbraMail'>"
						+ "<doc name='"
						+ docItem.getName()
						+ "' l='"
						+ briefcaseFolder.getId()
						+ "' ct='application/x-zimbra-doc'>"
						+ "<content>"
						+ contentHTML
						+ "</content>"
						+ "</doc>"
						+ "</SaveDocumentRequest>");

		// Create a tag
		String tagName = "tag" + ConfigProperties.getUniqueString();

		account.soapSend("<CreateTagRequest xmlns='urn:zimbraMail'>" + "<tag name='" + tagName + "' color='1' />"
				+ "</CreateTagRequest>");

		TagItem tagItem = TagItem.importFromSOAP(app.zGetActiveAccount(), tagName);

		ZAssert.assertNotNull(tagItem, "Verify the new tag was created");

		String tagId = tagItem.getId();

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Click on created document
		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, docItem);

		// Tag document using Right Click context menu
		app.zPageBriefcase.zListItem(Action.A_RIGHTCLICK, Button.O_TAG_FILE,
				tagItem.getName(), docItem);

		// Make sure the tag was applied to the document
		account.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='document'>"
						+ "<query>"
						+ docItem.getName()
						+ "</query>"
						+ "</SearchRequest>");

		String id = account.soapSelectValue("//mail:SearchResponse//mail:doc", "t");
		ZAssert.assertNotNull(id, "Verify the search response returns the document tag id");
		ZAssert.assertEquals(id, tagId, "Verify the tag was attached to the document");

		// Select briefcase folder
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, true);

		// Click on tagged document
		app.zPageBriefcase.zListItem(Action.A_LEFTCLICK, docItem);

		// Click Remove Tag
		app.zPageBriefcase.zToolbarPressPulldown(Button.B_TAG,
				Button.O_TAG_REMOVETAG, docItem);

		account.soapSend("<SearchRequest xmlns='urn:zimbraMail' types='document'>"
						+ "<query>"
						+ docItem.getName()
						+ "</query>"
						+ "</SearchRequest>");

		id = account.soapSelectValue("//mail:SearchResponse//mail:doc", "t");
		ZAssert.assertStringDoesNotContain(id, tagId, "Verify that the tag is removed from the message");
	}
}