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
package com.zimbra.qa.selenium.projects.ajax.pages.mail;

import java.util.ArrayList;
import java.util.List;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTree;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.pages.*;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogTag;

public class TreeMail extends AbsTree {

	public final static String stringToReplace = "<TREE_ITEM_NAME>";

	public static class Locators {

		public final static String zTreeItems = new StringBuffer("//td[text()='").append(stringToReplace).append("']")
				.toString();
		public static final String createNewFolderButton = "css=div[id='zov__main_Mail'] td[id='ztih__main_Mail__FOLDER_optCell'] td[id$='_title']";
		public static final String ztih__main_Mail__ZIMLET_ID = "ztih__main_Mail__ZIMLET";
		public static final String ztih__main_Mail__ZIMLET_nodeCell_ID = "ztih__main_Mail__ZIMLET_nodeCell";
		public static final String ztih_main_Mail__FOLDER_ITEM_ID = new StringBuffer("ztih__main_Mail__")
				.append(stringToReplace).append("_textCell").toString();
		public static final String zNewTagIcon = "//td[contains(@class,'overviewHeader-Text FakeAnchor')]/div[contains(@class,'ImgNewTag')]";
		public static final String zShowRemainingFolders = "css=td#zti__main_Mail__-3_textCell";
		public static final String treeExpandCollapseButton = "css=div[id='ztih__main_Mail__FOLDER_div'] div[class^='ImgNode']";
		public static final String zDeleteTreeMenuItem = "//div[contains(@class,'ZMenuItem')]//tbody//td[contains(@id,'_left_icon')]/div[contains(@class,'ImgDelete')]";
		public static final String zRenameTreeMenuItem = "//div[contains(@class,'ZMenuItem')]//tbody//td[contains(@id,'_left_icon')]/div[contains(@class,'ImgRename')]";
		public static final String zEditTreeMenuItem = "//td[contains(@id,'_title') and contains(text(),'Edit Properties')]";

		// Context menus
		public static final String ContextMenuCLVFoldersCSS = "css=div[id='ZmActionMenu_conversationList_FOLDER']";
		public static final String ContextMenuCLVSearchesCSS = "css=div[id='ZmActionMenu_conversationList_SEARCH']";
		public static final String ContextMenuCLVTagsCSS = "css=div[id='ZmActionMenu_conversationList_TAG']";
		public static final String ContextMenuCLVTagsCSS2 = "css=div[id^='ZmActionMenu_conversationList_TAG__']";
		public static final String ContextMenuTVFoldersCSS = "css=div[id='ZmActionMenu_mail_FOLDER']";
		public static final String ContextMenuTVSearchesCSS = "css=div[id='ZmActionMenu_mail_SEARCH']";
		public static final String ContextMenuTVTagsCSS = "css=div[id='ZmActionMenu_mail_TAG']";
		public static final String ContextMenuTVTagsCSS2 = "css=div[id^='ZmActionMenu_mail_TAG__']";

	}

	public TreeMail(AbsApplication application) {
		super(application);
		logger.info("new " + TreeMail.class.getCanonicalName());
	}

