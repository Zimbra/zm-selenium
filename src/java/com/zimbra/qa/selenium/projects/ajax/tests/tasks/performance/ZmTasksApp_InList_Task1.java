/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.tasks.performance;

import java.io.File;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.RestUtil;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.performance.PerfKey;
import com.zimbra.qa.selenium.framework.util.performance.PerfMetrics;
import com.zimbra.qa.selenium.framework.util.performance.PerfToken;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;

public class ZmTasksApp_InList_Task1 extends AjaxCommonTest {

   public ZmTasksApp_InList_Task1() {
      logger.info("New " + ZmTasksApp_InList_Task1.class.getCanonicalName());
      super.startingPage = app.zPageMail;
   }

   @DataProvider(name = "DataProvider_LoadingApp_1Task")
   public Object[][] DataProvideNewMessageShortcuts() {
     return new Object[][] {
           new Object[] { "Load (initial) the Tasks app, 1 task in list"},
           new Object[] { "Load (from cache) the Tasks app, 1 task in list"}
     };
   }


   @Test( description = "Measure the time to load Tasks page with 1 task",
         groups = {"performance", "deprecated"}, dataProvider = "DataProvider_LoadingApp_1Task")

   public void ZmTasksApp_01(String logMessage) throws HarnessException {

		String subject = "task"+ ConfigProperties.getUniqueString();

		app.zGetActiveAccount().soapSend(
            "<CreateTaskRequest xmlns='urn:zimbraMail'>" +
            "<m >" +
            "<inv>" +
            "<comp name='"+ subject +"'>" +
            "<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
            "</comp>" +
            "</inv>" +
            "<su>"+ subject +"</su>" +
            "<mp ct='text/plain'>" +
            "<content>content"+ ConfigProperties.getUniqueString() +"</content>" +
            "</mp>" +
            "</m>" +
		"</CreateTaskRequest>");

		PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmTasksApp, logMessage);
		app.zPageContacts.zClickAt("css=td[id='zb__App__Tasks_title']","");
		PerfMetrics.waitTimestamp(token);

		// Wait for the app to load
		app.zPageTasks.zWaitForActive();
   }


   @Test( description="Measure the time to load Tasks page with 100 tasks",
         groups={"performance", "deprecated"})

   public void ZmTasksApp_02() throws HarnessException {

		// Import 100 appointments using Tasks.ics and REST to speed up the setup
		String filename = ConfigProperties.getBaseDirectory() + "/data/public/ics/100tasks.ics";

		RestUtil rest = new RestUtil();
		rest.setAuthentication(app.zGetActiveAccount());
		rest.setPath("/service/home/~/Tasks");
		rest.setQueryParameter("fmt", "ics");
		rest.setUploadFile(new File(filename));
		rest.doPost();

		PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmTasksApp, "Load the Tasks app, 100 tasks in list");
		app.zPageContacts.zClickAt("css=td[id='zb__App__Tasks_title']","");
		PerfMetrics.waitTimestamp(token);

		// Wait for the app to load
		app.zPageTasks.zWaitForActive();
   }
}