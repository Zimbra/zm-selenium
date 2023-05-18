/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.pages;

import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.projects.ajax.pages.AutocompleteEntry.Icon;

public class BubbleEmailAddress extends AbsBubble {
	protected static Logger logger = LogManager.getLogger(BubbleEmailAddress.class);

	protected String myLocatorExpandIcon = null;

	public static class Locators {
	}

	public BubbleEmailAddress(AbsApplication application) {
		super(application);

		logger.info("new " + this.getClass().getCanonicalName());
	}

	public AbsBubble parseBubble(String bubbleLocator) throws HarnessException {
		logger.info("Bubble.parseBubble(" + bubbleLocator + ")");

		String locator = null;

		this.myLocator = bubbleLocator;

		// Set the text contents (i.e. display name or email address)
		locator = bubbleLocator + ">span";
		if (this.sIsElementPresent(locator)) {
			this.setMyDisplayText(this.sGetText(locator).trim());
		}

		// Determine if there is 'expand'/'+' on the bubble
		locator = bubbleLocator + " div[class='ImgBubbleExpand']";
		if (this.sIsElementPresent(locator)) {
			myLocatorExpandIcon = locator;
		}

		logger.info(this.prettyPrint());

		return (this);
	}

	public boolean zHasExpandIcon() throws HarnessException {
		return (this.myLocatorExpandIcon != null);
	}

	public AbsPage zItem(Action action) throws HarnessException {
		logger.info(myPageName() + " zItem(" + action + ")");

		tracer.trace(action + " on bubble = " + this.toString());

		if (action == null)
			throw new HarnessException("action cannot be null");

		AbsPage page = null;
		String locator = null;

		if (action == Action.A_HOVEROVER) {

			locator = this.myLocator;
			page = null;

			this.sMouseOver(locator);
			this.zWaitForBusyOverlay();

			return (page);

		} else if (action == Action.A_EXPAND) {

			if (this.myLocatorExpandIcon == null) {
				throw new HarnessException("No expand icon.  Did you call parseBubble() first?");
			}

			locator = this.myLocatorExpandIcon;
			page = null;

		} else if (action == Action.A_REMOVE) {

			throw new HarnessException("Implement action: " + action);

		} else {
			return (super.zItem(action));
		}

		if (locator == null) {
			throw new HarnessException("No locator defined for action " + action);
		}

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Locator not present " + locator);
		}

		this.sClick(locator);
		this.zWaitForBusyOverlay();

		return (page);
	}

	public AbsPage zItem(Action action, Button option) throws HarnessException {
		logger.info(myPageName() + " zItem(" + action + ", " + option + ")");

		tracer.trace(action + " then " + option + " on bubble = " + this.toString());

		if (action == null)
			throw new HarnessException("action cannot be null");
		if (option == null)
			throw new HarnessException("option cannot be null");

		AbsPage page = null;
		String locator = null;

		if (action == Action.A_RIGHTCLICK) {

			locator = this.myLocator;

			if (option == Button.B_NEW_MAIL) {

				page = null;

				throw new HarnessException("implement me");

			} else if (option == Button.B_GO_TO_URL) {

				throw new HarnessException("implement me");

			} else if (option == Button.B_ADD_TO_CONTACTS) {

				throw new HarnessException("implement me");

			} else if (option == Button.B_DELETE) {

				throw new HarnessException("implement me");

			} else if (option == Button.B_EDIT) {

				throw new HarnessException("implement me");

			} else if (option == Button.B_EXPAND) {

				throw new HarnessException("implement me");

			} else if (option == Button.B_MOVE_TO_TO) {

				throw new HarnessException("implement me");

			} else if (option == Button.B_MOVE_TO_CC) {

				throw new HarnessException("implement me");

			} else if (option == Button.B_MOVE_TO_BCC) {

				throw new HarnessException("implement me");

			}

			if (locator != null) {

				// Right click on the bubble
				this.sRightClickAt(locator, "");
				this.zWaitForBusyOverlay();

			}

		} else {
			throw new HarnessException("No logic for action " + action);
		}

		return (page);

	}

	public AbsPage zItem(Action action, Button option, Button subOption) throws HarnessException {
		logger.info(myPageName() + " zItem(" + action + ", " + option + ", " + subOption + ")");

		tracer.trace(action + " then " + option + " then " + subOption + " on bubble = " + this.toString());

		if (action == null)
			throw new HarnessException("action cannot be null");
		if (option == null)
			throw new HarnessException("option cannot be null");
		if (subOption == null)
			throw new HarnessException("subOption cannot be null");

		AbsPage page = null;
		String locator = null;
		String optionLocator = null;
		String subOptionLocator = null;

		if (action == Action.A_RIGHTCLICK) {

			locator = this.myLocator;

			if (option == Button.B_FIND_EMAILS) {

				optionLocator = "implement me";
				subOptionLocator = "implement me";
				page = null;

				if (subOption == Button.B_SENT_TO_RECIPIENT) {

					optionLocator = "implement me";
					subOptionLocator = "implement me";
					page = null;

					throw new HarnessException("implement me");

				} else if (subOption == Button.B_RECEIVED_FROM_RECIPIENT) {

					optionLocator = "implement me";
					subOptionLocator = "implement me";
					page = null;

					throw new HarnessException("implement me");

				}

			} else if (option == Button.B_ADD_TO_FILTER) {

				throw new HarnessException("implement me");

			}

			if (locator != null) {

				// Right click on the bubble
				this.sRightClickAt(locator, "");
				this.zWaitForBusyOverlay();

				if (optionLocator != null) {

					// Click on the context menu
					this.sClickAt(optionLocator, "");
					this.zWaitForBusyOverlay();

					if (subOptionLocator != null) {

						// Click on the context menu
						this.sClickAt(optionLocator, "");
						this.zWaitForBusyOverlay();

					}
				}

			}

		} else {
			throw new HarnessException("No logic for action " + action);
		}

		return (page);

	}

	protected AutocompleteEntry parseAutocompleteEntry(String itemLocator) throws HarnessException {
		logger.info(myPageName() + " parseAutocompleteEntry()");

		String locator = null;

		// Get the icon
		locator = itemLocator + " td.AutocompleteMatchIcon div@class";
		String image = this.sGetAttribute(locator);

		// Get the address
		locator = itemLocator + " td + td";
		String address = this.sGetText(locator);

		AutocompleteEntry entry = new AutocompleteEntry(Icon.getIconFromImage(image), address, false, itemLocator);

		return (entry);
	}

	public List<AutocompleteEntry> zAutocompleteListGetEntries() throws HarnessException {
		logger.info(myPageName() + " zAutocompleteListGetEntries()");

		List<AutocompleteEntry> items = new ArrayList<AutocompleteEntry>();

		String containerLocator = "css=div[id^='ZmAutocompleteListView']";

		if (!this.zWaitForElementPresent(containerLocator, "5000")) {
			// Autocomplete is not visible, return an empty list.
			return (items);
		}

		String rowsLocator = containerLocator + " tr[id*='_acRow_']";
		int count = this.sGetCssCount(rowsLocator);
		for (int i = 1; i < count; i++) {

			// The first row (acRow_0) is the "select all" ... skip that one
			items.add(parseAutocompleteEntry(containerLocator + " tr[id$='_acRow_" + i + "']"));
		}
		return (items);
	}

	@Override
	public String myPageName() {
		return (this.getClass().getCanonicalName());
	}
}