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
package com.zimbra.qa.selenium.projects.ajax.pages.mail;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.pages.AjaxPages;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogShare;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogShareRevoke;

/**
 * Represents a "Create New Folder" dialog box
 * 
 * @author Matt Rhoades
 */

public class DialogEditFolder extends AbsDialog {

	public static class Locators {

		public static final String zEditPropertiesDialogId = "ZmFolderPropsDialog";
		public static final String zEditPropertiesDialogDropDown = "css=td[id$='_title']:contains('None')";
		public static final String zGrayColorId = "//td[contains(@id,'_title') and contains(text(),'Gray')]";
		public static final String zBlueColorId = "//td[contains(@id,'_title') and contains(text(),'Blue')]";
		public static final String zCyanColorId = "//td[contains(@id,'_title') and contains(text(),'Cyan')]";
		public static final String zGreenColorId = "//td[contains(@id,'_title') and contains(text(),'Green')]";

		public static final String zPurpleColorId = "//td[contains(@id,'_title') and contains(text(),'Purple')]";
		public static final String zRedColorId = "//td[contains(@id,'_title') and contains(text(),'Red')]";
		public static final String zYellowColorId = "//td[contains(@id,'_title') and contains(text(),'Yellow')]";
		public static final String zPinkColorId = "//td[contains(@id,'_title') and contains(text(),'Pink')]";
		public static final String zOrangeColorId = "//td[contains(@id,'_title') and contains(text(),'Orange')]";
		public static final String zMoreColors = "css= div[id$='SHOW_MORE_ITEMS'] td[id$='SHOW_MORE_ITEMS_title']:contains('More Colors...')";
		public static final String zCustomColors = "css=div[class='DwtMenu'] div[class$='DwtColorPicker'] div[class='Colors'] div:nth-child(4)";
		public static final String zSetCustom = "css=table[class='ZWidgetTable ZButtonTable ZButtonBorder'] td[class='ZWidgetTitle']:contains('Custom')";
		public static final String zEditColor = "css=td[class='Field'] td[class='ZDropDown'] div[class='ImgSelectPullDownArrow']";
		public static final String zExcludeFB = "css=input[id='_excludeFbCheckbox']";
		public static final String ConflictAttendeeNote = "css= div[id$='_attendee_status']:contains('One or more attendees are not available at the selected time.')";

	}

