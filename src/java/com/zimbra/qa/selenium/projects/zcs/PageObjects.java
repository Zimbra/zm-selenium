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
package com.zimbra.qa.selenium.projects.zcs;

import com.zimbra.qa.selenium.projects.zcs.ui.ABApp;
import com.zimbra.qa.selenium.projects.zcs.ui.ABCompose;
import com.zimbra.qa.selenium.projects.zcs.ui.AccPref;
import com.zimbra.qa.selenium.projects.zcs.ui.BriefcaseApp;
import com.zimbra.qa.selenium.projects.zcs.ui.CalApp;
import com.zimbra.qa.selenium.projects.zcs.ui.CalCompose;
import com.zimbra.qa.selenium.projects.zcs.ui.ComposeView;
import com.zimbra.qa.selenium.projects.zcs.ui.DocumentApp;
import com.zimbra.qa.selenium.projects.zcs.ui.DocumentCompose;
import com.zimbra.qa.selenium.projects.zcs.ui.FilterPref;
import com.zimbra.qa.selenium.projects.zcs.ui.GeneralPrefUI;
import com.zimbra.qa.selenium.projects.zcs.ui.LoginPage;
import com.zimbra.qa.selenium.projects.zcs.ui.MailApp;
import com.zimbra.qa.selenium.projects.zcs.ui.Sharing;
import com.zimbra.qa.selenium.projects.zcs.ui.SignaturePref;
import com.zimbra.qa.selenium.projects.zcs.ui.TaskApp;

public class PageObjects {
	public static LoginPage zLoginpage = new LoginPage();
	public static ComposeView zComposeView = new ComposeView();
	public static MailApp zMailApp = new MailApp();
	public static BriefcaseApp zBriefcaseApp = new BriefcaseApp();
	public static CalApp zCalApp = new CalApp();
	public static CalCompose zCalCompose = new CalCompose();
	public static TaskApp zTaskApp = new TaskApp();
	public static ABApp zABApp = new ABApp();
	public static ABCompose zABCompose = new ABCompose();
	public static DocumentCompose zDocumentCompose = new DocumentCompose();
	public static DocumentApp zDocumentApp = new DocumentApp();
	public static Sharing zSharing = new Sharing();
	public static FilterPref zFilterPreferences = new FilterPref();
	public static AccPref zAccPref = new AccPref();
	public static GeneralPrefUI zGenPrefUI = new GeneralPrefUI();
	public static SignaturePref zSignaturePref = new SignaturePref();
}
