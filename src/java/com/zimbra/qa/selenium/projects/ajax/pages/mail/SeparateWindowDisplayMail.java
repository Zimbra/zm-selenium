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
package com.zimbra.qa.selenium.projects.ajax.pages.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.By;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsSeparateWindow;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Shortcut;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;
import com.zimbra.qa.selenium.projects.ajax.pages.AjaxPages;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogWarning.DialogWarningID;
import com.zimbra.qa.selenium.projects.ajax.pages.SeparateWindowDialog;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.DisplayMail.Field;

/**
 * Represents a "Launch in New Window" display of a message
 * 
 * @author Matt Rhoades
 */
public class SeparateWindowDisplayMail extends AbsSeparateWindow {

	public static class Locators {

		public static final String zIncludeOriginalAsAttachmentMenuMail = "css=td[id='zmi__COMPOSE-1_REPLY__INC_ATTACHMENT_title']:contains('Include Original As Attachment')";
		public static final String zOptionsdropdown = "css=[id^=zb__COMPOSE][id$=__COMPOSE_OPTIONS_dropdown]";
		public static final String FormatAsHTMLMenu = "css=div[id^='zm__COMPOSE'] div[id$='NEW_MESSAGE__FORMAT_HTML'] tr[id^='POPUP_zmi__COMPOSE']";
		public static final String FormatAsPlainTextMenu = "css=div[id^='zm__COMPOSE'] div[id$='NEW_MESSAGE__FORMAT_TEXT'] tr[id^='POPUP_zmi__COMPOSE']";
		public static final String zSaveDraftIconBtn = "css=[id^=zb__COMPOSE][id$=__SAVE_DRAFT_title]";
		public static final String zCancelIconBtn = "css=[id^=zb__COMPOSE][id$=__CANCEL_title]";
		public static final String zUntagBubble = "css=table[id='zv__MSG-1__MSG_headerElement'] tr[id$='_tagRow'] span[class='addrBubble TagBubble'] span[class='ImgBubbleDelete']";
	}

	public String ContainerLocator = "css=div[id='zv__MSG-1__MSG']";

	public SeparateWindowDisplayMail(AbsApplication application) {
		super(application);
		this.DialogWindowTitle = null;
	}

