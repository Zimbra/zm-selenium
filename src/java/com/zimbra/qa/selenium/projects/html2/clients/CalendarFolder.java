/*
 * ***** BEGIN LICENSE BLOCK *****
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
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.html2.clients;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;




/**
 * @author raodv
 * Essentially same as Folder in html-client but with some extra apis like zCheck zunCheck etc.
 */
public class CalendarFolder extends Folder {

	public CalendarFolder() {
		super();
	} 
	/**
	 * Check Calendar folder
	 * @param folder
	 */
	public  void zCheck(String folder) throws HarnessException   {
		ClientSessionFactory.session().selenium().call("folderCore_html",  folder+"_check", "click", true, "", "");
	}	
	/**
	 * unchecks Calendar folder
	 * @param folder
	 */
	public  void zUnCheck(String folder) throws HarnessException   {
		ClientSessionFactory.session().selenium().call("folderCore_html",  folder+"_uncheck", "click", true, "", "");
	}		

	/**
	 * Checks if Calendar folder's checkbox is unchecked
	 * @param folder
	 */
	public  String zIsUnChecked(String folder)  throws HarnessException  {
		return ClientSessionFactory.session().selenium().call("folderCore_html",  folder+"_uncheck", "exists", true, "", "");
	}

	/**
	 * Checks if Calendar folder's checkbox is checked
	 * @param folder
	 */
	public  String zIsChecked(String folder)  throws HarnessException  {
		return ClientSessionFactory.session().selenium().call("folderCore_html",  folder+"_check", "exists", true, "", "");
	}	
}