/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.drafts;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.TagItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogTag;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;

public class DraftModeToolbar extends PrefGroupMailByMessageTest {

	public DraftModeToolbar() {

		logger.info("New " + DraftModeToolbar.class.getCanonicalName());
	}

	@Bugs(ids = "83164")
	@Test(description = "Verify disabled toolbar button in Draft mode when opened from Tag Selection", groups = { "functional1" })
	public void DraftModeToolbar_01() throws HarnessException {

		// Create the message data to be entered while composing mail
		String subject = "subject" + ZimbraSeleniumProperties.getUniqueString();
		String body = "body" + ZimbraSeleniumProperties.getUniqueString();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.AccountA().EmailAddress);
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);

		// Save the message
		mailform.zToolbarPressButton(Button.B_SAVE_DRAFT);
		SleepUtil.sleepMedium();
		mailform.zToolbarPressButton(Button.B_CLOSE);		

		// Go to draft		
		FolderItem drafts = FolderItem.importFromSOAP(app.zGetActiveAccount(), FolderItem.SystemFolder.Drafts);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, drafts);
		
		// Select the mail 
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
				
		//Tag name
		String tagName = "tag" + ZimbraSeleniumProperties.getUniqueString();

		// Creat new tag
		DialogTag dialogTag = (DialogTag) app.zPageMail.zToolbarPressPulldown(Button.B_TAG, Button.O_TAG_NEWTAG);
		dialogTag.zSetTagName(tagName);
		dialogTag.zClickButton(Button.B_OK);
		
		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		
		TagItem tag = TagItem.importFromSOAP(app.zGetActiveAccount(), tagName);
		ZAssert.assertNotNull(tag, "Verify the tag was created");
		
		//Select the tag
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, tag);		
		
		
		// Select the mail 
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent("css=div[id$='_REPLY'][class*='ZDisabled']"),"Verify Reply button is disabled in Draft mode");

		ZAssert.assertTrue(app.zPageMail.sIsElementPresent("css=div[id$='_REPLY_ALL'][class*='ZDisabled']"),"Verify Reply All button is disabled in Draft mode");
		
		ZAssert.assertTrue(app.zPageMail.sIsElementPresent("css=div[id$='_FORWARD'][class*='ZDisabled']"),"Verify Forward button is disabled in Draft mode");
		
	}

}