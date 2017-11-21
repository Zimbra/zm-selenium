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
/**
 * 
 */
package com.zimbra.qa.selenium.projects.touch.pages.contacts;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;

public class MoveContactView extends AbsTree {
    public static final String NEW_FOLDER="css=#ztih__main_Contacts__ADDRBOOK_table tbody tr td:nth-child(4)";
    public static final String COLLAPSE_TREE="css#ztih__main_Contacts__ADDRBOOK_nodeCell";
	public static class Locators {
		public static final String EXPAND_NODE  = "ImgNodeExpanded";
		public static final String COLLAPSE_NODE= "ImgNodeCollapsed";
	}
	
	public MoveContactView(AbsApplication application) {
		super(application);
		logger.info("new " + MoveContactView.class.getCanonicalName());
	}
	
	
	/* (non-Javadoc)
	 * @see framework.ui.AbsTree#zTreeItem(framework.ui.Action, framework.items.FolderItem)
	 */
	public AbsPage zTreeItem(Action action, IItem addressbook) throws HarnessException {
		tracer.trace("Click "+ action +" on addressbook "+ addressbook);

		// Validate the arguments
		if ( (action == null) || (addressbook == null) ) {
			throw new HarnessException("Must define an action and addressbook");
		}
		
		if ( !(addressbook instanceof FolderItem) ) {
			throw new HarnessException("Must use FolderItem as argument, but was "+ addressbook.getClass());
		}
				
		AbsPage page = null;
		String locator = null;
		
		if ( action == Action.A_LEFTCLICK ) {
			
			// choose target addressbook to move contact to (done for TP)
			locator = "css=div[class='zcs-menu-label']:contains('"+addressbook.getName()+"')";
			page = null;
		
		} else {
			throw new HarnessException("Action "+ action +" not yet implemented");
		}

		if ( !this.sIsElementPresent(locator) ) {
			throw new HarnessException("Unable to locator folder in tree "+ locator);
	    }
	
		// By default, left click at locator
		sClick(locator);
		zWaitForBusyOverlay();									

		return (page);
	}
	
	@Override
	public AbsPage zTreeItem(Action action, Button option, IItem folder) throws HarnessException {

		tracer.trace("Click "+ action +" then "+ option +" on folder "+ folder.getName());

		// Validate the arguments
		if ( (action == null) || (option == null) || (folder == null) ) {
			throw new HarnessException("Must define an action, option, and addressbook");
		}

		if ( folder instanceof FolderMountpointItem ) {
			return (zTreeItem(action, option, (FolderMountpointItem)folder));
		} else if ( folder instanceof SavedSearchFolderItem ) {
			return (zTreeItem(action, option, (SavedSearchFolderItem)folder));
		//} else if ( folder instanceof ZimletItem ) {
		//	return (zTreeItem(action, option, (ZimletItem)folder));
		} else if ( folder instanceof TagItem ) {
			return (zTreeItem(action, option, (TagItem)folder));
		} else if ( folder instanceof FolderItem ) { // FolderItem needs to go last
			return (zTreeItem(action, option, (FolderItem)folder));
		}

		throw new HarnessException("Must use TagItem FolderItem or SavedSearchFolderItem or ZimletItem as argument, but was "+ folder.getClass());
	}

	protected AbsPage zTreeItem(Action action, Button option, FolderMountpointItem folderItem) 
	throws HarnessException 
	{
		logger.info(myPageName() + " zTreeItem("+ action +", "+ option + "," + folderItem.getName() +")");
		tracer.trace(action +" then "+ option +" on Folder Item = "+ folderItem.getName());

		AbsPage page = null;
		String actionLocator = null;
		String optionLocator = null;

		if ((action == null) || (option == null) || (folderItem == null)) {
			throw new HarnessException("Must define an action, option, and addressbook");
		}
		
		

	
		if (folderItem.getName().equals("USER_ROOT")) {
			actionLocator = "css=div#ztih__main_Contacts__ADDRBOOK_div";
		} else {
			actionLocator = "css=div#zti__main_Contacts__" + folderItem.getId() +"_div";			
		}
		
		
		if ( action == Action.A_RIGHTCLICK ) {
			
			
			optionLocator = "css=div[id='ZmActionMenu_contacts_ADDRBOOK']";
			
			if (option == Button.B_TREE_NEWFOLDER) {
				
				optionLocator += " div[id='NEW_ADDRBOOK'] td[id$='_title']";
				page = null;			    

			}			
			else if (option == Button.B_DELETE) {
				
				optionLocator += " div[id='DELETE_WITHOUT_SHORTCUT'] td[id$='_title']";
				page = null;
				
		    } 
			else if (option == Button.B_RENAME) {
				
				optionLocator += " div[id='RENAME_FOLDER'] td[id$='_title']";
				page = null;

			}   			
			else if (option == Button.B_TREE_EDIT) {

				optionLocator += " div[id='EDIT_PROPS'] td[id$='_title']";
				page = null;

			} 
			else if (option == Button.B_TREE_FOLDER_EMPTY) {
				
				optionLocator += " div[id='EMPTY_FOLDER'] td[id$='_title']";
				page = null;

			}
			else {
				throw new HarnessException("implement action:"+ action +" option:"+ option);
			}
			
			// Default right-click behavior
			sRightClickAt(actionLocator,"0,0");
			zWaitForBusyOverlay();

			sClickAt(optionLocator, "0,0");
			zWaitForBusyOverlay();

			return (page);
			
		} else {
			throw new HarnessException("implement action:"+ action +" option:"+ option);
		}


	}
	
