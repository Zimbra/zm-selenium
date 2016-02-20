/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.ui;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;

public class DialogAttach extends AbsDialog {
	public static class Locators {

		// TODO: See https://bugzilla.zimbra.com/show_bug.cgi?id=54173
		public static final String zDialogId = "css=div[class='ZmAttachDialog']";
		public static final String zTitleId = "css=td[class='DwtDialogTitle']";
		/*
		 * public static final String zDialogContentId =
		 * "ChooseFolderDialog_content"; // TODO: Tree public static final
		 * String zDialogInputId = "ChooseFolderDialog_inputDivId"; public
		 * static final String zDialogInputLocator = "css=div[id='"+ zDialogId
		 * +"'] div[id='"+ zDialogInputId +"'] > div > input"; public static
		 * final String zDialogButtonsId = "ChooseFolderDialog_buttons";
		 */
	}

	public DialogAttach(AbsApplication application, AbsTab page) {
		super(application, page);

		logger.info("new " + DialogAttach.class.getCanonicalName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public AbsPage zClickButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zClickButton(" + button + ")");

		AbsPage page = null;
		String locator = null;

		if (button == Button.B_ATTACH) {

			locator = "css=div[class='ZmAttachDialog'] div[id$='_buttons'] td[id^='OK'] td[id$='_title']:contains('Attach')";

		} else if (button == Button.B_CANCEL) {

			locator = "css=div[class='ZmAttachDialog'] td[id^='Cancel_'] td[id$='_title']";

		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		this.zClick(locator);

		this.zWaitForBusyOverlay();

		return (page);
	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		return null;
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		logger.info(myPageName() + " zIsActive()");

		String locator = Locators.zDialogId;

		if (!this.sIsElementPresent(locator)) {
			return (false); // Not even present
		}

		if (!this.zIsVisiblePerPosition(locator, 0, 0)) {
			return (false); // Not visible per position
		}

		// Yes, visible
		logger.info(myPageName() + " zIsActive() = true");
		return (true);

	}

}
