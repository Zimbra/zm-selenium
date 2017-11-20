/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.touch.pages.contacts;

import java.awt.event.KeyEvent;
import java.util.*;
import org.apache.log4j.LogManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.touch.pages.mail.FormMailNew;
import com.zimbra.qa.selenium.projects.touch.pages.TouchPages;
import com.zimbra.qa.selenium.projects.touch.pages.PageMain;

public class PageAddressbook extends AbsTab {

	public boolean activeFlag = false;

	public static class Locators {
		public static final String PlusIcon		= "css=span[class$='x-button-icon x-shown add']";
		//public static final String EditIcon		= "css=div[id='ext-appview-2'] div[id^='ext-lefttitlebar'] div[id^='ext-button'] span[class$='x-button-icon x-shown edit']";
		//public static final String EditIcon		= "css=div[id='ext-appview-2'] div[ class$='x-layout-box'] div[id^='ext-button'] span[class$='x-button-icon x-shown edit']";
		public static final String EditIcon = "css=div[id^='ext-appview'][class='x-container x-layout-card-item x-sized'] div[ class$='x-layout-box'] div[id^='ext-button'] span[class='x-button-icon x-shown edit']";


		public static final String MenuIcon     = "css=div[id='ext-appview-2'] div[id^='ext-lefttitlebar'] div[id^='ext-button'] span[class$='x-button-icon x-shown arrow_down']";
		public static final String MenuIconMount     = "css=div[id^='ext-appview'] div[id^='ext-lefttitlebar'] div[id^='ext-button'] span[class$='arrow_down']";

		public static final String SearchContact     = "css=div[id='ext-container-21'] input";
		public static final String zNavigationButton	= "css=span[class='x-button-icon x-shown organizer']";


	}

	public static class CONTEXT_MENU {
		public static final String LOCATOR		= "id='zm__Contacts'";

		//contact's context menu
		public static final ContextMenuItem CONTACT_SEARCH = new ContextMenuItem("POPUP_SEARCH_MENU","Find Emails...","div[class*='ImgSearch']"," div[class*='ImgCascade']");
		public static final ContextMenuItem CONTACT_NEW_EMAIL = new ContextMenuItem("POPUP_NEW_MESSAGE","New Email","div[class*='ImgNewMessage']",":contains('nm')");

		//TODO: contact group: "Edit Group" instead of "Edit Contact"
		public static final ContextMenuItem CONTACT_EDIT = new ContextMenuItem("POPUP_CONTACT","Edit Contact","div[class*='ImgEdit']","");
		public static final ContextMenuItem CONTACT_FORWARD = new ContextMenuItem("POPUP_SEND_CONTACTS_IN_EMAIL","Forward Contact","div[class*='ImgMsgStatusSent']","");

		//TODO: contact group: "Tag Group" instead of "Tag Contact"
		public static final ContextMenuItem CONTACT_TAG = new ContextMenuItem("POPUP_TAG_MENU","Tag Contact","div[class*='ImgTag']"," div[class='ImgCascade']");
		public static final ContextMenuItem CONTACT_DELETE = new ContextMenuItem("POPUP_DELETE","Delete","div[class*='ImgDelete']",":contains('Del')");
		public static final ContextMenuItem CONTACT_MOVE = new ContextMenuItem("POPUP_MOVE","Move","div[class*='ImgMoveToFolder']","");
		public static final ContextMenuItem CONTACT_PRINT = new ContextMenuItem("POPUP_PRINT_CONTACT","Print","div[class*='ImgPrint']",":contains('p')");

		public static final ContextMenuItem CONTACT_GROUP = new ContextMenuItem("POPUP_CONTACTGROUP_MENU","Contact Group","div[class*='ImgGroup']","");
		public static final ContextMenuItem CONTACT_QUICK_COMMAND =  new ContextMenuItem("POPUP_QUICK_COMMANDS","Quick Commands","div[class='ImgQuickCommand']","");

	}


	public static class CONTEXT_SUB_MENU {

		public static final ContextMenuItem CONTACT_SUB_NEW_TAG = new ContextMenuItem("div#contacts_newtag","New Tag","div[class='ImgNewTag']",":contains('nt')");
		public static final ContextMenuItem CONTACT_SUB_REMOVE_TAG = new ContextMenuItem("div[id*='contacts_removetag']","Remove Tag","div[class='ImgDeleteTag']","");
		//public static final ContextMenuItem CONTACT_SUB_REMOVE_TAG = new ContextMenuItem("td#zmi__Contacts__TAG_MENU|MENU|REMOVETAG_title","Remove Tag","div[class='ImgDeleteTag']","");


		public static final ContextMenuItem CONTACT_SUB_RECEIVED_FROM_CONTACT = new ContextMenuItem("tr[id^=SEARCH__DWT]","Received From Contact","div[class='ImgSearch']","");
	    public static final ContextMenuItem CONTACT_SUB_SENT_TO_CONTACT = new ContextMenuItem("tr[id^=SEARCH_TO__DWT]","Sent To Contact","div[class='ImgSearch']","");

	    public static final ContextMenuItem CONTACT_SUB_NEW_CONTACT_GROUP = new ContextMenuItem("div[id^='CONTACTGROUP_MENU__DWT'][id$='|GROUP_MENU|NEWGROUP']","New Contact Group","div[class='ImgNewGroup']","");

	}

	/**
	 * A mapping of letter characters (upper case) to addressbook buttons, e.g. 'A' -> Button.B_AB_A
	 */
	public static final HashMap<Character, Button> buttons = new HashMap<Character, Button>() {
		private static final long serialVersionUID = -8341258587369022596L;
	{
    	put(Character.valueOf('A'), Button.B_AB_A);
    	put(Character.valueOf('B'), Button.B_AB_B);
    	put(Character.valueOf('C'), Button.B_AB_C);
    	put(Character.valueOf('D'), Button.B_AB_D);
    	put(Character.valueOf('E'), Button.B_AB_E);
    	put(Character.valueOf('F'), Button.B_AB_F);
    	put(Character.valueOf('G'), Button.B_AB_G);
    	put(Character.valueOf('H'), Button.B_AB_H);
    	put(Character.valueOf('I'), Button.B_AB_I);
    	put(Character.valueOf('J'), Button.B_AB_J);
    	put(Character.valueOf('K'), Button.B_AB_K);
    	put(Character.valueOf('L'), Button.B_AB_L);
    	put(Character.valueOf('M'), Button.B_AB_M);
    	put(Character.valueOf('N'), Button.B_AB_N);
    	put(Character.valueOf('O'), Button.B_AB_O);
    	put(Character.valueOf('P'), Button.B_AB_P);
    	put(Character.valueOf('Q'), Button.B_AB_Q);
    	put(Character.valueOf('R'), Button.B_AB_R);
    	put(Character.valueOf('S'), Button.B_AB_S);
    	put(Character.valueOf('T'), Button.B_AB_T);
    	put(Character.valueOf('U'), Button.B_AB_U);
    	put(Character.valueOf('V'), Button.B_AB_V);
    	put(Character.valueOf('W'), Button.B_AB_W);
    	put(Character.valueOf('X'), Button.B_AB_X);
    	put(Character.valueOf('Y'), Button.B_AB_Y);
    	put(Character.valueOf('Z'), Button.B_AB_Z);

	}};


