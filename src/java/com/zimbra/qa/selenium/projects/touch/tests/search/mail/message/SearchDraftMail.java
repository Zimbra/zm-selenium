/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.touch.tests.search.mail.message;

import org.testng.annotations.Test;

import java.awt.AWTException;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.touch.core.PrefGroupMailByMessageTest;

public class SearchDraftMail extends PrefGroupMailByMessageTest {

	public SearchDraftMail() {
		logger.info("New "+ SearchDraftMail.class.getCanonicalName());

	}
	
	@Bugs(	ids = "93246")
	@Test( description = "Bug 93246: Draft message content showing blank from search results",
			groups = { "functional" })
			
	public void SearchDraftMail_01() throws HarnessException, AWTException {
		
		//-- DATA
				String subject = "subject"+ ZimbraSeleniumProperties.getUniqueString();
				String body = "body"+ ZimbraSeleniumProperties.getUniqueString();
				
				app.zGetActiveAccount().soapSend(
						"<SaveDraftRequest xmlns='urn:zimbraMail'>" +
							"<m >" +
								"<e t='t' a='"+ ZimbraAccount.AccountA().EmailAddress +"'/>" +
								"<su>"+ subject +"</su>" +
								"<mp ct='text/plain'>" +
									"<content>"+ body +"</content>" +
								"</mp>" +
							"</m>" +
						"</SaveDraftRequest>");
		
		// Get the message from server and verify draft data matches
		MailItem draft = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");
		FolderItem draftsFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Drafts);
		
		app.zPageMail.zRefresh();
		
		// Click on Drafts
		app.zPageMail.zToolbarPressButton(Button.B_FOLDER_TREE);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, "Drafts");
		
		// Search email		
		app.zTreeMail.zFillField(Button.B_SEARCH, subject);

		// Select mail
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		//Verify body contents
		ZAssert.assertEquals(draft.dBodyText, body, "Verify the body field is correct");
		ZAssert.assertEquals(draft.dFolderId, draftsFolder.getId(), "Verify the draft is saved in the drafts folder");
		
	}
	
}
