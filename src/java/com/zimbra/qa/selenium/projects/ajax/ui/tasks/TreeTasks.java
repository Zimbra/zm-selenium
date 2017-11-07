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
package com.zimbra.qa.selenium.projects.ajax.ui.tasks;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.ui.*;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DialogEditFolder;

public class TreeTasks extends AbsTree {

	public static class Locators {
		public static final String ztih__main_Tasks__ZIMLET_ID = "ztih__main_Tasks__ZIMLET";
		public static final String ztih__main_Mail__ZIMLET_nodeCell_ID = "ztih__main_Mail__ZIMLET_nodeCell";
		public static final String zNewTagIcon = "//td[contains(@class,'overviewHeader-Text FakeAnchor')]/div[contains(@class,'ImgNewTag')]";
		public static final String zTagsHeader = "//td[contains(@id,'ztih__main_Tasks__TAG_textCell')]";
		public static final String zDeleteTreeMenuItem = "css=div[id^='DELETE_WITHOUT_SHORTCUT'] tr[id='POPUP_DELETE_WITHOUT_SHORTCUT']";
		public static final String zRenameTreeMenuItem = "css=tr#POPUP_RENAME_FOLDER";
		public static final String zEditTreeMenuItem = "css=tr#POPUP_EDIT_PROPS";
		public static final String zRenameTagTreeMenuItem = "css=div[id='RENAME_TAG'] tr[id='POPUP_RENAME_TAG']";
		public static final String zNewTagTreeMenuItem = "css=div[id='NEW_TAG'] tr[id='POPUP_NEW_TAG']";
		public static final String zShareTreeMenuItem = "css=div[id='SHARE_TASKFOLDER'] tr[id='POPUP_SHARE_TASKFOLDER']";
	}

	public TreeTasks(AbsApplication application) {
		super(application);
		logger.info("new " + TreeTasks.class.getCanonicalName());
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		tracer.trace("Press the " + button + " button");

		if (button == null)
			throw new HarnessException("Button cannot be null");

		AbsPage page = null;
		String locator = null;
		if (button == Button.B_TREE_NEWTAG) {

			locator = Locators.zNewTagIcon;
			if (!this.sIsElementPresent(locator)) {
				throw new HarnessException("Unable to locator folder in tree " + locator);
			}
			page = new DialogTag(MyApplication, ((AppAjaxClient) MyApplication).zPageTasks);

		} else if (button == Button.B_TREE_NEWTASKLIST) {

			locator = "css=div[id=ztih__main_Tasks__TASK] div[class^=ImgNewTaskList ZWidget]";
			page = new DialogCreateTaskFolder(MyApplication, ((AppAjaxClient) MyApplication).zPageTasks);

			if (!this.sIsElementPresent(locator)) {
				throw new HarnessException("Unable to locate folder in the tree " + locator);
			}
			this.zClickAt(locator, "0,0");
			zWaitForBusyOverlay();
			SleepUtil.sleepSmall();

			return page;

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (locator == null) {
			throw new HarnessException("locator was null for button " + button);
		}

		this.zClickAt(locator, "");
		this.zWaitForBusyOverlay();
		SleepUtil.sleepSmall();

		if (page != null) {
			page.zWaitForActive();

		}
		return (page);
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

			pulldownLocator = "css=div[id='zov__main_Tasks'] td[id='ztih__main_Tasks__TASK_optCell'] div[class*=ImgContextMenu]";

			if (option == Button.B_TREE_NEWTASKLIST) {

				optionLocator = "css=div[id='ZmActionMenu_tasks_TASK'] div[id='NEW_TASK_FOLDER'] td[id$='_title']";
				page = new DialogCreateTaskFolder(MyApplication, ((AppAjaxClient) MyApplication).zPageTasks);

			} else {
				throw new HarnessException("Pulldown/Option " + pulldown + "/" + option + " not implemented");
			}

		} else if (pulldown == Button.B_TREE_TAGS_OPTIONS) {

			pulldownLocator = "css=div[id='zov__main_Tasks'] td[id='ztih__main_Tasks__TAG_optCell'] div[class*=ImgContextMenu]";

			if (option == Button.B_TREE_NEWTAG) {
				optionLocator = "css=div[id='ZmActionMenu_tasks_TAG'] div[id='NEW_TAG'] td[id$='_title']";
				page = new DialogTag(MyApplication, ((AppAjaxClient) MyApplication).zPageTasks);

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

			this.zClickAt(pulldownLocator, "0,0");
			zWaitForBusyOverlay();

			if (optionLocator != null) {

				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator "
							+ optionLocator + " not present!");
				}

				this.zClickAt(optionLocator, "0,0");
				zWaitForBusyOverlay();
			}

			if (page != null) {
				page.zWaitForActive();
			}
		}

