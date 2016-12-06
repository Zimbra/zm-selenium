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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.mail.signatures;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.SignatureItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.signature.FormSignatureNew;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.signature.FormSignatureNew.Field;

public class CreateSignature extends AjaxCommonTest {
	public CreateSignature() {
		super.startingPage = app.zPagePreferences;
	}

	@Test(description = "Create Simple text signature through GUI", groups = { "sanity", "L0" })

	public void CreateBasicTextSignature_01() throws HarnessException {

		String sigName = "signame" + ConfigProperties.getUniqueString();
		String sigBody = "sigbody" + ConfigProperties.getUniqueString();

		// click on signature from left pane
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailSignatures);

		// Click on New signature button
		FormSignatureNew signew = (FormSignatureNew) app.zPageSignature.zToolbarPressButton(Button.B_NEW);

		// Fill Signature Name and body
		signew.zFillField(Field.SignatureName, sigName);
		signew.zFillField(Field.SignatureBody, sigBody);
		signew.zSubmit();

		SignatureItem signature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), sigName);

		// Verify signature name and body content
		ZAssert.assertEquals(signature.getName(), sigName, "Verify signature Name");
		ZAssert.assertEquals(signature.dBodyText, sigBody, "Verify text signature body");
	}

	@Test(description = "Create Simple Html signature through GUI", groups = { "sanity", "L0" })

	public void CreateBasicHtmlSignature_02() throws HarnessException {

		String sigName = "signame" + ConfigProperties.getUniqueString();
		String sigBody = "sigbody" + ConfigProperties.getUniqueString();

		// click on signature from left pane
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailSignatures);

		// Click on New signature button
		FormSignatureNew signew = (FormSignatureNew) app.zPageSignature.zToolbarPressButton(Button.B_NEW);

		signew.zSelectFormat("html");

		// Fill Signature Name and body
		signew.zFillField(Field.SignatureName, sigName);
		signew.zFillField(Field.SignatureHtmlBody, sigBody);
		signew.zSubmit();

		SignatureItem signature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), sigName);

		// Verify signature name and body contents
		ZAssert.assertEquals(signature.getName(), sigName, "Verify signature Name");
		ZAssert.assertStringContains(signature.dBodyHtmlText, sigBody, "Verify html signature body");
	}
}