	public String zGetMailProperty(Field field) throws HarnessException {
		logger.info(myPageName() + " zGetDisplayedValue(" + field + ")");

		String container = "css=div[id='zv__MSG-1__MSG']";
		String locator = null;

		if (field == Field.From) {

			locator = container + " td[id$='_from'] span:nth-child(1)>span[class='addrBubble']>span";

			if (!this.sIsElementPresent(locator)) {
				locator = container + " tr[id$='_from']";
			}

		} else if (field == Field.To) {

			locator = container + " tr[id$='_to'] span[id$='_com_zimbra_email']";
			if (!this.sIsElementPresent(locator)) {
				locator = container + " tr[id$='_to']";
			}

		} else if (field == Field.Cc) {

			locator = container + " tr[id$='_cc'] span[id$='_com_zimbra_email']";
			if (!this.sIsElementPresent(locator)) {
				locator = container + " tr[id$='_cc']";
			}

		} else if (field == Field.OnBehalfOf) {
			locator = container + " span[id$='_obo_span'] span[class='addrBubble']>span";

			if (!sIsElementPresent(locator)) {
				locator = container + " span[id$='_obo_span']";
			}

		} else if (field == Field.ResentFrom) {

			locator = container + " span[id$='_bwo_span'] span[class='addrBubble']>span";
			if (!sIsElementPresent(locator)) {
				locator = container + " span[id$='_bwo_span']";
			}

		} else if (field == Field.OnBehalfOfLabel) {

			locator = container + " td[id$='_obo_label']";

		} else if (field == Field.ReplyTo) {

			locator = container + " tr[id$='_reply to'] span[class='addrBubble'] span:contains(replyto)";

			if (!sIsElementPresent(locator)) {
				locator = container + " tr[id$='_reply to']";
			}

		} else if (field == Field.Subject) {

			locator = container + " tr[id='zv__MSG__MSG-1_hdrTableTopRow'] td[class~='SubjectCol']";

		} else if (field == Field.ReceivedDate) {

			locator = container + " tr[id$='_hdrTableTopRow'] td[class~='DateCol']";

		} else if (field == Field.ReceivedTime) {

			String timeAndDateLocator = container + " tr[id$='_hdrTableTopRow'] td[class~='DateCol'] ";

			if (!sIsElementPresent(timeAndDateLocator))
				throw new HarnessException("Unable to find the time and date field!");

			// Get the subject value
			String time = this.sGetText(timeAndDateLocator).trim();

			logger.info("zGetDisplayedValue(" + field + ") = " + time);
			return (time);

		} else if (field == Field.Body) {

			try {
				String bodyLocator = "body";
				webDriver().switchTo().defaultContent();
				webDriver().switchTo().frame(0);
				webDriver().findElement(By.cssSelector(bodyLocator));

				String htmlBody = this.sGetHtmlBody();
				logger.info("DisplayMail.zGetDisplayedValue(" + bodyLocator + ") = " + htmlBody);
				return (htmlBody);

			} finally {
				this.sSelectFrame("relative=top");
			}

		} else {

			throw new HarnessException("No logic defined for Field: " + field);

		}

		String value = sGetText(locator);

		logger.info("zGetDisplayedValue(" + field + ") = " + value);
		return (value);

	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	public void zWaitForActive() throws HarnessException {
		super.zWaitForActive(PageLoadDelay);

		String locator = "css=div[id='zv__MSG-1__MSG'] tr[id='zv__MSG__MSG-1_hdrTableTopRow'] td[class*='SubjectCol']";
		for (int i = 0; i < 15; i++) {

			boolean present = sIsElementPresent(locator);
			if (present) {
				return;
			}

			SleepUtil.sleepMedium();
		}
		throw new HarnessException("Page never became active!");

	}

	public boolean zMailSecurityPresent(String emailAddress) throws HarnessException {

		String locator = "css=td[class='ZmSecureMailCertificateRow'] td[id$='title']:contains('" + emailAddress + "')";

		if (this.sIsElementPresent(locator)) {
			return true;
		}

		return false;
	}

	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		tracer.trace("Press the " + button + " button");

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		String container = "css=div[id^='ztb__MSG']";
		String locator = null;
		String containerToolbar = "css=div[id^='ztb__MSG']";
		AbsPage page = null;

		if (button == Button.B_CLOSE) {

			locator = container + " div[id$='__CLOSE'] td[id$='_title']";
			this.sClick(locator);
			page = null;
			return page;

		} else if (button == Button.B_DELETE) {

			locator = container + " div[id$='__DELETE'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_SAVE_DRAFT) {

			locator = Locators.zSaveDraftIconBtn;
			page = this;

		} else if (button == Button.B_REPLY) {

			locator = container + " div[id$='__REPLY'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_CANCEL) {

			locator = Locators.zCancelIconBtn;
			page = new DialogWarning(DialogWarning.DialogWarningID.SaveCurrentMessageAsDraft, this.MyApplication,
					((AjaxPages) this.MyApplication).zPageMail);
			this.sClick(locator);
			this.zWaitForBusyOverlay();

			return (page);

		} else if (button == Button.B_REPLYALL) {

			locator = container + " div[id$='__REPLY_ALL'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_FORWARD) {

			locator = container + " div[id$='__FORWARD'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_RESPORTSPAM) {

			locator = container + " div[id$='__SPAM'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_RESPORTNOTSPAM) {

			locator = container + " div[id$='__SPAM'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_ARCHIVE) {

			locator = container + " div[id$='__ARCHIVE_ZIMLET_BUTTON_ID'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_ACTIONS) {

			locator = containerToolbar + " div[id$='__ACTIONS_MENU'] td[id$='_dropdown']>div";
			page = null;

		} else if (button == Button.B_SEND) {

			if (sIsElementPresent("css=div[id^='ztb__COMPOSE-2'] div[id*='SEND'] td[id$='_title']")) {
				locator = "css=div[id^='ztb__COMPOSE-2'] div[id*='SEND'] td[id$='_title']";
			} else {
				locator = "css=div[id^='ztb__COMPOSE'] div[id*='SEND'] td[id$='_title']";
			}

			SleepUtil.sleepSmall();

			this.sClick(locator);

			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();

			SleepUtil.sleepSmall();
			page = null;

			return (page);

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		this.sClick(locator);
		this.zWaitForBusyOverlay();
		SleepUtil.sleepMedium();

		return (page);
	}

	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown(" + pulldown + ", " + option + ")");

		tracer.trace("Click pulldown " + pulldown + " then " + option);

		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");

		if (option == null)
			throw new HarnessException("Option cannot be null!");

		String containerToolbar = "css=div[id^='ztb__MSG']";
		String containerActionMenu = "css=div[id^='zm__MSG']";
		String pulldownLocator = null;
		String optionLocator = null;
		AbsPage page = null;

		if (pulldown == Button.B_ACTIONS) {

			pulldownLocator = containerToolbar + " div[id$='__ACTIONS_MENU'] td[id$='_dropdown']>div";

			if (option == Button.B_PRINT) {

				optionLocator = containerActionMenu + " div[id='PRINT'] td[id$='_title']";

			} else if (option == Button.B_RESPORTSPAM) {

				optionLocator = containerActionMenu + " div[id='SPAM'] td[id$='_title']";

			} else if (option == Button.B_RESPORTNOTSPAM) {

				optionLocator = containerActionMenu + " div[id='SPAM'] td[id$='_title']";

			} else if (option == Button.O_MARK_AS_READ) {

				optionLocator = containerActionMenu + " div[id$='MARK_READ'] td[id$='_title']";

			} else if (option == Button.O_MARK_AS_UNREAD) {

				optionLocator = containerActionMenu + " div[id$='MARK_UNREAD'] td[id$='_title']";

			} else if (option == Button.O_SHOW_ORIGINAL) {

				optionLocator = containerActionMenu + " div[id='SHOW_ORIG'] td[id$='_title']";

			} else if (option == Button.B_REDIRECT) {

				optionLocator = containerActionMenu + " div[id*='REDIRECT'] td[id$='_title']";
				page = new SeparateWindowDialogRedirect(this.MyApplication, this);

			} else if (option == Button.O_EDIT_AS_NEW) {

				optionLocator = containerActionMenu + " div[id$='EDIT_AS_NEW'] td[id$='_title']";

			} else if (option == Button.O_CREATE_TASK) {

				optionLocator = containerActionMenu + " div[id$='CREATE_TASK'] td[id$='_title']";

			} else if (option == Button.O_CREATE_APPOINTMENT) {

				optionLocator = containerActionMenu + " div[id$='CREATE_APPT'] td[id$='_title']";

			} else if (option == Button.O_NEW_FILTER) {

				optionLocator = containerActionMenu + " div[id$='ADD_FILTER_RULE'] td[id$='_title']";

			} else if (option == Button.B_FLAG_MESSAGE) {

				optionLocator = containerActionMenu + " div[id$='FLAG'] td[id$='_title']";

			} else if (option == Button.B_UNFLAG_MESSAGE) {

				optionLocator = containerActionMenu + " div[id$='UNFLAG'] td[id$='_title']";

			} else {
				throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
			}

		} else if (pulldown == Button.B_TAG) {

			pulldownLocator = containerToolbar + " div[id$='__TAG_MENU'] td[id$='_dropdown']>div";

			if (option == Button.O_TAG_NEWTAG) {

				optionLocator = "css=td[id$='__TAG_MENU|MENU|NEWTAG_title']";

			} else if (option == Button.O_TAG_REMOVETAG) {

				optionLocator = "css=div[id$='__TAG_MENU|MENU'] div[id='message_removetag'] td[id$='_title']";

			} else {
				throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);
			}

		} else if (pulldown == Button.B_SECURE_EMAIL) {

			pulldownLocator = "css=td[id$='_com_zimbra_securemail_checkbox_title']";

			if (option == Button.O_DONT_SIGN) {
				optionLocator = "css=div[id$='_com_zimbra_securemail_menu'] table tbody tr:contains('Don't Sign')";
				page = this;

			} else if (option == Button.O_SIGN) {
				optionLocator = "//*[contains(@id,'_com_zimbra_securemail_menu')]/descendant::td[text()='Sign']";
				page = this;

			} else if (option == Button.O_SIGN_AND_ENCRYPT) {
				optionLocator = "//*[contains(@id,'_com_zimbra_securemail_menu')]/descendant::td[text()='Sign and Encrypt']";
				page = this;

			} else {
				throw new HarnessException("unsupported signing option " + option);
			}

		} else if (pulldown == Button.B_OPTIONS) {

			if (option == Button.O_INCLUDE_ORIGINAL_AS_ATTACHMENT) {

				pulldownLocator = Locators.zOptionsdropdown;
				optionLocator = Locators.zIncludeOriginalAsAttachmentMenuMail;

			} else if (option == Button.O_FORMAT_AS_HTML) {

				pulldownLocator = Locators.zOptionsdropdown;
				optionLocator = Locators.FormatAsHTMLMenu;

			} else if (option == Button.O_FORMAT_AS_PLAIN_TEXT) {

				pulldownLocator = Locators.zOptionsdropdown;
				optionLocator = Locators.FormatAsPlainTextMenu;

			} else if (option == Button.O_FORMAT_AS_HTML_MULTI_WINDOW) {

				pulldownLocator = Locators.zOptionsdropdown;
				optionLocator = Locators.FormatAsHTMLMenu;

				this.sClick(pulldownLocator);
				sClick("css=div[id='zm__COMPOSE-1_REPLY'] div[id='zmi__COMPOSE-1_REPLY__FORMAT_HTML'] td[id='zmi__COMPOSE-1_REPLY__FORMAT_HTML_title']");
				this.zWaitForBusyOverlay();
				SleepUtil.sleepMedium();

				String warningMessageOkButton = "css=div[id='OkCancel'] td[id$='_button2_title']:contains('OK')";
				if (sIsElementPresent(warningMessageOkButton)) {
					sClick(warningMessageOkButton);
				}

				return page;

			} else if (option == Button.O_FORMAT_AS_PLAIN_TEXT_MULTI_WINDOW) {

				pulldownLocator = Locators.zOptionsdropdown;
				optionLocator = Locators.FormatAsPlainTextMenu;

				this.sClick(pulldownLocator);
				sClick("css=div[id='zm__COMPOSE-1_REPLY'] div[id='zmi__COMPOSE-1_REPLY__FORMAT_TEXT'] td[id='zmi__COMPOSE-1_REPLY__FORMAT_TEXT_title']");
				this.zWaitForBusyOverlay();
				SleepUtil.sleepSmall();

				String warningMessageOkButton = "css=div[id='OkCancel'] td[id$='_button2_title']:contains('OK')";
				if (sIsElementPresent(warningMessageOkButton)) {
					sClick(warningMessageOkButton);
				}

				return page;
			}

		} else if (pulldown == Button.B_MOVE) {

			if (option == Button.O_NEW_FOLDER) {

				if (this.zIsVisiblePerPosition("css=div#ztb__MSG-1", 0, 0)) { // in case of new window
					pulldownLocator = "css=div[id='ztb__MSG-1'] div[id$='__MOVE_MENU'] td[id$='_dropdown']";
					optionLocator = "css=div[id='ZmMoveButton_MSG-1'] div[id$='NEWFOLDER'] td[id$='_title']";
				} else if (this.zIsVisiblePerPosition("css=div#ztb__CLV-main", 0, 0)) {
					pulldownLocator = "css=div[id='ztb__CLV-main'] div[id$='__MOVE_MENU'] td[id$='_dropdown']";
					optionLocator = "css=div[id='ZmMoveButton_CLV-main'] div[id$='NEWFOLDER'] td[id$='_title']";
				} else {
					pulldownLocator = "css=div[id='ztb__TV-main'] div[id$='__MOVE_MENU'] td[id$='_dropdown']";
					optionLocator = "css=div[id='ZmMoveButton_TV-main'] div[id$='NEWFOLDER'] td[id$='_title']";
				}
				page = new DialogCreateFolder(this.MyApplication, ((AjaxPages) this.MyApplication).zPageMail);

				if (!this.sIsElementPresent(pulldownLocator)) {
					throw new HarnessException(pulldownLocator + " not present!");
				}

				this.sClickAt(pulldownLocator, "0,0");
				zWaitForBusyOverlay();

				if (!this.sIsElementPresent(optionLocator)) {
					throw new HarnessException(optionLocator + " not present!");
				}

				this.sClickAt(optionLocator, "");
				zWaitForBusyOverlay();
				SleepUtil.sleepMedium();

				return (page);

			} else {
				throw new HarnessException("no logic defined for B_MOVE and " + option);
			}

		} else {
			throw new HarnessException("no logic defined for pulldown/option " + pulldown + "/" + option);

		}