	protected AbsPage zTreeItem(Action action, Button option, FolderItem folder) throws HarnessException {

		if ((action == null) || (option == null) || (folder == null)) {
			throw new HarnessException("Must define an action, option, and folder");
		}

		AbsPage page = null;
		String actionLocator = "zti__main_Mail__" + folder.getId() + "_textCell";
		;
		String optionLocator = Locators.ContextMenuTVFoldersCSS;

		tracer.trace("processing " + folder.getName());

		if (action == Action.A_RIGHTCLICK) {

			if (folder.getName().equals("USER_ROOT")) {
				actionLocator = "css=td[id='ztih__main_Mail__FOLDER_textCell']";
			}

			SleepUtil.sleepSmall();
			this.sRightClickAt(actionLocator, "");
			this.zWaitForBusyOverlay();
			SleepUtil.sleepSmall();

			optionLocator = Locators.ContextMenuTVFoldersCSS;
			if (!(this.sIsElementPresent(optionLocator) && this.zIsVisiblePerPosition(optionLocator, 0, 0))) {
				optionLocator = Locators.ContextMenuCLVFoldersCSS;
			}

			if ((option == Button.B_NEW) || (option == Button.O_NEW_FOLDER)) {

				optionLocator += " div[id^='NEW_FOLDER'] td[id$='_title']";
				page = new DialogCreateFolder(MyApplication, ((AjaxPages) MyApplication).zPageMail);

			} else if ((option == Button.O_MARK_AS_READ) || (option == Button.B_TREE_FOLDER_MARKASREAD)) {

				optionLocator += " div[id^='MARK_ALL_READ'] td[id$='_title']";
				page = null;

			} else if (option == Button.B_DELETE) {

				optionLocator += " div[id^='DELETE_WITHOUT_SHORTCUT'] td[id$='_title']";
				page = null;

			} else if (option == Button.B_RENAME) {

				optionLocator += " div[id^='RENAME_FOLDER'] td[id$='_title']";
				page = new DialogRenameFolder(MyApplication, ((AjaxPages) MyApplication).zPageMail);

			} else if (option == Button.B_MOVE) {

				optionLocator += " div[id^='MOVE'] td[id$='_title']";
				page = new DialogMove(MyApplication, ((AjaxPages) MyApplication).zPageMail);

			} else if (option == Button.B_SHARE) {

				optionLocator += " div[id^='SHARE_FOLDER'] td[id$='_title']";
				page = new DialogShare(MyApplication, ((AjaxPages) MyApplication).zPageMail);

			} else if (option == Button.B_TREE_EDIT) {

				optionLocator += " div[id^='EDIT_PROPS'] td[id$='_title']";
				page = new DialogEditFolder(MyApplication, ((AjaxPages) MyApplication).zPageMail);

			} else if (option == Button.B_TREE_FOLDER_EXPANDALL) {

				optionLocator += " div[id^='EXPAND_ALL'] td[id$='_title']";
				page = null;

			} else if (option == Button.B_TREE_FOLDER_GET_EXTERNAL) {

				optionLocator += " div[id^='SYNC'] td[id$='_title']";
				page = null;

			} else if (option == Button.B_TREE_FOLDER_EMPTY) {

				optionLocator += " div[id^='EMPTY_FOLDER'] td[id$='_title']";
				page = new DialogWarning(DialogWarning.DialogWarningID.EmptyFolderWarningMessage, MyApplication,
						((AjaxPages) MyApplication).zPageMail);

			} else if (option == Button.B_RECOVER_DELETED_ITEMS) {

				optionLocator += " div[id^='RECOVER_DELETED_ITEMS'] td[id$='_title']";
				page = new FormRecoverDeletedItems(MyApplication);

			} else if (option == Button.B_OPENTAB) {

				optionLocator += " div[id^='OPEN_IN_TAB'] td[id$='_title']";
				page = null;

			} else {
				throw new HarnessException("button " + option + " not yet implemented");
			}

		} else {
			throw new HarnessException("Action " + action + " not yet implemented");
		}

		sClick(optionLocator);
		this.zWaitForBusyOverlay();
		SleepUtil.sleepSmall();

		if (page != null) {
			page.zWaitForActive();
		}

		return page;
	}

