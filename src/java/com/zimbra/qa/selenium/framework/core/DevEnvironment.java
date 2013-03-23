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
package com.zimbra.qa.selenium.framework.core;

/**
 * The DevEnvironment class defines whether the harness is running in
 * the Zimbra Dev Environment
 * @author Matt Rhoades
 *
 */
public class DevEnvironment {

	/**
	 * Configure the harness for the dev environment
	 * @param flag true - if in dev environment, false otherwise
	 */
	public static void setDevEnvironment(boolean flag) {
		getSingleton().isDevEnvironment = flag;
	}
	
	public static boolean isUsingDevEnvironment() {
		return (getSingleton().isDevEnvironment);
	}
	
	private boolean isDevEnvironment = false;
	
	/**
	 * Singleton
	 */

    private volatile static DevEnvironment singleton;
 
    private DevEnvironment() {
    }
 
    public static DevEnvironment getSingleton() {
        if(singleton==null) {
            synchronized(DevEnvironment.class) {
                if(singleton == null) {
                    singleton = new DevEnvironment();
                }
            }
        }
        return singleton;
    }
}
