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
package com.zimbra.qa.selenium.projects.ajax.ui;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsDialog;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;


/**
 * A <code>DialogWarning</code> object represents a "Warning" dialog, such as "Save 
 * current message as draft", etc.
 * <p>
 * During construction, the div ID attribute must be specified, such as "YesNoCancel".
 * <p>
 * @author Matt Rhoades
 *
 */
public class DialogWarning extends AbsDialog {

	public static class DialogWarningID {
		
		public static final DialogWarningID ZmMsgDialog = new DialogWarningID("ZmMsgDialog");
		public static final DialogWarningID SaveCurrentMessageAsDraft = new DialogWarningID("YesNoCancel");
		public static final DialogWarningID SaveTaskChangeMessage = new DialogWarningID("YesNoCancel");
		public static final DialogWarningID SaveChanges = new DialogWarningID("YesNoCancel");
		public static final DialogWarningID SendLink = new DialogWarningID("css=div[class=DwtConfirmDialog]");
		public static final DialogWarningID DeleteTagWarningMessage = new DialogWarningID("YesNoMsgDialog");
		public static final DialogWarningID EmptyFolderWarningMessage = new DialogWarningID("OkCancel");
		public static final DialogWarningID SaveSignatureChangeMessage = new DialogWarningID("YesNoCancel");
		public static final DialogWarningID CancelCreateContact = new DialogWarningID("YesNoCancel");
		public static final DialogWarningID PermanentlyDeleteTheItem = new DialogWarningID("OkCancel");
		public static final DialogWarningID PermanentlyRemoveTheAttachment = new DialogWarningID("YesNoMsgDialog");
		public static final DialogWarningID DeleteItemWithinRetentionPeriod = new DialogWarningID("OkCancel");
		public static final DialogWarningID DeleteAppointment = new DialogWarningID("YesNo");
		
		public static final DialogWarningID ComposeOptionsChangeWarning = new DialogWarningID("OkCancel");
		
		// See bug: http://bugzilla.zimbra.com/show_bug.cgi?id=63353
		// In main, the dialog id is <div id='OkCancel' .../>
		// In 8.x, the dialog id is <div id='ShowDelayPastDialog' .../>
		// public static final DialogWarningID SelectedTimeIsInPast = new DialogWarningID("ShowDelayPastDialog");
		public static final DialogWarningID SelectedTimeIsInPast = new DialogWarningID("OkCancel");
		
		// See http://bugzilla.zimbra.com/show_bug.cgi?id=64081
		public static final DialogWarningID SendReadReceipt = new DialogWarningID("YesNoMsgDialog");;
		public static final DialogWarningID QuickCommandConfirmDelete = new DialogWarningID("ZmQuickCommandConfirmation1");
		public static final DialogWarningID PreferencesSaveChanges = new DialogWarningID("YesNoCancel");
		public static final DialogWarningID SwitchingToTextWillDiscardHtmlFormatting = new DialogWarningID("css=div[id$='_formatWarning']");
		public static final DialogWarningID SmsVerificationCodeSent = new DialogWarningID("ZmMsgDialog");
		public static final DialogWarningID ZmAcceptShare = new DialogWarningID("ZmAcceptShare");
	   	public static final DialogWarningID ConflictResource = new DialogWarningID("RESC_CONFLICT_DLG");
	   	public static final DialogWarningID DisableTwoStepAuthentication = new DialogWarningID("YesNoMsgDialog");
	   	public static final DialogWarningID RevokeTrustedDevice = new DialogWarningID("OkCancel_title");
		protected String Id;
		public DialogWarningID(String id) {
			Id = id;
		}
	}
	
