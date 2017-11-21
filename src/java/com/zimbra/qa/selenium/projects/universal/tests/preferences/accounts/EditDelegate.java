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
package com.zimbra.qa.selenium.projects.universal.tests.preferences.accounts;

import org.testng.annotations.Test;
import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCore;
import com.zimbra.qa.selenium.projects.universal.pages.DialogDelegate;
import com.zimbra.qa.selenium.projects.universal.pages.DialogError.DialogErrorID;
import com.zimbra.qa.selenium.projects.universal.pages.preferences.TreePreferences.TreeItem;

public class EditDelegate extends UniversalCore {

	public EditDelegate() {
		super.startingPage = app.zPagePreferences;
	}

	
	@Test (description = "Edit a 'Send As' delegate - Add 'Send On Behalf Of'", groups = { "functional", "L2" })
	
	public void EditDelegate_01() throws HarnessException {

		// -- Data Setup

		// Create an account to delegate to
		ZimbraAccount delegate = new ZimbraAccount();
		delegate.provision();
		delegate.authenticate();

		// Grant Send-As
		app.zGetActiveAccount().soapSend("<GrantRightsRequest xmlns='urn:zimbraAccount'>" + "<ace gt='usr' d='"
				+ delegate.EmailAddress + "' right='sendAs'/>" + "</GrantRightsRequest>");

		// -- GUI Steps

		// Refresh UI
		app.zPageMain.zRefreshMainUI();

		// Navigate to preferences -> accounts
		app.zPagePreferences.zNavigateTo();
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailAccounts);

