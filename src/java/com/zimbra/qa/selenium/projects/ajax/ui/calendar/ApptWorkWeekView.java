package com.zimbra.qa.selenium.projects.ajax.ui.calendar;




import java.util.Calendar;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.projects.ajax.ui.ApptView;

public class ApptWorkWeekView extends ApptView {
	
	protected ApptWorkWeekView(AbsApplication application) {
		super(application);
		
		logger.info("new " + ApptWorkWeekView.class.getCanonicalName());
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
		
		Calendar calendarWeekDayUTC = Calendar.getInstance();
		
		if ( calendarWeekDayUTC.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendarWeekDayUTC.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			return (sIsElementPresent("css=div[id='zb__CLD__WORK_WEEK_VIEW'][class*='ZSelected']"));
			
		} else {
			
			// sGetCssCount("css=div.ZmCalViewMgr>div.div.calendar_heading div.calendar_heading_day") == 0
		    return (sGetCssCount("css=div.calendar_heading>div.calendar_heading_day_today") >= 1);
		}
		
	}
	
}
