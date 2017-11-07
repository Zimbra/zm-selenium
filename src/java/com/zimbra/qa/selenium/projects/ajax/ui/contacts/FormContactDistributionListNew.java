/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.ui.contacts;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.ui.*;

public class FormContactDistributionListNew extends AbsForm {

	public static final String SELECT_OPTION_TEXT_GAL = "Global Address List";
	public static final String SELECT_OPTION_TEXT_CONTACTS = "Contacts";
	public static final String SELECT_OPTION_TEXT_SHARED_CONTACTS = "Personal and Shared Contacts";

	public static class Locators {

		public static final String zNewContactGroupMenuIconBtn = "css=id=^_left_icon div[class=ImgNewGroup]";
		public static String zActiveEditForm = "";

		public static String zGroupnameField = " div.companyName input[id$='_groupName']";
		public static String zGroupAddNewTextArea = " textarea[id$='_addNewField']";
		public static String zAddNewButton = " td[id$='_addNewButton'] td[id$='_title']";
		public static String zAddButton = " td[id$='_addButton'] td[id$='_title']";
		public static String zAddAllButton = " td[id$='_addAllButton'] td[id$='_title']";
		public static String zPrevButton = " td[id$='_prevButton'] td[id$='_title']";
		public static String zNextButton = " td[id$='_nextButton'] td[id$='_title']";

		public static String zFindField = " input[id$='_searchField']";
		public static String zSearchButton = " td[id$='_searchButton'][id^='DWT'] td[id$='_title']";
		public static String zSearchDropdown = " td[id$='_listSelect'] td[id$='_select_container'] ";
		public static String zFolderDropdown = ">table.contactHeaderTable td[id$='_title']";

		public static final String SAVE = "css=[id^=zb__CN][id$=__SAVE_left_icon]";
		public static final String CANCEL = "css=[id^=zb__CN][id$=__CANCEL]";

		public static final String zDropdownSelectContacts = "css=DYNAMIC_ID";
		public static final String zDropdownSelectSharedContacts = "css=DYNAMIC_ID";
		public static final String zDropdownSelectGAL = "css=DYNAMIC_ID";

		public static String zListView = " div[id$='_listView'] div#z1__GRP__rows";
		public static String zEmailView = " div[id$='_groupMembers'] div#z1__GRP__rows";

		public static String zDeleteAllButton = " td[id$='_delAllButton'] td[id$='_title']";
		public static String zDeleteButton = " td[id$='_delButton'] td[id$='_title']";
	}

	public static class Field {

		public static final Field DistributionListName = new Field("DistributionListName");
		public static final Field SearchField = new Field("SearchField");
		public static final Field CommaSeparatedEmailsField = new Field("CommaSeparatedEmailsField");
		public static final Field FreeFormAddress = new Field("FreeFormAddress");

		public static final Field DistributionListDisplayName = new Field("DistributionListDisplayName");

		private String field;

		private Field(String name) {
			field = name;
		}

		@Override
		public String toString() {
			return (field);
		}

	}

	protected String MyDivID = null;

