/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.admin.tests.searchmail;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.admin.core.AdminCommonTest;
import com.zimbra.qa.selenium.projects.admin.ui.FormSearchMail;
import com.zimbra.qa.selenium.projects.admin.ui.PageManageSearchMail;

public class SearchMailInSelectedAccounts extends AdminCommonTest {
	
	public SearchMailInSelectedAccounts() {
		logger.info("New "+ SearchMailInSelectedAccounts.class.getCanonicalName());

		// All tests start at the "Accounts" page
		super.startingPage = app.zPageManageSearchMail;
	}
	
	/**
	 * Testcase : Create new mail search
	 * Steps :
	 * 1. Go to Home > Tools and Migraton > Search Mail
	 * 2. Enter details and click on run search
	 * 3. Verify search is created successfully
	 * @throws HarnessException
	 */
	
	@Test( description = "Navigate to Search Mail",
			groups = { "smoke", "L1","network" })
			public void SearchMailInSelectedAccounts_01() throws HarnessException {
		
		String hostname = ConfigProperties.getStringProperty("server.host");
		String criteria ="selected";
		
		// Send a message to the account
		String subject = "crossmailboxsearch"+ ConfigProperties.getUniqueString();
		ZimbraAccount.AccountA().soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ ZimbraAccount.AccountB().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");
		String emailAddress =ZimbraAccount.AccountB().EmailAddress;
		String targetMailbox =ZimbraAccount.AccountC().EmailAddress;
		
		// Navigate to Home > Tools and Migraton > Search Mail
		app.zPageManageSearchMail.zClickAt(PageManageSearchMail.Locators.TOOLS_AND_MIGRATION_ICON, "");
		SleepUtil.sleepMedium();

		// Click on search mail
		app.zPageManageSearchMail.sClickAt(PageManageSearchMail.Locators.SEARCHMAIL, "");
		
		// Click on New
		FormSearchMail form =(FormSearchMail)app.zPageManageSearchMail.zToolbarPressPulldown(Button.B_GEAR_BOX, Button.O_NEW);
		
		// Enter search query
		form.setSearchQuery(subject);
		
		// Select host
		form.SelectServerName(hostname);
		
		// Enter target mailbox
		form.setTargetMailbox(targetMailbox);
		
		// Select option 'Select accounts to search'
		form.selectAccountsToSearch(criteria);
		
		// Search in selected account 
		form.searchInSelectedAccount(emailAddress);
		
		// Click on run search 
		form.zSubmit();	
		
		// Verify search is created successfully
		ZAssert.assertTrue(app.zPageManageSearchMail.zVerifySearchResult(targetMailbox), "Verfiy email address is returned in search result");
	
	}
}