	protected AbsPage zTreeItem(Action action, Button option, SavedSearchFolderItem savedSearchFolder)
			throws HarnessException {

		if ((action == null) || (option == null) || (savedSearchFolder == null)) {
			throw new HarnessException("Must define an action, option, and addressbook");
		}

		AbsPage page = null;
		String actionLocator = null;
		String optionLocator = Locators.ContextMenuTVSearchesCSS; // css=div[id='ZmActionMenu_mail_SEARCH'];
		SavedSearchFolderItem f = (SavedSearchFolderItem) savedSearchFolder;
		tracer.trace("processing " + f.getName());

		if (action == Action.A_LEFTCLICK) {

			actionLocator = "implement me";

		} else if (action == Action.A_RIGHTCLICK) {

			actionLocator = "zti__main_Mail__" + f.getId() + "_textCell";

			GeneralUtility.waitForElementPresent(this, actionLocator);
			this.sRightClick(actionLocator);
			this.zWaitForBusyOverlay();

			optionLocator = Locators.ContextMenuTVSearchesCSS;

			if (!(this.sIsElementPresent(optionLocator) && this.zIsVisiblePerPosition(optionLocator, 0, 0))) {
				optionLocator = Locators.ContextMenuCLVSearchesCSS;
			}

		} else {
			throw new HarnessException("Action " + action + " not yet implemented");
		}

		if (option == Button.B_DELETE) {

			optionLocator += " div[id^='DELETE_WITHOUT_SHORTCUT'] td[id$='_title']";
			page = null;

		} else if (option == Button.B_RENAME) {

			optionLocator += " div[id^='RENAME_SEARCH'] td[id$='_title']";
			page = new DialogRenameFolder(MyApplication, ((AjaxPages) MyApplication).zPageMail);

		} else if (option == Button.B_EDIT) {

			optionLocator += " div[id^='EDIT_PROPS'] td[id$='_title']";
			page = new DialogEditFolder(MyApplication, ((AjaxPages) MyApplication).zPageMail);

		} else if (option == Button.B_MOVE) {

			optionLocator += " div[id^='MOVE'] td[id$='_title']";
			page = new DialogMove(MyApplication, ((AjaxPages) MyApplication).zPageMail);

		}

		if (optionLocator == null)
			throw new HarnessException("locator is null for option " + option);

		sClick(optionLocator);
		this.zWaitForBusyOverlay();
		SleepUtil.sleepSmall();

		if (page != null) {
			page.zWaitForActive();
		}
		return page;
	}

	protected AbsPage zTreeItem(Action action, Button option, ZimletItem zimlet) throws HarnessException {
		throw new HarnessException("implement me!");
	}

	protected AbsPage zTreeItem(Action action, Button option, TagItem t) throws HarnessException {

		if ((action == null) || (option == null) || (t == null)) {
			throw new HarnessException("Must define an action, option, and addressbook");
		}
		AbsPage page = null;
		String actionLocator = null;
		String optionLocator = Locators.ContextMenuTVTagsCSS; // css=div[id='ZmActionMenu_conversationList_TAG']

		tracer.trace("processing " + t.getName());

		if (action == Action.A_LEFTCLICK) {

			actionLocator = "implement me";

		} else if (action == Action.A_RIGHTCLICK) {
			actionLocator = "css=td[id^='zti__main_Mail__']:contains('" + t.getName() + "')";

			this.sRightClickAt(actionLocator, "");

			this.zWaitForBusyOverlay();

			page = new DialogTag(MyApplication, ((AjaxPages) MyApplication).zPageMail);

			optionLocator = Locators.ContextMenuTVTagsCSS; // css=div[id='ZmActionMenu_mail_TAG']
			if (!(this.sIsElementPresent(optionLocator) && this.zIsVisiblePerPosition(optionLocator, 0, 0))) {
				optionLocator = Locators.ContextMenuTVTagsCSS2; // css=div[id^='ZmActionMenu_mail_TAG__']
				if (!(this.sIsElementPresent(optionLocator) && this.zIsVisiblePerPosition(optionLocator, 0, 0))) {
					optionLocator = Locators.ContextMenuCLVTagsCSS; // css=div[id='ZmActionMenu_conversationList_TAG']
					if (!(this.sIsElementPresent(optionLocator) && this.zIsVisiblePerPosition(optionLocator, 0, 0))) {
						optionLocator = Locators.ContextMenuCLVTagsCSS2; // css=div[id^='ZmActionMenu_conversationList_TAG__']
						if (!(this.sIsElementPresent(optionLocator)
								&& this.zIsVisiblePerPosition(optionLocator, 0, 0))) {
							throw new HarnessException("No context menu!");
						}
					}
				}
			}

		} else {
			throw new HarnessException("Action " + action + " not yet implemented");
		}

		if (option == Button.B_TREE_NEWTAG) {
			optionLocator += " div[id^='NEW_TAG'] td[id$='_title']";

		} else if (option == Button.B_DELETE) {

			optionLocator += " div[id^='DELETE_WITHOUT_SHORTCUT'] td[id$='_title']";
			page = new DialogWarning(DialogWarning.DialogWarningID.DeleteTagWarningMessage, MyApplication,
					((AjaxPages) MyApplication).zPageMail);

		} else if (option == Button.O_MARK_AS_READ) {

			optionLocator += " div[id^='MARK_ALL_READ'] td[id$='_title']";

		} else if (option == Button.B_RENAME) {

			optionLocator += " div[id^='RENAME_TAG'] td[id$='_title']";
			page = new DialogRenameTag(MyApplication, ((AjaxPages) MyApplication).zPageMail);

		} else {
			throw new HarnessException("button " + option + " not yet implemented");
		}

		sClick(optionLocator);
		this.zWaitForBusyOverlay();
		SleepUtil.sleepSmall();

		if (page != null) {
			page.zWaitForActive();
		}

		return (page);

	}

