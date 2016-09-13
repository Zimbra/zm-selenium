/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.ui;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;

public class Tooltip extends AbsTooltip {
	protected static Logger logger = LogManager.getLogger(Tooltip.class);

	public static class Locators {
	
		public static final String DwtToolTipId = "DwtToolTip";
		public static final String DwtToolTipCSS = "css=div[class='DwtToolTip']";
		
		public static final String TooltipContentsId = "tooltipContents";
		public static final String TooltipContentsCSS = "css=div[class='DwtToolTip'] div[id='tooltipContents']";
		
		
	}
	
	public Tooltip(AbsApplication application) {	
		super(application);
		
		logger.info("new " + this.getClass().getCanonicalName());
	}
	
	public String zGetContents() 
	throws HarnessException 
	{
		logger.info(myPageName() + " zGetContents()");

		return (this.sGetText(Locators.TooltipContentsCSS));
	}
	
	
	public boolean zIsActive() 
	throws HarnessException
	{
		logger.info(myPageName() + " zIsVisible()");
		
		boolean present = this.sIsElementPresent(Locators.DwtToolTipCSS);
		if ( !present )
			return (false);
		
		boolean visible = this.zIsVisiblePerPosition(Locators.DwtToolTipCSS, 0, 0);
		if ( !visible )
			return (false);
		
		return (true);
		
	}

	@Override
	public String myPageName() {
		return (this.getClass().getCanonicalName());
	}
	
}
