/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
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
/**
 * 
 */
package com.zimbra.qa.selenium.projects.ajax.ui.mail;

import java.awt.event.KeyEvent;
import java.util.*;

import com.zimbra.qa.selenium.framework.core.SeleniumService;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem;
import com.zimbra.qa.selenium.framework.items.RecipientItem.RecipientType;
import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsSeparateWindow;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.ui.Shortcut;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;
import com.zimbra.qa.selenium.projects.ajax.ui.SeparateWindowDialog;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew.Field;



/**
 * Represents a "Compose in New Window" form
 * <p>
 * @author Matt Rhoades
 *
 */
public class SeparateWindowFormMailNew extends AbsSeparateWindow {

	public static class Locators {

	}
	


	public SeparateWindowFormMailNew(AbsApplication application) {
		super(application);
		
		this.DialogWindowTitle = "Compose";
		
	}
	
	/* (non-Javadoc)
	 * @see framework.ui.AbsDialog#myPageName()
	 */
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	public void zFill(MailItem mail) throws HarnessException {
		logger.info(myPageName() + ".zFill(MailItem)");
		logger.info(mail.prettyPrint());
	
		if ( mail.dSubject != null ) {
			zFillField(Field.Subject, mail.dSubject);

		}
		
		if ( mail.dBodyText != null ) {
			zFillField(Field.Body, mail.dBodyText);
		}
		
		if ( mail.dBodyHtml != null ) {
		    if(ZimbraSeleniumProperties.isWebDriver()){
				sSelectWindow(this.DialogWindowID);
				String locator = "css=iframe[id*=ifr]";
				sClickAt(locator,"");
				zTypeFormattedText(locator, mail.dBodyHtml);					
		    } else {
		    	zFillField(Field.Body, mail.dBodyHtml);
		    }
		}
				
		StringBuilder to = null;
		StringBuilder cc = null;
		StringBuilder bcc = null;
		StringBuilder from = null;
		
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
		
		if ( to != null ) {
			this.zFillField(Field.To, to.toString());
		}
		
		if ( cc != null ) {
			this.zFillField(Field.Cc, cc.toString());
		}
		
		if ( bcc != null ) {
			this.zFillField(Field.Bcc, bcc.toString());
		}

	}

