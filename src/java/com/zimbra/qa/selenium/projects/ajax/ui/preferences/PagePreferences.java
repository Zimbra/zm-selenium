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
/**
 * 
 */
package com.zimbra.qa.selenium.projects.ajax.ui.preferences;

import java.awt.event.KeyEvent;
import java.util.*;
import org.apache.commons.lang.StringUtils;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties.AppType;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;
import com.zimbra.qa.selenium.projects.ajax.ui.*;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogShare.ShareRole;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning.DialogWarningID;

/**
 * @author Matt Rhoades
 *
 */
public class PagePreferences extends AbsTab {


	public static class Locators {

		public static final String zPreferencesMainID = "zov__main_Options";
		// Preferences Toolbar: Save, Cancel
		public static final String zToolbarSaveID = "zb__PREF__SAVE_title";
		public static final String zToolbarCancelID = "zb__PREF__CANCEL_title";

		public static final String zSaveChangesYes = "id=DWT241_title";
		public static final String zSaveChangesNo = "id=DWT242_title";
		public static final String zSaveChangesCancel = "id=DWT243_title";
		
		//General > Time zone and language
		//Compose Direction
		public static final String zShowComposeDirection = "css=input[id$='_SHOW_COMPOSE_DIRECTION_BUTTONS']";

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

		//Share dialogue
		public static final String zDialogShareId = "ShareDialog";

		//Accounts
		public static final String z2FAEnableLink = "css=div[id='Prefs_Pages_ACCOUNTS_PRIMARY'] a[id='Prefs_Pages_ACCOUNTS_TWO_STEP_AUTH_LINK']:contains('Setup two-step authentication ...')";
		public static final String zDisable2FALink = "css=div[id='Prefs_Pages_ACCOUNTS_PRIMARY'] a[id='Prefs_Pages_ACCOUNTS_TWO_STEP_AUTH_LINK']:contains('Disable two-step authentication ...')";
		public static final String zTrustedDeviceCount = "css=td[class='ZOptionsField'] span[id='Prefs_Pages_ACCOUNTS_TRUSTED_DEVICES_COUNT']:contains('You have 1 trusted device')";
		public static final String zRevokeThisDeviceLink = "css=td[class='ZOptionsField'] a[id='Prefs_Pages_ACCOUNTS_TRUSTED_DEVICE_REVOKE_LINK']:contains('revoke this device')";
		public static final String zChangePwdButton= "css=td[id='CHANGE_PASSWORD_title']";

		//Import/Export
		public static final String zBrowseFileButton= "css=input#ZmImportView_FILE";
		public static final String zImportButton= "css=div[id='IMPORT_BUTTON'] td[id$='_title']";
		public static final String zImportDialog= "css=div[id='ErrorDialog']";
		public static final String zImportDialogContent= "css=div[id='ErrorDialog'] div[id$='_content']";
		public static final String zImportDialogOkButton= "css=div[id='ErrorDialog'] div[id$='_button2']";

		//Mail > composing 
		public static final String zMandatorySpellCheck = "css=input[id$='_MAIL_MANDATORY_SPELLCHECK']";


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
		return sIsElementPresent("css=td[class='ZOptionsField'] a[id='Prefs_Pages_ACCOUNTS_TRUSTED_DEVICE_REVOKE_LINK'].ZmLinkDisabled");
	}

	public Boolean zVerifyTrustedDeviceCount(int deviceCount) throws HarnessException {
		return sIsElementPresent("css=td[class='ZOptionsField'] span[id='Prefs_Pages_ACCOUNTS_TRUSTED_DEVICES_COUNT']:contains('You have " + deviceCount + " trusted device')");
	}


	public PagePreferences(AbsApplication application) {
		super(application);

		logger.info("new " + PagePreferences.class.getCanonicalName());

	}

	/* (non-Javadoc)
	 * @see projects.admin.ui.AbsPage#isActive()
	 */
	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the main page is active
		if ( !((AppAjaxClient)MyApplication).zPageMain.zIsActive() ) {
			((AppAjaxClient)MyApplication).zPageMain.zNavigateTo();
		}

		/*
		 * Active:
		 * <div id="zov__main_Options" style="position: absolute; overflow: auto; z-index: 300; left: 0px; top: 76px; width: 169px; height: 537px;" class="ZmOverview" parentid="z_shell">
		 * 
		 * Not active:
		 * <div id="zov__main_Options" style="position: absolute; overflow: auto; z-index: 300; left: -10000px; top: -10000px; width: 169px; height: 537px;" class="ZmOverview" parentid="z_shell">
		 */