	public PageAddressbook(AbsApplication application) {
		super(application);
		logger.info("new " + PageAddressbook.class.getCanonicalName());

	}

	/* (non-Javadoc)
	 * @see projects.admin.pages.AbsPage#isActive()
	 */
	@Override
	public boolean zIsActive() throws HarnessException {

		// Make sure the main page is active
		if (!((TouchPages) MyApplication).zPageMain.zIsActive()) {
			((TouchPages) MyApplication).zPageMain.zNavigateTo();
			zNavigateTo();
		}

		if (activeFlag == true) {
			return (true);
		} else {
			return false;
		}

	}

	/* (non-Javadoc)
	 * @see projects.admin.pages.AbsPage#myPageName()
	 */
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	/* (non-Javadoc)
	 * @see projects.admin.pages.AbsPage#navigateTo()
	 */
	@Override
	public void zNavigateTo() throws HarnessException {

		if (zIsActive()) {
			return;
		}

		// Make sure we are logged in
		if (!((TouchPages) MyApplication).zPageMain.zIsActive()) {
			((TouchPages) MyApplication).zPageMain.zNavigateTo();
		}

		tracer.trace("Navigate to " + this.myPageName());

		SleepUtil.sleepMedium();
		sClickAt(PageMain.Locators.zNavigationButton, "0,0");
		SleepUtil.sleepSmall();

		sClickAt(PageMain.Locators.zAppsButton, "0,0");
		SleepUtil.sleepSmall();

		sClickAt(PageMain.Locators.zContactsApp, "0,0");
		SleepUtil.sleepSmall();

		if (!this.sIsElementPresent("css=span[class='x-button-icon x-shown add']")) {
			throw new HarnessException("Unable to determine locator : " + "css=span[class='x-button-icon x-shown add']");
		}

		this.zWaitForBusyOverlay();

		activeFlag = true;

	}

	/* (non-Javadoc)
	 * @see com.zimbra.qa.selenium.framework.ui.AbsTab#zRefresh()
	 */
	public void zRefresh() throws HarnessException {

		((TouchPages)this.MyApplication).zPageMail.zToolbarPressButton(Button.B_FOLDER_TREE);
		((TouchPages)this.MyApplication).zPageMain.sClickAt(PageMain.Locators.zContactsApp, "0,1");

		SleepUtil.sleepLong();
	}

	//get subFolders
	public List<FolderItem> zListGetFolders(ZimbraAccount account, FolderItem parentFolder) throws HarnessException {
		List <FolderItem> list = new ArrayList<FolderItem>();
		String folderId = "zti" + ((parentFolder.getName().equals("USER_ROOT"))?"h":"") + "__main_Contacts__" + ((parentFolder.getName().equals("USER_ROOT"))?"ADDRBOOK":parentFolder.getId()) +"_div";

		//ensure it is in Addressbook main page
		zNavigateTo();

		String elements="window.document.getElementById('" + folderId + "').nextSibling.childNodes";
	    int length = Integer.parseInt(sGetEval(elements + ".length"));


	    for (int i=0; i<length; i++) {
	        String id= sGetEval(elements + "[" + i +"].id");

	        if (id.contains("Contacts")) {
		       list.add(FolderItem.importFromSOAP(account, sGetText("css=td#" + id + "_textCell")));
	        }
	      }

	    return list;
	}

	public boolean zIsContactDisplayed(ContactItem contactItem) throws HarnessException {
        String setslocator = "css=div[id^='ext-contactsitemview'] div[class='zcs-contactview-fieldSets']";
        if (!this.sIsElementPresent(setslocator)) {
			throw new HarnessException(setslocator);
		}
        WebElement we = this.getElement(setslocator);
        String setLocator = "css=div[class='zcs-contactview-fieldSet']";
        List<WebElement> wes = we.findElements(By.cssSelector(setLocator));
        Iterator<WebElement> itr = wes.iterator();
        while(itr.hasNext()) {
        	String labelLocator = "css=div[class='zcs-contactview-label']";
        	String valueLocator = "css=div[class='zcs-contactview-field']";
        	WebElement ww1 = ((WebElement)itr.next()).findElement(By.cssSelector(labelLocator));
        	WebElement ww2 = ((WebElement)itr.next()).findElement(By.cssSelector(valueLocator));
        	String value = contactItem.getAttribute(ww1.getText());
        	if (ww2.getText() != value) {
        		return false;
        	}
        }

        return true;
	}

	public boolean zIsSlectedContactDisplayedonMainframe(ContactItem contactItem) throws HarnessException {
        boolean isContactFound = false;
        // Ensure it is in Address book main page
		zNavigateTo();

        //assume that this is a list view
		String listLocator = "div[id='zv__CNS-main']";
		String rowLocator  = "li[id^='zli__CNS-main__']";
	    String noResultLocator = "td.NoResults";
		String fileAsLocator = " td[id^=zlif__CNS-main__][id$=__fileas]";

		//actually this is a search view
		if (zIsInSearchView()) {
			listLocator= "div[id=zv__CNS-SR-Contacts-1]";
		   	rowLocator= "li[id^=zli__CNS-SR-Contacts-1__]";
		   	fileAsLocator=" td[id^=zlif__CNS-SR-Contacts-1__][id$=__fileas]";
		}

		// if there is no result
		if (sIsElementPresent("css=" + listLocator + " " + noResultLocator)) {
           return false;
		}

		if (!this.sIsElementPresent("css=" + listLocator + " " + rowLocator)) {
			throw new HarnessException("css=" + listLocator + " " + rowLocator + " not present");
		}

		//Get the number of contacts (String)
		int count = this.sGetCssCount("css=" + listLocator + " " + rowLocator);

		logger.info(myPageName() + " zIsContactDisplayed: number of contacts: "+ count);

		// Get each contact's data from the table list
		for (int i = 1; i <= count && !isContactFound; i++) {
			String commonLocator = "css=" + listLocator + " li:nth-child(" + i +")";

			String contactType = getContactType(commonLocator);

			String contactDisplayedLocator = commonLocator + fileAsLocator;
			String fileAs = sGetText(contactDisplayedLocator);
			logger.info("...found "+ contactType + " - " + fileAs );
			isContactFound = ((contactType.equals(ContactGroupItem.IMAGE_CLASS) &&  contactItem instanceof ContactGroupItem) ||
				  (contactType.equals(ContactItem.IMAGE_CLASS) &&  contactItem instanceof ContactItem)) &&
				  (contactItem.fileAs.equals(fileAs.trim()));


		}


		return isContactFound;
	}

