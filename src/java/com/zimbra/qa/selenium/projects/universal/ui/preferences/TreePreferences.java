/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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
/**
 * 
 */
package com.zimbra.qa.selenium.projects.universal.ui.preferences;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

public class TreePreferences extends AbsTree {

	public static class Locators {
		public final static String zGeneralTextID = "zti__main_Options__PREF_PAGE_GENERAL_textCell";
		public final static String zGeneralImageID = "zti__main_Options__PREF_PAGE_GENERAL_imageCell";
		public final static String zsignatureTextID = "zti__main_Options__PREF_PAGE_SIGNATURES_textCell";
		public final static String zAddressBookTextID = "zti__main_Options__PREF_PAGE_CONTACTS_textCell";
	}

	public enum TreeItem {
		General, Mail, MailComposing, MailSignatures, MailAccounts, MailFilters, MailOutOfOffice, MailTrustedAddresses, AddressBook, Calendar, SecureEmail, Sharing, Notifications, MobileDevices, ImportExport, Shortcuts, QuickCommands, Zimlets
	}

	public TreePreferences(AbsApplication application) {
		super(application);
		logger.info("new " + TreePreferences.class.getCanonicalName());
	}

	/**
	 * Click on an item in the preference tree
	 * 
	 * @param action
	 * @param item
	 * @throws HarnessException
	 */
	public void zTreeItem(Action action, TreeItem item) throws HarnessException {
		logger.info("zTreeItem(" + action + ", " + item + ")");
		String locator = null;

		if (!itemToLocator.containsKey(item)) {
			throw new HarnessException("locator not defined in itemToLocator for " + item);
		}

		if (itemToLocator.get(item) == null) {
			throw new HarnessException("locator is null in itemToLocator for " + item);
		}

		locator = itemToLocator.get(item);

		if (!sIsElementPresent(locator)) {
			throw new HarnessException("locator is not present " + locator);
		}

		sClickAt(locator, "");
		SleepUtil.sleepLong();
		zWaitForBusyOverlay();

	}

	/**
	 * Not implemented. Use zTreeItem(Action action, TreeItem item) instead
	 */
	@Override
	public AbsPage zTreeItem(Action action, IItem preference) throws HarnessException {
		throw new HarnessException("Not implemented.  Use zTreeItem(Action action, TreeItem item) instead");
	}

	/**
	 * Not implemented. Use zTreeItem(Action action, TreeItem item) instead
	 */
	@Override
	public AbsPage zTreeItem(Action action, Button option, IItem item) throws HarnessException {
		throw new HarnessException("Not applicable.");
	}

	/**
	 * Not implemented. There are no buttons in the preferences tree.
	 */
	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		throw new HarnessException("Not implemented.  There are no buttons in the preferences tree.");
	}

	private static final Map<TreeItem, String> itemToLocator = createItemToLocator();

	private static Map<TreeItem, String> createItemToLocator() {

		Map<TreeItem, String> map = new HashMap<TreeItem, String>();

		map.put(TreeItem.General, "id=" + Locators.zGeneralTextID);
		map.put(TreeItem.Mail, "css=td[id='zti__main_Options__PREF_PAGE_MAIL_textCell']");
		map.put(TreeItem.MailComposing, "css=td[id='zti__main_Options__PREF_PAGE_COMPOSING_textCell']");
		map.put(TreeItem.MailSignatures, "id=" + Locators.zsignatureTextID);
		map.put(TreeItem.MailAccounts, "css=td[id='zti__main_Options__PREF_PAGE_ACCOUNTS_textCell']");
		map.put(TreeItem.MailFilters, "css=td[id='zti__main_Options__PREF_PAGE_FILTERS_textCell']");
		map.put(TreeItem.MailOutOfOffice, "css=td[id='zti__main_Options__PREF_PAGE_OUTOFOFFICE_textCell']");
		map.put(TreeItem.MailTrustedAddresses, "css=td[id='zti__main_Options__PREF_PAGE_TRUSTED_ADDR_textCell']");
		map.put(TreeItem.AddressBook, "id=" + Locators.zAddressBookTextID);
		map.put(TreeItem.Calendar, "css=td[id='zti__main_Options__PREF_PAGE_CALENDAR_textCell']");
		map.put(TreeItem.Sharing, "css=td[id='zti__main_Options__PREF_PAGE_SHARING_textCell']");
		map.put(TreeItem.Notifications, "css=td[id='zti__main_Options__PREF_PAGE_NOTIFICATIONS_textCell']");
		map.put(TreeItem.MobileDevices, "css=td[id='zti__main_Options__PREF_PAGE_MOBILE_textCell']");
		map.put(TreeItem.ImportExport, "css=td[id='zti__main_Options__PREF_PAGE_IMPORT_EXPORT_textCell']");
		map.put(TreeItem.Shortcuts, "css=td[id='zti__main_Options__PREF_PAGE_SHORTCUTS_textCell']");
		map.put(TreeItem.QuickCommands, "css=td[id='zti__main_Options__PREF_PAGE_QUICKCOMMANDS_textCell']");
		map.put(TreeItem.Zimlets, "css=td[id='zti__main_Options__PREF_PAGE_PREF_ZIMLETS_textCell']");
		map.put(TreeItem.SecureEmail, "css=td[id='zti__main_Options__PREF_PAGE_SECURITY_textCell']");

		return (Collections.unmodifiableMap(map));
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		throw new HarnessException("implement me");
	}

}