		// If the "folders" tree is visible, then mail is active
		String locator = null;
		if (ZimbraSeleniumProperties.getAppType() == AppType.DESKTOP) {
			locator = "css=div[id='zov__local@host.local:main_Options']";
		} else {
			locator = "css=div#"+ Locators.zPreferencesMainID;
		}

		boolean loaded = this.sIsElementPresent(locator);
		if ( !loaded )
			return (loaded);

		boolean active = this.zIsVisiblePerPosition(locator, -1, 74);
		return (active);

	}

	/* (non-Javadoc)
	 * @see projects.admin.ui.AbsPage#myPageName()
	 */
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	/* (non-Javadoc)
	 * @see projects.admin.ui.AbsPage#navigateTo()
	 */
	@Override
	public void zNavigateTo() throws HarnessException {

		// Check if this page is already active.
		if ( zIsActive() ) {
			return;
		}

		// Make sure we are logged into the Mobile app
		if ( !((AppAjaxClient)MyApplication).zPageMain.zIsActive() ) {
			((AppAjaxClient)MyApplication).zPageMain.zNavigateTo();
		}

		logger.info("Navigate to "+ this.myPageName());

		// Click on Preferences icon
		if ( !sIsElementPresent(PageMain.Locators.zAppbarPreferences) ) {
			throw new HarnessException("Can't locate preferences icon");
		}

		zClick(PageMain.Locators.zAppbarPreferences);
		SleepUtil.sleepSmall();
		zWaitForElementPresent(Locators.zToolbarSaveID);

		zWaitForActive();

		logger.info("Navigated to "+ this.myPageName() + " page");

	}

	/**
	 * Click "Cancel" to navigate away from preferences
	 * @throws HarnessException 
	 */
	public void zNavigateAway(Button savechanges) throws HarnessException {
		logger.info("zNavigateAway(" + savechanges +")");

		// See also bug 53203

		// Click Cancel
		zToolbarPressButton(Button.B_CANCEL);

		// Check if the "Would you like to save your changes?" appears
		//

		// Wait for the dialog to appear
		SleepUtil.sleep(5000);

		// Check for the dialog
		if ( zIsVisiblePerPosition("id=DWT240", 420, 200) ) {
			logger.debug("zNavigateAway(" + savechanges +") - dialog is showing");

			String locator = null;

			// "Would you like to save your changes?" is displayed.  
			if ( savechanges == Button.B_YES ) {
				locator = Locators.zSaveChangesYes;
			} else if ( savechanges == Button.B_NO ) {
				locator = Locators.zSaveChangesNo;
			} else if ( savechanges == Button.B_CANCEL ) {
				locator = Locators.zSaveChangesCancel;
			} else {
				throw new HarnessException("zNavigateAway() not defined for button "+ savechanges);
			}

			if ( locator == null ) {
				throw new HarnessException("zNavigateAway() no locator for button "+ savechanges);
			}

			if ( !sIsElementPresent(locator) ) {
				throw new HarnessException("zNavigateAway() locator is not present "+ locator);
			}

			zClick(locator);

		} else {
			logger.debug("zNavigateAway(" + savechanges +") - dialog did not show");
		}


	}


	/**
	 * Determine if a checkbox is checked or not
	 * @param preference the Account preference to check
	 * @return true if checked, false if not checked
	 * @throws HarnessException
	 */
	public boolean zGetCheckboxStatus(String preference) throws HarnessException {
		logger.info("zGetCheckboxStatus(" + preference +")");

		String locator = null;

		if ( preference.equals("zimbraPrefIncludeSpamInSearch")) {

			locator = "css=input[id$=_SEARCH_INCLUDES_SPAM]";

		} else if (preference.equals("zimbraPrefIncludeTrashInSearch")) {

			locator = "css=input[id$=_SEARCH_INCLUDES_TRASH]";

		} else if (preference.equals("zimbraPrefShowSearchString")) {

			locator = "css=input[id$=_SHOW_SEARCH_STRING]";

		} else if (preference.equals("zimbraPrefAutoAddAddressEnabled")) {

			locator = "css=input[id$=_AUTO_ADD_ADDRESS]";

		}  else if (preference.equals("zimbraPrefAutocompleteAddressBubblesEnabled")) {

			locator = "css=input[id$=_USE_ADDR_BUBBLES]";

		}else {
			throw new HarnessException("zGetCheckboxStatus() not defined for preference "+ preference);
		}

		if ( !sIsElementPresent(locator) ) {
			throw new HarnessException("locator not present "+ locator);
		}

		boolean checked = sIsChecked(locator);
		logger.info("zGetCheckboxStatus(" + preference +") = "+ checked);

		return (checked);

	}

	@Override
	public AbsPage zListItem(Action action, String item) throws HarnessException {
		throw new HarnessException(myPageName() + " does not have a Toolbar");
	}

	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption ,String item)
			throws HarnessException {

		throw new HarnessException("Not applicaple for Preference");
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String item) throws HarnessException {
		throw new HarnessException(myPageName() + " does not have a Toolbar");
	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton("+ button +")");

		tracer.trace("Click button "+ button);

		if ( button == null )
			throw new HarnessException("Button cannot be null!");


		// Default behavior variables
		//
		String locator = null;			// If set, this will be clicked
		AbsPage page = null;	// If set, this page will be returned

		// Based on the button specified, take the appropriate action(s)
		//

		if ( button == Button.B_SAVE ) {

			locator = "id="+ Locators.zToolbarSaveID;
			page = null;

		} else if ( button == Button.B_CANCEL ) {

			locator = "id="+ Locators.zToolbarCancelID;
			page = null;

		} else if ( button == Button.B_CHANGE_PASSWORD ) {

			//	locator = "css=td[id='CHANGE_PASSWORD_title']";
			locator = Locators.zChangePwdButton;
			page = new SeparateWindowChangePassword(MyApplication);

		} else if ( button == Button.B_NEW_FILTER ) {

			locator = "css=div[id='zb__FRV__ADD_FILTER_RULE'] td[id$='_title']";
			page = new DialogEditFilter(MyApplication,((AppAjaxClient) MyApplication).zPagePreferences);

		} else if ( button == Button.B_ACTIVITY_STREAM_SETTINGS ) {

			locator = "css=div[id$='_ACTIVITY_STREAM_BUTTON'] td[id$='_title']";
			page = new DialogActivityStream(MyApplication,((AppAjaxClient) MyApplication).zPagePreferences);

		} else if ( button == Button.B_NEW_QUICK_COMMAND ) {

			locator = "css=div[id='zb__QCV__ADD_QUICK_COMMAND'] td[id$='_title']";
			page = new DialogEditQuickCommand(MyApplication,((AppAjaxClient) MyApplication).zPagePreferences);

		} else if ( button == Button.B_EDIT_QUICK_COMMAND ) {

			locator = "css=div[id='zb__QCV__EDIT_QUICK_COMMAND'] td[id$='_title']";
			page = new DialogEditQuickCommand(MyApplication,((AppAjaxClient) MyApplication).zPagePreferences);

		} else if ( button == Button.B_DELETE_QUICK_COMMAND ) {

			locator = "css=div[id='zb__QCV__REMOVE_QUICK_COMMAND'] td[id$='_title']";
			page = new DialogWarning(DialogWarningID.QuickCommandConfirmDelete, MyApplication,((AppAjaxClient) MyApplication).zPagePreferences);

		} else {
			throw new HarnessException("no logic defined for button "+ button);
		}

		// Make sure the button exists
		if ( !this.sIsElementPresent(locator) )
			throw new HarnessException("Button is not present locator="+ locator +" button="+ button);

		// Click it
		this.zClick(locator);

		this.zWaitForBusyOverlay();

		if ( page != null ) {
			page.zWaitForActive();
			page.zWaitForBusyOverlay();
		}

		SleepUtil.sleepMedium();

		return (page);
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		tracer.trace("Click pulldown "+ pulldown +" then "+ option);
		AbsPage page = null;	// If set, this page will be returned

		// Default behavior
		if ( pulldown != null ) {
			if ( pulldown == Button.O_START_WEEK_ON ){

				// Make sure the locator exists
				if ( !this.sIsElementPresent(Locators.zStartWeekOn) ) {
					throw new HarnessException("pulldownLocator not present! "+ Locators.zStartWeekOn);
				}

				this.zClick(Locators.zStartWeekOn);

				this.zWaitForBusyOverlay();

				if ( option != null ) {

					if ( option == Button.O_START_WEEK_ON_TUESDAY ){
						// Make sure the locator exists
						if ( !this.sIsElementPresent(Locators.zStartWeekOnTuesday) ) {
							throw new HarnessException("optionLocator not present! "+ Locators.zStartWeekOnTuesday);
						}

						this.zClick(Locators.zStartWeekOnTuesday);

						this.zWaitForBusyOverlay();
					}
				}
			}

			if ( pulldown == Button.O_DEFAULT_APPOINTMENT_DURATION ){

				// Make sure the locator exists
				if ( !this.sIsElementPresent(Locators.zDefaultAppointmentDuration) ) {
					throw new HarnessException("pulldownLocator not present! "+ Locators.zDefaultAppointmentDuration);
				}

				this.zClick(Locators.zDefaultAppointmentDuration);

				this.zWaitForBusyOverlay();

				if ( option != null ) {

					if ( option == Button.O_APPOINTMENT_DURATION_90 ){
						// Make sure the locator exists
						if ( !this.sIsElementPresent(Locators.zAppointmentDuration90) ) {
							throw new HarnessException("optionLocator not present! "+ Locators.zAppointmentDuration90);
						}

						this.zClick(Locators.zAppointmentDuration90);

						this.zWaitForBusyOverlay();
					}
				}
			}

		}
		return (page);

	}


	public AbsPage zFolderPressPulldown(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton("+ button +")");

		tracer.trace("Click button "+ button);


		// Default behavior variables
		//
		//String locator = null;			// If set, this will be clicked
		AbsPage page = null;	// If set, this page will be returned

		// Default behavior
		if ( button != null ) {
			if ( button == Button.O_SHARE_FOLDER_TYPE ){

				// Make sure the locator exists
				if ( !this.sIsElementPresent(Locators.zShareFolderType) ) {
					throw new HarnessException("pulldownLocator not present! "+ Locators.zShareFolderType);
				}

				this.zClick(Locators.zShareFolderType);
				this.zWaitForBusyOverlay();
				SleepUtil.sleepSmall();
			}		
		}
		return page;
	}

	public void zSelectRadioButton (Button option) throws HarnessException {

		if ( option == null )
			throw new HarnessException("Option cannot be null!");

		String locator = null;

		if ( option == Button.R_CUSTOM_WORK_HOURS ) {

			locator = Locators.zCustomWorkHours;

		} else {
			throw new HarnessException("no logic defined for option "+ option);
		}

		if ( locator == null ) {
			throw new HarnessException("locator was null for option "+ option);
		}

		this.sClickAt(locator, "");
		this.zWaitForBusyOverlay();

	}

	public void zSelectCheckBox (Button option) throws HarnessException {

		if ( option == null )
			throw new HarnessException("Option cannot be null!");

		String locator = null;

		if ( option == Button.B_MONDAY_CHECK_BOX ) {

			locator = Locators.zMondayCustomWorkHour;

		} else {
			throw new HarnessException("no logic defined for option "+ option);
		}

		if ( locator == null ) {
			throw new HarnessException("locator was null for option "+ option);
		}

		this.sClickAt(locator, "");
		this.zWaitForBusyOverlay();

	}	

	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton("+ button +")");

		tracer.trace("Click button "+ button);

		if ( button == null )
			throw new HarnessException("Button cannot be null!");

		String locator = null;
		AbsPage page = null;

		if ( button == Button.B_CUSTOMIZE ) {

			locator = Locators.zCustomizeButton;
			page = null;

		} else if ( button == Button.B_YES ) {

			locator = Locators.zYesButtonWarningDialog;
			page = null;

		} else if ( button == Button.B_OK ) {

			locator = Locators.zOKButtonCustomDialog;
			page = null;

		} else if ( button == Button.B_CANCEL ) {

			locator = Locators.zCancelButtonCustomDialog;
			page = null;

		} else if ( button == Button.B_NO ) {

			locator = Locators.zNoButtonWarningDialog;
			page = null;

		} else if ( button == Button.B_IMPORT ) {

			locator = Locators.zImportButton;
			page = null;

		}  else if ( button == Button.B_IMPORT_OK ) {

			locator = Locators.zImportDialogOkButton;
			page = null;

		} else {
			throw new HarnessException("no logic defined for button "+ button);
		}

		if ( locator == null ) {
			throw new HarnessException("locator was null for button "+ button);
		}

		// Make sure the button exists
		if ( !this.sIsElementPresent(locator) )
			throw new HarnessException("Button is not present locator="+ locator +" button="+ button);

		// Click it
		this.zClick(locator);

		this.zWaitForBusyOverlay();

		return (page);
	}

	public void zCheckboxSet(Button checkbox, boolean status) throws HarnessException {

		logger.info(myPageName() + " zPressButton("+ checkbox +")");

		tracer.trace("Click button "+ checkbox);

		if ( checkbox == null )
			throw new HarnessException("Button cannot be null!");

		String locator = null;

		if ( checkbox == Button.C_SUNDAY_WORK_HOUR ) {

			locator = Locators.zSundayCustomWorkHour;

		} else if ( checkbox == Button.C_MONDAY_WORK_HOUR ) {

			locator = Locators.zMondayCustomWorkHour;

		} else if ( checkbox == Button.C_TUESDAY_WORK_HOUR ) {

			locator = Locators.zTuesdayCustomWorkHour;

		} else if ( checkbox == Button.C_WEDNESDAY_WORK_HOUR) {

			locator = Locators.zWednesdayCustomWorkHour;

		} else if ( checkbox == Button.C_THURSDAY_WORK_HOUR ) {

			locator = Locators.zThursdayCustomWorkHour;

		} else if ( checkbox == Button.C_FRIDAY_WORK_HOUR ) {

			locator = Locators.zFridayCustomWorkHour;

		} else if ( checkbox == Button.C_SATURDAY_WORK_HOUR ) {

			locator = Locators.zSaturdayCustomWorkHour;

		} else if ( checkbox == Button.C_MONDAY_WORK_WEEK ) {

			locator = Locators.zMondayWorkWeek;

		}else {
			throw new HarnessException("no logic defined for checkbox "+ checkbox);
		}

		if ( locator == null ) {
			throw new HarnessException("locator was null for checkbox "+ checkbox);
		}

		// Make sure the button exists
		if ( !this.sIsElementPresent(locator) )
			throw new HarnessException("Button is not present checkbox="+ locator +" button="+ checkbox);

		if ( status == true ) {
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
			return (String.format("name:%s with:%s item:%s type:%s role:%s folder:%s email:%s", name, with, item, type, role, folder, email));
		}

	}

	// See https://bugzilla.zimbra.com/show_bug.cgi?id=65919
	// parseUnacceptedShareItem and parseAcceptedShareItem can
	// likely be combined once unique ID's are added to the DOM
	//

	protected ShareItem parseUnacceptedShareItem(String itemLocator) throws HarnessException {
		logger.info(myPageName() + " parseUnacceptedShareItem("+ itemLocator +")");


		if ( !this.sIsElementPresent(itemLocator) ) {
			throw new HarnessException("item is not present! "+ itemLocator);
		}

		/**

			<!-- Unaccepted -->
			<tr id="DWT398">
				<td width="180">enus13186341202964</td>
				<td id="DWT397_it" width="auto">/Inbox/ownerfolder13186341358436</td>
				<td width="60">Folder</td>
				<td id="DWT397_ro" width="50">Viewer</td>
				<td width="180">
					<a href="javascript:;" onclick='ZmSharingView._handleAcceptLink("DWT397");'>Accept</a>
				</td>
				<td width="180">enus13186341276255@testdomain.com</td>
			</tr>

		 */

		String locator = null;

		ShareItem item = new ShareItem();

		locator = itemLocator + " td";
		if ( this.sIsElementPresent(locator) ) {
			item.name = this.sGetText(locator);
		}

		locator = itemLocator + " td[id$='_it']";
		if ( this.sIsElementPresent(locator) ) {
			item.item = this.sGetText(locator);
		}

		locator = itemLocator + " td + td + td";
		if ( this.sIsElementPresent(locator) ) {
			item.type = this.sGetText(locator);
		}

		locator = itemLocator + " td[id$='_ro']";
		if ( this.sIsElementPresent(locator) ) {
			item.role = this.sGetText(locator);
		}

		locator = itemLocator + " td + td + td + td + td + td";
		if ( this.sIsElementPresent(locator) ) {
			item.email = this.sGetText(locator);
		}


		return (item);
	}

	protected ShareItem parseAcceptedShareItem(String itemLocator) throws HarnessException {
		logger.info(myPageName() + " parseAcceptedShareItem("+ itemLocator +")");


		if ( !this.sIsElementPresent(itemLocator) ) {
			throw new HarnessException("item is not present! "+ itemLocator);
		}

		/**

			<!-- Accepted -->
			<tr id="DWT334">
				<td width="180">enus13186341202964</td>
				<td id="DWT333_it" width="auto">/Inbox/ownerfolder13186341358436</td>
				<td width="60">Folder</td>
				<td id="DWT333_ro" width="50">Viewer</td>
				<td id="DWT333_fo" width="150">mountpoint13186341362347</td>
				<td width="180">enus13186341276255@testdomain.com</td>
			</tr>

		 */

		String locator = null;

		ShareItem item = new ShareItem();

		locator = itemLocator + " td";
		if ( this.sIsElementPresent(locator) ) {
			item.name = this.sGetText(locator);
		}

		locator = itemLocator + " td[id$='_it']";
		if ( this.sIsElementPresent(locator) ) {
			item.item = this.sGetText(locator);
		}

		locator = itemLocator + " td + td + td";
		if ( this.sIsElementPresent(locator) ) {
			item.type = this.sGetText(locator);
		}

		locator = itemLocator + " td[id$='_ro']";
		if ( this.sIsElementPresent(locator) ) {
			item.role = this.sGetText(locator);
		}

		locator = itemLocator + " td[id$='_fo']";
		if ( this.sIsElementPresent(locator) ) {
			item.folder = this.sGetText(locator);
		}

		locator = itemLocator + " td + td + td + td + td + td";
		if ( this.sIsElementPresent(locator) ) {
			item.email = this.sGetText(locator);
		}


		return (item);
	}


	protected ShareItem parseSharedByMeShareItem(String itemLocator) throws HarnessException {
		logger.info(myPageName() + " parseAcceptedShareItem("+ itemLocator +")");


		if ( !this.sIsElementPresent(itemLocator) ) {
			throw new HarnessException("item is not present! "+ itemLocator);
		}

		/**

			<!-- Shared By Me -->
			<tr id="DWT435">
				<td width="180">admin@server</td>
				<td id="DWT434_it" width="auto">Inbox</td>
				<td width="60">Mail Folder</td>
				<td id="DWT434_ro" width="50">Viewer</td>
				<td width="180">
					<a href="javascript:;" onclick='ZmSharingView._handleShareAction("DWT434", "_handleEditShare");'>Edit</a>
					<a href="javascript:;" onclick='ZmSharingView._handleShareAction("DWT434", "_handleRevokeShare");'>Revoke</a>
					<a href="javascript:;" onclick='ZmSharingView._handleShareAction("DWT434", "_handleResendShare");'>Resend</a>
				</td>
			</tr>

		 */

		String locator = null;

		ShareItem item = new ShareItem();

		locator = itemLocator + " td";
		if ( this.sIsElementPresent(locator) ) {
			item.with = this.sGetText(locator);
		}

		locator = itemLocator + " td[id$='_it']";
		if ( this.sIsElementPresent(locator) ) {
			item.item = this.sGetText(locator);
		}

		locator = itemLocator + " td + td + td";
		if ( this.sIsElementPresent(locator) ) {
			item.type = this.sGetText(locator);
		}

		locator = itemLocator + " td[id$='_ro']";
		if ( this.sIsElementPresent(locator) ) {
			item.role = this.sGetText(locator);
		}		

		return (item);
	}

	/**
	 * Get a list of share rows from the Preferences->Sharing page
	 * @throws HarnessException 
	 */
	public List<ShareItem> zSharesGetUnaccepted() throws HarnessException {
		logger.info(myPageName() + " zSharesGetUnaccepted()");

		List<ShareItem> items = new ArrayList<ShareItem>();


		String rowsLocator = "css=div[id='zl__SVP__rows'] tr";
		if ( !this.sIsElementPresent(rowsLocator) ) {
			logger.info("No rows - return empty list");
			return (items);
		}

		int count = this.sGetCssCount(rowsLocator);
		for (int i = 0; i < count; i++) {
			String itemLocator = "css=div[id='zl__SVP__rows'] tr"+ StringUtils.repeat(" + tr", i);
			ShareItem item = parseUnacceptedShareItem(itemLocator);
			items.add(item);
		}

		return (items);

	}

	/**
	 * Get a list of share rows from the Preferences->Sharing page
	 * @throws HarnessException 
	 */
	public List<ShareItem> zSharesGetAccepted() throws HarnessException {
		logger.info(myPageName() + " zSharesGetUnaccepted()");

		List<ShareItem> items = new ArrayList<ShareItem>();


		String rowsLocator = "css=div[id='zl__SVM__rows'] tr";
		if ( !this.sIsElementPresent(rowsLocator) ) {
			logger.info("No rows - return empty list");
			return (items);
		}

		int count = this.sGetCssCount(rowsLocator);
		for (int i = 0; i < count; i++) {
			String itemLocator = "css=div[id='zl__SVM__rows'] tr"+ StringUtils.repeat(" + tr", i);
			ShareItem item = parseAcceptedShareItem(itemLocator);
			items.add(item);
		}

		return (items);

	}

	/**
	 * Get a list of share rows from the Preferences->Sharing page
	 * @throws HarnessException 
	 */
	public List<ShareItem> zSharesGetSharedByMe() throws HarnessException {
		logger.info(myPageName() + " zSharesGetUnaccepted()");

		List<ShareItem> items = new ArrayList<ShareItem>();


		String rowsLocator = "css=div[id='zl__SVP__rows'] tr";
		if ( !this.sIsElementPresent(rowsLocator) ) {
			logger.info("No rows - return empty list");
			return (items);
		}

		int count = this.sGetCssCount(rowsLocator);
		for (int i = 0; i < count; i++) {
			String itemLocator = "css=div[id='zl__SVG__rows'] tr"+ StringUtils.repeat(" + tr", i);
			ShareItem item = parseSharedByMeShareItem(itemLocator);
			items.add(item);
		}

		return (items);

	}

	public void zSetEmailAddress(String email) throws HarnessException {
		logger.info(myPageName() + " zSetEmailAddress(" + email + ")");

		String locator = "css=input#ShareDialog_grantee";

		// Make sure the locator exists
		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("zSetEmailAddress " + locator + " is not present");
		}

		// Seems that the client can't handle filling out the new mail form too quickly
		// Click in the "To" fields, etc, to make sure the client is ready
		this.sFocus(locator);
		this.zClick(locator);
		this.zWaitForBusyOverlay();

		//this.zKeyboard.zTypeCharacters(email);
		this.sType(locator, email);
		SleepUtil.sleepSmall();
		this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
		SleepUtil.sleepSmall();
		this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_TAB);
		SleepUtil.sleepMedium();
		this.zWaitForBusyOverlay();

	}


	public AbsPage zClickButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zClickButton("+ button +")");

		String locator = null;

		if ( button == Button.B_OK ) {

			locator = "css=div[id='"+ Locators.zDialogShareId +"'] td[id^='OK'] td[id$='_title']";

		} else if ( button == Button.B_CANCEL ) {

			locator = "css=div[id='"+ Locators.zDialogShareId +"'] td[id^='Cancel'] td[id$='_title']";

		} else {
			throw new HarnessException("Button "+ button +" not implemented");
		}

		this.zClick(locator);
		SleepUtil.sleepSmall();
		zWaitForBusyOverlay();

		// This dialog sends a message, so we need to check the queue
		Stafpostqueue sp = new Stafpostqueue();
		sp.waitForPostqueue();

		return (null);

	}

	public void zSetRole(ShareRole role) throws HarnessException {
		logger.info(myPageName() + " zSetRole("+ role +")");
		String locator =null;
		if(role== ShareRole.Admin){
			locator = "//div[@id='"+ Locators.zDialogShareId +"']//div[contains(@id,'_content')]//div/fieldset/div/table/tbody/tr[4]/td/input[contains(@id,'ShareRole_ADMIN')]";
		}else if (role== ShareRole.Manager){
			locator = "//div[@id='"+ Locators.zDialogShareId +"']//div[contains(@id,'_content')]//div/fieldset/div/table/tbody/tr[3]/td/input[contains(@id,'ShareRole_MANAGER')]";
		}else{
			throw new HarnessException("zSetRole "+ locator +" is not present");
		}
		this.sFocus(locator);
		this.sClick(locator);
		SleepUtil.sleepMedium();

		//this.sCheck(locator);
	}
}
