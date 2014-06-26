/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
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

import com.zimbra.qa.selenium.framework.util.HarnessException;

import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.XmlStringUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.signature.FormSignatureNew;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.signature.PageSignature;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.signature.FormSignatureNew.Field;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.signature.PageSignature.Locators;



public class EditHtmlSignature extends AjaxCommonTest {
	String sigName = "signame" + ZimbraSeleniumProperties.getUniqueString();
	String bodyHTML = "text<strong>bold"+ ZimbraSeleniumProperties.getUniqueString() + "</strong>text";
	String contentHTML = XmlStringUtil.escapeXml("<html>" + "<head></head>"
			+ "<body>" + bodyHTML + "</body>" + "</html>");

	public EditHtmlSignature() throws HarnessException {
		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences = null;

	}

	/**
	 * Added @beforeClass because after logged in ,when we try to create
	 * signature through soap, it doesn't shows in(GUI) 'Pref/signatures' unless and
	 * until we refresh browser.
	 * 
	 * @throws HarnessException
	 */
	@BeforeMethod(groups = { "always" })
	public void CreateHtmlSignature() throws HarnessException {
		ZimbraAccount.AccountZWC().authenticate();
		ZimbraAccount.AccountZWC().soapSend(
				"<CreateSignatureRequest xmlns='urn:zimbraAccount'>"
				+ "<signature name='" + this.sigName + "' >"
				+ "<content type='text/html'>'" + this.contentHTML
				+ "'</content>" + "</signature>"
				+ "</CreateSignatureRequest>");

		this.app.zPageLogin.zNavigateTo();
		this.app.zPagePreferences.zNavigateTo();

		logger.info("CreateSignature: finish");
	}

	/**
	 * Test case : Create html signature through soap then Edit and verify
	 * edited html signature through soap
	 * 
	 * @throws HarnessException
	 */

	@Test(description = "Edit signature through GUI and verify through soap", groups = { "smoke" })
	public void EditHtmlSignature_01() throws HarnessException {

		String sigEditName = "editsigname"+ ZimbraSeleniumProperties.getUniqueString();
		String editbodyHTML = "edittextbold"+ ZimbraSeleniumProperties.getUniqueString() + "text";

		// HTML Signature is created
		SignatureItem signature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), this.sigName);
		ZAssert.assertEquals(signature.getName(), this.sigName,"verified Html Signature name ");

		// Click on Mail/signature
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK,TreeItem.MailSignatures);
		
		//Reason:With "?dev=1&debug=0", Tinymce editor in HTML mode takes more time to load 
		//commented out incompatible to webdriver reference
		//if(ClientSessionFactory.session().selenium().getEval("window.tinyMCE").equalsIgnoreCase("null")){
			SleepUtil.sleepVeryLong();
		//}else{
		//	SleepUtil.sleepSmall();
		//}
		PageSignature pagesig = new PageSignature(app);

		//Select created signature signature 
		pagesig.zClick(Locators.zSignatureListView);
		app.zPageSignature.zClick("//td[contains(text(),'"+signature.getName()+"')]");

		//Verify Body contents
		String signaturebodytext = pagesig.zGetHtmlSignatureBody();
		ZAssert.assertStringContains(signaturebodytext, this.bodyHTML,"Verify the html signature body");

		FormSignatureNew signew = new FormSignatureNew(app);

		// Edit signame and sigbody
		signew.zFillField(Field.SignatureName, sigEditName);
		signew.zFillField(Field.SignatureHtmlBody, editbodyHTML);
		signew.zSubmit();

		SignatureItem editsignature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), sigEditName);

		//Verify signature name and body contents
		ZAssert.assertEquals(editsignature.getName(),sigEditName,"Verify Edited signature name");
		ZAssert.assertStringContains(editsignature.dBodyHtmlText,editbodyHTML,"Verify Edited Html signature body");
		ZAssert.assertStringDoesNotContain(editsignature.getName(), this.sigName, "Verify after edit 1st signature  does not present");

	}

}
