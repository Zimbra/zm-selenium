/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.mail.signatures;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.SignatureItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.signature.FormSignatureNew;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.signature.PageSignature.Locators;

public class DeleteTextSignature extends AjaxCommonTest {
	String sigName = "signame" + ZimbraSeleniumProperties.getUniqueString();
	String sigBody = "sigbody" + ZimbraSeleniumProperties.getUniqueString();

	public DeleteTextSignature() throws HarnessException{
		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences = null;
	}

	/**
	 * Added @beforeClass because after logged in ,when we try to create signature through soap,
	 * it doesn't shows in (GUI)'Pref/signatures' unless and until we refresh browser.
	 * @throws HarnessException
	 */
	@BeforeMethod(groups = { "always" })
	public void CreateSignature() throws HarnessException {
		ZimbraAccount.AccountZWC().authenticate();
		ZimbraAccount.AccountZWC().soapSend(
				"<CreateSignatureRequest xmlns='urn:zimbraAccount'>"
				+ "<signature name='" + this.sigName + "' >"
				+ "<content type='text/plain'>" + this.sigBody
				+ "</content>" + "</signature>"
				+ "</CreateSignatureRequest>");
		// Logout and login
		this.app.zPageLogin.zNavigateTo();
		this.app.zPagePreferences.zNavigateTo();

		logger.info("CreateSignature: finish");

	}

	/**
	 * Test case :Create signature through soap then delete and verify signature through soap
	 * @Steps:
	 * Create signature through soap
	 * Delete signature using delete button.
	 * Verify signature doesn't exist from soap
	 * @throws HarnessException
	 */
	@Test(description = " Delete Text signature using Delete button and verify  through soap ", groups = { "smoke" })
	public void DeleteTextSignatures() throws HarnessException {

		//Click on Mail/signature
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK,TreeItem.MailSignatures);

		//Signature is created
		SignatureItem signature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), this.sigName);
		ZAssert.assertEquals(signature.getName(), this.sigName, "verified Text Signature is created");

		FormSignatureNew signew = new FormSignatureNew(app); 

		//Select signature which is to be Delete
		signew.zClick(Locators.zSignatureListView);
		signew.zClick("//td[contains(text(),'"+signature.getName()+"')]");

		//click Delete button
		app.zPageSignature.zToolbarPressButton(Button.B_DELETE);

		//click Save
		signew.zSubmit();

		// To check whether deleted signature is exist
		app.zGetActiveAccount().soapSend("<GetSignaturesRequest xmlns='urn:zimbraAccount'/>");

		String signame = app.zGetActiveAccount().soapSelectValue("//acct:signature[@name='" + this.sigName + "']","name");
		ZAssert.assertNull(signame, "Verify  signature is deleted");

	}
}
