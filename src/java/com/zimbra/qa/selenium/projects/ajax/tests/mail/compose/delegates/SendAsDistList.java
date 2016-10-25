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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.delegates;

import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;

public class SendAsDistList extends PrefGroupMailByMessageTest {

	public SendAsDistList() {
		logger.info("New "+ SendAsDistList.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
	}
	
	@Test( description = "Send As Distribution List",
			groups = { "smoke" })
	
	public void SendAsDistList_01() throws HarnessException {
		
		// Mail data
		String subject = "subject"+ ConfigProperties.getUniqueString();
		
		// The grantor
		ZimbraDistributionList list = new ZimbraDistributionList();
		list.provision();

		// Add a member
		list.addMember(ZimbraAccount.AccountA());
		
		// Grant send rights
		list.grantRight(app.zGetActiveAccount(), "sendAsDistList");

		// Refresh UI
		app.zPageMain.sRefresh();
	
		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");
		
		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.AccountA().EmailAddress);
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, "body" + ConfigProperties.getUniqueString());
		mailform.zFillField(Field.From, list.EmailAddress);	
		mailform.zSubmit();
		
		//-- Data verification
		
		ZimbraAccount.AccountA().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
			+		"<query>subject:("+ subject +")</query>"
			+	"</SearchRequest>");
		String id = ZimbraAccount.AccountA().soapSelectValue("//mail:m", "id");

		ZimbraAccount.AccountA().soapSend(
				"<GetMsgRequest xmlns='urn:zimbraMail' >"
			+		"<m id='"+ id +"'/>"
			+	"</GetMsgRequest>");

		// Verify From: grantor
		String from = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='f']", "a");
		ZAssert.assertEquals(from, list.EmailAddress, "Verify From: grantor");
		
		// Verify no headers contain active account
		Element[] nodes = ZimbraAccount.AccountA().soapSelectNodes("//mail:e");
		for (Element e : nodes) {
			String attr = e.getAttribute("a", null);
			if ( attr != null ) {
				ZAssert.assertStringDoesNotContain(
						attr, 
						app.zGetActiveAccount().EmailAddress, 
						"Verify no headers contain the active account email address");
			}
		}
	}

	
}
