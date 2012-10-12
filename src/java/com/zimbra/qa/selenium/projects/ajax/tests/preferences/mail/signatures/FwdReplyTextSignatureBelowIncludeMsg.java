package com.zimbra.qa.selenium.projects.ajax.tests.preferences.mail.signatures;
import java.util.HashMap;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.SignatureItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount.SOAP_DESTINATION_HOST_TYPE;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;



public class FwdReplyTextSignatureBelowIncludeMsg extends AjaxCommonTest {
	String sigName = "signame" + ZimbraSeleniumProperties.getUniqueString();
	String sigBody = "sigbody" + ZimbraSeleniumProperties.getUniqueString();

	@SuppressWarnings("serial")
	public FwdReplyTextSignatureBelowIncludeMsg() {
		super.startingPage = app.zPageMail;
		super.startingAccountPreferences = new HashMap<String, String>() {
			{
				put("zimbraPrefComposeFormat", "text");
				put("zimbraPrefGroupMailBy", "message");
				put("zimbraPrefMailSignatureStyle","internet");
			}
		};
	}

	@BeforeClass(groups = { "always" })
	public void CreateSignature() throws HarnessException {
		System.out.println(this.sigName);
		ZimbraAccount.AccountZWC().authenticate(SOAP_DESTINATION_HOST_TYPE.SERVER);
		ZimbraAccount.AccountZWC().soapSend(
				"<CreateSignatureRequest xmlns='urn:zimbraAccount'>"
				+ "<signature name='" + this.sigName + "' >"
				+ "<content type='text/plain'>" + this.sigBody
				+ "</content>" + "</signature>"
				+ "</CreateSignatureRequest>");

	}

