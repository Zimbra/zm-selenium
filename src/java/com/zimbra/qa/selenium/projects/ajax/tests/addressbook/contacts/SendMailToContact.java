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
package com.zimbra.qa.selenium.projects.ajax.tests.addressbook.contacts;


import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.*;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;

public class SendMailToContact extends AjaxCommonTest  {
	public SendMailToContact() {
		logger.info("New "+ SendMailToContact.class.getCanonicalName());
		
		// All tests start at the Address page
		super.startingPage = app.zPageAddressbook;

		super.startingAccountPreferences = null;		
		
	}
	

	@Test(	description = "Right click then click New Email",
			groups = { "smoke" })
	public void NewEmail() throws HarnessException {

		//--  Data
		
		// The message subject
		String subject = "subject"+ ZimbraSeleniumProperties.getUniqueString();
		
		// Create a contact
		String firstName = "First" + ZimbraSeleniumProperties.getUniqueString();
		String lastName = "Last" + ZimbraSeleniumProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
	                "<CreateContactRequest xmlns='urn:zimbraMail'>" +
	                		"<cn >" +
	                			"<a n='firstName'>" + firstName +"</a>" +
	                			"<a n='lastName'>" + lastName +"</a>" +
	                			"<a n='email'>" + ZimbraAccount.AccountA().EmailAddress + "</a>" +
                			"</cn>" +
	                "</CreateContactRequest>");
		
		
		
		//-- GUI
		
		// Refresh
		app.zPageAddressbook.zRefresh();
		
		// Right Click -> New Email
        FormMailNew formMailNew = (FormMailNew) app.zPageAddressbook.zListItem(Action.A_RIGHTCLICK, Button.B_NEW, firstName);        

        formMailNew.zFillField(Field.Subject, subject);
        formMailNew.zFillField(Field.Body, "body"+ ZimbraSeleniumProperties.getUniqueString());
        formMailNew.zSubmit();
        
        
        //-- Verification
        
        MailItem message1 = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "subject:("+ subject +")");
        ZAssert.assertNotNull(message1, "Verify the message is received by Account A");

        

	}
	

	

}

