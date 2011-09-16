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
package com.zimbra.qa.selenium.projects.html2.clients;

import com.zimbra.qa.selenium.framework.core.*;
import com.zimbra.qa.selenium.framework.util.HarnessException;




/**
 * @author raodv
 *
 */
public class Folder extends ZObject {

	public Folder() {
		super("folderCore_html", "Folder");
	} 

	public  String ZObjectCore(String folderNameseparatedBySlash, String action, Boolean retryOnFalse,
			String panel, String param1)  throws HarnessException  {
		String rc = "false";
		String[] fldrs = folderNameseparatedBySlash.split("/");

		for (int i = 0; i < fldrs.length; i++) {
			String currentFolder = fldrs[i];
			//dont wait if we are checking for not exist
			if(!action.equals("notexists"))
			    zWait(currentFolder, panel, param1);
			if (i < fldrs.length-1){
				this._expndFldrIfRequired(currentFolder, panel, param1);
				continue;
			}
			rc = ClientSessionFactory.session().selenium().call("folderCore_html",  currentFolder, action, retryOnFalse, panel, param1);
		}
		return rc;		
	}	
	
	public  void zExpand(String folder)  throws HarnessException  {
		ClientSessionFactory.session().selenium().call("folderCore_html",  folder+"_expand", "click", true, "", "");
	}

	public  void zCollapse(String folder) throws HarnessException   {
		ClientSessionFactory.session().selenium().call("folderCore_html",  folder+"_collapse", "click", true, "", "");
	}

	/**
	 * Clicks on the edit-link on folder-headers
	 * @param folder
	 */
	public  void zEdit(String folder)  throws HarnessException  {
		ClientSessionFactory.session().selenium().call("folderCore_html",  folder+"_edit", "click", true, "", "");
	}	


	
	
	private  void _expndFldrIfRequired(String folder, String panel, String param1) throws HarnessException   {
	//	String rc = selenium.call("this.doZfolderExpandBtnExists("
	//			+ doubleQuote + folder + doubleQuote + ")");
		String rc = ClientSessionFactory.session().selenium().call("folderCore_html",  folder+"_expand", "exists", true, panel, param1);
		if(rc.equals("true"))
			ClientSessionFactory.session().selenium().call("folderCore_html",  folder+"_expand", "click", true, panel, param1);
	}	

}