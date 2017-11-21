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
package com.zimbra.qa.selenium.projects.universal.tests.contacts.performance;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.ContactItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.performance.PerfKey;
import com.zimbra.qa.selenium.framework.util.performance.PerfMetrics;
import com.zimbra.qa.selenium.framework.util.performance.PerfToken;
import com.zimbra.qa.selenium.projects.universal.core.UniversalCore;

public class ZmContactsItem_BasicContact1 extends UniversalCore{

   public ZmContactsItem_BasicContact1() {
      logger.info("New " + ZmContactsItem_BasicContact1.class.getCanonicalName());

      // All tests start at the login page
      super.startingPage = app.zPageContacts;

      // Make sure we are using an account with message view
      
   }

   @Test (description = "Measure the time to view Basic contact item",
         groups={"performance", "L4"})
   public void ZmContactsItem_01() throws HarnessException {
      // Create 2 contacts via Soap because by default the first one will be selected
      // therefore measuring the performance of loading the second one
      ContactItem.createContactItem(app.zGetActiveAccount());
      ContactItem contactItem = ContactItem.createContactItem(app.zGetActiveAccount());

      FolderItem contactFolder = FolderItem.importFromSOAP(
            app.zGetActiveAccount(), "Contacts");

      // Refresh the contact list
      app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, contactFolder);

      PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmContactsItem,
            "Load the basic contact view");

      // Select the contact
   //  app.zPageContacts.zListItem(Action.A_LEFTCLICK, contactItem.fileAs);;       
      app.zPageContacts.sClickAt("css=div[id='zv__CNS-main'] li[id^='zli__CNS-main'] div[id$='__fileas']:contains('"+contactItem.fileAs+"')","");
      PerfMetrics.waitTimestamp(token);

   }
}