	/**
	 * Test case : Verify Text Signature BelowIncludedMsg While Fwd'ing
	 * Create signature through soap 
	 * Send message with text signature through soap
	 * Select Same Msg and click Fwd
	 * Click Options dropdown and select Signature
	 * Verify signature should place Below included message while fwd'ing msg
	 * @throws HarnessException
	 */
	@Test(description = "Verify Text Signature BelowIncludedMsg While Fwd'ing- Verify through GUI ", groups = { "functional" })
	public void FwdMsgWithTextSignatureBelowIncludeMsg_01() throws HarnessException {

		// Signature is created
		FolderItem inboxFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(),SystemFolder.Inbox);
		SignatureItem signature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), this.sigName);
		ZAssert.assertEquals(signature.getName(), this.sigName,"verified Text Signature is created");

		String subject = "subject"+ ZimbraSeleniumProperties.getUniqueString();


		// Send a message to the account(self)
		ZimbraAccount.AccountZWC().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
				"<m>" +
				"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
				"<su>"+ subject +"</su>" +
				"<mp ct='text/plain'>" +
				"<content>content"+ ZimbraSeleniumProperties.getUniqueString() +"</content>" +
				"</mp>" +
				"</m>" +
		"</SendMsgRequest>");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inboxFolder);

		// Select message
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Forward the item
		actual.zPressButton(Button.B_FORWARD);
		ZAssert.assertTrue(actual.zGetMailPropertyAsText(com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail.Field.Subject).contains("Fwd"),"Verify Fwd Window");

		//Click Options Drop Down and select Signature
		app.zPageMail.zToolbarPressPulldown(Button.B_OPTIONS,Button.O_ADD_FWD_SIGNATURE,this.sigName);		

		//Verify Signature is place Below included message.
		actual.zVerifySignaturePlaceInText("BelowIncludedMsg", this.sigBody, "Forward");

		//Closing Fwd compose tab
		app.zPageMail.zClickAt(FormMailNew.Locators.zCancelIconBtn, "0,0");

	}
	/**
	 * Test case : Verify Text Signature BelowIncludedMsg While Reply'ing
	 * Create signature through soap 
	 * Send message with text signature through soap
	 * Select Same Msg and click Reply from toolbar
	 * Click Options dropdown and select Signature
	 * Verify signature should place Below included message while Replying msg
	 * @throws HarnessException
	 */

	@Test(description = "Verify Text Signature BelowIncludedMsg While Replying Msg ", groups = { "functional" })
	public void ReplyMsgWithTextSignatureBelowIncludeMsg_02() throws HarnessException {

		// Signature is created
		FolderItem inboxFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(),SystemFolder.Inbox);

		SignatureItem signature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), this.sigName);
		ZAssert.assertEquals(signature.getName(), this.sigName,"verified Text Signature is created");

		String subject = "subject"+ ZimbraSeleniumProperties.getUniqueString();


		// Send a message to the account(self)
		ZimbraAccount.AccountZWC().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
				"<m>" +
				"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
				"<su>"+ subject +"</su>" +
				"<mp ct='text/plain'>" +
				"<content>content"+ ZimbraSeleniumProperties.getUniqueString() +"</content>" +
				"</mp>" +
				"</m>" +
		"</SendMsgRequest>");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inboxFolder);

		// Select message
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Reply the item
		actual.zPressButton(Button.B_REPLY);
		ZAssert.assertTrue(actual.zGetMailPropertyAsText(com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail.Field.Subject).contains("Re"),"Verify Reply Window");

		//Click Options Drop Down and select Signature
		app.zPageMail.zToolbarPressPulldown(Button.B_OPTIONS,Button.O_ADD_Reply_SIGNATURE,this.sigName);		

		//Verify Signature is place Below included message.
		actual.zVerifySignaturePlaceInText("BelowIncludedMsg", this.sigBody, "Reply");
		//Clsoing Reply Window
		app.zPageMail.zClickAt(FormMailNew.Locators.zCancelIconBtn, "0,0");

	}
	/**
	 * Test case : Verify Text Signature BelowIncludedMsg While ReplyingAll
	 * Create signature through soap 
	 * Send message with text signature through soap
	 * Select Same Msg and click ReplyAll from toolbar
	 * Click Options dropdown and select Signature
	 * Verify signature should place Below included message while Replying msg
	 * @throws HarnessException
	 */
	@Test(description = "Verify Text Signature BelowIncludedMsg While ReplyingAll Msg ", groups = { "functional" })
	public void ReplyAllMsgWithTextSignatureBelowIncludeMsg_03() throws HarnessException {

		// Signature is created
		FolderItem inboxFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(),SystemFolder.Inbox);
		SignatureItem signature = SignatureItem.importFromSOAP(app.zGetActiveAccount(), this.sigName);
		ZAssert.assertEquals(signature.getName(), this.sigName,"verified Text Signature is created");

		String subject = "subject"+ ZimbraSeleniumProperties.getUniqueString();


		// Send a message to the account(self)
		ZimbraAccount.AccountZWC().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
				"<m>" +
				"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
				"<su>"+ subject +"</su>" +
				"<mp ct='text/plain'>" +
				"<content>content"+ ZimbraSeleniumProperties.getUniqueString() +"</content>" +
				"</mp>" +
				"</m>" +
		"</SendMsgRequest>");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);
		app.zTreeMail.zTreeItem(Action.A_LEFTCLICK, inboxFolder);

		// Select message
		DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);

		// Reply the item
		actual.zPressButton(Button.B_REPLYALL);
		ZAssert.assertTrue(actual.zGetMailPropertyAsText(com.zimbra.qa.selenium.projects.ajax.ui.mail.DisplayMail.Field.Subject).contains("Re"),"Verify ReplyAll Window");

		//Click Options Drop Down and select Signature
		app.zPageMail.zToolbarPressPulldown(Button.B_OPTIONS,Button.O_ADD_ReplyAll_SIGNATURE,this.sigName);		

		//Verify Signature is place Below included message in ReplyAll window.
		actual.zVerifySignaturePlaceInText("BelowIncludedMsg", this.sigBody, "ReplyAll");	
		app.zPageMail.zClickAt(FormMailNew.Locators.zCancelIconBtn, "0,0");

	}
}


