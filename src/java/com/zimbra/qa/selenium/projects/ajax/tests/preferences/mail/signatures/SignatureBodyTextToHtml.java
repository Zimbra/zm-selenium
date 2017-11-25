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

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.SignatureItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.signature.FormSignatureNew;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.signature.FormSignatureNew.Field;

public class SignatureBodyTextToHtml extends AjaxCore {

	public SignatureBodyTextToHtml() throws HarnessException {
		super.startingPage = app.zPagePreferences;
	}


	@Bugs (ids = "98736")
	@Test (description = "Verify that body content of signature is not lost when changed from text to HTMl",
			groups = { "functional", "L3" })

	public void SignatureBodyTextToHtml_01() throws HarnessException {

		String sigName = "signame" + ConfigProperties.getUniqueString();
		String sigBody = "sigbody" + ConfigProperties.getUniqueString();

		// Click on signature from left pane
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailSignatures);

		// Click on New signature button
		FormSignatureNew signew = (FormSignatureNew) app.zPageSignature.zToolbarPressButton(Button.B_NEW);

		// Fill Signature Name and body
		signew.zFillField(Field.SignatureName, sigName);
		signew.zFillField(Field.SignatureBody, sigBody);

		// Select html format from drop down
		signew.zSelectFormat("html");

		ZAssert.assertStringContains(signew.zGetHtmlSignatureBody(), sigBody, "Verify the HTML content signature body");

		app.zPageSignature.zToolbarPressButton(Button.B_DELETE);

		String sigName1 = "signame" + ConfigProperties.getUniqueString();
		String sigBody1 = "sigbody" + ConfigProperties.getUniqueString();

		// Click on New signature button
		FormSignatureNew signew1 = (FormSignatureNew) app.zPageSignature.zToolbarPressButton(Button.B_NEW);

		// Fill Signature Name and body
		signew1.zFillField(Field.SignatureName, sigName1);
		signew1.zFillField(Field.SignatureBody, sigBody1);

		// Select html format from drop down
		signew1.zSelectFormat("html");

		ZAssert.assertStringContains(signew1.zGetHtmlSignatureBody(), sigBody1, "Verify the HTML content signature body");

		signew1.zSubmit();

		SignatureItem editsignature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), sigName1);

		// Verify signature name and body contents
		ZAssert.assertEquals(editsignature.getName(), sigName1, "Verify signature name");
		ZAssert.assertStringContains(editsignature.dBodyHtmlText, sigBody1, "Verify Html signature body");
	}
}