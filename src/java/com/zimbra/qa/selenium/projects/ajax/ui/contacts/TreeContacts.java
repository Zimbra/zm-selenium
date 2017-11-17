/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.ui.*;

public class TreeContacts extends AbsTree {
	public static final String NEW_FOLDER = "css=#ztih__main_Contacts__ADDRBOOK_table tbody tr td:nth-child(4)";
	public static final String COLLAPSE_TREE = "css#ztih__main_Contacts__ADDRBOOK_nodeCell";

	public static class Locators {
		public static final String EXPAND_NODE = "ImgNodeExpanded";
		public static final String COLLAPSE_NODE = "ImgNodeCollapsed";
	}

	public TreeContacts(AbsApplication application) {
		super(application);
		logger.info("new " + TreeContacts.class.getCanonicalName());
	}

	public AbsPage zTreeItem(Action action, IItem addressbook) throws HarnessException {
		tracer.trace("Click " + action + " on addressbook " + addressbook);

		if ((action == null) || (addressbook == null)) {
			throw new HarnessException("Must define an action and addressbook");
		}

		if (!(addressbook instanceof FolderItem)) {
			throw new HarnessException("Must use FolderItem as argument, but was " + addressbook.getClass());
		}

		FolderItem folder = (FolderItem) addressbook;

		AbsPage page = null;
		String locator = null;

		if (action == Action.A_LEFTCLICK) {

			locator = "id=zti__main_Contacts__" + folder.getId() + "_textCell";
			page = null;
		} else if (action == Action.A_RIGHTCLICK) {

			locator = "id=zti__main_Contacts__" + folder.getId() + "_textCell";
			if (!this.sIsElementPresent(locator)) {
				throw new HarnessException("Unable to locator folder in tree " + locator);
			}

			this.sRightClickAt(locator, "");
			zWaitForBusyOverlay();

			return (null);

		} else {
			throw new HarnessException("Action " + action + " not yet implemented");
		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Unable to locator folder in tree " + locator);
		}

		sClick(locator);
		zWaitForBusyOverlay();
		SleepUtil.sleepSmall();

		return (page);
	}

	@Override
	public AbsPage zTreeItem(Action action, Button option, IItem folder) throws HarnessException {

		tracer.trace("Click " + action + " then " + option + " on folder " + folder.getName());

		if ((action == null) || (option == null) || (folder == null)) {
			throw new HarnessException("Must define an action, option, and addressbook");
		}

		if (folder instanceof FolderMountpointItem) {
			return (zTreeItem(action, option, (FolderMountpointItem) folder));
		} else if (folder instanceof SavedSearchFolderItem) {
			return (zTreeItem(action, option, folder));
		} else if (folder instanceof TagItem) {
			return (zTreeItem(action, option, (TagItem) folder));
		} else if (folder instanceof FolderItem) {
			return (zTreeItem(action, option, (FolderItem) folder));
		}

		throw new HarnessException(
				"Must use TagItem FolderItem or SavedSearchFolderItem or ZimletItem as argument, but was "
						+ folder.getClass());
	}

	protected AbsPage zTreeItem(Action action, Button option, FolderMountpointItem folderItem) throws HarnessException {

		logger.info(myPageName() + " zTreeItem(" + action + ", " + option + "," + folderItem.getName() + ")");
		tracer.trace(action + " then " + option + " on Folder Item = " + folderItem.getName());

		AbsPage page = null;
		String actionLocator = null;
		String optionLocator = null;

		if ((action == null) || (option == null) || (folderItem == null)) {
			throw new HarnessException("Must define an action, option, and addressbook");
		}

		if (folderItem.getName().equals("USER_ROOT")) {
			actionLocator = "css=div#ztih__main_Contacts__ADDRBOOK_div";
		} else {
			actionLocator = "css=div#zti__main_Contacts__" + folderItem.getId() + "_div";
		}

		if (action == Action.A_RIGHTCLICK) {
			optionLocator = "css=div[id='ZmActionMenu_contacts_ADDRBOOK']";

			if (option == Button.B_TREE_NEWFOLDER) {
				optionLocator = "css=div[id^='NEW_ADDRBOOK']";
				page = new DialogCreateFolder(MyApplication, ((AppAjaxClient) MyApplication).zPageContacts);

			} else if (option == Button.B_DELETE) {
				optionLocator += " div[id='DELETE_WITHOUT_SHORTCUT'] td[id$='_title']";
				page = null;

			} else if (option == Button.B_RENAME) {
				optionLocator += " div[id='RENAME_FOLDER'] td[id$='_title']";
				page = new DialogRenameFolder(MyApplication, ((AppAjaxClient) MyApplication).zPageContacts);

			} else if (option == Button.B_TREE_EDIT) {
				optionLocator += " div[id='EDIT_PROPS'] td[id$='_title']";
				page = new DialogEditFolder(MyApplication, ((AppAjaxClient) MyApplication).zPageContacts);

			} else if (option == Button.B_TREE_FOLDER_EMPTY) {
				optionLocator += " div[id='EMPTY_FOLDER'] td[id$='_title']";
				page = new DialogWarning(DialogWarning.DialogWarningID.EmptyFolderWarningMessage, MyApplication,
						((AppAjaxClient) MyApplication).zPageContacts);

			} else {
				throw new HarnessException("implement action:" + action + " option:" + option);
			}

			// Default right-click behavior
			sRightClickAt(actionLocator, "0,0");
			zWaitForBusyOverlay();
			SleepUtil.sleepSmall();

			sClickAt(optionLocator, "0,0");
			zWaitForBusyOverlay();
			SleepUtil.sleepSmall();

			if (page != null) {
				page.zWaitForActive();
			}
			return (page);

		} else {
			throw new HarnessException("implement action:" + action + " option:" + option);
		}
	}

