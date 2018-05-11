/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.trustedaddresses;

import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;

public class TrustedDomainMessageView extends AjaxCore {

	@SuppressWarnings("serial")
	public TrustedDomainMessageView() throws HarnessException {
		super.startingPage = app.zPageMail;
		super.startingAccountPreferences = new HashMap<String, String>() {{
			put("zimbraPrefGroupMailBy", "message");
			put("zimbraPrefMessageViewHtmlPreferred", "TRUE");
			put("zimbraPrefMailTrustedSenderList", "testdomain.com");
		}};
	}


	/**
	 * TestCase : Trusted Domain with message view
	 * 1.Set domain in Preference/Mail/Trusted Addresses
	 * Verify it through soap(GetPrefsRequest)
	 * 2.In message View Inject mail with external image
	 * 3.Verify To,From,Subject through soap
	 * 4.Click on same mail
	 * 5.Yellow color Warning message Info bar should not present for trusted domain
	 */

	@Test (description = "Verify Display Image link in Trusted doamin for message view",
			groups = { "smoke", "L1" })

	public void TrustedDomainMsgView_01() throws HarnessException {

		final String subject = "TestTrustedAddress";
		final String from = ConfigProperties.getStringProperty("adminUser") + "@" + ConfigProperties.getStringProperty("testdomain");
		final String to = ConfigProperties.getStringProperty("adminUser") + "@" + ConfigProperties.getStringProperty("testdomain");
		final String mimeFile = ConfigProperties.getBaseDirectory() + "/data/public/mime/ExternalImg.txt";

		// Verify domain through soap- GetPrefsRequest
		String PrefMailTrustedAddr = ZimbraAccount.AccountZCS().getPreference("zimbraPrefMailTrustedSenderList");
		ZAssert.assertTrue(PrefMailTrustedAddr.equals("testdomain.com"), "Verify doamin is present /Pref/TrustedAddr");

		// Inject the external image message(s)
		injectMessage(app.zGetActiveAccount(), mimeFile);

		MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(),subject);

		ZAssert.assertNotNull(mail, "Verify message is received");
		ZAssert.assertEquals(from, mail.dFromRecipient.dEmailAddress,"Verify the from matches");
		ZAssert.assertEquals(to, mail.dToRecipients.get(0).dEmailAddress,"Verify the to address");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_REFRESH);

		// Select the message so that it shows in the reading pane
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Verify Warning info bar with other links
		ZAssert.assertFalse(app.zPageMail.zHasWDDLinks(), "Verify Warning icon, Display Image and Domain link does not present");
	}
}