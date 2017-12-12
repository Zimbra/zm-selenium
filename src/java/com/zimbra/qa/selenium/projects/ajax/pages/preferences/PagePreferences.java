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
package com.zimbra.qa.selenium.projects.ajax.pages.preferences;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZDate;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;
import com.zimbra.qa.selenium.projects.ajax.pages.AjaxPages;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogInformational;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogShare.ShareRole;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogWarning.DialogWarningID;
import com.zimbra.qa.selenium.projects.ajax.pages.PageMain;

/**
 * @author Matt Rhoades
 *
 */
public class PagePreferences extends AbsTab {

	public static class Locators {

		// Preferences Toolbar: Save, Cancel
		public static final String zToolbarSaveID = "zb__PREF__SAVE_title";
		public static final String zToolbarCancelID = "zb__PREF__CANCEL_title";
		public static final String zGeneralPreferencesOverviewPane = "zti__main_Options__PREF_PAGE_GENERAL_div";

		public static final String zSaveChangesYes = "id=DWT241_title";
		public static final String zSaveChangesNo = "id=DWT242_title";
		public static final String zSaveChangesCancel = "id=DWT243_title";

		// General > Time zone and language
		public static final String zTimezone = "css=td[id='Prefs_Select_DEFAULT_TIMEZONE_dropdown']";
		public static final String zDownArrow = "css=div[id='Prefs_Select_DEFAULT_TIMEZONE_Menu_1'] div[class='ImgDownArrowSmall']";
		public static final String zUpArrow = "css=div[id='Prefs_Select_DEFAULT_TIMEZONE_Menu_1'] div[class='ImgUpArrowSmall']";
		public static final String zTimezonelistXpath = "xpath=//div[@id='Prefs_Select_DEFAULT_TIMEZONE_Menu_1']/descendant::td[contains(text(),";

		// Compose Direction
		public static final String zShowComposeDirection = "css=input[id$='_SHOW_COMPOSE_DIRECTION_BUTTONS']";

		// Search Folders
		public static final String zSearchTrashFolder = "css=input[id$=_SEARCH_INCLUDES_TRASH]";

		// OOO
		public static final String zSendAutoReplyMessage = "css=input[id='VACATION_MSG_ENABLED_input']";
		public static final String zSendAutoRepliesForTimePeriod = "css=input[id='Prefs_Pages_OUTOFOFFICE_VACATION_DURATION_ENABLED']";
		public static final String zOutOfOfficeAllDay = "css=input[id='Prefs_Pages_OUTOFOFFICE_VACATION_DURATION_ALL_DAY']";
		public static final String zOutOfOfficeCalendarAppt = "css=input[id='Prefs_Pages_OUTOFOFFICE_VACATION_CALENDAR_ENABLED']";
		// Calendar
		public static final String zCustomWorkHours = "css=td[id$='_CAL_WORKING_HOURS_CUSTOM'] input[name$='_normalCustom']";
		public static final String zCustomizeButton = "css=td[id$='_CAL_CUSTOM_WORK_HOURS'] td[id$='_title']:contains('Customize')";
		public static final String zSundayCustomWorkHour = "css=div[class='DwtDialog'] td[id$='_CAL_WORKING_DAY_0'] input[id$='_input']";
		public static final String zMondayCustomWorkHour = "css=div[class='DwtDialog'] td[id$='_CAL_WORKING_DAY_1'] input[id$='_input']";
		public static final String zTuesdayCustomWorkHour = "css=div[class='DwtDialog'] td[id$='_CAL_WORKING_DAY_2'] input[id$='_input']";
		public static final String zWednesdayCustomWorkHour = "css=div[class='DwtDialog'] td[id$='_CAL_WORKING_DAY_3'] input[id$='_input']";
		public static final String zThursdayCustomWorkHour = "css=div[class='DwtDialog'] td[id$='_CAL_WORKING_DAY_4'] input[id$='_input']";
		public static final String zFridayCustomWorkHour = "css=div[class='DwtDialog'] td[id$='_CAL_WORKING_DAY_5'] input[id$='_input']";
		public static final String zSaturdayCustomWorkHour = "css=div[class='DwtDialog'] td[id$='_CAL_WORKING_DAY_6'] input[id$='_input']";
		public static final String zOKButtonCustomDialog = "css=div[class='DwtDialog'] td[id$='_button2_title']";
		public static final String zCancelButtonCustomDialog = "css=div[class='DwtDialog'] td[id$='_button1_title']";
		public static final String zYesButtonWarningDialog = "css=div[id='YesNoMsgDialog'] td[id='YesNoMsgDialog_button5_title']";
		public static final String zNoButtonWarningDialog = "css=div[id='YesNoMsgDialog'] td[id='YesNoMsgDialog_button4_title']";
		public static final String zMondayWorkWeek = "css=div[id='CAL_WORKING_HOURS1'] td[id='CAL_WORKING_HOURS1_CAL_WORKING_DAY_1'] input[type='checkbox']";
		public static final String zStartWeekOn = "css=td[id='Prefs_Select_CAL_FIRST_DAY_OF_WEEK_select_container']";
		public static final String zStartWeekOnSunday = "css=td[id$='_title']:contains('Sunday')";
		public static final String zStartWeekOnMonday = "css=td[id$='_title']:contains('Monday')";
		public static final String zStartWeekOnTuesday = "css=td[id$='_title']:contains('Tuesday')";
		public static final String zStartWeekOnWednesday = "css=td[id$='_title']:contains('Wednesday')";
		public static final String zDefaultAppointmentDuration = "css=td[id='Prefs_Select_CAL_DEFAULT_APPT_DURATION_select_container']";
		public static final String zAppointmentDuration30 = "css=td[id$='_title']:contains('30')";
		public static final String zAppointmentDuration60 = "css=td[id$='_title']:contains('60')";
		public static final String zAppointmentDuration90 = "css=td[id$='_title']:contains('90')";
		public static final String zAppointmentDuration120 = "css=td[id$='_title']:contains('120')";
		public static final String zShareFolderType = "css=td[id$='_shareButton_title']";
		public static final String zShowCalendarsWithWeekNumbers = "css=input[id='Prefs_Pages_CALENDAR_CAL_SHOW_CALENDAR_WEEK']";

