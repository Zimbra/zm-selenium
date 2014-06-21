/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.spellcheck;


import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;

public class SpellCheckHtml extends PrefGroupMailByMessageTest {

	public SpellCheckHtml() {
		logger.info("New "+ SpellCheckHtml.class.getCanonicalName());
		
		
		
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
		
	}
	
	@Test(	description = "Spell Check an HTML message",
			groups = { "functional" })
	public void SpellCheckHtml_01() throws HarnessException {
		
		String subject = "subject" + ZimbraSeleniumProperties.getUniqueString();
		
		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");
		
		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.AccountA().EmailAddress);
		mailform.zFillField(Field.Subject, subject);
		//mailform.zFillField(Field.Body, "write mispeled words here");
		
		/* TODO: ... debugging to be removed */
		String bodyLocator = "css=body[id=tinymce]";
		boolean present = mailform.zWaitForElementPresent(bodyLocator, "30000");
		ZAssert.assertTrue(present,"Verify the body field is available");
		mailform.sClickAt(bodyLocator,"");
		mailform.zTypeFormattedText("css=iframe[id*=ifr]", "write mispeled words here");

		// Send the message
		mailform.zToolbarPressButton(Button.B_SPELL_CHECK);
			
		// Verify the misspelled word is highlighted
		ZAssert.assertTrue(
				mailform.sIsElementPresent("css=span[class='ZM-SPELLCHECK-MISSPELLED']:contains(mispeled)"),
				"Verify the misspelled word is highlighted");

		// Verify the misspelled word is highlighted
		ZAssert.assertFalse(
				mailform.sIsElementPresent("css=span[class='ZM-SPELLCHECK-MISSPELLED']:contains(words)"),
				"Verify the correctly spelled words are not highlighted");

		mailform.zToolbarPressButton(Button.B_SEND);
		
	}

}
