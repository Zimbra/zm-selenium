/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.delegates;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;


public class SendOnBehalfOf extends PrefGroupMailByMessageTest {


	public SendOnBehalfOf() {
		logger.info("New "+ SendOnBehalfOf.class.getCanonicalName());
		
		
		

		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
		
	}
	
	@Test(	description = "Send On Behalf Of another user",
			groups = { "smoke" })
	public void SendOnBehalfOf_01() throws HarnessException {
		
		//-- Data Setup
		
		// Mail data
		String subject = "subject"+ ZimbraSeleniumProperties.getUniqueString();
		
		// The grantor
		ZimbraAccount grantor = null;
		grantor = new ZimbraAccount();
		grantor.provision();
		grantor.authenticate();

		grantor.soapSend(
					"<GrantRightsRequest xmlns='urn:zimbraAccount'>"
				+		"<ace gt='usr' d='"+ app.zGetActiveAccount().EmailAddress +"' right='sendOnBehalfOf'/>"
				+	"</GrantRightsRequest>");

		// Login to load the rights
		app.zPageLogin.zNavigateTo();
		this.startingPage.zNavigateTo();
		
		
		//-- GUI Steps
		
		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");
		
		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.AccountA().EmailAddress);
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, "body" + ZimbraSeleniumProperties.getUniqueString());
		mailform.zFillField(Field.From, grantor.EmailAddress);	
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
		ZAssert.assertEquals(from, grantor.EmailAddress, "Verify From: grantor");
		
		// Verify Sender: active account
		String sender = ZimbraAccount.AccountA().soapSelectValue("//mail:e[@t='s']", "a");
		ZAssert.assertEquals(sender, app.zGetActiveAccount().EmailAddress, "Verify Sender: active account");

	}

	
}