		// Share dialogue
		public static final String zDialogShareId = "ShareDialog";

		// Accounts
		public static final String zAddDelegateButton = "css=div[id$='_PRIMARY'] td[id$='_title']:contains('Add Delegate')";
		public static final String zEditPermissionsButton = "css=div[id$='_PRIMARY'] td[id$='_title']:contains('Edit Permissions')";
		public static final String zRemovePermissionsButton = "css=div[id$='_PRIMARY'] td[id$='_title']:contains('Remove')";

		public static final String z2FAEnableLink = "css=div[id='Prefs_Pages_ACCOUNTS_PRIMARY'] a[id='Prefs_Pages_ACCOUNTS_TWO_STEP_AUTH_LINK']:contains('Setup two-step authentication ...')";
		public static final String zDisable2FALink = "css=div[id='Prefs_Pages_ACCOUNTS_PRIMARY'] a[id='Prefs_Pages_ACCOUNTS_TWO_STEP_AUTH_LINK']:contains('Disable two-step authentication ...')";
		public static final String zTrustedDeviceCount = "css=td[class='ZOptionsField'] span[id='Prefs_Pages_ACCOUNTS_TRUSTED_DEVICES_COUNT']:contains('You have 1 trusted device')";
		public static final String zRevokeThisDeviceLink = "css=td[class='ZOptionsField'] a[id='Prefs_Pages_ACCOUNTS_TRUSTED_DEVICE_REVOKE_LINK']:contains('revoke this device')";
		public static final String zChangePwdButton = "css=td[id='CHANGE_PASSWORD_title']";
		public static final String zAddApplicationCodeButton = "css=td[id='addApplicationCodeBtn_title']";
		public static final String zAddExternalAccountButton = "css=td[id$='_title']:contains('Add External Account')";
		public static final String zExternalAccountRow1 = "css=div[id^='zlic__ACCT__new-dsrc'][id$='__na_name']:contains('New External Account')";
		public static final String zAddPersonaButton = "css=td[id$='_title']:contains('Add Persona')";
		public static final String zPersonaRow1 = "css=div[id^='zlic__ACCT__new-persona'][id$='__na_name']:contains('New Persona')";
		public static final String zPop3RadioButton = "css=input[id$='_input'][value='Pop']";
		public static final String zImapRadioButton = "css=input[id$='_input'][value='Imap']";
		public static final String zImapText = "css=div#Prefs_Pages_ACCOUNTS_EXTERNAL td.ZOptionsField:contains('IMAP')";
		public static final String zPop3Text = "css=div#Prefs_Pages_ACCOUNTS_EXTERNAL td.ZOptionsField:contains('POP3')";

		// Import/Export
		public static final String zBrowseFileButton = "css=input#ZmImportView_FILE";
		public static final String zImportButton = "css=div[id='IMPORT_BUTTON'] td[id$='_title']";
		public static final String zImportDialog = "css=div[id='ErrorDialog']";
		public static final String zImportDialogContent = "css=div[id='ErrorDialog'] div[id$='_content']";
		public static final String zImportDialogOkButton = "css=div[id='ErrorDialog'] div[id$='_button2']";

		// Mail > Displaying Messages
		public static final String zDisplayExternalImage = "css=input[id$='_MAIL_DISPLAY_EXTERNAL_IMAGES']";
		public static final String zDisplayMessageColor = "css=input[id$='_MAIL_COLOR_MESSAGES']";
		public static final String zDisplayMailAsHTML = "div[id='Prefs_RadioGroup_VIEW_AS_HTML'] input[value='true']";
		public static final String zDisplayMailAsText = "div[id='Prefs_RadioGroup_VIEW_AS_HTML'] input[value='false']";

		// Mail > composing
		public static final String zMandatorySpellCheck = "css=input[id$='_MAIL_MANDATORY_SPELLCHECK']";

		// Secure Email
		public static final String zBrowseToCertificate = "css=div[id$='_UploadCertificateBtn'] td:contains('Browse to certificate...')";
		public static final String zRemoveCertificateLink = "css=td[class='ZmSecureMailCertificateRow'] td:contains('Remove')";
		public static final String zViewCertificateLink = "css=td[class='ZmSecureMailCertificateRow'] div:contains('View')";

		// Filters
		public static final String zFilterRowCss = "css=div[class='DwtListView-Rows'] div[id^='zli_'] td";
		public static final String zOutGoingFilterTab = "css=div[id$='_tabbar'] td[id$='_title']:contains(Outgoing)";
		public static final String zAfterFilterRunOKButton = "css=div[id='ZmMsgDialog'] div[id$='MsgDialog_buttons'] td[id^='OK'] td[id$='_title']";

