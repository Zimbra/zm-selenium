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
package com.zimbra.qa.selenium.projects.ajax.ui.calendar;

import java.util.ArrayList;
import java.util.List;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.items.SavedSearchFolderItem;
import com.zimbra.qa.selenium.framework.items.TagItem;
import com.zimbra.qa.selenium.framework.items.ZimletItem;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTree;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.GeneralUtility;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.projects.ajax.ui.*;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogDeleteTag.DialogDeleteTagID;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DialogCreateFolder;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DialogEditFolder;

public class TreeCalendar extends AbsTree {

	public static class Locators {

		public static final String MainDivID = "zov__main_Calendar";
		public static final String CreateNewFolderIconCSS = "css=div[id='" + MainDivID
				+ "'] table[id='ztih__main_Calendar__CALENDAR_table'] td[id$='_title']";
		public static final String CalendarZimletPane = "css=div[id='ztih__main_Calendar__ZIMLET']";
		public static final String RenameTagMenu = "css=div[id='RENAME_TAG'] td[id='RENAME_TAG_title']";
		public static final String DeleteTagMenu = "css=div[id='DELETE_WITHOUT_SHORTCUT'] td[id='DELETE_WITHOUT_SHORTCUT_title']";

	}

	public TreeCalendar(AbsApplication application) {
		super(application);
		logger.info("new " + TreeCalendar.class.getCanonicalName());
	}

	protected AbsPage zTreeItem(Action action, Button option, FolderItem folder) throws HarnessException {

		if ((action == null) || (option == null) || (folder == null)) {
			throw new HarnessException("Must define an action, option, and addressbook");
		}

		SleepUtil.sleepMedium();

		tracer.trace("processing " + folder.getName());

		String actionLocator = String.format("css=div[id='zti__main_Calendar__%s'] td[id$='_textCell']",
				folder.getId());
		String optionLocator = null;
		AbsPage page = null;

		if (folder.getName().equals("USER_ROOT")) {

			if ((action == Action.A_RIGHTCLICK) && (option == Button.O_NEW_CALENDAR || option == Button.O_NEW_FOLDER)) {

				actionLocator = "css=td[id='ztih__main_Calendar__CALENDAR_textCell']";
				optionLocator = "css=table[class$='MenuTable'] td[id$='_title']:contains(New Calendar)";
				page = new DialogCreateFolder(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);

				sRightClick(actionLocator);
				sClick(optionLocator);
				this.zWaitForBusyOverlay();

				if (page != null) {

					page.zWaitForActive();
				}

				return (page);
			}
		}

		GeneralUtility.waitForElementPresent(this, actionLocator);

		optionLocator = "css=div[id='ZmActionMenu_calendar_CALENDAR']";
		if ((action == Action.A_RIGHTCLICK) && (option == Button.B_DELETE)) {

			optionLocator += " div[id^='DELETE_WITHOUT_SHORTCUT'] td[id$='_title']";
			page = null;

			this.sRightClick(actionLocator);

		} else if ((action == Action.A_RIGHTCLICK) && (option == Button.B_TREE_EDIT)) {

			// Use default actionLocator
			optionLocator += " div[id^='EDIT_PROPS'] td[id$='_title']";
			page = new DialogEditFolder(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);

			this.sRightClick(actionLocator);

		} else if ((action == Action.A_RIGHTCLICK) && (option == Button.B_MOVE)) {

			// Use default actionLocator
			optionLocator += " div[id^='MOVE'] td[id$='_title']";
			page = new DialogMove(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);

			this.sRightClick(actionLocator);

		} else if ((action == Action.A_RIGHTCLICK) && (option == Button.B_SHARE)) {

			// Use default actionLocator
			optionLocator += " div[id^='SHARE_CALENDAR'] td[id$='_title']";
			page = new DialogShare(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);

			this.sRightClick(actionLocator);

		} else if ((action == Action.A_RIGHTCLICK) && (option == Button.B_RELOAD)) {

			// Use default actionLocator
			optionLocator += " div[id^='SYNC'] td[id$='_title']";
			page = null;

			this.sRightClick(actionLocator);

		} else if ((action == Action.A_RIGHTCLICK) && (option == Button.B_LAUNCH_IN_SEPARATE_WINDOW)) {

			// Use default actionLocator
			optionLocator += " div[id^='DETACH_WIN'] td[id$='_title']";

			this.sRightClick(actionLocator);
			this.sClickAt(optionLocator, "");

			page = new SeparateWindow(this.MyApplication);
			((SeparateWindow) page).zInitializeWindowNames();

			this.zWaitForBusyOverlay();

			return (page);

		} else if ((action == Action.A_RIGHTCLICK) && (option == Button.B_RECOVER_DELETED_ITEMS)) {

			// Use default actionLocator
			optionLocator += " div[id^='RECOVER_DELETED_ITEMS'] td[id$='_title']";
			page = null;

			this.sRightClick(actionLocator);

		} else {
			throw new HarnessException("No logic defined for action " + action + " with option " + option);
		}

		if (actionLocator == null)
			throw new HarnessException("locator is null for action " + action);

		this.sClickAt(optionLocator, "");
		this.zWaitForBusyOverlay();

		if (page != null) {

			page.zWaitForActive();
		}

		return page;
	}

