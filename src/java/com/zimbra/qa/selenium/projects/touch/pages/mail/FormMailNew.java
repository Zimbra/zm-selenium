/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.touch.pages.mail;

import java.awt.event.KeyEvent;
import java.util.List;
import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem.RecipientType;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsForm;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;

/**
 * The <code>FormMailNew<code> object defines a compose new message view
 * in the Zimbra Ajax client.
 * <p>
 * This class can be used to compose a new message.
 * <p>
 * 
 * @author Matt Rhoades
 * @see http://wiki.zimbra.com/wiki/Testing:_Selenium:_ZimbraSelenium_Overview#Mail_Page
 */
public class FormMailNew extends AbsForm {
	
	/**
	 * Defines Selenium locators for various objects in {@link FormMailNew}
	 */
	public static class Locators {
		
		public static final String zSendButton			= "css=div[id^='ext-composepanel'] span[class='x-button-label']:contains('Send')";
		public static final String zCancelButton		= "css=div[id^='ext-composepanel'] span[class='x-button-label']:contains('Cancel')";
		public static final String zSaveDraftButton		= "css=div[id^='ext-composepanel'] span[class='x-button-label']:contains('Save Draft')";
		public static final String zAttachButton		= "css=div[id^='ext-composepanel'] div[id^=ext-element']['x-innerhtml']:contains('Attach')";
				
		public static final String zToField				= "css=div[id^='ext-composepanel'] div[id='ext-contactfield-1'] div[class^='x-innerhtml'] input";
		public static final String zCcField				= "css=div[id^='ext-composepanel'] div[id='ext-contactfield-2'] div[class^='x-innerhtml'] input";
		public static final String zBccField			= "css=div[id^='ext-composepanel'] div[id='ext-contactfield-3'] div[class^='x-innerhtml'] input";
		public static final String zSubjectField		= "css=input[name=subject]";
		public static final String zBodyField			= "css=div[class$='zcs-fully-editable']";
		
		public static final String zYesWarningDialog	= "css=div[id^='ext-sheet'] div[id^='ext-toolbar'] span[class='x-button-label']:contains('Yes')";
		public static final String zNoWarningDialog		= "css=div[id^='ext-sheet'] div[id^='ext-toolbar'] span[class='x-button-label']:contains('No')";
		public static final String zCancelWarningDialog	= "css=div[id^='ext-sheet'] div[id^='ext-toolbar'] span[class='x-button-label']:contains('Cancel')";
		
	}

	public static class Field {
	
		public static final Field To = new Field("To");
		public static final Field Cc = new Field("Cc");
		public static final Field Bcc = new Field("Bcc");
		public static final Field From = new Field("From");
		public static final Field Subject = new Field("Subject");
		public static final Field Body = new Field("Body");
		
		
		private String field;
		private Field(String name) {
			field = name;
		}
		
		@Override
		public String toString() {
			return (field);
		}

	}
	
	
	/**
	 * Protected constuctor for this object.  Only classes within
	 * this package should create DisplayMail objects.
	 * 
	 * @param application
	 */
	public FormMailNew(AbsApplication application) {
		super(application);
		
		logger.info("new " + FormMailNew.class.getCanonicalName());

	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}
	

	@Override
	public void zSubmit() throws HarnessException {
		logger.info("FormMailNew.submit()");
		
		SleepUtil.sleepSmall();
		zToolbarPressButton(Button.B_SEND);

		this.zWaitForBusyOverlay();

	}

	/**
	 * Press the toolbar button
	 * @param button
	 * @return
	 * @throws HarnessException
	 */
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton("+ button +")");
		
		tracer.trace("Click button "+ button);

		if ( button == null )
			throw new HarnessException("Button cannot be null!");
		
		// Fallthrough objects
		AbsPage page = null;
		String locator = null;
		
		if ( button == Button.B_SEND ) {
			
			locator = Locators.zSendButton;
			page = this;
		
		} else if ( button == Button.B_CANCEL ) {

			locator = Locators.zCancelButton;
			page = this;
			
		} else if ( button == Button.B_SAVE_DRAFT ) {

			locator = Locators.zSaveDraftButton;
			page = this;
			
		} else if ( button == Button.B_EXPAND) {

			locator = "css=span[class='x-button-icon x-shown collapsed']";
			page = this;

			if ( zCcBccIsActive() )
				return (this);
			
		}
		else {
			throw new HarnessException("no logic defined for button "+ button);
		}

		if ( locator == null )
			throw new HarnessException("locator was null for button "+ button);

		this.sClickAt(locator, "");
		SleepUtil.sleepMedium();
		this.zWaitForBusyOverlay();
		
		// Wait for the message to be delivered
		Stafpostqueue sp = new Stafpostqueue();
		sp.waitForPostqueue();
		