		// Select the grant in the list
		String itemLocator = "css=div[id$='_PRIMARY'] div[id$='__na_name']:contains('" + delegate.EmailAddress + "')";
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(itemLocator),
				"Verify the delegate item is present in the list");
		app.zPagePreferences.sClickAt(itemLocator, "");

		// See http://bugzilla.zimbra.com/show_bug.cgi?id=74282
		String buttonLocator = "css=div[id$='_PRIMARY'] td[id$='_title']:contains('Edit Permissions')";
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(buttonLocator),
				"Verify the add delegate button is present");
		app.zPagePreferences.sClickAt(buttonLocator, "");

		// Wait for the dialog to appear
		DialogDelegate dialog = new DialogDelegate(app, app.zPagePreferences);
		dialog.zWaitForActive();

		// Send As is already checked
		// Also, check Send On Behalf Of
		dialog.zCheckRight(DialogDelegate.Rights.SendOnBehalfOf);
		dialog.zPressButton(Button.B_OK);

		SleepUtil.sleepSmall();

		// -- Verification
		app.zGetActiveAccount().soapSend("<GetRightsRequest xmlns='urn:zimbraAccount' >" + "<ace right='sendAs'/>"
				+ "<ace right='sendOnBehalfOf'/>" + "</GetRightsRequest>");

		boolean foundSendAs = false;
		boolean foundSendOnBehalfOf = false;
		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//acct:ace[@d='" + delegate.EmailAddress + "']");
		for (Element e : nodes) {
			String right = e.getAttribute("right", null);
			if (right != null) {
				if (right.equals("sendAs")) {
					foundSendAs = true;
				} else if (right.equals("sendOnBehalfOf")) {
					foundSendOnBehalfOf = true;
				}
			}
		}

		ZAssert.assertTrue(foundSendAs, "Verify the sendAs is set");
		ZAssert.assertTrue(foundSendOnBehalfOf, "Verify the sendOnBehalfOf is set");
	}

	
	@Test (description = "Edit a 'Send As' delegate - Remove 'Send As' and Add 'Send On Behalf Of'", groups = {
			"functional", "L3" })
	
	public void EditDelegate_02() throws HarnessException {

		// -- Data Setup

		// Create an account to delegate to
		ZimbraAccount delegate = new ZimbraAccount();
		delegate.provision();
		delegate.authenticate();

		// Grant Send-As
		app.zGetActiveAccount().soapSend("<GrantRightsRequest xmlns='urn:zimbraAccount'>" + "<ace gt='usr' d='"
				+ delegate.EmailAddress + "' right='sendAs'/>" + "</GrantRightsRequest>");

		// -- GUI Steps

		// Refresh UI
		app.zPageMain.zRefreshMainUI();
		this.startingPage.zNavigateTo();

		AbsDialog errorDialog = app.zPageMain.zGetErrorDialog(DialogErrorID.Zimbra);
		if ((errorDialog != null) && (errorDialog.zIsActive())) {

			// Dismiss the dialog and carry on
			errorDialog.zPressButton(Button.B_OK);
		}

		// Navigate to preferences -> notifications
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailAccounts);

		// Select the grant in the list
		String itemLocator = "css=div[id$='_PRIMARY'] div[id$='__na_name']:contains('" + delegate.EmailAddress + "')";
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(itemLocator),
				"Verify the delegate item is present in the list");
		app.zPagePreferences.sClickAt(itemLocator, "");

		// See http://bugzilla.zimbra.com/show_bug.cgi?id=74282
		String buttonLocator = "css=div[id$='_PRIMARY'] td[id$='_title']:contains('Edit Permissions')";
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(buttonLocator),
				"Verify the add delegate button is present");
		app.zPagePreferences.sClickAt(buttonLocator, "");

		// Wait for the dialog to appear
		DialogDelegate dialog = new DialogDelegate(app, app.zPagePreferences);
		dialog.zWaitForActive();

		dialog.zCheckRight(DialogDelegate.Rights.SendOnBehalfOf);
		dialog.zPressButton(Button.B_OK);
		app.zPagePreferences.sClickAt(itemLocator, "");
		app.zPagePreferences.sClickAt(buttonLocator, "");
		dialog.zWaitForActive();
		dialog.zUnCheckRight(DialogDelegate.Rights.SendAs);
		// SleepUtil.sleepMedium();
		SleepUtil.sleepLong();
		dialog.zPressButton(Button.B_OK);
		// SleepUtil.sleepMedium();
		SleepUtil.sleepLong();

		// -- Verification
		app.zGetActiveAccount().soapSend("<GetRightsRequest xmlns='urn:zimbraAccount' >" + "<ace right='sendAs'/>"
				+ "<ace right='sendOnBehalfOf'/>" + "</GetRightsRequest>");

		boolean foundSendAs = false;
		boolean foundSendOnBehalfOf = false;
		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//acct:ace[@d='" + delegate.EmailAddress + "']");
		for (Element e : nodes) {
			String right = e.getAttribute("right", null);
			if (right != null) {
				if (right.equals("sendAs")) {
					foundSendAs = true;
				} else if (right.equals("sendOnBehalfOf")) {
					foundSendOnBehalfOf = true;
				}
			}
		}

		ZAssert.assertFalse(foundSendAs, "Verify the sendAs is NOT set");
		ZAssert.assertTrue(foundSendOnBehalfOf, "Verify the sendOnBehalfOf is set");

	}

	
	@Test (description = "Edit a 'Send As' delegate - Remove 'Send As'", groups = { "functional", "L3" })
	
	public void EditDelegate_03() throws HarnessException {

		// -- Data Setup

		// Create an account to delegate to
		ZimbraAccount delegate = new ZimbraAccount();
		delegate.provision();
		delegate.authenticate();

		// Grant Send-As
		app.zGetActiveAccount().soapSend("<GrantRightsRequest xmlns='urn:zimbraAccount'>" + "<ace gt='usr' d='"
				+ delegate.EmailAddress + "' right='sendAs'/>" + "</GrantRightsRequest>");

		// -- GUI Steps

		// Refresh UI
		app.zPageMain.zRefreshMainUI();
		this.startingPage.zNavigateTo();

		// Navigate to preferences -> notifications
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailAccounts);

		// Select the grant in the list
		String itemLocator = "css=div[id$='_PRIMARY'] div[id$='__na_name']:contains('" + delegate.EmailAddress + "')";
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(itemLocator),
				"Verify the delegate item is present in the list");
		app.zPagePreferences.sClickAt(itemLocator, "");

		// See http://bugzilla.zimbra.com/show_bug.cgi?id=74282
		String buttonLocator = "css=div[id$='_PRIMARY'] td[id$='_title']:contains('Edit Permissions')";
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(buttonLocator),
				"Verify the add delegate button is present");
		app.zPagePreferences.sClickAt(buttonLocator, "");

		// Wait for the dialog to appear
		DialogDelegate dialog = new DialogDelegate(app, app.zPagePreferences);
		dialog.zWaitForActive();

		dialog.zUnCheckRight(DialogDelegate.Rights.SendAs);
		dialog.zPressButton(Button.B_OK);

		// -- Verification
		app.zGetActiveAccount().soapSend("<GetRightsRequest xmlns='urn:zimbraAccount' >" + "<ace right='sendAs'/>"
				+ "<ace right='sendOnBehalfOf'/>" + "</GetRightsRequest>");

		boolean foundSendAs = false;
		boolean foundSendOnBehalfOf = false;
		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//acct:ace[@d='" + delegate.EmailAddress + "']");
		for (Element e : nodes) {
			String right = e.getAttribute("right", null);
			if (right != null) {
				if (right.equals("sendAs")) {
					foundSendAs = true;
				} else if (right.equals("sendOnBehalfOf")) {
					foundSendOnBehalfOf = true;
				}
			}
		}

		ZAssert.assertFalse(foundSendAs, "Verify the sendAs is NOT set");
		ZAssert.assertFalse(foundSendOnBehalfOf, "Verify the sendOnBehalfOf is NOT set");

	}

	@Test (description = "Edit a 'Send On Behalf Of' delegate - Add 'Send As'", groups = { "functional", "L3" })
	public void EditDelegate_04() throws HarnessException {

		// -- Data Setup

		// Create an account to delegate to
		ZimbraAccount delegate = new ZimbraAccount();
		delegate.provision();
		delegate.authenticate();

		// Grant Send-As
		app.zGetActiveAccount().soapSend("<GrantRightsRequest xmlns='urn:zimbraAccount'>" + "<ace gt='usr' d='"
				+ delegate.EmailAddress + "' right='sendOnBehalfOf'/>" + "</GrantRightsRequest>");

		// -- GUI Steps

		// Refresh UI
		app.zPageMain.zRefreshMainUI();
		this.startingPage.zNavigateTo();

		// Navigate to preferences -> notifications
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailAccounts);

		// Select the grant in the list
		String itemLocator = "css=div[id$='_PRIMARY'] div[id$='__na_name']:contains('" + delegate.EmailAddress + "')";
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(itemLocator),
				"Verify the delegate item is present in the list");
		app.zPagePreferences.sClickAt(itemLocator, "");

		// See http://bugzilla.zimbra.com/show_bug.cgi?id=74282
		String buttonLocator = "css=div[id$='_PRIMARY'] td[id$='_title']:contains('Edit Permissions')";
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(buttonLocator),
				"Verify the add delegate button is present");
		app.zPagePreferences.sClickAt(buttonLocator, "");

		// Wait for the dialog to appear
		DialogDelegate dialog = new DialogDelegate(app, app.zPagePreferences);
		dialog.zWaitForActive();

		dialog.zCheckRight(DialogDelegate.Rights.SendAs);
		dialog.zPressButton(Button.B_OK);

		// -- Verification
		app.zGetActiveAccount().soapSend("<GetRightsRequest xmlns='urn:zimbraAccount' >" + "<ace right='sendAs'/>"
				+ "<ace right='sendOnBehalfOf'/>" + "</GetRightsRequest>");

		boolean foundSendAs = false;
		boolean foundSendOnBehalfOf = false;
		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//acct:ace[@d='" + delegate.EmailAddress + "']");
		for (Element e : nodes) {
			String right = e.getAttribute("right", null);
			if (right != null) {
				if (right.equals("sendAs")) {
					foundSendAs = true;
				} else if (right.equals("sendOnBehalfOf")) {
					foundSendOnBehalfOf = true;
				}
			}
		}

		ZAssert.assertTrue(foundSendAs, "Verify the sendAs is set");
		ZAssert.assertTrue(foundSendOnBehalfOf, "Verify the sendOnBehalfOf is set");
	}

	@Test (description = "Edit a 'Send On Behalf Of' delegate - Remove 'Send On Behalf Of' and Add 'Send As'", groups = {
			"functional", "L3" })
	public void EditDelegate_05() throws HarnessException {

		// -- Data Setup

		// Create an account to delegate to
		ZimbraAccount delegate = new ZimbraAccount();
		delegate.provision();
		delegate.authenticate();

		// Grant Send-As
		app.zGetActiveAccount().soapSend("<GrantRightsRequest xmlns='urn:zimbraAccount'>" + "<ace gt='usr' d='"
				+ delegate.EmailAddress + "' right='sendOnBehalfOf'/>" + "</GrantRightsRequest>");

		// -- GUI Steps

		// Refresh UI
		app.zPageMain.zRefreshMainUI();
		this.startingPage.zNavigateTo();

		AbsDialog errorDialog = app.zPageMain.zGetErrorDialog(DialogErrorID.Zimbra);
		if ((errorDialog != null) && (errorDialog.zIsActive())) {

			// Dismiss the dialog and carry on
			errorDialog.zPressButton(Button.B_OK);
		}

		// Navigate to preferences -> notifications
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailAccounts);

		// Select the grant in the list
		String itemLocator = "css=div[id$='_PRIMARY'] div[id$='__na_name']:contains('" + delegate.EmailAddress + "')";
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(itemLocator),
				"Verify the delegate item is present in the list");
		app.zPagePreferences.sClickAt(itemLocator, "");

		// See http://bugzilla.zimbra.com/show_bug.cgi?id=74282
		String buttonLocator = "css=div[id$='_PRIMARY'] td[id$='_title']:contains('Edit Permissions')";
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(buttonLocator),
				"Verify the add delegate button is present");
		app.zPagePreferences.sClickAt(buttonLocator, "");

		// Wait for the dialog to appear
		DialogDelegate dialog = new DialogDelegate(app, app.zPagePreferences);
		dialog.zWaitForActive();

		dialog.zCheckRight(DialogDelegate.Rights.SendAs);
		dialog.zPressButton(Button.B_OK);
		app.zPagePreferences.sClickAt(itemLocator, "");
		app.zPagePreferences.sClickAt(buttonLocator, "");
		dialog.zWaitForActive();
		dialog.zUnCheckRight(DialogDelegate.Rights.SendOnBehalfOf);
		dialog.zPressButton(Button.B_OK);

		// -- Verification
		app.zGetActiveAccount().soapSend("<GetRightsRequest xmlns='urn:zimbraAccount' >" + "<ace right='sendAs'/>"
				+ "<ace right='sendOnBehalfOf'/>" + "</GetRightsRequest>");

		boolean foundSendAs = false;
		boolean foundSendOnBehalfOf = false;
		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//acct:ace[@d='" + delegate.EmailAddress + "']");
		for (Element e : nodes) {
			String right = e.getAttribute("right", null);
			if (right != null) {
				if (right.equals("sendAs")) {
					foundSendAs = true;
				} else if (right.equals("sendOnBehalfOf")) {
					foundSendOnBehalfOf = true;
				}
			}
		}

		ZAssert.assertTrue(foundSendAs, "Verify the sendAs is set");
		ZAssert.assertFalse(foundSendOnBehalfOf, "Verify the sendOnBehalfOf is NOT set");
	}

	@Test (description = "Edit a 'Send On Behalf Of' delegate - Remove 'Send On Behalf Of'", groups = { "functional", "L3" })
	public void EditDelegate_06() throws HarnessException {

		// -- Data Setup

		// Create an account to delegate to
		ZimbraAccount delegate = new ZimbraAccount();
		delegate.provision();
		delegate.authenticate();

		// Grant Send-As
		app.zGetActiveAccount().soapSend("<GrantRightsRequest xmlns='urn:zimbraAccount'>" + "<ace gt='usr' d='"
				+ delegate.EmailAddress + "' right='sendOnBehalfOf'/>" + "</GrantRightsRequest>");

		// -- GUI Steps

		// Refresh UI
		app.zPageMain.zRefreshMainUI();
		this.startingPage.zNavigateTo();

		// Navigate to preferences -> notifications
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailAccounts);

		// Select the grant in the list
		String itemLocator = "css=div[id$='_PRIMARY'] div[id$='__na_name']:contains('" + delegate.EmailAddress + "')";
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(itemLocator),
				"Verify the delegate item is present in the list");
		app.zPagePreferences.sClickAt(itemLocator, "");

		// See http://bugzilla.zimbra.com/show_bug.cgi?id=74282
		String buttonLocator = "css=div[id$='_PRIMARY'] td[id$='_title']:contains('Edit Permissions')";
		ZAssert.assertTrue(app.zPagePreferences.sIsElementPresent(buttonLocator),
				"Verify the add delegate button is present");
		app.zPagePreferences.sClickAt(buttonLocator, "");

		// Wait for the dialog to appear
		DialogDelegate dialog = new DialogDelegate(app, app.zPagePreferences);
		dialog.zWaitForActive();

		dialog.zUnCheckRight(DialogDelegate.Rights.SendOnBehalfOf);
		dialog.zPressButton(Button.B_OK);

		// -- Verification
		app.zGetActiveAccount().soapSend("<GetRightsRequest xmlns='urn:zimbraAccount' >" + "<ace right='sendAs'/>"
				+ "<ace right='sendOnBehalfOf'/>" + "</GetRightsRequest>");

		boolean foundSendAs = false;
		boolean foundSendOnBehalfOf = false;
		Element[] nodes = app.zGetActiveAccount().soapSelectNodes("//acct:ace[@d='" + delegate.EmailAddress + "']");
		for (Element e : nodes) {
			String right = e.getAttribute("right", null);
			if (right != null) {
				if (right.equals("sendAs")) {
					foundSendAs = true;
				} else if (right.equals("sendOnBehalfOf")) {
					foundSendOnBehalfOf = true;
				}
			}
		}

		ZAssert.assertFalse(foundSendAs, "Verify the sendAs is NOTset");
		ZAssert.assertFalse(foundSendOnBehalfOf, "Verify the sendOnBehalfOf is NOT set");
	}
}
