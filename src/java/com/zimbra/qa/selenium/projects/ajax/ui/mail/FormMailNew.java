package com.zimbra.qa.selenium.projects.ajax.ui.mail;

import java.util.List;

import com.zimbra.qa.selenium.framework.items.IItem;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem.RecipientType;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsForm;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsSeleniumObject;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.Stafpostqueue;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;




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
		
		public static final String zSendIconBtn			= "css=[id^=zb__COMPOSE][id$=__SEND_title]";
		public static final String zCancelIconBtn		= "css=[id^=zb__COMPOSE][id$=__CANCEL_title]";
		public static final String zSaveDraftIconBtn	= "css=[id^=zb__COMPOSE][id$=__SAVE_DRAFT_title]";
		public static final String zSpellCheckIconBtn	= "css=[id^=zb__COMPOSE][id$=__SPELL_CHECK_title]";

		public static final String zToField				= "css=[id^=zv__COMPOSE][id$=_to_control]";
		public static final String zCcField				= "css=[id^=zv__COMPOSE][id$=_cc_control]";
		public static final String zBccField			= "css=[id^=zv__COMPOSE][id$=_bcc_control]";
		public static final String zSubjectField		= "css=div[id^=zv__COMPOSE] input[id$=_subject_control]";
		
		public static final String zBodyFrameHTML		= "//div[contains(id,'zv__COMPOSE')]//iframe";

	}

	public static class Field {
	
		public static final Field To = new Field("To");
		public static final Field Cc = new Field("Cc");
		public static final Field Bcc = new Field("Bcc");
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
	protected FormMailNew(AbsApplication application) {
		super(application);
		
		logger.info("new " + FormMailNew.class.getCanonicalName());

	}

	public boolean zIsVisible() {
		String locator = "//div[contains(@id,'ztb__COMPOSE')]";
		
		if ( !this.sIsElementPresent(locator) )
			return (false);
		
		if ( !this.zIsVisiblePerPosition(locator, 0, 0) )
			return (false);
		
		return (true);
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}
	

	@Override
	public void zSubmit() throws HarnessException {
		logger.info("FormMailNew.submit()");
		
		zToolbarPressButton(Button.B_SEND);

	}

	/**
	 * Press the toolbar button
	 * @param button
	 * @return
	 * @throws HarnessException
	 */
	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton("+ button +")");
		
		if ( button == null )
			throw new HarnessException("Button cannot be null!");
		
		// Fallthrough objects
		AbsPage page = null;
		String locator = null;
		
		if ( button == Button.B_SEND ) {
			
			locator = Locators.zSendIconBtn;
			
			// Look for "Send"
			if ( !this.sIsElementPresent(Locators.zSendIconBtn) )
				throw new HarnessException("Send button is not visible "+ Locators.zSendIconBtn);
			
			// Click on it
			this.zClick(locator);
			
			// Need to wait for the client request to be sent
			SleepUtil.sleepSmall();
			
			// Wait for the message to be delivered
			try {
			
				// Check the message queue
				Stafpostqueue sp = new Stafpostqueue();
				sp.waitForPostqueue();
			
			} catch (Exception e) {
				throw new HarnessException("Unable to wait for message queue", e);
			}
			
			return (null);
		
		} else if ( button == Button.B_CANCEL ) {

			locator = Locators.zCancelIconBtn;
			page = new DialogWarning(DialogWarning.DialogWarningID.SaveCurrentMessageAsDraft, this.MyApplication);
			
			// If the compose view is not dirty (i.e. no pending changes)
			// then the dialog will not appear.  So, click the button
			// and return the page, without waiting for it to be active
			
			if ( !this.sIsElementPresent(locator) )
				throw new HarnessException("Button is not present locator="+ locator +" button="+ button);
			
			this.zClick(locator);

			// Return the page, if specified
			return (page);
			
		} else if ( button == Button.B_SAVE_DRAFT ) {

			locator = Locators.zSaveDraftIconBtn;
			page = this;
			
			// FALL THROUGH
			
		} else if ( button == Button.B_ADD_ATTACHMENT ) {

			throw new HarnessException("implement me (?)");
			
			// FALL THROUGH
			
		} else if ( button == Button.B_SPELL_CHECK ) {

			locator = Locators.zSpellCheckIconBtn;
			page = this;
			
			// FALL THROUGH
			
		} else if ( button == Button.B_SIGNATURE ) {

			throw new HarnessException("use zToolbarPressPulldown to attach signature");
			
		} else if ( button == Button.B_OPTIONS ) {

			throw new HarnessException("use zToolbarPressPulldown to attach signature");
			
		} else {
			throw new HarnessException("no logic defined for button "+ button);
		}

		// Make sure a locator was set
		if ( locator == null )
			throw new HarnessException("locator was null for button "+ button);

		
		// Default behavior, process the locator by clicking on it
		//
		
		// Make sure the button exists
		if ( !this.sIsElementPresent(locator) )
			throw new HarnessException("Button is not present locator="+ locator +" button="+ button);
		
		// Click it
		this.zClick(locator);

		
		if ( page != null ) {
			
			// Make sure the page becomes active
			page.zWaitForActive();
			
		}
		
		// Return the page, if specified
		return (page);

	}
	
	/**
	 * Press the toolbar pulldown and the menu option
	 * @param pulldown
	 * @param option
	 * @return
	 * @throws HarnessException
	 */
	public AbsSeleniumObject zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButtonWithPulldown("+ pulldown +", "+ option +")");
		
		throw new HarnessException("implement me!");

	}
	
	/**
	 * Fill in the form field with the specified text
	 * @param field
	 * @param value
	 * @throws HarnessException
	 */
	public void zFillField(Field field, String value) throws HarnessException {
	
		String locator = null;
		
		if ( field == Field.To ) {
			
			locator = Locators.zToField;
			
			// FALL THROUGH
			
		} else if ( field == Field.Cc ) {
			
			locator = Locators.zCcField;
			
			// FALL THROUGH
			
		} else if ( field == Field.Bcc ) {
			
			locator = Locators.zBccField;
			
			// FALL THROUGH
			
		} else if ( field == Field.Subject ) {
			
			locator = Locators.zSubjectField;
			
			// FALL THROUGH
			
		} else if ( field == Field.Body ) {

			int frames = this.sGetXpathCount("//iframe");
			logger.debug("Body: # of frames: "+ frames);

			if ( frames == 0 ) {
				////
				// Text compose
				////
				
				locator = "//textarea[contains(@id,'textarea_')]";
				
				if ( !this.sIsElementPresent(locator))
					throw new HarnessException("Unable to locate compose body");

				
				this.sFocus(locator);
				this.zClick(locator);
				this.sType(locator, value);
				
				return;
				
			} else if ( frames == 1 ) {
				////
				// HTML compose
				////
				
				try {
					
					this.sSelectFrame("index=0"); // iframe index is 0 based
					
					locator = "//html//body";
					
					if ( !this.sIsElementPresent(locator))
						throw new HarnessException("Unable to locate compose body");

					this.sFocus(locator);
					this.zClick(locator);
					this.sType(locator, value);
					
				} finally {
					// Make sure to go back to the original iframe
					this.sSelectFrame("relative=top");
				}
				
				return;

			} else {
				throw new HarnessException("Compose //iframe count was "+ frames);
			}
			

		} else {
			throw new HarnessException("not implemented for field "+ field);
		}
		
		if ( locator == null ) {
			throw new HarnessException("locator was null for field "+ field);
		}
		
		// Default behavior, enter value into locator field
		//
		
		// Make sure the button exists
		if ( !this.sIsElementPresent(locator) )
			throw new HarnessException("Field is not present field="+ field +" locator="+ locator);
		
		// Enter text
		this.sType(locator, value);
		
		// Is this sleep required?
		SleepUtil.sleepSmall();

	}
	
	
	@Override
	public void zFill(IItem item) throws HarnessException {
		logger.info("FormMailNew.fill(ZimbraItem)");
		logger.info(item.prettyPrint());

		// Make sure the item is a MailItem
		if ( !(item instanceof MailItem) ) {
			throw new HarnessException("Invalid item type - must be MailItem");
		}
		
		// Convert object to MailItem
		MailItem mail = (MailItem) item;
		
		// Fill out the form
		//
		
		// Handle the subject
		if ( mail.dSubject != null ) {
			
			zFillField(Field.Subject, mail.dSubject);

		}
		
		if ( mail.dBodyText != null ) {
			
			zFillField(Field.Body, mail.dBodyText);
			
		}
		
		// TODO: how to handle HTML body?
		
		// Handle the Recipient list, which can be a combination
		// of To, Cc, Bcc, and From
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
		}
		
		if ( cc != null ) {
			this.zFillField(Field.Cc, to.toString());
		}
		
		if ( bcc != null ) {
			this.zFillField(Field.To, to.toString());
		}

		
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		logger.info(myPageName() + " zIsActive()");
		
		// Look for the div
		String locator = "css=div[id^='ztb__COMPOSE']";
		
		if ( !this.sIsElementPresent(locator) ) {
			return (false);	
		}
		
		if ( !this.zIsVisiblePerPosition(locator, 150, 80) ) {
			return (false);
		}
		
		logger.info(myPageName() + " zIsActive() = true");
		return (true);
	}

}
