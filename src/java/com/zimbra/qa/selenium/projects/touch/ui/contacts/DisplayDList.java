/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.touch.ui.contacts;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import java.util.*;


public class DisplayDList extends AbsDisplay {
	public static String ALPHABET_PREFIX = "css=table[id$='alphabet'] td[_idx=";
	public static String ALPHABET_POSTFIX = "]";

	/**
	 * Defines Selenium locators for various objects in {@link DisplayDList}
	 */
	public static class Locators {
		public static final String zLocator = "xpath=//div[@class='ZmContactInfoView']";

	}

	/**
	 * The various displayed fields 
	 */
	public static enum Field {
     DisplayName,
     Message,
     Member
	}
	

	/**
	 * Protected constructor for this object.  Only classes within
	 * this package should create DisplayContact objects.
	 * 
	 * @param application
	 */
	protected DisplayDList(AbsApplication application) {
		super(application);
		
		logger.info("new " + DisplayDList.class.getCanonicalName());
	   
	}
	
	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zDisplayPressButton("+ button +")");
		
		tracer.trace("Click "+ button);
		
       	throw new HarnessException("implement me");
	}
	


	/**
	 * Get the string value of the specified field
	 * @return the displayed string value
	 * @throws HarnessException
	 */
	public String zGetContactProperty(Field field) throws HarnessException {
		logger.info("DisplayContactGroup.zGetContactProperty(" + field + ")");

		ArrayList<String> locatorArray = new ArrayList<String>();
	

		if ( field == Field.DisplayName ) {			
		  //locator = "xpath=//table[@class='contactHeaderTable NoneBg']/div[@class='contactHeader']";
		  locatorArray.add("css=table[class^='contactHeaderTable'][class$='ZPropertySheet'] div[class*='contactHeader']");
		}
		else if ( field == Field.Message ) {			
			  locatorArray.add("css=table[class^='contactHeaderTable'] td[id$='_subscriptionMsg']");
			}
		else if ( field == Field.Member ) {					   			
			getAllLocators(locatorArray);
		} 
		else {			
		  throw new HarnessException("no logic defined for field "+ field);			
		}
		
		String value = "";

		for (int i=0; i<locatorArray.size(); i++) {
           String locator = locatorArray.get(i);
           
			// Make sure something was set
		   if ( locator == null )
			  throw new HarnessException("locator was null for field = "+ field);
		
		   //SleepUtil.sleep(123456789);
		   // Make sure the element is present
		   if ( !sIsElementPresent(locator) )
			 throw new HarnessException("Unable to find the field = "+ field +" using locator = "+ locator);
		
		   // Get the element value
		    value += sGetText(locator).trim();		
		}
		
		logger.info("DisplayContactGroup.zGetContactProperty(" + field + ") = " + value);
		return(value);

		
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		return sIsElementPresent("css=div#zv__CNS-main");
	}
	

    private void getAllLocators(ArrayList<String> array) throws HarnessException {
  	   String css= "css=table[class='contactGroupTable'] tr";
 
  	   int count= this.sGetCssCount(css);

    	   for (int i=2; i<=count; i++) {
    		   String tdLocator=  css + ":nth-of-type(" + i + ")" + " span[id^='OBJ_PREFIX_DWT'][id$='_com_zimbra_emai']";
    		   if (sIsElementPresent(tdLocator)) {
    			   logger.info(tdLocator + " has text " + sGetText(tdLocator).trim());
    			   array.add(tdLocator);	    	 
    		   }
    	   }
    }

}
