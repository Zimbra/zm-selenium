/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.mail;

import java.io.*;
import org.testng.annotations.*;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.*;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.*;

public class SendMailWithSpecialCharacter extends PrefGroupMailByMessageTest {

	public SendMailWithSpecialCharacter() {
		logger.info("New " + SendMailWithSpecialCharacter.class.getCanonicalName());
	}

	@Bugs( ids = "82073")
	@Test( description = "Send a mail with the '&' character in the subject - verify no '&amp;'", groups = { "functional" } )
	
	public void SendMailWithSpecialCharacter_01() throws HarnessException {

		//-- DATA
		
		String subject = ConfigProperties.getUniqueString() + " & " + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

		//-- GUI
		
		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");
		
		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.AccountA().EmailAddress);
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, body);
		
		// Send the message
		mailform.zSubmit();
		
		//-- VERIFIFICATION
		
		// Verify the retention policy on the folder
		MailItem received = MailItem.importFromSOAP(ZimbraAccount.AccountA(), "content:("+ body +")");
		ZAssert.assertStringDoesNotContain(received.getSubject(), "amp", "Verify the subject does not contain '&amp;'");
		
	}

	@Bugs( ids = "82073")
	@Test( description = "Receive a mail with the '&' character in the subject - verify no '&amp;'", groups = { "functional" } )
	
	public void ReceiveMailWithSpecialCharacter_02() throws HarnessException {

		//-- DATA
		
		String subject = "13680547408344 & 13680547408345";
		
		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/Bugs/Bug82073/mime1.txt";
		LmtpInject.injectFile(app.zGetActiveAccount().EmailAddress, new File(mimeFile));

		//-- GUI
		app.zPageMail.zVerifyMailExists(subject);

	}
}