	public FormContactDistributionListNew(AbsApplication application) {
		super(application);

		logger.info("new " + FormContactDistributionListNew.class.getCanonicalName());

	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public void zSubmit() throws HarnessException {
		this.zToolbarPressButton(Button.B_SAVE);
		SleepUtil.sleepLong();
	}

	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		tracer.trace("Click button " + button);

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		AbsPage page = null;
		String locator = null;

		if (button == Button.B_SAVE) {

			locator = "css=div#" + getToolbarID() + " div[id$='__SAVE'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_CLOSE) {

			locator = "css=div#" + getToolbarID() + " div[id$='__CANCEL'] td[id$='_title']";
			page = null;

			this.sClickAt(locator, "");

			return (page);

		} else if (button == Button.B_DELETE) {

			locator = "css=div#" + getToolbarID() + " div[id$='__DELETE'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_CHOOSE_ADDRESSBOOK) {

			locator = "css=div#" + MyDivID + " td[id$='_LOCATION_FOLDER'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_DISTRIBUTIONLIST_ADD_ADDRESS) {

			locator = "css=div#" + MyDivID + " td[id$='_addNewButton'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_DISTRIBUTIONLIST_ADD_SEARCH_RESULT) {

			locator = "css=div#" + MyDivID + " td[id$='_addButton'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_DISTRIBUTIONLIST_ADD_ALL_SEARCH_RESULT) {

			locator = "css=div#" + MyDivID + " td[id$='_addAllButton'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_SEARCH) {

			locator = "css=div#" + MyDivID + " td[id$='_searchButton'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_ADD) {

			locator = "css=td[id$='_addButton'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_ADD_NEW) {

			locator = "css=td[id$='_addNewButton'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_ADD_ALL) {

			locator = "css=td[id$='_addAllButton'] td[id$='_title']";
			page = null;

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		this.sClickAt(locator, "");

		SleepUtil.sleepMedium();

		this.zWaitForBusyOverlay();

		return (page);

	}

	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {

		logger.info(myPageName() + " zToolbarPressPulldown(" + pulldown + ", " + option + ")");

		tracer.trace("Click pulldown " + pulldown + " then " + option);

		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");

		if (option == null)
			throw new HarnessException("Option cannot be null!");

		String pulldownLocator = null;
		String optionLocator = null;

		if (pulldown == Button.B_DISTRIBUTIONLIST_SEARCH_TYPE) {

			pulldownLocator = "css=td[id$='_listSelect'] td[id$='_title']:contains('Global Address List')";
			if (!this.sIsElementPresent(pulldownLocator)) {
				pulldownLocator = "css=td[id$='_listSelect'] td[id$='_title']:contains('Contacts')";
			}

			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " option " + option + " pulldownLocator "
						+ pulldownLocator + " not present!");
			}

			this.sClickAt(pulldownLocator, "");
			this.zWaitForBusyOverlay();

			if (option == Button.O_DISTRIBUTIONLIST_SEARCH_GAL) {
				if (sIsElementPresent("css=td[id$='Menu_2_option_3_title']:contains('Global Address List')")) {
					optionLocator = "css=td[id$='Menu_2_option_3_title']:contains('Global Address List')";
				} else {
					optionLocator = "css=td[id$='Menu_1_option_3_title']:contains('Global Address List')";
				}

			} else if (option == Button.O_DISTRIBUTIONLIST_SEARCH_CONTACTS) {
				if (sIsElementPresent("css=td[id$='Menu_2_option_1_title']:contains('Contacts')")) {
					optionLocator = "css=td[id$='Menu_2_option_1_title']:contains('Contacts')";
				} else {
					optionLocator = "css=td[id$='Menu_1_option_1_title']:contains('Contacts')";
				}

			} else if (option == Button.O_DISTRIBUTIONLIST_SEARCH_PERSONAL_AND_SHARED) {
				if (sIsElementPresent("css=td[id$='Menu_2_option_2_title']:contains('Personal and Shared Contacts')")) {
					optionLocator = "css=td[id$='Menu_2_option_2_title']:contains('Personal and Shared Contacts')";
				} else {
					optionLocator = "css=td[id$='Menu_1_option_2_title']:contains('Personal and Shared Contacts')";
				}

			} else {
				throw new HarnessException("implement " + pulldown + " and " + option);
			}

			if (!this.sIsElementPresent(optionLocator)) {
				throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator "
						+ optionLocator + " not present!");
			}

			this.sClickAt(optionLocator, "");
			this.zWaitForBusyOverlay();
			SleepUtil.sleepLong();

