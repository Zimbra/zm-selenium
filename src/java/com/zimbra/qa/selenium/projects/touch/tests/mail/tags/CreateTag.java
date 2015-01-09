/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */

package com.zimbra.qa.selenium.projects.touch.tests.mail.tags;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.touch.ui.PageCreateTag;


public class CreateTag extends PrefGroupMailByMessageTest {

	private String tagName = null;
	private String errorMessage = null;
	public CreateTag() {
		logger.info("New " + CreateTag.class.getCanonicalName());
	}
	
	/**
	 * Test case : Create a tag item
	 * Steps :
	 * 1. Create a tag from GUI. 
	 * 2. Verify tag is created from SOAP & UI
	 * @throws HarnessException
	 */
	
	@Test( description = "Create a new tag by clicking to Edit -> 'New tag'", 
			groups = { "sanity" })
	
	public void CreateTag_01() throws HarnessException  {
		
		tagName = "tag" + ZimbraSeleniumProperties.getUniqueString();
	
		PageCreateTag createtagPage = (PageCreateTag) app.zTreeMail.zPressButton(Button.B_NEW_TAG);
		
		createtagPage.zEnterTagName(tagName);
		createtagPage.zClickButton(Button.B_SAVE);

		// Make sure the tag was created on the ZCS server
		TagItem tag = TagItem.importFromSOAP(app.zGetActiveAccount(),tagName);
		ZAssert.assertNotNull(tag, "Verify the new tag was created");
		ZAssert.assertEquals(tag.getName(), tagName,"Verify the server and client tag names match");
		
		// UI verification
		ZAssert.assertTrue(createtagPage.zVerifyTagExists(tagName), "Verify created tag visible in tag pane");
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
	
	@Test( description = "Try to create duplicate tag & verify error.", 
			groups = { "functional" })
	
	public void CreateTag_02() throws HarnessException  {
		
		tagName = "tag" + ZimbraSeleniumProperties.getUniqueString();
		errorMessage="An object with that name already exists.";
	
		PageCreateTag createtagPage = (PageCreateTag) app.zTreeMail.zPressButton(Button.B_NEW_TAG);
		
		//Create a tag item
		createtagPage.zEnterTagName(tagName);
		createtagPage.zClickButton(Button.B_SAVE);
		
		//Try to create duplicate tag
		app.zTreeMail.zPressButton(Button.B_NEW_TAG);
		createtagPage.zEnterTagName(tagName);
		createtagPage.zClickButton(Button.B_SAVE);
		
		//Verify error dialog is displayed
		createtagPage.zVerifyDialogText(errorMessage);
		
		//Click on OK button 
		createtagPage.zClickButton(Button.B_OK);
		
		// Verify existing tag
		TagItem tag = TagItem.importFromSOAP(app.zGetActiveAccount(),tagName);
		ZAssert.assertNotNull(tag, "Verify the new tag was created");
		ZAssert.assertEquals(tag.getName(), tagName,"Verify the server and client tag names match");
				
		// UI verification
		ZAssert.assertTrue(createtagPage.zVerifyTagExists(tagName), "Verify created tag visible in tag pane");
		
		
	}
		
}