	protected AbsPage zTreeItem(Action action, Button option, SavedSearchFolderItem folder) throws HarnessException {

		if ((action == null) || (option == null) || (folder == null)) {
			throw new HarnessException("Must define an action, option, and addressbook");
		}

		tracer.trace("processing " + folder.getName());

		throw new HarnessException("locator is null for action " + action);
	}

	protected AbsPage zTreeItem(Action action, Button option, ZimletItem zimlet) throws HarnessException {
		throw new HarnessException("implement me!");
	}

	protected AbsPage zTreeItem(Action action, Button option, TagItem folder) throws HarnessException {

		if ((action == null) || (option == null) || (folder == null)) {
			throw new HarnessException("Must define an action, option, and calendar");
		}

		tracer.trace("processing " + folder.getName());

		String optionLocator = null;
		optionLocator = "css=div[id='ztih__main_Calendar__TAG'] td[id*='zti__main_Calendar__']:contains('"
				+ folder.getName() + "')";

		if (action == Action.A_LEFTCLICK) {

			this.sClick(optionLocator);

			return (new ContextMenu(MyApplication));

		} else if (action == Action.A_RIGHTCLICK && option == Button.B_RENAME) {

			// Select tag
			this.sRightClick(optionLocator);
			this.sClick(Locators.RenameTagMenu);

			return (new DialogRenameTag(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar));

		} else if (action == Action.A_RIGHTCLICK && option == Button.B_DELETE) {

			// Select tag
			this.sRightClick(optionLocator);
			this.sClick(Locators.DeleteTagMenu);

			return (new DialogDeleteTag(DialogDeleteTagID.DeleteTag, MyApplication,
					((AppAjaxClient) MyApplication).zPageCalendar));

		} else {
			throw new HarnessException("Action " + action + " not yet implemented");
		}

	}

	protected AbsPage zTreeItem(Action action, FolderItem folder) throws HarnessException {

		if ((action == null) || (folder == null)) {
			throw new HarnessException("Must define an action, option, and addressbook");
		}

		tracer.trace("processing " + folder.getName());

		throw new HarnessException("locator is null for action " + action);
	}

	protected AbsPage zTreeItem(Action action, SavedSearchFolderItem folder) throws HarnessException {

		if ((action == null) || (folder == null)) {
			throw new HarnessException("Must define an action, option, and addressbook");
		}

		tracer.trace("processing " + folder.getName());

		throw new HarnessException("locator is null for action " + action);
	}

	protected AbsPage zTreeItem(Action action, ZimletItem zimlet) throws HarnessException {
		throw new HarnessException("implement me");
	}

