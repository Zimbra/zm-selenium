/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.performance;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.performance.PerfKey;
import com.zimbra.qa.selenium.framework.util.performance.PerfMetrics;
import com.zimbra.qa.selenium.framework.util.performance.PerfToken;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCore;

public class ZmContactsItem_BasicContact1 extends AjaxCore{

   public ZmContactsItem_BasicContact1() {
      logger.info("New " + ZmContactsItem_BasicContact1.class.getCanonicalName());
      super.startingPage = app.zPageContacts;
   }


   @Test (description = "Measure the time to view Basic contact item",
         groups = { "performance", "deprecated" })

   public void ZmContactsItem_01() throws HarnessException {

      ContactItem.createContactItem(app.zGetActiveAccount());
      ContactItem contactItem = ContactItem.createContactItem(app.zGetActiveAccount());

      FolderItem contactFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(), "Contacts");

      // Refresh the contact list
      app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, contactFolder);
      PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmContactsItem, "Load the basic contact view");

      app.zPageContacts.sClickAt("css=div[id='zv__CNS-main'] li[id^='zli__CNS-main'] div[id$='__fileas']:contains('"+contactItem.fileAs+"')","");
      PerfMetrics.waitTimestamp(token);
   }
}