			return (null);
		}

		throw new HarnessException("no logic defined for pulldown " + pulldown);

	}

	public static String getLocator(String locator) {
		if (locator.startsWith("css=")) {
			locator = locator.substring(locator.indexOf(" "));
		}

		return "css=div#" + Locators.zActiveEditForm + locator;
	}

	public void zFillField(Field field, String value) throws HarnessException {

		tracer.trace("Set " + field + " to " + value);

		String locator = "css=div#" + MyDivID;

		if (field == Field.DistributionListName) {

			locator = "css=div[id$='_groupNameParent'] input[id$='_groupName']";
			clearField(locator);
			sType(locator, value);
			return;

		} else if (field == Field.SearchField) {
			locator += "  table.ZPropertySheet input[id$='_searchField']";

		} else if (field == Field.CommaSeparatedEmailsField) {
			locator = "css=textarea[id$='addNewField']";

		} else if (field == Field.FreeFormAddress) {

			locator += " table.ZPropertySheet textarea[id$='_addNewField']";

			this.sType(locator, "");
			this.sType(locator, value);
			this.zWaitForBusyOverlay();

			this.zToolbarPressButton(Button.B_DISTRIBUTIONLIST_ADD_ADDRESS);
			this.zWaitForBusyOverlay();

			return;

		} else if (field == Field.DistributionListDisplayName) {
			locator = "css=input[id$='_dlDisplayName']";

		} else {
			throw new HarnessException("not implemented for field " + field);
		}

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("Field is not present field=" + field + " locator=" + locator);

		this.sClickAt(locator, "");
		this.sType(locator, value);

		this.zWaitForBusyOverlay();

	}

	@Override
	public void zFill(IItem item) throws HarnessException {
		logger.info("FormContactGroupNew.fill(IItem)");
		logger.info(item.prettyPrint());

		// Make sure the item is a ContactGroupItem
		if (!(item instanceof ContactGroupItem)) {
			throw new HarnessException("Invalid item type - must be ContactGroupItem");
		}

		// Convert object to ContactGroupItem
		ContactGroupItem group = (ContactGroupItem) item;

		// Fill out the form

		// Fill out the name
		if ((group.getName() == null) || (group.getName().trim().length() == 0)) {
			throw new HarnessException("Empty group name - group name is required");
		}
		zFillField(Field.DistributionListName, group.getName());

		// Add addresses
		if (group.getMemberList().size() == 0) {
			throw new HarnessException("Empty group members - group members are required");
		}

		for (ContactGroupItem.MemberItem m : group.getMemberList()) {

			// Depending on the type, fill out the form appropriately.

			if (m instanceof ContactGroupItem.MemberItemAddress) {
				zFillField(Field.FreeFormAddress, m.getValue());
			} else if (m instanceof ContactGroupItem.MemberItemContact) {
				throw new HarnessException("implement me!");
			} else if (m instanceof ContactGroupItem.MemberItemGAL) {
				throw new HarnessException("implement me!");
			} else {
				throw new HarnessException("implement me!");
			}

		}

	}

	public boolean zIsListGroupEmpty() throws HarnessException {
		return sIsElementPresent(
				getLocator(" div#[id$=_listView].groupMembers div[id^=zl__DWT][id$=__rows] td.NoResults"));
	}

	public ArrayList<ContactItem> zListGetMembers() throws HarnessException {
		throw new HarnessException("implement me");
	}

	public ArrayList<ContactItem> zListGetSearchResults() throws HarnessException {
		logger.info("zListGetSearchResults()");

		String locator = null;
		ArrayList<ContactItem> items = new ArrayList<ContactItem>();

		int count = this
				.sGetCssCount("css=div#" + MyDivID + " div[id$='_listView'] div[id$='__rows'] div[id^='zli__']");
		for (int i = 1; i <= count; i++) {

			locator = "css=div#" + MyDivID + " div[id$='_listView'] div[id$='__rows']>div:nth-child(" + i + ")";

			String name = this.sGetText(locator + " td:nth-child(2)");
			String email = this.sGetText(locator + " td:nth-child(3)");

			ContactItem item = new ContactItem();
			item.setAttribute("fileAs", name);
			item.setAttribute("email", email);

			logger.info(item.prettyPrint());
			items.add(item);

		}

		return items;
	}

	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		if (MyDivID == null) {

			// Determine which ZmContactView div is visible (if any)
			String locator = "//div[@id='z_shell']/div[contains(@class, 'ZmContactView')]";
			int count = this.sGetXpathCount(locator);

			for (int i = 1; i <= count; i++) {
				String id = this.sGetAttribute(locator + "[" + i + "]@id");
				if (this.zIsVisiblePerPosition("css=div#" + id, 0, 0)) {
					MyDivID = id;
					return (true);
				}
			}

			// No ZmContactView is active
			return (false);
		}

		String locator = "css=div#" + MyDivID;

		boolean present = this.sIsElementPresent(locator);
		if (!present) {
			return (false);
		}

		boolean visible = this.zIsVisiblePerPosition(locator, 0, 0);
		if (!visible) {
			return (false);
		}

		logger.info(myPageName() + " zIsActive() = true");
		return (true);

	}

	public void select(AppAjaxClient app, String dropdown, String option) throws HarnessException {

		String postfix = " td[id$='_dropdown'] div[class='ImgSelectPullDownArrow']";
		String textLocator = " td[id$='_title']";

		if (this.sGetText(dropdown + textLocator).equals(option)) {
			return;
		}

		// select contact dropdown
		zClick(dropdown + postfix);
		SleepUtil.sleepSmall();

		// assume contact is one arrow key down away from top
		// assume shared contact is two arrow key down away from top
		// assume GAL is three arrow key down away from top
		app.zPageContacts.zKeyboardKeyEvent(KeyEvent.VK_DOWN);
		if (!option.equals(SELECT_OPTION_TEXT_CONTACTS)) {
			app.zPageContacts.zKeyboardKeyEvent(KeyEvent.VK_DOWN);
			if (!option.equals(SELECT_OPTION_TEXT_SHARED_CONTACTS)) {
				app.zPageContacts.zKeyboardKeyEvent(KeyEvent.VK_DOWN);
			}
		}

		app.zPageContacts.zKeyboardKeyEvent(KeyEvent.VK_ENTER);

		return;
	}

	private String MyToolbarID = null;

	protected String getToolbarID() throws HarnessException {
		logger.info("getToolbarID()");

		if (MyToolbarID != null) {
			logger.info("getToolbarID() - Re-using " + MyToolbarID);
			return (MyToolbarID);
		}

		String locator = "//div[@id='z_shell']/div[contains(@id, 'ztb__CN-')]";
		int count = this.sGetXpathCount(locator);

		for (int i = 1; i <= count; i++) {
			String id = this.sGetAttribute(locator + "[" + i + "]@id");
			if (this.zIsVisiblePerPosition("css=div#" + id, 0, 0)) {
				MyToolbarID = id;
				return (id);
			}
		}

		throw new HarnessException("Unable to determine the Toolbar ID " + this.sGetHtmlSource());
	}

	private String MySearchPulldownID = null;

	protected String getSearchPulldownID() throws HarnessException {
		logger.info("getSearchPulldownID()");

		if (MySearchPulldownID != null) {
			logger.info("getSearchPulldownID() - Re-using " + MySearchPulldownID);
			return (MySearchPulldownID);
		}

		String locator = "//div[@id='z_shell']/div[contains(@class, 'DwtMenu')]";
		int count = this.sGetXpathCount(locator);

		for (int i = 1; i <= count; i++) {
			String id = this.sGetAttribute(locator + "[" + i + "]@id");
			if (this.zIsVisiblePerPosition("css=div#" + id, 0, 0)) {
				MySearchPulldownID = id;
				return (id);
			}
		}

		throw new HarnessException("Unable to determine the Search Pulldown ID " + this.sGetHtmlSource());
	}
}