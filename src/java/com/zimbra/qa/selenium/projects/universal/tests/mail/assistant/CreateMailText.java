/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.mail.assistant;

import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Shortcut;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.universal.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.universal.ui.DialogAssistant;


public class CreateMailText extends PrefGroupMailByMessageTest {
	
	public CreateMailText() {
		logger.info("New "+ CreateMailText.class.getCanonicalName());
	}
	
	@Test( description = "Send a text mail using the Zimbra Assistant",
			groups = { "deprecated", "L4" })
	public void CreateMailText_01() throws HarnessException {
		
		// Create the message data to be sent
		String subject = "subject" + ConfigProperties.getUniqueString();
		String command = "mail \"" + subject + "\" to: "+ ZimbraAccount.AccountA().EmailAddress + " body: foo";

		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		DialogAssistant assistant = (DialogAssistant)app.zPageMail.zKeyboardShortcut(Shortcut.S_ASSISTANT);
		assistant.zEnterCommand(command);
		assistant.zClickButton(Button.B_OK);
		
		ZimbraAccount.AccountA().soapSend(
					"<SearchRequest xmlns='urn:zimbraMail' types='message'>"
				+		"<query>subject:("+ subject +")</query>"
				+	"</SearchRequest>");
		Element[] nodes = ZimbraAccount.AccountA().soapSelectNodes("//mail:m");
		ZAssert.assertGreaterThan(nodes.length, 0, "Verify the message was received");

	}


}
