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
package com.zimbra.qa.selenium.projects.html.ui.mail;

import java.util.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.html.ui.*;


/**
 * @author zimbra
 *
 */
public class TreeMail extends AbsTree {

	public static class Locators {
	}

	public TreeMail(AbsApplication application) {
		super(application);
		logger.info("new " + TreeMail.class.getCanonicalName());
	}

	protected AbsPage zTreeItem(Action action, Button option, FolderItem folder) throws HarnessException {
		throw new HarnessException("implement me!");
	}

	protected AbsPage zTreeItem(Action action, Button option, SavedSearchFolderItem savedSearchFolder) throws HarnessException {
		throw new HarnessException("implement me!");
	}

	protected AbsPage zTreeItem(Action action, Button option, ZimletItem zimlet) throws HarnessException {
		throw new HarnessException("implement me!");
	}

	protected AbsPage zTreeItem(Action action, FolderItem folder) throws HarnessException {
		throw new HarnessException("locator is null for action "+ action);
	}

	/**
	 * To get whether the tree is collapsed or not
	 * @return true if tree is collapsed, otherwise false
	 * @throws HarnessException 
	 */
	public boolean isCollapsed() throws HarnessException {

		if (sIsElementPresent("ImgNodeCollapsed")) {
			return true;
		} else {
			return false;
		}
	}

	protected AbsPage zTreeItem(Action action, SavedSearchFolderItem savedSearch) throws HarnessException {
		throw new HarnessException("locator is null for action "+ action);
	}

	protected AbsPage zTreeItem(Action action, ZimletItem zimlet) throws HarnessException {
		throw new HarnessException("implement me");
	}

	/* (non-Javadoc)
	 * @see com.zimbra.qa.selenium.framework.ui.AbsTree#zPressButton(com.zimbra.qa.selenium.framework.ui.Button)
	 */
	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		throw new HarnessException("button is null for action");
	}

	public AbsPage zTreeItem(Action action, String locator) throws HarnessException {
		AbsPage page = null;


		if ( locator == null )
			throw new HarnessException("locator is null for action "+ action);

		if ( !this.sIsElementPresent(locator) )
			throw new HarnessException("Unable to locator folder in tree "+ locator);

		if ( action == Action.A_LEFTCLICK ) {

			// FALL THROUGH
		} else if ( action == Action.A_RIGHTCLICK ) {


		} else {
			throw new HarnessException("Action "+ action +" not yet implemented");
		}

		// Default behavior.  Click the locator
		zClick(locator);

		return (page);
	}

	/* (non-Javadoc)
	 * @see framework.ui.AbsTree#zTreeItem(framework.ui.Action, framework.items.FolderItem)
	 */
	public AbsPage zTreeItem(Action action, IItem folder) throws HarnessException {

		// Validate the arguments
		if ( (action == null) || (folder == null) ) {
			throw new HarnessException("Must define an action and addressbook");
		}

		tracer.trace("Click "+ action +" on folder "+ folder.getName());

		if ( folder instanceof FolderItem ) {
			return (zTreeItem(action, (FolderItem)folder));
		} else if ( folder instanceof SavedSearchFolderItem ) {
			return (zTreeItem(action, (SavedSearchFolderItem)folder));
		} else if ( folder instanceof ZimletItem ) {
			return (zTreeItem(action, (ZimletItem)folder));
		}

		throw new HarnessException("Must use FolderItem or SavedSearchFolderItem or ZimletItem as argument, but was "+ folder.getClass());
	}

	@Override
	public AbsPage zTreeItem(Action action, Button option, IItem folder) throws HarnessException {

		// Validate the arguments
		if ( (action == null) || (option == null) || (folder == null) ) {
			throw new HarnessException("Must define an action, option, and addressbook");
		}

		tracer.trace("Click "+ action +" then "+ option +" on folder "+ folder.getName());

		if ( folder instanceof FolderItem ) {
			return (zTreeItem(action, option, (FolderItem)folder));
		} else if ( folder instanceof SavedSearchFolderItem ) {
			return (zTreeItem(action, option, (SavedSearchFolderItem)folder));
		} else if ( folder instanceof ZimletItem ) {
			return (zTreeItem(action, option, (ZimletItem)folder));
		}

		throw new HarnessException("Must use FolderItem or SavedSearchFolderItem or ZimletItem as argument, but was "+ folder.getClass());
	}


	/**
	 * Used for recursively building the tree list
	 * @param top
	 * @return
	 * @throws HarnessException
	 */
	private List<FolderItem>zListGetFolders(String top) throws HarnessException {
		List<FolderItem> items = new ArrayList<FolderItem>();

		String searchLocator = top + " div[class='DwtComposite']";

		int count = this.sGetCssCount(searchLocator);
		for ( int i = 1; i <= count; i++) {
			String itemLocator = searchLocator + " div:nth-of-type("+ i +") ";

			if ( !this.sIsElementPresent(itemLocator) ) {
				continue;
			}

			String locator;

			// Strip the ID from the DOM
			String id = sGetAttribute(itemLocator + "[id^='zti__main_Mail__']@id");
			if ( id == null || id.trim().length() == 0 || !(id.startsWith("zti__main_Mail__")) ) {
				// Not a folder
				// Maybe "Find Shares ..."
				continue;
			}

			FolderItem item = new FolderItem();

			// Set the locator
			// TODO: This could probably be made safer, to make sure the id matches an int pattern
			item.setId(id.replace("zti__main_Mail__", ""));

			// Set the name
			locator = itemLocator + " td[id$='_textCell']";
			item.setName(this.sGetText(locator));

			// Set the expanded boolean
			locator = itemLocator + " td[id$='_nodeCell']>div[class*=ImgNodeExpanded]";
			item.gSetIsExpanded( sIsElementPresent(locator) );

			items.add(item);

			// Add any sub folders
			items.addAll(zListGetFolders(itemLocator));


		}

		return (items);

	}

	public List<FolderItem> zListGetFolders() throws HarnessException {

		List<FolderItem> items = new ArrayList<FolderItem>();

		// Recursively fill out the list, starting with all mail folders
		items.addAll(zListGetFolders("div[id='ztih__main_Mail__FOLDER']"));

		return (items);

	}

	public List<SavedSearchFolderItem> zListGetSavedSearches() throws HarnessException {

		List<SavedSearchFolderItem> items = new ArrayList<SavedSearchFolderItem>();

		// TODO: implement me!

		// Return the list of items
		return (items);

	}

	public List<TagItem> zListGetTags() throws HarnessException {


		List<TagItem> items = new ArrayList<TagItem>();

		// TODO: implement me!

		// Return the list of items
		return (items);


	}

	public enum FolderSectionAction {
		Expand,
		Collapse
	}

	public enum FolderSection {
		Folders,
		Searches,
		Tags,
		Zimlets
	}

	/**
	 * Apply an expand/collpase to the Folders, Searches, Tags and Zimlets sections
	 * @param a
	 * @param section
	 * @throws HarnessException
	 */
	public AbsPage zSectionAction(FolderSectionAction action, FolderSection section) throws HarnessException {
		throw new HarnessException("section is null for action "+ action);
	}


	/* (non-Javadoc)
	 * @see framework.ui.AbsTree#myPageName()
	 */
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the main page is active
		if ( !((AppHtmlClient)MyApplication).zPageMail.zIsActive() ) {
			((AppHtmlClient)MyApplication).zPageMail.zNavigateTo();
		}

		String locator = "//span[@title='Inbox']";
		boolean loaded = this.sIsElementPresent(locator);
		if ( !loaded )
			return (false);

		return (loaded);

	}


}