	protected AbsPage zTreeItem(Action action, Button option, FolderItem folderItem) throws HarnessException {

		AbsPage page = null;
		String actionLocator = null;
		String optionLocator = null;

		if ((action == null) || (option == null) || (folderItem == null)) {
			throw new HarnessException("Must define an action, option, and addressbook");
		}

		SleepUtil.sleepSmall();

		logger.info(myPageName() + " zTreeItem(" + action + ", " + option + "," + folderItem.getName() + ")");
		tracer.trace(action + " then " + option + " on Folder Item = " + folderItem.getName());

		if (folderItem.getName().equals("USER_ROOT")) {
			actionLocator = "css=div#ztih__main_Contacts__ADDRBOOK_div";
		} else {
			actionLocator = "css=div#zti__main_Contacts__" + folderItem.getId() + "_div";
		}

		if (action == Action.A_RIGHTCLICK) {

			if (option == Button.B_TREE_NEWFOLDER) {
				optionLocator = "css=div[id^='NEW_ADDRBOOK']";
				page = new DialogCreateFolder(MyApplication, ((AppAjaxClient) MyApplication).zPageContacts);

			} else if (option == Button.B_DELETE) {
				optionLocator = "css=div[id^='ZmActionMenu_contacts_ADDRBOOK'] div[id^='DELETE_WITHOUT_SHORTCUT'] td[id$='_title']";
				page = null;

			} else if (option == Button.B_RENAME) {
				optionLocator = "css=div[id^='ZmActionMenu_contacts_ADDRBOOK'] div[id^='RENAME_FOLDER'] td[id$='_title']";
				page = new DialogRenameFolder(MyApplication, ((AppAjaxClient) MyApplication).zPageContacts);

			} else if (option == Button.B_TREE_EDIT) {
				optionLocator = "css=div[id^='ZmActionMenu_contacts_ADDRBOOK'] div[id^='EDIT_PROPS'] td[id$='_title']";
				page = new DialogEditFolder(MyApplication, ((AppAjaxClient) MyApplication).zPageContacts);

			} else if (option == Button.B_TREE_FOLDER_EMPTY) {
				optionLocator = "css=div[id^='ZmActionMenu_contacts_ADDRBOOK'] div[id^='EMPTY_FOLDER'] td[id$='_title']";
				page = new DialogWarning(DialogWarning.DialogWarningID.EmptyFolderWarningMessage, MyApplication,
						((AppAjaxClient) MyApplication).zPageContacts);

			} else if (option == Button.B_SHARE) {
				optionLocator = "css=div[id='ZmActionMenu_contacts_ADDRBOOK'] div[id='SHARE_ADDRBOOK'] td[id$='_title']";
				page = new DialogShare(MyApplication, ((AppAjaxClient) MyApplication).zPageContacts);

			} else {
				throw new HarnessException("implement action:" + action + " option:" + option);
			}

			if (actionLocator != null) {
				sRightClickAt(actionLocator, "0,0");
				zWaitForBusyOverlay();
				SleepUtil.sleepSmall();
			}

			if (optionLocator != null) {
				sClickAt(optionLocator, "0,0");
				zWaitForBusyOverlay();
				SleepUtil.sleepSmall();
			}

			if (page != null) {
				page.zWaitForActive();
			}

			return page;

		} else if (action == Action.A_LEFTCLICK) {

			if (option == Button.B_TREE_NEWFOLDER) {

				sClickAt("css=div[class^=ImgNewContactsFolder][class*=ZWidget]", "0,0");
				SleepUtil.sleepSmall();
				page = new DialogCreateFolder(MyApplication, ((AppAjaxClient) MyApplication).zPageContacts);

			} else {
				throw new HarnessException("implement action:" + action + " option:" + option);
			}

		} else {
			throw new HarnessException("implement action:" + action + " option:" + option);
		}

		if (page != null) {
			page.zWaitForActive();
		}

		SleepUtil.sleepMedium();

		return page;
	}

