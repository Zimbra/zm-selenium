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
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.notifications;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogInformational;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.FormApptNew.Field;
import com.zimbra.qa.selenium.projects.ajax.pages.calendar.PageCalendar;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.PagePreferences.Locators;
import com.zimbra.qa.selenium.projects.ajax.pages.preferences.TreePreferences.TreeItem;

public class SMSNotificationViaEmail extends AjaxCore {

	public SMSNotificationViaEmail() {
		super.startingPage = app.zPagePreferences;
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 8123430160111682678L; {
				put("zimbraFeatureCalendarReminderDeviceEmailEnabled", "TRUE");
				put("zimbraPrefCalendarApptReminderWarningTime", "2");
			}
		};
	}


	@Test (description = "Verify SMS notification via Email for appointment reminder.",
			groups = { "functional-known-failure" } )

	public void SMSNotificationViaEmail_01() throws HarnessException {

		try {

			boolean found = false;

			// Navigate to preferences -> notifications
			app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.Notifications);

			// Select the carrier option as 'Custom'
			app.zPagePreferences.sClick(Locators.zCarrierPullDown);
			app.zPagePreferences.sClick(Locators.zCarrierOptionCustom);

			// Enter email address for SMS notification
			app.zPagePreferences.sType(Locators.zSMSEmailUsername, ZimbraAccount.Account3().EmailAddress.split("@")[0]);
			app.zPagePreferences.sType(Locators.zSMSEmailDomainName, ZimbraAccount.Account3().EmailAddress.split("@")[1]);

			// Select the carrier option as 'Custom' again to enable the button
			app.zPagePreferences.sClick(Locators.zCarrierPullDown);
			app.zPagePreferences.sClick(Locators.zCarrierOptionCustom);

			// Click Send verification code button to send the verification code to the entered email address
			DialogInformational dialog = (DialogInformational) app.zPagePreferences.zPressButton(Button.B_SEND_VERIFICATION_CODE);

			// Check that Information dialog is displayed
			ZAssert.assertTrue(dialog.zIsActive(), "Verify that the information dialog is displayed");

			// Click OK on the dialog
			dialog.zPressButton(Button.B_OK);

			// Check the mailbox of other user for mail containing the verification code
			MailItem received = MailItem.importFromSOAP(ZimbraAccount.Account3(), "in:inbox content:(verification)");

			// Extract the code from the mail and enter it
			app.zPagePreferences.sType(Locators.zValidationCode, received.dBodyText.split(" ")[(received.dBodyText.split(" ").length)-1]);

			// This needs to be done again to enable the Validate code button
			app.zPagePreferences.sClick(Locators.zCarrierPullDown);
			app.zPagePreferences.sClick(Locators.zCarrierOptionCustom);

			// Click on Validate code button
			app.zPagePreferences.zPressButton(Button.B_VALIDATE_CODE);

			// Go to calendar tab
			app.zPageCalendar.zNavigateTo();

			// Open the new appointment form
			FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
			ZAssert.assertNotNull(apptForm, "Verify the new form opened");

			// Appointment Data
			String subject = "Send notification via Email "+ConfigProperties.getUniqueString();

			apptForm.zFillField(Field.Subject,subject );
			apptForm.zFillField(Field.Attendees, ZimbraAccount.AccountA().EmailAddress);
			// apptForm.zFillField(Field.Body, "Content "+ConfigProperties.getUniqueString());

			// Get time for creating the appointment
			Calendar now = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");

			// Making start time 2 mins. ahead of the current time
			now.add(Calendar.MINUTE,3);
			apptForm.zFillField(Field.StartTime, sdf.format(now.getTime()));

			// Making stop time 32 mins. ahead of the current time
			now.add(Calendar.MINUTE,30);
			apptForm.zFillField(Field.EndTime, sdf.format(now.getTime()));

			// Select the check box for SMS reminder
			apptForm.sClick(FormApptNew.Locators.SMSCheckBox);

			// Submit the appointment
			apptForm.zSubmit();

			// Wait for the Appointment reminder dialog to appear
			logger.info("zWaitForElementVisible(" + PageCalendar.Locators.zAppointmentReminderDialog + ")");
		    for (int i = 0; i < 120; i++) {
				if (app.zPageCalendar.sIsVisible(PageCalendar.Locators.zAppointmentReminderDialog)) {
					return;
				}
				SleepUtil.sleepSmall();
			}

			// Look for notification mail in the other user's mailbox
			for (int i = 0; i < 40; i++) {
				received = MailItem.importFromSOAP(ZimbraAccount.Account3(), "in:inbox content:("+ subject +")");
				if (received != null) {
					found = true;
					break;

				} else {
					SleepUtil.sleepVeryVeryLong();
				}
			}

			// Verify that the SMS notification as mail is delivered
			ZAssert.assertTrue(found, "Notification mail is not received");

		} finally {

			zFreshLogin();
			logger.info(app.zGetActiveAccount().EmailAddress);
		}
	}
}