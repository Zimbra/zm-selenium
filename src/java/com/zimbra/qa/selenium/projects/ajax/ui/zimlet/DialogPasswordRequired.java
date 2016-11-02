package com.zimbra.qa.selenium.projects.ajax.ui.zimlet;

/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2016 Synacor, Inc.
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
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;

/**
 * Represents a "Add Document Version Notes" dialog box
 * <p>
 */
public class DialogPasswordRequired extends AbsDialog {

	public static class Locators {
		public static final String zDialogClass = "ZmCertificatePasswordDialog";
		public static final String zDialogTitleClass = "css=td[class=DwtDialogTitle]";
		public static final String zDialogButtonsClass = "DwtDialogButtonBar";
		public static final String zPassword = "css=input[id='CertificatePasswordDialog_password']";
	}

	public DialogPasswordRequired(AbsApplication application, AbsTab tab) {
		super(application, tab);
		logger.info("new " + DialogPasswordRequired.class.getCanonicalName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");

		String locator = Locators.zDialogTitleClass;

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

	@Override
	public AbsPage zClickButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zClickButton(" + button + ")");
		tracer.trace("Click dialog button " + button);

		String locator = null;

		if (button == Button.B_SUBMIT) {
			locator = "css=td[id='CertificatePasswordDialog_button2_title']:contains('Submit')";
			
		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}
		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("Button " + button + " locator "
					+ locator + " not present!");
		}
		this.zClickAt(locator, "0,0");

		return (null);
	}
	
	public void zEnterPassword(String password) throws HarnessException {

		if (password == null)
			throw new HarnessException("password must not be null");
		
		String locator = Locators.zPassword;
		
		if (!this.zWaitForElementPresent(locator, "10000"))
			throw new HarnessException("unable to find body field " + locator);

		this.sFocus(locator);
		this.zClickAt(locator, "0,0");
		this.sFocus(locator);
		this.zKeyboard.zTypeCharacters(password);
	}
	
	@Override
	public String zGetDisplayedText(String text) throws HarnessException {
		return null;
	}

}