	public AbsPage zPressPulldown(Button pulldown, Button option) throws HarnessException {
		logger.info(myPageName() + " zPressPulldown(" + pulldown + ", " + option + ")");

		tracer.trace("Click " + pulldown + " then " + option);

		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null");

		if (option == null)
			throw new HarnessException("Option cannot be null");

		AbsPage page = null;
		String pulldownLocator = null;
		String optionLocator = null;

		if (pulldown == Button.B_TREE_FOLDERS_OPTIONS) {

			pulldownLocator = "css=div[id='zov__main_Calendar'] td[id='ztih__main_Calendar__CALENDAR_optCell'] div[class*=ImgContextMenu]";

			if (option == Button.B_TREE_NEWFOLDER) {

				optionLocator = "css=div[id='NEW_CALENDAR']";
				page = new DialogCreateFolder(MyApplication, ((AppAjaxClient) MyApplication).zPageMail);

			} else if (option == Button.B_TREE_FIND_SHARES) {

				optionLocator = "css=div[id='FIND_SHARES']";
				page = new DialogShareFind(MyApplication, ((AppAjaxClient) MyApplication).zPageMail);

			} else if (option == Button.B_TREE_NEW_EXTERNAL_CALENDAR) {

				optionLocator = "css=div[id='ADD_EXTERNAL_CALENDAR']";
				page = new DialogAddExternalCalendar(MyApplication, ((AppAjaxClient) MyApplication).zPageMail);

			} else {
				throw new HarnessException("Pulldown/Option " + pulldown + "/" + option + " not implemented");
			}

		} else if (pulldown == Button.B_TREE_TAGS_OPTIONS) {

			pulldownLocator = "css=div[id='zov__main_Calendar'] td[id='ztih__main_Mail__TAG_optCell'] td[id$='_title']";

			if (option == Button.B_TREE_NEWTAG) {

				optionLocator = "css=div[id='ZmActionMenu_calendar_TAG'] div[id='NEW_TAG'] td[id$='_title']";
				page = new DialogTag(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);

			} else {
				throw new HarnessException("Pulldown/Option " + pulldown + "/" + option + " not implemented");
			}

		} else {
			throw new HarnessException("Pulldown/Option " + pulldown + "/" + option + " not implemented");
		}

		if (pulldownLocator != null) {

			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " option " + option + " pulldownLocator "
						+ pulldownLocator + " not present!");
			}

			this.sClickAt(pulldownLocator, "0,0");

			zWaitForBusyOverlay();

