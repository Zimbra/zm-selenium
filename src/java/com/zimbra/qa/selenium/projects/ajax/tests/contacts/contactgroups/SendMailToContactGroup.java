/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.contactgroups;


import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.*;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;

public class SendMailToContactGroup extends AjaxCommonTest  {
	public SendMailToContactGroup() {
		logger.info("New "+ SendMailToContactGroup.class.getCanonicalName());
		
		// All tests start at the Address page
		super.startingPage = app.zPageContacts;

		super.startingAccountPreferences = null;		
		
	}
	
	@Bugs(ids="97157")
	@Test(	description = "Right click then click New Email",
			groups = { "smoke" })
	public void NewEmail() throws HarnessException {

		//--  Data
		
		// The message subject
		String subject = "subject"+ ZimbraSeleniumProperties.getUniqueString();
		
		// Create a contact group
		String groupName = "group" + ZimbraSeleniumProperties.getUniqueString();
		app.zGetActiveAccount().soapSend(
				"<CreateContactRequest xmlns='urn:zimbraMail'>" +
					"<cn >" +
						"<a n='type'>group</a>" +
						"<a n='nickname'>" + groupName +"</a>" +
						"<a n='fileAs'>8:" +  groupName +"</a>" +
				        "<m type='I' value='" + ZimbraAccount.AccountA().EmailAddress + "' />" +
				        "<m type='I' value='" + ZimbraAccount.AccountB().EmailAddress + "' />" +
					"</cn>" +
				"</CreateContactRequest>");
		
		
		
		//-- GUI
		
		// Refresh
		app.zPageContacts.zRefresh();
		
		// Right Click -> New Email
        FormMailNew formMailNew = (FormMailNew) app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_NEW, groupName);        

        formMailNew.zFillField(Field.Subject, subject);
        formMailNew.zFillField(Field.Body, "body"+ ZimbraSeleniumProperties.getUniqueString());
        formMailNew.zSubmit();
        
        
        //-- Verification
        
        MailItem message1 = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ subject +")");
        ZAssert.assertNotNull(message1, "Verify the message is received by Account Bugs:97157");

        MailItem message2 = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ subject +")");
        ZAssert.assertNotNull(message2, "Verify the message is received by Account B");
        

	}
	

	

}

