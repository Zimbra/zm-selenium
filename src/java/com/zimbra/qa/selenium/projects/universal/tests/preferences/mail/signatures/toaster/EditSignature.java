/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.preferences.mail.signatures.toaster;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.SignatureItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.Toaster;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.TreePreferences.TreeItem;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.signature.FormSignatureNew;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.signature.PageSignature;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.signature.FormSignatureNew.Field;
import com.zimbra.qa.selenium.projects.universal.ui.preferences.signature.PageSignature.Locators;

public class EditSignature extends UniversalCommonTest {
	String sigName = "signame" + ConfigProperties.getUniqueString();
	String sigBody = "sigbody" + ConfigProperties.getUniqueString();

	public EditSignature() throws HarnessException {

		super.startingPage = app.zPagePreferences;

	}

	/**
	 * Added @beforeClass because after logged in ,when we try to create
	 * signature through soap, it doesn't shows in (GUI)'Pref/signatures' unless
	 * and until we refresh browser.
	 * 
	 * @throws HarnessException
	 */
	@BeforeClass(groups = { "always" })
	public void CreateSignature() throws HarnessException {
		ZimbraAccount.AccountZCS().authenticate();
		ZimbraAccount.AccountZCS()
				.soapSend("<CreateSignatureRequest xmlns='urn:zimbraAccount'>" + "<signature name='" + this.sigName
						+ "' >" + "<content type='text/plain'>" + this.sigBody + "</content>" + "</signature>"
						+ "</CreateSignatureRequest>");

	}

	/**
	 * Test case : Create signature through soap Edit it through GUI and Verify
	 * toast message
	 * 
	 * @throws HarnessException
	 */
	@Test(description = " Edit Text singature and verify toast msg", groups = { "functional", "L3"  })
	public void EditTextSignatureToastMsg_01() throws HarnessException {

		String sigEditName = "editsigname" + ConfigProperties.getUniqueString();
		String sigEditBody = "editsigbody" + ConfigProperties.getUniqueString();

		// Signature is created
		SignatureItem signature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), this.sigName);
		ZAssert.assertEquals(signature.getName(), this.sigName, "verified Text Signature is created");

		// Click on Mail/signature
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailSignatures);
		SleepUtil.sleepSmall();
		FormSignatureNew signew = new FormSignatureNew(app);

		// Select signature which is to be edit
		signew.zClick(Locators.zSignatureListView);
		signew.zClick("//td[contains(text(),'" + signature.getName() + "')]");

		// Verify Body contents
		PageSignature pagesig = new PageSignature(app);
		String signaturebodytext = pagesig.zGetSignatureBodyText();
		ZAssert.assertStringContains(signaturebodytext, this.sigBody, "Verify the text signature body");

		// Edit signame and sigbody
		signew.zFillField(Field.SignatureName, sigEditName);
		signew.zFillField(Field.SignatureBody, sigEditBody);
		signew.zSubmit();

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMsg = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMsg, "Preferences Saved", "Verify toast message: Preferences Saved");

	}

}