	protected AbsPage zTreeItem(Action action, TagItem tag) throws HarnessException {
		AbsPage page = null;
		String locator = null;

		if (action == Action.A_LEFTCLICK) {
			locator = "css=td[id='zti__main_Mail__" + tag.getId() + "_textCell']";

		} else if (action == Action.A_RIGHTCLICK) {

			locator = "css=td[id='zti__main_Mail__" + tag.getId() + "_textCell']";
			this.sRightClickAt(locator, "");
			SleepUtil.sleepSmall();

			return (new ContextMenu(MyApplication));

		} else if (action == Action.A_TREE_EXPAND) {

			locator = "css=[id='zti__main_Mail__" + tag.getId() + "_nodeCell'] div[class='ImgNodeCollapsed']";
			if (!this.sIsElementPresent(locator)) {
				logger.warn("Trying to expand a folder that probably has no subfolders or is already expanded");
				return (page);
			}

			this.sMouseDown(locator);
			this.zWaitForBusyOverlay();

			// Work around for discarding zimbra tooltip
			this.sMouseOver(Locators.ztih__main_Mail__ZIMLET_ID);
			SleepUtil.sleepVerySmall();

			return (null);

		} else if (action == Action.A_TREE_COLLAPSE) {

			locator = "css=[id='zti__main_Mail__" + tag.getId() + "_nodeCell'] div[class='ImgNodeExpanded']";
			if (!this.sIsElementPresent(locator)) {
				logger.warn("Trying to collapse a folder that probably has no subfolders or is already collapsed");
				return (page);
			}

			this.sMouseDown(locator);
			this.zWaitForBusyOverlay();

			// Work around for discarding zimbra tooltip
			this.sMouseOver(Locators.ztih__main_Mail__ZIMLET_ID);
			SleepUtil.sleepVerySmall();

			return (null);

		} else if (action == Action.A_HOVEROVER) {

			locator = "css=td[id='zti__main_Mail__" + tag.getId() + "_textCell']";
			page = new TooltipFolder(MyApplication);

			if (page.zIsActive()) {

				// Mouse over
				this.sMouseOver(locator);
				this.zWaitForBusyOverlay();

				// Wait for the new text
				SleepUtil.sleep(5000);
				page.zWaitForActive();

			} else {

				// Mouse over
				this.sMouseOver(locator);
				this.zWaitForBusyOverlay();

				// Make sure the tooltip is active
				page.zWaitForActive();
			}

			return (page);

		} else {
			throw new HarnessException("Action " + action + " not yet implemented");
		}

		sClick(locator);
		this.zWaitForBusyOverlay();
		SleepUtil.sleepSmall();

		return (page);

	}

