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
package com.zimbra.qa.selenium.projects.universal.tests.mail.gui;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.PageMail.Column;

public class EditColumnView extends UniversalCommonTest {

	public EditColumnView() {
		logger.info("New "+ EditColumnView.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefReadingPaneLocation", "bottom");
		super.startingAccountPreferences.put("zimbraPrefGroupMailBy", "message");
	}
	
	@Bugs (ids = "44785")
	@Test (description = "Edit the columns to display in message view and verify that change is preserved in new session", 
			groups = { "functional", "L2"})

	public void EditColumnView_01() throws HarnessException {
		
		String subject = "subject "+ConfigProperties.getUniqueString();
		ZimbraAccount account = app.zGetActiveAccount();
		
		// Send a message to the account
		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
						"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");
		
		// Refresh current view
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		
		//Uncheck the Size filed to remove Size column
		app.zPageMail.zEditColumnView(Action.A_UNCHECKBOX, Column.Size);
		
		//Check the presence of size column in mail list view
		boolean isPresent = app.zPageMail.zVerifyColumnPresent(Column.Size);
		ZAssert.assertFalse(isPresent, "The " + Column.Size.name() + " column is still present after deselecting it!" );
		
		// Refresh current view
		//app.zPageMail.zToolbarPressButton(Button.B_REFRESH);
		
		//Uncheck the Flag filed to remove Flag column
		app.zPageMail.zEditColumnView(Action.A_UNCHECKBOX, Column.Flag);

		//Check the presence of Flag column in mail list view
		isPresent = app.zPageMail.zVerifyColumnPresent(Column.Flag);
		ZAssert.assertFalse(isPresent, "The "  + Column.Flag.name() + " column is still present after deselecting it!" );

		// Logout
		app.zPageMain.zLogout();
		
		//Login again to check the changes done are preserved in the new session
		app.zPageLogin.zLogin(account);

		isPresent = app.zPageMail.zVerifyColumnPresent(Column.Size);
		ZAssert.assertFalse(isPresent, "The " + Column.Size.name() + " column is displayed in new session!" );
		
		isPresent = app.zPageMail.zVerifyColumnPresent(Column.Flag);
		ZAssert.assertFalse(isPresent, "The " + Column.Flag.name() + " column is displayed in new session!" );
	}
	
	@Test (description = "Edit the column to display in conversation view and verify that change is preserved in new session", 
			groups = { "functional", "L3"})

	public void EditColumnView_02() throws HarnessException {
		
		String subject = "subject "+ConfigProperties.getUniqueString();
		ZimbraAccount account = app.zGetActiveAccount();
		
		// Send a message to the account
		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
						"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");
		
		//Switch to conversation view
		app.zPageMail.zToolbarPressButton(Button.B_MAIL_VIEW_BY_CONVERSATION);
		
		//check the Size filed to add Size column back in the view
		app.zPageMail.zEditColumnView(Action.A_UNCHECKBOX, Column.Size);
		boolean isPresent = app.zPageMail.zVerifyColumnPresent(Column.Size);
		ZAssert.assertFalse(isPresent, "The " + Column.Size.name() + " column is still present after deselecting it!" );
		
		//Check the presence of Flag column in mail list view.
		app.zPageMail.zEditColumnView(Action.A_CHECKBOX, Column.Folder);
		isPresent = app.zPageMail.zVerifyColumnPresent(Column.Folder);
		ZAssert.assertTrue(isPresent, "The " + Column.Folder.name() + " column is still not present after selecting it!" );

		// Logout
		app.zPageMain.zLogout();
		
		//Login again to check the changes done are preserved in the new session
		app.zPageLogin.zLogin(account);

		isPresent = app.zPageMail.zVerifyColumnPresent(Column.Size);
		ZAssert.assertFalse(isPresent, "The " + Column.Size.name() + " column is displayed in new session!" );
		
		isPresent = app.zPageMail.zVerifyColumnPresent(Column.Folder);
		ZAssert.assertTrue(isPresent, "The " + Column.Folder.name() + " column is not displayed in new session!" );
	}
}
