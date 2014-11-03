/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.touch.tests.contacts.contacts;
import org.testng.annotations.Test;import com.zimbra.qa.selenium.framework.ui.Button;import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.ui.contacts.FormContactNew;
import com.zimbra.qa.selenium.projects.touch.ui.contacts.FormContactNew.Field;
import com.zimbra.qa.selenium.projects.touch.core.TouchCommonTest;
public class CancelCreateContact extends TouchCommonTest  {
	public CancelCreateContact() {
		logger.info("New "+ CancelCreateContact.class.getCanonicalName());
		super.startingPage = app.zPageAddressbook;
	}	
	@Test(description = "Fill up creat contact form with basic attributes and cancel it",
			groups = { "smoke" })
	public void CancelCreateContact_01() throws HarnessException {
		//-- DATA
		// generate basic attribute values for new account
		String contactFirst = "First" + ZimbraSeleniumProperties.getUniqueString();
		String contactLast = "Last"+ ZimbraSeleniumProperties.getUniqueString();
		String contactCompany = "Company"+ ZimbraSeleniumProperties.getUniqueString();
		//-- GUI Action
		// Click +(Add) button
		FormContactNew formContactNew = (FormContactNew)app.zPageAddressbook.zToolbarPressButton(Button.B_NEW);
        // Fill in the form
		formContactNew.zFillField(Field.FirstName, contactFirst);
		formContactNew.zFillField(Field.LastName, contactLast);
		formContactNew.zFillField(Field.Company, contactCompany);
		// Click cancel button and confirm to discard the form data
		formContactNew.zToolbarPressButton(Button.B_CANCEL);
		//dw.zClickButton(Button.B_NO);
		//-- Data Verification		// Search the data stored in Zimbra server
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='contact'>"
			+		"<query>#firstname:"+ contactFirst +"</query>"
			+	"</SearchRequest>");
	    String contactId = app.zGetActiveAccount().soapSelectValue("//mail:cn", "id");
	    // Make sure if the data is not fetched
	    ZAssert.assertNull(contactId, "Verify the contact is not returned in the search");
	}
}
