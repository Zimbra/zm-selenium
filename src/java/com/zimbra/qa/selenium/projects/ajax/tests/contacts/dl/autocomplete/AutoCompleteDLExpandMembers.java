/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.contacts.dl.autocomplete;

import java.util.List;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.ExecuteHarnessMain;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.AutocompleteEntry;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;

public class AutoCompleteDLExpandMembers extends AjaxCore  {

	public AutoCompleteDLExpandMembers() {
		logger.info("New "+ AutoCompleteDLExpandMembers.class.getCanonicalName());
		super.startingPage = app.zPageMail;
	}


	@Test (description = "Auto complete DL, expand members and send mail to all",
			groups = { "sanity" })

	public void AutoCompleteDLExpandMembers_01() throws HarnessException {
		String subject = "subject" + ConfigProperties.getUniqueString();
		String body = "body" + ConfigProperties.getUniqueString();

		// Compose mail
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		mailform.zFillField(FormMailNew.Field.Subject, subject);
		mailform.zFillField(FormMailNew.Field.Body, body);

		// Auto complete DL
		List<AutocompleteEntry> entries = mailform.zAutocompleteFillField(FormMailNew.Field.To, ExecuteHarnessMain.distributionlists.get("distributionlist1")[0]);
		AutocompleteEntry found = null;
		for (AutocompleteEntry entry : entries) {
			if ( entry.getAddress().contains(ExecuteHarnessMain.distributionlists.get("distributionlist1")[0]) ) {
				found = entry;
				break;
			}
		}
		ZAssert.assertNotNull(found, "Verify the autocomplete entry exists in the returned list");

		// Expand members
		app.zPageContacts.sClick("css=div[id='zac__COMPOSE-1_acExpandText_0']");
		SleepUtil.sleepMedium();
		app.zPageContacts.sClick("css=div[id='ZmAutocompleteListView_1'] td:contains(Select all 3 addresses)");

		// Send the message
		mailform.zSubmit();
	}
}