	protected AbsPage zTreeItem(Action action, FolderItem folder) throws HarnessException {
		AbsPage page = null;
		String locator = null;

		if (action == Action.A_LEFTCLICK) {
			locator = "css=td[id='zti__main_Mail__" + folder.getId() + "_textCell']";

		} else if (action == Action.A_RIGHTCLICK) {

			locator = "css=td[id='zti__main_Mail__" + folder.getId() + "_textCell']";

			zWaitForElementPresent(locator);
			this.sRightClickAt(locator, "");
			SleepUtil.sleepSmall();

			return (new ContextMenu(MyApplication));

		} else if (action == Action.A_TREE_EXPAND) {

			locator = "css=[id='zti__main_Mail__" + folder.getId() + "_nodeCell'] div[class='ImgNodeCollapsed']";
			if (!this.sIsElementPresent(locator)) {
				logger.warn("Trying to expand a folder that probably has no subfolders or is already expanded");
				return (page);
			}

			this.sClickAt(locator, "");
			this.zWaitForBusyOverlay();
			SleepUtil.sleepSmall();

			// Work around for discarding zimbra tooltip
			this.sMouseOver(Locators.ztih__main_Mail__ZIMLET_ID);
			SleepUtil.sleepVerySmall();

			return (null);

		} else if (action == Action.A_TREE_COLLAPSE) {

			locator = "css=[id='zti__main_Mail__" + folder.getId() + "_nodeCell'] div[class='ImgNodeExpanded']";
			if (!this.sIsElementPresent(locator)) {
				logger.warn("Trying to collapse a folder that probably has no subfolders or is already collapsed");
				return (page);
			}

			this.sClickAt(locator, "");
			this.zWaitForBusyOverlay();
			SleepUtil.sleepSmall();

			// Work around for discarding zimbra tooltip
			this.sMouseOver(Locators.ztih__main_Mail__ZIMLET_ID);
			SleepUtil.sleepVerySmall();

			return (null);

		} else if (action == Action.A_HOVEROVER) {

			locator = "css=td[id='zti__main_Mail__" + folder.getId() + "_textCell']";
			page = new TooltipFolder(MyApplication);

			if (page.zIsActive()) {

				// Mouse over
				this.sMouseOver(locator);
				this.zWaitForBusyOverlay();

				// Wait for the new text
				SleepUtil.sleep(5000);

				// Make sure the tooltip is active
				page.zWaitForActive();

			} else {

				// Mouse over
				this.sMouseOver(locator);
				this.zWaitForBusyOverlay();
				SleepUtil.sleepSmall();

				// Make sure the tooltip is active
				page.zWaitForActive();

			}

			return (page);

		} else {
			throw new HarnessException("Action " + action + " not yet implemented");
		}

		sClick(locator);
		this.zWaitForBusyOverlay();
		SleepUtil.sleepSmall();

		return (page);
	}

	public boolean isCollapsed() throws HarnessException {
		if (sIsElementPresent(Locators.treeExpandCollapseButton.replace("ImgNode", "ImgNodeCollapsed"))) {
			return true;
		} else {
			return false;
		}
	}

