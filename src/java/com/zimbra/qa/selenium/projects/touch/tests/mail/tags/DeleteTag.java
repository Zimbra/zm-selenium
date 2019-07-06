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
package com.zimbra.qa.selenium.projects.touch.tests.mail.tags;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.core.SetGroupMailByMessagePreference;
import com.zimbra.qa.selenium.projects.touch.pages.PageCreateTag;

public class DeleteTag extends SetGroupMailByMessagePreference  {
	public DeleteTag() {
			logger.info("New "+ DeleteTag.class.getCanonicalName());
	}
	
	/**
	 * Test case : Delete a tag item.
	 * Steps :
	 * 1. Create a tag item using SOAP.
	 * 2. Delete tag from GUI. 
	 * 3. Verify tag is deleted using SOAP.
	 * @throws HarnessException
	 */
	
	@Test (description = "Delete a tag item", 
			groups = { "bhr" })
	
	public void DeleteTag_01() throws HarnessException {
		//----------------------- Data ------------------------------
		// Create the tag to delete
		TagItem tag = TagItem.CreateUsingSoap(app.zGetActiveAccount());
		ZAssert.assertNotNull(tag, "Verify the tag was created");
		String tagName = tag.getName();
		
		
		// Refresh to get the tag into the client
		app.zPageMail.zRefresh();		
		SleepUtil.sleepSmall();
		
		//Select tag
		PageCreateTag createtagPage = (PageCreateTag) app.zTreeMail.zPressButton(Button.B_EDIT);
		createtagPage.zSelectTag(tagName);
		
		// Click on delete button
		createtagPage.zPressButton(Button.B_DELETE);
	
		//Click on Yes
		createtagPage.zPressButton(Button.B_YES);
			
		// To check whether deleted tag is not exist
		app.zGetActiveAccount().soapSend("<GetTagRequest xmlns='urn:zimbraMail'/>");

		String tagname = app.zGetActiveAccount().soapSelectValue("//mail:GetTagResponse//mail:tag[@name='" + tag.getName() + "']","name");
		ZAssert.assertNull(tagname, "Verify the tag is deleted");
		
		
	}
	
	/**
	 * Test case :Cancel delete tag operation
	 * Steps :
	 * 1. Create a tag item using SOAP.
	 * 2. Cancel delete tag operation from GUI. 
	 * 3. Verify tag is exist using SOAP & GUI.
	 * @throws HarnessException
	 */
	@Test (description = "Cancel delete tag operation",
			groups = { "functional" })
	
	public void DeleteTag_02() throws HarnessException {
		//----------------------- Data ------------------------------
		// Create the tag to delete
		TagItem tag = TagItem.CreateUsingSoap(app.zGetActiveAccount());
		ZAssert.assertNotNull(tag, "Verify the tag was created");
		String tagName = tag.getName();
		
		
		// Refresh to get the tag into the client
		app.zPageMail.zRefresh();		
		SleepUtil.sleepSmall();
		
		//Select tag
		PageCreateTag createtagPage = (PageCreateTag) app.zTreeMail.zPressButton(Button.B_EDIT);
		createtagPage.zSelectTag(tagName);
		
		// Click on delete button
		createtagPage.zPressButton(Button.B_DELETE);
	
		//Click on Yes
		createtagPage.zPressButton(Button.B_NO);
			
		// To check whether tag is exist
		// Make sure the tag is existed on the ZCS server
		TagItem newTag = TagItem.importFromSOAP(app.zGetActiveAccount(),tagName);
		ZAssert.assertNotNull(newTag, "Verify the new tag was created");
		ZAssert.assertEquals(newTag.getName(), tagName,"Verify the server and client tag names match");
				
		// UI verification
		ZAssert.assertTrue(createtagPage.zVerifyTagExists(tagName), "Verify created tag visible in tag pane");
		
		
	}
}

