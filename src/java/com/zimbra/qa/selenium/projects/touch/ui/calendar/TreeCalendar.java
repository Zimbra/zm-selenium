/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.touch.ui.calendar;

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
import com.zimbra.qa.selenium.projects.touch.ui.*;


/**
 * @author zimbra
 *
 */
public class TreeCalendar extends AbsTree {

	public static class Locators {
		
		public static final String MainDivID = "zov__main_Calendar";
		public static final String CreateNewFolderIconCSS = "css=div[id='"+ MainDivID +"'] table[id='ztih__main_Calendar__CALENDAR_table'] td[id$='_title']";
		public static final String ztih__main_Calendar__ZIMLETCSS = "css=div[id='ztih__main_Calendar__ZIMLET']";
		public static final String RenameTagMenu = "css=div[id='RENAME_TAG'] td[id='RENAME_TAG_title']";
		public static final String DeleteTagMenu = "css=div[id='DELETE_WITHOUT_SHORTCUT'] td[id='DELETE_WITHOUT_SHORTCUT_title']";
		
	}
	
	public TreeCalendar(AbsApplication application) {
		super(application);
		logger.info("new " + TreeCalendar.class.getCanonicalName());
	}

	protected AbsPage zTreeItem(Action action, Button option, FolderItem folder) throws HarnessException {
		if ( (action == null) || (option == null) || (folder == null) ) {
			throw new HarnessException("Must define an action, option, and addressbook");
		}
		tracer.trace("processing " + folder.getName());

		String actionLocator = String.format("css=div[id='zti__main_Calendar__%s'] td[id$='_textCell']", folder.getId());
		String optionLocator = null;
		AbsPage page = null;

		// Special case for clicking on "Calendars" header rather than a specific Calendar Item
		if ( folder.getName().equals("USER_ROOT") ) {
			
			if ( (action == Action.A_RIGHTCLICK) && (option == Button.O_NEW_CALENDAR || option == Button.O_NEW_FOLDER) ) {
				
				// TODO: I18N

				actionLocator = "css=td[id='ztih__main_Calendar__CALENDAR_textCell']"; // override the default
				optionLocator = "css=table[class$='MenuTable'] td[id$='_title']:contains(New Calendar)";
				page = null;

				zRightClick(actionLocator);
				zClick(optionLocator);
				this.zWaitForBusyOverlay();

				return (page);
			}

		}
		
		// After  clicking REFRESH, sometimes the links don't appear right away
		GeneralUtility.waitForElementPresent(this, actionLocator);
		
		optionLocator = "css=div[id='ZmActionMenu_calendar_CALENDAR']";
		if ( (action == Action.A_RIGHTCLICK) && (option == Button.B_DELETE) ) {

			// Use default actionLocator
			// See http://bugzilla.zimbra.com/show_bug.cgi?id=64023 ... POPUP_ needs to be updated
			optionLocator += " div[id^='DELETE_WITHOUT_SHORTCUT'] td[id$='_title']";
			page = null;

			this.zRightClick(actionLocator);

			// FALL THROUGH
			
		} else if ( (action == Action.A_RIGHTCLICK) && (option == Button.B_TREE_EDIT) ) {
			
			// Use default actionLocator
			optionLocator += " div[id^='EDIT_PROPS'] td[id$='_title']";
			page = null;

			this.zRightClick(actionLocator);

			// FALL THROUGH

		} else if ( (action == Action.A_RIGHTCLICK) && (option == Button.B_MOVE) ) {
			
			// Use default actionLocator
			optionLocator += " div[id^='MOVE'] td[id$='_title']";
			page = null;

			this.zRightClick(actionLocator);

			// FALL THROUGH

		} else if ( (action == Action.A_RIGHTCLICK) && (option == Button.B_SHARE) ) {
			
			// Use default actionLocator
			optionLocator += " div[id^='SHARE_CALENDAR'] td[id$='_title']";
			page = null;

			this.zRightClick(actionLocator);

			// FALL THROUGH

		} else if ( (action == Action.A_RIGHTCLICK) && (option == Button.B_RELOAD) ) {
			
			// Use default actionLocator
			optionLocator += " div[id^='SYNC'] td[id$='_title']";
			page = null;

			this.zRightClick(actionLocator);

			// FALL THROUGH

		} else if ( (action == Action.A_RIGHTCLICK) && (option == Button.B_LAUNCH_IN_SEPARATE_WINDOW) ) {
			
			// Use default actionLocator
			optionLocator += " div[id^='DETACH_WIN'] td[id$='_title']";

			this.zRightClick(actionLocator);
			this.zClickAt(optionLocator,"");
			
			page = null;
			
			this.zWaitForBusyOverlay();
			
			return (page);

		} else if ( (action == Action.A_RIGHTCLICK) && (option == Button.B_RECOVER_DELETED_ITEMS) ) {
			
			// Use default actionLocator
			optionLocator += " div[id^='RECOVER_DELETED_ITEMS'] td[id$='_title']";
			page = null; // TODO

			this.zRightClick(actionLocator);

			// FALL THROUGH

		} else {
			throw new HarnessException("No logic defined for action "+ action +" with option "+ option);
		}


		if (actionLocator == null)
			throw new HarnessException("locator is null for action " + action);

		// Default behavior. Click the locator
		this.zClickAt(optionLocator,"");
		this.zWaitForBusyOverlay();
		
		return page;
	}

