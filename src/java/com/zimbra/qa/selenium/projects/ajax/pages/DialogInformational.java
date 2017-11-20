/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2015, 2016 Synacor, Inc.
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

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsDialog;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;

public class DialogInformational extends AbsDialog {

	public static class DialogWarningID {

		public static final DialogWarningID InformationalDialog = new DialogWarningID("ZmMsgDialog");
		public static final DialogWarningID ShortcutDialog = new DialogWarningID("css=div[class='ZmShortcutsPanel']");
		protected String Id;

		public DialogWarningID(String id) {
			Id = id;
		}
	}

	protected String MyDivId = null;

	public DialogInformational(DialogWarningID dialogId, AbsApplication application, AbsTab tab) {
		super(application, tab);
		MyDivId = dialogId.Id;
		logger.info("new " + DialogInformational.class.getCanonicalName());
	}

	public String zGetWarningTitle() throws HarnessException {
		String locator = "css=div[id='" + MyDivId + "'] td[id='" + MyDivId + "_title']";
		return (zGetDisplayedText(locator));
	}

	public String zGetWarningContent() throws HarnessException {
		String locator = "css=td[id^='MessageDialog'][class='DwtMsgArea']";
		return (zGetDisplayedText(locator));
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		if (button == null)
			throw new HarnessException("button cannot be null");

		String locator = null;
		AbsPage page = null;

		String buttonsTableLocator = "css=div[id='" + MyDivId + "'] div[id$='_buttons']";

		if (button == Button.B_OK) {

			locator = buttonsTableLocator + " td[id^='OK_'] td[id$='_title']";

		} else if (button == Button.B_CANCEL) {

			locator = buttonsTableLocator + " td[id^='CANCEL_'] td[id$='_title']";

		} else if (button == Button.B_CLOSE) {

			locator = "css=div[class='ZmShortcutsPanel'] span[class='link']:contains('Close')";
			sClickAt(locator, "");
			return page;

		} else if (button == Button.B_NEWWINDOW) {

			locator = "css=div[class='ZmShortcutsPanel'] span[class='link']:contains('New Window')";
			page = new SeparateWindow(this.MyApplication);
			sClickAt(locator, "");
			SleepUtil.sleepVeryLong();
			return page;

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		sClickAt(locator, "");
		zWaitForBusyOverlay();
		SleepUtil.sleepMedium();

		if (button == Button.B_OK) {
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();
		}

		return (page);
	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		if (locator == null)
			throw new HarnessException("locator cannot be null");

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("locator cannot be found");

		return (this.sGetText(locator));

	}

	@Override
	public boolean zIsActive() throws HarnessException {

		if (!this.sIsElementPresent(MyDivId))
			return (false);

		if (!this.zIsVisiblePerPosition(MyDivId, 0, 0))
			return (false);

		return (true);
	}
}