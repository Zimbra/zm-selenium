/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.core;

import java.util.*;

/**
 * A base class that sets
 *  1) the starting page to be the calendar app
 *  2) sets zimbraPrefCalendarInitialView=WorkWeek (default)
 *  3) sets weekDayUTC ... a Calendar object, on current week day
 *  
 * @author Matt Rhoades
 *
 */
public class CalendarWorkWeekTest extends AjaxCommonTest {
	
	public static boolean organizerTest = false;
	public static boolean allDayTest = false;
	
	protected Calendar calendarWeekDayUTC = null;
	
	public CalendarWorkWeekTest() {
		
		super.startingPage = app.zPageCalendar;

		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = -109947857488617841L;
		{
		    put("zimbraPrefCalendarInitialView", "workWeek");
		}};
		
		calendarWeekDayUTC = Calendar.getInstance();
		
		if ( calendarWeekDayUTC.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY ) {
			
			// If UTC is Friday, tests may fail if the TZ offset
			// puts the date into Saturday.  Move the time to Thursday
			// just to be safe.
			
			calendarWeekDayUTC.add(Calendar.HOUR, -24);
			
		} else if ( calendarWeekDayUTC.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ) {
			
			// Change Saturdays to Thursday.
			
			calendarWeekDayUTC.add(Calendar.HOUR, -48);
			
		} else if ( calendarWeekDayUTC.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ) {
			
			// Change Sundays to Tuesday.
			
			calendarWeekDayUTC.add(Calendar.HOUR, 48);
			
		} else if ( calendarWeekDayUTC.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY ) {
			
			// If UTC is Monday, tests may fail if the TZ offset
			// puts the date into Sunday.  Move the time to Tuesday
			// just to be safe.
			
			
			calendarWeekDayUTC.add(Calendar.HOUR, 24);
			
		}
	}
	
}
