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
package com.zimbra.qa.selenium.projects.ajax.ui.search;

import java.util.*;

import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.ui.*;


/**
 * @author Matt Rhoades
 *
 */
public class PageSearch extends AbsTab {

	public static class Locators {
		
		public static final String zActiveLocator = "css=div#ztb_search";
		
		public static final String zSearchInput = "css=input#zi_search_inputfield";
		public static final String zSearchButton = "css=td#zb__Search__SAVE_left_icon";
		
	}

	private boolean zIsIncludeSharedItems=false;
	private static HashMap<Button,String> imagesMap             = new HashMap<Button,String>();
	private static HashMap<Button,String> imagesIncludeShareMap = new HashMap<Button,String>();
	{	
		imagesMap.put(Button.O_SEARCHTYPE_ALL,"ImgGlobe");
		imagesIncludeShareMap.put(Button.O_SEARCHTYPE_ALL,"ImgGlobe");
		
		imagesMap.put(Button.O_SEARCHTYPE_EMAIL,"ImgMessage");
		imagesIncludeShareMap.put(Button.O_SEARCHTYPE_EMAIL,"ImgSharedMailFolder");
		
		imagesMap.put(Button.O_SEARCHTYPE_CONTACTS,"ImgContact");
		imagesIncludeShareMap.put(Button.O_SEARCHTYPE_CONTACTS,"ImgSharedContactsFolder");

		imagesMap.put(Button.O_SEARCHTYPE_GAL,"ImgGAL");
		imagesIncludeShareMap.put(Button.O_SEARCHTYPE_GAL,"ImgGAL");
		
		imagesMap.put(Button.O_SEARCHTYPE_APPOINTMENTS,"ImgAppointment");
		imagesIncludeShareMap.put(Button.O_SEARCHTYPE_APPOINTMENTS,"ImgAppointment");
		
		imagesMap.put(Button.O_SEARCHTYPE_TASKS,"ImgTasksApp");
		imagesIncludeShareMap.put(Button.O_SEARCHTYPE_TASKS,"ImgSharedTaskList");
		
		imagesMap.put(Button.O_SEARCHTYPE_FILES,"ImgDoc");
		imagesIncludeShareMap.put(Button.O_SEARCHTYPE_FILES,"ImgDoc");		
	}
	
	public PageSearch(AbsApplication application) {
		super(application);
		
		logger.info("new " + PageSearch.class.getCanonicalName());

	}

	/* (non-Javadoc)
	 * @see projects.admin.ui.AbsPage#isActive()
	 */
	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the Mobile Client is loaded in the browser
		if ( !MyApplication.zIsLoaded() )
			throw new HarnessException("Application is not active!");
		

		// Look for the search toolbar button
		boolean present = sIsElementPresent(Locators.zActiveLocator);
		if ( !present ) {
			logger.debug("isActive() present = "+ present);
			return (false);
		}
		
