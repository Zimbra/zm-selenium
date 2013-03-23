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
package com.zimbra.qa.selenium.projects.html2;

import com.zimbra.qa.selenium.projects.html2.ui.ABComposeHtml;
import com.zimbra.qa.selenium.projects.html2.ui.AccPref;
import com.zimbra.qa.selenium.projects.html2.ui.CalFolderApp;
import com.zimbra.qa.selenium.projects.html2.ui.CalendarApp;
import com.zimbra.qa.selenium.projects.html2.ui.ComposePrefUI;
import com.zimbra.qa.selenium.projects.html2.ui.ComposeView;
import com.zimbra.qa.selenium.projects.html2.ui.GeneralPrefUI;
import com.zimbra.qa.selenium.projects.html2.ui.LoginPage;
import com.zimbra.qa.selenium.projects.html2.ui.MailApp;
import com.zimbra.qa.selenium.projects.html2.ui.MailPrefUI;
import com.zimbra.qa.selenium.projects.html2.ui.TaskApp;


public class PageObjects {
	public static LoginPage zLoginpage = new LoginPage();
	public static ComposeView zComposeView = new ComposeView();
	public static MailApp zMailApp = new MailApp();
	public static ABComposeHtml zABComposeHTML = new ABComposeHtml();
	public static TaskApp zTaskApp = new TaskApp();
	public static CalendarApp zCalendarApp = new CalendarApp();
	public static CalFolderApp zCalFolderApp = new CalFolderApp();
	public static AccPref zAccPref = new AccPref();
	public static GeneralPrefUI zGeneralPrefUI = new GeneralPrefUI();
	public static MailPrefUI zMailPrefUI = new MailPrefUI();
	public static ComposePrefUI zComposePrefUI = new ComposePrefUI();
}
