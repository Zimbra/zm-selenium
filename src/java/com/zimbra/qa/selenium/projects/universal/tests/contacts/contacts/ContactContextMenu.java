/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.universal.tests.contacts.contacts;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.PagePrint;
import com.zimbra.qa.selenium.projects.universal.ui.contacts.PageContacts;
import com.zimbra.qa.selenium.projects.universal.ui.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.universal.ui.search.PageAdvancedSearch;

public class ContactContextMenu extends UniversalCommonTest {
	public ContactContextMenu() {
		logger.info("New " + ContactContextMenu.class.getCanonicalName());

		// All tests start at the Address page
		super.startingPage = app.zPageContacts;

	}

	private ContactItem createSelectAContactItem(String firstName, String lastName, String email, String... tagIdArray)
			throws HarnessException {
		String tagParam = "";
		// default value for file as is last, first
		String fileAs = lastName + ", " + firstName;

		if (tagIdArray.length == 1) {
			tagParam = " t='" + tagIdArray[0] + "'";
		}
		app.zGetActiveAccount()
				.soapSend("<CreateContactRequest xmlns='urn:zimbraMail'>" + "<cn " + tagParam + " fileAsStr='" + fileAs
						+ "' >" + "<a n='firstName'>" + firstName + "</a>" + "<a n='lastName'>" + lastName + "</a>"
						+ "<a n='email'>" + email + "</a>" + "</cn>" + "</CreateContactRequest>");

		ContactItem contactItem = ContactItem.importFromSOAP(app.zGetActiveAccount(),
				"FIELD[lastname]:" + lastName + "");

		// Refresh the view, to pick up the new contact
		FolderItem contactFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), "Contacts");
		app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, contactFolder);

		app.zPageContacts.zToolbarPressButton(Button.B_REFRESH);

		return contactItem;

	}

	private ContactItem createSelectARandomContactItem(String... tagIdArray) throws HarnessException {

		String firstName = "first" + ConfigProperties.getUniqueString();
		String lastName = "last" + ConfigProperties.getUniqueString();
		String email = "email" + ConfigProperties.getUniqueString() + "@zimbra.com";

		return createSelectAContactItem(firstName, lastName, email, tagIdArray);
	}

	@Test(description = "Right click a contact to show a menu", 
			groups = { "smoke", "L0"})
	public void ShowContextMenu_01() throws HarnessException {

		ContactItem contactItem = createSelectARandomContactItem();

		// Right click to show the menu
		app.zPageContacts.zListItem(Action.A_RIGHTCLICK, contactItem.fileAs);

		// Verify all items in the context menu list
		ZAssert.assertTrue(app.zPageContacts.sIsElementPresent(PageContacts.CONTEXT_MENU.CONTACT_SEARCH.locator),
				"Verify contact search in context menu");
		ZAssert.assertTrue(app.zPageContacts.sIsElementPresent(PageContacts.CONTEXT_MENU.CONTACT_NEW_EMAIL.locator),
				"Verify new email in context menu");
		ZAssert.assertTrue(app.zPageContacts.sIsElementPresent(PageContacts.CONTEXT_MENU.CONTACT_EDIT.locator),
				"Verify edit contact  in context menu");
		ZAssert.assertTrue(app.zPageContacts.sIsElementPresent(PageContacts.CONTEXT_MENU.CONTACT_FORWARD.locator),
				"Verify forward email in context menu");
		ZAssert.assertTrue(app.zPageContacts.sIsElementPresent(PageContacts.CONTEXT_MENU.CONTACT_TAG.locator),
				"Verify tag option in context menu");
		ZAssert.assertTrue(app.zPageContacts.sIsElementPresent(PageContacts.CONTEXT_MENU.CONTACT_DELETE.locator),
				"Verify delete option in context menu");
		ZAssert.assertTrue(app.zPageContacts.sIsElementPresent(PageContacts.CONTEXT_MENU.CONTACT_MOVE.locator),
				"Verify move option in context menu");
		ZAssert.assertTrue(app.zPageContacts.sIsElementPresent(PageContacts.CONTEXT_MENU.CONTACT_PRINT.locator),
				"Verify print option in context menu");
	}

	@Test(description = "Right click then click New Email", 
			groups = { "smoke", "L0"})
	public void NewEmail_02() throws HarnessException {

		ContactItem contactItem = createSelectARandomContactItem();

		// Click New Email
		FormMailNew formMailNew = (FormMailNew) app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_NEW,
				contactItem.fileAs);

		// Verify Form New mail is active
		ZAssert.assertTrue(formMailNew.zIsActive(), "Verify Form New Mail is active");

		// Verify contactItem.first contactItem.last displayed in the "To" field
		ZAssert.assertTrue(
				app.zPageContacts.sGetText(FormMailNew.Locators.zBubbleToField)
						.contains(contactItem.firstName + " " + contactItem.lastName),
				"Verify contact email displayed in field To - expected " + contactItem.firstName + " "
						+ contactItem.lastName + " - was "
						+ app.zPageContacts.sGetText(FormMailNew.Locators.zBubbleToField));

		// TODO: Verify send email
	}

	@Test(description = "Right click then click Advanced Search", 
			groups = { "deprecated", "L4"})
	public void AdvancedSearch_03() throws HarnessException {

		ContactItem contactItem = createSelectARandomContactItem();

		// Click Advanced Search
		PageAdvancedSearch pageAdvancedSearch = (PageAdvancedSearch) app.zPageContacts.zListItem(Action.A_RIGHTCLICK,
				Button.B_SEARCHADVANCED, contactItem.fileAs);

		// Verify Advanced Search page is active
		ZAssert.assertTrue(pageAdvancedSearch.zIsActive(), "Verify Advanced Search page is active");

		// close pageAdvancedSearch panel
		pageAdvancedSearch.zToolbarPressButton(Button.B_CLOSE);
	}

	@Test(description = "Right click then click Print", 
			groups = { "smoke-skip", "L4" })
	public void Print_04() throws HarnessException {
		ContactItem contactItem = createSelectARandomContactItem();

		PagePrint pagePrint = (PagePrint) app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_PRINT,
				contactItem.fileAs);

		// close Print Dialog
		pagePrint.cancelPrintDialog();

		// verify first,last,email displayed in Print View
		Assert.assertTrue(
				pagePrint.isContained("css=td[class='contactHeader']",
						contactItem.lastName + ", " + contactItem.firstName),
				" expected: " + contactItem.lastName + "," + contactItem.firstName + " not displayed in Print Page"
						+ " was:");

		Assert.assertTrue(pagePrint.isContained("css=td[class='contactOutput']", contactItem.email),
				contactItem.firstName + " not displayed in Print Page");

	}

	@Test(description = "Right click then  click Find Emails->Received From contact", 
			groups = { "smoke" , "L1"})
	public void FindEmailsReceivedFromContact_05() throws HarnessException {

		// Create email sent to this contacts
		String subject = "subject" + ConfigProperties.getUniqueString();
		String lastName = "lastname " + ConfigProperties.getUniqueString();

		// Send the message from AccountB to the ZWC user
		ZimbraAccount.AccountB()
				.soapSend("<SendMsgRequest xmlns='urn:zimbraMail'>" + "<m>" + "<e t='t' a='"
						+ app.zGetActiveAccount().EmailAddress + "'/>" + "<su>" + subject + "</su>"
						+ "<mp ct='text/plain'>" + "<content>" + "body" + ConfigProperties.getUniqueString()
						+ "</content>" + "</mp>" + "</m>" + "</SendMsgRequest>");

		MailItem.importFromSOAP(ZimbraAccount.AccountB(), "subject:(" + subject + ")");

		ContactItem contactItem = createSelectAContactItem(app.zGetActiveAccount().DisplayName, lastName,
				ZimbraAccount.AccountB().EmailAddress);

		// Click Find Emails -> Received From Contact
		app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_SEARCH, Button.O_SEARCH_MAIL_RECEIVED_FROM_CONTACT,
				contactItem.fileAs);

		// Get the bubleText
		String bubleText;
		if (app.zPageContacts.sIsElementPresent("css=div[id='ztb_searchresults__SR-3'] span[class='addrBubble']")) {
			bubleText = app.zPageSearch.sGetText("css=div[id='ztb_searchresults__SR-3'] span[class='addrBubble']");
		} else if (app.zPageContacts
				.sIsElementPresent("css=div[id='ztb_searchresults__SR-2'] span[class='addrBubble']")) {
			bubleText = app.zPageSearch.sGetText("css=div[id='ztb_searchresults__SR-2'] span[class='addrBubble']");
		} else {
			bubleText = app.zPageSearch.sGetText("css=[class='addrBubble']");
		}

		ZAssert.assertEquals(bubleText, "from:" + ZimbraAccount.AccountB().EmailAddress,
				"Verify the message list exists");

	}

	@Test(description = "Right click then  click Find Emails->Sent To contact", 
			groups = { "smoke", "L1" })
	public void FindEmailsSentToContact_06() throws HarnessException {

		// Create email sent to this contacts
		String subject = "subject" + ConfigProperties.getUniqueString();
		String lastName = "lastname " + ConfigProperties.getUniqueString();

		// Send the message from AccountA to the ZWC user
		ZimbraAccount.AccountA()
				.soapSend("<SendMsgRequest xmlns='urn:zimbraMail'>" + "<m>" + "<e t='t' a='"
						+ app.zGetActiveAccount().EmailAddress + "'/>" + "<su>" + subject + "</su>"
						+ "<mp ct='text/plain'>" + "<content>" + "body" + ConfigProperties.getUniqueString()
						+ "</content>" + "</mp>" + "</m>" + "</SendMsgRequest>");
		MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:(" + subject + ")");

		ContactItem contactItem = createSelectAContactItem(app.zGetActiveAccount().DisplayName, lastName,
				app.zGetActiveAccount().EmailAddress);

		// Click Find Emails -> Sent To Contact
		app.zPageContacts.zListItem(Action.A_RIGHTCLICK, Button.B_SEARCH, Button.O_SEARCH_MAIL_SENT_TO_CONTACT,
				contactItem.fileAs);

		// Get the bubleText
		String bubleText;
		if (app.zPageContacts.sIsElementPresent("css=div[id='ztb_searchresults__SR-3'] span[class='addrBubble']")) {
			bubleText = app.zPageSearch.sGetText("css=div[id='ztb_searchresults__SR-3'] span[class='addrBubble']");
		} else if (app.zPageContacts
				.sIsElementPresent("css=div[id='ztb_searchresults__SR-2'] span[class='addrBubble']")) {
			bubleText = app.zPageSearch.sGetText("css=div[id='ztb_searchresults__SR-2'] span[class='addrBubble']");
		} else {
			bubleText = app.zPageSearch.sGetText("css=[class='addrBubble']");
		}

		ZAssert.assertEquals(bubleText, "tocc:" + app.zGetActiveAccount().EmailAddress,
				"Verify the message list exists");

	}

}