		List<String> locators = new ArrayList<String>();
		locators.add(pulldownLocator);
		locators.add(optionLocator);
		this.sClick(locators);
		SleepUtil.sleepLong();

		return (page);

	}

	public void zFillField(Field field, String value) throws HarnessException {
		logger.info(myPageName() + "zFillField(" + field + ", " + value + ")");

		tracer.trace("Set " + field + " to " + value);

		String locator = null;

		if (field == Field.Body) {

			SleepUtil.sleepLong();

			int frames = sGetCssCountNewWindow("css=iframe");

			logger.debug("Body: # of frames: " + frames);

			if (frames == 0) {

				// Text compose
				sTypeNewWindow("css=textarea[class='ZmHtmlEditorTextArea']", value);

				return;

			} else if (frames == 1) {

				// HTML compose

				try {

					locator = "css=body[id='tinymce']";

					if (!sIsElementPresent(locator))
						throw new HarnessException("Unable to locate compose body");

					sFocus(locator);
					sClick(locator);
					zTypeCharacters(value);

				} finally {
					this.sSelectFrame("relative=top");
				}

				return;

			} else if (frames >= 2) {

				// HTML compose

				try {

					locator = "css=iframe[id$='_body_ifr']";

					if (!this.sIsElementPresent(locator))
						throw new HarnessException("Unable to locate compose body");

					this.sFocus(locator);
					this.sClick(locator);
					this.zTypeCharacters(value);

				} finally {
					this.sSelectFrame("relative=top");
				}

			} else {
				throw new HarnessException("Compose //iframe count was " + frames);
			}
		}

	}

	public AbsPage zToolbarPressPulldown(Button button, Object dynamic) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton(" + button + ", " + dynamic + ")");

		tracer.trace("Click pulldown " + button + " then " + dynamic);

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		if (dynamic == null)
			throw new HarnessException("Dynamic cannot be null!");

		String container = "css=div[id^='ztb__MSG']";
		String pulldownLocator = null;
		String optionLocator = null;
		AbsPage page = null;

		if (button == Button.B_TAG) {

			if (!(dynamic instanceof String))
				throw new HarnessException("if button = B_TAG, then dynamic should be a tag name");
			String tagname = (String) dynamic;

			pulldownLocator = container + " div[id$='__TAG_MENU'] td[id$='_dropdown']>div";
			optionLocator = "css=div[id$='__TAG_MENU|MENU'] td[id$='_title']:contains(" + tagname + ")";
			page = null;

		} else if (button == Button.B_MOVE) {

			if (!(dynamic instanceof FolderItem))
				throw new HarnessException("if pulldown = " + Button.B_MOVE + ", then dynamic must be FolderItem");

			FolderItem folder = (FolderItem) dynamic;

			// Check if we are CLV or MV
			if (this.zIsVisiblePerPosition("css=div#ztb__MSG-1", 0, 0)) { // in case of new window
				pulldownLocator = "css=div[id='ztb__MSG-1'] div[id$='__MOVE_MENU'] td[id$='_dropdown']";
				optionLocator = "css=td#zti__ZmFolderChooser_MailMSG-1__" + folder.getId() + "_textCell";
			} else if (this.zIsVisiblePerPosition("css=div#ztb__CLV-main", 0, 0)) {
				pulldownLocator = "css=td#zb__CLV-main__MOVE_MENU_dropdown>div";
				optionLocator = "css=td#zti__ZmFolderChooser_MailCLV-main__" + folder.getId() + "_textCell";
			} else {
				pulldownLocator = "css=td#zb__TV-main__MOVE_MENU_dropdown>div";
				optionLocator = "css=td#zti__ZmFolderChooser_MailTV-main__" + folder.getId() + "_textCell";
			}
			page = null;

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		if (pulldownLocator != null) {
			this.sClick(pulldownLocator);
			SleepUtil.sleepSmall();
			if (optionLocator != null) {
				this.sClick(optionLocator);
			}
		}
		this.zWaitForBusyOverlay();

		return (page);

	}

	public AbsPage zKeyboardShortcut(Shortcut shortcut) throws HarnessException {
		logger.info(myPageName() + " zKeyboardShortcut(" + shortcut + ")");

		if (shortcut == null)
			throw new HarnessException("Shortcut cannot be null");

		tracer.trace("Using the keyboard, press the " + shortcut.getKeys() + " keyboard shortcut");

		AbsPage page = null;

		if (shortcut == Shortcut.S_ESCAPE) {
			zKeyDown("27");
			return page;
		}

		zTypeCharacters(shortcut.getKeys());
		return (page);

	}

	public boolean zHasShareADButtons() throws HarnessException {
		List<String> locators = Arrays.asList(this.ContainerLocator + " td[id$='__Shr__SHARE_ACCEPT_title']",
				this.ContainerLocator + " td[id$='__Shr__SHARE_DECLINE_title']");

		for (String locator : locators) {
			if (!this.sIsElementPresent(locator))
				return (false);
		}

		return (true);
	}

	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zDisplayPressButton(" + button + ")");

		tracer.trace("Click " + button);

		AbsPage page = this;
		String locator = null;
		boolean doPostfixCheck = false;

		if (button == Button.B_VIEW_ENTIRE_MESSAGE) {

			locator = this.ContainerLocator + " span[id$='_msgTruncation_link']";

			if (!this.sIsElementPresent(locator))
				throw new HarnessException("locator is not present for button " + button + " : " + locator);

			this.sClick(locator);

			this.zWaitForBusyOverlay();

			return (page);

		} else if (button == Button.B_VIEW_CERTIFICATE) {

			locator = "css=td[class='ZmSecureMailCertificateRow'] div[class='FakeAnchor'] td[id$='title']:contains('View certificate')";

			if (!this.sIsElementPresent(locator))
				throw new HarnessException("locator is not present for button " + button + " : " + locator);

			this.sClick(locator);

			this.zWaitForBusyOverlay();

			return (page);

		} else if (button == Button.B_HIGHLIGHT_OBJECTS) {

			locator = this.ContainerLocator + " span[id$='_highlightObjects_link']";

			if (!this.sIsElementPresent(locator))
				throw new HarnessException("locator is not present for button " + button + " : " + locator);

			this.sClick(locator);
			this.zWaitForBusyOverlay();

			return (page);

		} else if (button == Button.B_ACCEPT) {

			locator = DisplayMail.Locators.AcceptButton;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.O_ACCEPT_NOTIFY_ORGANIZER) {

			locator = DisplayMail.Locators.AcceptNotifyOrganizerMenu;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.O_ACCEPT_EDIT_REPLY) {

			locator = DisplayMail.Locators.AcceptEditReplyMenu;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.O_ACCEPT_DONT_NOTIFY_ORGANIZER) {

			locator = DisplayMail.Locators.AcceptDontNotifyOrganizerMenu;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.B_TENTATIVE) {

			locator = DisplayMail.Locators.TentativeButton;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.O_TENTATIVE_NOTIFY_ORGANIZER) {

			locator = DisplayMail.Locators.TentativeNotifyOrganizerMenu;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.O_TENTATIVE_EDIT_REPLY) {

			locator = DisplayMail.Locators.TentativeEditReplyMenu;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.O_TENTATIVE_DONT_NOTIFY_ORGANIZER) {

			locator = DisplayMail.Locators.TentativeDontNotifyOrganizerMenu;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.B_DECLINE) {

			locator = DisplayMail.Locators.DeclineButton;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.O_DECLINE_NOTIFY_ORGANIZER) {

			locator = DisplayMail.Locators.DeclineNotifyOrganizerMenu;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.O_DECLINE_EDIT_REPLY) {

			locator = DisplayMail.Locators.DeclineEditReplyMenu;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.O_DECLINE_DONT_NOTIFY_ORGANIZER) {

			locator = DisplayMail.Locators.DeclineDontNotifyOrganizerMenu;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.B_PROPOSE_NEW_TIME) {

			locator = DisplayMail.Locators.ProposeNewTimeButton;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.B_ACCEPT_SHARE) {

			locator = this.ContainerLocator + " td[id$='__Shr__SHARE_ACCEPT_title']";
			page = new SeparateWindowDialog(DialogWarningID.ZmAcceptShare, MyApplication, this);
			((SeparateWindowDialog) page).zSetWindowTitle(this.DialogWindowTitle);
			doPostfixCheck = true;

		} else if (button == Button.B_DECLINE_SHARE) {

			locator = this.ContainerLocator + " td[id$='__Shr__SHARE_DECLINE_title']";
			page = new SeparateWindowDialog(DialogWarningID.ZmDeclineShare, MyApplication, this);
			((SeparateWindowDialog) page).zSetWindowTitle(this.DialogWindowTitle);
			doPostfixCheck = true;

		} else if (button == Button.O_ATTACH_DROPDOWN) {
			if (sIsElementPresent("css=td[id='zb__COMPOSE-2___attachments_btn_dropdown']")) {
				locator = "css=td[id='zb__COMPOSE-2___attachments_btn_dropdown']";
			} else {
				locator = "css=td[id='zb__COMPOSE-1___attachments_btn_dropdown']";
			}
			page = null;

			this.sClick(locator);
			return (page);

		} else if (button == Button.B_ATTACH) {
			if (sIsElementPresent("css=td[id='zb__COMPOSE-2___attachments_btn_title']")) {
				locator = "css=td[id='zb__COMPOSE-2___attachments_btn_title']";
			} else {
				logger.info(sIsElementPresent("css=td[id='zb__COMPOSE-1___attachments_btn_title']"));
				locator = "css=td[id='zb__COMPOSE-1___attachments_btn_title']";
			}
			page = null;

			this.sClick(locator);
			SleepUtil.sleepMedium();

			return (page);

		} else if (button == Button.B_MY_COMPUTER) {
			locator = "css=td[id$='_title']:contains('My Computer')";
			page = null;

			this.sClick(locator);
			SleepUtil.sleepMedium();

			return (page);

		} else if (button == Button.B_ATTACH_INLINE) {
			locator = "css=td[id$='_title']:contains('Attach Inline')";
			page = null;

			this.sClick(locator);

			return (page);

		} else {
			throw new HarnessException("no implementation for button: " + button);
		}

		if (locator == null)
			throw new HarnessException("no locator defined for button " + button);

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("locator is not present for button " + button + " : " + locator);

		this.sClick(locator);

		if (doPostfixCheck) {
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();
		}

		return (page);
	}

	public AbsPage zPressButtonPulldown(Button pulldown, Button option) throws HarnessException {

		logger.info(myPageName() + " zPressButtonPulldown(" + pulldown + ", " + option + ")");

		tracer.trace("Click pulldown " + pulldown + " then " + option);

		if (pulldown == null || option == null)
			throw new HarnessException("Button/options cannot be null!");

		String pulldownLocator = null;
		String optionLocator = null;
		AbsPage page = this;
		boolean doPostfixCheck = false;

		if (pulldown == Button.B_ACCEPT) {

			pulldownLocator = DisplayMail.Locators.AcceptDropdown;
			page = null;
			doPostfixCheck = true;

			if (option == Button.O_ACCEPT_NOTIFY_ORGANIZER) {

				optionLocator = DisplayMail.Locators.AcceptNotifyOrganizerMenu;
				page = null;
				doPostfixCheck = true;

			} else if (option == Button.O_ACCEPT_EDIT_REPLY) {

				optionLocator = DisplayMail.Locators.AcceptEditReplyMenu;
				page = null;
				doPostfixCheck = true;

			} else if (option == Button.O_ACCEPT_DONT_NOTIFY_ORGANIZER) {

				optionLocator = DisplayMail.Locators.AcceptDontNotifyOrganizerMenu;

				page = null;

			} else {

				throw new HarnessException("No logic defined for pulldown " + pulldown + " and option " + option);

			}

		} else if (pulldown == Button.B_TENTATIVE) {

			pulldownLocator = DisplayMail.Locators.TentativeDropdown;

			if (option == Button.O_TENTATIVE_NOTIFY_ORGANIZER) {

				optionLocator = DisplayMail.Locators.TentativeNotifyOrganizerMenu;
				page = null;
				doPostfixCheck = true;

			} else if (option == Button.O_TENTATIVE_EDIT_REPLY) {

				optionLocator = DisplayMail.Locators.TentativeEditReplyMenu;
				page = null;
				doPostfixCheck = true;

			} else if (option == Button.O_TENTATIVE_DONT_NOTIFY_ORGANIZER) {

				optionLocator = DisplayMail.Locators.TentativeDontNotifyOrganizerMenu;

				page = null;

			} else {

				throw new HarnessException("No logic defined for pulldown " + pulldown + " and option " + option);

			}

		} else if (pulldown == Button.B_DECLINE) {

			pulldownLocator = DisplayMail.Locators.DeclineDropdown;

			if (option == Button.O_DECLINE_NOTIFY_ORGANIZER) {

				optionLocator = DisplayMail.Locators.DeclineNotifyOrganizerMenu;
				page = null;
				doPostfixCheck = true;

			} else if (option == Button.O_DECLINE_EDIT_REPLY) {

				optionLocator = DisplayMail.Locators.DeclineEditReplyMenu;
				page = null;
				doPostfixCheck = true;

			} else if (option == Button.O_DECLINE_DONT_NOTIFY_ORGANIZER) {

				optionLocator = DisplayMail.Locators.DeclineDontNotifyOrganizerMenu;
				page = null;
			}

		} else if (pulldown == Button.B_CALENDAR) {

			pulldownLocator = DisplayMail.Locators.CalendarDropdown;
			page = null;

		} else {

			throw new HarnessException("No logic defined for pulldown " + pulldown + " and option " + option);
		}

		sClick(pulldownLocator);
		zWaitForBusyOverlay();

		this.sClick(optionLocator);
		zWaitForBusyOverlay();

		SleepUtil.sleepMedium();

		if (doPostfixCheck) {
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();
		}

		return (page);

	}

	public boolean waitForWindowDisplay(String pageTitle) throws HarnessException {

		sWaitForCondition("var x; for(var windowName in selenium.browserbot.openedWindows)"
				+ "{var targetWindow = selenium.browserbot.openedWindows[windowName];"
				+ "if (!selenium.browserbot._windowClosed(targetWindow)&&" + "(targetWindow.name.indexOf('"
				+ pageTitle.split("\\.")[0] + "')!=-1||targetWindow.document.title.indexOf('"
				+ pageTitle.split("\\.")[0] + "')!=-1)) {x=windowName;}};x!=null;", "60000");

		sSelectWindow(pageTitle);

		zWaitForElementPresent("css=html>body[class*='MsgBody']", "30000");

		return true;
	}
}