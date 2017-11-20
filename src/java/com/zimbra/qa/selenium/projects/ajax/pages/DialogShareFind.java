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
package com.zimbra.qa.selenium.projects.ajax.pages;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsDialog;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;

public class DialogShareFind extends AbsDialog {

	public static class Locators {
		public static final String zDialogLocatorCSS = "css=div[id='ZmShareSearchDialog']";
		public static final String zFolderListTop = zDialogLocatorCSS + " div[class='DwtTreeItemChildDiv']";
		public static final String zEmailInputLocator = "css=input[id='ZmShareSearchView_EMAIL_input']";
		public static final String zButtonSearchLocator = "css=div[id='ZmShareSearchView_SEARCH'] td[id$='_title']";
		public static final String zButtonAddLocator = "css=div[id='ZmShareSearchDialog_buttons'] td[id^='OK_'] td[id$='_title']";
		public static final String zButtonCancelLocator = "css=div[id='ZmShareSearchDialog_buttons'] td[id^='Cancel_'] td[id$='_title']";
	}

	public DialogShareFind(AbsApplication application, AbsTab tab) {
		super(application, tab);
	}

	public static class ShareIncludeType {
		public static ShareIncludeType All = new ShareIncludeType("All");
		public static ShareIncludeType Mail = new ShareIncludeType("Mail");
		public static ShareIncludeType Addressbook = new ShareIncludeType("Addressbook");
		public static ShareIncludeType Calendar = new ShareIncludeType("Calendar");
		public static ShareIncludeType Tasks = new ShareIncludeType("Tasks");
		public static ShareIncludeType Briefcase = new ShareIncludeType("Briefcase");

		protected String ID;

		protected ShareIncludeType(String id) {
			ID = id;
		}

		public String toString() {
			return (ID);
		}
	}

	public void zSetIncludeType(ShareIncludeType type) throws HarnessException {
		logger.info(myPageName() + " zSetIncludeType(" + type + ")");
		throw new HarnessException("implement me!");
	}

	public void zSetFilter(String filter) throws HarnessException {
		logger.info(myPageName() + " zSetFilter(" + filter + ")");
		throw new HarnessException("implement me!");
	}

	public void zSetFindEmail(String email) throws HarnessException {
		logger.info(myPageName() + " zSetFindEmail(" + email + ")");

		String locator = Locators.zEmailInputLocator;

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Locator " + locator + " not present");
		}

		// To activate the Search button, need to focus/click
		this.sFocus(locator);
		this.sClickAt(locator, "");
		this.zKeyboard.zTypeCharacters(email);
		if (!(sGetValue(locator).equalsIgnoreCase(email))) {
			this.sType(locator, email);
		}
		this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_HOME);
		this.zWaitForBusyOverlay();
	}

	public List<String> zListGetFolders() throws HarnessException {
		logger.info(myPageName() + " zListGetFolders()");

		List<String> items = new ArrayList<String>();

		items.addAll(this.zListGetFolders(Locators.zFolderListTop));

		return (items);
	}

	private List<String> zListGetFolders(String top) throws HarnessException {
		List<String> items = new ArrayList<String>();

		String rowLocator = top + ">div";
		int count = this.sGetCssCount(rowLocator);
		for (int i = 1; i <= count; i++) {
			String itemLocator = rowLocator + ":nth-child(" + i + ")";

			String foldername = this.sGetText(itemLocator + " td[id$='_textCell']");
			items.add(foldername);
		}

		return (items);
	}

	private String zGetLocator(String name) throws HarnessException {
		String topLocator = Locators.zFolderListTop;
		String rowLocator = topLocator + ">div";

		int count = this.sGetCssCount(rowLocator);
		for (int i = 1; i <= count; i++) {
			String itemLocator = rowLocator + ":nth-of-type(" + i + ")";

			String foldername = this.sGetText(itemLocator + " td[id$='_textCell']");
			if (foldername.toLowerCase().contains(name.toLowerCase())) {
				return (itemLocator);
			}
		}

		throw new HarnessException("Foldername " + name + " does not exist!");
	}

	@SuppressWarnings("unused")
	public AbsPage zTreeItem(Action action, String folder) throws HarnessException {
		AbsPage page = null;
		String locator = this.zGetLocator(folder);

		String itemLocator = this.zGetLocator(folder);

		if (action == Action.A_TREE_CHECKBOX) {

			// See https://bugzilla.zimbra.com/show_bug.cgi?id=63350
			locator = itemLocator + " div[class='ZTreeItemCheckbox']";
			page = null;

			this.sClick(locator);

			this.zWaitForBusyOverlay();

			return (page);

		} else if (action == Action.A_TREE_COLLAPSE) {

			locator = null;
			page = null;

		} else {
			throw new HarnessException("Action " + action + " not yet implemented");
		}

		if (locator == null)
			throw new HarnessException("locator is null for action " + action);

		sClickAt(locator, "");

		this.zWaitForBusyOverlay();

		if (page != null) {

			page.zWaitForActive();
		}

		return (page);
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");

		String locator = null;
		AbsPage page = null;

		if (button == Button.B_ADD) {

			locator = Locators.zButtonAddLocator;

		} else if (button == Button.B_CANCEL) {

			locator = Locators.zButtonCancelLocator;

		} else if (button == Button.B_SEARCH) {

			locator = Locators.zButtonSearchLocator;

		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (locator == null) {
			throw new HarnessException("Button " + button + " not implemented");
		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Locator " + locator + " not present");
		}

		this.sClickAt(locator, "");

		zWaitForBusyOverlay();

		SleepUtil.sleepLong();

		return (page);
	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		throw new HarnessException("implement me");
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		String locator = Locators.zDialogLocatorCSS;

		if (!this.sIsElementPresent(locator)) {
			return (false);
		}

		if (!this.zIsVisiblePerPosition(locator, 0, 0)) {
			return (false);
		}

		logger.info(myPageName() + " zIsVisible() = true");
		return (true);
	}
}