	protected AbsPage zTreeItem(Action action, SavedSearchFolderItem savedSearch) throws HarnessException {
		AbsPage page = null;
		String locator = null;

		if (action != Action.A_LEFTCLICK)
			throw new HarnessException("No implementation for Action = " + action);

		locator = "css=td#zti__main_Mail__" + savedSearch.getId() + "_textCell";

		sClickAt(locator, "");
		this.zWaitForBusyOverlay();
		SleepUtil.sleepSmall();

		return (page);
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

			pulldownLocator = "css=div[id='zov__main_Mail'] td[id='ztih__main_Mail__FOLDER_optCell'] div[class*=ImgContextMenu]";

			if (option == Button.B_TREE_NEWFOLDER) {

				optionLocator = "css=div[id='NEW_FOLDER']";
				page = new DialogCreateFolder(MyApplication, ((AjaxPages) MyApplication).zPageMail);

			} else if (option == Button.B_TREE_FIND_SHARES) {

				optionLocator = "css=div[id='FIND_SHARES']";
				page = new DialogShareFind(MyApplication, ((AjaxPages) MyApplication).zPageMail);

			} else {
				throw new HarnessException("Pulldown/Option " + pulldown + "/" + option + " not implemented");
			}

		} else if (pulldown == Button.B_TREE_TAGS_OPTIONS) {

			pulldownLocator = "css=div[id='zov__main_Mail'] td[id='ztih__main_Mail__TAG_optCell'] div[class*=ImgContextMenu]";

			if (option == Button.B_TREE_NEWTAG) {

				optionLocator = "css=div[id='NEW_TAG']";
				page = new DialogTag(MyApplication, ((AjaxPages) MyApplication).zPageMail);

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

			this.sClick(pulldownLocator);
			SleepUtil.sleepMedium();
			zWaitForBusyOverlay();

			if (optionLocator != null) {

				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator "
							+ optionLocator + " not present!");
				}

				this.sClick(optionLocator);
				zWaitForBusyOverlay();
			}

