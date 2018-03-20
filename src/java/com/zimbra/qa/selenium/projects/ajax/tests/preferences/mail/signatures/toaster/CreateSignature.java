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
import com.zimbra.common.soap.Element;
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
		groups = { "functional", "L3" })

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


	@Test (description = "Create Simple Html signature and verify toast message through GUI",
			groups = { "functional", "L3" })

	public void CreateHtmlSignature_02() throws HarnessException {

		String sigName = "signame" + ConfigProperties.getUniqueString();
		String sigBody = "sigbody" + ConfigProperties.getUniqueString();

		// Click on signature from left pane
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailSignatures);

		// Click on New signature button
		FormSignatureNew signew = (FormSignatureNew) app.zPageSignature.zToolbarPressButton(Button.B_NEW);

		// select html format from drop down
		signew.zSelectFormat("html");

		// Fill Signature Name and body
		signew.zFillField(Field.SignatureName, sigName);
		signew.zFillField(Field.SignatureHtmlBody, sigBody);
		signew.zSubmit();

		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMessage = toast.zGetToastMessage();
		ZAssert.assertStringContains(toastMessage, "Preferences Saved", "Verify toast message: Preferences Saved");
	}
	
	
	@Test (description = "Create an signature with existing signature name and verify toast message through GUI",
			groups = { "functional", "L3" })

	public void CreateHtmlSignature_03() throws HarnessException {

		String sigName1 = "signame" + ConfigProperties.getUniqueString();
		String sigBody1 = "sigbody" + ConfigProperties.getUniqueString();

		// Click on signature from left pane
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailSignatures);

		// Click on New signature button
		FormSignatureNew signew = (FormSignatureNew) app.zPageSignature.zToolbarPressButton(Button.B_NEW);

		// select html format from drop down
		signew.zSelectFormat("html");

		// Fill Signature Name and body
		signew.zFillField(Field.SignatureName, "signature1");
		signew.zFillField(Field.SignatureHtmlBody, "Body1");
		signew.zSubmit();
		
		// Navigate to preference --> Signature page
		app.zPagePreferences.zNavigateTo();
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailSignatures);
			
		// Click on new signature button
		signew = (FormSignatureNew) app.zPageSignature.zToolbarPressButton(Button.B_NEW);
		signew.zSelectFormat("html");
		signew.zFillField(Field.SignatureName, sigName1);
		signew.zFillField(Field.SignatureHtmlBody, sigBody1);
		signew.zSubmit();
		
		// Navigate to preference --> Signature page
		app.zPagePreferences.zNavigateTo();
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailSignatures);

		// Create a new signature with name same as the first signature name but in different case
		signew = (FormSignatureNew) app.zPageSignature.zToolbarPressButton(Button.B_NEW);
		signew.zSelectFormat("html");
		signew.zFillField(Field.SignatureName, "Signature1");
		signew.zFillField(Field.SignatureHtmlBody, "body1");
		signew.zSubmit();
		
		// Verifying the toaster message
		Toaster toast = app.zPageMain.zGetToaster();
		String toastMessage = toast.zGetToastMessage();
		System.out.println(toastMessage);
		ZAssert.assertStringContains(toastMessage, "signature already exists: Signature1", "Verify toast message: signature already exists: Signature1");
		
		// Verify through SOAP that the signature is not created
		app.zGetActiveAccount().soapSend("<GetSignaturesRequest xmlns='urn:zimbraAccount'/>");
		Element[] results = app.zGetActiveAccount().soapSelectNodes("//acct:signature[@name='Signature1']");
		ZAssert.assertTrue(results.length==0, "Verify that the signature is not created");	
	}
}