		// Notifications preferences
		public static final String zCarrierPullDown = "css=div[id$='DEVICE_EMAIL_CARRIER'] td[id*='DEVICE_EMAIL_CARRIER'] div.ImgSelectPullDownArrow";
		public static final String zCarrierOptionCustom = "css=div[id$='_DEVICE_EMAIL_CARRIER_Menu_1'] td[id*='_option_']:contains('Custom')";
		public static final String zSMSEmailUsername = "css=div[id$='_DEVICE_EMAIL_CUSTOM_NUMBER'] input[id$='_DEVICE_EMAIL_CUSTOM_NUMBER_input']";
		public static final String zSMSEmailDomainName = "css=div[id$='_DEVICE_EMAIL_CUSTOM_ADDRESS'] input[id$='_DEVICE_EMAIL_CUSTOM_ADDRESS_input']";
		public static final String zValidationCode = "css=input[id$='_DEVICE_EMAIL_CODE_input']";
		public static final String zSendVerificationCodeBtn = "css=td[id$='_DEVICE_EMAIL_CUSTOM_SEND_CODE_title']";
		public static final String zValidateCodeBtn = "css=td[id$='_DEVICE_EMAIL_CODE_VALIDATE_title']";
	}

	public static class Field {

		public static final Field StartDate = new Field("StartDate");
		public static final Field StartTime = new Field("StartTime");
		public static final Field EndDate = new Field("EndDate");
		public static final Field EndTime = new Field("EndTime");

		private String field;

		private Field(String name) {
			field = name;
		}

		@Override
		public String toString() {
			return (field);
		}

	}

	public Boolean zVerifyDisable2FALink() throws HarnessException {
		return sIsElementPresent(Locators.zDisable2FALink);
	}

	public Boolean zVerifySetup2FALink() throws HarnessException {
		return sIsElementPresent(Locators.z2FAEnableLink);
	}

	public Boolean zVerifyRevokeThisDevice() throws HarnessException {
		return sIsElementPresent(Locators.zRevokeThisDeviceLink);
	}

	public Boolean zVerifyDisabledRevokeThisDeviceLink() throws HarnessException {
		return sIsElementPresent(
				"css=td[class='ZOptionsField'] a[id='Prefs_Pages_ACCOUNTS_TRUSTED_DEVICE_REVOKE_LINK'].ZmLinkDisabled");
	}

	public Boolean zVerifyTrustedDeviceCount(int deviceCount) throws HarnessException {
		return sIsElementPresent(
				"css=td[class='ZOptionsField'] span[id='Prefs_Pages_ACCOUNTS_TRUSTED_DEVICES_COUNT']:contains('You have "
						+ deviceCount + " trusted device')");
	}

