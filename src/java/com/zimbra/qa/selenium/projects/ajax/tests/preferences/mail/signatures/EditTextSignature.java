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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.mail.signatures;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.SignatureItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.signature.FormSignatureNew;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.signature.PageSignature;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.signature.FormSignatureNew.Field;

public class EditTextSignature extends AjaxCore {
	String sigName = "signame " + ConfigProperties.getUniqueString();
	String sigBody = "sigbody " + ConfigProperties.getUniqueString();

	public EditTextSignature() throws HarnessException {
		super.startingPage = app.zPagePreferences;
	}


	/**
	 * Added @BeforeMethod because after logged in, when we try to create
	 * signature through soap, it doesn't shows in (GUI) 'Pref/signatures'
	 * unless and until we refresh browser.
	 */

	@BeforeMethod(groups = { "always" })
	public void CreateSignature() throws HarnessException {
		ZimbraAccount.AccountZCS().authenticate();
		ZimbraAccount.AccountZCS()
				.soapSend("<CreateSignatureRequest xmlns='urn:zimbraAccount'>" + "<signature name='" + this.sigName
						+ "'>" + "<content type='text/plain'>" + this.sigBody + "</content>" + "</signature>"
						+ "</CreateSignatureRequest>");

		// Refresh UI
		app.zPageMain.zRefreshMainUI();
		this.app.zPagePreferences.zNavigateTo();

		logger.info("CreateSignature: finish");
	}


	/**
	 * Test case : Create signature through soap Edit it through GUI and Verify
	 * edit text signature through soap
	 */

	@Test (description = "Edit and verify text signature through soap",
			groups = { "smoke", "L1" })

	public void EditTextSignature_01() throws HarnessException {

		String sigEditName = "edit name " + ConfigProperties.getUniqueString();
		String sigEditBody = "edit body " + ConfigProperties.getUniqueString();

		// Signature is created
		SignatureItem signature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), this.sigName);
		ZAssert.assertEquals(signature.getName(), this.sigName, "Verify text signature is created");

		// Click on Mail/signature
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailSignatures);
		FormSignatureNew signew = new FormSignatureNew(app);

		// Select signature which is to be edit
		app.zPageSignature.zSelectSignature(sigName);

		// Verify Body contents
		PageSignature pagesig = new PageSignature(app);
		String signaturebodytext = pagesig.zGetSignatureBodyText();
		ZAssert.assertStringContains(signaturebodytext, this.sigBody, "Verify the text signature body");

		// Edit signame and sigbody
		signew.zFillField(Field.SignatureName, sigEditName);
		signew.zFillField(Field.SignatureBody, sigEditBody);
		signew.zSubmit();

		SignatureItem editsignature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), sigEditName);

		// Verify Edited signature name and body contents
		ZAssert.assertEquals(editsignature.getName(), sigEditName, "Verify Edited signature name");
		ZAssert.assertEquals(editsignature.dBodyText, sigEditBody, "Verify Edited text signature body");
		ZAssert.assertStringDoesNotContain(editsignature.getName(), this.sigName, 	"Verify after edit 1st signature  does not present");
	}
}