			if (page != null) {
				page.zWaitForActive();
			}
		}

		SleepUtil.sleepSmall();
		return (page);

	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");

		tracer.trace("Click " + button);

		if (button == null)
			throw new HarnessException("Button cannot be null");

		AbsPage page = null;
		String locator = null;

		if (button == Button.B_TREE_NEWFOLDER) {
			SleepUtil.sleepMedium(); // Create folder test intermittantly fails without any reason
			return (zPressPulldown(Button.B_TREE_FOLDERS_OPTIONS, Button.B_TREE_NEWFOLDER));

		} else if (button == Button.B_TREE_NEWTAG) {

			return (zPressPulldown(Button.B_TREE_TAGS_OPTIONS, Button.B_TREE_NEWTAG));

		} else if (button == Button.B_TREE_FIND_SHARES) {

			locator = "css=a[id$='_addshare_link']";
			if (!this.sIsElementPresent(locator)) {
				throw new HarnessException("Unable to locator folder in tree " + locator);
			}

			page = new DialogShareFind(MyApplication, ((AjaxPages) MyApplication).zPageMail);

			// Use sClick, not default sClick
			this.sClickAt(locator, "");
			SleepUtil.sleepSmall();

			this.zWaitForBusyOverlay();

			page.zWaitForActive();

			return (page);

		} else if (button == Button.B_TREE_SHOW_REMAINING_FOLDERS) {

			locator = Locators.zShowRemainingFolders;
			page = null;

			if (!this.sIsElementPresent(locator)) {
				throw new HarnessException("Unable to find 'show remaining folders' in tree " + locator);
			}

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for button " + button);
		}

		this.sClick(locator);
		this.zWaitForBusyOverlay();

		return (page);

	}

	protected AbsPage zTreeItem(Action action, String locator) throws HarnessException {
		AbsPage page = null;

		if (locator == null)
			throw new HarnessException("locator is null for action " + action);

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("Unable to locator folder in tree " + locator);

		if (action == Action.A_LEFTCLICK) {

		} else if (action == Action.A_RIGHTCLICK) {

			this.sRightClick(locator);

			return (new ContextMenu(MyApplication));

		} else {
			throw new HarnessException("Action " + action + " not yet implemented");
		}

		sClick(locator);
		SleepUtil.sleepSmall();

		return (page);
	}

	public AbsPage zTreeItem(Action action, IItem folder) throws HarnessException {

		// Validate the arguments
		if ((action == null) || (folder == null)) {
			throw new HarnessException("Must define an action and addressbook");
		}

		logger.info(myPageName() + " zTreeItem(" + action + ", " + folder.getName() + ")");

		tracer.trace("Click " + action + " on folder " + folder.getName());

		SleepUtil.sleepSmall();

		if (folder instanceof FolderItem) {
			return (zTreeItem(action, (FolderItem) folder));

		} else if (folder instanceof TagItem) {
			return (zTreeItem(action, (TagItem) folder));

		} else if (folder instanceof SavedSearchFolderItem) {
			return (zTreeItem(action, (SavedSearchFolderItem) folder));

		} else if (folder instanceof ZimletItem) {
			return (zTreeItem(action, (ZimletItem) folder));
		}

		throw new HarnessException(
				"Must use FolderItem or SavedSearchFolderItem or ZimletItem as argument, but was " + folder.getClass());
	}

	@Override
	public AbsPage zTreeItem(Action action, Button option, IItem folder) throws HarnessException {

		// Validate the arguments
		if ((action == null) || (option == null) || (folder == null)) {
			throw new HarnessException("Must define an action, option, and folder");
		}

		logger.info(myPageName() + " zTreeItem(" + action + ", " + option + ", " + folder.getName() + ")");

		tracer.trace("Click " + action + " then " + option + " on folder " + folder.getName());

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
		locator = "css=div[id='zti__main_Mail__" + id + "'] td[id$='_textCell']";
		item.setName(this.sGetText(locator));

		// Set the expanded boolean
		locator = "css=div[id='zti__main_Mail__" + id + "'] td[id$='_nodeCell']>div";
		if (sIsElementPresent(locator)) {
			// The image could be hidden, if there are no subfolders
			item.gSetIsExpanded("ImgNodeExpanded".equals(sGetAttribute(locator + "@class")));
		}

		// Set the selected boolean
		locator = "css=div[id='zti__main_Mail__" + id + "'] div[id='zti__main_Mail__" + id + "_div']";
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

		for (int i = 1; i <= count + 1; i++) {
			String itemLocator = searchLocator + ":nth-child(" + i + ")";

			if (!this.sIsElementPresent(itemLocator)) {
				continue;
			}

			String identifier = sGetAttribute(itemLocator + "@id");
			logger.debug(myPageName() + " identifier: " + identifier);

			if (identifier == null || identifier.trim().length() == 0 || !(identifier.startsWith("zti__main_Mail__"))) {
				count++;
				continue;
			}

			String id = identifier.replace("zti__main_Mail__", "");

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

		String searchLocator = top + " div[id^='zti__main_Mail__']";

		int count = this.sGetCssCount(searchLocator);
		for (int i = 1; i <= count; i++) {
			String itemLocator = searchLocator + ":nth-child(" + i + ")";

			if (!this.sIsElementPresent(itemLocator)) {
				continue;
			}

			String locator;

			String id = this.sGetAttribute(itemLocator + "@id");
			if (id == null || id.trim().length() == 0 || !(id.startsWith("zti__main_Mail__"))) {
				// Not a folder
				// Maybe "Find Shares ..."
				continue;
			}

			// Since we have the ID, just simplify the locator
			// Example: zti__main_Mail__257
			itemLocator = "css=div#" + id;

			SavedSearchFolderItem item = new SavedSearchFolderItem();

			item.setId(id.replace(
					"zti__" + ((AjaxPages) MyApplication).zGetActiveAccount().EmailAddress + ":main_Mail__", ""));

			// Set the name
			locator = itemLocator + " td[id$='_textCell']";
			item.setName(this.sGetText(locator));

			// Set the expanded boolean
			locator = itemLocator + " td[id$='_nodeCell']>div";
			if (sIsElementPresent(locator)) {
				// The image could be hidden, if there are no subfolders
				// item.gSetIsExpanded("ImgNodeExpanded".equals(sGetAttribute("xpath=("+
				// locator + ")@class")));
			}

			items.add(item);

			// Add any sub folders
			items.addAll(zListGetSavedSearchFolders(itemLocator));

		}

		return (items);

	}

	public List<FolderItem> zListGetFolders() throws HarnessException {

		// Bug 65234
		// Sleep for a bit to load up the new folders
		SleepUtil.sleepVerySmall();

		List<FolderItem> items = new ArrayList<FolderItem>();

		// Recursively fill out the list, starting with all mail folders
		items.addAll(zListGetFolders("css=div[id='ztih__main_Mail__FOLDER']"));

		return (items);

	}

	public List<SavedSearchFolderItem> zListGetSavedSearches() throws HarnessException {

		List<SavedSearchFolderItem> items = new ArrayList<SavedSearchFolderItem>();

		// Recursively fill out the list, starting with all mail folders
		items.addAll(zListGetSavedSearchFolders("css=div#ztih__main_Mail__SEARCH div.DwtTreeItemLevel1ChildDiv"));

		// Return the list of items
		return (items);

	}

	public List<TagItem> zListGetTags() throws HarnessException {

		List<TagItem> items = new ArrayList<TagItem>();

		// Return the list of items
		return (items);

	}

	public List<ZimletItem> zListGetZimlets() throws HarnessException {

		// Create a list of items to return
		List<ZimletItem> items = new ArrayList<ZimletItem>();

		String treeLocator = Locators.ztih__main_Mail__ZIMLET_ID;

		if (!this.sIsElementPresent(treeLocator))
			throw new HarnessException("Zimlet Tree is not present " + treeLocator);

		// Zimlet's div ID seems to start with -999
		for (int zimletNum = -999; zimletNum < 0; zimletNum++) {

			String zimletLocator = null;
			String imageLocator = null;
			String nameLocator = null;

			zimletLocator = "zti__main_Mail__" + zimletNum + "_z_div";
			imageLocator = "xpath=(//*[@id='zti__main_Mail__" + zimletNum + "_z_imageCell']/div)@class";
			nameLocator = "zti__main_Mail__" + zimletNum + "_z_textCell";

			if (!this.sIsElementPresent(zimletLocator)) {
				// No more items to parse
				return (items);
			}

			// Parse this div element into a ZimletItem object

			ZimletItem item = new ZimletItem();

			// Get the image
			item.setFolderTreeImage(this.sGetAttribute(imageLocator));

			// Get the display name
			item.setFolderTreeName(this.sGetText(nameLocator));

			// Set the locator
			item.setFolderTreeLocator(zimletLocator);

			// Add this item to the list
			items.add(item);

		}

		// If we get here, there were over 1000 zimlets or something went wrong
		throw new HarnessException("Too many zimlets!");

	}

	public enum FolderSectionAction {
		Expand, Collapse
	}

	public enum FolderSection {
		Folders, Searches, Tags, Zimlets
	}

	/**
	 * Apply an expand/collpase to the Folders, Searches, Tags and Zimlets sections
	 *
	 * @param a
	 * @param section
	 *
	 */
	public AbsPage zSectionAction(FolderSectionAction action, FolderSection section) throws HarnessException {

		AbsPage page = null;
		String locator = null;
		boolean expanded = false;

		if (section == FolderSection.Zimlets) {

			// What is the current state of the section?
			locator = "xpath=(//td[@id='" + Locators.ztih__main_Mail__ZIMLET_nodeCell_ID + "']/div)@class";

			// Image is either ImgNodeExpanded or ImgNodeCollapsed
			expanded = sGetAttribute(locator).equals("ImgNodeExpanded");

			if (action == FolderSectionAction.Expand) {

				if (expanded) {
					logger.info("section is already expanded");
					return (page);
				}

				locator = "css=td[id=" + Locators.ztih__main_Mail__ZIMLET_nodeCell_ID + "] div";

			}

		}

		if (locator == null) {
			throw new HarnessException("no locator defined for " + action + " " + section);
		}

		this.sClickAt(locator, "");
		this.zWaitForBusyOverlay();
		SleepUtil.sleepSmall();

		return (page);
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		if (!((AjaxPages) MyApplication).zPageMail.zIsActive()) {
			((AjaxPages) MyApplication).zPageMail.zNavigateTo();
		}

		// Zimlets seem to be loaded last
		// So, wait for the zimlet div to load
		String locator = Locators.ztih__main_Mail__ZIMLET_ID;

		boolean loaded = this.sIsElementPresent(locator);
		if (!loaded)
			return (false);

		return (loaded);
	}
}