	protected AbsPage zTreeItem(Action action, Button option, ZimletItem zimlet) throws HarnessException {
		throw new HarnessException("implement me!");
	}
	
	protected AbsPage zTreeItem(Action action, Button option, TagItem folder) throws HarnessException {
		if ( (action == null) || (option == null) || (folder == null) ) {
			throw new HarnessException("Must define an action, option, and calendar");
		}
		tracer.trace("processing " + folder.getName());
		
		String optionLocator = null;
		optionLocator = "css=div[id='ztih__main_Calendar__TAG'] td[id*='zti__main_Calendar__']:contains('" + folder.getName() + "')";

		if ( action == Action.A_LEFTCLICK ) {

			// Select the folder
			this.zClick(optionLocator);

			// return a context menu
			return null;
			
		} else if ( action == Action.A_RIGHTCLICK && option == Button.B_RENAME) {

			// Select tag
			this.zRightClick(optionLocator);
			this.zClick(Locators.RenameTagMenu);

			return null;
		
		} else if ( action == Action.A_RIGHTCLICK && option == Button.B_DELETE) {

			// Select tag
			this.zRightClick(optionLocator);
			this.zClick(Locators.DeleteTagMenu);
			
			return null;

		} else {
			throw new HarnessException("Action "+ action +" not yet implemented");
		}
		
	}

	protected AbsPage zTreeItem(Action action, ZimletItem zimlet) throws HarnessException {
		throw new HarnessException("implement me");
	}

	public AbsPage zPressPulldown(Button pulldown, Button option) throws HarnessException {
		logger.info(myPageName() + " zPressPulldown("+ pulldown +", "+ option +")");

		tracer.trace("Click "+ pulldown +" then "+ option);

		if ( pulldown == null )
			throw new HarnessException("Pulldown cannot be null");

		if ( option == null )
			throw new HarnessException("Option cannot be null");


		AbsPage page = null;
		String pulldownLocator = null;
		String optionLocator = null;
		
		
		if ( pulldown == Button.B_TREE_FOLDERS_OPTIONS ) {
			
			pulldownLocator = "css=div[id='zov__main_Calendar'] td[id='ztih__main_Calendar__CALENDAR_optCell'] div[class*=ImgContextMenu]";
			
			if ( option == Button.B_TREE_NEWFOLDER ) {
				
				optionLocator = "css=div[id='ZmActionMenu_calendar_CALENDAR'] div[id='NEW_CALENDAR'] td[id$='_title']";
				page = null;
			
				/**
				 * TODO: add other options:
				 * 
				optionLocator = "css=div[id='ZmActionMenu_calendar_CALENDAR'] div[id='ADD_EXTERNAL_CALENDAR'] td[id$='_title']";
				optionLocator = "css=div[id='ZmActionMenu_calendar_CALENDAR'] div[id='CHECK_ALL'] td[id$='_title']";
				optionLocator = "css=div[id='ZmActionMenu_calendar_CALENDAR'] div[id='CLEAR_ALL'] td[id$='_title']";
				optionLocator = "css=div[id='ZmActionMenu_calendar_CALENDAR'] div[id='FREE_BUSY_LINK'] td[id$='_title']";

				 */

			} else if ( option == Button.B_TREE_NEW_EXTERNAL_CALENDAR ) {
				
				optionLocator = "css=div[id='ZmActionMenu_calendar_CALENDAR'] div[id='ADD_EXTERNAL_CALENDAR'] td[id$='_title']";
				page = null;
				
				// FALL THROUGH
				
			} else {
				throw new HarnessException("Pulldown/Option "+ pulldown +"/"+ option +" not implemented");
			}

			// FALL THROUGH
			
		} else if ( pulldown == Button.B_TREE_TAGS_OPTIONS ) {
			
			pulldownLocator = "css=div[id='zov__main_Calendar'] td[id='ztih__main_Mail__TAG_optCell'] td[id$='_title']";
			
			if ( option == Button.B_TREE_NEWTAG ) {

				optionLocator = "css=div[id='ZmActionMenu_calendar_TAG'] div[id='NEW_TAG'] td[id$='_title']";
				page = null;

			} else {
				throw new HarnessException("Pulldown/Option "+ pulldown +"/"+ option +" not implemented");
			}

			// FALL THROUGH
			
		} else {
			throw new HarnessException("Pulldown/Option "+ pulldown +"/"+ option +" not implemented");
		}
		
		

		// Default behavior
		if (pulldownLocator != null) {

			// Make sure the locator exists
			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " option " + option + " pulldownLocator " + pulldownLocator + " not present!");
			}

