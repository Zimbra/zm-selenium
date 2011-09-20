/**
 * 
 */
package com.zimbra.qa.selenium.projects.octopus.ui;

import java.util.ArrayList;
import java.util.List;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.octopus.ui.DialogError;
import com.zimbra.qa.selenium.projects.octopus.ui.DialogError.DialogErrorID;

public class PageMyFiles extends AbsTab {

	public static class Locators {
		public static final Locators zTabMyFiles = new Locators(
		"css=div.octopus-tab-label:contains(My Files)");
		public static final Locators zTabMyFilesSelected = new Locators(
				"css=div[class^=octopus-tab sc-collection-item sel]>div.octopus-tab-label:contains(My Files)");
		public static final Locators zMyFilesView = new Locators(
		"css=div[id=octopus-myfiles-view]");
		public static final Locators zMyFilesArrowButton = new Locators(
				"css=span[class*=my-files-list-item-action-button myfiles-button button]");
		public static final Locators zNewFolderOption = new Locators(
				"css=div[class^=octopus-template-context-menu-item action-new-folder]:contains(New Folder)");
		public static final Locators zMyFilesListView = new Locators(
				"css=div[class*=my-files-list-view]");
		public static final Locators zMyFilesListViewItems = new Locators(
				"css=div[class*=my-files-list-view]>div.my-files-list-item");
		public static final Locators zDeleteFolderOption = new Locators(
				"css=div[class^=sc-view sc-menu-item] a[class=menu-item]>span:contains(Delete)");

		public final String locator;

		private Locators(String locator) {
			this.locator = locator;
		}
	}

	public PageMyFiles(AbsApplication application) {
		super(application);

		logger.info("new " + PageMyFiles.class.getCanonicalName());

	}

	public Toaster zGetToaster() throws HarnessException {
		return (new Toaster(this.MyApplication));
	}

