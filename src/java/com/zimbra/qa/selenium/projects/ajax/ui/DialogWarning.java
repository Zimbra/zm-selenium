/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.ui;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsDialog;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;

/**
 * A <code>DialogWarning</code> object represents a "Warning" dialog, such as
 * "Save current message as draft", etc.
 * <p>
 * During construction, the div ID attribute must be specified, such as
 * "YesNoCancel".
 * <p>
 *
 * @author Matt Rhoades
 *
 */
public class DialogWarning extends AbsDialog {

	public static class DialogWarningID {

		public static final DialogWarningID ZmMsgDialog = new DialogWarningID("ZmMsgDialog");
		public static final DialogWarningID SaveCurrentMessageAsDraft = new DialogWarningID("YesNoCancel");
		public static final DialogWarningID SaveTaskChangeMessage = new DialogWarningID("YesNoCancel");
		public static final DialogWarningID SaveChanges = new DialogWarningID("YesNoCancel");
		public static final DialogWarningID SendLink = new DialogWarningID("css=div[class=DwtConfirmDialog]");
		public static final DialogWarningID DeleteTagWarningMessage = new DialogWarningID("YesNoMsgDialog");
		public static final DialogWarningID EmptyFolderWarningMessage = new DialogWarningID("OkCancel");
		public static final DialogWarningID SaveSignatureChangeMessage = new DialogWarningID("YesNoCancel");
		public static final DialogWarningID CancelCreateContact = new DialogWarningID("YesNoCancel");
		public static final DialogWarningID PermanentlyDeleteTheItem = new DialogWarningID("OkCancel");
		public static final DialogWarningID PermanentlyRemoveTheAttachment = new DialogWarningID("YesNoMsgDialog");
		public static final DialogWarningID DeleteItemWithinRetentionPeriod = new DialogWarningID("OkCancel");
		public static final DialogWarningID DeleteAppointment = new DialogWarningID("YesNo");
		public static final DialogWarningID DeleteItem = new DialogWarningID("OkCancel");

		public static final DialogWarningID ComposeOptionsChangeWarning = new DialogWarningID("OkCancel");
		public static final DialogWarningID SelectedTimeIsInPast = new DialogWarningID("OkCancel");
		public static final DialogWarningID SendReadReceipt = new DialogWarningID("YesNoMsgDialog");;
		public static final DialogWarningID QuickCommandConfirmDelete = new DialogWarningID(
				"ZmQuickCommandConfirmation1");
		public static final DialogWarningID PreferencesSaveChanges = new DialogWarningID("YesNoCancel");
		public static final DialogWarningID SwitchingToTextWillDiscardHtmlFormatting = new DialogWarningID(
				"css=div[id$='_formatWarning']");
		public static final DialogWarningID SmsVerificationCodeSent = new DialogWarningID("ZmMsgDialog");
		public static final DialogWarningID ZmAcceptShare = new DialogWarningID("ZmAcceptShare");
		public static final DialogWarningID ConflictResource = new DialogWarningID("RESC_CONFLICT_DLG");
		public static final DialogWarningID DisableTwoStepAuthentication = new DialogWarningID("YesNoMsgDialog");
		public static final DialogWarningID RemoveCertificate = new DialogWarningID("YesNoMsgDialog");
		public static final DialogWarningID ReloadApplication = new DialogWarningID("YesNoMsgDialog");
		public static final DialogWarningID RevokeTrustedDevice = new DialogWarningID("OkCancel_title");
		public static final DialogWarningID SwitchToTextComposeAppointment = new DialogWarningID(
				"APPT_COMPOSE_1_formatWarning");
		public static final DialogWarningID ZmDeclineShare = new DialogWarningID(
				"css=div[class='ZmDeclineShareDialog']");
		public static final DialogWarningID DeleteFilterWarningMessage = new DialogWarningID("YesNoMsgDialog");

		protected String Id;

		public DialogWarningID(String id) {
			Id = id;
		}
	}

	protected String MyDivId = null;

	public DialogWarning(DialogWarningID dialogId, AbsApplication application, AbsTab tab) {
		super(application, tab);
		MyDivId = dialogId.Id;

		logger.info("new " + DialogWarning.class.getCanonicalName());
		logger.info("mydivid is" + MyDivId);
	}

	public String zGetWarningTitle() throws HarnessException {
		String locator = "css=div[id='" + MyDivId + "'] td[id='" + MyDivId + "_title']";
		return (zGetDisplayedText(locator));
	}

	public String zGetWarningContent() throws HarnessException {
		// String locator = "css=div[id='YesNoCancel_content']";
		String locator = "css=td[id^='MessageDialog'][class='DwtMsgArea']";
		return (zGetDisplayedText(locator));
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public AbsPage zClickButton(Button button) throws HarnessException {
		if (button == null)
			throw new HarnessException("button cannot be null");

		String locator = null;
		AbsPage page = null;

		String buttonsTableLocator = "css=div[id='" + MyDivId + "'] div[id$='_buttons']";

		if (button == Button.B_YES) {
			locator = buttonsTableLocator + " td[id$='_button5_title']";
			if (MyDivId.contains("css=div[class=DwtConfirmDialog]")) {
				page = new FormMailNew(this.MyApplication);
			}

		} else if (button == Button.B_NO) {
			locator = buttonsTableLocator + " td[id$='_button4_title']";

		} else if (button == Button.B_CANCEL) {
			locator = buttonsTableLocator + " td[id$='_button1_title']";

		} else if (button == Button.B_OK) {
			locator = buttonsTableLocator + " td[id$='_button2_title']";

		} else if (button == Button.B_REVOKE) {
			locator = "css=div[id='OkCancel'] div[id='OkCancel_button2'] td[id$='_button2_title']:contains('Revoke')";

		} else if (button == Button.B_SAVE_WITH_CONFLICT) {
			locator = "css= div[id^='RESC_CONFLICT_DLG_button'] td[id^='RESC_CONFLICT_DLG_']:contains('Save')";

		} else if (button == Button.B_CANCEL_CONFLICT) {
			locator = "css= div[id^='RESC_CONFLICT_DLG_button'] td[id^='RESC_CONFLICT_DLG_']:contains('Cancel')";

		} else if (button == Button.B_Signature_OK) {
			locator = "css= div[role='alertdialog'] div[id$='_buttons'] td[id$='_button2_title']";

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		sClick(locator);
		zWaitForBusyOverlay();

		if (page != null) {
			page.zWaitForActive();
		}

		if (button == Button.B_YES || button == Button.B_OK || button == Button.B_SAVE_WITH_CONFLICT
				|| button == Button.B_REVOKE) {
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();
			if (button == Button.B_SAVE_WITH_CONFLICT) {
				SleepUtil.sleepVeryLong();
			} else {
				SleepUtil.sleepMedium();
			}

		} else {
			SleepUtil.sleepMedium();
		}

		return (page);
	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		if (locator == null)
			throw new HarnessException("locator cannot be null");

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("locator cannot be found");

		return (this.sGetText(locator));

	}

	@Override
	public boolean zIsActive() throws HarnessException {

		String dialogPresent = "css=div[id='" + MyDivId + "'][style*='display: block;']";

		if (!this.sIsElementPresent(dialogPresent))
			return (false);

		if (!this.zIsVisiblePerPosition(dialogPresent, 0, 0))
			return (false);

		return (true);
	}
}