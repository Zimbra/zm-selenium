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
package com.zimbra.qa.selenium.projects.ajax.pages.contacts;

import java.util.*;
import org.apache.log4j.LogManager;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.pages.*;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew;

public class PageContacts extends AbsTab {

	public static class Locators {
		public static final String zContactsZimletsPane = "ztih__main_Contacts__ZIMLET_textCell";
		public static final String zContactsTagsPane = "ztih__main_Contacts__TAG_textCell";
		public static final String zContactsFolder = "zti__main_Contacts__7_textCell";
		public static final String zViewCertificate = "css=td[class='ZmSecureMailCertificateRow'] div[class='FakeAnchor'] td[id$='title']:contains('View certificate')";
	}

	public static class CONTEXT_MENU {

		public static final String LOCATOR = "id='zm__Contacts'";

		// contact's context menu
		public static final ContextMenuItem CONTACT_SEARCH = new ContextMenuItem("POPUP_zmi__Contacts__SEARCH_MENU",
				"Find Emails...", "div[class*='ImgSearch']", " div[class*='ImgCascade']");
		public static final ContextMenuItem CONTACT_NEW_EMAIL = new ContextMenuItem("POPUP_zmi__Contacts__NEW_MESSAGE",
				"New Email", "div[class*='ImgNewMessage']", ":contains('nm')");

		public static final ContextMenuItem CONTACT_EDIT = new ContextMenuItem("POPUP_zmi__Contacts__CONTACT",
				"Edit Contact", "", "");
		public static final ContextMenuItem CONTACT_FORWARD = new ContextMenuItem(
				"POPUP_zmi__Contacts__SEND_CONTACTS_IN_EMAIL", "Forward Contact", "", "");

		public static final ContextMenuItem CONTACT_TAG = new ContextMenuItem("POPUP_zmi__Contacts__TAG_MENU",
				"Tag Contact", "div[class*='ImgTag']", " div[class='ImgCascade']");
		public static final ContextMenuItem CONTACT_DELETE = new ContextMenuItem("POPUP_zmi__Contacts__DELETE",
				"Delete", "div[class*='ImgDelete']", ":contains('Del')");
		public static final ContextMenuItem CONTACT_MOVE = new ContextMenuItem("POPUP_z" + "mi__Contacts__MOVE", "Move",
				"div[class*='ImgMoveToFolder']", "");
		public static final ContextMenuItem CONTACT_PRINT = new ContextMenuItem("POPUP_zmi__Contacts__PRINT_CONTACT",
				"Print", "div[class*='ImgPrint']", ":contains('p')");

		public static final ContextMenuItem CONTACT_GROUP = new ContextMenuItem(
				"POPUP_zmi__Contacts__CONTACTGROUP_MENU", "Contact Group", "div[class*='ImgGroup']", "");
		public static final ContextMenuItem CONTACT_QUICK_COMMAND = new ContextMenuItem("POPUP_QUICK_COMMANDS",
				"Quick Commands", "div[class='ImgQuickCommand']", "");

		public static final ContextMenuItem DL_DELETE_MENU = new ContextMenuItem("POPUP_zmi__Contacts__DELETE",
				"Delete", "div[class*='ImgDelete']", ":contains('Del')");
		public static final ContextMenuItem DL_EDIT_DISTRIBUTION_LIST_MENU = new ContextMenuItem(
				"POPUP_zmi__Contacts__CONTACT", "Edit Distribution List", "div[class*='ImgEdit']", ":contains('')");
		public static final ContextMenuItem DL_NEW_EMAIL_MENU = new ContextMenuItem("POPUP_zmi__Contacts__NEW_MESSAGE",
				"New Email", "div[class*='ImgNewMessage']", ":contains('nm')");

	}

	public static class CONTEXT_SUB_MENU {
		public static final ContextMenuItem CONTACT_SUB_NEW_TAG = new ContextMenuItem("div#contacts_newtag", "New Tag",
				"div[class='ImgNewTag']", ":contains('nt')");
		public static final ContextMenuItem CONTACT_SUB_REMOVE_TAG = new ContextMenuItem(
				"div[id*='contacts_removetag']", "Remove Tag", "div[class='ImgDeleteTag']", "");
		// public static final ContextMenuItem CONTACT_SUB_REMOVE_TAG = new
		// ContextMenuItem("td#zmi__Contacts__TAG_MENU|MENU|REMOVETAG_title",
		// "Remove Tag", "div[class='ImgDeleteTag']", "");
		public static final ContextMenuItem CONTACT_SUB_RECEIVED_FROM_CONTACT = new ContextMenuItem(
				"tr[id^=SEARCH__DWT]", "Received From Contact", "div[class='ImgSearch']", "");
		public static final ContextMenuItem CONTACT_SUB_SENT_TO_CONTACT = new ContextMenuItem("tr[id^=SEARCH_TO__DWT]",
				"Sent To Contact", "div[class='ImgSearch']", "");

		public static final ContextMenuItem CONTACT_SUB_NEW_CONTACT_GROUP = new ContextMenuItem(
				"div[id^='zmi__Contacts__CONTACTGROUP_MENU'][id$='|GROUP_MENU|NEWGROUP']", "New Contact Group",
				"div[class='ImgNewGroup']", "");
	}

