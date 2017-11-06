/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.tasks.tags;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.DialogTag;

public class CreateTag extends UniversalCommonTest {

	public CreateTag() {
		logger.info("New "+ CreateTag.class.getCanonicalName());

		// All tests start at the login page
		super.startingPage = app.zPageTasks;
		
	}

	@Test( description = "Create a new tag by clicking 'new tag' on Task page", 
			groups = { "sanity", "L0"})
	
	public void CreateTag_01() throws HarnessException {

		// Set the new tag name
		String name = "tag" + ConfigProperties.getUniqueString();

		DialogTag dialog = null;
		dialog = (DialogTag)app.zTreeTasks.zPressPulldown(Button.B_TREE_TAGS_OPTIONS, Button.B_TREE_NEWTAG);
		ZAssert.assertNotNull(dialog, "Verify the new tag dialog opened");

		// Fill out the form with the basic details
		dialog.zSubmit(name);

		// Make sure the tag was created on the server
		TagItem tag = app.zPageTasks.zGetTagItem(app.zGetActiveAccount(), name);

		ZAssert.assertNotNull(tag, "Verify the new tag was created");
		ZAssert.assertEquals(tag.getName(), name, "Verify the server and client tag names match");
	}

	@Test( description = "Create a new tag using keyboard shortcuts on Task page", 
			groups = { "smoke", "L1"})
	
	public void CreateTag_02() throws HarnessException {

		Shortcut shortcut = Shortcut.S_NEWTAG;
		FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Tasks);

		// Set the new tag name
		String name = "tag" + ConfigProperties.getUniqueString();
		
		//Added explicitly boz some time focus does shifted into search input after login
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		DialogTag dialog = (DialogTag)app.zPageTasks.zKeyboardShortcut(shortcut);
		ZAssert.assertNotNull(dialog, "Verify the new dialog opened");

		// Fill out the form with the basic details
		dialog.zSubmit(name);

		

		//Need to click on Task folder explicitly so that created tag does show in tag list.
		app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

		// Make sure the tag was created on the server
		TagItem tag = app.zPageTasks.zGetTagItem(app.zGetActiveAccount(), name);

		ZAssert.assertNotNull(tag, "Verify the new tag was created");
		ZAssert.assertEquals(tag.getName(), name, "Verify the server and client tag names match");

	}
	
	@Test( description = "Create a new tag using context menu from a tag", 
			groups = { "smoke", "L1"})
	
	public void CreateTag_03() throws HarnessException {

		// Set the new tag name
		String name1 = "tag" + ConfigProperties.getUniqueString();
		String name2 = "tag" + ConfigProperties.getUniqueString();

		// Create a tag to right click on
		app.zGetActiveAccount().soapSend(
				"<CreateTagRequest xmlns='urn:zimbraMail'>" +
				"<tag name='"+name2+"' color='1' />" +
		"</CreateTagRequest>");

		// Get the tag
		TagItem tag2 = TagItem.importFromSOAP(app.zGetActiveAccount(), name2);

		//Need to click on Task folder explicitly so that created tag does show in tag list.
		app.zPageTasks.zToolbarPressButton(Button.B_REFRESH);
		
		// Work around
		app.zPageMain.zRefreshMainUI();
		app.zPageTasks.zNavigateTo();

		// Create a new tag using the context menu + New Tag
		DialogTag dialog = (DialogTag)app.zTreeTasks.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_NEWTAG, tag2);
		ZAssert.assertNotNull(dialog, "Verify the new dialog opened");

		// Fill out the form with the basic details
		dialog.zSubmit(name1);

		// Make sure the tag was created on the server
		TagItem tag1 = app.zPageTasks.zGetTagItem(app.zGetActiveAccount(), name1);
		ZAssert.assertNotNull(tag1, "Verify the new tag was created");

		ZAssert.assertEquals(tag1.getName(), name1, "Verify the server and client tag names match");
	}
	
	@Test( description = "Create a new tag using task app New -> Tag", 
			groups = { "smoke", "L1"})
	
	public void CreateTag_04() throws HarnessException {

		// Set the new tag name
		String name = "tag" + ConfigProperties.getUniqueString();

		// Create a new tag in the task page using the context menu + New tag
		DialogTag dialog = (DialogTag) app.zPageTasks.zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_TAG);
		ZAssert.assertNotNull(dialog, "Verify the new dialog opened");

		// Fill out the form with the basic details
		dialog.zSubmit(name);

		// Make sure the task was created on the server
		TagItem tag = app.zPageTasks.zGetTagItem(app.zGetActiveAccount(), name);
		ZAssert.assertNotNull(tag, "Verify the new tag was created");

		ZAssert.assertEquals(tag.getName(), name,
		"Verify the server and client tag names match");

	}

}