		logger.debug("isActive() = "+ true);
		return (true);

	}
	
	public void zClose() throws HarnessException {
		
		if ( !zIsActive() ) {
			return; // Already closed
		}
		
		String locator = "css=div[id^='zb__App__tab_SR'] td[id$='_right_icon'] div.ImgCloseGray";
		
		if ( !this.sIsElementPresent(locator) ) {
			return; // Already closed
		}
		
		this.zClickAt(locator, "");
		this.zWaitForBusyOverlay();
		
		return;
	}

	/* (non-Javadoc)
	 * @see projects.admin.ui.AbsPage#myPageName()
	 */
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	/* (non-Javadoc)
	 * @see projects.admin.ui.AbsPage#navigateTo()
	 */
	@Override
	public void zNavigateTo() throws HarnessException {


		if ( zIsActive() ) {
			// This page is already active
			return;
		}
		

		// If search is not active, then we must not be logged in
		if ( !((AppAjaxClient)MyApplication).zPageMain.zIsActive() ) {
			((AppAjaxClient)MyApplication).zPageMain.zNavigateTo();
		}

		// Nothing more to do to make search appear, since it is always active if the app is active
		tracer.trace("Navigate to "+ this.myPageName());

		zWaitForActive();
		
	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton("+ button +")");
		
		tracer.trace("Click button "+ button);

		if ( button == null )
			throw new HarnessException("Button cannot be null!");
		
				
		// Default behavior variables
		//
		String locator = null;	// If set, this will be clicked
		AbsPage page = null;	// If set, this page will be returned
		
		// Based on the button specified, take the appropriate action(s)
		//
		
		if ( button == Button.B_SEARCH ) {
			
			locator = "css=div#zb__Search__SEARCH_left_icon div.ImgSearch2";
			page = null;
			
		} else if ( (button == Button.B_SEARCHSAVE) || (button == Button.B_SAVE) ) {
			
			locator = "css=div[id^='ztb_searchresults__'] td[id$='_saveButton'] td[id$='_title']";
			page = new DialogSaveSearch(MyApplication, this);
			
		} else if ( (button == Button.B_DELETE) ) {
			
			if (zGetPropMailView() == SearchView.BY_MESSAGE) {
				locator = "css=div[id^='ztb__TV-SR-'] div[id$='__DELETE'] td[id$='_title']";
			} else {
				locator = "css=div[id^='ztb__CLV-SR-'] div[id$='__DELETE'] td[id$='_title']";
			}
			page = null;
			
		} else {
			throw new HarnessException("no logic defined for button "+ button);
		}

		if ( locator == null ) {
			throw new HarnessException("locator was null for button "+ button);
		}
		
		// Make sure the button exists
		if ( !sIsElementPresent(locator) )
			throw new HarnessException("Button is not present locator="+ locator +" button="+ button);
		
		// Default behavior, process the locator by clicking on it
		//
	
		// Click it
		zClickAt(locator, "");
		SleepUtil.sleepSmall();
		
		// If the app is busy, wait for it to become active
		zWaitForBusyOverlay();
		
		
		// If page was specified, make sure it is active
		if ( page != null ) {
            
			// This function (default) throws an exception if never active
			page.zWaitForActive();
			
		}
		
		return (page);
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown("+ pulldown +", "+ option +")");
		
		tracer.trace("Click pulldown "+ pulldown +" then "+ option);

		if ( pulldown == null )
			throw new HarnessException("Pulldown cannot be null!");
		
		if ( option == null )
			throw new HarnessException("Option cannot be null!");

		// Default behavior variables
		//
		String pulldownLocator = null;	// If set, this will be expanded
		String optionLocator = null;	// If set, this will be clicked
		AbsPage page = null;	// If set, this page will be returned
		
		// Based on the button specified, take the appropriate action(s)
		//
		
		if ( pulldown == Button.B_SEARCHTYPE ) {
			pulldownLocator = "css=td#ztb_search_searchMenuButton";
			
			if ( option == Button.O_SEARCHTYPE_ALL ) {        
				optionLocator = "css=div#zmi__Search__ANY";						
			} else if ( option == Button.O_SEARCHTYPE_EMAIL ) {
				optionLocator = "css=div#zmi__Search__MAIL";		
			} else if ( option == Button.O_SEARCHTYPE_CONTACTS ) {
				optionLocator = "css=div#zmi__Search__CONTACT";		
			} else if ( option == Button.O_SEARCHTYPE_GAL ) {
				optionLocator = "css=div#zmi__Search__GAL";		
			} else if ( option == Button.O_SEARCHTYPE_APPOINTMENTS ) {
				optionLocator = "css=div#zmi__Search__APPT";		
			} else if ( option == Button.O_SEARCHTYPE_TASKS ) {
				optionLocator = "css=div#zmi__Search__TASK";		
			} else if ( option == Button.O_SEARCHTYPE_FILES ) {
				optionLocator = "css=div#zmi__Search__BRIEFCASE_ITEM";		
			} else if ( option == Button.O_SEARCHTYPE_INCLUDESHARED ) {
				optionLocator = "css=div#zmi__Search__SHARED";		
				
			} else {
				throw new HarnessException("no logic defined for pulldown/option "+ pulldown +"/"+ option);
			}
			
		} else {
			throw new HarnessException("no logic defined for pulldown "+ pulldown);
		}

		// Default behavior
		if ( pulldownLocator != null ) {
						
			// Make sure the locator exists
			if ( !this.sIsElementPresent(pulldownLocator) ) {
				throw new HarnessException("Button "+ pulldown +" option "+ option +" pulldownLocator "+ pulldownLocator +" not present!");
			}
			
			this.zClick(pulldownLocator);

			// If the app is busy, wait for it to become active
			this.zWaitForBusyOverlay();
			
			
			if ( optionLocator != null ) {

				// Make sure the locator exists
				if ( !this.sIsElementPresent(optionLocator) ) {
					throw new HarnessException("Button "+ pulldown +" option "+ option +" optionLocator "+ optionLocator +" not present!");
				}
				
				this.zClick(optionLocator);

				// If the app is busy, wait for it to become active
				this.zWaitForBusyOverlay();
				
				if ( option == Button.O_SEARCHTYPE_INCLUDESHARED ) {
				   zIsIncludeSharedItems = !zIsIncludeSharedItems; 
				}
				
			}
			
			// If we click on pulldown/option and the page is specified, then
			// wait for the page to go active
			if ( page != null ) {
				page.zWaitForActive();
			}
			
			if (!zIsSearchType(option)) {
				throw new HarnessException("Not able to change search type "+ option ); 
			}
			
		}
		
		// Return the specified page, or null if not set
		return (page);
	}

	/**
	 * Activate a pulldown with dynamic values, such as "Move to folder" and "Add a tag".
	 * 
	 * @param pulldown the toolbar button to press
	 * @param dynamic the toolbar item to click such as FolderItem or TagItem
	 * @throws HarnessException 
	 */
	public AbsPage zToolbarPressPulldown(Button pulldown, Object dynamic) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown("+ pulldown +", "+ dynamic +")");

		tracer.trace("Click pulldown "+ pulldown +" then "+ dynamic);


		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");

		if (dynamic == null)
			throw new HarnessException("Option cannot be null!");


		// Default behavior variables
		String pulldownLocator = null; // If set, this will be expanded
		String optionLocator = null; // If set, this will be clicked
		AbsPage page = null; // If set, this page will be returned


		if ( pulldown == Button.B_MOVE ) {

			if ( !(dynamic instanceof FolderItem) ) 
				throw new HarnessException("if pulldown = " + Button.B_MOVE +", then dynamic must be FolderItem");

			FolderItem folder = (FolderItem)dynamic;
			
			String pulldownLocator1=null, pulldownLocator2=null, optionLocator1=null, optionLocator2=null;
						
			if (this.sIsElementPresent("css=div[id^='ztb__TV-SR-3']")) {
				pulldownLocator1 = "css=div[id^='ztb__TV-SR-3'] div[id$='__MOVE_MENU'] td[id$='_dropdown']>div";
				optionLocator1 = "css=td[id^='zti__ZmFolderChooser_MailTV-SR-3'][id$='" + folder.getId() +"_textCell']";
			} else if (this.sIsElementPresent("css=div[id^='ztb__TV-SR-2']")) {
				pulldownLocator1 = "css=div[id^='ztb__TV-SR-2'] div[id$='__MOVE_MENU'] td[id$='_dropdown']>div";
				optionLocator1 = "css=td[id^='zti__ZmFolderChooser_MailTV-SR-2'][id$='" + folder.getId() +"_textCell']";
			} else if (this.sIsElementPresent("css=div[id^='ztb__TV-SR-1']")) {
				pulldownLocator1 = "css=div[id^='ztb__TV-SR-1'] div[id$='__MOVE_MENU'] td[id$='_dropdown']>div";
				optionLocator1 = "css=td[id^='zti__ZmFolderChooser_MailTV-SR-1'][id$='" + folder.getId() +"_textCell']";
			}
			
			if (this.sIsElementPresent("css=div[id^='ztb__CLV-SR-3']")) {
				pulldownLocator2 = "css=div[id^='ztb__CLV-SR-3'] div[id$='__MOVE_MENU'] td[id$='_dropdown']>div";
				optionLocator2 = "css=td[id^='zti__ZmFolderChooser_MailCLV-SR-3'][id$='" + folder.getId() +"_textCell']";
			} else if (this.sIsElementPresent("css=div[id^='ztb__CLV-SR-2']")) {
				pulldownLocator2 = "css=div[id^='ztb__CLV-SR-2'] div[id$='__MOVE_MENU'] td[id$='_dropdown']>div";
				optionLocator2 = "css=td[id^='zti__ZmFolderChooser_MailCLV-SR-2'][id$='" + folder.getId() +"_textCell']";
			} else if (this.sIsElementPresent("css=div[id^='ztb__CLV-SR-1']")) {
				pulldownLocator2 = "css=div[id^='ztb__CLV-SR-1'] div[id$='__MOVE_MENU'] td[id$='_dropdown']>div";
				optionLocator2 = "css=td[id^='zti__ZmFolderChooser_MailCLV-SR-1'][id$='" + folder.getId() +"_textCell']";
			}
			
			// Check if we are CLV or MV
			if (zGetPropMailView() == SearchView.BY_MESSAGE) {
				pulldownLocator = pulldownLocator1;
				optionLocator = optionLocator1;
			} else {
				pulldownLocator = pulldownLocator2;
				optionLocator = optionLocator2;
			}


			page = null;


		} else if ( pulldown == Button.B_TAG ) {
			
			if ( !(dynamic instanceof TagItem) ) 
				throw new HarnessException("if pulldown = " + Button.B_TAG +", then dynamic must be TagItem");

			TagItem tag = (TagItem)dynamic;

			pulldownLocator = "css=td[id$='__TAG_MENU_dropdown']>div[class='ImgSelectPullDownArrow']";
			optionLocator = "css=div[id='zb__TV-main__TAG_MENU|MENU'] td[id$='_title']:contains("+ tag.getName() +")";
			page = null;

		} else {

			throw new HarnessException("no logic defined for pulldown/dynamic " + pulldown + "/" + dynamic);

		}

		// Default behavior
		if (pulldownLocator != null) {

			// Make sure the locator exists
			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " pulldownLocator " + pulldownLocator + " not present!");
			}

			this.zClickAt(pulldownLocator,"");

			// If the app is busy, wait for it to become active
			zWaitForBusyOverlay();

			SleepUtil.sleepSmall();

			if (optionLocator != null) {

				// Make sure the locator exists
				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException(" dynamic " + dynamic + " optionLocator " + optionLocator + " not present!");
				}

				this.zClickAt(optionLocator,"");

				// If the app is busy, wait for it to become active
				zWaitForBusyOverlay();
			}

			// If we click on pulldown/option and the page is specified, then
			// wait for the page to go active
			if (page != null) {
				page.zWaitForActive();
			}

		}



		// Return the specified page, or null if not set
		return (page);



	}


	protected AbsPage zListItemMessages(Action action, String subject) throws HarnessException {
		
		// Copied from PageMail.  It would probably be better to re-use somehow.
		
		
		logger.info(myPageName() + " zListItem("+ action +", "+ subject +")");

		tracer.trace(action +" on subject = "+ subject);

		if ( action == null )
			throw new HarnessException("action cannot be null");

		if ( subject == null )
			throw new HarnessException("subject cannot be null");

		AbsPage page = null;
		String listLocator;
		String rowLocator;
		String itemlocator = null;

		String listLocator1=null, rowLocator1=null, listLocator2=null, rowLocator2=null;
		
		if (this.sIsElementPresent("css=div[id^='ztb__TV-SR-3']")) {
			listLocator1 = "css=ul[id^='zl__TV-SR-3']";
			rowLocator1 = "li[id^='zli__TV-SR-3']";
		} else if (this.sIsElementPresent("css=div[id^='ztb__TV-SR-2']")) {
			listLocator1 = "css=ul[id^='zl__TV-SR-2']";
			rowLocator1 = "li[id^='zli__TV-SR-2']";
		} else if (this.sIsElementPresent("css=div[id^='ztb__TV-SR-1']")) {
			listLocator1 = "css=ul[id^='zl__TV-SR-1']";
			rowLocator1 = "li[id^='zli__TV-SR-1']";
		}
		
		if (this.sIsElementPresent("css=ul[id^='zl__CLV-SR-3']")) {
			listLocator2 = "css=ul[id^='zl__CLV-SR-3']";
			rowLocator2 = "li[id^='zli__CLV-SR-3']";
		} else if (this.sIsElementPresent("css=ul[id^='zl__CLV-SR-2']")) {
			listLocator2 = "css=ul[id^='zl__CLV-SR-2']";
			rowLocator2 = "li[id^='zli__CLV-SR-2']";
		} else if (this.sIsElementPresent("css=ul[id^='zl__CLV-SR-1']")) {
			listLocator2 = "css=ul[id^='zl__CLV-SR-1']";
			rowLocator2 = "li[id^='zli__CLV-SR-1']";
		}

		if (zGetPropMailView() == SearchView.BY_MESSAGE) {
			listLocator = listLocator1;
			rowLocator = rowLocator1;
		} else {
			listLocator = listLocator2;
			rowLocator = rowLocator2;
		}

		// TODO: how to handle both messages and conversations, maybe check the view first?
		if ( !this.sIsElementPresent(listLocator) )
			throw new HarnessException("List View Rows is not present "+ listLocator);

		// How many items are in the table?
		int count = this.sGetCssCount(listLocator + " " + rowLocator);
		logger.debug(myPageName() + " zListSelectItem: number of list items: "+ count);


		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {

			itemlocator = listLocator + " li:nth-of-type("+ i +") ";
			String s = this.sGetText(itemlocator + " [id$='__su']").trim();

			if ( s.contains(subject) ) {
				break; // found it
			}

			itemlocator = null;
		}

		if ( itemlocator == null ) {
			throw new HarnessException("Unable to locate item with subject("+ subject +")");
		}

		if ( action == Action.A_LEFTCLICK ) {

			// Left-Click on the item
			this.zClickAt(itemlocator,"");

			this.zWaitForBusyOverlay();

			// Return the displayed mail page object
			if ( zGetPropMailView() == SearchView.BY_MESSAGE ) {
				page = new DisplayMail(MyApplication);
			} else {
				page = new DisplayConversation(MyApplication);
			}
			
			// FALL THROUGH

		} else if ( action == Action.A_DOUBLECLICK ) {

			// Double-Click on the item
			this.sDoubleClick(itemlocator);

			this.zWaitForBusyOverlay();

//			page = new DisplayMail(MyApplication);
			page = null;

			// FALL THROUGH
		} else if ( action == Action.A_CTRLSELECT ) {

			throw new HarnessException("implement me!  action = "+ action);

		} else if ( action == Action.A_SHIFTSELECT ) {

			throw new HarnessException("implement me!  action = "+ action);

		} else if ( action == Action.A_RIGHTCLICK ) {

			// Right-Click on the item
			this.zRightClick(itemlocator);

			// Return the displayed mail page object
			page = new ContextMenu(MyApplication);

			// FALL THROUGH

		} else if ( action == Action.A_MAIL_CHECKBOX ) {

			String selectlocator = itemlocator + " div[id$='__se']";
			if ( !this.sIsElementPresent(selectlocator) )
				throw new HarnessException("Checkbox locator is not present "+ selectlocator);

			String image = this.sGetAttribute(selectlocator +"@class");
			if ( image.equals("ImgCheckboxChecked") )
				throw new HarnessException("Trying to check box, but it was already enabled");

			// Left-Click on the flag field
			this.zClick(selectlocator);

			this.zWaitForBusyOverlay();

			// No page to return
			page = null;

			// FALL THROUGH

		} else if ( action == Action.A_MAIL_UNCHECKBOX ) {

			String selectlocator = itemlocator + " div[id$='__se']";
			if ( !this.sIsElementPresent(selectlocator) )
				throw new HarnessException("Checkbox locator is not present "+ selectlocator);

			String image = this.sGetAttribute(selectlocator +"@class");
			if ( image.equals("ImgCheckboxUnchecked") )
				throw new HarnessException("Trying to uncheck box, but it was already disabled");

			// Left-Click on the flag field
			this.zClick(selectlocator);

			this.zWaitForBusyOverlay();

			// No page to return
			page = null;

			// FALL THROUGH

		} else if ( action == Action.A_MAIL_EXPANDCONVERSATION ) {

			String selectlocator = itemlocator + " div[id$='__ex']";
			if ( !this.sIsElementPresent(selectlocator) )
				throw new HarnessException("Checkbox locator is not present "+ selectlocator);

			String image = this.sGetAttribute(selectlocator +"@class");
			if ( image.equals("ImgNodeExpanded") )
				throw new HarnessException("Trying to expand, but conversation was alread expanded");

			// Left-Click on the flag field
			this.zClick(selectlocator);

			this.zWaitForBusyOverlay();

			// No page to return
			page = null;

		} else if ( action == Action.A_MAIL_COLLAPSECONVERSATION ) {

			String selectlocator = itemlocator + " div[$id$='__ex']";
			if ( !this.sIsElementPresent(selectlocator) )
				throw new HarnessException("Checkbox locator is not present "+ selectlocator);

			String image = this.sGetAttribute(selectlocator +"@class");
			if ( image.equals("ImgNodeCollapsed") )
				throw new HarnessException("Trying to collapse, but conversation was alread collapsed");

			// Left-Click on the flag field
			this.zClick(selectlocator);

			this.zWaitForBusyOverlay();

			// No page to return
			page = null;

		} else if ( (action == Action.A_MAIL_FLAG) || (action == Action.A_MAIL_UNFLAG) ) {
			// Both FLAG and UNFLAG have the same action and result

			String flaglocator = itemlocator + " div[id$='__fg']";

			// Left-Click on the flag field
			this.zClick(flaglocator);

			this.zWaitForBusyOverlay();

			// No page to return
			page = null;

			// FALL THROUGH

		} else {
			throw new HarnessException("implement me!  action = "+ action);
		}


		if ( page != null ) {
			page.zWaitForActive();
		}

		// default return command
		return (page);


	}
	
	@Override
	public AbsPage zListItem(Action action, String item) throws HarnessException {
		
		// TODO: need to determine if the search results
		// TODO: are displaying messages, contacts, appointments, etc.
		// TODO: for now, assume messages
		// TODO:
		return (zListItemMessages(action, item));

	}

	@Override
	public AbsPage zListItem(Action action, Button option, String item) throws HarnessException {
		throw new HarnessException(myPageName() + " does not have a list view");
	}

	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption ,String item)  throws HarnessException {
	   throw new HarnessException(myPageName() + " does not have a list view");
    }
		
	/**
	 * Enter text into the query string field
	 * @param query
	 * @throws HarnessException 
	 */
	public void zAddSearchQuery(String query) throws HarnessException {
		logger.info(myPageName() + " zAddSearchQuery("+ query +")");
		
		tracer.trace("Search for the query "+ query);

		
		this.zTypeKeys(Locators.zSearchInput, query);

	}
	

    public boolean zIsSearchType(Button button) throws HarnessException
    {
    	String imageClass=null;
        if (zIsIncludeSharedItems) {
        	imageClass = imagesIncludeShareMap.get(button);
        }
        else {
        	imageClass = imagesMap.get(button);
        }
            
    	return sIsElementPresent("css=td#zb__Search__MENU_left_icon>div." + imageClass);
    }

	public List<MailItem> zListGetMessages() throws HarnessException {

		List<MailItem> items = new ArrayList<MailItem>();
		
		String listLocator=null,listLocator1=null, listLocator2=null,rowLocator=null, rowLocator1=null, rowLocator2=null;
		if (this.sIsElementPresent("css=div[id^='ztb__TV-SR-3']")) {
			listLocator1 = "css=ul[id^='zl__TV-SR-3']";
			rowLocator1 = "li[id^='zli__TV-SR-3']";
		} else if (this.sIsElementPresent("css=div[id^='ztb__TV-SR-2']")) {
			listLocator1 = "css=ul[id^='zl__TV-SR-2']";
			rowLocator1 = "li[id^='zli__TV-SR-2']";
		} else if (this.sIsElementPresent("css=div[id^='ztb__TV-SR-1']")) {
			listLocator1 = "css=ul[id^='zl__TV-SR-1']";
			rowLocator1 = "li[id^='zli__TV-SR-1']";
		}
		
		if (this.sIsElementPresent("css=ul[id^='zl__CLV-SR-3']")) {
			listLocator2 = "css=ul[id^='zl__CLV-SR-3']";
			rowLocator2 = "li[id^='zli__CLV-SR-3']";
		} else if (this.sIsElementPresent("css=ul[id^='zl__CLV-SR-2']")) {
			listLocator2 = "css=ul[id^='zl__CLV-SR-2']";
			rowLocator2 = "li[id^='zli__CLV-SR-2']";
		} else if (this.sIsElementPresent("css=ul[id^='zl__CLV-SR-1']")) {
			listLocator2 = "css=ul[id^='zl__CLV-SR-1']";
			rowLocator2 = "li[id^='zli__CLV-SR-1']";
		}

		if (zGetPropMailView() == SearchView.BY_MESSAGE) {
			listLocator = listLocator1;
			rowLocator = rowLocator1;
		} else {
			listLocator = listLocator2;
			rowLocator = rowLocator2;
		}
		
		// Make sure the button exists
		if ( !this.sIsElementPresent(listLocator) )
			throw new HarnessException("Message List View Rows is not present: " + listLocator);

		String tableLocator = listLocator + " " + rowLocator;
		
		// How many items are in the table?
		int count = this.sGetCssCount(tableLocator);
		logger.debug(myPageName() + " zListGetMessages: number of messages: "+ count);

		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {

			// Add the new item to the list
			MailItem item = ((AppAjaxClient)this.MyApplication).zPageMail.parseMessageRow(listLocator + " li:nth-of-type("+ i +") ");
			items.add(item);
			logger.info(item.prettyPrint());
		}

		// Return the list of items
		return (items);
	}
	
	public enum SearchView {
		BY_MESSAGE, BY_CONVERSATION
	}


	public SearchView zGetPropMailView() throws HarnessException {
		String locator1=null, locator2=null;
		
		if (this.sIsElementPresent("css=ul[id^='zl__CLV-SR-3']")) {
			locator1 = "css=ul[id^='zl__CLV-SR-3']";
		} else if (this.sIsElementPresent("css=ul[id^='zl__CLV-SR-2']")) {
			locator1 = "css=ul[id^='zl__CLV-SR-2']";
		} else if (this.sIsElementPresent("css=ul[id^='zl__CLV-SR-1']")) {
			locator1 = "css=ul[id^='zl__CLV-SR-1']";
		}
		
		if (this.sIsElementPresent("css=ul[id^='zl__TV-SR-3']")) {
			locator2 = "css=ul[id^='zl__TV-SR-3']";
		} else if (this.sIsElementPresent("css=ul[id^='zl__TV-SR-2']")) {
			locator2 = "css=ul[id^='zl__TV-SR-2']";
		} else if (this.sIsElementPresent("css=ul[id^='zl__TV-SR-1']")) {
			locator2 = "css=ul[id^='zl__TV-SR-1']";
		}			
		
		if ( locator1 != null && this.zIsVisiblePerPosition(locator1, 0, 0) ) {
			return (SearchView.BY_CONVERSATION);
		} else if ( locator2 != null && this.zIsVisiblePerPosition(locator2, 0, 0) ) {
			return (SearchView.BY_MESSAGE);
		}

		throw new HarnessException("Unable to determine the Page Mail View");
	}

	
	private ContactItem parseContactRow(String top) throws HarnessException {
		
		
		
		/*

		<li id="zli__CNS-SR-Contacts-1__257" class="Row SimpleContact SimpleContact RowEven Row-selected SimpleContact">
		  	<div id="zlif__CNS-SR-Contacts-1__257__rw">
		  		<div id="zlif__CNS-SR-Contacts-1__257__type" class="ZmContactIcon ImgContact"></div>
	  			<div id="zlif__CNS-SR-Contacts-1__257__fileas">last13660676917405, first13660676917404</div>
	  			<div class="ZmListFlagsWrapper">
	  				<div id="zlif__CNS-SR-Contacts-1__257__tg" class="Tag ImgBlank_16"></div>
  				</div>
			</div>
		</li>

	 */

		ContactItem item = new ContactItem();

		String locator;

		// Is it a contact icon?
		locator = top + " div[id$='__type'].ImgContact";
		// TODO
		
		// Get the fileAs
		locator = top + " div[id$='__fileas']";
		if ( this.sIsElementPresent(locator) ) {
			item.setAttribute("fileAs", this.sGetText(locator));
		}

		return (item);

	}
	
	public List<ContactItem> zListGetContacts() throws HarnessException {

		String listLocator=null, rowLocator=null;
		List<ContactItem> items = new ArrayList<ContactItem>();

		// Temporary work around if n number of lists opened
		
		if ( this.sIsElementPresent("css=div[id^='zv__CNS-SR-2']") ) {
			listLocator = "css=div[id^='zv__CNS-SR-2']";
			rowLocator = "li[id^='zli__CNS-SR-2']";
		} else if ( this.sIsElementPresent("css=div[id^='zv__CNS-SR-1']") ) {
			listLocator = "css=div[id^='zv__CNS-SR-1']";
			rowLocator = "li[id^='zli__CNS-SR-1']";
		}

		// Make sure the button exists
		if ( !this.sIsElementPresent(listLocator) )
			throw new HarnessException("Contacts Rows is not present: " + listLocator);

		String tableLocator = listLocator + " " + rowLocator;
		
		// How many items are in the table?
		int count = this.sGetCssCount(tableLocator);
		logger.debug(myPageName() + " zListGetContacts: number of contacts: "+ count);

		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {

			// Add the new item to the list
			ContactItem item = parseContactRow(listLocator + " div:nth-of-type("+ i +") ");
			items.add(item);
			logger.info(item.prettyPrint());
		}

		// Return the list of items
		return (items);
	}
	

}
