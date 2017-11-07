/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
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

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;

/**
 * Represents a "Rename Folder" dialog box
 * 
 * @author Matt Rhoades
 */
public class DialogShareDecline extends AbsDialog {

	public static class Locators {
		public static final String zDialogShareId = "ShareDialog";
		public static final String zButtonsId = "ShareDialog_buttons";
	}

	public DialogShareDecline(AbsApplication application, AbsTab tab) {
		super(application, tab);
	}

	public static class ShareWith {
		public static ShareWith InternalUsers = new ShareWith("InternalUsers");
		public static ShareWith ExternalGuests = new ShareWith("ExternalGuests");
		public static ShareWith Public = new ShareWith("Public");

		protected String ID;

		protected ShareWith(String id) {
			ID = id;
		}

		public String toString() {
			return (ID);
		}

	}

	public void zSetShareWith(ShareWith name) throws HarnessException {
		logger.info(myPageName() + " zSetShareWith(" + name + ")");

		String locator = "implement me";

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("zSetShareWith " + locator + " is not present");
		}

	}

	public void zSetEmailAddress(String email) throws HarnessException {
		logger.info(myPageName() + " zSetEmailAddress(" + email + ")");

		String locator = "css=div#ShareDialog_grantee>input";

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("zSetEmailAddress " + locator + " is not present");
		}
		this.sFocus(locator);
		this.sKeyPress(locator, "\13");
		this.sType(locator, email);
		this.sKeyUp(locator, "\13");

	}

	public static class ShareRole {
		public static ShareRole None = new ShareRole("None");
		public static ShareRole Viewer = new ShareRole("Viewer");
		public static ShareRole Manager = new ShareRole("Manager");
		public static ShareRole Admin = new ShareRole("Admin");

		protected String ID;

		protected ShareRole(String id) {
			ID = id;
		}

		public String toString() {
			return (ID);
		}

	}

	public void zSetRole(ShareRole role) throws HarnessException {
		logger.info(myPageName() + " zSetRole(" + role + ")");
		String locator = null;
		if (role == ShareRole.Admin) {
			locator = "//div[@id='" + Locators.zDialogShareId
					+ "']//div[contains(@id,'_content')]//div/fieldset/div/table/tbody/tr[4]/td/input[contains(@id,'ShareRole_ADMIN')]";
		} else if (role == ShareRole.Manager) {
			locator = "//div[@id='" + Locators.zDialogShareId
					+ "']//div[contains(@id,'_content')]//div/fieldset/div/table/tbody/tr[3]/td/input[contains(@id,'ShareRole_MANAGER')]";
		} else {
			throw new HarnessException("zSetRole " + locator + " is not present");
		}
		this.sFocus(locator);
		this.sClick(locator);
	}

	public static class ShareMessageType {
		public static ShareMessageType SendStandardMsg = new ShareMessageType("SendStandardMsg");
		public static ShareMessageType DoNotSendMsg = new ShareMessageType("DoNotSendMsg");
		public static ShareMessageType AddNoteToStandardMsg = new ShareMessageType("AddNoteToStandardMsg");
		public static ShareMessageType ComposeInNewWindow = new ShareMessageType("ComposeInNewWindow");

		protected String ID;

		protected ShareMessageType(String id) {
			ID = id;
		}

		public String toString() {
			return (ID);
		}
	}

	public void zSetMessageType(ShareMessageType type) throws HarnessException {
		logger.info(myPageName() + " zSetMessageType(" + type + ")");

		String locator = "implement me";

		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("zSetRole " + locator + " is not present");
		}

	}

	@Override
	public AbsPage zClickButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zClickButton(" + button + ")");

		String locator = null;

		if (button == Button.B_OK) {
			locator = "//div[@id='" + Locators.zDialogShareId + "']//div[@id='" + Locators.zButtonsId
					+ "']//td[text()='OK']";

		} else if (button == Button.B_CANCEL) {
			locator = "//div[@id='" + Locators.zDialogShareId + "']//div[@id='" + Locators.zButtonsId
					+ "']//td[text()='Cancel']";

		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		this.sClick(locator);
		zWaitForBusyOverlay();

		if (button == Button.B_OK) {
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();
		}

		return (null);
	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		throw new HarnessException("implement me");
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public boolean zIsActive() throws HarnessException {

		logger.info(myPageName() + " zIsActive()");

		String locator = "id=" + Locators.zDialogShareId;

		if (!this.sIsElementPresent(locator)) {
			return (false);
		}

		if (!this.zIsVisiblePerPosition(locator, 0, 0)) {
			return (false);
		}

		logger.info(myPageName() + " zIsVisible() = true");
		return (true);
	}
}