	protected AbsPage zTreeItem(Action action, Button option, FolderItem folderItem)
	throws HarnessException {

		AbsPage page = null;
		String actionLocator = null;
		String optionLocator = null;

		if ((action == null) || (option == null) || (folderItem == null)) {
			throw new HarnessException("Must define an action, option, and addressbook");
		}
		
		
		logger.info(myPageName() + " zTreeItem("+ action +", "+ option + "," + folderItem.getName() +")");
		tracer.trace(action +" then "+ option +" on Folder Item = "+ folderItem.getName());

	
		if (folderItem.getName().equals("USER_ROOT")) {
			actionLocator = "css=div#ztih__main_Contacts__ADDRBOOK_div";
		} else {
			actionLocator = "css=div#zti__main_Contacts__" + folderItem.getId() +"_div";			
		}
		
		
		if ( action == Action.A_RIGHTCLICK ) {
			
				
			if (option == Button.B_TREE_NEWFOLDER) {
				
				optionLocator = "css=div[id^='ZmActionMenu_contacts_ADDRBOOK'] div[id^='NEW_ADDRBOOK'] td[id$='_title']";
				page = null;
				
			}			
			else if (option == Button.B_DELETE) {												
				
				optionLocator = "css=div[id^='ZmActionMenu_contacts_ADDRBOOK'] div[id^='DELETE_WITHOUT_SHORTCUT'] td[id$='_title']";
				page = null;
				
		    } 
			else if (option == Button.B_RENAME) {

				optionLocator="css=div[id^='ZmActionMenu_contacts_ADDRBOOK'] div[id^='RENAME_FOLDER'] td[id$='_title']";				
				page = null;

			}   			
			else if (option == Button.B_TREE_EDIT) {
				
				optionLocator="css=div[id^='ZmActionMenu_contacts_ADDRBOOK'] div[id^='EDIT_PROPS'] td[id$='_title']";				
				page = null;
			    
			} 
			else if (option == Button.B_TREE_FOLDER_EMPTY) {
				
				optionLocator = "css=div[id^='ZmActionMenu_contacts_ADDRBOOK'] div[id^='EMPTY_FOLDER'] td[id$='_title']";
				page = null;

			}
			else if (option == Button.B_SHARE) {
				
				optionLocator = "css=div[id='ZmActionMenu_contacts_ADDRBOOK'] div[id='SHARE_ADDRBOOK'] td[id$='_title']";
				page = null;
				
			}
			else {
				throw new HarnessException("implement action:"+ action +" option:"+ option);
			}
			
			if ( actionLocator != null ) {

				sRightClickAt(actionLocator,"0,0");
				zWaitForBusyOverlay();

			}
			
			if ( optionLocator != null ) {
				
				sClickAt(optionLocator,"0,0");				
				zWaitForBusyOverlay();						

			}

			return page;

		} else if (action == Action.A_LEFTCLICK) {
			
			if (option == Button.B_TREE_NEWFOLDER) {
				
				sClickAt("css=div[class^=ImgNewContactsFolder][class*=ZWidget]","0,0");
				page = null;
			      				
			} else {
				throw new HarnessException("implement action:"+ action +" option:"+ option);
			}
			
		} else {
			throw new HarnessException("implement action:"+ action +" option:"+ option);
		}

		return page;
	}
	
	
	protected AbsPage zTreeItem(Action action, Button option, TagItem t)
	throws HarnessException {

		if ((action == null) || (option == null) || (t == null)) {
			throw new HarnessException(
			"Must define an action, option, and addressbook");
		}
		AbsPage page = null;
		String actionLocator = null;
		String optionLocator = "css=div[id^='ZmActionMenu_contacts_TAG'] ";

		tracer.trace("processing " + t.getName());

		if (action == Action.A_LEFTCLICK) {

			throw new HarnessException("implement me!");

		} else if (action == Action.A_RIGHTCLICK) {

			actionLocator = "css=td[id^='zti__main_Contacts__']:contains('"+ t.getName() +"')";
			this.sRightClickAt(actionLocator,"");
			
			this.zWaitForBusyOverlay();



		} else {
			throw new HarnessException("Action " + action
					+ " not yet implemented");
		}
		

		if (option == Button.B_TREE_NEWTAG) {

			// optionLocator = "//td[contains(@id,'_left_icon')]/div[contains(@class,'ImgNewTag')]";
			// optionLocator="//div[contains(@id,'POPUP_DWT') and contains(@class,'ZHasSubMenu')]//tbody/tr[@id='POPUP_NEW_TAG']";
			// optionLocator = css=div[id='ZmActionMenu_conversationList_TAG'] div[id='NEW_TAG'] td[id$='_title']
			optionLocator += " div[id^='NEW_TAG'] td[id$='_title']";

			page = null;
						
		} else if (option == Button.B_DELETE) {

			// optionLocator = Locators.zDeleteTreeMenuItem;
			optionLocator += " div[id^='DELETE_WITHOUT_SHORTCUT'] td[id$='_title']";

			page = null;

		} else if (option == Button.B_RENAME) {

			// optionLocator = Locators.zRenameTreeMenuItem;
			optionLocator += " div[id^='RENAME_TAG'] td[id$='_title']";

			page = null;

		} else {
			throw new HarnessException("button " + option
					+ " not yet implemented");
		}
		
		// Default behavior. Click the locator
		sClickAt(optionLocator, "0,0");

		// If there is a busy overlay, wait for that to finish
		this.zWaitForBusyOverlay();

		return (page);

	}
	
