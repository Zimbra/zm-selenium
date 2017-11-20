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

public class RenameTag extends TouchCore  {
	public RenameTag() {
			logger.info("New "+ RenameTag.class.getCanonicalName());
	}
	
	/**
	 * Test case : rename a tag item.
	 * Steps :
	 * 1. Create a tag item using SOAP.
	 * 2. rename tag from GUI. 
	 * 3. Verify tag is renamed using SOAP.
	 * @throws HarnessException
	 */
	
	@Test (description = "Rename a tag item", 
			groups = { "smoke" })
	
	public void RenameTag_01() throws HarnessException {
		//----------------------- Data ------------------------------
		// Create the tag to rename
		TagItem tag = TagItem.CreateUsingSoap(app.zGetActiveAccount());
		ZAssert.assertNotNull(tag, "Verify the tag was created");
		
		String tagName = tag.getName();
		String newTagName= "Tag" + ConfigProperties.getUniqueString();;
		
		// Refresh to get the tag into the client
		app.zPageMail.zRefresh();		
		SleepUtil.sleepSmall();
		
		PageCreateTag createTagPage = new PageCreateTag((TouchPages) app , this.startingPage);
		
		//Click on edit button
		createTagPage.zPressButton(Button.B_EDIT);
		
		//Select tag
		createTagPage.zSelectTag(tagName);
		
		// Rename the tag
		createTagPage.zEnterTagName(newTagName);
	
		//Click on Save
		createTagPage.zPressButton(Button.B_SAVE);
			
		// UI verification
		ZAssert.assertTrue(createTagPage.zVerifyTagExists(newTagName), "Verify created tag visible in tag pane");
	}
	
}

