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
/**
 * 
 */
package com.zimbra.qa.selenium.projects.zcs.ui;

/**
 * This abstract class is used to define end-user actions.
 * 
 * In the Zimbra application, there are several methods to
 * create, modify, and delete objects.  This class defines
 * those methods.
 * 
 * 
 * For example, to create a new contact you can:
 * 1. Click "New" - "Contact"
 * 2. Right click on email address -> "Add to contacts"
 * 3. Right click on folder -> "New Contact"
 * 4. etc.
 * 
 * @author Matt Rhoades
 *
 */
public class ActionMethod  {
	
	/**
	 * Apply the default method
	 */
	public static final ActionMethod DEFAULT = new ActionMethod("Default");
	
	
	protected String myMethod = null;
	protected ActionMethod(String method) {
		myMethod = method;
	}

	@Override
	public boolean equals(Object other) {
		if ( this == other )
			return (true);
		if ( !(other instanceof ActionMethod) )
			return (false);
		ActionMethod o = (ActionMethod)other;
		return (myMethod.equals(o.myMethod));
	}
	
	@Override
	public int hashCode() {
		return(myMethod.hashCode());
	}
}