	public AbsPage zTreeItem(Action action, String folderName) throws HarnessException {
		
		AbsPage page = null;
		String locator;

		if ( folderName == null )
			throw new HarnessException("locator is null for action "+ action);

		if ( action == Action.A_LEFTCLICK ) {
			locator = "css=div[class='zcs-menu-label']:contains('"+ folderName +"')";
			
		} else {
			throw new HarnessException("Action "+ action +" not yet implemented");
		}

		sClickAt(locator,"0,0");
		
		SleepUtil.sleepMedium();

		return (page);
	}
	

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		tracer.trace("Click button "+ button);

		if ( button == null )
			throw new HarnessException("Button cannot be null");
			
		AbsPage page = null;
		String locator = null;
		String subLocator = null;
		if ( button == Button.B_TREE_NEWADDRESSBOOK ) {
			
			locator = "css=div[id=main_Contacts-parent-ADDRBOOK] div[class*=ImgContextMenu]";
		    subLocator ="css=div#ZmActionMenu_contacts_ADDRBOOK td#NEW_ADDRBOOK_title";
		    page = null;			    
			
		
		} else if ( button == Button.B_TREE_NEWTAG ) { 			
			//locator = zNewTagIcon;
			locator = "css=div[id=main_Contacts-parent-TAG] div[class*=ImgContextMenu]"; //td#ztih__main_Contacts__TAG_optCell";
            subLocator ="css=div#ZmActionMenu_contacts_TAG td#NEW_TAG_title";
            page = null;
				
		} else {
			throw new HarnessException("no logic defined for button "+ button);
		}

	
		
		// Default behavior, process the locator by clicking on it
		//
		
		// Make sure the button exists
		if ( !this.sIsElementPresent(locator) )
			throw new HarnessException("Button is not present locator="+ locator +" button="+ button);
		
		// Click it
		this.sClickAt(locator,"");
		
		// If the app is busy, wait for that to finish
		this.zWaitForBusyOverlay();
		
		// Make sure the subLocator exists
		if ( !this.sIsElementPresent(subLocator) )
			throw new HarnessException("Button is not present locator="+ subLocator );
		
		this.sClick(subLocator);
		
		// If the app is busy, wait for that to finish
		this.zWaitForBusyOverlay();
		
		return (page);

	}

	//expand the folder to show folder's children
	public void zExpand(FolderItem folderItem) throws HarnessException{
		
	    String locator="css=td#zti__main_Contacts__" + folderItem.getId() +"_nodeCell" + ">div." ;
		//already expanded or not have sub folders
	    if (!sIsElementPresent(locator+ Locators.COLLAPSE_NODE)) {
		  return;
	    }
	    SleepUtil.sleepMedium();
	    if (this.sIsElementPresent(locator+ Locators.COLLAPSE_NODE)) {
		   sMouseDown(locator+ Locators.COLLAPSE_NODE);
		}
	    zWaitForElementPresent(locator+ Locators.EXPAND_NODE);
	}

	/* (non-Javadoc)
	 * @see framework.ui.AbsTree#myPageName()
	 */
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	// (done for TP)
	@Override
	public boolean zIsActive() throws HarnessException {
		
		boolean present = sIsElementPresent("css=div[id^='ext-moveview']");
		if ( !present ) {
			logger.debug("Settings button present = "+ present);
			return (false);
		}

		logger.debug("isActive() = "+ true);
		return (true);

	}


	

}
