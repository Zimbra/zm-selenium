/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;

public class SpellCheck extends PrefGroupMailByMessageTest {

	public SpellCheck() {
		logger.info("New "+ SpellCheck.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "text");
	}

	
	@Test(description = "Spell check a single word", groups = { "functional", "L2" })
	
	public void SpellCheck_01() throws HarnessException {

		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.Body, "write mispeled words here");

		// Spell check
		mailform.zToolbarPressButton(Button.B_SPELL_CHECK);

		// Verify the misspelled word is highlighted
		ZAssert.assertTrue(
				mailform.sIsElementPresent("css=span[class='ZM-SPELLCHECK-MISSPELLED']:contains(mispeled)"),
				"Verify the misspelled word is highlighted");

		// View the suggestions
		mailform.sClick("css=span[class='ZM-SPELLCHECK-MISSPELLED']:contains(mispeled)");
		mailform.zWaitForBusyOverlay();
		
		// Look at the list to make sure the correctly spelled word is there
		ZAssert.assertTrue(mailform.sIsElementPresent("css=tr[id*='_sug-'] td[id$='_title']:contains(misspelled)"),
				"Verify the misspelled word is highlighted");

	}

	
	@Test(description = "Spell check multiple words", groups = { "functional", "L2" })
	
	public void SpellCheck_02() throws HarnessException {
		
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		// Fill out the form with the data
		mailform.zFillField(Field.Body, "write mispeled incorect words here");

		// Spell check
		mailform.zToolbarPressButton(Button.B_SPELL_CHECK);

		// Verify the misspelled word is highlighted
		ZAssert.assertTrue(
				mailform.sIsElementPresent("css=span[class='ZM-SPELLCHECK-MISSPELLED']:contains('mispeled')"),
				"Verify the misspelled word is highlighted");
		ZAssert.assertTrue(
				mailform.sIsElementPresent("css=span[class='ZM-SPELLCHECK-MISSPELLED']:contains('incorect')"),
				"Verify the misspelled word is highlighted");

		// View the suggestions
		mailform.sClick("css=span[class='ZM-SPELLCHECK-MISSPELLED']:contains(mispeled)");
		mailform.zWaitForBusyOverlay();
		ZAssert.assertTrue(mailform.sIsElementPresent("css=tr[id*='_sug-'] td[id$='_title']:contains('misspelled')"),
				"Verify the misspelled word is highlighted");

		mailform.sClick("css=span[class='ZM-SPELLCHECK-MISSPELLED']:contains(incorect)");
		mailform.zWaitForBusyOverlay();
		ZAssert.assertTrue(mailform.sIsElementPresent("css=tr[id*='_sug-'] td[id$='_title']:contains('incorrect')"),
				"Verify the misspelled word is highlighted");

	}
}