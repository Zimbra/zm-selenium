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
/**
 * 
 */
package com.zimbra.qa.selenium.projects.universal.ui;

import java.awt.event.KeyEvent;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;

/**
 * Represents a "Rename Folder" dialog box
 * <p>
 * 
 * @author Matt Rhoades
 *
 */
public class DialogShare extends AbsDialog {

	public static class Locators {
		public static final String zDialogShareId = "ShareDialog";
		public static final String zButtonsId = "ShareDialog_buttons";
		public static final String zShareMessageNote = "css=div[id='ZmShareReply'] textarea";
		public static final String zShareMessageDropDown = "css=td[id='ZmShareReplySelect_select_container']";
		public static final String zAddNoteToStandardMessage = "//td[contains(@id,'_title') and contains(text(),'Add note to standard message')]";
		public static final String zDoNotSendMailAboutThisShare = "//td[contains(@id,'_title') and contains(text(),'Do not send mail about this share')]";

	}

	public DialogShare(AbsApplication application, AbsTab tab) {
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

	public void zSetShareWith(ShareWith type) throws HarnessException {
		logger.info(myPageName() + " zSetShareWith(" + type + ")");

		String locator = null;

		if (type == ShareWith.InternalUsers) {

			locator = "css=input#ShareWith_user";

		} else if (type == ShareWith.ExternalGuests) {

			locator = "css=input#ShareWith_external";

		} else if (type == ShareWith.Public) {

			locator = "css=input#ShareWith_public";

		} else {
			throw new HarnessException("type = " + type + " not implemented yet");
		}

		// Make sure the locator exists
		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("zSetShareWith " + locator + " is not present");
		}

		// check the box
		this.sClick(locator);
		this.zWaitForBusyOverlay();

	}

	public void zSetEmailAddress(String email) throws HarnessException {
		logger.info(myPageName() + " zSetEmailAddress(" + email + ")");

		String locator = "css=input#ShareDialog_grantee";

		// Make sure the locator exists
		if (!this.sIsElementPresent(locator)) {
			throw new HarnessException("zSetEmailAddress " + locator + " is not present");
		}

		// Seems that the client can't handle filling out the new mail form too
		// quickly
		// Click in the "To" fields, etc, to make sure the client is ready
		this.sFocus(locator);
		this.zClick(locator);
		this.zWaitForBusyOverlay();

		// this.zKeyboard.zTypeCharacters(email);
		this.sType(locator, email);
		SleepUtil.sleepSmall();
		this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
		SleepUtil.sleepSmall();
		this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_TAB);
		SleepUtil.sleepSmall();
		this.zWaitForBusyOverlay();
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
		// this.sCheck(locator);
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

	public void zSetMessageType(ShareMessageType type, String message) throws HarnessException {
		logger.info(myPageName() + " zSetMessageType(" + type + ")");

		if (type == null)
			throw new HarnessException("folder must not be null");

		if (type == ShareMessageType.AddNoteToStandardMsg) {

			zClickAt(Locators.zShareMessageDropDown, "");
			zClick(Locators.zAddNoteToStandardMessage);
			this.sFocus(Locators.zShareMessageNote);
			this.zClick(Locators.zShareMessageNote);
			this.zWaitForBusyOverlay();

			// this.zKeyboard.zTypeCharacters(message);
			this.sType(Locators.zShareMessageNote, message);
			SleepUtil.sleepSmall();

		}

		else if (type == ShareMessageType.DoNotSendMsg) {
			zClickAt(Locators.zShareMessageDropDown, "");
			zClick(Locators.zDoNotSendMailAboutThisShare);
		}
	}

	@Override
	public AbsPage zClickButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zClickButton(" + button + ")");

		String locator = null;

		if (button == Button.B_OK) {

			locator = "css=div[id='" + Locators.zDialogShareId + "'] td[id^='OK'] td[id$='_title']";

		} else if (button == Button.B_CANCEL) {

			locator = "css=div[id='" + Locators.zDialogShareId + "'] td[id^='Cancel'] td[id$='_title']";

		} else {
			throw new HarnessException("Button " + button + " not implemented");
		}

		this.sClickAt(locator, "");
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