			// 8.0 change ... need zClickAt()
			// this.zClick(pulldownLocator);
			this.zClickAt(pulldownLocator, "0,0");

			// If the app is busy, wait for it to become active
			zWaitForBusyOverlay();

			if (optionLocator != null) {

				// Make sure the locator exists
				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException("Button " + pulldown + " option " + option + " optionLocator " + optionLocator + " not present!");
				}

				// 8.0 change ... need zClickAt()
				// this.zClick(optionLocator);
				this.zClickAt(optionLocator, "0,0");

				// If the app is busy, wait for it to become active
				zWaitForBusyOverlay();
			}

		}
		
		
		// Return the specified page, or null if not set
		return (page);


	}

	/* (non-Javadoc)
	 * @see com.zimbra.qa.selenium.framework.ui.AbsTree#zPressButton(com.zimbra.qa.selenium.framework.ui.Button)
	 */
	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {

		tracer.trace("Click "+ button);

		if ( button == null )
			throw new HarnessException("Button cannot be null");

		AbsPage page = null;
		String locator = null;

		if ( button == Button.B_TREE_NEWFOLDER ) {
			
			return (zPressPulldown(Button.B_TREE_FOLDERS_OPTIONS, Button.B_TREE_NEWFOLDER));

		} else if (button == Button.B_TREE_NEWTAG) {

			return (zPressPulldown(Button.B_TREE_TAGS_OPTIONS, Button.B_TREE_NEWTAG));

		} else if ( button == Button.B_TREE_FIND_SHARES ) {

			locator = "css=TODO#TODO";
			page = null;

			// Use sClick, not default zClick
			this.sClick(locator);

			// If the app is busy, wait for that to finish
			this.zWaitForBusyOverlay();

			return (page);

		}else if (button == Button.B_TREE_SHOW_REMAINING_FOLDERS ) {

			locator = "css=TODO#TODO";
			page = null;

			if (!this.sIsElementPresent(locator)) {
				throw new HarnessException("Unable to find 'show remaining folders' in tree " + locator);
			}
			
			// FALL THROUGH
			
		} else {
			throw new HarnessException("no logic defined for button "+ button);
		}

		// Click it
		this.zClick(locator);

		// If the app is busy, wait for that to finish
		this.zWaitForBusyOverlay();

		return (page);

	}

	public AbsPage zTreeItem(Action action, String locator) throws HarnessException {
		
		locator = "css=td[id^='zti__main_Calendar']:contains('" + locator + "')";

		if ( !this.sIsElementPresent(locator) )
			throw new HarnessException("Unable to find tag in tree "+ locator);

		if ( action == Action.A_LEFTCLICK ) {

			// Select the folder
			this.zClick(locator);

			// return a context menu
			return null;
			
		} else if ( action == Action.A_RIGHTCLICK ) {

			// Select the folder
			this.zRightClick(locator);

			// return a context menu
			return null;

		} else {
			throw new HarnessException("Action "+ action +" not yet implemented");
		}
	}


	/* (non-Javadoc)
	 * @see framework.ui.AbsTree#zTreeItem(framework.ui.Action, framework.items.FolderItem)
	 */
	public AbsPage zTreeItem(Action action, IItem folder) throws HarnessException {

		tracer.trace("Click "+ action +" on folder "+ folder.getName());

		// Validate the arguments
		if ( (action == null) || (folder == null) ) {
			throw new HarnessException("Must define an action and addressbook");
		}

		if ( folder instanceof FolderItem ) {
			return (zTreeItem(action, (FolderItem)folder));
		} else if ( folder instanceof SavedSearchFolderItem ) {
			return (zTreeItem(action, (SavedSearchFolderItem)folder));
		} else if ( folder instanceof ZimletItem ) {
			return (zTreeItem(action, (ZimletItem)folder));
		}

		throw new HarnessException("Must use FolderItem or SavedSearchFolderItem or ZimletItem as argument, but was "+ folder.getClass());
	}
	
	public void zSelectMountedFolder(String folderName) throws HarnessException {
		
		tracer.trace("Click on folder "+ folderName);
		
		SleepUtil.sleepSmall();
		this.sClickAt("css=div[id='main_Calendar-parent-CALENDAR'] div[id^='zti__main_Calendar__'] td[id$='_textCell']:contains('" + folderName + "')", "");
		this.sClickAt("css=div[id='main_Calendar-parent-CALENDAR'] div[id^='zti__main_Calendar__'][class^='DwtTreeItem-selected'] td[id$='_checkboxCell'] div[class='ImgMenuCheck']", "");
		this.zWaitForBusyOverlay();
		SleepUtil.sleepLong(); //Let calendar UI refresh
	}
	
	public void zDeSelectCalendarFolder(String folderName) throws HarnessException {
		
		tracer.trace("Click on folder "+ folderName);
		
		SleepUtil.sleepSmall();
		this.sClickAt("css=div[id='main_Calendar-parent-CALENDAR'] div[id^='zti__main_Calendar__'] td[id$='_textCell']:contains('" + folderName + "')", "");
		this.sClickAt("css=div[id='main_Calendar-parent-CALENDAR'] div[id^='zti__main_Calendar__'][class^='DwtTreeItem-selected'] td[id$='_checkboxCell'] div[class='ImgMenuCheck']", "");
		this.zWaitForBusyOverlay();
		SleepUtil.sleepMedium(); //Let calendar UI refresh
	}
	
	@Override
	public AbsPage zTreeItem(Action action, Button option, IItem folder) throws HarnessException {

		tracer.trace("Click "+ action +" then "+ option +" on folder "+ folder.getName());

		// Validate the arguments
		if ( (action == null) || (option == null) || (folder == null) ) {
			throw new HarnessException("Must define an action, option, and folder");
		}

		if ( folder instanceof FolderItem ) {
			return (zTreeItem(action, option, (FolderItem)folder));
		} else if ( folder instanceof SavedSearchFolderItem ) {
			return (zTreeItem(action, option, (SavedSearchFolderItem)folder));
		} else if ( folder instanceof ZimletItem ) {
			return (zTreeItem(action, option, (ZimletItem)folder));
		}else if ( folder instanceof TagItem ) {
			return (zTreeItem(action, option, (TagItem)folder));
		}

		throw new HarnessException("Must use TagItem FolderItem or SavedSearchFolderItem or ZimletItem as argument, but was "+ folder.getClass());
	}


	private FolderItem parseFolderRow(String id) throws HarnessException {
	
		String locator;

		FolderItem item = new FolderItem();

		item.setId(id);

		// Set the name
		locator = "css=div[id='zti__main_Calendar__"+ id +"'] td[id$='_textCell']";
		item.setName(this.sGetText(locator));

		// Set the expanded boolean
		locator = "css=div[id='zti__main_Calendar__"+ id +"'] td[id$='_nodeCell']>div";
		if ( sIsElementPresent(locator) ) {
			// The image could be hidden, if there are no subfolders
			item.gSetIsExpanded("ImgNodeExpanded".equals(sGetAttribute(locator + "@class")));
		}
		
		// Set the selected boolean
		locator = "css=div[id='zti__main_Calendar__"+ id +"'] div[id='zti__main_Calendar__"+ id +"_div']";
		if ( sIsElementPresent(locator) ) {
			item.gSetIsSelected("DwtTreeItem-selected".equals(sGetAttribute(locator + "@class")));
		}

		// TODO: color

		return (item);
	}
	
	/**
	 * Used for recursively building the tree list for Mail Folders
	 * @param css
	 * @return
	 * @throws HarnessException
	 */
	private List<FolderItem> zListGetFolders(String css) throws HarnessException {
		List<FolderItem> items = new ArrayList<FolderItem>();

		String searchLocator = css + " div[class='DwtComposite']";

		int count = this.sGetCssCount(searchLocator);
		logger.debug(myPageName() + " zListGetFolders: number of folders: "+ count);

		for ( int i = 1; i <= count; i++) {
			String itemLocator = searchLocator + ":nth-child("+i+")";

			if ( !this.sIsElementPresent(itemLocator) ) {
				continue;
			}
			
			String identifier = sGetAttribute(itemLocator +"@id");
			logger.debug(myPageName() + " identifier: "+ identifier);

			if ( identifier == null || identifier.trim().length() == 0 || !(identifier.startsWith("zti__main_Calendar__")) ) {
				// Not a folder
				// Maybe "Find Shares ..."
				count++; // Add one more to the total 'count' for this 'unknown' item
				continue;
			}

			// Set the locator
			// TODO: This could probably be made safer, to make sure the id matches an int pattern
			String id = identifier.replace("zti__main_Calendar__", "");

			FolderItem item = this.parseFolderRow(id);
			items.add(item);
			logger.info(item.prettyPrint());

			// Add any sub folders
			items.addAll(zListGetFolders(itemLocator));


		}

		return (items);

	}

	/**
    * Used for recursively building the tree list for Saved Search Folders
    * @param top
    * @return
    * @throws HarnessException
    */
   private List<SavedSearchFolderItem>zListGetSavedSearchFolders(String top) throws HarnessException {
      List<SavedSearchFolderItem> items = new ArrayList<SavedSearchFolderItem>();

      String searchLocator = top + "//div[@class='DwtComposite']";

      int count = this.sGetXpathCount(searchLocator);
      for ( int i = 1; i <= count; i++) {
         String itemLocator = searchLocator + "["+ i + "]";

         if ( !this.sIsElementPresent(itemLocator) ) {
            continue;
         }

         String locator;

         String id = sGetAttribute("xpath=("+ itemLocator +"/.)@id");
         if ( id == null || id.trim().length() == 0 || !(id.startsWith("zti__main_Mail__")) ) {
            // Not a folder
            // Maybe "Find Shares ..."
            continue;
         }

         SavedSearchFolderItem item = new SavedSearchFolderItem();

         // Set the locator
         // TODO: This could probably be made safer, to make sure the id matches an int pattern
         item.setId(id.replace("zti__" + ((AppTouchClient)MyApplication).zGetActiveAccount().EmailAddress +
               ":main_Mail__", ""));

         // Set the name
         locator = itemLocator + "//td[contains(@id, '_textCell')]";
         item.setName(this.sGetText(locator));

         // Set the expanded boolean
         locator = itemLocator + "//td[contains(@id, '_nodeCell')]/div";
         if ( sIsElementPresent(locator) ) {
            // The image could be hidden, if there are no subfolders
            //item.gSetIsExpanded("ImgNodeExpanded".equals(sGetAttribute("xpath=("+ locator + ")@class")));
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

		// TODO: implement me!

		// Return the list of items
		return (items);


	}

	public List<ZimletItem> zListGetZimlets() throws HarnessException {
		throw new HarnessException("implement me!");
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
		throw new HarnessException("implement me!");
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
		if ( !((AppTouchClient)MyApplication).zPageCalendar.zIsActive() ) {
			((AppTouchClient)MyApplication).zPageCalendar.zNavigateTo();
		}

		// Zimlets seem to be loaded last
		// So, wait for the zimlet div to load
		String locator = Locators.ztih__main_Calendar__ZIMLETCSS;

		boolean loaded = this.sIsElementPresent(locator);
		if ( !loaded )
			return (false);

		return (loaded);

	}


}