    // only return the list with a certain contact type
	// contactType should be one of ContactGroupItem.IMAGE_CLASS , ContactItem.IMAGE_CLASS
	public List<ContactItem> zListGetContacts(String contactType) throws HarnessException {

		List <ContactItem> list= new ArrayList<ContactItem>();

		//ensure it is in Addressbook main page
		//zNavigateTo();

		//assume that this is a list view
		String listLocator = "div[id='zv__CNS-main']";
		String rowLocator  = "li[id^='zli__CNS-main__']";
        String fileAsLocator = " td[id^=zlif__CNS-main__][id$=__fileas]";
        String noResultLocator = " td.NoResults";

		//actually this is a search view
		if (zIsInSearchView()) {
			listLocator= "div[id=zv__CNS-SR-Contacts-1]";
		   	rowLocator= "li[id^=zli__CNS-SR-Contacts-1__]";
		   	fileAsLocator=" td[id^=zlif__CNS-SR-Contacts-1__][id$=__fileas]";
		}

		// no result
		if (sIsElementPresent("css=" + listLocator +  noResultLocator)) {
			return list;
		}

		if (!this.sIsElementPresent("css=" + listLocator + " " + rowLocator)) {
			throw new HarnessException("css=" + listLocator + " " + rowLocator + " not present");
		}

	    int count = this.sGetCssCount("css=" + listLocator + " " + rowLocator);

		logger.info(myPageName() + " zListGetContacts: number of contacts: "+ count);

		// Get each contact's data from the table list
		for (int i = 1; i <= count; i++) {
			String commonLocator = "css=" + listLocator + " li:nth-child(" + i +")";

		    if (sIsElementPresent(commonLocator + " div[class*=" + contactType + "]")) {

			    ContactItem ci=null;
			    String contactDisplayedLocator = commonLocator + fileAsLocator;
			    String fileAs = sGetText(contactDisplayedLocator);
		        logger.info(" found " + fileAs);

		        //check contact type
		        if ( contactType.equals(ContactGroupItem.IMAGE_CLASS)) {
		        	ci=new ContactGroupItem(fileAs);
		        }
		        else if (  contactType.equals(ContactItem.IMAGE_CLASS) ||
		        		   contactType.equals(ContactItem.GAL_IMAGE_CLASS)) {
		        	ci=new ContactItem(fileAs);
		        }
		        else {
		        	throw new HarnessException("Image not neither conntact group nor contact.");
		        }

		        list.add(ci);
			}
		}


		return list;
	}


	public List<ContactItem> zListGetContacts() throws HarnessException {

		List <ContactItem> list= new ArrayList<ContactItem>();

		//ensure it is in Addressbook main page
		zNavigateTo();
		if ( !this.sIsElementPresent("id=zv__CNS-main") )
		//maybe return empty list?????
			throw new HarnessException("Contact List is not present "+ "id='zv__CNS-main'");

		//Get the number of contacts (String)
		int count = this.sGetCssCount("css=div[id='zv__CNS-main'] li[id^=zli__CNS-main__]");

		logger.info(myPageName() + " zListGetContacts: number of contacts: "+ count);

		// Get each contact's data from the table list
		for (int i = 1; i <= count; i++) {
			String commonLocator = "css=div[id='zv__CNS-main'] li:nth-child("+ i +")";

			String contactType = getContactType(commonLocator);

			ContactItem ci=null;
			String contactDisplayedLocator = commonLocator + " div[id^=zlif__CNS-main__][id$=__fileas]";
			String fileAs = sGetText(contactDisplayedLocator);
		    logger.info(" found " + fileAs);

			//check if it is a contact. contactgroup, gal, or dlist item
			if ( contactType.equals(ContactGroupItem.IMAGE_CLASS)) {
                ci=new ContactGroupItem(fileAs);
			}
			else if (  contactType.equals(ContactItem.IMAGE_CLASS) ) {
				ci=new ContactItem(fileAs);
			}
			else if (  contactType.equals(GALItem.IMAGE_CLASS) ) {
				ci=new GALItem(fileAs);
			}
			else if (  contactType.equals(DistributionListItem.IMAGE_CLASS) ) {
				ci=new DistributionListItem(fileAs,fileAs); //TODO???
			}
			else {
				throw new HarnessException("Image type not valid.");
			}

			list.add(ci);
		}


		return list;
	}


	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton("+ button +")");

		tracer.trace("Press the "+ button +" button");

		if ( button == null )
			throw new HarnessException("Button cannot be null!");


		
		//
		String locator = null;		
		AbsPage page = null;

		if ( button == Button.B_REFRESH ) {

			return (((TouchPages)this.MyApplication).zPageMain.zToolbarPressButton(Button.B_REFRESH));

		} else if ( button == Button.B_NEW ) {

			locator = Locators.PlusIcon;
			page = new FormContactNew(this.MyApplication);


		} else if ( button == Button.B_EDIT ) {

			locator = Locators.EditIcon;
			page = new FormContactNew(this.MyApplication);

	    } else if ( button == Button.B_MOVE) {

		    String id = "zb__CNS__MOVE_left_icon";

		    if (sIsElementPresent("css=td#" + id + " div[class*=ZDisabledImage]")) {
				throw new HarnessException("Tried clicking on "+ button +" but it was disabled "+ id);
			}

		   locator = "id="+ id;
		   page = null;
	    } else if ( button == Button.B_CANCEL) {
 	    	//String id ="dizb__CN__CANCEL";
 	    	locator = "css=div[id^=zb__CN][id$=__CANCEL]" ;
		    if (zIsElementDisabled(locator)) {
				throw new HarnessException("Tried clicking on "+ locator +" but it was disabled ");
		    }

		    page = null;
	    //click close without changing contact contents
	    } else if ( button == Button.B_CLOSE) {
 	    	locator = "css=div[id^=zb__CN][id$=__CANCEL]" ;
		    if (zIsElementDisabled(locator)) {
				throw new HarnessException("Tried clicking on "+ locator +" but it was disabled ");
		    }
	    }

	    if ( locator == null )
			throw new HarnessException("locator was null for button "+ button);


		// Make sure the button exists
		if ( !sIsElementPresent(locator) )
			throw new HarnessException("Button is not present locator="+ locator +" button="+ button);

		// Click it
		sClickAt(locator,"0,0");

		zWaitForBusyOverlay();


