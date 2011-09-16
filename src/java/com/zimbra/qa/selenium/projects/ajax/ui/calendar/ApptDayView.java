/*
 * ***** BEGIN LICENSE BLOCK *****
 * 
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011 VMware, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.ui.calendar;





import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.projects.ajax.ui.ApptView;

public class ApptDayView extends ApptView {
	
	protected ApptDayView(AbsApplication application) {
		super(application);
		
		logger.info("new " + ApptDayView.class.getCanonicalName());
	}
	
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}
	
	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zDisplayPressButton("+ button +")");
		
		tracer.trace("Click "+ button);

		throw new HarnessException("no logic defined for button: "+ button);
		
	}
	
	public boolean isApptExist(AppointmentItem appt) throws HarnessException {
	  return super.isApptExist(appt);
	}
	@Override
	public boolean zIsActive() throws HarnessException {
        return 
        (
         //TODO
        //( sGetCssCount("css=div.ZmCalViewMgr>div.div.calendar_heading div.calendar_heading_day") == 0)
        //&&		
        (sGetCssCount("css=div.calendar_heading>div.calendar_heading_day_today") >= 1)
        );
						
	}
	
}
