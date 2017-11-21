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

package com.zimbra.qa.selenium.projects.touch.tests.contacts.tags;

import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.pages.TouchPages;
import com.zimbra.qa.selenium.projects.touch.pages.PageCreateTag;
import com.zimbra.qa.selenium.projects.touch.core.TouchCore;

public class CreateTag extends TouchCore {

	private String tagName = null;
	private String errorMessage = null;
	public CreateTag() {
		logger.info("New " + CreateTag.class.getCanonicalName());
		super.startingPage = app.zPageAddressbook;
	}
	
	/**
	 * Test case : Create a tag item
	 * Steps :
	 * 1. Create a tag from GUI. 
	 * 2. Verify tag is created from SOAP & UI
	 * @throws HarnessException
	 */
	
	@Test (description = "Create a new tag by clicking to Edit -> 'New tag'", 
			groups = { "sanity" })
	
	public void CreateTag_01() throws HarnessException  {
		
		tagName = "tag" + ConfigProperties.getUniqueString();
	
		PageCreateTag createTagPage = new PageCreateTag((TouchPages) app , this.startingPage);
		
		//Click on edit button
		createTagPage.zPressButton(Button.B_EDIT);
		//Click on new tag button
		createTagPage.zPressButton(Button.B_NEW_TAG);
		//Enter tag name
		createTagPage.zEnterTagName(tagName);
		//Click on Save
		createTagPage.zPressButton(Button.B_SAVE);

		// Make sure the tag was created on the ZCS server
		TagItem tag = TagItem.importFromSOAP(app.zGetActiveAccount(),tagName);
		ZAssert.assertNotNull(tag, "Verify the new tag was created");
		ZAssert.assertEquals(tag.getName(), tagName,"Verify the server and client tag names match");
		
		// UI verification
		ZAssert.assertTrue(createTagPage.zVerifyTagExists(tagName), "Verify created tag visible in tag pane");		
	}
	
	/**
	 * Test case : Verify error is displayed on creation of duplicate tag 
	 * Steps :
	 * 1. Create a tag from GUI.
	 * 2. Try to create duplicate tag 
	 * 3. Verify error message
	 * 4. Verify existing tag
	 * @throws HarnessException
	 */
	
	@Test (description = "Try to create duplicate tag & verify error", 
			groups = { "functional" })
	
	public void CreateTag_02() throws HarnessException  {
		
		tagName = "tag" + ConfigProperties.getUniqueString();
		errorMessage="An object with that name already exists.";
	
		PageCreateTag createTagPage = new PageCreateTag((TouchPages) app , this.startingPage);
		
		//Click on edit button
		createTagPage.zPressButton(Button.B_EDIT);
		//Click on new tag button
		createTagPage.zPressButton(Button.B_NEW_TAG);
		//Enter tag name
		createTagPage.zEnterTagName(tagName);
		//Click on Save
		createTagPage.zPressButton(Button.B_SAVE);
		
		//Try to create duplicate tag
		//Click on edit button
		createTagPage.zPressButton(Button.B_EDIT);
		//Click on new tag button
		createTagPage.zPressButton(Button.B_NEW_TAG);
		//Enter tag name
		createTagPage.zEnterTagName(tagName);
		//Click on Save
		createTagPage.zPressButton(Button.B_SAVE);
		
		
		//Verify error dialog is displayed
		createTagPage.zVerifyDialogText(errorMessage);
		
		//Click on OK button 
		createTagPage.zPressButton(Button.B_OK);
		
		// Verify existing tag
		TagItem tag = TagItem.importFromSOAP(app.zGetActiveAccount(),tagName);
		ZAssert.assertNotNull(tag, "Verify the new tag was created");
		ZAssert.assertEquals(tag.getName(), tagName,"Verify the server and client tag names match");
				
		// UI verification
		ZAssert.assertTrue(createTagPage.zVerifyTagExists(tagName), "Verify created tag visible in tag pane");
		
		
	}
		
}