	public PagePreferences(AbsApplication application) {
		super(application);
		logger.info("new " + PagePreferences.class.getCanonicalName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		if (!((AjaxPages) MyApplication).zPageMain.zIsActive()) {
			((AjaxPages) MyApplication).zPageMain.zNavigateTo();
		}

		String locator;
		boolean loaded, visible;

		locator = PagePreferences.Locators.zGeneralPreferencesOverviewPane;
		loaded = this.sIsElementPresent(locator);
		visible = this.zIsVisiblePerPosition(locator, -1, 74);
		if (loaded && visible)
			return (true);

		return false;
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public void zNavigateTo() throws HarnessException {

		if (zIsActive()) {
			logger.info(myPageName() + " is already loaded");
			return;
		}

		((AjaxPages) MyApplication).zPageMain.zCheckAppLoaded(PageMain.Locators.zPreferencesTab);
	}

	public void zNavigateAway(Button savechanges) throws HarnessException {
		logger.info("zNavigateAway(" + savechanges + ")");

		// Click Cancel
		zToolbarPressButton(Button.B_CANCEL);

		// Wait for the dialog to appear
		SleepUtil.sleep(5000);

		// Check for the dialog
		if (zIsVisiblePerPosition("id=DWT240", 420, 200)) {
			logger.debug("zNavigateAway(" + savechanges + ") - dialog is showing");

			String locator = null;

			// "Would you like to save your changes?" is displayed.
			if (savechanges == Button.B_YES) {
				locator = Locators.zSaveChangesYes;
			} else if (savechanges == Button.B_NO) {
				locator = Locators.zSaveChangesNo;
			} else if (savechanges == Button.B_CANCEL) {
				locator = Locators.zSaveChangesCancel;
			} else {
				throw new HarnessException("zNavigateAway() not defined for button " + savechanges);
			}

			if (locator == null) {
				throw new HarnessException("zNavigateAway() no locator for button " + savechanges);
			}

			if (!sIsElementPresent(locator)) {
				throw new HarnessException("zNavigateAway() locator is not present " + locator);
			}

			sClick(locator);

		} else {
			logger.debug("zNavigateAway(" + savechanges + ") - dialog did not show");
		}

	}

	public boolean zGetCheckboxStatus(String preference) throws HarnessException {
		logger.info("zGetCheckboxStatus(" + preference + ")");

		String locator = null;

		if (preference.equals("zimbraPrefIncludeSpamInSearch")) {

			locator = "css=input[id$=_SEARCH_INCLUDES_SPAM]";

		} else if (preference.equals("zimbraPrefIncludeTrashInSearch")) {

			locator = "css=input[id$=_SEARCH_INCLUDES_TRASH]";

		} else if (preference.equals("zimbraPrefShowSearchString")) {

			locator = "css=input[id$=_SHOW_SEARCH_STRING]";

		} else if (preference.equals("zimbraPrefAutoAddAddressEnabled")) {

			locator = "css=input[id$=_AUTO_ADD_ADDRESS]";

		} else if (preference.equals("zimbraPrefAutocompleteAddressBubblesEnabled")) {

			locator = "css=input[id$=_USE_ADDR_BUBBLES]";

		} else {
			throw new HarnessException("zGetCheckboxStatus() not defined for preference " + preference);
		}

		if (!sIsElementPresent(locator)) {
			throw new HarnessException("locator not present " + locator);
		}

		boolean checked = sIsChecked(locator);
		logger.info("zGetCheckboxStatus(" + preference + ") = " + checked);

		return (checked);
	}

	@Override
	public AbsPage zListItem(Action action, String item) throws HarnessException {
		throw new HarnessException(myPageName() + " does not have a Toolbar");
	}

	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption, String item) throws HarnessException {

		throw new HarnessException("Not applicaple for Preference");
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String item) throws HarnessException {
		throw new HarnessException(myPageName() + " does not have a Toolbar");
	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		tracer.trace("Click button " + button);

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		String locator = null;
		AbsPage page = null;

		if (button == Button.B_SAVE) {

			locator = "id=" + Locators.zToolbarSaveID;
			page = null;

		} else if (button == Button.B_CANCEL) {

			locator = "id=" + Locators.zToolbarCancelID;
			page = null;

		} else if (button == Button.B_BROWSE_TO_CERTIFICATE) {

			locator = Locators.zBrowseToCertificate;

		} else if (button == Button.B_CHANGE_PASSWORD) {

			locator = Locators.zChangePwdButton;
			page = new SeparateWindowChangePassword(MyApplication);

		} else if (button == Button.B_NEW_IN_FILTER) {

			locator = "css=div[id='zb__FRV__ADD_FILTER_RULE'] td[id$='_title']";
			page = new DialogEditFilter(MyApplication, ((AjaxPages) MyApplication).zPagePreferences);

		} else if (button == Button.B_EDIT_IN_FILTER) {

			locator = "css=div[id='zb__FRV__EDIT_FILTER_RULE'] td[id$='_title']";
			page = new DialogEditFilter(MyApplication, ((AjaxPages) MyApplication).zPagePreferences);
		} else if (button == Button.B_DELETE_IN_FILTER) {

			locator = "css=div[id='zb__FRV__REMOVE_FILTER_RULE'] td[id$='_title']";
			page = new DialogWarning(DialogWarningID.DeleteFilterWarningMessage, MyApplication,
					((AjaxPages) MyApplication).zPagePreferences);
		} else if (button == Button.B_RUN_IN_FILTER) {

			locator = "css=div[id='zb__FRV__RUN_FILTER_RULE'] td[id$='_title']";
			page = null;
		} else if (button == Button.B_NEW_OUT_FILTER) {

			locator = "css=div[id^='zb__FRV__ADD_FILTER_RULE__DWT'] td[id$='_title']";
			page = new DialogEditFilter(MyApplication, ((AjaxPages) MyApplication).zPagePreferences);

		} else if (button == Button.B_EDIT_OUT_FILTER) {

			locator = "css=div[id^='zb__FRV__EDIT_FILTER_RULE__DWT'] td[id$='_title']";
			page = new DialogEditFilter(MyApplication, ((AjaxPages) MyApplication).zPagePreferences);
		} else if (button == Button.B_DELETE_OUT_FILTER) {

			locator = "css=div[id^='zb__FRV__REMOVE_FILTER_RULE__DWT'] td[id$='_title']";
			page = new DialogWarning(DialogWarningID.DeleteFilterWarningMessage, MyApplication,
					((AjaxPages) MyApplication).zPagePreferences);
		} else if (button == Button.B_RUN_OUT_FILTER) {

			locator = "css=div[id^='zb__FRV__RUN_FILTER_RULE__DWT'] td[id$='_title']";
			page = null;
		} else if (button == Button.B_ACTIVITY_STREAM_SETTINGS) {

			locator = "css=div[id$='_ACTIVITY_STREAM_BUTTON'] td[id$='_title']";
			page = new DialogActivityStream(MyApplication, ((AjaxPages) MyApplication).zPagePreferences);

		} else if (button == Button.B_NEW_QUICK_COMMAND) {

			locator = "css=div[id='zb__QCV__ADD_QUICK_COMMAND'] td[id$='_title']";
			page = new DialogEditQuickCommand(MyApplication, ((AjaxPages) MyApplication).zPagePreferences);

		} else if (button == Button.B_EDIT_QUICK_COMMAND) {

			locator = "css=div[id='zb__QCV__EDIT_QUICK_COMMAND'] td[id$='_title']";
			page = new DialogEditQuickCommand(MyApplication, ((AjaxPages) MyApplication).zPagePreferences);

		} else if (button == Button.B_DELETE_QUICK_COMMAND) {

			locator = "css=div[id='zb__QCV__REMOVE_QUICK_COMMAND'] td[id$='_title']";
			page = new DialogWarning(DialogWarningID.QuickCommandConfirmDelete, MyApplication,
					((AjaxPages) MyApplication).zPagePreferences);

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("Button is not present locator=" + locator + " button=" + button);

		this.sClick(locator);
		this.zWaitForBusyOverlay();

		if (page != null) {
			page.zWaitForActive();
			page.zWaitForBusyOverlay();
		}

		SleepUtil.sleepMedium();

		return (page);
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		tracer.trace("Click pulldown " + pulldown + " then " + option);
		AbsPage page = null;

		if (pulldown != null) {
			if (pulldown == Button.O_START_WEEK_ON) {

				if (!this.sIsElementPresent(Locators.zStartWeekOn)) {
					throw new HarnessException("pulldownLocator not present! " + Locators.zStartWeekOn);
				}

				this.sClick(Locators.zStartWeekOn);

				this.zWaitForBusyOverlay();

				if (option != null) {

					if (option == Button.O_START_WEEK_ON_TUESDAY) {
						if (!this.sIsElementPresent(Locators.zStartWeekOnTuesday)) {
							throw new HarnessException("optionLocator not present! " + Locators.zStartWeekOnTuesday);
						}

						this.sClick(Locators.zStartWeekOnTuesday);

						this.zWaitForBusyOverlay();
					}
				}
			}

			else if (pulldown == Button.O_DEFAULT_APPOINTMENT_DURATION) {

				if (!this.sIsElementPresent(Locators.zDefaultAppointmentDuration)) {
					throw new HarnessException("pulldownLocator not present! " + Locators.zDefaultAppointmentDuration);
				}

				this.sClick(Locators.zDefaultAppointmentDuration);

				this.zWaitForBusyOverlay();

				if (option != null) {

					if (option == Button.O_APPOINTMENT_DURATION_90) {
						if (!this.sIsElementPresent(Locators.zAppointmentDuration90)) {
							throw new HarnessException("optionLocator not present! " + Locators.zAppointmentDuration90);
						}

						this.sClick(Locators.zAppointmentDuration90);

						this.zWaitForBusyOverlay();
					}
				}
			}

		}
		return (page);
	}

	public AbsPage zFolderPressPulldown(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		tracer.trace("Click button " + button);

		AbsPage page = null;

		if (button != null) {
			if (button == Button.O_SHARE_FOLDER_TYPE) {

				if (!this.sIsElementPresent(Locators.zShareFolderType)) {
					throw new HarnessException("pulldownLocator not present! " + Locators.zShareFolderType);
				}

				this.sClick(Locators.zShareFolderType);
				this.zWaitForBusyOverlay();
				SleepUtil.sleepSmall();
			}

			else if (button == Button.O_TIMEZONE) {

				if (!this.sIsElementPresent(Locators.zTimezone)) {
					throw new HarnessException("pulldownLocator not present! " + Locators.zTimezone);
				}

				this.sClick(Locators.zTimezone);
				this.zWaitForBusyOverlay();
				SleepUtil.sleepSmall();

			}
		}
		return page;
	}

	public void zSelectRadioButton(Button option) throws HarnessException {

		if (option == null)
			throw new HarnessException("Option cannot be null!");

		String locator = null;

		if (option == Button.R_CUSTOM_WORK_HOURS) {

			locator = Locators.zCustomWorkHours;

		} else if (option == Button.R_SEND_AUTOREPLY_MESSAGE) {

			locator = Locators.zSendAutoReplyMessage;

		} else {
			throw new HarnessException("no logic defined for option " + option);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for option " + option);
		}

		this.sClick(locator);
		this.zWaitForBusyOverlay();

	}

	public void zSelectCheckBox(Button option) throws HarnessException {

		if (option == null)
			throw new HarnessException("Option cannot be null!");

		String locator = null;

		if (option == Button.B_MONDAY_CHECK_BOX) {

			locator = Locators.zMondayCustomWorkHour;

		} else if (option == Button.C_SEND_AUTOREPLY_FOR_TIME_PERIOD) {

			locator = Locators.zSendAutoRepliesForTimePeriod;

		} else if (option == Button.C_OUT_OF_OFFICE_ALLDAY) {

			locator = Locators.zOutOfOfficeAllDay;

		} else if (option == Button.C_OUT_OF_OFFICE_CALENDAR_APPT) {

			locator = Locators.zOutOfOfficeCalendarAppt;

		} else {
			throw new HarnessException("no logic defined for option " + option);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for option " + option);
		}

		this.sClickAt(locator, "");
		this.zWaitForBusyOverlay();

	}

	public AbsPage zPressButton(Button button) throws HarnessException {

		logger.info(myPageName() + " zPressButton(" + button + ")");

		tracer.trace("Click button " + button);

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		String locator = null;
		AbsPage page = null;

		if (button == Button.B_CUSTOMIZE) {

			locator = Locators.zCustomizeButton;
			page = null;

		} else if (button == Button.B_YES) {

			locator = Locators.zYesButtonWarningDialog;
			page = null;

		} else if (button == Button.B_OK) {

			if (sIsElementPresent("css=div[id='" + Locators.zDialogShareId + "'] td[id^='OK'] td[id$='_title']")) {
				locator = "css=div[id='" + Locators.zDialogShareId + "'] td[id^='OK'] td[id$='_title']";
			} else {
				locator = Locators.zOKButtonCustomDialog;
			}
			page = null;

		} else if (button == Button.B_CANCEL) {

			if (sIsElementPresent("css=div[id='" + Locators.zDialogShareId + "'] td[id^='Cancel'] td[id$='_title']")) {
				locator = "css=div[id='" + Locators.zDialogShareId + "'] td[id^='Cancel'] td[id$='_title']";
			} else {
				locator = Locators.zCancelButtonCustomDialog;
			}
			page = null;

		} else if (button == Button.B_NO) {

			locator = Locators.zNoButtonWarningDialog;
			page = null;

		} else if (button == Button.B_IMPORT) {

			locator = Locators.zImportButton;
			page = null;

		} else if (button == Button.B_ADD_APPLICATION_CODE) {

			locator = Locators.zAddApplicationCodeButton;
			page = new DialogAddApplicationCode(MyApplication, ((AjaxPages) MyApplication).zPagePreferences);

		} else if (button == Button.B_IMPORT_OK) {

			locator = Locators.zImportDialogOkButton;
			page = null;

		} else if (button == Button.B_SEND_VERIFICATION_CODE) {

			locator = Locators.zSendVerificationCodeBtn;
			page = new DialogInformational(DialogInformational.DialogWarningID.InformationalDialog, this.MyApplication,
					((AjaxPages) this.MyApplication).zPagePreferences);

		} else if (button == Button.B_VALIDATE_CODE) {

			locator = Locators.zValidateCodeBtn;
			page = null;

		} else if (button == Button.B_ADD_DELEGATE) {

			locator = Locators.zAddDelegateButton;
			page = null;

		} else if (button == Button.B_EDIT_PERMISSIONS) {

			locator = Locators.zEditPermissionsButton;
			page = null;

		} else if (button == Button.B_REMOVE_PERMISSIONS) {

			locator = Locators.zRemovePermissionsButton;
			page = null;

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for button " + button);
		}

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("Button is not present locator=" + locator + " button=" + button);

		this.sClick(locator);
		SleepUtil.sleepMedium();

		if (button == Button.B_OK || button == Button.B_YES) {
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();
		}

		this.zWaitForBusyOverlay();

		return (page);
	}

	public void zFillField(Field field, ZDate value) throws HarnessException {
		String stringFormat;

		if (field == Field.StartDate || field == Field.EndDate) {
			stringFormat = value.toMM_DD_YYYY();
		} else if (field == Field.StartTime || field == Field.EndTime) {
			stringFormat = value.tohh_mm_aa();
		} else {
			throw new HarnessException("zFillField() not implemented for field: " + field);
		}

		zFillField(field, stringFormat);
	}

	public void zFillField(Field field, String value) throws HarnessException {

		tracer.trace("Set " + field + " to " + value);

		String locator = null;

		// start date
		if (field == Field.StartDate) {

			locator = "css=input[id='Prefs_Pages_OUTOFOFFICE_VACATION_FROM1']";

			// start time
		} else if (field == Field.StartTime) {

			locator = "css=input[id='DwtTimeInputSelect_5_startTimeInput']";

			// end date
		} else if (field == Field.EndDate) {

			locator = "css=input[id='Prefs_Pages_OUTOFOFFICE_VACATION_UNTIL1']";

			// end time
		} else if (field == Field.EndTime) {

			locator = "css=input[id='DwtTimeInputSelect_6_endTimeInput']";

		} else {
			throw new HarnessException("not implemented for field " + field);
		}

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("Field is not present field=" + field + " locator=" + locator);

		else {
			this.sClickAt(locator, "");
			this.clearField(locator);
			this.sClickAt(locator, "");

			if (field == Field.StartDate || field == Field.EndDate || field == Field.StartTime
					|| field == Field.EndTime) {
				this.zKeyboard.zSelectAll(locator);
				this.sTypeDateTime(locator, value);

			} else {
				this.sFocus(locator);
				SleepUtil.sleepSmall();
				this.sType(locator, value);
				this.sFocus(locator);
				this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
				SleepUtil.sleepSmall();
				this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_TAB);
			}
		}
		SleepUtil.sleepSmall();
	}

	public void zCheckboxSet(Button checkbox, boolean status) throws HarnessException {

		logger.info(myPageName() + " zPressButton(" + checkbox + ")");

		tracer.trace("Click button " + checkbox);

		if (checkbox == null)
			throw new HarnessException("Button cannot be null!");

		String locator = null;

		if (checkbox == Button.C_SUNDAY_WORK_HOUR) {

			locator = Locators.zSundayCustomWorkHour;

		} else if (checkbox == Button.C_MONDAY_WORK_HOUR) {

			locator = Locators.zMondayCustomWorkHour;

		} else if (checkbox == Button.C_TUESDAY_WORK_HOUR) {

			locator = Locators.zTuesdayCustomWorkHour;

		} else if (checkbox == Button.C_WEDNESDAY_WORK_HOUR) {

			locator = Locators.zWednesdayCustomWorkHour;

		} else if (checkbox == Button.C_THURSDAY_WORK_HOUR) {

			locator = Locators.zThursdayCustomWorkHour;

		} else if (checkbox == Button.C_FRIDAY_WORK_HOUR) {

			locator = Locators.zFridayCustomWorkHour;

		} else if (checkbox == Button.C_SATURDAY_WORK_HOUR) {

			locator = Locators.zSaturdayCustomWorkHour;

		} else if (checkbox == Button.C_MONDAY_WORK_WEEK) {

			locator = Locators.zMondayWorkWeek;

		} else if (checkbox == Button.C_SHOW_CALENDAR_WEEK_NUMBERS) {

			locator = Locators.zShowCalendarsWithWeekNumbers;

		} else {
			throw new HarnessException("no logic defined for checkbox " + checkbox);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for checkbox " + checkbox);
		}

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("Button is not present checkbox=" + locator + " button=" + checkbox);

		if (status == true) {
			this.sCheck(locator);
		} else {
			this.sUncheck(locator);
		}

		this.zWaitForBusyOverlay();

	}

	public static class ShareItem {
		public String name = null;
		public String item = null;
		public String type = null;
		public String role = null;
		public String folder = null;
		public String email = null;
		public String with = null;

		public ShareItem() {
		}

		public String toString() {
			return (String.format("name:%s with:%s item:%s type:%s role:%s folder:%s email:%s", name, with, item, type,
					role, folder, email));
		}

	}

	protected ShareItem parseUnacceptedShareItem(String itemLocator) throws HarnessException {
		logger.info(myPageName() + " parseUnacceptedShareItem(" + itemLocator + ")");

		if (!this.sIsElementPresent(itemLocator)) {
			throw new HarnessException("item is not present! " + itemLocator);
		}

		String locator = null;

		ShareItem item = new ShareItem();

		locator = itemLocator + " td";
		if (this.sIsElementPresent(locator)) {
			item.name = this.sGetText(locator);
		}

		locator = itemLocator + " td[id$='_it']";
		if (this.sIsElementPresent(locator)) {
			item.item = this.sGetText(locator);
		}

		locator = itemLocator + " td + td + td";
		if (this.sIsElementPresent(locator)) {
			item.type = this.sGetText(locator);
		}

		locator = itemLocator + " td[id$='_ro']";
		if (this.sIsElementPresent(locator)) {
			item.role = this.sGetText(locator);
		}

		locator = itemLocator + " td + td + td + td + td + td";
		if (this.sIsElementPresent(locator)) {
			item.email = this.sGetText(locator);
		}

		return (item);
	}

	protected ShareItem parseAcceptedShareItem(String itemLocator) throws HarnessException {
		logger.info(myPageName() + " parseAcceptedShareItem(" + itemLocator + ")");

		if (!this.sIsElementPresent(itemLocator)) {
			throw new HarnessException("item is not present! " + itemLocator);
		}

		String locator = null;

		ShareItem item = new ShareItem();

		locator = itemLocator + " td";
		if (this.sIsElementPresent(locator)) {
			item.name = this.sGetText(locator);
		}

		locator = itemLocator + " td[id$='_it']";
		if (this.sIsElementPresent(locator)) {
			item.item = this.sGetText(locator);
		}

		locator = itemLocator + " td + td + td";
		if (this.sIsElementPresent(locator)) {
			item.type = this.sGetText(locator);
		}

		locator = itemLocator + " td[id$='_ro']";
		if (this.sIsElementPresent(locator)) {
			item.role = this.sGetText(locator);
		}

		locator = itemLocator + " td[id$='_fo']";
		if (this.sIsElementPresent(locator)) {
			item.folder = this.sGetText(locator);
		}

		locator = itemLocator + " td + td + td + td + td + td";
		if (this.sIsElementPresent(locator)) {
			item.email = this.sGetText(locator);
		}

		return (item);
	}

	protected ShareItem parseSharedByMeShareItem(String itemLocator) throws HarnessException {
		logger.info(myPageName() + " parseAcceptedShareItem(" + itemLocator + ")");

		if (!this.sIsElementPresent(itemLocator)) {
			throw new HarnessException("item is not present! " + itemLocator);
		}

		String locator = null;

		ShareItem item = new ShareItem();

		locator = itemLocator + " td";
		if (this.sIsElementPresent(locator)) {
			item.with = this.sGetText(locator);
		}

		locator = itemLocator + " td[id$='_it']";
		if (this.sIsElementPresent(locator)) {
			item.item = this.sGetText(locator);
		}

		locator = itemLocator + " td + td + td";
		if (this.sIsElementPresent(locator)) {
			item.type = this.sGetText(locator);
		}

		locator = itemLocator + " td[id$='_ro']";
		if (this.sIsElementPresent(locator)) {
			item.role = this.sGetText(locator);
		}

		return (item);
	}

	public List<ShareItem> zSharesGetUnaccepted() throws HarnessException {
		logger.info(myPageName() + " zSharesGetUnaccepted()");

		List<ShareItem> items = new ArrayList<ShareItem>();

		String rowsLocator = "css=div[id='zl__SVP__rows'] tr";
		if (!this.sIsElementPresent(rowsLocator)) {
			logger.info("No rows - return empty list");
			return (items);
		}

		int count = this.sGetCssCount(rowsLocator);
		for (int i = 0; i < count; i++) {
			String itemLocator = "css=div[id='zl__SVP__rows'] tr" + StringUtils.repeat(" + tr", i);
			ShareItem item = parseUnacceptedShareItem(itemLocator);
			items.add(item);
		}

		return (items);

	}

	/**
	 * Get a list of share rows from the Preferences->Sharing page
	 */
	public List<ShareItem> zSharesGetAccepted() throws HarnessException {
		logger.info(myPageName() + " zSharesGetUnaccepted()");

		List<ShareItem> items = new ArrayList<ShareItem>();

		String rowsLocator = "css=div[id='zl__SVM__rows'] tr";
		if (!this.sIsElementPresent(rowsLocator)) {
			logger.info("No rows - return empty list");
			return (items);
		}

		int count = this.sGetCssCount(rowsLocator);
		for (int i = 0; i < count; i++) {
			String itemLocator = "css=div[id='zl__SVM__rows'] tr" + StringUtils.repeat(" + tr", i);
			ShareItem item = parseAcceptedShareItem(itemLocator);
			items.add(item);
		}

		return (items);

	}

	/**
	 * Get a list of share rows from the Preferences->Sharing page
	 */
	public List<ShareItem> zSharesGetSharedByMe() throws HarnessException {
		logger.info(myPageName() + " zSharesGetUnaccepted()");

		List<ShareItem> items = new ArrayList<ShareItem>();

		String rowsLocator = "css=div[id='zl__SVP__rows'] tr";
		if (!this.sIsElementPresent(rowsLocator)) {
			logger.info("No rows - return empty list");
			return (items);
		}

		int count = this.sGetCssCount(rowsLocator);
		for (int i = 0; i < count; i++) {
			String itemLocator = "css=div[id='zl__SVG__rows'] tr" + StringUtils.repeat(" + tr", i);
			ShareItem item = parseSharedByMeShareItem(itemLocator);
			items.add(item);
		}

		return (items);

	}

	public void zSetEmailAddress(String email) throws HarnessException {
		logger.info(myPageName() + " zSetEmailAddress(" + email + ")");

		String locator = "css=input#ShareDialog_grantee";

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("zSetEmailAddress " + locator + " is not present");
		}

		this.sFocus(locator);
		this.sClick(locator);
		this.zWaitForBusyOverlay();

		// this.zKeyboard.zTypeCharacters(email);
		this.sType(locator, email);
		SleepUtil.sleepSmall();
		this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
		SleepUtil.sleepSmall();
		this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_TAB);
		SleepUtil.sleepMedium();
		this.zWaitForBusyOverlay();

	}

	public void zSetRole(ShareRole role) throws HarnessException {
		logger.info(myPageName() + " zSetRole(" + role + ")");
		String locator = null;
		if (role == ShareRole.Admin) {
			locator = "//div[@id='" + Locators.zDialogShareId
					+ "']//div[contains(@id,'_content')]//div/fieldset/div/table/tbody/tr[4]/td/input[contains(@id,'ShareRole_ADMIN')]";
		} else if (role == ShareRole.Manager) {
			locator = "//div[@id='" + Locators.zDialogShareId
					+ "']//div[contains(@id,'_content')]//div/fieldset/div/table/tbody/tr[3]/td/input[contains(@id,'ShareRole_MANAGER')]";
		} else {
			throw new HarnessException("zSetRole " + locator + " is not present");
		}
		this.sFocus(locator);
		this.sClick(locator);
		SleepUtil.sleepMedium();

	}

	// To run an incoming filter
	public void zRunIncomingFilter(String filterName, FolderItem... folders) throws HarnessException {
		logger.info(myPageName() + " zRunIncomingFilter (" + filterName + ")" + " on " + folders);

		if (!this.sIsElementPresent(Locators.zFilterRowCss + ":contains(" + filterName + ")")) {

			throw new HarnessException("zRunIncomingFilter " + filterName + " is not present");
		}

		// Select the filter to run it
		this.sClick(Locators.zFilterRowCss + ":contains(" + filterName + ")");

		// Click Run Filter
		this.zToolbarPressButton(Button.B_RUN_IN_FILTER);
		SleepUtil.sleepVerySmall();

		if (folders.length >= 1) {
			for (int i = 0; i < folders.length; i++) {
				this.sClick("css=div[id='zti__ZmFilterRulesController_incoming__" + folders[i].getId() + "_checkbox']");
			}

		} else {
			logger.info(myPageName() + " zRunIncomingFilter (" + filterName + ")" + " on Inbox folder");
			this.sClick("css=div[id='zti__ZmFilterRulesController_incoming__2_checkbox']");
		}

		String ChooseFolderOKButton = "css=div[id^='ChooseFolderDialog'] td[id^='OK'] td[id$='_title']";

		// To make sure that correct OK buttons are clicked;
		if ((sIsElementPresent("css=div[id^='ChooseFolderDialog__DWT'] td[id^='OK'] td[id$='_title']"))
				&& (sIsVisible("css=div[id^='ChooseFolderDialog__DWT'] td[id^='OK'] td[id$='_title']"))) {

			ChooseFolderOKButton = "css=div[id^='ChooseFolderDialog__DWT'] td[id^='OK'] td[id$='_title']";
		}

		// Click OK
		this.sClick(ChooseFolderOKButton);
		SleepUtil.sleepSmall();

		// Click OK when the filter run is complete
		this.sMouseMoveAt(Locators.zAfterFilterRunOKButton, "0,0");
		this.sClickAt(Locators.zAfterFilterRunOKButton, "0,0");
	}

	// To run a outging filter
	public void zRunOutgoingFilter(String filterName, FolderItem... folders) throws HarnessException {
		logger.info(myPageName() + " zRunOutgoingFilter (" + filterName + ")" + " on " + folders);

		// Go to outgoing filter tab
		this.sClick(PagePreferences.Locators.zOutGoingFilterTab);
		SleepUtil.sleepVerySmall();

		if (!this.sIsElementPresent(Locators.zFilterRowCss + ":contains(" + filterName + ")")) {

			throw new HarnessException("zRunOutgoingFilter " + filterName + " is not present");
		}

		// Select the filter to run it
		this.sClick(Locators.zFilterRowCss + ":contains(" + filterName + ")");

		// Click Run Filter
		this.zToolbarPressButton(Button.B_RUN_OUT_FILTER);
		SleepUtil.sleepVerySmall();

		if (folders.length >= 1) {

			for (int i = 0; i < folders.length; i++) {
				this.sClick("css=div[id='zti__ZmFilterRulesController_outgoing__" + folders[i].getId() + "_checkbox']");
			}

		} else {
			logger.info(myPageName() + " zRunIncomingFilter (" + filterName + ")" + " on Inbox folder");
			this.sClick("css=div[id='zti__ZmFilterRulesController_outgoing__2_checkbox']");
		}

		String ChooseFolderOKButton = "css=div[id^='ChooseFolderDialog'] td[id^='OK'] td[id$='_title']";

		// To make sure that correct OK buttons are clicked;
		if ((sIsElementPresent("css=div[id^='ChooseFolderDialog__DWT'] td[id^='OK'] td[id$='_title']"))
				&& (sIsVisible("css=div[id^='ChooseFolderDialog__DWT'] td[id^='OK'] td[id$='_title']"))) {

			ChooseFolderOKButton = "css=div[id^='ChooseFolderDialog__DWT'] td[id^='OK'] td[id$='_title']";
		}

		// Click OK
		this.sClick(ChooseFolderOKButton);
		SleepUtil.sleepSmall();

		// Click OK when the filter run is complete
		this.sMouseMoveAt(Locators.zAfterFilterRunOKButton, "0,0");
		this.sClickAt(Locators.zAfterFilterRunOKButton, "0,0");
	}
}