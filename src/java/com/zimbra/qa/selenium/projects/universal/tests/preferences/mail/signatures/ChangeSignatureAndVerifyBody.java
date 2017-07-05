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

package com.zimbra.qa.selenium.projects.universal.tests.preferences.mail.signatures;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.SignatureItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.universal.ui.mail.FormMailNew.Field;

public class ChangeSignatureAndVerifyBody extends AjaxCommonTest {

	private String AllowEmailAddress = null;
	private String AllowFromDisplay = null;
	private String sigName1 = null;
	private String sigBody1 = null;
	private String sigName2 = null;
	private String sigBody2 = null;

	public ChangeSignatureAndVerifyBody() {
		logger.info("New "+ ChangeSignatureAndVerifyBody.class.getCanonicalName());
		super.startingAccountPreferences.put("zimbraPrefComposeFormat", "html");
	}

	@Bugs(ids="47375")
	@Test(description = "Change signature by deleting the previous signature and verify the body of the mail",
			groups = { "functional", "L3" })

	public void ChangeSignatureAndVerifyBody_01() throws HarnessException {

		sigName1 = "signame1";
		sigBody1 = "sigbody1 " + "signature1 <b>bold" + ConfigProperties.getUniqueString() + "</b> signature";
		sigName2 = "signame2";
		sigBody2 = "sigbody2 " + "signature2 <b>bold" + ConfigProperties.getUniqueString() + "</b> signature";

		// Create two signatures
		app.zGetActiveAccount().soapSend(
				"<CreateSignatureRequest xmlns='urn:zimbraAccount'>"
						+ "<signature name='" + sigName1 + "' >"
						+ "<content type='text/html'>" + sigBody1
						+ "</content>" + "</signature>"
						+ "</CreateSignatureRequest>");

		// Create two signatures
		app.zGetActiveAccount().soapSend(
				"<CreateSignatureRequest xmlns='urn:zimbraAccount'>"
						+ "<signature name='" + sigName2 + "' >"
						+ "<content type='text/html'>" + sigBody2
						+ "</content>" + "</signature>"
						+ "</CreateSignatureRequest>");

		// Refresh UI
		app.zPageMain.sRefresh();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		String bodyText = "body " + ConfigProperties.getUniqueString();
		mailform.zFillField(Field.Body,bodyText);

		// Select the signature below the body text
		mailform.zKeyboard.zTypeCharacters("<SHIFT><DOWN>");
		mailform.zKeyboard.zTypeCharacters("<SHIFT><DOWN>");
		mailform.zKeyboard.zTypeCharacters("<SHIFT><DOWN>");

		// Delete the signature
		mailform.zKeyboard.zTypeCharacters("<Delete>");

		// Press Enter to go to next line before adding other signature
		mailform.zKeyboard.zTypeCharacters("<ENTER>");

		// Select another signature from Options
		app.zPageMail.zToolbarPressPulldown(Button.B_OPTIONS,Button.O_ADD_SIGNATURE,sigName2);

		// Verify that entered body text is still present after changing the signature
		ZAssert.assertTrue(mailform.zGetFieldValue(Field.Body).contains(bodyText), "Entered body text is lost after changing the signature!");
		ZAssert.assertTrue(mailform.zGetFieldValue(Field.Body).contains(sigBody2.replace("<b>", "").replace("</b>", "")), "Verify added signature");

	}
	

	@Bugs(ids="47375")
	@Test(description = "Verify the body of the mail after deleting the signature and changing the From Persona which has a different signature",
			groups = { "functional", "L3" })

	public void ChangeSignatureAndVerifyBody_02() throws HarnessException {

		SignatureItem signature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), sigName2);

		// Create a From persona with different signature
		AllowFromDisplay = "allowed" + ConfigProperties.getUniqueString();
		AllowEmailAddress = AllowFromDisplay + "@" + ConfigProperties.getStringProperty("testdomain");

		String identity = "identity" + ConfigProperties.getUniqueString();

		ZimbraAdminAccount.GlobalAdmin().soapSend(
					"<ModifyAccountRequest xmlns='urn:zimbraAdmin'>"
				+		"<id>"+ app.zGetActiveAccount().ZimbraId +"</id>"
				+		"<a n='zimbraAllowFromAddress'>"+ AllowEmailAddress +"</a>"
				+	"</ModifyAccountRequest>");

		app.zGetActiveAccount().soapSend(
				" <CreateIdentityRequest xmlns='urn:zimbraAccount'>"
			+		"<identity name='"+ identity +"'>"
			+			"<a name='zimbraPrefIdentityName'>"+ identity +"</a>"
			+			"<a name='zimbraPrefFromDisplay'>"+ AllowFromDisplay +"</a>"
			+			"<a name='zimbraPrefFromAddress'>"+ AllowEmailAddress +"</a>"
			+			"<a name='zimbraPrefReplyToEnabled'>FALSE</a>"
			+			"<a name='zimbraPrefReplyToDisplay'/>"
			+			"<a name='zimbraPrefDefaultSignatureId'>" + signature.getId() +"</a>"
			+			"<a name='zimbraPrefForwardReplySignatureId'/>"
			+			"<a name='zimbraPrefWhenSentToEnabled'>FALSE</a>"
			+			"<a name='zimbraPrefWhenInFoldersEnabled'>FALSE</a>"
			+		"</identity>"
			+	"</CreateIdentityRequest>");


		// Refresh UI
		app.zPageMain.sRefresh();

		// Open the new mail form
		FormMailNew mailform = (FormMailNew) app.zPageMail.zToolbarPressButton(Button.B_NEW);
		ZAssert.assertNotNull(mailform, "Verify the new form opened");

		String bodyText = "body " + ConfigProperties.getUniqueString();
		mailform.zFillField(Field.Body,"");

		// Select the signature below the body text
		mailform.zKeyboard.zTypeCharacters("<SHIFT><DOWN>");
		mailform.zKeyboard.zTypeCharacters("<SHIFT><DOWN>");
		mailform.zKeyboard.zTypeCharacters("<SHIFT><DOWN>");

		// Delete the signature
		mailform.zKeyboard.zTypeCharacters("<Delete>");

		// Enter the text in body
		mailform.zFillField(Field.To, "");
		mailform.zFillField(Field.Subject,"");
		mailform.zFillField(Field.Body,bodyText);

		// Fill out the form with the data
		mailform.zFillField(Field.From, AllowEmailAddress);

		// Verify that entered body text is still present after changing the signature
		ZAssert.assertTrue(mailform.zGetFieldValue(Field.Body).contains(bodyText), "Entered body text is lost after changing the signature!");
		ZAssert.assertTrue(mailform.zGetFieldValue(Field.Body).contains(sigBody2.replace("<b>", "").replace("</b>", "")), "Verify added signature");
	}
}