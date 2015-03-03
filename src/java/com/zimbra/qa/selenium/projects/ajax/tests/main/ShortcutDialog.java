/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.main;

import org.testng.annotations.*;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.*;
import com.zimbra.qa.selenium.projects.ajax.ui.*;

public class ShortcutDialog extends AjaxCommonTest {

	public ShortcutDialog() {
		logger.info("New " + ShortcutDialog.class.getCanonicalName());

		super.startingPage = app.zPageMail;
		super.startingAccountPreferences = null;

	}

	@Test(description = "Open the 'Shortcut' dialog >> Verify contents and Close Link", groups = { "functional" })
	public void Shortcutdialog_Close() throws HarnessException {

		// Click the Account -> Shrotcut menu
		DialogInformational dialog = (DialogInformational) app.zPageMain
				.zToolbarPressPulldown(Button.B_ACCOUNT, Button.O_SHORTCUT);

		// -- VERIFICATION
		ZAssert.assertTrue(dialog.zIsActive(), "Verify the Shortcut dialog opens");
		ZAssert.assertStringContains(dialog.sGetBodyText(), "Zimbra Keyboard Shortcuts", "Verify Zimbra Keyboard Shortcuts in New window view");
		dialog.zClickButton(Button.B_CLOSE);

	}
	
	
	@Test(description = "Open the 'Shortcut' dialog >> Verify in New Window link and contents", groups = { "functional" })
	public void Shortcutdialog_NewWindow() throws HarnessException {

		// Click the Account -> Shrotcut menu
		DialogInformational dialog = (DialogInformational) app.zPageMain
				.zToolbarPressPulldown(Button.B_ACCOUNT, Button.O_SHORTCUT);

		// -- VERIFICATION
		ZAssert.assertTrue(dialog.zIsActive(), "Verify the Shortcut dialog opens");

		SeparateWindow window = null;
		String windowTitle = "Zimbra";
		String CloseLink = "css=div[class='ZmShortcutsWindow'] div[class='actions'] span[class='link'][onclick*='closeCallback();']";
		try{
			window=(SeparateWindow)dialog.zClickButton(Button.B_NEWWINDOW);
			SleepUtil.sleepLong();
			window.zWaitForActive();
			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");
			window.sSelectWindow(windowTitle);
			logger.info(window.sGetBodyText());
			ZAssert.assertStringContains(window.sGetBodyText(), "Zimbra Keyboard Shortcuts", "Verify Zimbra Keyboard Shortcuts in New window view");
			ZAssert.assertTrue(window.sIsElementPresent(CloseLink),"");
			app.zPageMain.zSeparateWindowClose(windowTitle);
			//dialog.zClickButton(Button.B_CLOSE);Zimbra Keyboard Shortcuts
			app.zPageMain.sSelectWindow(null);

		}finally{
			if(window!=null){
				app.zPageMain.zSeparateWindowClose(windowTitle);
				app.zPageMain.sSelectWindow(null);
				window.sSelectWindow(null);

			}
		}

	}

}
