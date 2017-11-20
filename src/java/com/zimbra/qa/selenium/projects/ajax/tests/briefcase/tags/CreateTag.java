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
package com.zimbra.qa.selenium.projects.ajax.tests.briefcase.tags;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.EnableBriefcaseFeature;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogTag;

public class CreateTag extends EnableBriefcaseFeature {

	public CreateTag() {
		logger.info("New " + CreateTag.class.getCanonicalName());
		super.startingPage = app.zPageBriefcase;
	}


	@Test (description = "Create a new tag by clicking 'new tag' on folder tree",
			groups = { "functional", "L2" })

	public void CreateTag_01() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		// Set the new tag name
		String name = "tag" + ConfigProperties.getUniqueString();

		DialogTag dialog = (DialogTag) app.zTreeBriefcase.zPressPulldown(Button.B_TREE_TAGS_OPTIONS,
				Button.B_TREE_NEWTAG);
		ZAssert.assertNotNull(dialog, "Verify the new dialog opened");

		// Fill out the input field
		dialog.zSetTagName(name);
		dialog.zPressButton(Button.B_OK);

		// Make sure the tag was created on the server
		TagItem tag = TagItem.importFromSOAP(account, name);
		ZAssert.assertNotNull(tag, "Verify the new folder was created");

		ZAssert.assertEquals(tag.getName(), name, "Verify the server and client tag names match");
	}


	@Test (description = "Create a new tag using keyboard shortcuts",
			groups = { "functional", "L3" })

	public void CreateTag_02() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		Shortcut shortcut = Shortcut.S_NEWTAG;

		// Set the new tag name
		String name = "tag" + ConfigProperties.getUniqueString();

		// Select briefcase folder tags section before creating a new tag
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		DialogTag dialog = (DialogTag) app.zPageBriefcase.zKeyboardShortcut(shortcut);
		ZAssert.assertNotNull(dialog, "Verify the new dialog opened");

		SleepUtil.sleepMedium();
		// Fill out the input field
		dialog.zSetTagName(name);
		dialog.zPressButton(Button.B_OK);

		// Select briefcase folder tags section after creating a new tag
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		// Make sure the tag was created on the server
		TagItem tag = TagItem.importFromSOAP(account, name);
		ZAssert.assertNotNull(tag, "Verify the new folder was created");

		// Click on the tag and make sure it appears in the tree
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, tag);

		ZAssert.assertEquals(tag.getName(), name, "Verify the server and client tag names match");
	}


	@Test (description = "Create a new tag using context menu on a tag",
			groups = { "functional", "L3" })

	public void CreateTag_03() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Set the new tag name
		String name1 = "tag" + ConfigProperties.getUniqueString();
		String name2 = "tag" + ConfigProperties.getUniqueString();

		// Create a tag to right click on
		account.soapSend("<CreateTagRequest xmlns='urn:zimbraMail'>" + "<tag name='" + name1 + "' color='1' />"
				+ "</CreateTagRequest>");

		// Get the tag
		TagItem tag1 = TagItem.importFromSOAP(account, name1);

		// Select briefcase folder tags section before creating a new tag
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		// Create a new tag using the context menu + New Tag
		DialogTag dialog = (DialogTag) app.zTreeBriefcase.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_NEWTAG, tag1);
		ZAssert.assertNotNull(dialog, "Verify the new dialog opened");

		SleepUtil.sleepMedium();
		// Fill out the input field
		dialog.zSetTagName(name2);
		dialog.zPressButton(Button.B_OK);

		// Select briefcase folder tags section after creating a new tag
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		// Make sure the tag was created on the server
		TagItem tag2 = TagItem.importFromSOAP(account, name2);
		ZAssert.assertNotNull(tag2, "Verify the new tag was created");

		ZAssert.assertEquals(tag2.getName(), name2, "Verify the server and client tag names match");
	}


	@Test (description = "Create a new tag using briefcase app New -> New Tag",
			groups = { "functional", "L3" })

	public void CreateTag_04() throws HarnessException {

		ZimbraAccount account = app.zGetActiveAccount();

		FolderItem briefcaseFolder = FolderItem.importFromSOAP(account, SystemFolder.Briefcase);

		// Set the new tag name
		String name = "tag" + ConfigProperties.getUniqueString();

		// Select briefcase folder tags section before creating a new tag
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		// Create a new tag in the Briefcase using the New pull down menu + Tag
		DialogTag dialog = (DialogTag) app.zPageBriefcase.zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_TAG, null);
		ZAssert.assertNotNull(dialog, "Verify the new dialog opened");

		SleepUtil.sleepMedium();
		// Fill out the input field
		dialog.zSetTagName(name);
		dialog.zPressButton(Button.B_OK);

		// Select briefcase folder tags section after creating a new tag
		app.zTreeBriefcase.zTreeItem(Action.A_LEFTCLICK, briefcaseFolder, false);

		// Make sure the tag was created on the server
		TagItem tag = TagItem.importFromSOAP(account, name);
		ZAssert.assertNotNull(tag, "Verify the new tag was created");
		ZAssert.assertEquals(tag.getName(), name, "Verify the server and client tag names match");
	}
}