	protected String MyDivId = null;
	
	
	public DialogWarning(DialogWarningID dialogId, AbsApplication application, AbsTab tab) {
		super(application, tab);
		
		// Remember which div this object is pointing at
		/*
		 * Example:
		 * <div id="YesNoCancel" style="position: absolute; overflow: visible; left: 229px; top: 208px; z-index: 700;" class="DwtDialog" parentid="z_shell">
		 *   <div class="DwtDialog WindowOuterContainer">
		 *   ...
		 *   </div>
		 * </div>
		 */
		MyDivId = dialogId.Id;
		
		logger.info("new " + DialogWarning.class.getCanonicalName());
		logger.info("mydivid is" + MyDivId);
	}
	
	public String zGetWarningTitle() throws HarnessException {
		String locator = "css=div[id='"+ MyDivId +"'] td[id='"+ MyDivId +"_title']";
		return (zGetDisplayedText(locator));
	}
	
	public String zGetWarningContent() throws HarnessException {	
		//String locator = "css=div[id='YesNoCancel_content']";
		String locator = "css=td[id^='MessageDialog'][class='DwtMsgArea']";
		return (zGetDisplayedText(locator));
	}
	
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public AbsPage zClickButton(Button button) throws HarnessException {
		if ( button == null )
			throw new HarnessException("button cannot be null");

		String locator = null;
		AbsPage page = null; 		// Does this ever result in a page being returned?

		// See http://bugzilla.zimbra.com/show_bug.cgi?id=54560
		// Need unique id's for the buttons
		String buttonsTableLocator = "css=div[id='"+ MyDivId +"'] div[id$='_buttons']";

		if ( button == Button.B_YES ) {

			locator = buttonsTableLocator + " td[id$='_button5_title']";

			if(MyDivId.contains("css=div[class=DwtConfirmDialog]")){
				page = 	new FormMailNew(this.MyApplication);
			}


		} else if ( button == Button.B_NO ) {

			locator = buttonsTableLocator + " td[id$='_button4_title']";

		} else if ( button == Button.B_CANCEL ) {

			locator = buttonsTableLocator + " td[id$='_button1_title']";

		} else if (button == Button.B_OK) {

			locator = buttonsTableLocator + " td[id$='_button2_title']";

		} else if (button == Button.B_REVOKE) {

			locator = "css=div[id='OkCancel'] div[id='OkCancel_button2'] td[id$='_button2_title']:contains('Revoke')";
			
		}else if (button == Button.B_SAVE_WITH_CONFLICT) {

			locator = "css= div[id^='RESC_CONFLICT_DLG_button'] td[id^='RESC_CONFLICT_DLG_']:contains('Save')";

		}else if (button == Button.B_CANCEL_CONFLICT) {

			locator = "css= div[id^='RESC_CONFLICT_DLG_button'] td[id^='RESC_CONFLICT_DLG_']:contains('Cancel')";

		}  else {
			throw new HarnessException("no logic defined for button "+ button);
		}

		sClickAt(locator,"0,0");

		zWaitForBusyOverlay();

		if ( page != null ) {
			page.zWaitForActive();

		}
		
		Stafpostqueue sp = new Stafpostqueue();
		sp.waitForPostqueue();

		return (page);
	}

	@Override
	public String zGetDisplayedText(String locator) throws HarnessException {
		if ( locator == null )
			throw new HarnessException("locator cannot be null");
		
		if ( !this.sIsElementPresent(locator) )
			throw new HarnessException("locator cannot be found");
		
		return (this.sGetText(locator));
		
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		String dialogPresent = "css=div[id='" + MyDivId + "'][style*='display: block;']";
		if ( !this.sIsElementPresent(dialogPresent) )
			return (false);
		
		// mountpionts.viewer.FlagMail seems to keep failing on this dialog, even
		// though the PERM_DENIED dialog is showing correctly
		//
		// 7.X: 		if ( !this.zIsVisiblePerPosition(MyDivId, 225, 300) )
		// 8.X: dev says any dialogs with non-negative positions should be visible, so using (0,0)
		//
		if ( !this.zIsVisiblePerPosition(dialogPresent, 0, 0) )
			return (false);
		
		return (true);
	}

}
