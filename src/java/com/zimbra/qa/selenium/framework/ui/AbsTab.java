package com.zimbra.qa.selenium.framework.ui;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.zimbra.qa.selenium.framework.items.TagItem;
import com.zimbra.qa.selenium.framework.util.GeneralUtility;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.framework.util.GeneralUtility.WAIT_FOR_OPERAND;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties.AppType;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.PageMail;


/**
 * A <code>AbsTab</code> object represents a major Zimbra application "tab",
 * such as a Mail, Addressbook, Calendar, Tasks, Briefcase, Preferences, etc.
 * <p>
 * In addition to the major application tabs, the AbsPage also implements
 * other frames in the clients, such as the top title area and the search area.
 * <p>
 * Most AbsPage objects include methods for managing and interacting with
 * the toolbars, lists, mouseclick actions, and other GUI elements.  It is
 * intended that the test case methods would use the AbsPage objects to
 * perform the majority of the GUI interaction, without having to access
 * Selenium methods or locators directly.
 * <p>
 * @author Matt Rhoades
 *
 */
public abstract class AbsTab extends AbsPage {
	protected static Logger logger = LogManager.getLogger(AbsTab.class);



	/**
	 * Create this page object that exists in the specified application
	 * @param application
	 */
	public AbsTab(AbsApplication application) {
		super(application);
		
		logger.info("new AbsTab");
	}
	
	

	
	/**
	 * Navigate to this page
	 * @throws HarnessException
	 */
	public abstract void zNavigateTo() throws HarnessException;
	

	
	/**
	 * Take action on list items
	 * 
	 * (mainly applies to mail, contacts, tasks)
	 * For mail, item identifier is the subject.
	 * For contacts, item identifier is the email.
	 * For tasks, item identifier is the summary.
	 * 
	 * @param action See Actions class
	 * @param item The item identifier
	 * @return
	 * @throws HarnessException
	 */
	public abstract AbsPage zListItem(Action action, String item) throws HarnessException;
	
	/**
	 * Take action on list items with optional action
	 * (mainly right-click -> context menu)
	 */
	public abstract AbsPage zListItem(Action action, Button option, String item) throws HarnessException;
	
	/**
	 * Take action on list items with optional action
	 * (mainly right-click -> context menu-> sub menu)
	 */
	public abstract AbsPage zListItem(Action action, Button option, Button subOption, String item) throws HarnessException;
	
	/**
	 * Click on a button
	 * @param button the button to press
	 * @return Returns the resulting Page, Wizard, etc. or null
	 * @throws HarnessException
	 */
	public abstract AbsPage zToolbarPressButton(Button button) throws HarnessException;
	
	
	/**
	 * Click on a pulldown with the specified option in the pulldown
	 * @param pulldown
	 * @param option
	 * @return Returns the resulting Page, Wizard, etc. or null
	 * @throws HarnessException
	 */
	public abstract AbsPage zToolbarPressPulldown(Button pulldown, Button option) throws HarnessException;
	
	/**
	 * Use the keyboard to enter the specified keyboard shortcut
	 * @param shortcut
	 * @return Returns the resulting Page, Wizard, etc. or null
	 * @throws HarnessException
	 */
	public AbsPage zKeyboardShortcut(Shortcut shortcut) throws HarnessException {
		AbsPage page = null;
		zKeyboardTypeString(shortcut.getKeys());
		return (page);
	}

	/**
	 * Use the keyboard to enter the specified keyboard key event
	 * @param keyEvent see java.awt.event.KeyEvent
	 * @return Returns the resulting Page, Wizard, etc. or null
	 * @throws HarnessException
	 */
	public AbsPage zKeyboardKeyEvent(int keyEvent) throws HarnessException {
		AbsPage page = null;
		this.zKeyboard.zTypeKeyEvent(keyEvent);
		return (page);
	}
	
	/**
	 * Use the keyboard to enter the specified string
	 * @param keys
	 * @return
	 * @throws HarnessException
	 */
	public AbsPage zKeyboardTypeString(String keys) throws HarnessException {
		AbsPage page = null;
		this.zKeyboard.zTypeCharacters(keys);
		return (page);
	}
	
   /** Waiting for the desktop loading spinner
    * @throws HarnessException 
    * 
    */
   public void zWaitForDesktopLoadingSpinner(long timeout) throws HarnessException {
      if (ZimbraSeleniumProperties.getAppType() == AppType.DESKTOP) {
         String spinnerLocator = "css=img[src='/img/animated/ImgSpinner.gif']";
         if (GeneralUtility.waitForElementPresent(this,
               spinnerLocator, timeout)) {
            Object[] params = {spinnerLocator};
            GeneralUtility.waitFor(null, this, false, "sIsElementPresent",
                  params, WAIT_FOR_OPERAND.EQ, false, 30000, 1000);
         }
      }
   }

   /**
    * Getting tagItem
    * @param account Account from which, tagItem will be retrieved
    * @param tagName Tag Name to be searched for
    * @return Tag Item with the given tagName under given account
    * @throws HarnessException
    */
   public TagItem zGetTagItem(ZimbraAccount account, String tagName)
   throws HarnessException {
      if (account == null) {
         throw new HarnessException("Account cannot be null");
      } else if (tagName == null) {
         throw new HarnessException("tagName cannot be null");
      }

      zWaitForDesktopLoadingSpinner(5000);
      return TagItem.importFromSOAP(account, tagName);

   }
}
