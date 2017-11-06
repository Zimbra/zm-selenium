/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.Toaster;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.signature.FormSignatureNew;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.signature.FormSignatureNew.Field;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.signature.PageSignature.Locators;

public class EmptySignature extends AjaxCommonTest {
	public EmptySignature() {
		super.startingPage = app.zPagePreferences;
	}


	@Test( description = "Verify Toast Msg for Empty Signature Name",
			groups = { "functional", "L2"  })

	public void EmptySignature_01() throws HarnessException {

		String sigName = "";
		String sigBody = "sigbody" + ConfigProperties.getUniqueString();

		// Click on signature from left pane
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK,TreeItem.MailSignatures);

		// Click on New signature button
		FormSignatureNew signew =(FormSignatureNew) app.zPageSignature.zToolbarPressButton(Button.B_NEW);

		// Empty Signature Name and some text in body
		signew.zFillField(Field.SignatureName, sigName);
		signew.zFillField(Field.SignatureBody, sigBody);
		signew.zSubmit();

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMsg = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMsg, "Signature name is empty. It's required","Verify toast message:Signature name is empty. It's required");
	}


	@Test( description = "Verify Toast Msg for Empty Signature body (asp per bug comment#10)",
			groups = { "deprecated" })

	public void EmptySignature_02() throws HarnessException {

		String sigName = "signame" + ConfigProperties.getUniqueString();
		String sigBody = "";

		// Click on signature from left pane
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK,TreeItem.MailSignatures);

		// Click on New signature button
		FormSignatureNew signew =(FormSignatureNew) app.zPageSignature.zToolbarPressButton(Button.B_NEW);
		SleepUtil.sleepMedium();

		// Empty Signature body and some text in Name
		signew.zFillField(Field.SignatureName, sigName);
		signew.zFillField(Field.SignatureBody, sigBody);
		signew.zSubmit();

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMsg = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMsg, "Signature value is empty. It's required","Verify toast message:Signature value is empty. It's required");

		// Select signature which is to be Delete
		signew.zClick(Locators.zSignatureListView);
		signew.zClick("//td[contains(text(),'"+sigName+"')]");

		// Click Delete button
		app.zPageSignature.zToolbarPressButton(Button.B_DELETE);
	}
}