		SleepUtil.sleepMedium();
		return (page);
	}

	public AbsPage zTreeItem(Action action, IItem tasklist) throws HarnessException {

		tracer.trace(action + " on folder = " + tasklist.getName());

		AbsPage page = null;
		String locator = null;

		if (!(tasklist instanceof FolderItem))
			throw new HarnessException("folder must be of type FolderItem");

		FolderItem f = (FolderItem) tasklist;

		if (action == Action.A_LEFTCLICK) {
			locator = "zti__main_Tasks__" + f.getId() + "_textCell";

		} else if (action == Action.A_RIGHTCLICK) {
			locator = "zti__main_Tasks__" + f.getId() + "_textCell";

			this.zRightClick(locator);
			return (new ContextMenu(MyApplication));

		} else {
			throw new HarnessException("Action " + action + " not yet implemented");
		}

		zClick(locator);
		this.zWaitForBusyOverlay();
		SleepUtil.sleepSmall();

		return (page);

	}

	@Override
	public AbsPage zTreeItem(Action action, Button option, IItem tasklist) throws HarnessException {

		logger.info(myPageName() + " zListItem(" + action + ", " + option + ", " + tasklist + ")");

		if (action == null)
			throw new HarnessException("action cannot be null");
		if (option == null)
			throw new HarnessException("button cannot be null");
		if (tasklist == null)
			throw new HarnessException("folder cannot be null");

		tracer.trace(action + " then " + option + " on task = " + tasklist.getName());

		AbsPage page = null;
		String actionLocator = null;
		String optionLocator = null;

		if (!(tasklist instanceof TagItem))
			throw new HarnessException("folder must be of type FolderItem");

		TagItem t = (TagItem) tasklist;

		tracer.trace("processing " + t.getName());

		if (action == Action.A_LEFTCLICK) {
			actionLocator = "implement me";

		} else if (action == Action.A_RIGHTCLICK) {

			actionLocator = "css=td[id^='zti__main_Tasks__']:contains('" + t.getName() + "')";
			this.zRightClickAt(actionLocator, "");
			page = new DialogTag(MyApplication, ((AppAjaxClient) MyApplication).zPageTasks);

		} else {
			throw new HarnessException("Action " + action + " not yet implemented");
		}

		if (option == Button.B_TREE_NEWTAG) {

			optionLocator = Locators.zNewTagTreeMenuItem;

		} else if (option == Button.B_DELETE) {

			optionLocator = Locators.zDeleteTreeMenuItem;

			page = new DialogWarning(DialogWarning.DialogWarningID.DeleteTagWarningMessage, MyApplication,
					((AppAjaxClient) MyApplication).zPageTasks);

		} else if (option == Button.B_RENAME) {

			optionLocator = Locators.zRenameTagTreeMenuItem;

			page = new DialogRenameTag(MyApplication, ((AppAjaxClient) MyApplication).zPageTasks);

		} else {
			throw new HarnessException("button " + option + " not yet implemented");
		}

		if (optionLocator == null)
			throw new HarnessException("locator is null for option " + option);

		sClickAt(optionLocator, "");
		this.zWaitForBusyOverlay();
		SleepUtil.sleepSmall();

		if (page != null) {
			page.zWaitForActive();
		}

		return (page);
	}

	public AbsPage zTreeItem(Action action, Button option, FolderItem folderItem) throws HarnessException {

		if (action == null)
			throw new HarnessException("action cannot be null");
		if (option == null)
			throw new HarnessException("button cannot be null");
		if (folderItem == null)
			throw new HarnessException("folder cannot be null");

		tracer.trace(action + " on folder = " + folderItem.getName());

		AbsPage page = null;
		String actionLocator = null;
		String optionLocator = null;
		// String locator = null;

		if (action == Action.A_LEFTCLICK) {
			actionLocator = "zti__main_Tasks__" + folderItem.getId() + "_textCell";

		} else if (action == Action.A_RIGHTCLICK) {
			actionLocator = "zti__main_Tasks__" + folderItem.getId() + "_textCell";

			this.zRightClickAt(actionLocator, "0,0");
			this.zWaitForBusyOverlay();
			SleepUtil.sleepSmall();

		} else {
			throw new HarnessException("Action " + action + " not yet implemented");
		}

		if (option == Button.B_DELETE) {

			optionLocator = Locators.zDeleteTreeMenuItem;
			page = null;

		} else if (option == Button.B_RENAME) {

			optionLocator = Locators.zRenameTreeMenuItem;

			page = new DialogRenameFolder(MyApplication, ((AppAjaxClient) MyApplication).zPageTasks);

		} else if (option == Button.B_TREE_EDIT) {

			optionLocator = Locators.zEditTreeMenuItem;
			page = new DialogEditFolder(MyApplication, ((AppAjaxClient) MyApplication).zPageMail);

		} else if (option == Button.B_SHARE) {

			optionLocator = Locators.zShareTreeMenuItem;
			page = new DialogShare(MyApplication, ((AppAjaxClient) MyApplication).zPageTasks);

		} else {
			throw new HarnessException("button " + option + " not yet implemented");
		}

		if (optionLocator == null)
			throw new HarnessException("locator is null for option " + option);

		zClickAt(optionLocator, "");
		this.zWaitForBusyOverlay();
		SleepUtil.sleepSmall();

		if (page != null) {
			page.zWaitForActive();
		}
		return (page);
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		if (!((AppAjaxClient) MyApplication).zPageTasks.zIsActive()) {
			((AppAjaxClient) MyApplication).zPageTasks.zNavigateTo();
		}

		String locator = Locators.ztih__main_Tasks__ZIMLET_ID;

		boolean loaded = this.sIsElementPresent(locator);
		if (!loaded)
			return (false);

		return (loaded);
	}
}