	public void zFillField(Field field, String value) throws HarnessException {
		logger.info(myPageName() + "zFillField("+ field +", "+ value +")");

		tracer.trace("Set "+ field +" to "+ value);

		String container = "css=div[id^='zv__COMPOSE']";
		String locator = null;
		
		if ( field == Field.To ) {
			
			locator = container + " tr[id$='_to_row'] input[id$='_to_control']";
						
		} else if ( field == Field.Cc ) {
			
			locator = container + " tr[id$='_cc_row'] input[id$='_cc_control']";
						
		} else if ( field == Field.Bcc ) {
			
			locator = container + " tr[id$='_bcc_row'] input[id$='_bcc_control']";
			
			if ( !zBccIsActive() ) {
				this.zToolbarPressButton(Button.B_SHOWBCC);
			}
						
		} else if ( field == Field.Subject ) {
			
			locator = container + " tr[id$='_subject_row'] input[id$='_subject_control']";
			
		} else if (field == Field.Body) {

			SleepUtil.sleepLong();

			int frames = sGetCssCountNewWindow("css=iframe");
			
			logger.debug("Body: # of frames: " + frames);
			String browser = SeleniumService.getInstance().getSeleniumBrowser();

			if (browser.equalsIgnoreCase("iexplore")) {
				if (frames == 1) {
					// //
					// Text compose
					// //

					locator = "css=textarea[id*='textarea_']";

					if (!this.sIsElementPresent(locator))
						throw new HarnessException(
								"Unable to locate compose body");

					this.sFocus(locator);
					this.zClick(locator);
					this.sTypeNewWindow(locator, value);

					return;

				} else if (frames == 2) {

					//locator = "css=iframe[id^='iframe_DWT']";
					locator ="css=iframe[id$='_content_ifr']";
					if (!this.sIsElementPresent(locator))
						throw new HarnessException(
								"Unable to locate compose body");

					zTypeFormattedText(locator, value);

					return;

				}

			} else {
				if (frames == 0) {
					
					// //
					// Text compose
					// //

					sTypeNewWindow("css=textarea[class='ZmHtmlEditorTextArea']", value);

					return;

				} else if (frames == 1) {
					
					// //
					// HTML compose
					// //
					try {

						sSelectFrame("index=0"); // iframe index is 0 based

						locator = "css=body[id='tinymce']";

						if (!sIsElementPresent(locator))
							throw new HarnessException("Unable to locate compose body");

						sFocus(locator);
						zClick(locator);
						// this.sType(locator, value);
						zTypeCharacters(value);
						
					} finally {
						this.sSelectFrame("relative=top");
					}

					return;

				} else {
					throw new HarnessException("Compose //iframe count was " + frames);
				}
			}

		} else {
			throw new HarnessException("not implemented for field " + field);
		}
		
		if ( locator == null ) {
			throw new HarnessException("locator was null for field "+ field);
		}
		
		this.sFocus(locator);
		this.zClickAt(locator, "10,10");
		this.zWaitForBusyOverlay();
		
		sTypeNewWindow(locator, value);
		SleepUtil.sleepSmall();
		this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_ENTER);
		SleepUtil.sleepSmall();
		this.zKeyboard.zTypeKeyEvent(KeyEvent.VK_TAB);
		SleepUtil.sleepSmall();
		this.zWaitForBusyOverlay();
		
	}
	

	private boolean zBccIsActive() throws HarnessException {
		logger.info(myPageName() + ".zBccIsActive()");

		String locator;
		
		locator = "css=div[id^='zv__COMPOSE'] tr[id$='_bcc_row']";
		if ( !sIsElementPresent(locator) )
			throw new HarnessException("Unable to locate the BCC field "+ locator);
		
		locator = locator + "[style*=none]";
		return (!sIsElementPresent(locator));
	}


	public AbsPage zToolbarPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressButton("+ button +")");

		tracer.trace("Press the "+ button +" button");

		if ( button == null )
			throw new HarnessException("Button cannot be null!");

		String container = "css=div[id^='ztb__COMPOSE']";
		String locator = null;			// If set, this will be clicked
		AbsPage page = null;	// If set, this page will be returned

		if ( button == Button.B_SEND ) {

			locator = container + " div[id$='__SEND'] td[id$='_title']";
			page = null;
			
			if(zIsBrowserMatch(BrowserMasks.BrowserMaskIE)){
			    this.sDoubleClick(locator);	 
			}else{
			    this.zClickAt(locator,"0,0");
			}
			
			Stafpostqueue postqueue = new Stafpostqueue();
			postqueue.waitForPostqueue();
			
			return (page);

		} else if ( button == Button.B_CANCEL ) {

			locator = container + " div[id$='__CANCEL'] td[id$='_title']";
			page = null;

			this.zClickAt(locator,"0,0");

			// Wait for a while for the window to close
			SleepUtil.sleepMedium();

			return (page);

		} else if ( button == Button.B_SAVE_DRAFT ) {

			locator = container + " div[id$='__SAVE_DRAFT'] td[id$='_title']";
			page = null;

			this.zClickAt(locator,"0,0");

			this.zWaitForBusyOverlay();

			return (page);

		} else if ( button == Button.B_ADD_ATTACHMENT ) {

			locator = container + " div[id$='__ATTACHMENT'] td[id$='_title']";
			page = null;

			// FALL THROUGH

		} else if ( button == Button.B_SPELL_CHECK ) {

			locator = container + " div[id$='__SPELL_CHECK'] td[id$='_title']";
			page = null;

			// FALL THROUGH

		} else {
			
			throw new HarnessException("no logic defined for button "+ button);
			
		}

		this.zClickAt(locator,"0,0");

		return (page);
		
	}

	
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zPressButton(" + button + ")");

		tracer.trace("Press the " + button + " button");

		if (button == null)
			throw new HarnessException("Button cannot be null!");

		String locator = null;
		AbsPage page = null;
		
		SleepUtil.sleepSmall();

		if (button == Button.O_ATTACH_DROPDOWN) {
			if (sIsElementPresent("css=td[id='zb__COMPOSE-2___attachments_btn_dropdown']")) {
				locator = "css=td[id='zb__COMPOSE-2___attachments_btn_dropdown']";
			} else {
				locator = "css=td[id='zb__COMPOSE-1___attachments_btn_dropdown']";
			}
			
			logger.info(sIsElementPresent("css=td[id='zb__COMPOSE-1___attachments_btn_dropdown'])"));
			this.zClickAt(locator, "0,0");
			return(page);
			
		} else if (button == Button.B_ATTACH) {
			if (sIsElementPresent("css=td[id='zb__COMPOSE-2___attachments_btn_title']")) {
				locator = "css=td[id='zb__COMPOSE-2___attachments_btn_title']";
			} else {
				
				logger.info(sIsElementPresent("css=td[id='zb__COMPOSE-1___attachments_btn_title']"));
				locator = "css=td[id='zb__COMPOSE-1___attachments_btn_title']";
			}
			
		} else if (button == Button.B_MY_COMPUTER) {
			locator = "css=td[id$='_title']:contains('My Computer')";
			
		} else if (button == Button.B_ATTACH_INLINE) {
			locator = "css=td[id$='_title']:contains('Attach Inline')";
			logger.info(sIsElementPresent("css=td[id$='_title']:contains('Attach Inline')"));
			this.zClickAt(locator, "0,0");
			return(page);

		} else {
			throw new HarnessException("no logic defined for button " + button);
		}

		//Explicitly use sclick >> sclickat not working
		this.sClick(locator);
		
		SleepUtil.sleepMedium();

		return (page);
	}
	
	
	
	public AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException {
		logger.info(myPageName() + " zToolbarPressPulldown("+ pulldown +", "+ option +")");
		
		tracer.trace("Click pulldown "+ pulldown +" then "+ option);

		if ( pulldown == null )
			throw new HarnessException("Pulldown cannot be null!");
		
		if ( option == null )
			throw new HarnessException("Option cannot be null!");

		// Default behavior variables
		//
		String pulldownLocator = null;	// If set, this will be expanded
		String optionLocator = null;	// If set, this will be clicked
		AbsPage page = null;	// If set, this page will be returned
		
		// Based on the button specified, take the appropriate action(s)
		//

		if ( pulldown == Button.B_PRIORITY ) {
			
			
			pulldownLocator = "css=[id$=__COMPOSE_OPTIONS_title]";
			
			if ( option == Button.O_PRIORITY_HIGH ) {
				
				optionLocator = "css=div[id$=PRIORITY_HIGH]";
				
				page = null;

			} else if ( option == Button.O_PRIORITY_NORMAL ) {
				
				optionLocator = "css=div[id$=PRIORITY_NORMAL]";
				page = null;

			} else if ( option == Button.O_PRIORITY_LOW ) {
				
				optionLocator = "css=div[id$=PRIORITY_LOW]";
				page = null;

			} else {
				throw new HarnessException("unsupported priority option "+ option);
			}
		
		} else {
			throw new HarnessException("no logic defined for pulldown "+ pulldown);
		}

		// Default behavior

		if ( ZimbraSeleniumProperties.isWebDriver() ) {

			// Webdriver
			List<String> locators = new ArrayList<String>();
			locators.add(pulldownLocator);
			locators.add(optionLocator);
			
			// Click on:
			// 1. pulldownLocator
			// 2. optionLocator
			//
			this.sClick(locators);
			
		} else {
			
			// Selenium
			if ( pulldownLocator != null ) {
							
				zClickAt(pulldownLocator, "");

				if ( optionLocator != null ) {

					zClickAt(optionLocator, "");

				}
				
			}

		}
		
		// Return the specified page, or null if not set
		return (page);
	}

	public AbsPage zKeyboardShortcut(Shortcut shortcut) throws HarnessException {
		logger.info(myPageName() + " zKeyboardShortcut("+ shortcut +")");
		
		if (shortcut == null)
			throw new HarnessException("Shortcut cannot be null");

		tracer.trace("Using the keyboard, press the "+ shortcut.getKeys() +" keyboard shortcut");

		AbsPage page = null;

		if (shortcut== Shortcut.S_ESCAPE) {

			// This dialog may or may not appear, depending on the message content
			page = new SeparateWindowDialog(
					DialogWarning.DialogWarningID.SaveCurrentMessageAsDraft,
					this.MyApplication,
					this);
			((AbsSeparateWindow)page).zSetWindowTitle(DialogWindowTitle);
			((AbsSeparateWindow)page).zSetWindowID(DialogWindowTitle);

			zKeyDown("27");
			return page;

		}


		zTypeCharacters(shortcut.getKeys());

		return (page);	
		
	}
	
	/* TODO: ... debugging to be removed */
	public boolean waitForComposeWindow() throws HarnessException {
	    	String pageTitle = "Zimbra: Compose";
	    	if (ZimbraSeleniumProperties.isWebDriver()){
	    	    sWaitForPopUp(pageTitle,"30000");
	    	}else{
	    	    sWaitForCondition("var x; for(var windowName in selenium.browserbot.openedWindows)"
			+ "{var targetWindow = selenium.browserbot.openedWindows[windowName];"
			+ "if(!selenium.browserbot._windowClosed(targetWindow)&&"
			+ "(targetWindow.name.indexOf('"
			+ pageTitle.split("\\.")[0]
			+ "')!=-1||targetWindow.document.title.indexOf('"
			+ pageTitle.split("\\.")[0]
			+ "')!=-1)){x=windowName;}};x!=null;","60000");
	    	}
		sSelectWindow(pageTitle);

		zWaitForElementPresent("css=textarea[id*='DWT'][class='DwtHtmlEditorTextArea']","10000");

		return true;
	}

}
