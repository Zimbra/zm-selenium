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

import java.awt.event.KeyEvent;
import java.util.*;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem.RecipientType;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsSeparateWindow;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Shortcut;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;
import com.zimbra.qa.selenium.projects.ajax.pages.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.pages.SeparateWindowDialog;
import com.zimbra.qa.selenium.projects.ajax.pages.mail.FormMailNew.Field;

public class SeparateWindowFormMailNew extends AbsSeparateWindow {

	public static class Locators {
	}

	public SeparateWindowFormMailNew(AbsApplication application) {
		super(application);
		this.DialogWindowTitle = ": Compose";
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	public void zFill(MailItem mail) throws HarnessException {
		logger.info(myPageName() + ".zFill(MailItem)");
		logger.info(mail.prettyPrint());

		if (mail.dSubject != null) {
			zFillField(Field.Subject, mail.dSubject);
		}

		if (mail.dBodyText != null) {
			zFillField(Field.Body, mail.dBodyText);
		}

		if (mail.dBodyHtml != null) {
			zFillField(Field.Body, mail.dBodyHtml);
		}

		StringBuilder to = null;
		StringBuilder cc = null;
		StringBuilder bcc = null;
		StringBuilder from = null;

		List<RecipientItem> recipients = mail.dAllRecipients();
		if (recipients != null) {
			if (!recipients.isEmpty()) {

				for (RecipientItem r : recipients) {
					if (r.dType == RecipientType.To) {
						if (to == null) {
							to = new StringBuilder();
							to.append(r.dEmailAddress);
						} else {
							to.append(";").append(r.dEmailAddress);
						}
					}
					if (r.dType == RecipientType.Cc) {
						if (cc == null) {
							cc = new StringBuilder();
							cc.append(r.dEmailAddress);
						} else {
							cc.append(";").append(r.dEmailAddress);
						}
					}
					if (r.dType == RecipientType.Bcc) {
						if (bcc == null) {
							bcc = new StringBuilder();
							bcc.append(r.dEmailAddress);
						} else {
							bcc.append(";").append(r.dEmailAddress);
						}
					}
					if (r.dType == RecipientType.From) {
						if (from == null) {
							from = new StringBuilder();
							from.append(r.dEmailAddress);
						} else {
							from.append(";").append(r.dEmailAddress);
						}
					}
				}

			}
		}

		if (to != null) {
			this.zFillField(Field.To, to.toString());
		}

		if (cc != null) {
			this.zFillField(Field.Cc, cc.toString());
		}

		if (bcc != null) {
			this.zFillField(Field.Bcc, bcc.toString());
		}

	}

	public void zFillField(Field field, String value) throws HarnessException {
		logger.info(myPageName() + "zFillField(" + field + ", " + value + ")");

		tracer.trace("Set " + field + " to " + value);

		String container = "css=div[id^='zv__COMPOSE']";
		String locator = null;

		if (field == Field.To) {

			if (ConfigProperties.getStringProperty("browser").contains("edge")) {
				locator = "css=textarea[id$='_to_control']";
			} else {
				locator = container + " tr[id$='_to_row'] input[id$='_to_control']";
			}

		} else if (field == Field.Cc) {

			if (ConfigProperties.getStringProperty("browser").contains("edge")) {
				locator = "css=textarea[id$='_cc_control']";
			} else {
				locator = container + " tr[id$='_cc_row'] input[id$='_cc_control']";
			}

		} else if (field == Field.Bcc) {

			if (ConfigProperties.getStringProperty("browser").contains("edge")) {
				locator = "css=textarea[id$='_bcc_control']";
			} else {
				locator = container + " tr[id$='_bcc_row'] input[id$='_bcc_control']";
			}

			if (!zBccIsActive()) {
				this.zToolbarPressButton(Button.B_SHOWBCC);
			}

		} else if (field == Field.Subject) {

			locator = container + " tr[id$='_subject_row'] input[id$='_subject_control']";

		} else if (field == Field.Body) {
			SleepUtil.sleepLongMedium();

			int frames = sGetCssCountNewWindow("css=iframe");

			logger.info("Body: # of frames: " + frames);

			if (frames == 0) {

				// Text compose
				sType("css=textarea[class='ZmHtmlEditorTextArea']", value);
				return;

			} else if (frames >= 1) {

				// HTML compose
				logger.info("SeparateWindowFormMailNew.zFillField: Html Compose");
				SleepUtil.sleepMedium();
				try {

					this.sSelectFrame("css=iframe[id$='_body_ifr']");

					locator = "css=html body";

					if (!this.sIsElementPresent(locator))
						throw new HarnessException("Unable to locate compose body");

					sClick(locator);
					zTypeCharacters(value);

				} finally {
					this.sSelectFrame("relative=top");
				}
				return;

			} else {
				throw new HarnessException("Compose //iframe count was " + frames);
			}

		} else {
			throw new HarnessException("not implemented for field " + field);
		}

		sType(locator, value);

		if (field == Field.To || field == Field.Cc || field == Field.Bcc) {
			SleepUtil.sleepMedium();
			this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
			SleepUtil.sleepSmall();
		}

		SleepUtil.sleepSmall();
		this.zWaitForBusyOverlay();
	}

	private boolean zBccIsActive() throws HarnessException {
		logger.info(myPageName() + ".zBccIsActive()");

		String locator;

		locator = "css=div[id^='zv__COMPOSE'] tr[id$='_bcc_row']";
		if (!sIsElementPresent(locator))
			throw new HarnessException("Unable to locate the BCC field " + locator);

		locator = locator + "[style*=none]";
		return (!sIsElementPresent(locator));
	}

	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton(" + button + ")");

		tracer.trace("Press the " + button + " button");

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		String container = "css=div[id^='ztb__COMPOSE']";
		String locator = null;
		AbsPage page = null;

		if (button == Button.B_SEND) {

			locator = container + " div[id$='__SEND'] td[id$='_title']";
			page = null;

			this.sClick(locator);
			Stafpostqueue postqueue = new Stafpostqueue();
			postqueue.waitForPostqueue();

			return (page);

		} else if (button == Button.B_CANCEL) {

			locator = container + " div[id$='__CANCEL'] td[id$='_title']";
			page = null;

			this.sClick(locator);
			SleepUtil.sleepMedium();

			return (page);

		} else if (button == Button.B_SAVE_DRAFT) {

			locator = container + " div[id$='__SAVE_DRAFT'] td[id$='_title']";
			page = null;

			this.sClick(locator);
			this.zWaitForBusyOverlay();

			return (page);

		} else if (button == Button.B_ADD_ATTACHMENT) {

			locator = container + " div[id$='__ATTACHMENT'] td[id$='_title']";
			page = null;

		} else if (button == Button.B_SPELL_CHECK) {

			locator = container + " div[id$='__SPELL_CHECK'] td[id$='_title']";
			page = null;

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		this.sClick(locator);
		this.zWaitForBusyOverlay();
		SleepUtil.sleepMedium();

		return (page);

	}

	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");

		tracer.trace("Press the " + button + " button");

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		String locator = null;
		AbsPage page = null;

		SleepUtil.sleepMedium();

		if (button == Button.O_ATTACH_DROPDOWN) {
			if (sIsElementPresent("css=td[id='zb__COMPOSE-2___attachments_btn_dropdown']")) {
				locator = "css=td[id='zb__COMPOSE-2___attachments_btn_dropdown']";
			} else {
				locator = "css=td[id='zb__COMPOSE-1___attachments_btn_dropdown']";
			}

		} else if (button == Button.B_ATTACH) {

			if (ConfigProperties.getStringProperty("browser").contains("edge")) {
				if (sIsElementPresent("css=td[id='zb__COMPOSE-2___attachments_btn_title']")) {
					locator = "css=td[id='zb__COMPOSE-2___attachments_btn_title']";
				} else {
					locator = "css=td[id='zb__COMPOSE-1___attachments_btn_title']";
				}
				this.sClick(locator);
				SleepUtil.sleepSmall();
				this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
				SleepUtil.sleepLong();
				return page;

			} else {
				if (sIsElementPresent("css=td[id='zb__COMPOSE-2___attachments_btn_title']")) {
					locator = "css=td[id='zb__COMPOSE-2___attachments_btn_title']";
				} else {
					locator = "css=td[id='zb__COMPOSE-1___attachments_btn_title']";
				}
			}

		} else if (button == Button.B_MY_COMPUTER) {

			if (ConfigProperties.getStringProperty("browser").contains("edge")) {
				this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
				this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
				SleepUtil.sleepLong();
				return page;

			} else {
				locator = "css=div[class='DwtMenu'] td[id$='_title']:contains('My Computer')";
			}

		} else if (button == Button.B_ATTACH_INLINE) {

			if (ConfigProperties.getStringProperty("browser").contains("edge")) {
				this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_DOWN);
				SleepUtil.sleepSmall();
				this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
				this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
				SleepUtil.sleepLong();
				return page;

			} else {
				locator = "css=div[class='DwtMenu'] td[id$='_title']:contains('Attach Inline')";
			}

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		this.sClick(locator);
		SleepUtil.sleepSmall();

		return (page);
	}

	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressPulldown(" + pulldown + ", " + option + ")");

