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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.mail.signatures.toaster;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.Toaster;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.signature.FormSignatureNew;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.signature.FormSignatureNew.Field;

public class CreateSignature extends AjaxCore {

	public CreateSignature() {
		super.startingPage = app.zPagePreferences;
	}


	@Test (description = "Create Simple text signature and verify toast message through GUI",
		groups = { "functional" })

	public void CreateTextSignature_01() throws HarnessException {

		String sigName = "signame" + ConfigProperties.getUniqueString();
		String sigBody = "sigbody" + ConfigProperties.getUniqueString();

		// Click on signature from left pane
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailSignatures);

		// Click on New signature button
		FormSignatureNew signew = (FormSignatureNew) app.zPageSignature.zToolbarPressButton(Button.B_NEW);

		// Fill Signature Name and body
		signew.zFillField(Field.SignatureName, sigName);
		signew.zFillField(Field.SignatureBody, sigBody);
		signew.zSubmit();

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMessage = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMessage, "Preferences Saved", "Verify toast message: Preferences Saved");
	}
}