		if ( page != null ) {
			//sWaitForPageToLoad();
			page.zWaitForActive();
		}
		return (page);
	}

	public void clickDistributionListsFolder(TouchPages app) throws HarnessException {
	      FolderItem contactFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), "Distribution Lists");
	      app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, contactFolder);
	}


	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown("+ pulldown +", "+ option +")");

		tracer.trace("Click pulldown "+ pulldown +" then "+ option);

		if ( pulldown == null )
			throw new HarnessException("Button cannot be null!");

		//SleepUtil.sleepSmall();

		String pulldownLocator = null;
		String optionLocator = null;
		AbsPage page = null;

		if ( pulldown == Button.B_TAG ) {

	      if ( option == Button.O_TAG_NEWTAG ) {

	         pulldownLocator = "css=td#zb__CNS-main__TAG_MENU_dropdown div.ImgSelectPullDownArrow";
	         optionLocator = "css=td#contacts_newtag_title";

	         page = null;

	      } else if ( option == Button.O_TAG_REMOVETAG ) {

	    	 pulldownLocator = "css=td#zb__CNS-main__TAG_MENU_dropdown div.ImgSelectPullDownArrow";
		     optionLocator = "css=div[id='zb__CNS-main__TAG_MENU|MENU'] div[id^=contacts_removetag] td.ZWidgetTitle";
			 page = null;


	      }

	   } else if ( pulldown == Button.B_NEW ) {

		   pulldownLocator = "css=div#zb__NEW_MENU td#zb__NEW_MENU_dropdown";
		   if ( option == Button.O_NEW_CONTACT ) {
                optionLocator="css=div#zb__NEW_MENU_NEW_CONTACT";
			    page = new FormContactNew(this.MyApplication);
		   }
		   else if ( option == Button.O_NEW_CONTACTGROUP) {
				optionLocator="css=div#zb__NEW_MENU_NEW_GROUP";
				page = new FormContactGroupNew(this.MyApplication);
		   }
		   else if ( option == Button.O_NEW_TAG ) {
		        optionLocator = "css=div#zb__NEW_MENU_NEW_TAG td#zb__NEW_MENU_NEW_TAG_title";
		        page = null;
		   }
		   else if ( option == Button.O_NEW_CONTACTS_FOLDER ) {
			    optionLocator = "css=div#zb__NEW_MENU_NEW_ADDRBOOK td#zb__NEW_MENU_NEW_ADDRBOOK_title";
			    page = null;


		   } else {
			   //option not suppored
			   pulldownLocator=null;
		   }

	   } else if ( pulldown == Button.B_ACTIONS ) {

		   pulldownLocator = Locators.MenuIcon;

		   if ( option == Button.B_DELETE) {
			    optionLocator="css=div[id^='ext-listitem'] div[id^='ext-element']:contains('Delete')";
			    page = this;
		   } else if (option == Button.B_MOVE) {
			    optionLocator="css=div[id^='ext-listitem'] div[id^='ext-element']:contains('Move')";
		        page = new MoveContactView(this.MyApplication);
		   } else if (option == Button.B_TAG) {
			    optionLocator="css=div[id^='ext-listitem'] div[id^='ext-element']:contains('Tag')";
		        page = new TagContactView(this.MyApplication);
		   }
	   }

	// Default behavior
		if ( pulldownLocator != null ) {

			// Make sure the locator exists
			if ( !sIsElementPresent(pulldownLocator) ) {
				throw new HarnessException("Button "+ pulldown +" option "+ option +" pulldownLocator "+ pulldownLocator +" not present!");
			}

			//central coordinate "x,y"
			//String center= sGetElementWidth(pulldownLocator)/2 + "," + sGetElementHeight(pulldownLocator)/2;
			if ( this.zIsBrowserMatch(BrowserMasks.BrowserMaskIE)) {

				// TODO check if the following code make the test case CreateContactGroup.GroupOfNewEmail() pass in wdc
			    	/*
			    	sGetEval("return var evObj = document.createEventObject();"
						+ "var x = selenium.browserbot.findElementOrNull('" + pulldownLocator + "');"
						+ "x.focus();x.blur();x.fireEvent('onclick');");
			    	*/
				//the following code failed in wdc, but pass in my machine :
				sClickAt(pulldownLocator,"");
			}
			else {
			    //others
			    sClickAt(pulldownLocator,"");
			}

			zWaitForBusyOverlay();

			if ( optionLocator != null ) {
             	// Make sure the locator exists and visible
				zWaitForElementPresent(optionLocator);

				if (!zIsElementDisabled(optionLocator)) {
				   sClick(optionLocator);
				   zWaitForBusyOverlay();
				}

			}

			// If we click on pulldown/option and the page is specified, then
			// wait for the page to go active
			if ( page != null ) {
				//sWaitForPageToLoad();
				page.zWaitForActive();
			}

		}
	    return page;
	}

	public AbsPage zToolbarPressPulldownMount(Button pulldown, Button option) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown("+ pulldown +", "+ option +")");

		tracer.trace("Click pulldown "+ pulldown +" then "+ option);

		if ( pulldown == null )
			throw new HarnessException("Button cannot be null!");

		SleepUtil.sleepSmall();

		String pulldownLocator = null;
		String optionLocator = null;
		AbsPage page = null;

		if ( pulldown == Button.B_TAG ) {

	      if ( option == Button.O_TAG_NEWTAG ) {

	         pulldownLocator = "css=td#zb__CNS-main__TAG_MENU_dropdown div.ImgSelectPullDownArrow";
	         optionLocator = "css=td#contacts_newtag_title";

	         page = null;

	      }

	   } else if ( pulldown == Button.B_ACTIONS ) {

		   pulldownLocator = Locators.MenuIconMount;

		   if ( option == Button.B_DELETE) {
			    optionLocator="css=div[id^='ext-listitem'] div[id^='ext-element']:contains('Delete')";
			    page = this;
		   } else if (option == Button.B_MOVE) {
			    optionLocator="css=div[id^='ext-listitem'] div[id^='ext-element']:contains('Move')";
		        page = new MoveContactView(this.MyApplication);
		   } else if (option == Button.B_TAG) {
			    optionLocator="css=div[id^='ext-listitem'] div[id^='ext-element']:contains('Tag')";
		        page = new TagContactView(this.MyApplication);
		   }
	   }

	// Default behavior
		if ( pulldownLocator != null ) {

			// Make sure the locator exists
			if ( !sIsElementPresent(pulldownLocator) ) {
				throw new HarnessException("Button "+ pulldown +" option "+ option +" pulldownLocator "+ pulldownLocator +" not present!");
			}

			//central coordinate "x,y"
			//String center= sGetElementWidth(pulldownLocator)/2 + "," + sGetElementHeight(pulldownLocator)/2;
			if ( this.zIsBrowserMatch(BrowserMasks.BrowserMaskIE)) {

				// TODO check if the following code make the test case CreateContactGroup.GroupOfNewEmail() pass in wdc
			    	/*
			    	sGetEval("return var evObj = document.createEventObject();"
						+ "var x = selenium.browserbot.findElementOrNull('" + pulldownLocator + "');"
						+ "x.focus();x.blur();x.fireEvent('onclick');");
			    	*/
				//the following code failed in wdc, but pass in my machine :
				sClickAt(pulldownLocator,"");
			}
			else {
			    //others
			    sClickAt(pulldownLocator,"");
			}

			zWaitForBusyOverlay();

			if ( optionLocator != null ) {
             	// Make sure the locator exists and visible
				zWaitForElementPresent(optionLocator);

				if (!zIsElementDisabled(optionLocator)) {
				   sClick(optionLocator);
				   zWaitForBusyOverlay();
				}

			}

			// If we click on pulldown/option and the page is specified, then
			// wait for the page to go active
			if ( page != null ) {
				//sWaitForPageToLoad();
				page.zWaitForActive();
			}

		}
	    return page;
	}


	public AbsPage zSelectContact(String contact) throws HarnessException {
		logger.info(myPageName() + " zSelectContact("+ contact +")");

		if ( contact == null )
			throw new HarnessException("Contact cannot be null!");

		AbsPage page = null;

		this.sClickAt("css=div[id^='ext-contactslistview'] div[class='zcs-contactList-name']:contains('"+ contact +"')", "0,0");

		SleepUtil.sleepMedium();

	    return page;
	}

	public Boolean zVerifyContactExists(String contact) throws HarnessException {
		logger.info(myPageName() + " zVerifyContactExists("+ contact +")");

		if ( contact == null )
			throw new HarnessException("Contact cannot be null!");

		String locator = "css=div[id^='ext-contactslistview'] div[class='zcs-contactList-name']:contains('"+ contact +"')";

		if (this.sIsElementPresent(locator)) {
			return true;
		} else {
			return false;
		}
	}

	public AbsPage zToolbarPressPulldown(Button pulldown, IItem item) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown("+ pulldown +", "+ item +")");

		tracer.trace("Click pulldown "+ pulldown +" then "+ item);

		if ( pulldown == null )
			throw new HarnessException("Button cannot be null!");

		String pulldownLocator = null;
		String optionLocator = null;
		AbsPage page = null;
	   if ( pulldown == Button.B_MOVE ) {

	      if ( item instanceof FolderItem) {
             FolderItem folder = (FolderItem) item;
	         pulldownLocator = "css=td#zb__CNS-main__MOVE_MENU_dropdown.ZDropDown";
	         optionLocator   = "css=td#zti__ZmFolderChooser_ContactsCNS-main__" + folder.getId() + "_textCell.DwtTreeItem-Text";
	         //TODO page=?
	      }
	   }
	   else if ( pulldown == Button.B_TAG ) {
		   if ( item instanceof TagItem) {
			 pulldownLocator = "css=td#zb__CNS-main__TAG_MENU_dropdown div.ImgSelectPullDownArrow";

			 //Selenium cannot find the following optionLocator
             //optionLocator = "css=div#zb__CNS-main__TAG_MENU|MENU div:contains('" +((TagItem)item).getName()   + "'";

		     page = null;
	       }
	   }

	   if ( pulldownLocator != null ) {

			// Make sure the locator exists
			if ( !sIsElementPresent(pulldownLocator) ) {
				throw new HarnessException("Button "+ pulldown +" folder "+ item +" pulldownLocator "+ pulldownLocator +" not present!");
			}

			//central coordinate "x,y"
			String center= sGetElementWidth(pulldownLocator)/2 + "," + sGetElementHeight(pulldownLocator)/2;
			sClickAt(pulldownLocator,center);

			zWaitForBusyOverlay();

			// find optionLocator
			if ( pulldown == Button.B_TAG ) {
				String tagName = ((TagItem)item).getName();

				//get number of menu's options
				int countOption= Integer.parseInt(sGetEval("window.document.getElementById('zb__CNS-main__TAG_MENU|MENU').children[0].children[0].children.length"));
				String id= null;

				//find option id contains the tag name
				for (int i=0; i <countOption; i++) {
				 id= sGetEval("window.document.getElementById('zb__CNS-main__TAG_MENU|MENU').children[0].children[0].children[" + i + "].children[0].children[0].id");

				 if (sGetText("css=div#" + id).contains(tagName)) {
					 optionLocator = "css=div#" + id ;
					 break;
				 }
				}
			}

			if ( optionLocator != null ) {
				// Make sure the locator exists and visible
				zWaitForElementPresent(optionLocator);

				if (zIsVisiblePerPosition(optionLocator,0,0)) {
				   sClick(optionLocator);
				   zWaitForBusyOverlay();
				}

			}

			// If we click on pulldown/option and the page is specified, then
			// wait for the page to go active
			//if ( page != null ) {
			//	page.zWaitForActive();
			//}

		}
	    return page;

	}

	public AbsPage zToolbarPressPulldown(Button pulldown, Button option, Object item) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown("+ pulldown +", "+ option + " , " + item +")");

		tracer.trace("Click pulldown "+ pulldown +" then "+ option + " and " + item);

		if ( pulldown == null )
			throw new HarnessException("Button cannot be null!");

		String pulldownLocator  = null;
		String optionLocator    = null;
		String subOptionLocator = null;

		AbsPage page = null;

		if ( pulldown == Button.B_TAG ) {
			 pulldownLocator = "css=td#zb__CNS-main__TAG_MENU_dropdown div.ImgSelectPullDownArrow";

			 if (option == Button.O_TAG_REMOVETAG) {
				  optionLocator = "css=div[id='zb__CNS-main__TAG_MENU|MENU'] div[id^='contacts_removetag'] td[id^='contacts_removetag'][id$=_title]";


			 }
		     page = null;
	    }


		if ( pulldownLocator != null ) {

			// Make sure the locator exists
			if ( !sIsElementPresent(pulldownLocator) ) {
				throw new HarnessException("Button "+ pulldown +" folder "+ item +" pulldownLocator "+ pulldownLocator +" not present!");
			}

			//central coordinate "x,y"
			String center= sGetElementWidth(pulldownLocator)/2 + "," + sGetElementHeight(pulldownLocator)/2;
			sClickAt(pulldownLocator,center);

			zWaitForBusyOverlay();

			// find optionLocator

			if ( optionLocator != null ) {
				// Make sure the locator exists and visible
				zWaitForElementPresent(optionLocator);

				if (zIsVisiblePerPosition(optionLocator,0,0)) {
				   sMouseOver(optionLocator);
				   zWaitForBusyOverlay();

				   if (item instanceof TagItem) {

					   if (item == TagItem.Remove_All_Tags) {
						   subOptionLocator = "css=div[id=REMOVE_TAG_MENU_TAG_MENU|MENU] div[id=REMOVE_ALL_TAGS]";
					   }
					   else {
						   subOptionLocator = "css=div[id=REMOVE_TAG_MENU_TAG_MENU|MENU] td[id=^Remove_tag_][id$=_title]:contains('" +  ((TagItem) item).getName() + "')";
					   }

						// find active menu id
						/*
					   //get number of z_shell's children
					   int countOption= Integer.parseInt(sGetEval("window.document.getElementById('z_shell').children.length"));
					   String parentMenuid= null;

					   //find id of the active menu
					   for (int i=countOption-1; i>0;  i--) {
						   parentMenuid= sGetEval("window.document.getElementById('z_shell').children[" + i + "].id");

						   if (sGetEval("window.document.getElementById('" + parentMenuid + "').getAttribute('class')").contains("ActionMenu ZHasIcon")
								 && sIsVisible(parentMenuid)) {
								 subOptionLocator = "css=div#" + parentMenuid + " td[id$=title]:contains(" + tagName + ")";
								 break;
						   }
					   }	*/
				   }

				   if (subOptionLocator != null) {
					   // Make sure the locator exists and visible
						zWaitForElementPresent(subOptionLocator);

						//if (zIsVisiblePerPosition(subOptionLocator,0,0)) {
						   sClick(subOptionLocator);
						   zWaitForBusyOverlay();
						//}
				   }
				}
			}

		}

		//if ( page != null ) {
		//	page.zWaitForActive();
		//}

	    return page;

	}
	// return the type of a contact
	private String getContactType(String locator) throws HarnessException {
		String imageLocator = locator +" div[class*=";


		if (sIsElementPresent(imageLocator + ContactGroupItem.IMAGE_CLASS + "]"))
		{
			return ContactGroupItem.IMAGE_CLASS;
		}
		else if (sIsElementPresent(imageLocator + ContactItem.IMAGE_CLASS + "]"))
		{
			return ContactItem.IMAGE_CLASS;
		}
		else if (sIsElementPresent(imageLocator + DistributionListItem.IMAGE_CLASS + "]"))
		{
			return DistributionListItem.IMAGE_CLASS;
		}
		logger.info(sGetAttribute(locator+ " div@class") + " not contain neither " + ContactGroupItem.IMAGE_CLASS + " nor " + ContactItem.IMAGE_CLASS );
		return null;
	}

    // return the xpath locator of a contact
	private String getContactLocator(String contact) throws HarnessException {
		//assume that this is a list view
		String listLocator = "div[id='zv__CNS-main']";
		String rowLocator  = "li[id^='zli__CNS-main__']";

		String contactLocator = null;

		//actually this is a search view
		if (zIsInSearchView()) {
			listLocator= "div[id=zv__CNS-SR-Contacts-1]";
		   	rowLocator= "li[id^=zli__CNS-SR-Contacts-1__]";
		}

		if (!this.sIsElementPresent("css=" + listLocator + " " + rowLocator)) {
			throw new HarnessException("css=" + listLocator + " " + rowLocator + " not present");
		}

		//Get the number of contacts (String)
	    int count = this.sGetCssCount("css=" + listLocator + " " + rowLocator);
		logger.debug(myPageName() + " zListItem: number of contacts: "+ count);

		if ( count == 0 )
			throw new HarnessException("List count was zero");

		// Get each contact's data from the table list
		for (int i = 1; i<=count; i++) {

			String itemLocator = "css=" + listLocator + " li:nth-child(" + i +")";
			if ( !this.sIsElementPresent(itemLocator) ) {
				throw new HarnessException("unable to locate item " + itemLocator);
			}


			String displayAs = sGetText(itemLocator);

			// Log this item to the debug output
			LogManager.getLogger("projects").info("zListItem: found contact "+ displayAs);
			if (displayAs != null) {
				if (displayAs.toLowerCase().contains(contact.toLowerCase())) {
					// Found the item!
					contactLocator = itemLocator;
					break;
				}
			}

		}


		if (contactLocator == null) {
			throw new HarnessException("Never found the contact "+ contact);
		}

		return contactLocator;
	}

    //get selected contacts locators
	private ArrayList<String> getSelectedContactLocator() throws HarnessException {
		String listLocator = "div#zv__CNS-main";
		String rowLocator = "li[id^='zli__CNS-main__']";


	    ArrayList<String> arrayList = new ArrayList<String>();

		if ( !sIsElementPresent("css=" + listLocator) )
			throw new HarnessException("List View Rows is not present "+ listLocator);

		if ( !sIsElementPresent("css=" + rowLocator) )
		    return arrayList; //an empty arraylist

		//Get the number of contacts (String)
		int count = sGetCssCount("css=" + listLocator + " " + rowLocator);

		logger.debug(myPageName() + " getSelectedContactLocator: number of contacts: "+ count);

		if ( count == 0 )
			throw new HarnessException("List count was zero");

		// Get each contact's data from the table list
		for (int i = 1; i<=count; i++) {
			String itemLocator = "css=" + listLocator + " li:nth-child(" + i +")";

			if ( !sIsElementPresent(itemLocator) ) {
				logger.info("reach the end of list - unable to locate item " + itemLocator);
				break;
			}

			if (sIsElementPresent(itemLocator+ "[class*=Row-selected]")) {
			    arrayList.add(itemLocator);
			}

			// Log this item to the debug output
			LogManager.getLogger("projects").info("getSelectedContactLocator: found selected contact "+ itemLocator);

		}

		return arrayList;
	}



	public AbsPage zListItem(Action action, Button option ,Button subOption, String contact) throws HarnessException {
		String locator = null;		
		AbsPage page = null;
		String parentLocator = null;
		String extraLocator="";

		tracer.trace(action +" then "+ option +" then "+ subOption +" on contact = "+ contact);

        if ( action == Action.A_RIGHTCLICK ) {
			ContextMenuItem cmi=null;
		    ContextMenuItem sub_cmi = null;

		    sRightClickAt(getContactLocator(contact),"0,0");


			if (option == Button.B_TAG) {

				cmi=CONTEXT_MENU.CONTACT_TAG;

				if (subOption == Button.O_TAG_NEWTAG) {
					sub_cmi = CONTEXT_SUB_MENU.CONTACT_SUB_NEW_TAG;
					page = null;
				}

				else if (subOption == Button.O_TAG_REMOVETAG) {
					sub_cmi = CONTEXT_SUB_MENU.CONTACT_SUB_REMOVE_TAG;
					//parentLocator= "div[id^=TAG_MENU__DWT][id$=|MENU]";
					parentLocator= "div[id='TAG_MENU|MENU']";

					page = null;
				}
			}
			else if (option == Button.B_CONTACTGROUP) {
				if (subOption == Button.O_NEW_CONTACTGROUP) {
					cmi= CONTEXT_MENU.CONTACT_GROUP;
					sub_cmi= CONTEXT_SUB_MENU.CONTACT_SUB_NEW_CONTACT_GROUP;
					page = null;
				}
			}
			else if (option == Button.B_SEARCH) {
				cmi=CONTEXT_MENU.CONTACT_SEARCH;
				if (subOption == Button.O_SEARCH_MAIL_SENT_TO_CONTACT) {
					sub_cmi = CONTEXT_SUB_MENU.CONTACT_SUB_SENT_TO_CONTACT;
		    		page = ((TouchPages)MyApplication).zPageSearch;
				}

				else if (subOption == Button.O_SEARCH_MAIL_RECEIVED_FROM_CONTACT) {
					sub_cmi = CONTEXT_SUB_MENU.CONTACT_SUB_RECEIVED_FROM_CONTACT;
					page = ((TouchPages)MyApplication).zPageSearch;

				}

			}
			else {
				throw new HarnessException("option " + option + " not supported.");
			}


			if ((cmi == null) || (sub_cmi == null)) {
				throw new HarnessException("option " + option + " not supported.");
			}


			if (zIsInSearchView()) {
				locator = "css=div[id^=zm__Contacts__DWT]";
			} else {
				locator = "css=div#zm__Contacts";
			}

			if ( ((option == Button.B_CONTACTGROUP) && (subOption == Button.O_NEW_CONTACTGROUP)) ||
				  (option == Button.B_SEARCH) )
			{
				locator = locator + " tr[id^="+ cmi.locator + "]";
			}
			else {
				locator = locator + " tr#"+ cmi.locator;
			}
			//locator = "id="+ id;

			//  Make sure the context menu exists
			zWaitForElementPresent(locator) ;

			// TODO: Check if the item is enabled
			//if (zIsElementDisabled("div#" + id )) {
			//	throw new HarnessException("Tried clicking on "+ cmi.text +" but it was disabled ");
			//}

			//For Safari
			// as an alternative for sMouseOver(locator)
		    if (zIsBrowserMatch(BrowserMasks.BrowserMaskSafari)) {
				zKeyboard.zTypeKeyEvent(KeyEvent.VK_DOWN);
				zKeyboard.zTypeKeyEvent(KeyEvent.VK_DOWN);
				zKeyboard.zTypeKeyEvent(KeyEvent.VK_DOWN);
				zKeyboard.zTypeKeyEvent(KeyEvent.VK_DOWN);

				ArrayList<String> selectedContactArrayList=getSelectedContactLocator();
		        String contactType = getContactType(selectedContactArrayList.get(0));

		        //check if it is a contact
                if (  contactType.equals(ContactItem.IMAGE_CLASS) ) {
    				zKeyboard.zTypeKeyEvent(KeyEvent.VK_DOWN);
    				zKeyboard.zTypeKeyEvent(KeyEvent.VK_DOWN);

			    }

				zKeyboard.zTypeKeyEvent(KeyEvent.VK_RIGHT);
		    }
		    else {
			 // Mouse over the option
			 sFocus(locator);
			 sMouseOver(locator);
		    }

		    zWaitForBusyOverlay();

			if (option == Button.B_SEARCH) {

				//find parent locators

				try {

					int total= Integer.parseInt(sGetEval("window.document.getElementById('z_shell').childNodes.length")) -1;

				    for (int i=total; i>=0 ; i--, parentLocator=null) {
				    	parentLocator = sGetEval("window.document.getElementById('z_shell').childNodes[" + i + "].id" );
				    	if ( parentLocator.startsWith("POPUP_DWT") && zIsVisiblePerPosition(parentLocator, 0, 0))
				        {
				    		logger.info("parent = " + parentLocator);
				    		parentLocator = "div#" + parentLocator;
				    		break;
				    	}
			        }


				}
			    catch (Exception e) {
					parentLocator=null;
					logger.info("cannot find parent id for " + sub_cmi.locator + " " + e.getMessage());
				}

			}

	    	if (parentLocator != null) {
				locator = "css=" + parentLocator + " " + sub_cmi.locator + extraLocator;
			}
	    	else {
	            locator = "css=" + sub_cmi.locator + extraLocator;
	    	}


			//  Make sure the sub context menu exists
			zWaitForElementPresent(locator) ;

			// make sure the sub context menu enabled
			//zWaitForElementEnabled(locator);

        }

        //ExecuteHarnessMain.ResultListener.captureScreen();

       // SleepUtil.sleep(987654321);
    	//else {
       if (option == Button.B_SEARCH) {
			if (subOption == Button.O_SEARCH_MAIL_SENT_TO_CONTACT) {
				locator="css=td[id^=SEARCH_TO__DWT][id$=_title]:contains('Sent To Contact')";
			}

			else if (subOption == Button.O_SEARCH_MAIL_RECEIVED_FROM_CONTACT) {
				locator="css=td[id^=SEARCH__DWT][id$=_title]:contains('Received From Contact')";
			}
       }
       		//if (subOption == Button.O_TAG_REMOVETAG) {
       		//    ExecuteHarnessMain.ResultListener.captureScreen();
       		//}

    		sFocus(locator);
            sMouseOver(locator);
            SleepUtil.sleepSmall();
            //jClick(locator);
            //sClickAt(locator, "0,0");
             sClickAt(locator, "0,0");
     	//}
       zWaitForBusyOverlay();


		if ( page != null ) {
			//sWaitForPageToLoad();
			page.zWaitForActive();
		}
		return (page);

	}



	/**
	 * Action -> Option -> suboption -> object on contact.  For example,
	 * Right click -> Tag -> Remove Tag -> tagname on ContactA
	 * @param action e.g. A_RIGHTCLICK
	 * @param option e.g B_TAG
	 * @param subOption e.g O_TAG_REMOVETAG
	 * @param choice e.g. String tagname
	 * @param contact The contact to take the action on
	 * @return
	 * @throws HarnessException
	 */
	public AbsPage zListItem(Action action, Button option, Button subOption, Object choice, String contact) throws HarnessException {

		AbsPage page = null;
		String contactLocator = getContactLocator(contact);
		String locator = null;


		tracer.trace(action +" then "+ option +" then "+ subOption + " and choose " + choice + " on contact = "+ contact);

        if ( action == Action.A_RIGHTCLICK ) {

			if (option == Button.B_TAG) {

				if (subOption == Button.O_TAG_REMOVETAG) {

					if ( !(choice instanceof String) ) {
						throw new HarnessException("choice must be a string of the tag name! "+ choice);
					}

					String tagName = (String)choice;


					String tagContactLocator = "css=div[id^='zm__Contacts'] div[id^='TAG_MENU'] td[id$='_title']";
					String removeTagLocator = "css=div[id^='TAG_MENU|MENU'] div[id^='contacts_removetag'] td[id$='_title']";
					locator = "css=div[id='REMOVE_TAG_MENU_TAG_MENU|MENU'] td[id=^Remove_tag_][id$=_title]:contains('" +  tagName + "')";

				    // Right click on contact
				    sRightClickAt(contactLocator,"0,0");
					zWaitForBusyOverlay();

				    // Left Click "Tag"
					this.sMouseOver(tagContactLocator);
					SleepUtil.sleepMedium();
					sClickAt(tagContactLocator, "");
					SleepUtil.sleepMedium();
					zWaitForBusyOverlay();

					/*
					 The context menu has two different looks, depending on how
					 many tags are on the item.

					 If 0 tags, then the "remove tag" option is disabled.
					 If 1 tag, then the "remove tag" option appears without a sub menu.
					 If 1+ tags, then the "remove tag" option appears with a sub menu, where
					   a specific tag may be chosen.

					 */

					if ( this.sIsElementPresent("css=div[id^='TAG_MENU|MENU'] div[id^='contacts_removetag'].ZHasDropDown") ) {

					    // Has sub menu
						// Mouse over "remove tag", then Left Click "<tag name>"
						this.sMouseOver(removeTagLocator);
						zWaitForBusyOverlay();

					    // Left Click "<tag name>"
						sClickAt(locator, "");
						zWaitForBusyOverlay();


					} else {

					    // No sub menu, just Left Click "Remove Tag"
						sClickAt(removeTagLocator, "");
						zWaitForBusyOverlay();


					}

					return (page);
				}
			}
	    }
		return (page);
	}

	public AbsPage zListItem(Action action, Button option, IItem item, String contact) throws HarnessException {

		AbsPage page = null;
		String contactLocator = getContactLocator(contact);
		String optionLocator = null;
		String itemLocator = null;


		tracer.trace(action +" then "+ option +" then "+ item +" on contact = "+ contact);


        if ( action == Action.A_RIGHTCLICK ) {


			if (option == Button.B_TAG) {

				// Hover over the context menu "tags" item
				optionLocator = "css=div#zm__Contacts div#TAG_MENU td[id$='_title']";

				if (item instanceof TagItem) {

					// Left click the existing tag
					itemLocator = "css=div[id^='TAG_MENU|MENU'] td[id$='_title']:contains('" + item.getName() + "')";

				}

			}
			else if (option == Button.B_CONTACTGROUP) {

				optionLocator = "css=div#zm__Contacts div[id^='CONTACTGROUP_MENU'] td[id$='_title']";

				if ( item instanceof ContactGroupItem) {
					itemLocator = "css=div[id^='CONTACTGROUP_MENU'] td[id$='_title']:contains('"+ item.getName() +"')";
				}

			}

			if ( !this.sIsElementPresent(contactLocator) ) {
				throw new HarnessException("Unable to right click on contact");
			}

        	// Right click on contact
			sRightClickAt(contactLocator,"0,0");
			this.zWaitForBusyOverlay();

			if ( !this.sIsElementPresent(optionLocator) ) {
				throw new HarnessException("Unable to hover over context menu");
			}

			// Mouse over the option
			sMouseOver(optionLocator);
			this.zWaitForBusyOverlay();

			// It seems to take a while to draw the context menu
			// Sleep a bit to let it draw.
			SleepUtil.sleepLong();


			if ( !this.sIsElementPresent(itemLocator) ) {
				throw new HarnessException("Unable to click on sub-menu");
			}

			// Left click the sub-option
			this.sClickAt(itemLocator, "");
			this.zWaitForBusyOverlay();

        }

        return (page);

	}

	@Override
	public AbsPage zListItem(Action action, Button option, String contact) throws HarnessException {
		String locator = null;		
		AbsPage page = null;

        String contactLocator = getContactLocator(contact);

		tracer.trace(action +" then "+ option +" on contact = "+ contact);

		if ( action == Action.A_RIGHTCLICK ) {
			ContextMenuItem cmi=null;


			if (option == Button.B_DELETE) {
                cmi=CONTEXT_MENU.CONTACT_DELETE;
			}
			else if (option == Button.B_MOVE) {
				cmi=CONTEXT_MENU.CONTACT_MOVE;
				page = null;
			}

			else if (option == Button.B_EDIT) {
				cmi=CONTEXT_MENU.CONTACT_EDIT;
				page = newFormSelected();
				// select the item only
				sClickAt(contactLocator,"0,0");
			}

			else if (option == Button.B_NEW) {
				cmi=CONTEXT_MENU.CONTACT_NEW_EMAIL;
				page = new FormMailNew(MyApplication);
			}


			else if (option == Button.B_PRINT) {
				cmi=CONTEXT_MENU.CONTACT_PRINT;
				page = null;
			}
			else if (option == Button.B_FORWARD) {
				cmi=CONTEXT_MENU.CONTACT_FORWARD;
				page = new FormMailNew(MyApplication);
			}
			else {
				throw new HarnessException("option " + option + " not supported");
			}

		    sRightClickAt(contactLocator,"0,0");


		    locator = "css=div#zm__Contacts tr#"+ cmi.locator;

		    if (option == Button.B_NEW) {
		    	locator = "css=div#zm__Contacts tr[id^="+ cmi.locator +"]";
		    }

			//locator = "id="+ id;


			//  Make sure the context menu exists
			zWaitForElementPresent(locator) ;

			// Check if the item is enabled
			if (sIsElementPresent(locator + "[class*=ZDisabled]")) {
				throw new HarnessException("Tried clicking on "+cmi.text +" but it was disabled ");
			}

		}


		sClickAt(locator,"0,0");

		zWaitForBusyOverlay();


		if ( page != null ) {
			page.zWaitForActive();
		}
		return (page);


	}

	public DisplayDList getDisplayDList() {
		return new DisplayDList(MyApplication);
	}


	/* (non-Javadoc)
	 * @see com.zimbra.qa.selenium.framework.ui.AbsTab#zListItem(com.zimbra.qa.selenium.framework.ui.Action, java.lang.String)
	 */
	@Override
	public AbsPage zListItem(Action action, String contact) throws HarnessException {
		logger.info(myPageName() + " zListItem("+ action +", "+ contact +")");
        String contactLocator=getContactLocator(contact);
    	AbsPage page = null;
		tracer.trace(action +" on contact = "+ contact);

		if ( action == Action.A_LEFTCLICK ) {
			//click
			sClick(contactLocator);
			zWaitForBusyOverlay();

			ArrayList<String> selectedContactArrayList=getSelectedContactLocator();
	        String contactType = getContactType(selectedContactArrayList.get(0));

	        //check if it is a contact or a contact group item
		    if ( contactType.equals(ContactGroupItem.IMAGE_CLASS)) {
			  page = new DisplayContactGroup(MyApplication);
		    }
		    else if (  contactType.equals(ContactItem.IMAGE_CLASS) ) {
			  page = new DisplayContact(MyApplication);
		    }
		    else {
			  throw new HarnessException(" Error: not support the contact type");
		    }

		}
		else if ( action == Action.A_CHECKBOX) {
            //enable user preference for checkbox


			//get the checkbox locator
			contactLocator=contactLocator + " div.ImgCheckboxUnchecked";

			//check the box
			sClick(contactLocator);

			//zWaitForBusyOverlay();

			ArrayList<String> selectedContactArrayList=getSelectedContactLocator();
	        String contactType = getContactType(selectedContactArrayList.get(0));

	        //check if it is a contact or a contact group item
		    if ( contactType.equals(ContactGroupItem.IMAGE_CLASS)) {
			  page = new DisplayContactGroup(MyApplication);
		    }
		    else if (  contactType.equals(ContactItem.IMAGE_CLASS) ) {
			  page = new DisplayContact(MyApplication);
		    }
		    else {
			  throw new HarnessException(" Error: not support the contact type");
		    }

		}
		else if (action == Action.A_RIGHTCLICK ) {

            sRightClickAt(contactLocator,"0,0");
            //zWaitForBusyOverlay();
            page = null;
		}
		else if (action == Action.A_DOUBLECLICK) {
		    sDoubleClick(contactLocator) ;
		    page = newFormSelected();
		}
		else {
			throw new HarnessException("Action " + action + " not supported");
		}

		if (page != null) {
		    page.zWaitForActive();
		}
		return page;
	}




	private AbsPage newFormSelected() throws HarnessException {
	    AbsPage page = null;
		ArrayList<String> selectedContactArrayList=getSelectedContactLocator();

	    if (selectedContactArrayList.size() == 0) {
		  throw new HarnessException("No selected contact/contact group ");
	    }

	    /*if (selectedContactArrayList.size() > 1) {
	      for (int i=0; i<selectedContactArrayList.size(); i++) {
	    	  logger.info(selectedContactArrayList.get(i));
	      }
		  throw new HarnessException("Cannot edit more than one contact/contact group ");
	    }*/

        String contactType = getContactType(selectedContactArrayList.get(0));

        //check if it is a contact or a contact group item
	    if ( contactType.equals(ContactGroupItem.IMAGE_CLASS)) {
		  page = new FormContactGroupNew(MyApplication);
	    }
	    else if (  contactType.equals(ContactItem.IMAGE_CLASS) ) {
		  page = new FormContactNew(MyApplication);
	    }

	    return page;

	}
	private boolean zIsInSearchView() throws HarnessException {
		return zIsVisiblePerPosition("css=div#z_filterPanel__SR-Contacts-1",0,0);
	}


	public void zSearchContact(Button button, String searchText) throws HarnessException{

		String locator = null;

		if ( button == Button.B_SEARCH) {

			locator = Locators.SearchContact;

		} else {
			throw new HarnessException("Button "+ button +" not yet implemented");
		}

		// Default behavior.  Click the locator
		SleepUtil.sleepMedium();
		sClickAt(locator, "");
        sType(locator, searchText);
        sFocus(locator);
        SleepUtil.sleepMedium();
        String keyCode = "13";
		zKeyDown(keyCode);
        SleepUtil.sleepMedium();

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

}
