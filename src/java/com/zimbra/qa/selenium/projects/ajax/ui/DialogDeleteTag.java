/*
 * ***** BEGIN LICENSE BLOCK *****
 * 
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012 VMware, Inc.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.3 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * 
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.ui;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.ui.AbsDialog;
import com.zimbra.qa.selenium.framework.ui.AbsPage;
import com.zimbra.qa.selenium.framework.ui.AbsTab;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.FormMailNew;

public class DialogDeleteTag extends AbsDialog {

	public static class DialogDeleteTagID {
		public static final DialogDeleteTagID DeleteTag = new DialogDeleteTagID("YesNo");
		
		protected String Id;
		public DialogDeleteTagID(String id) {
			Id = id;
		}
	}
	
	protected String MyDivId = null;
	
	
	public DialogDeleteTag(DialogDeleteTagID dialogId, AbsApplication application, AbsTab tab) {
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
		
		logger.info("new " + DialogDeleteTag.class.getCanonicalName());

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

		if ( button == Button.B_YES ) {

			locator = "css=div [id='YesNoMsgDialog_button5'] td[id='YesNoMsgDialog_button5_title']";

			if(MyDivId.contains("css=div[class=DwtConfirmDialog]")){
				page = 	new FormMailNew(this.MyApplication);
			}

		} else if ( button == Button.B_NO ) {

			locator = "css=div [id='YesNoMsgDialog_button4'] td[id='YesNoMsgDialog_button4_title']";

		} else {
			throw new HarnessException("no logic defined for button "+ button);
		}

		if ( locator == null ) {
			throw new HarnessException("locator was null for button "+ button);
		}

		// Default behavior, process the locator by clicking on it
		//

		// Click it
		zClickAt(locator,"0,0");

		// If the app is busy, wait for it to become active
		zWaitForBusyOverlay();

		// If page was specified, make sure it is active
		if ( page != null ) {

			// This function (default) throws an exception if never active
			page.zWaitForActive();

		}

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
		
		if ( !this.sIsElementPresent(MyDivId) )
			return (false);
		
		// mountpionts.viewer.FlagMail seems to keep failing on this dialog, even
		// though the PERM_DENIED dialog is showing correctly
		//
		// 7.X: 		if ( !this.zIsVisiblePerPosition(MyDivId, 225, 300) )
		// 8.X: dev says any dialogs with non-negative positions should be visible, so using (0,0)
		//
		if ( !this.zIsVisiblePerPosition(MyDivId, 0, 0) )
			return (false);
		
		return (true);
	}

}
