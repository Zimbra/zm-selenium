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
package com.zimbra.qa.selenium.projects.ajax.tests.mail.compose.spellcheck;


import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Locators;

public class SpellCheckAddIgnore extends PrefGroupMailByMessageTest {

	public SpellCheckAddIgnore() {
		logger.info("New "+ SpellCheck.class.getCanonicalName());		
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
	}
	
	@Bugs(ids = "47151")
	@Test( description = "Spell Check a misspelled word and add it to spell-check dictionary.",
			groups = { "functional" })
	
	public void SpellCheckAddIgnore_01() throws HarnessException {

		String subject = "subject" + ConfigProperties.getUniqueString();
		String misspelledWord="addword";

		// Open the new mail form
		FormMailNew mailform = null;		
		mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.AccountA().EmailAddress);
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, misspelledWord);

		//Spell check the message
		mailform.zToolbarPressButton(Button.B_SPELL_CHECK);			

		//CLick on the misspelled word
		mailform.sClickAt(Locators.zMisspelledWordCss+":contains('"+misspelledWord+"')","");

		//Click on Add  to add the word to spell check dictionary
		SleepUtil.sleepSmall();
		mailform.sClickAt(Locators.zAddMisspelledWord,"");
		
		//To go out of Spell check view
		mailform.zToolbarPressButton(Button.B_SPELL_CHECK);				

		//Retyping the same word to check if the word has been added to the dictionary
		mailform.zFillField(Field.Body, " "+misspelledWord);
		mailform.zToolbarPressButton(Button.B_SPELL_CHECK);		

		//Check that the word is not highlighted now
		ZAssert.assertFalse(
				mailform.sIsElementPresent(Locators.zMisspelledWordCss+":contains('"+misspelledWord+"')"),
				"The misspelled word is still getting highlighted.");
		
		//Send the mail
		mailform.zToolbarPressButton(Button.B_SEND);
	
	}	
	
	
	@Bugs(ids = "47151")
	@Test( description = "Spell Check a misspelled word and ignore it.",
			groups = { "functional" })
	
	public void SpellCheckAddIgnore_02() throws HarnessException {

		String subject = "subject" + ConfigProperties.getUniqueString();
		String misspelledWord="IgnoreWord";

		// Open the new mail form
		FormMailNew mailform = null;		
		mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.To, ZimbraAccount.AccountA().EmailAddress);
		mailform.zFillField(Field.Subject, subject);
		mailform.zFillField(Field.Body, misspelledWord);

		//Spell check the message
		mailform.zToolbarPressButton(Button.B_SPELL_CHECK);
		
		//CLick on the misspelled word
		mailform.sClickAt(Locators.zMisspelledWordCss+":contains('"+misspelledWord+"')","0,0");

		//Click on Add  to add the word to spell check dictionary
		SleepUtil.sleepSmall();
		mailform.sClickAt(Locators.zIgnoreMisspelledWord,"0,0");
		
		//To go out of Spell check view
		mailform.zToolbarPressButton(Button.B_SPELL_CHECK);				

		//Retyping the same word to check if the word has been added to the dictionary
		mailform.zFillField(Field.Body, " "+misspelledWord);
		mailform.zToolbarPressButton(Button.B_SPELL_CHECK);
		
		//Check that the word is not highlighted now
		ZAssert.assertFalse(
				mailform.sIsElementPresent(Locators.zMisspelledWordCss+":contains('"+misspelledWord+"')"),
				"Verify the misspelled word is not highlighted now");
		
		//Send the mail
		mailform.zToolbarPressButton(Button.B_SEND);
	}

}