		tracer.trace("Click pulldown " + pulldown + " then " + option);

		if (pulldown == null)
			throw new HarnessException("Pulldown cannot be null!");

		if (option == null)
			throw new HarnessException("Option cannot be null!");

		String pulldownLocator = null;
		String optionLocator = null;
		AbsPage page = null;

		if (pulldown == Button.B_PRIORITY) {

			pulldownLocator = "css=[id$=__COMPOSE_OPTIONS_title]";

			if (option == Button.O_PRIORITY_HIGH) {

				optionLocator = "css=div[id$=PRIORITY_HIGH]";

				page = null;

			} else if (option == Button.O_PRIORITY_NORMAL) {

				optionLocator = "css=div[id$=PRIORITY_NORMAL]";
				page = null;

			} else if (option == Button.O_PRIORITY_LOW) {

				optionLocator = "css=div[id$=PRIORITY_LOW]";
				page = null;

			} else {
				throw new HarnessException("unsupported priority option " + option);
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

		} else {
			throw new HarnessException("no logic defined for pulldown " + pulldown);
		}

		List<String> locators = new ArrayList<String>();
		locators.add(pulldownLocator);
		locators.add(optionLocator);
		this.sClick(locators);

		return (page);
	}

	public AbsPage zKeyboardShortcut(Shortcut shortcut) throws HarnessException {
		logger.info(myPageName() + " zKeyboardShortcut(" + shortcut + ")");

		if (shortcut == null)
			throw new HarnessException("Shortcut cannot be null");

		tracer.trace("Using the keyboard, press the " + shortcut.getKeys() + " keyboard shortcut");

		AbsPage page = null;

		if (shortcut == Shortcut.S_ESCAPE) {

			page = new SeparateWindowDialog(DialogWarning.DialogWarningID.SaveCurrentMessageAsDraft, this.MyApplication,
					this);
			((AbsSeparateWindow) page).zSetWindowTitle(DialogWindowTitle);
			((AbsSeparateWindow) page).zSetWindowID(DialogWindowTitle);

			zKeyDown("27");
			return page;

		}

		zTypeCharacters(shortcut.getKeys());

		return (page);
	}

	public boolean waitForComposeWindow() throws HarnessException {
		zWaitForElementPresent("css=textarea[id*='DWT'][class='DwtHtmlEditorTextArea']", "10000");
		return true;
	}
}