	public DialogError zGetErrorDialog(DialogErrorID zimbra) {
		return (new DialogError(zimbra, this.MyApplication, this));
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		// Look for the My Files tab
		boolean selected = sIsElementPresent(Locators.zTabMyFilesSelected.locator);

		if (!selected) {
			logger.debug("zIsActive(): "
					+ selected);
			return (false);
		}

		logger.debug("isActive() = " + true);
		return (true);
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public void zNavigateTo() throws HarnessException {

		if (zIsActive()) {
			// This page is already active
			return;
		}

		// Make sure PageOctopus page is active
		if (!((AppOctopusClient) MyApplication).zPageOctopus.zIsActive()) {
			((AppOctopusClient) MyApplication).zPageOctopus.zNavigateTo();
		}
		
		String locator = Locators.zTabMyFiles.locator;

		zWaitForElementPresent(locator);

		// Click on My Files tab
		zClickAt(locator, "0,0");

		zWaitForBusyOverlay();

		zWaitForActive();
	}

	public AbsPage zToolbarPressPulldown(Button pulldown, Button option,
			IItem item) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressPulldown(" + pulldown + ", "
				+ option + ")");

		tracer.trace("Click pulldown " + pulldown + " then " + option);

		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");

		if (option == null)
			throw new HarnessException("Option cannot be null!");

		// Default behavior variables
		String itemName = null;
		String pulldownLocator = null; // If set, this will be expanded
		String optionLocator = null; // If set, this will be clicked
		AbsPage page = null; // If set, this page will be returned

		if (item != null) {
			if (!(item instanceof FolderItem)) {
				throw new HarnessException("Not supported item: "
						+ item.getClass());
			} else
				itemName = item.getName();
		}

		// Based on the button specified, take the appropriate action(s)
		if (pulldown == Button.B_MY_FILES) {
			pulldownLocator = Locators.zMyFilesArrowButton.locator;

			if (option == Button.O_NEW_FOLDER) {
				optionLocator = Locators.zNewFolderOption.locator;

				zClick(pulldownLocator);

				zWaitForBusyOverlay();

				if (optionLocator.contains("NF")) {
					for (int i = 0; i < 2; i++) {
						zKeyEvent(optionLocator, "40", "keydown");
					}
					zKeyEvent(optionLocator, "13", "keydown");
				} else
					this.sClick(optionLocator);

				// sGetCssCount("css=div[class*=my-files-list-view]>div.my-files-list-item");
				// this.zClick(Locators.zMyFilesListView.locator +
				// ">div.my-files-list-item:last-child");
				// this.zClick(Locators.zMyFilesListView.locator +
				// ">div.my-files-list-item:nth-child(1)");

				if (this.zWaitForElementPresent(
						"css=div[role=dialog]>label[class^=edit]", "3000")) {
					// this.sClick("css=div[role=dialog]>label[class^=edit]");
					zKeyEvent("css=div[role=dialog]>label[class^=edit]", "13",
							"keydown");
					// zKeyEvent("css=div[role=dialog]>label[class^=edit]",
					// "13", "keyup");
				}

				return page;

			} else {
				throw new HarnessException("no logic defined for option "
						+ pulldown + "/" + option);
			}
		} else if (pulldown == Button.B_MY_FILES_LIST_ITEM) {

			pulldownLocator = Locators.zMyFilesListViewItems.locator
					+ ":contains(" + itemName
					+ ") span[class^=my-files-list-item-action-button]";

			if (!this.zWaitForElementPresent(pulldownLocator, "2000"))
				throw new HarnessException("Button is not present locator="
						+ pulldownLocator);

			zClick(pulldownLocator);

			// If the app is busy, wait for it to become active
			zWaitForBusyOverlay();

			if (option == Button.O_DELETE) {
				optionLocator = Locators.zDeleteFolderOption.locator;

				if (!this.zWaitForElementPresent(optionLocator, "2000"))
					throw new HarnessException("Button is not present locator="
							+ optionLocator);

				this.sClickAt(optionLocator, "0,0");

				// If the app is busy, wait for it to become active
				zWaitForBusyOverlay();

				return page;
			} else {
				throw new HarnessException("no logic defined for option "
						+ pulldown + "/" + option);
			}
		} else {
			logger.info("no logic defined for pulldown" + pulldown + "/"
					+ option);
		}

		// Default behavior
		if (pulldownLocator != null) {

			// Make sure the locator exists
			if (!this.sIsElementPresent(pulldownLocator)) {
				throw new HarnessException("Button " + pulldown + " option "
						+ option + " pulldownLocator " + pulldownLocator
						+ " not present!");
			}

			zClick(pulldownLocator);

			// If the app is busy, wait for it to become active
			zWaitForBusyOverlay();
		}

		if (optionLocator != null) {

			// Make sure the locator exists
			if (!this.sIsElementPresent(optionLocator)) {
				throw new HarnessException("Button " + pulldown + " option "
						+ option + " optionLocator " + optionLocator
						+ " not present!");
			}

			this.sClick(optionLocator);

			// If the app is busy, wait for it to become active
			zWaitForBusyOverlay();
		}

		// If we click on pulldown/option and the page is specified, then
		// wait for the page to go active
		if (page != null) {
			page.zWaitForActive();
		}

		// Return the specified page, or null if not set
		return (page);
	}

	public List<String> zGetListViewItems() throws HarnessException {
		List<String> items = new ArrayList<String>();
		String locator = Locators.zMyFilesListViewItems.locator;

		int count = sGetCssCount(locator);
		String str;

		for (int i = 1; i <= count; i++) {
			str = this.sGetText(locator + ":nth-child(" + i
					+ ") span.my-files-list-item-name");

			items.add(str);
		}
		return items;
	}
	

	@Override
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		throw new HarnessException("Implement me");
	}

	@Override
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option)
			throws HarnessException {
		throw new HarnessException("Implement me");
	}

	@Override
	public AbsPage zListItem(Action action, String item)
			throws HarnessException {
		throw new HarnessException("Implement me");
	}

	@Override
	public AbsPage zListItem(Action action, Button option, String item)
			throws HarnessException {
		throw new HarnessException("Implement me");
	}

	@Override
	public AbsPage zListItem(Action action, Button option, Button subOption,
			String item) throws HarnessException {
		throw new HarnessException("Implement me");
	}
}