	protected AbsPage zTreeItem(Action action, Button option, TagItem t) throws HarnessException {

		if ((action == null) || (option == null) || (t == null)) {
			throw new HarnessException("Must define an action, option, and addressbook");
		}
		AbsPage page = null;
		String actionLocator = null;
		String optionLocator = "css=div[id^='ZmActionMenu_contacts_TAG'] ";

		tracer.trace("processing " + t.getName());

		if (action == Action.A_LEFTCLICK) {
			throw new HarnessException("implement me!");

		} else if (action == Action.A_RIGHTCLICK) {

			actionLocator = "css=td[id^='zti__main_Contacts__']:contains('" + t.getName() + "')";
			this.sRightClickAt(actionLocator, "");
			SleepUtil.sleepSmall();
			this.zWaitForBusyOverlay();

		} else {
			throw new HarnessException("Action " + action + " not yet implemented");
		}

		if (option == Button.B_TREE_NEWTAG) {
			optionLocator += " div[id^='NEW_TAG'] td[id$='_title']";
			page = new DialogTag(MyApplication, ((AppAjaxClient) MyApplication).zPageMail);

		} else if (option == Button.B_DELETE) {
			optionLocator += " div[id^='DELETE_WITHOUT_SHORTCUT'] td[id$='_title']";
			page = new DialogWarning(DialogWarning.DialogWarningID.DeleteTagWarningMessage, MyApplication,
					((AppAjaxClient) MyApplication).zPageContacts);

		} else if (option == Button.B_RENAME) {
			optionLocator += " div[id^='RENAME_TAG'] td[id$='_title']";
			page = new DialogRenameTag(MyApplication, ((AppAjaxClient) MyApplication).zPageContacts);

		} else {
			throw new HarnessException("button " + option + " not yet implemented");
		}

		sClickAt(optionLocator, "");
		this.zWaitForBusyOverlay();
		SleepUtil.sleepMedium();

		if (page != null) {
			page.zWaitForActive();
		}
		return (page);
	}

	public AbsPage zTreeItem(Action action, String locator) throws HarnessException {
		AbsPage page = null;

		SleepUtil.sleepMedium();

		if (locator == "Distribution Lists") {
			locator = "css=td[id='zti__main_Contacts__-18_textCell']";
		}

		if (locator == null)
			throw new HarnessException("locator is null for action " + action);

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("Unable to locator folder in tree " + locator);

		if (action == Action.A_LEFTCLICK) {
			sClickAt(locator, "0,0");
			SleepUtil.sleepLong();

		} else if (action == Action.A_RIGHTCLICK) {

			sRightClickAt(locator, "0,0");
			zWaitForBusyOverlay();
			SleepUtil.sleepSmall();

			return (new ContextMenu(MyApplication));

		} else {
			throw new HarnessException("Action " + action + " not yet implemented");
		}

		return (page);
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		tracer.trace("Click button " + button);

		if (button == null)
			throw new HarnessException("Button cannot be null");

		AbsPage page = null;
		String locator = null;
		String subLocator = null;
		if (button == Button.B_TREE_NEWADDRESSBOOK) {

			locator = "css=div[id=main_Contacts-parent-ADDRBOOK] div[class*=ImgContextMenu]";
			subLocator = "css=div[id^='NEW_ADDRBOOK']";
			page = new DialogCreateFolder(MyApplication, ((AppAjaxClient) MyApplication).zPageContacts);

		} else if (button == Button.B_TREE_NEWTAG) {
			locator = "css=div[id=main_Contacts-parent-TAG] div[class*=ImgContextMenu]";
			subLocator = "css=div[id='NEW_TAG']";
			page = new DialogTag(MyApplication, ((AppAjaxClient) MyApplication).zPageContacts);

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("Button is not present locator=" + locator + " button=" + button);

		this.sClickAt(locator, "");
		this.zWaitForBusyOverlay();
		SleepUtil.sleepSmall();

		if (!this.sIsElementPresent(subLocator))
			throw new HarnessException("Button is not present locator=" + subLocator);

		this.sClickAt(subLocator, "");
		this.zWaitForBusyOverlay();
		SleepUtil.sleepSmall();

		if (page != null) {
			page.zWaitForActive();
		}

		return (page);
	}

	public void zExpand(FolderItem folderItem) throws HarnessException {

		String locator = "css=td#zti__main_Contacts__" + folderItem.getId() + "_nodeCell" + ">div.";
		if (!sIsElementPresent(locator + Locators.COLLAPSE_NODE)) {
			return;
		}
		SleepUtil.sleepMedium();
		if (this.sIsElementPresent(locator + Locators.COLLAPSE_NODE)) {
			sMouseDown(locator + Locators.COLLAPSE_NODE);
			SleepUtil.sleepSmall();
		}
		zWaitForElementPresent(locator + Locators.EXPAND_NODE);
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		return false;
	}
}