			if (optionLocator != null) {

				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator "
							+ optionLocator + " not present!");
				}

				this.sClickAt(optionLocator, "0,0");

				zWaitForBusyOverlay();
			}

			if (page != null) {
				page.zWaitForActive();
			}
		}

		SleepUtil.sleepMedium();

		return (page);

	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {

		tracer.trace("Click " + button);

		if (button == null)
			throw new HarnessException("Button cannot be null");

		AbsPage page = null;
		String locator = null;

		if (button == Button.B_TREE_NEWFOLDER) {

			return (zPressPulldown(Button.B_TREE_FOLDERS_OPTIONS, Button.B_TREE_NEWFOLDER));

		} else if (button == Button.B_TREE_NEWTAG) {

			return (zPressPulldown(Button.B_TREE_TAGS_OPTIONS, Button.B_TREE_NEWTAG));

		} else if (button == Button.B_TREE_FIND_SHARES) {

			locator = "css=TODO#TODO";
			page = new DialogShareFind(MyApplication, ((AppAjaxClient) MyApplication).zPageCalendar);

			// Use sClick, not default sClick
			this.sClick(locator);

			this.zWaitForBusyOverlay();

			page.zWaitForActive();

			return (page);

		} else if (button == Button.B_TREE_SHOW_REMAINING_FOLDERS) {

			locator = "css=TODO#TODO";
			page = null;

			if (!this.sIsElementPresent(locator)) {
				throw new HarnessException("Unable to find 'show remaining folders' in tree " + locator);
			}

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		this.sClick(locator);

		this.zWaitForBusyOverlay();

		return (page);

	}

	public AbsPage zTreeItem(Action action, String locator) throws HarnessException {

		locator = "css=td[id^='zti__main_Calendar']:contains('" + locator + "')";

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("Unable to find tag in tree " + locator);

		if (action == Action.A_LEFTCLICK) {

			this.sClick(locator);

			return (new ContextMenu(MyApplication));

		} else if (action == Action.A_RIGHTCLICK) {

			this.sRightClick(locator);

			return (new ContextMenu(MyApplication));

		} else {
			throw new HarnessException("Action " + action + " not yet implemented");
		}
	}

	public AbsPage zTreeItem(Action action, IItem folder) throws HarnessException {

		tracer.trace("Click " + action + " on folder " + folder.getName());

		// Validate the arguments
		if ((action == null) || (folder == null)) {
			throw new HarnessException("Must define an action and addressbook");
		}

		if (folder instanceof FolderItem) {
			return (zTreeItem(action, (FolderItem) folder));
		} else if (folder instanceof SavedSearchFolderItem) {
			return (zTreeItem(action, (SavedSearchFolderItem) folder));
		} else if (folder instanceof ZimletItem) {
			return (zTreeItem(action, (ZimletItem) folder));
		}

		throw new HarnessException(
				"Must use FolderItem or SavedSearchFolderItem or ZimletItem as argument, but was " + folder.getClass());
	}

	public void zMarkOnOffCalendarFolder(String folderName) throws HarnessException {

		tracer.trace("Click on folder " + folderName);

		this.sClickAt(Locators.CalendarZimletPane, "");
		SleepUtil.sleepVerySmall();

		FolderItem folderID = FolderItem.importFromSOAP(MyApplication.zGetActiveAccount(), folderName);
		this.sClickAt("css=div[id='zti__main_Calendar__" + folderID.getId() + "_checkbox']", "");
		this.zWaitForBusyOverlay();
		SleepUtil.sleepLong();
	}

	public void zVerifyCalendarChecked(Boolean status, String folderId) throws HarnessException {
		ZAssert.assertEquals(this.sIsElementPresent("css=div[id=zti__main_Calendar__" + folderId + "_checkboxImg]"),
				status, "Verify calendar checked status");
	}

	@Override
	public AbsPage zTreeItem(Action action, Button option, IItem folder) throws HarnessException {

		tracer.trace("Click " + action + " then " + option + " on folder " + folder.getName());

		// Validate the arguments
		if ((action == null) || (option == null) || (folder == null)) {
			throw new HarnessException("Must define an action, option, and folder");
		}

		if (folder instanceof FolderItem) {
			return (zTreeItem(action, option, (FolderItem) folder));
		} else if (folder instanceof SavedSearchFolderItem) {
			return (zTreeItem(action, option, (SavedSearchFolderItem) folder));
		} else if (folder instanceof ZimletItem) {
			return (zTreeItem(action, option, (ZimletItem) folder));
		} else if (folder instanceof TagItem) {
			return (zTreeItem(action, option, (TagItem) folder));
		}

		throw new HarnessException(
				"Must use TagItem FolderItem or SavedSearchFolderItem or ZimletItem as argument, but was "
						+ folder.getClass());
	}

	private FolderItem parseFolderRow(String id) throws HarnessException {

		String locator;

		FolderItem item = new FolderItem();

		item.setId(id);

		// Set the name
		locator = "css=div[id='zti__main_Calendar__" + id + "'] td[id$='_textCell']";
		item.setName(this.sGetText(locator));

		// Set the expanded boolean
		locator = "css=div[id='zti__main_Calendar__" + id + "'] td[id$='_nodeCell']>div";
		if (sIsElementPresent(locator)) {
			// The image could be hidden, if there are no subfolders
			item.gSetIsExpanded("ImgNodeExpanded".equals(sGetAttribute(locator + "@class")));
		}

		// Set the selected boolean
		locator = "css=div[id='zti__main_Calendar__" + id + "'] div[id='zti__main_Calendar__" + id + "_div']";
		if (sIsElementPresent(locator)) {
			item.gSetIsSelected("DwtTreeItem-selected".equals(sGetAttribute(locator + "@class")));
		}

		return (item);
	}

	private List<FolderItem> zListGetFolders(String css) throws HarnessException {
		List<FolderItem> items = new ArrayList<FolderItem>();

		String searchLocator = css + " div[class='DwtTreeItem-Control']";

		int count = this.sGetCssCount(searchLocator);
		logger.debug(myPageName() + " zListGetFolders: number of folders: " + count);

		for (int i = 1; i <= count; i++) {
			String itemLocator = searchLocator + ":nth-child(" + i + ")";

			if (!this.sIsElementPresent(itemLocator)) {
				continue;
			}

			String identifier = sGetAttribute(itemLocator + "@id");
			logger.debug(myPageName() + " identifier: " + identifier);

			if (identifier == null || identifier.trim().length() == 0
					|| !(identifier.startsWith("zti__main_Calendar__"))) {

				count++;
				continue;
			}

			String id = identifier.replace("zti__main_Calendar__", "");

			FolderItem item = this.parseFolderRow(id);
			items.add(item);
			logger.info(item.prettyPrint());

			// Add any sub folders
			items.addAll(zListGetFolders(itemLocator));

		}

		return (items);
	}

	private List<SavedSearchFolderItem> zListGetSavedSearchFolders(String top) throws HarnessException {
		List<SavedSearchFolderItem> items = new ArrayList<SavedSearchFolderItem>();

		String searchLocator = top + "//div[@class='DwtTreeItem-Control']";

		int count = this.sGetXpathCount(searchLocator);
		for (int i = 1; i <= count; i++) {
			String itemLocator = searchLocator + "[" + i + "]";

			if (!this.sIsElementPresent(itemLocator)) {
				continue;
			}

			String locator;

			String id = sGetAttribute("xpath=(" + itemLocator + "/.)@id");
			if (id == null || id.trim().length() == 0 || !(id.startsWith("zti__main_Mail__"))) {
				continue;
			}

			SavedSearchFolderItem item = new SavedSearchFolderItem();

			item.setId(id.replace(
					"zti__" + ((AppAjaxClient) MyApplication).zGetActiveAccount().EmailAddress + ":main_Mail__", ""));

			// Set the name
			locator = itemLocator + "//td[contains(@id, '_textCell')]";
			item.setName(this.sGetText(locator));

			// Set the expanded boolean
			locator = itemLocator + "//td[contains(@id, '_nodeCell')]/div";
			if (sIsElementPresent(locator)) {

			}

			items.add(item);

			// Add any sub folders
			items.addAll(zListGetSavedSearchFolders(itemLocator));

		}

		return (items);

	}

	public List<FolderItem> zListGetFolders() throws HarnessException {

		List<FolderItem> items = new ArrayList<FolderItem>();

		// Recursively fill out the list, starting with all mail folders
		items.addAll(zListGetFolders("css=div[id='ztih__main_Calendar__CALENDAR']"));

		return (items);

	}

	public List<SavedSearchFolderItem> zListGetSavedSearches() throws HarnessException {

		List<SavedSearchFolderItem> items = new ArrayList<SavedSearchFolderItem>();

		// Recursively fill out the list, starting with all mail folders
		items.addAll(zListGetSavedSearchFolders("//div[@id='ztih__main_Mail__SEARCH']"));

		// Return the list of items
		return (items);

	}

	public List<TagItem> zListGetTags() throws HarnessException {

		List<TagItem> items = new ArrayList<TagItem>();
		return (items);
	}

	public List<ZimletItem> zListGetZimlets() throws HarnessException {
		throw new HarnessException("implement me!");
	}

	public enum FolderSectionAction {
		Expand, Collapse
	}

	public enum FolderSection {
		Folders, Searches, Tags, Zimlets
	}

	public AbsPage zSectionAction(FolderSectionAction action, FolderSection section) throws HarnessException {
		throw new HarnessException("implement me!");
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		if (!((AppAjaxClient) MyApplication).zPageCalendar.zIsActive()) {
			((AppAjaxClient) MyApplication).zPageCalendar.zNavigateTo();
		}

		String locator = Locators.CalendarZimletPane;

		boolean loaded = this.sIsElementPresent(locator);
		if (!loaded)
			return (false);

		return (loaded);

	}
}