		return (page);

	}
	
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton("+ button +")");
		
		tracer.trace("Click button "+ button);

		if ( button == null )
			throw new HarnessException("Button cannot be null!");
		
		// Fallthrough objects
		AbsPage page = null;
		String locator = null;
		
		if ( button == Button.B_YES ) {
			
			locator = Locators.zYesWarningDialog;
			page = this;
			
		} else if ( button == Button.B_NO ) {
			
			locator = Locators.zNoWarningDialog;
			page = this;
			
		} else if ( button == Button.B_CANCEL ) {
			
			locator = Locators.zCancelWarningDialog;
			page = this;
			
		}
		else {
			throw new HarnessException("no logic defined for button "+ button);
		}

		if ( locator == null )
			throw new HarnessException("locator was null for button "+ button);

		this.sClickAt(locator, "");
		SleepUtil.sleepMedium();
		this.zWaitForBusyOverlay();
		
		// Wait for the message to be delivered
		Stafpostqueue sp = new Stafpostqueue();
		sp.waitForPostqueue();
		
		return (page);

	}
	
	/**
	 * Set the 'From' value
	 * @param value
	 */
	public void zSetFromIdentity(String value) throws HarnessException {
		logger.info(myPageName() + " zSetFrom("+ value +")");

		String pulldownLocator = "css=div[id^='zv__COMPOSE'] tr[id$='_identity_row'] td[id$='_dropdown']";
		String optionLocator = "css=td[id$='_title']:contains("+ value +")";
		
		// Default behavior
		if ( pulldownLocator != null ) {
						
			// Make sure the locator exists
			if ( !this.sIsElementPresent(pulldownLocator) ) {
				throw new HarnessException("pulldownLocator not present! "+ pulldownLocator);
			}
			
			this.sClick(pulldownLocator);

			this.zWaitForBusyOverlay();
			
			if ( optionLocator != null ) {

				// Make sure the locator exists
				if ( !this.sIsElementPresent(optionLocator) ) {
					throw new HarnessException("optionLocator not present! "+ optionLocator);
				}
				
				this.sClick(optionLocator);

				this.zWaitForBusyOverlay();

			}
			
		}
		
	}
	
	/**
	 * Fill in the form field with the specified text
	 * @param field
	 * @param value
	 * @throws HarnessException
	 */
	public void zFillField(Field field, String value) throws HarnessException {
			
		tracer.trace("Set "+ field +" to "+ value);
		
		SleepUtil.sleepMedium();

		String locator = null;
		
		if ( field == Field.To ) {
			
			locator = Locators.zToField;
			
			if ( !this.sIsElementPresent(locator) )
				throw new HarnessException("Field is not present field="+ field +" locator="+ locator);
			
			this.sClickAt(locator, "");
			this.zTypeKeys(locator, value);
			SleepUtil.sleepSmall();
			this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_SPACE);
			SleepUtil.sleepSmall();

			return;

		} else if ( field == Field.Cc ) {
			
			locator = Locators.zCcField;

			if ( !zCcBccIsActive() ) {
				this.zToolbarPressButton(Button.B_EXPAND);
			}
			
			if ( !this.sIsElementPresent(locator) )
				throw new HarnessException("Field is not present field="+ field +" locator="+ locator);
			
			this.sClickAt(locator, "");
			this.sType(locator, value);
			SleepUtil.sleepSmall();
			this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_SPACE);
			SleepUtil.sleepSmall();

			return;
						
		} else if ( field == Field.Bcc ) {
			
			locator = Locators.zBccField;
			
			if ( !this.sIsElementPresent(locator) )
				throw new HarnessException("Field is not present field="+ field +" locator="+ locator);
			
			this.sClickAt(locator, "");
			this.sType(locator, value);
			SleepUtil.sleepSmall();
			this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_SPACE);
			SleepUtil.sleepSmall();

			return;
			
		} else if ( field == Field.From ) {
			
			zSetFromIdentity(value);
			return;
						
		} else if ( field == Field.Subject ) {
			
			locator = Locators.zSubjectField;
			this.sFocus(locator);
			this.sClickAt(locator, "");
			
		} else if (field == Field.Body) {
			
			locator = Locators.zBodyField;
			
			this.sFocus(Locators.zSubjectField);
			this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_TAB);
			this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_TAB);
			
			this.sClickAt(locator, "");
			this.sFocus(locator);
			
			SleepUtil.sleepSmall();
			this.zKeyboard.zTypeCharacters(value);
			
			if (!this.sIsElementPresent(locator))
				throw new HarnessException("Unable to locate compose body");
			return;
			
		} else {
			throw new HarnessException("not implemented for field " + field);
		}
		
		if ( locator == null ) {
			throw new HarnessException("locator was null for field "+ field);
		}
		
		if ( !this.sIsElementPresent(locator) )
			throw new HarnessException("Field is not present field="+ field +" locator="+ locator);
		
		this.sFocus(locator);
		this.sClick(locator);
		this.sType(locator, value);

	}
	
	
	private boolean zCcBccIsActive() throws HarnessException {
		logger.info(myPageName() + ".zCcBccIsActive()");

		String locator;
		locator = "css=span[class='x-button-icon x-shown collapsed']";
		
		if ( !sIsElementPresent(locator) )
			throw new HarnessException("Unable to locate the BCC field "+ locator);
		
		return (!sIsElementPresent(locator));
	}

	@Override
	public void zFill(IItem item) throws HarnessException {
		logger.info(myPageName() + ".zFill(ZimbraItem)");
		logger.info(item.prettyPrint());

		// Make sure the item is a MailItem
		if ( !(item instanceof MailItem) ) {
			throw new HarnessException("Invalid item type - must be MailItem");
		}
		
		// Convert object to MailItem
		MailItem mail = (MailItem) item;
		
		// Handle the subject
		if ( mail.dSubject != null ) {
			zFillField(Field.Subject, mail.dSubject);
		}
		
		if ( mail.dBodyText != null ) {
			zFillField(Field.Body, mail.dBodyText);
		}
		
		// Handle the Recipient list, which can be a combination of To, Cc, Bcc, and From
		StringBuilder to = null;
		StringBuilder cc = null;
		StringBuilder bcc = null;
		StringBuilder from = null;
		
		// Convert the list of recipients to a semicolon separated string
		List<RecipientItem> recipients = mail.dAllRecipients();
		if ( recipients != null ) {
			if ( !recipients.isEmpty() ) {
				
				for (RecipientItem r : recipients) {
					if ( r.dType == RecipientType.To ) {
						if ( to == null ) {
							to = new StringBuilder();
							to.append(r.dEmailAddress);
						} else {
							to.append(";").append(r.dEmailAddress);
						}
					}
					if ( r.dType == RecipientType.Cc ) {
						if ( cc == null ) {
							cc = new StringBuilder();
							cc.append(r.dEmailAddress);
						} else {
							cc.append(";").append(r.dEmailAddress);
						}
					}
					if ( r.dType == RecipientType.Bcc ) {
						if ( bcc == null ) {
							bcc = new StringBuilder();
							bcc.append(r.dEmailAddress);
						} else {
							bcc.append(";").append(r.dEmailAddress);
						}
					}
					if ( r.dType == RecipientType.From ) {
						if ( from == null ) {
							from = new StringBuilder();
							from.append(r.dEmailAddress);
						} else {
							from.append(";").append(r.dEmailAddress);
						}
					}
				}
				
			}
		}
		
		// Fill out the To field
		if ( to != null ) {
			this.zFillField(Field.To, to.toString());
			this.zMouseClick(500, 500); //temporary work around to enable save button (see bug https://bugzilla.zimbra.com/show_bug.cgi?id=85490)
		}
		
		if ( cc != null ) {
			this.zFillField(Field.Cc, cc.toString());
			this.zMouseClick(500, 500); //temporary work around to enable save button (see bug https://bugzilla.zimbra.com/show_bug.cgi?id=85490)
		}
		
		if ( bcc != null ) {
			this.zFillField(Field.Bcc, bcc.toString());
			this.zMouseClick(500, 500); //temporary work around to enable save button (see bug https://bugzilla.zimbra.com/show_bug.cgi?id=85490)
		}
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");
		
		// Look for the div
		String locator = Locators.zSendButton;
		
		if ( !this.sIsElementPresent(locator) ) {
			return (false);	
		}
		
		if ( !this.zIsVisiblePerPosition(locator, 0, 0) ) {
			return (false);
		}
		
		logger.info(myPageName() + " zIsActive() = true");
		return (true);
	}
	
	public boolean zHasAttachment() throws HarnessException {
	
		String locator = "css=span[id$='_attachments_div'] div.attBubbleContainer";
		
		boolean hasBubble = this.sIsElementPresent(locator);
		
		return (hasBubble);
		
	}
	
	public boolean zHasAttachment(String name)  throws HarnessException {
	    
	    // Is the bubble there?
		if ( !zHasAttachment() ) {
			return (false);
		}
		
		// Find the attachment name
		String locator = "css=span[id$='_attachments_div'] div.attBubbleContainer a.AttLink";
		
		if ( !this.sIsElementPresent(locator) ) {
			return (false);
		}
		
		
		String filename = this.sGetText(locator);
		
		if ( filename == null || filename.trim().length() == 0 ) {
			return (false);
		}
	    
		return (filename.contains(name));
		
	}

}
