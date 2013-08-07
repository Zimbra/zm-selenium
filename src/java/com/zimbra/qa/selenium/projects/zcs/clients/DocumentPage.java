/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013 Zimbra Software, LLC.
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
package com.zimbra.qa.selenium.projects.zcs.clients;

import com.zimbra.qa.selenium.framework.util.HarnessException;

/**
 * @author raodv
 *
 */


public class DocumentPage extends ZObject {
	public DocumentPage() {
		super("documentCore", "Document TOC");
	} 
	
	/* Clicks on a document  page in Documents's Table Of Contents
	 * @pageName pageName
	 */	
	public void zClick(String pageName)  throws HarnessException  {
		ZObjectCore(pageName, "click", true, "", "");
	}

	/* Clicks on a link(Edit, History, Delete etc links) for a given page in Documents's Table Of Contents
	 * @pageName pageName
	 * @linkNameInPage link(Edit, History, Delete etc links)
	 */
	public void zClick(String pageName, String linkNameInPage)  throws HarnessException  {
		ZObjectCore(pageName, "click", true, linkNameInPage, "");
	}
	
}	