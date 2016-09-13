/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2016 Synacor, Inc.
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

import org.apache.log4j.*;

import com.zimbra.qa.selenium.framework.ui.AbsApplication;
import com.zimbra.qa.selenium.framework.util.HarnessException;

public class TooltipImage extends Tooltip {
	protected static Logger logger = LogManager.getLogger(TooltipImage.class);

	
	/*
	 * Example DOM:
	 * 
	 *  <div class="DwtToolTip" style="position: absolute; z-index: 775; left: -10000px; top: -10000px;">
	 * 	 <div id="tooltipContents" class="DwtToolTipBody">
	 *    <img id="DWT81" width="110" style="visibility: visible;" src="https://server/service/home/~/?auth=co&loc=en_US&id=259&part=2">
	 *   </div>
	 *  </div>
	 *  
	 */
	
	
	public static class Locators {
	
	}
	
	public static class Field {
		
		public static final Field URL = new Field("URL");
		
		
		private String field;
		private Field(String name) {
			field = name;
		}
		
		@Override
		public String toString() {
			return (field);
		}

	}

	public TooltipImage(AbsApplication application) {
		super(application);

		logger.info("new " + this.getClass().getCanonicalName());

	}

	public String zGetField(Field field) throws HarnessException {
		
		String locator = null;
		String value = null;
		
		if ( field == Field.URL ) {

			locator = Tooltip.Locators.DwtToolTipCSS + " img";
			if ( !this.sIsElementPresent(locator) ) {
				throw new HarnessException("Unable to find image source "+ locator);
			}
			
			value = this.sGetAttribute(locator + "@src");
			return (value);
			
		} else {
			
			throw new HarnessException("implement me: "+ field);
			
		}
		
	}
		
}