	/**
	 * A mapping of letter characters (upper case) to addressbook buttons, e.g. 'A'
	 * -> Button.B_AB_A
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

		}
	};

	public PageContacts(AbsApplication application) {
		super(application);
		logger.info("new " + PageContacts.class.getCanonicalName());

	}

	@Override
	public boolean zIsActive() throws HarnessException {

		if (!((AjaxPages) MyApplication).zPageMain.zIsActive()) {
			((AjaxPages) MyApplication).zPageMain.zNavigateTo();
		}

		boolean active = sIsElementPresent("css=div[id='zb__App__Contacts'][class*=ZSelected]");

		String locator = "css=div#ztih__main_Contacts__ADDRBOOK_div";

		active &= this.sIsElementPresent(locator);
		return (active);

	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public void zNavigateTo() throws HarnessException {

		if (zIsActive()) {
			logger.info(myPageName() + " is already loaded");
			return;
		}

		((AjaxPages) MyApplication).zPageMain.zCheckAppLoaded(Locators.zContactsZimletsPane);
	}

	// get subFolders
	public List<FolderItem> zListGetFolders(ZimbraAccount account, FolderItem parentFolder) throws HarnessException {
		List<FolderItem> list = new ArrayList<FolderItem>();
		String folderId = "zti" + ((parentFolder.getName().equals("USER_ROOT")) ? "h" : "") + "__main_Contacts__"
				+ ((parentFolder.getName().equals("USER_ROOT")) ? "ADDRBOOK" : parentFolder.getId()) + "_div";

		// ensure it is in Addressbook main page
		zNavigateTo();

		String elements = "window.document.getElementById('" + folderId + "').nextSibling.childNodes";
		int length = Integer.parseInt(sGetEval(elements + ".length"));

		for (int i = 0; i < length; i++) {
			String id = sGetEval(elements + "[" + i + "].id");

			if (id.contains("Contacts")) {
				list.add(FolderItem.importFromSOAP(account, sGetText("css=td#" + id + "_textCell")));
			}
		}

		return list;
	}

	public boolean zIsContactDisplayed(ContactItem contactItem) throws HarnessException {
		boolean isContactFound = false;
		// ensure it is in Addressbook main page
		zNavigateTo();

		// assume that this is a list view
		String listLocator = "div[id='zv__CNS-main']";
		String rowLocator = "li[id^='zli__CNS-main__']";
		String noResultLocator = "td.NoResults";
		String fileAsLocator = " td[id^=zlif__CNS-main__][id$=__fileas]";

		// actually this is a search view
		if (zIsInSearchView()) {
			listLocator = "div[id=zv__CNS-SR-Contacts-1]";
			rowLocator = "li[id^=zli__CNS-SR-Contacts-1__]";
			fileAsLocator = " td[id^=zlif__CNS-SR-Contacts-1__][id$=__fileas]";
		}

		// if there is no result
		if (sIsElementPresent("css=" + listLocator + " " + noResultLocator)) {
			return false;
		}

		if (!this.sIsElementPresent("css=" + listLocator + " " + rowLocator)) {
			throw new HarnessException("css=" + listLocator + " " + rowLocator + " not present");
		}

		// Get the number of contacts (String)
		int count = this.sGetCssCount("css=" + listLocator + " " + rowLocator);

		logger.info(myPageName() + " zIsContactDisplayed: number of contacts: " + count);

		// Get each contact's data from the table list
		for (int i = 1; i <= count && !isContactFound; i++) {
			String commonLocator = "css=" + listLocator + " li:nth-child(" + i + ")";

			String contactType = getContactType(commonLocator);

			String contactDisplayedLocator = commonLocator + fileAsLocator;
			String fileAs = sGetText(contactDisplayedLocator);
			logger.info("...found " + contactType + " - " + fileAs);
			isContactFound = ((contactType.equals(ContactGroupItem.IMAGE_CLASS)
					&& contactItem instanceof ContactGroupItem)
					|| (contactType.equals(ContactItem.IMAGE_CLASS) && contactItem instanceof ContactItem))
					&& (contactItem.fileAs.equals(fileAs.trim()));

		}

		return isContactFound;
	}

	// only return the list with a certain contact type
	// contactType should be one of ContactGroupItem.IMAGE_CLASS ,
	// ContactItem.IMAGE_CLASS
	public List<ContactItem> zListGetContacts(String contactType) throws HarnessException {

		List<ContactItem> list = new ArrayList<ContactItem>();

		// ensure it is in Addressbook main page
		// zNavigateTo();

		// assume that this is a list view
		String listLocator = "div[id='zv__CNS-main']";
		String rowLocator = "li[id^='zli__CNS-main__']";
		String fileAsLocator = " td[id^=zlif__CNS-main__][id$=__fileas]";
		String noResultLocator = " td.NoResults";

		// actually this is a search view
		if (zIsInSearchView()) {
			listLocator = "div[id=zv__CNS-SR-Contacts-1]";
			rowLocator = "li[id^=zli__CNS-SR-Contacts-1__]";
			fileAsLocator = " td[id^=zlif__CNS-SR-Contacts-1__][id$=__fileas]";
		}

		// no result
		if (sIsElementPresent("css=" + listLocator + noResultLocator)) {
			return list;
		}

		if (!this.sIsElementPresent("css=" + listLocator + " " + rowLocator)) {
			throw new HarnessException("css=" + listLocator + " " + rowLocator + " not present");
		}

		int count = this.sGetCssCount("css=" + listLocator + " " + rowLocator);

		logger.info(myPageName() + " zListGetContacts: number of contacts: " + count);

		// Get each contact's data from the table list
		for (int i = 1; i <= count; i++) {
			String commonLocator = "css=" + listLocator + " li:nth-child(" + i + ")";

			if (sIsElementPresent(commonLocator + " div[class*=" + contactType + "]")) {

				ContactItem ci = null;
				String contactDisplayedLocator = commonLocator + fileAsLocator;
				String fileAs = sGetText(contactDisplayedLocator);
				logger.info(" found " + fileAs);

				// check contact type
				if (contactType.equals(ContactGroupItem.IMAGE_CLASS)) {
					ci = new ContactGroupItem(fileAs);
				} else if (contactType.equals(ContactItem.IMAGE_CLASS)
						|| contactType.equals(ContactItem.GAL_IMAGE_CLASS)) {
					ci = new ContactItem(fileAs);
				} else {
					throw new HarnessException("Image not neither conntact group nor contact.");
				}

				list.add(ci);
			}
		}

		return list;
	}

	public List<ContactItem> zListGetContacts() throws HarnessException {

		List<ContactItem> list = new ArrayList<ContactItem>();

		// ensure it is in Addressbook main page
		zNavigateTo();
		if (!this.sIsElementPresent("id=zv__CNS-main"))
			// maybe return empty list?????
			throw new HarnessException("Contact List is not present " + "id='zv__CNS-main'");

		// Get the number of contacts (String)
		int count = this.sGetCssCount("css=div[id='zv__CNS-main'] li[id^=zli__CNS-main__]");

		logger.info(myPageName() + " zListGetContacts: number of contacts: " + count);

		// Get each contact's data from the table list
		for (int i = 1; i <= count; i++) {
			String commonLocator = "css=div[id='zv__CNS-main'] li:nth-child(" + i + ")";

			String contactType = getContactType(commonLocator);

			ContactItem ci = null;
			String contactDisplayedLocator = commonLocator + " div[id^=zlif__CNS-main__][id$=__fileas]";
			String fileAs = sGetText(contactDisplayedLocator);
			logger.info(" found " + fileAs);

			// check if it is a contact. contactgroup, gal, or dlist item
			if (contactType.equals(ContactGroupItem.IMAGE_CLASS)) {
				ci = new ContactGroupItem(fileAs);
			} else if (contactType.equals(ContactItem.IMAGE_CLASS)) {
				ci = new ContactItem(fileAs);
			} else if (contactType.equals(GALItem.IMAGE_CLASS)) {
				ci = new GALItem(fileAs);
			} else if (contactType.equals(DistributionListItem.IMAGE_CLASS)) {
				ci = new DistributionListItem(fileAs, fileAs);
			} else {
				throw new HarnessException("Image type not valid.");
			}

			list.add(ci);
		}

		return list;
	}

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		tracer.trace("Press the " + button + " button");

		if (button == null)
			throw new HarnessException("Button cannot be null");

		String locator = null;
		AbsPage page = null;

		if (button == Button.B_REFRESH) {
			return (((AjaxPages) this.MyApplication).zPageMain.zToolbarPressButton(Button.B_REFRESH));

		} else if (button == Button.B_NEW) {

			SleepUtil.sleepSmall();
			locator = "css=div#zb__NEW_MENU td[id$='_title']";
			page = new FormContactNew(this.MyApplication);

		} else if (button == Button.B_DELETE) {

			String id = "zb__CNS-main__DELETE";
			if (this.zIsElementDisabled("css=div#" + id)) {
				throw new HarnessException("Tried clicking on " + button + " but it was disabled " + id);
			}

			locator = "id=" + id;

		} else if (button == Button.B_EDIT) {
			String id = "zb__CNS-main__EDIT";
			if (zIsElementDisabled("css=div#" + id)) {
				throw new HarnessException("Tried clicking on " + button + " but it was disabled " + id);
			}

			locator = "id=" + id;
			page = newFormSelected();

		} else if (button == Button.B_MOVE) {
			String id = "zb__CNS__MOVE_left_icon";
			if (sIsElementPresent("css=td#" + id + " div[class*=ZDisabledImage]")) {
				throw new HarnessException("Tried clicking on " + button + " but it was disabled " + id);
			}

			locator = "id=" + id;
			page = new DialogMove(MyApplication, this);

		} else if (button == Button.B_FORWARD) {
			locator = "css=div[id^=zb__CN-][id$=__SEND_CONTACTS_IN_EMAIL]";
			if (zIsElementDisabled(locator)) {
				throw new HarnessException("Tried clicking on " + button + " but it was disabled ");
			}
			page = new FormMailNew(MyApplication);

		} else if (button == Button.B_CANCEL) {
			locator = "css=div[id^=zb__CN][id$=__CANCEL]";
			if (zIsElementDisabled(locator)) {
				throw new HarnessException("Tried clicking on " + locator + " but it was disabled ");
			}

			page = new DialogWarning(DialogWarning.DialogWarningID.CancelCreateContact, this.MyApplication,
					((AjaxPages) this.MyApplication).zPageContacts);

		} else if (button == Button.B_CLOSE) {
			locator = "css=div[id^=zb__CN][id$=__CANCEL]";
			if (zIsElementDisabled(locator)) {
				throw new HarnessException("Tried clicking on " + locator + " but it was disabled ");
			}

		} else if (button == Button.B_VIEW_CERTIFICATE) {

			locator = "css=td[class='ZmSecureMailCertificateRow'] div[class='FakeAnchor'] td[id$='title']:contains('View certificate')";

			if (!this.sIsElementPresent(locator))
				throw new HarnessException("locator is not present for button " + button + " : " + locator);

			this.sClick(locator); // sClick() is required for this element

			this.zWaitForBusyOverlay();

			return (page);

		} else if (button == Button.B_DISTRIBUTIONLIST_PROPERTIES) {
			locator = "css=td[id$='_title']:contains('Distribution List Properties')";
			page = null;

		} else if (isAlphabetButton(button)) {
			locator = DisplayContactGroup.ALPHABET_PREFIX + button.toString() + DisplayContactGroup.ALPHABET_POSTFIX;
		}

		if (locator == null)
			throw new HarnessException("locator is null for button " + button);

		if (!sIsElementPresent(locator))
			throw new HarnessException("Button is not present locator=" + locator + " button=" + button);

		sClickAt(locator, "0,0");
		SleepUtil.sleepSmall();
		zWaitForBusyOverlay();

		if (page != null) {
			page.zWaitForActive();
		}
		return (page);
	}

	public AbsPage zKeyboardShortcut(Shortcut shortcut) throws HarnessException {
		logger.info(myPageName() + " zKeyboardShortcut(" + shortcut.getKeys() + ")");

		tracer.trace("Click the shortcut " + shortcut.getKeys());

		AbsPage page = null;

		if (shortcut == Shortcut.S_NEWTAG) {
			page = new DialogTag(MyApplication, ((AjaxPages) MyApplication).zPageContacts);
		} else if (shortcut == Shortcut.S_MOVE) {
			page = new DialogMove(MyApplication, this);
			zKeyDown("77");
			zWaitForBusyOverlay();
			page.zWaitForActive();
			return (page);
		} else if (shortcut == Shortcut.S_ASSISTANT) {
			page = new DialogAssistant(MyApplication, ((AjaxPages) MyApplication).zPageContacts);
		} else if (shortcut == Shortcut.S_MAIL_REMOVETAG) {
			page = null;
		} else {
			throw new HarnessException("No logic for shortcut : " + shortcut);
		}

		// zKeyboardTypeString(shortcut.getKeys());
		zKeyboard.zTypeCharacters(shortcut.getKeys());

		zWaitForBusyOverlay();

		if (page != null) {
			page.zWaitForActive();
		}
		return (page);
	}

	public void clickDistributionListsFolder(AjaxPages app) throws HarnessException {
		FolderItem contactFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), "Distribution Lists");
		app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, contactFolder);
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown(" + pulldown + ", " + option + ")");

		tracer.trace("Click pulldown " + pulldown + " then " + option);

		if (pulldown == null)
			throw new HarnessException("Button cannot be null!");

		String pulldownLocator = null;
		String optionLocator = null;
		AbsPage page = null;

		if (pulldown == Button.B_TAG) {

			if (option == Button.O_TAG_NEWTAG) {

				pulldownLocator = "css=td#zb__CNS-main__TAG_MENU_dropdown div.ImgSelectPullDownArrow";
				optionLocator = "css=td#contacts_newtag_title";

				page = new DialogTag(this.MyApplication, this);

			} else if (option == Button.O_TAG_REMOVETAG) {

				pulldownLocator = "css=td#zb__CNS-main__TAG_MENU_dropdown div.ImgSelectPullDownArrow";
				optionLocator = "css=div[id='zb__CNS-main__TAG_MENU|MENU'] div[id^=contacts_removetag] td.ZWidgetTitle";
				page = null;

			}

		} else if (pulldown == Button.B_NEW) {

			pulldownLocator = "css=div#zb__NEW_MENU td#zb__NEW_MENU_dropdown";
			if (option == Button.O_NEW_CONTACT) {
				optionLocator = "css=div#zb__NEW_MENU_NEW_CONTACT";
				page = new FormContactNew(this.MyApplication);

			} else if (option == Button.O_NEW_CONTACTGROUP) {
				optionLocator = "css=div#zb__NEW_MENU_NEW_GROUP";
				page = new FormContactGroupNew(this.MyApplication);

			} else if (option == Button.O_NEW_TAG) {
				optionLocator = "css=div#zb__NEW_MENU_NEW_TAG td#zb__NEW_MENU_NEW_TAG_title";
				page = new DialogTag(this.MyApplication, this);

			} else if (option == Button.O_NEW_CONTACTS_FOLDER) {
				optionLocator = "css=div#zb__NEW_MENU_NEW_ADDRBOOK td#zb__NEW_MENU_NEW_ADDRBOOK_title";
				page = new DialogCreateFolder(MyApplication, ((AjaxPages) MyApplication).zPageContacts);

			} else {
				pulldownLocator = null;
			}

		}

		if (pulldownLocator != null) {

			if (!sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " option " + option + " pulldownLocator "
						+ pulldownLocator + " not present!");
			}

			sClickAt(pulldownLocator, "10,20");
			SleepUtil.sleepSmall();
			zWaitForBusyOverlay();

			if (optionLocator != null) {

				zWaitForElementPresent(optionLocator);

				if (!zIsElementDisabled(optionLocator)) {
					sClick(optionLocator);
					SleepUtil.sleepSmall();
					zWaitForBusyOverlay();
				}

			}

			if (page != null) {
				page.zWaitForActive();
			}
		}

		SleepUtil.sleepMedium();

		return page;
	}

	public AbsPage zToolbarPressPulldown(Button pulldown, IItem item) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown(" + pulldown + ", " + item + ")");

		tracer.trace("Click pulldown " + pulldown + " then " + item);

		if (pulldown == null)
			throw new HarnessException("Button cannot be null!");

		String pulldownLocator = null;
		String optionLocator = null;
		AbsPage page = null;

		SleepUtil.sleepSmall();

		if (pulldown == Button.B_MOVE) {
			if (item instanceof FolderItem) {
				FolderItem folder = (FolderItem) item;
				pulldownLocator = "css=td#zb__CNS-main__MOVE_MENU_dropdown.ZDropDown";
				optionLocator = "css=td#zti__ZmFolderChooser_ContactsCNS-main__" + folder.getId()
						+ "_textCell.DwtTreeItem-Text";
			}

		} else if (pulldown == Button.B_TAG) {
			if (item instanceof TagItem) {
				pulldownLocator = "css=td#zb__CNS-main__TAG_MENU_dropdown div.ImgSelectPullDownArrow";
				page = null;
			}
		}

		if (pulldownLocator != null) {
			if (!sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " folder " + item + " pulldownLocator "
						+ pulldownLocator + " not present!");
			}

			sClickAt(pulldownLocator, "");
			zWaitForBusyOverlay();
			SleepUtil.sleepSmall();

			// find optionLocator
			if (pulldown == Button.B_TAG) {
				String tagName = ((TagItem) item).getName();
				optionLocator = "css=div[id='zb__CNS-main__TAG_MENU|MENU'] td[id$='_title']:contains('" + tagName
						+ "')";
			}

			if (optionLocator != null) {
				zWaitForElementPresent(optionLocator);

				if (zIsVisiblePerPosition(optionLocator, 0, 0)) {
					sClickAt(optionLocator, "");
					zWaitForBusyOverlay();
				}
			}
		}

		SleepUtil.sleepSmall();

		return page;
	}

	public AbsPage zToolbarPressPulldown(Button pulldown, Button option, Object item) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown(" + pulldown + ", " + option + " , " + item + ")");

		tracer.trace("Click pulldown " + pulldown + " then " + option + " and " + item);

		if (pulldown == null)
			throw new HarnessException("Button cannot be null!");

		String pulldownLocator = null;
		String optionLocator = null;
		String subOptionLocator = null;

		AbsPage page = null;

		if (pulldown == Button.B_TAG) {
			pulldownLocator = "css=td#zb__CNS-main__TAG_MENU_dropdown div.ImgSelectPullDownArrow";

			if (option == Button.O_TAG_REMOVETAG) {
				optionLocator = "css=div[id='zb__CNS-main__TAG_MENU|MENU'] div[id^='contacts_removetag'] td[id^='contacts_removetag'][id$=_title]";
			}
			page = null;
		}

		if (pulldownLocator != null) {

			if (!sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " folder " + item + " pulldownLocator "
						+ pulldownLocator + " not present!");
			}

			sClickAt(pulldownLocator, "");
			SleepUtil.sleepSmall();
			zWaitForBusyOverlay();

			if (optionLocator != null) {
				zWaitForElementPresent(optionLocator);

				if (zIsVisiblePerPosition(optionLocator, 0, 0)) {
					sMouseOver(optionLocator);
					SleepUtil.sleepSmall();
					zWaitForBusyOverlay();

					if (item instanceof TagItem) {

						if (item == TagItem.Remove_All_Tags) {
							subOptionLocator = "css=div[id^='REMOVE_TAG_MENU'] div[id='REMOVE_ALL_TAGS']";

						} else {
							subOptionLocator = "//div[contains(@id,'REMOVE_TAG_MENU')]//td[contains(@id,'Remove_tag') and contains(text(),'"
									+ ((TagItem) item).getName() + "')]";
						}
					}

					if (subOptionLocator != null) {
						sClickAt(subOptionLocator, "");
						SleepUtil.sleepSmall();
						zWaitForBusyOverlay();
					}
				}
			}

		}
		return page;

	}

	// return the type of a contact
	private String getContactType(String locator) throws HarnessException {
		String imageLocator = locator + " div[class*=";

		if (sIsElementPresent(imageLocator + ContactGroupItem.IMAGE_CLASS + "]")) {
			return ContactGroupItem.IMAGE_CLASS;
		} else if (sIsElementPresent(imageLocator + ContactItem.IMAGE_CLASS + "]")) {
			return ContactItem.IMAGE_CLASS;
		} else if (sIsElementPresent(imageLocator + DistributionListItem.IMAGE_CLASS + "]")) {
			return DistributionListItem.IMAGE_CLASS;
		}
		logger.info(sGetAttribute(locator + " div@class") + " not contain neither " + ContactGroupItem.IMAGE_CLASS
				+ " nor " + ContactItem.IMAGE_CLASS);
		return null;
	}

	// return the xpath locator of a contact
	private String getContactLocator(String contact) throws HarnessException {
		// assume that this is a list view
		String listLocator = "div[id='zv__CNS-main']";
		String rowLocator = "li[id^='zli__CNS-main__']";

		String contactLocator = null;

		// actually this is a search view
		if (zIsInSearchView()) {
			listLocator = "div[id=zv__CNS-SR-1]";
			rowLocator = "li[id^=zli__CNS-SR-1__]";
		}

		if (!this.sIsElementPresent("css=" + listLocator + " " + rowLocator)) {
			throw new HarnessException("css=" + listLocator + " " + rowLocator + " not present");
		}

		// Get the number of contacts (String)
		int count = this.sGetCssCount("css=" + listLocator + " " + rowLocator);
		logger.debug(myPageName() + " zListItem: number of contacts: " + count);

		if (count == 0)
			throw new HarnessException("List count was zero");

		// Get each contact's data from the table list
		for (int i = 1; i <= count; i++) {

			String itemLocator = "css=" + listLocator + " li:nth-child(" + i + ")";
			if (!this.sIsElementPresent(itemLocator)) {
				throw new HarnessException("unable to locate item " + itemLocator);
			}

			String displayAs = sGetText(itemLocator);

			// Log this item to the debug output
			LogManager.getLogger("projects").info("zListItem: found contact " + displayAs);
			if (displayAs != null) {
				if (displayAs.toLowerCase().contains(contact.toLowerCase())) {
					// Found the item!
					contactLocator = itemLocator;
					break;
				}
			}

		}

		if (contactLocator == null) {
			throw new HarnessException("Never found the contact " + contact);
		}

		return contactLocator;
	}

	// get selected contacts locators
	private ArrayList<String> getSelectedContactLocator() throws HarnessException {
		String listLocator = "div#zv__CNS-main";
		String rowLocator = "li[id^='zli__CNS-main__']";

		ArrayList<String> arrayList = new ArrayList<String>();

		if (!sIsElementPresent("css=" + listLocator))
			throw new HarnessException("List View Rows is not present " + listLocator);

		if (!sIsElementPresent("css=" + rowLocator))
			return arrayList; // an empty arraylist

		// Get the number of contacts (String)
		int count = sGetCssCount("css=" + listLocator + " " + rowLocator);

		logger.debug(myPageName() + " getSelectedContactLocator: number of contacts: " + count);

		if (count == 0)
			throw new HarnessException("List count was zero");

		// Get each contact's data from the table list
		for (int i = 1; i <= count; i++) {
			String itemLocator = "css=" + listLocator + " li:nth-child(" + i + ")";

			if (!sIsElementPresent(itemLocator)) {
				logger.info("reach the end of list - unable to locate item " + itemLocator);
				break;
			}

			if (sIsElementPresent(itemLocator + "[class*=Row-selected]")) {
				arrayList.add(itemLocator);
			}

			// Log this item to the debug output
			LogManager.getLogger("projects").info("getSelectedContactLocator: found selected contact " + itemLocator);

		}

		return arrayList;
	}

	public AbsPage zListItem(Action action, Button option, Button subOption, String contact) throws HarnessException {
		String locator = null;
		AbsPage page = null;
		String parentLocator = null;
		String extraLocator = "";

		tracer.trace(action + " then " + option + " then " + subOption + " on contact = " + contact);

		if (action == Action.A_RIGHTCLICK) {
			ContextMenuItem cmi = null;
			ContextMenuItem sub_cmi = null;

			sRightClickAt(getContactLocator(contact), "0,0");

			if (option == Button.B_TAG) {

				cmi = CONTEXT_MENU.CONTACT_TAG;

				if (subOption == Button.O_TAG_NEWTAG) {
					sub_cmi = CONTEXT_SUB_MENU.CONTACT_SUB_NEW_TAG;
					page = new DialogTag(this.MyApplication, this);
				}

				else if (subOption == Button.O_TAG_REMOVETAG) {
					sub_cmi = CONTEXT_SUB_MENU.CONTACT_SUB_REMOVE_TAG;
					parentLocator = "div[id^='zmi__Contacts__TAG_MENU']";
					page = null;
				}

			} else if (option == Button.B_CONTACTGROUP) {
				if (subOption == Button.O_NEW_CONTACTGROUP) {
					cmi = CONTEXT_MENU.CONTACT_GROUP;
					sub_cmi = CONTEXT_SUB_MENU.CONTACT_SUB_NEW_CONTACT_GROUP;
					page = new DialogNewContactGroup((AjaxPages) MyApplication, this);
				}

			} else if (option == Button.B_SEARCH) {
				cmi = CONTEXT_MENU.CONTACT_SEARCH;
				if (subOption == Button.O_SEARCH_MAIL_SENT_TO_CONTACT) {
					sub_cmi = CONTEXT_SUB_MENU.CONTACT_SUB_SENT_TO_CONTACT;
					page = ((AjaxPages) MyApplication).zPageSearch;
				} else if (subOption == Button.O_SEARCH_MAIL_RECEIVED_FROM_CONTACT) {
					sub_cmi = CONTEXT_SUB_MENU.CONTACT_SUB_RECEIVED_FROM_CONTACT;
					page = ((AjaxPages) MyApplication).zPageSearch;
				}

			} else {
				throw new HarnessException("option " + option + " not supported.");
			}

			if ((cmi == null) || (sub_cmi == null)) {
				throw new HarnessException("option " + option + " not supported.");
			}

			if (zIsInSearchView()) {
				locator = "css=div[id^=zm__Contacts]";
			} else {
				locator = "css=div#zm__Contacts";
			}

			if (((option == Button.B_CONTACTGROUP) && (subOption == Button.O_NEW_CONTACTGROUP))
					|| (option == Button.B_SEARCH)) {
				locator = locator + " tr[id^=" + cmi.locator + "]";
			} else {
				locator = locator + " tr#" + cmi.locator;
			}

			sFocus(locator);
			sMouseOver(locator);
			zWaitForBusyOverlay();

			if (option == Button.B_SEARCH) {

				try {

					int total = Integer
							.parseInt(sGetEval("window.document.getElementById('z_shell').childNodes.length")) - 1;

					for (int i = total; i >= 0; i--, parentLocator = null) {
						parentLocator = sGetEval("window.document.getElementById('z_shell').childNodes[" + i + "].id");
						if (parentLocator.startsWith("POPUP_DWT") && zIsVisiblePerPosition(parentLocator, 0, 0)) {
							logger.info("parent = " + parentLocator);
							parentLocator = "div#" + parentLocator;
							break;
						}
					}

				} catch (Exception e) {
					parentLocator = null;
					logger.info("cannot find parent id for " + sub_cmi.locator + " " + e.getMessage());
				}

			}

			if (parentLocator != null) {
				locator = "css=" + parentLocator + " " + sub_cmi.locator + extraLocator;
			} else {
				locator = "css=" + sub_cmi.locator + extraLocator;
			}

		}

		if (option == Button.B_SEARCH) {
			if (subOption == Button.O_SEARCH_MAIL_SENT_TO_CONTACT) {
				locator = "css=td[id^=SEARCH_TO__DWT][id$=_title]:contains('Sent To Contact')";
			} else if (subOption == Button.O_SEARCH_MAIL_RECEIVED_FROM_CONTACT) {
				locator = "css=td[id^=SEARCH__DWT][id$=_title]:contains('Received From Contact')";
			}
		}

		sFocus(locator);
		sMouseOver(locator);
		sClickAt(locator, "");
		zWaitForBusyOverlay();
		SleepUtil.sleepMedium();

		return (page);
	}

	public AbsPage zListItem(Action action, Button option, Button subOption, Object choice, String contact)
			throws HarnessException {

		AbsPage page = null;
		String contactLocator = getContactLocator(contact);
		String locator = null;

		tracer.trace(action + " then " + option + " then " + subOption + " and choose " + choice + " on contact = "
				+ contact);

		if (action == Action.A_RIGHTCLICK) {

			if (option == Button.B_TAG) {

				if (subOption == Button.O_TAG_REMOVETAG) {

					if (!(choice instanceof String)) {
						throw new HarnessException("choice must be a string of the tag name! " + choice);
					}

					String tagName = (String) choice;
					String tagContactLocator = "css=div[id^='zm__Contacts'] div[id^='zmi__Contacts__TAG_MENU'] td[id$='_title']";
					String removeTagLocator = "css=div[id^='zmi__Contacts__TAG_MENU'] div[id^='contacts_removetag'].ZHasDropDown";
					locator = "//div[contains(@id,'REMOVE_TAG_MENU')]//td[contains(@id,'Remove_tag') and contains(text(),'"
							+ tagName + "')]";

					// Right click on contact
					sRightClickAt(contactLocator, "0,0");
					SleepUtil.sleepSmall();
					zWaitForBusyOverlay();

					// Left Click "Tag"
					this.sMouseOver(tagContactLocator);
					SleepUtil.sleepSmall();
					zWaitForBusyOverlay();

					if (this.sIsElementPresent(
							"css=div[id^='zmi__Contacts__TAG_MENU'] div[id^='contacts_removetag'].ZHasDropDown")) {
						this.sMouseOver(removeTagLocator);
						SleepUtil.sleepSmall();
						zWaitForBusyOverlay();

						if (locator.contains("All Tags")) {
							sClickAt("css=div[id^='REMOVE_TAG_MENU'] div[id='REMOVE_ALL_TAGS']", "");
						} else {
							sClickAt(locator, "");
						}
						SleepUtil.sleepSmall();
						zWaitForBusyOverlay();

					} else {
						sClickAt(removeTagLocator, "");
						SleepUtil.sleepSmall();
						zWaitForBusyOverlay();
					}

					SleepUtil.sleepMedium();
					return (page);
				}
			}
		}

		SleepUtil.sleepMedium();
		return (page);
	}

	public AbsPage zListItem(Action action, Button option, IItem item, String contact) throws HarnessException {

		AbsPage page = null;
		String contactLocator = getContactLocator(contact);
		String optionLocator = null;
		String itemLocator = null;

		tracer.trace(action + " then " + option + " then " + item + " on contact = " + contact);

		if (action == Action.A_RIGHTCLICK) {

			if (option == Button.B_TAG) {

				// Hover over the context menu "tags" item
				optionLocator = "css=div#zm__Contacts div#zmi__Contacts__TAG_MENU td[id$='_title']";

				if (item instanceof TagItem) {

					// Left click the existing tag
					itemLocator = "css=div[id^='zmi__Contacts__TAG_MENU|MENU'] td[id$='_title']:contains('"
							+ item.getName() + "')";

				}

			} else if (option == Button.B_CONTACTGROUP) {

				optionLocator = "css=div#zm__Contacts div[id^='zmi__Contacts__CONTACTGROUP_MENU'] td[id$='_title']";

				if (item instanceof ContactGroupItem) {
					itemLocator = "css=div[id^='zmi__Contacts__CONTACTGROUP_MENU'] td[id$='_title']:contains('"
							+ item.getName() + "')";
				}

			}

			if (!this.sIsElementPresent(contactLocator)) {
				throw new HarnessException("Unable to right click on contact");
			}

			// Right click on contact
			sRightClickAt(contactLocator, "0,0");
			SleepUtil.sleepSmall();
			this.zWaitForBusyOverlay();

			if (!this.sIsElementPresent(optionLocator)) {
				throw new HarnessException("Unable to hover over context menu");
			}

			// Mouse over the option
			sMouseOver(optionLocator);
			this.zWaitForBusyOverlay();

			// It seems to take a while to draw the context menu
			// Sleep a bit to let it draw.
			SleepUtil.sleepLong();

			if (!this.sIsElementPresent(itemLocator)) {
				throw new HarnessException("Unable to click on sub-menu");
			}

			// Left click the sub-option
			this.sClickAt(itemLocator, "");
			SleepUtil.sleepSmall();
			this.zWaitForBusyOverlay();

		}

		return (page);

	}

	@Override
	public AbsPage zListItem(Action action, Button option, String contact) throws HarnessException {
		String locator = null;
		AbsPage page = null;

		String contactLocator = getContactLocator(contact);

		tracer.trace(action + " then " + option + " on contact = " + contact);

		if (action == Action.A_RIGHTCLICK) {
			ContextMenuItem cmi = null;

			if (option == Button.B_DELETE) {
				cmi = CONTEXT_MENU.CONTACT_DELETE;

			} else if (option == Button.O_DELETE_MENU) {
				cmi = CONTEXT_MENU.DL_DELETE_MENU;
				page = new DialogWarning(DialogWarning.DialogWarningID.EmptyFolderWarningMessage, this.MyApplication,
						((AjaxPages) this.MyApplication).zPageContacts);

			} else if (option == Button.O_NEW_EMAIL) {
				cmi = CONTEXT_MENU.DL_NEW_EMAIL_MENU;
				page = new FormMailNew(MyApplication);

			} else if (option == Button.O_EDIT_DISTRIBUTION_LIST) {
				cmi = CONTEXT_MENU.DL_EDIT_DISTRIBUTION_LIST_MENU;
				page = new FormContactDistributionListNew(MyApplication);

			} else if (option == Button.B_MOVE) {
				cmi = CONTEXT_MENU.CONTACT_MOVE;
				page = new DialogMove(MyApplication, this);

			} else if (option == Button.B_EDIT) {
				cmi = CONTEXT_MENU.CONTACT_EDIT;
				page = newFormSelected();
				sClick(contactLocator);
				SleepUtil.sleepSmall();

			} else if (option == Button.B_NEW) {
				cmi = CONTEXT_MENU.CONTACT_NEW_EMAIL;
				page = new FormMailNew(MyApplication);

			} else if (option == Button.B_PRINT) {
				cmi = CONTEXT_MENU.CONTACT_PRINT;
				page = new PagePrint(MyApplication);

			} else if (option == Button.B_FORWARD) {
				cmi = CONTEXT_MENU.CONTACT_FORWARD;
				page = new FormMailNew(MyApplication);

			} else {
				throw new HarnessException("option " + option + " not supported");
			}

			sRightClickAt(contactLocator, "0,0");
			SleepUtil.sleepSmall();

			locator = "css=div#zm__Contacts tr#" + cmi.locator;
			if (option == Button.B_NEW) {
				locator = "css=div#zm__Contacts tr[id^=" + cmi.locator + "]";
			} else if (option == Button.B_FORWARD) {
				locator = "css=div#zm__Contacts tr[id^=" + cmi.locator + "]";
			}

			zWaitForElementPresent(locator);

			if (sIsElementPresent(locator + "[class*=ZDisabled]")) {
				throw new HarnessException("Tried clicking on " + cmi.text + " but it was disabled ");
			}

		} else if (action == Action.A_LEFTCLICK) {

			if (option == Button.B_EDIT) {
				locator = "css=td[id='zb__CNS-main__EDIT_title']";
				sClick(locator);
				zWaitForBusyOverlay();
				SleepUtil.sleepMedium();
				return null;
			}
		}

		sClick(locator);
		zWaitForBusyOverlay();
		SleepUtil.sleepMedium();

		if (page != null) {
			page.zWaitForActive();
		}

		return (page);
	}

	@Override
	public AbsPage zListItem(Action action, String contact) throws HarnessException {
		logger.info(myPageName() + " zListItem(" + action + ", " + contact + ")");
		String contactLocator = getContactLocator(contact);
		AbsPage page = null;
		tracer.trace(action + " on contact = " + contact);

		if (action == Action.A_LEFTCLICK) {
			sClickAt(contactLocator, "");
			SleepUtil.sleepSmall();
			zWaitForBusyOverlay();

			ArrayList<String> selectedContactArrayList = getSelectedContactLocator();
			String contactType = getContactType(selectedContactArrayList.get(0));

			// check if it is a contact or a contact group item or DL
			if (contactType.equals(ContactGroupItem.IMAGE_CLASS)) {
				page = new DisplayContactGroup(MyApplication);
			} else if (contactType.equals(DistributionListItem.IMAGE_CLASS)) {
				page = new DisplayDistributionList(MyApplication);
			} else if (contactType.equals(ContactItem.IMAGE_CLASS)) {
				page = new DisplayContact(MyApplication);
			} else {
				throw new HarnessException(" Error: not support the contact type");
			}

		} else if (action == Action.A_CHECKBOX) {

			contactLocator = contactLocator + " div[id$=__se]>div.ImgCheckboxUnchecked";
			sClickAt(contactLocator, "0,0");
			SleepUtil.sleepSmall();

			ArrayList<String> selectedContactArrayList = getSelectedContactLocator();
			String contactType = getContactType(selectedContactArrayList.get(0));

			// check if it is a contact or a contact group item
			if (contactType.equals(ContactGroupItem.IMAGE_CLASS)) {
				page = new DisplayContactGroup(MyApplication);
			} else if (contactType.equals(DistributionListItem.IMAGE_CLASS)) {
				page = new DisplayDistributionList(MyApplication);
			} else if (contactType.equals(ContactItem.IMAGE_CLASS)) {
				page = new DisplayContact(MyApplication);
			} else {
				throw new HarnessException(" Error: not support the contact type");
			}

		} else if (action == Action.A_RIGHTCLICK) {

			sRightClickAt(contactLocator, "0,0");
			SleepUtil.sleepSmall();
			zWaitForBusyOverlay();
			return (new ContextMenu(MyApplication));

		} else if (action == Action.A_DOUBLECLICK) {
			sDoubleClick(contactLocator);
			SleepUtil.sleepMedium();
			page = newFormSelected();

		} else {
			throw new HarnessException("Action " + action + " not supported");
		}

		if (page != null) {
			page.zWaitForActive();
		}
		return page;
	}

	// To verify the presence of contact using last name
	public boolean zVerifyContactExists(String lastname) throws HarnessException {

		boolean found = false;

		for (int i = 1; i <= 5; i++) {

			zToolbarPressButton(Button.B_REFRESH);

			List<ContactItem> items = zListGetContacts();

			for (ContactItem item : items) {
				if (item.fileAs.contains(lastname)) {
					found = true;
					break;
				} else {
					logger.info("Contact is not displayed in current view");
				}
			}

			if (found == true) {
				logger.info("Conatct displayed in current view");
				ZAssert.assertTrue(found, "Contact is not displayed in current view");
				break;
			}
		}

		return found;
	}

	public boolean zVerifyDLMemberExists(String dlEmailAddress) throws HarnessException {
		return sIsVisible("css=div[class='contactGroupList'] td[class='contactGroupTableContent'] div:contains("
				+ dlEmailAddress + ")");
	}

	private AbsPage newFormSelected() throws HarnessException {
		AbsPage page = null;
		ArrayList<String> selectedContactArrayList = getSelectedContactLocator();

		if (selectedContactArrayList.size() == 0) {
			throw new HarnessException("No selected contact/contact group ");
		}

		String contactType = getContactType(selectedContactArrayList.get(0));

		// check if it is a contact or a contact group item
		if (contactType.equals(ContactGroupItem.IMAGE_CLASS)) {
			page = new FormContactGroupNew(MyApplication);
		} else if (contactType.equals(ContactItem.IMAGE_CLASS)) {
			page = new FormContactNew(MyApplication);
		}

		return page;

	}

	private boolean isAlphabetButton(Button button) {
		return (button == Button.B_AB_ALL) || (button == Button.B_AB_123) || (button == Button.B_AB_A)
				|| (button == Button.B_AB_B) || (button == Button.B_AB_C) || (button == Button.B_AB_D)
				|| (button == Button.B_AB_E) || (button == Button.B_AB_F) || (button == Button.B_AB_G)
				|| (button == Button.B_AB_H) || (button == Button.B_AB_I) || (button == Button.B_AB_J)
				|| (button == Button.B_AB_K) || (button == Button.B_AB_L) || (button == Button.B_AB_M)
				|| (button == Button.B_AB_N) || (button == Button.B_AB_O) || (button == Button.B_AB_P)
				|| (button == Button.B_AB_Q) || (button == Button.B_AB_R) || (button == Button.B_AB_S)
				|| (button == Button.B_AB_T) || (button == Button.B_AB_U) || (button == Button.B_AB_V)
				|| (button == Button.B_AB_W) || (button == Button.B_AB_X) || (button == Button.B_AB_Y)
				|| (button == Button.B_AB_Z);
	}

	public boolean zVerifyCertificatePresent(String emailAddress) throws HarnessException {

		String locator = "css=td[class='ZmSecureMailCertificateRow'] td[id$='title']:contains('" + emailAddress + "')";

		if (this.sIsElementPresent(locator)) {
			// Mail Security header found
			return true;
		}

		return false;
	}

	private boolean zIsInSearchView() throws HarnessException {
		return zIsVisiblePerPosition("css=div#z_filterPanel__SR-1", 0, 0);
	}

	public boolean zVerifyDisabledControl(Button buttonName) throws HarnessException {

		if (buttonName.equals(Button.O_NEW_CONTACTS_FOLDER)) {
			return sIsElementPresent("css=div[id='ZmActionMenu_contacts_ADDRBOOK'] div[id='NEW_ADDRBOOK'].ZDisabled");
		} else if (buttonName.equals(Button.O_SHARE_CONTACTS_FOLDER)) {
			return sIsElementPresent("css=div[id='ZmActionMenu_contacts_ADDRBOOK'] div[id='SHARE_ADDRBOOK'].ZDisabled");
		} else if (buttonName.equals(Button.O_DELETE)) {
			return sIsElementPresent(
					"css=div[id='ZmActionMenu_contacts_ADDRBOOK'] div[id='DELETE_WITHOUT_SHORTCUT'].ZDisabled");
		} else if (buttonName.equals(Button.O_RENAME_FOLDER)) {
			return sIsElementPresent("css=div[id='ZmActionMenu_contacts_ADDRBOOK'] div[id='RENAME_FOLDER'].ZDisabled");
		} else if (buttonName.equals(Button.O_EDIT_PROPERTIES)) {
			return sIsElementPresent("css=div[id='ZmActionMenu_contacts_ADDRBOOK'] div[id='EDIT_PROPS'].ZDisabled");
		} else if (buttonName.equals(Button.O_EXPAND_ALL)) {
			return sIsElementPresent("css=div[id='ZmActionMenu_contacts_ADDRBOOK'] div[id='EXPAND_ALL'].ZDisabled");

		} else {
			return false;
		}
	}
}
