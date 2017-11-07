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
package com.zimbra.qa.selenium.projects.ajax.ui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;

public class ContextMenu extends AbsDisplay {

	public ContextMenu(AbsApplication application) {
		super(application);
	}

	public void zSelect(ContextMenuItem cmi) throws HarnessException {
		logger.info(myPageName() + " zSelect(" + cmi.text + ")");
		this.sClick(cmi.locator);
		zWaitForBusyOverlay();
	}

	@SuppressWarnings("rawtypes")
	public ContextMenuItem getContextMenuItem(String parentLocator, Class contextMenuItemObject)
			throws HarnessException {
		ContextMenuItem cmi = null;

		if (parentLocator.startsWith("DWT")) {
			cmi = ContextMenuItem.C_SEPARATOR;
			return cmi;
		}

		String locator = sGetAttribute("xpath=(//div[@id='" + parentLocator + "']/table/tbody/tr)@id");
		Field[] fields = contextMenuItemObject.getFields();

		for (Field f : fields) {
			try {
				cmi = (ContextMenuItem) f.get(null);

				if (locator.startsWith(cmi.locator)) {
					String cssLocator = "css=td[id='" + parentLocator;

					if (!this.sIsElementPresent(cssLocator + "_left_icon" + "'] " + cmi.image)) {
						throw new HarnessException("cannot find " + cssLocator + "_left_icon" + "'] " + cmi.image);
					}

					if (!this.sIsElementPresent(cssLocator + "_dropdown" + "']" + cmi.shortcut)) {
						throw new HarnessException("cannot find " + cssLocator + "_dropdown" + "']" + cmi.shortcut);
					}

					break;
				}
			} catch (Exception e) {
			}
		}

		if (cmi == null) {
			throw new HarnessException("cannot find context menu " + locator);
		}

		return cmi;
	}

	@SuppressWarnings("rawtypes")
	public ArrayList<ContextMenuItem> zListGetContextMenuItems(Class contextMenuItemObjects) throws HarnessException {

		ArrayList<ContextMenuItem> list = new ArrayList<ContextMenuItem>();
		String typeLocator = null;

		try {
			typeLocator = (String) contextMenuItemObjects.getField("LOCATOR").get(null);
		} catch (Exception e) {
			throw new HarnessException("Context Menu LOCATOR not defined", e);
		}

		if (!this.sIsElementPresent("css=div[" + typeLocator + "]"))
			throw new HarnessException("Context Menu List is not present(visible) " + "css=div[" + typeLocator + "]");

		// Get the number of context menu item including separator
		int count = sGetCssCount("css=div[" + typeLocator + "]>table>tbody>tr");

		logger.debug(
				myPageName() + " zListGetContextMenuItems: number of context menu item including separators: " + count);
		System.out.println(
				myPageName() + " zListGetContextMenuItems: number of context menu item including separators: " + count);

		// Get each context item data's data from the table list
		for (int i = 1; i <= count; i++) {
			// get id attribute
			String id = sGetAttribute("xpath=(//div[@" + typeLocator + "]/table/tbody/tr[" + i + "]/td/div)@id");

			ContextMenuItem ci = getContextMenuItem(id, contextMenuItemObjects);
			list.add(ci);
			ci.parentLocator = id;
		}
		return list;
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zDisplayPressButton(" + button + ")");
		tracer.trace("Click " + button);

		throw new HarnessException("no logic defined for button: " + button);
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		return true;
		// return ( this.sIsElementPresent(Locators.zTagDialogId) );
	}

	// check if a context menu item is enable
	public boolean isEnable(ContextMenuItem cmi) throws HarnessException {
		return !zIsElementDisabled("css=div#" + cmi.parentLocator);
	}

}