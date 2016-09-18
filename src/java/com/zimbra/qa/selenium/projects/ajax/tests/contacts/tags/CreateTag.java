/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.tags;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.TagItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogTag;




public class CreateTag extends PrefGroupMailByMessageTest {

	public CreateTag() {
		logger.info("New "+ CreateTag.class.getCanonicalName());
		
		
		// All tests start at the addressbook page
		super.startingPage = app.zPageContacts;
		

	}
	
	@Test( description = "Create a new tag by clicking 'new tag' on folder tree",
			groups = { "sanity" })
	
	public void CreateTag_01() throws HarnessException {
		
		
		// Set the new tag name
		String name = "tag" + ConfigProperties.getUniqueString();
		
		DialogTag dialog = (DialogTag)app.zTreeContacts.zPressButton(Button.B_TREE_NEWTAG);
		ZAssert.assertNotNull(dialog, "Verify the new dialog opened");
		
		// Fill out the form with the basic details
		dialog.zSetTagName(name);
		dialog.zSubmit();


		// Make sure the tag was created on the server
		TagItem tag = TagItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertNotNull(tag, "Verify the new folder was created");
		
		ZAssert.assertEquals(tag.getName(), name, "Verify the server and client tag names match");
		
	}

	
	
	@Test( description = "Create a new tag using keyboard shortcuts",
			groups = { "functional" })
	public void CreateTag_02() throws HarnessException {
		
		Shortcut shortcut = Shortcut.S_NEWTAG;
		
		
		
		// Set the new tag name
		String name = "tag" + ConfigProperties.getUniqueString();
		
		DialogTag dialog = (DialogTag)app.zPageContacts.zKeyboardShortcut(shortcut);
		ZAssert.assertNotNull(dialog, "Verify the new dialog opened");
		
		// Fill out the form with the basic details
		dialog.zSetTagName(name);
		dialog.zSubmit();


		// Make sure the tag was created on the server
		TagItem tag = TagItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertNotNull(tag, "Verify the new folder was created");
		
		ZAssert.assertEquals(tag.getName(), name, "Verify the server and client tag names match");
		
		
	}

	@Test( description = "Create a new tag using context menu from a tag",
			groups = { "functional" })
	public void CreateTag_03() throws HarnessException {
		
		// Work around due to duplicate dialog ids
		app.zPageMain.sRefresh();
		app.zPageContacts.zNavigateTo();
		
		// Set the new tag name
		String name1 = "tag" + ConfigProperties.getUniqueString();
		String name2 = "tag" + ConfigProperties.getUniqueString();
		
		// Create a tag to right click on
		app.zGetActiveAccount().soapSend(
				"<CreateTagRequest xmlns='urn:zimbraMail'>" +
            		"<tag name='"+ name2 +"' color='1' />" +
            	"</CreateTagRequest>");


		// Get the tag
		TagItem tag2 = TagItem.importFromSOAP(app.zGetActiveAccount(), name2);

		
		// Refresh to get tag2
		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);
		
		// Create a new tag using the context menu + New Tag
		DialogTag dialog = (DialogTag)app.zTreeContacts.zTreeItem(Action.A_RIGHTCLICK, Button.B_TREE_NEWTAG, tag2);
		ZAssert.assertNotNull(dialog, "Verify the new dialog opened");
		
		// Fill out the form with the basic details
		dialog.zSetTagName(name1);
		dialog.zSubmit();


		// Make sure the folder was created on the server
		TagItem tag1 = TagItem.importFromSOAP(app.zGetActiveAccount(), name1);

		ZAssert.assertNotNull(tag1, "Verify the new tag was created");
		
		ZAssert.assertEquals(tag1.getName(), name1, "Verify the server and client tag names match");
		
	}

	@Test( description = "Create a new tag using mail app New -> New Tag",
			groups = { "functional" })
	public void CreateTag_04() throws HarnessException {
		
		
		// Set the new folder name
		String name = "tag" + ConfigProperties.getUniqueString();
				
		// Create a new folder in the inbox
		// using the context menu + New Folder
		DialogTag dialog = (DialogTag)app.zPageContacts.zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_TAG);
		ZAssert.assertNotNull(dialog, "Verify the new dialog opened");
		
		// Fill out the form with the basic details
		dialog.zSetTagName(name);
		dialog.zSubmit();


		// Make sure the folder was created on the server
		TagItem tag = TagItem.importFromSOAP(app.zGetActiveAccount(), name);
		ZAssert.assertNotNull(tag, "Verify the new tag was created");
		
		ZAssert.assertEquals(tag.getName(), name, "Verify the server and client tag names match");
		
	}


}
