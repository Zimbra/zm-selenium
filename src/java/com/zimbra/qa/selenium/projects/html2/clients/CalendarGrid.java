/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013 Zimbra Software, LLC.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.4 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.html2.clients;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;




 
public class CalendarGrid extends ZObject {
	public CalendarGrid() {
		super("calGridCore_html", "HTML Calendar Grid");
	} 
	public String zGetApptDateTime(String apptName) throws HarnessException   {
		String res= ClientSessionFactory.session().selenium().call("calGridCore_html", apptName, "getDT", true, "", "");
		return res.replace("  ","");
	}
	public String zGetApptCount(String apptName)  throws HarnessException  {
		return ClientSessionFactory.session().selenium().call("calGridCore_html", apptName, "getCount", true, "", "");

	}	
}	