	public DialogEditFolder(AbsApplication application, AbsTab tab) {
		super(application, tab);
		logger.info("new " + DialogEditFolder.class.getCanonicalName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		logger.info(myPageName() + " zIsActive()");

		String locator = "class=" + Locators.zEditPropertiesDialogId;

		if (!this.sIsElementPresent(locator)) {
			return (false);
		}

		if (!this.zIsVisiblePerPosition(locator, 0, 0)) {
			return (false);
		}

		logger.info(myPageName() + " zIsActive() = true");
		return (true);
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");

		tracer.trace("Click dialog button " + button);

		AbsPage page = null;
		String locator = null;

		if (button == Button.B_OK) {

			locator = "//div[@class='" + Locators.zEditPropertiesDialogId
					+ "']//div[contains(@id,'_buttons')]//td[text()='OK']";

		} else if (button == Button.B_CANCEL) {

			locator = "//div[@class='" + Locators.zEditPropertiesDialogId
					+ "']//div[contains(@id,'_buttons')]//td[text()='Cancel']";

		} else if (button == Button.B_SHARE) {

			locator = "//div[@class='" + Locators.zEditPropertiesDialogId
					+ "']//div[contains(@id,'_buttons')]//td[text()=''Add Share...']";

		} else if (button == Button.O_RESEND_LINK) {

			throw new HarnessException("implement me");

		} else if (button == Button.O_REVOKE_LINK) {

			locator = "//div[@class='" + Locators.zEditPropertiesDialogId
					+ "']//div[contains(@id,'_content')]//div/fieldset/div/table/tbody/tr/td/a[contains(text(),'Revoke')]";
			page = new DialogShareRevoke(MyApplication, ((AjaxPages) MyApplication).zPageMail);

			// Click the link
			this.sClick(locator);
			this.zWaitForBusyOverlay();

			// Wait for the Edit dialog to appear
			page.zWaitForActive();
			this.zWaitForBusyOverlay();

			return (page);

		} else if (button == Button.O_EDIT_LINK) {

			locator = "//div[@class='" + Locators.zEditPropertiesDialogId
					+ "']//div[contains(@id,'_content')]//div/fieldset/div/table/tbody/tr/td/a[contains(text(),'Edit')]";
			page = new DialogShare(MyApplication, ((AjaxPages) MyApplication).zPageMail);

			// Click the link
			this.sClick(locator);
			this.zWaitForBusyOverlay();

			// Wait for the Edit dialog to appear
			page.zWaitForActive();
			this.zWaitForBusyOverlay();

			return (page);

		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		this.sClick(locator);
		this.zWaitForBusyOverlay();
		SleepUtil.sleepSmall();

		return (page);
	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		logger.info(myPageName() + " zGetDisplayedText(" + locator + ")");

		if (locator == null)
			throw new HarnessException("locator was null");

		return (this.sGetText(locator));
	}

	public void zSetNewName(String folder) throws HarnessException {
		logger.info(myPageName() + " zEnterFolderName(" + folder + ")");

		tracer.trace("Enter new folder name " + folder);

		if (folder == null)
			throw new HarnessException("folder must not be null");

		String locator = "css=div.DwtDialogBody div input";

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("unable to find folder name field " + locator);

		SleepUtil.sleepSmall();
		this.clearField(locator);
		this.sFocus(locator);
		sType(locator, folder);
		SleepUtil.sleepSmall();
		this.zWaitForBusyOverlay();
	}

	public enum FolderColor {
		None, Blue, Cyan, Green, Purple, Red, Yellow, Pink, Gray, Orange, MoreColors
	}

	public void zSetNewColor(FolderColor color) throws HarnessException {

		logger.info(myPageName() + " zEnterFolderColor(" + color + ")");
		String actionLocator = null;
		String optionLocator = null;
		tracer.trace("Enter folder color " + color);

		if (color == null)
			throw new HarnessException("folder must not be null");

		if (color == FolderColor.MoreColors) {
			actionLocator = Locators.zEditColor;
			optionLocator = Locators.zMoreColors;

			sClickAt(actionLocator, "");
			this.zWaitForBusyOverlay();
			sClick(optionLocator);
			this.zWaitForBusyOverlay();

			optionLocator = Locators.zCustomColors;
			sClick(optionLocator);

		} else if (color == FolderColor.Gray) {

			actionLocator = Locators.zEditPropertiesDialogDropDown;
			optionLocator = Locators.zGrayColorId;

			sClick(actionLocator);
			this.zWaitForBusyOverlay();
			sClick(optionLocator);

		} else if (color == FolderColor.Blue) {

			actionLocator = Locators.zEditPropertiesDialogDropDown;
			optionLocator = Locators.zBlueColorId;

			sClickAt(actionLocator, "");
			this.zWaitForBusyOverlay();
			sClick(optionLocator);

		} else if (color == FolderColor.Cyan) {

			actionLocator = Locators.zEditPropertiesDialogDropDown;
			optionLocator = Locators.zCyanColorId;

			sClick(actionLocator);
			this.zWaitForBusyOverlay();
			sClick(optionLocator);

		} else if (color == FolderColor.Green) {

			actionLocator = Locators.zEditPropertiesDialogDropDown;
			optionLocator = Locators.zGreenColorId;

			sClick(actionLocator);
			this.zWaitForBusyOverlay();
			sClick(optionLocator);

		} else if (color == FolderColor.Purple) {

			actionLocator = Locators.zEditPropertiesDialogDropDown;
			optionLocator = Locators.zPurpleColorId;

			sClick(actionLocator);
			this.zWaitForBusyOverlay();
			sClick(optionLocator);

		} else {
			throw new HarnessException("color " + color + " not yet implemented");
		}

		this.zWaitForBusyOverlay();
	}

	public void zSetNewColor(FolderColor color, AjaxPages app) throws HarnessException {

		logger.info(myPageName() + " zEnterFolderColor(" + color + ")");
		String actionLocator = null;
		String optionLocator = null;
		tracer.trace("Enter folder color " + color);

		if (color == null)
			throw new HarnessException("folder must not be null");

		if (color == FolderColor.Green) {

			actionLocator = Locators.zBlueColorId;
			optionLocator = Locators.zGreenColorId;

			sClick(actionLocator);
			this.zWaitForBusyOverlay();
			sClick(optionLocator);

		} else {
			throw new HarnessException("color " + color + " not yet implemented");
		}

		this.zWaitForBusyOverlay();
	}

	public enum DialogTab {
		Properties, Retention, Disposal, Other
	}

	public void zNavigateToTab(DialogTab tab) throws HarnessException {
		logger.info(myPageName() + " zNavigateToTab(" + tab + ")");

		tracer.trace("Click on dialog tab " + tab);

		String locator = null;

		if (tab == DialogTab.Properties) {
			locator = "css=div[id='FolderProperties'] td[id$='_title']:contains('Properties')";

		} else if (tab == DialogTab.Retention || tab == DialogTab.Disposal) {
			locator = "css=div[id='FolderProperties'] td[id$='_title']:contains('Retention')";

		} else {
			throw new HarnessException("No logic defined for tab = " + tab);
		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Locator not found: " + locator);
		}

		// Click on the tab
		this.sClickAt(locator, "");
		this.zWaitForBusyOverlay();
	}

	public enum RetentionRangeUnits {
		Years, Months, Weeks, Days, Other
	};

	public enum RetentionRangeType {
		Custom, Other
	};

	public void zRetentionSetRange(RetentionRangeType type, RetentionRangeUnits units, int value)
			throws HarnessException {
		logger.info(myPageName() + " zRetentionSetRange(" + type + ", " + units + ", " + value + ")");

		tracer.trace("Set retention range " + type + " " + value + " " + units);

		// Make sure we are on the retention tab
		zNavigateToTab(DialogTab.Retention);

		// Set the values
		zRetentionSetRangeType(type);
		zRetentionSetRangeUnits(units);
		zRetentionSetRangeValue(value);

	}

	public void zRetentionSetRangeType(RetentionRangeType type) throws HarnessException {
		logger.info(myPageName() + " zRetentionSetRangeType(" + type + ")");

		tracer.trace("Set retention range type " + type);

		if (type != RetentionRangeType.Custom) {
			throw new HarnessException("implement me: retention range type: " + type);
		}

	}

	public void zRetentionSetRangeValue(int value) throws HarnessException {
		logger.info(myPageName() + " zRetentionSetRangeValue(" + value + ")");
		tracer.trace("Set retention range value " + value);

		String locator = "css=div[id='FolderProperties'] input[id$='_keepValue']";

		this.clearField(locator);
		this.sType(locator, "" + value);
		this.zWaitForBusyOverlay();
	}

	public void zRetentionSetRangeUnits(RetentionRangeUnits units) throws HarnessException {
		logger.info(myPageName() + " zRetentionSetRangeUnits(" + units + ")");

		tracer.trace("Set retention range units " + units);

		String locator = "css=div[id='FolderProperties'] select[id$='_keepUnit']";
		String option = "value=day";

		switch (units) {
		case Days:
			option = "value=day";
			break;
		case Weeks:
			option = "value=week";
			break;
		case Months:
			option = "value=month";
			break;
		case Years:
			option = "value=year";
			break;
		default:
			throw new HarnessException("Unknown units: " + units);

		}

		// Pulldown
		this.sSelectDropDown(locator, option);
		this.zWaitForBusyOverlay();

	}

	public void zRetentionEnable() throws HarnessException {
		logger.info(myPageName() + " zRetentionEnable()");

		tracer.trace("Enable retention");

		// Make sure we are on the retention tab
		zNavigateToTab(DialogTab.Retention);

		// Check the checkbox
		String locator = "css=div[id='FolderProperties'] input[id$='_keepCheckbox']";

		if (this.sIsChecked(locator)) {
			logger.info("Checkbox already checked");
		} else {
			this.sCheck(locator);
			this.zWaitForBusyOverlay();
		}

	}

	public void zRetentionDisable() throws HarnessException {
		logger.info(myPageName() + " zRetentionDisable()");

		tracer.trace("Disable retention");

		// Make sure we are on the retention tab
		zNavigateToTab(DialogTab.Retention);

		// Uncheck the checkbox
		String locator = "css=div[id='FolderProperties'] input[id$='_keepCheckbox']";

		if (!this.sIsChecked(locator)) {
			logger.info("Checkbox already unchecked");

		} else {
			this.sUncheck(locator);
			this.zWaitForBusyOverlay();
		}

	}

	public void zDisposalSetRange(RetentionRangeType type, RetentionRangeUnits units, int value)
			throws HarnessException {
		logger.info(myPageName() + " zDisposalSetRange(" + type + ", " + units + ", " + value + ")");

		tracer.trace("Set disposal range " + type + " " + value + " " + units);

		// Make sure we are on the retention tab
		zNavigateToTab(DialogTab.Disposal);

		// Set the values
		zDisposalSetRangeType(type);
		zDisposalSetRangeUnits(units);
		zDisposalSetRangeValue(value);
	}

	public void zDisposalSetRangeType(RetentionRangeType type) throws HarnessException {
		logger.info(myPageName() + " zDisposalSetRangeType(" + type + ")");

		tracer.trace("Set disposal range type " + type);

		if (type != RetentionRangeType.Custom) {
			throw new HarnessException("implement me: retention range type: " + type);
		}

	}

	public void zDisposalSetRangeValue(int value) throws HarnessException {
		logger.info(myPageName() + " zDisposalSetRangeValue(" + value + ")");
		tracer.trace("Set disposal range value " + value);

		String locator = "css=div[id='FolderProperties'] input[id$='_purgeValue']";
		this.clearField(locator);
		this.sType(locator, "" + value);
		this.zWaitForBusyOverlay();
	}

	public void zDisposalSetRangeUnits(RetentionRangeUnits units) throws HarnessException {
		logger.info(myPageName() + " zDisposalSetRangeUnits(" + units + ")");

		tracer.trace("Set disposal range units " + units);

		String locator = "css=div[id='FolderProperties'] select[id$='_purgeUnit']";
		String option = "value=day";

		switch (units) {
		case Days:
			option = "value=day";
			break;
		case Weeks:
			option = "value=week";
			break;
		case Months:
			option = "value=month";
			break;
		case Years:
			option = "value=year";
			break;
		default:
			throw new HarnessException("Unknown units: " + units);

		}

		// Pulldown
		this.sSelectDropDown(locator, option);
		this.zWaitForBusyOverlay();
	}

	public void zDisposalEnable() throws HarnessException {
		logger.info(myPageName() + " zDisposalEnable()");

		tracer.trace("Enable disposal");

		// Make sure we are on the retention tab
		zNavigateToTab(DialogTab.Disposal);

		// Check the checkbox
		String locator = "css=div[id='FolderProperties'] input[id$='_purgeCheckbox']";

		if (this.sIsChecked(locator)) {
			logger.info("Checkbox already checked");
		} else {
			this.sCheck(locator);
			this.zWaitForBusyOverlay();
		}
	}

	public void zDisposalDisable() throws HarnessException {
		logger.info(myPageName() + " zDisposalDisable()");

		tracer.trace("Disable disposal");

		// Make sure we are on the retention tab
		zNavigateToTab(DialogTab.Disposal);

		// Uncheck the checkbox
		String locator = "css=div[id='FolderProperties'] input[id$='_purgeCheckbox']";

		if (!this.sIsChecked(locator)) {
			logger.info("Checkbox already unchecked");
		} else {
			this.sUncheck(locator);
			this.zWaitForBusyOverlay();
		}

	}

	public void zExcludeFBEnable() throws HarnessException {
		logger.info(myPageName() + " zDisposalEnable()");
		tracer.trace("Enable Exclude this calendar when reporting free/busy times");

		// Check the checkbox
		if (this.sIsChecked(Locators.zExcludeFB)) {
			logger.info("Checkbox already checked");
		} else {
			this.sCheck(Locators.zExcludeFB);
			this.zWaitForBusyOverlay();
		}

	}

	public void zExcludeFBDisable() throws HarnessException {
		logger.info(myPageName() + " zDisposalDisable()");
		tracer.trace("Disable Exclude this calendar when reporting free/busy times");

		// Uncheck the checkbox
		if (!this.sIsChecked(Locators.zExcludeFB)) {
			logger.info("Checkbox already unchecked");
		} else {
			this.sUncheck(Locators.zExcludeFB);
			this.zWaitForBusyOverlay();
		}

	}
}