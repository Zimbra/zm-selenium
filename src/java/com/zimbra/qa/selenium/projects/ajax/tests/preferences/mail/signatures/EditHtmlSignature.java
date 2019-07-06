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

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.SignatureItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.XmlStringUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.signature.FormSignatureNew;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.signature.PageSignature;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.signature.FormSignatureNew.Field;

public class EditHtmlSignature extends AjaxCore {
	String sigName = "signame " + ConfigProperties.getUniqueString();
	String bodyHTML = "text <strong>bold" + ConfigProperties.getUniqueString() + " </strong>text";
	String contentHTML = XmlStringUtil.escapeXml("<html>" + "<head></head>" + "<body>" + bodyHTML + "</body>" + "</html>");

	public EditHtmlSignature() throws HarnessException {
		super.startingPage = app.zPagePreferences;
	}


	/**
	 * Added @BeforeMethod because after logged in, when we try to create
	 * signature through soap, it doesn't shows in(GUI) 'Pref/signatures' unless
	 * and until we refresh browser.
	 */

	@BeforeMethod(groups = { "always" })
	public void CreateHtmlSignature() throws HarnessException {
		ZimbraAccount.AccountZCS().authenticate();
		ZimbraAccount.AccountZCS()
				.soapSend("<CreateSignatureRequest xmlns='urn:zimbraAccount'>" + "<signature name='" + this.sigName
						+ "'>" + "<content type='text/html'>" + this.contentHTML + "</content>" + "</signature>"
						+ "</CreateSignatureRequest>");

		// Refresh UI
		app.zPageMain.zRefreshMainUI();
		this.app.zPagePreferences.zNavigateTo();

		logger.info("CreateSignature: finish");
	}


	/**
	 * Test case : Create html signature through soap then Edit and verify
	 * edited html signature through soap
	 */

	@Test (description = "Edit signature through GUI and verify through soap",
		groups = { "smoke" })

	public void EditHtmlSignature_01() throws HarnessException {

		String sigEditName = "edit name " + ConfigProperties.getUniqueString();
		String editbodyHTML = "edit body " + ConfigProperties.getUniqueString();

		// HTML Signature is created
		SignatureItem signature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), this.sigName);
		ZAssert.assertEquals(signature.getName(), this.sigName, "Verify html signature name");

		// Click on Mail/signature
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailSignatures);
		PageSignature pagesig = new PageSignature(app);

		// Select created signature
		app.zPageSignature.zSelectSignature(sigName);

		// Verify Body contents
		String signaturebodytext = pagesig.zGetHtmlSignatureBody();
		ZAssert.assertStringContains(signaturebodytext, this.bodyHTML.replace("<b>", "").replace("</b>", ""),
				"Verify the html signature body");

		FormSignatureNew signew = new FormSignatureNew(app);

		// Edit signame and sigbody
		signew.zFillField(Field.SignatureName, sigEditName);
		signew.zFillField(Field.SignatureHtmlBody, editbodyHTML);
		signew.zSubmit();

		SignatureItem editsignature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), sigEditName);

		// Verify signature name and body contents
		ZAssert.assertEquals(editsignature.getName(), sigEditName, "Verify Edited signature name");
		ZAssert.assertStringContains(editsignature.dBodyHtmlText, editbodyHTML, "Verify Edited Html signature body");
		ZAssert.assertStringDoesNotContain(editsignature.getName(), this.sigName, "Verify after edit 1st signature  does not present");
	}
}