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
package com.zimbra.qa.selenium.projects.universal.tests.zimlets.phone;

import java.util.HashMap;
import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.XmlStringUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.projects.universal.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.universal.ui.mail.DisplayMail;
import com.zimbra.qa.selenium.projects.universal.ui.mail.DisplayMail.Field;

public class DisableZimlet extends AjaxCommonTest {

   @SuppressWarnings("serial")
   public DisableZimlet() {
      logger.info("New "+ DisableZimlet.class.getCanonicalName());

      // All tests start at the login page
      super.startingPage = app.zPageMail;

      // Make sure we are using an account with message view
      super.startingAccountPreferences = new HashMap<String, String>() {{
    	  put("zimbraPrefGroupMailBy", "message");
    	  put("zimbraPrefMessageViewHtmlPreferred", "TRUE");
      }};

      super.startingUserZimletPreferences = new HashMap<String, String>() {{
    	  put("com_zimbra_phone", "disabled");
      }};
   }

   @Test( description = "Receive a text mail - verify numeric mail contents is not converted into phone number hyperlink",
         groups = { "functional", "L2" })

   public void DisableZimlet_01() throws HarnessException {

      // Create the message data to be sent
      String subject = "subject" + ConfigProperties.getUniqueString();

      ZimbraAccount.AccountA().soapSend(
               "<SendMsgRequest xmlns='urn:zimbraMail'>" +
                  "<m>" +
                     "<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
                     "<e t='c' a='"+ ZimbraAccount.AccountB().EmailAddress +"'/>" +
                     "<su>"+ subject +"</su>" +
                     "<mp ct='text/plain'>" +
                        "<content>"+ ConfigProperties.getUniqueString() +"</content>" +
                     "</mp>" +
                  "</m>" +
               "</SendMsgRequest>");

      // Get all the SOAP data for later verification
      MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");

      // Refresh current view
   	  ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

      // Select the message so that it shows in the reading pane
      DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

      // Verify the To, From, Subject, Body
      ZAssert.assertEquals(actual.zGetMailProperty(Field.Subject), mail.dSubject, "Verify the subject matches");
      ZAssert.assertNotNull(actual.zGetMailProperty(Field.ReceivedDate), "Verify the date is displayed");
      ZAssert.assertNotNull(actual.zGetMailProperty(Field.ReceivedTime), "Verify the time is displayed");
      ZAssert.assertEquals(actual.zGetMailProperty(Field.From), ZimbraAccount.AccountA().EmailAddress, "Verify the From matches");
      ZAssert.assertEquals(actual.zGetMailProperty(Field.Cc), ZimbraAccount.AccountB().EmailAddress, "Verify the Cc matches");
      ZAssert.assertEquals(actual.zGetMailProperty(Field.To), app.zGetActiveAccount().EmailAddress, "Verify the To matches");

      // The body could contain HTML, even though it is only displaying text (e.g. <br> may be present)
      // do a contains, rather than equals.
      ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), mail.dBodyText, "Verify the body matches");
      ZAssert.assertStringDoesNotContain(actual.zGetMailProperty(Field.Body), "<span", "Ensure that the body doesn't contain <span from phone zimlet");

   }

   @Test(   description = "Receive an html mail - verify numeric mail contents is not converted into phone number hyperlink",
         groups = { "functional", "L3" })

   public void DisableZimlet_02() throws HarnessException {

      // Create the message data to be sent
      String subject = "subject" + ConfigProperties.getUniqueString();
      String bodyText = "text" + ConfigProperties.getUniqueString();
      String bodyHTML = "text <strong>bold"+ ConfigProperties.getUniqueString() +"</strong> text";
      String contentHTML = XmlStringUtil.escapeXml(
         "<html>" +
            "<head></head>" +
            "<body>"+ bodyHTML +"</body>" +
         "</html>");

      ZimbraAccount.AccountA().soapSend(
               "<SendMsgRequest xmlns='urn:zimbraMail'>" +
                  "<m>" +
                     "<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
                     "<e t='c' a='"+ ZimbraAccount.AccountB().EmailAddress +"'/>" +
                     "<su>"+ subject +"</su>" +
                     "<mp ct='multipart/alternative'>" +
                        "<mp ct='text/plain'>" +
                           "<content>" + bodyText +"</content>" +
                        "</mp>" +
                        "<mp ct='text/html'>" +
                           "<content>"+ contentHTML +"</content>" +
                        "</mp>" +
                     "</mp>" +
                  "</m>" +
               "</SendMsgRequest>");

      MailItem mail = MailItem.importFromSOAP(app.zGetActiveAccount(), "subject:("+ subject +")");

      // Refresh current view
   	  ZAssert.assertTrue(app.zPageMail.zVerifyMailExists(subject), "Verify message displayed in current view");

      // Select the message so that it shows in the reading pane
      DisplayMail actual = (DisplayMail) app.zPageMail.zListItem(Action.A_LEFTCLICK, mail.dSubject);

      // Verify the To, From, Subject, Body
      ZAssert.assertEquals(actual.zGetMailProperty(Field.Subject), mail.dSubject, "Verify the subject matches");
      ZAssert.assertNotNull(actual.zGetMailProperty(Field.ReceivedDate), "Verify the date is displayed");
      ZAssert.assertNotNull(actual.zGetMailProperty(Field.ReceivedTime), "Verify the time is displayed");
      ZAssert.assertEquals(actual.zGetMailProperty(Field.From), ZimbraAccount.AccountA().EmailAddress, "Verify the From matches");
      ZAssert.assertEquals(actual.zGetMailProperty(Field.Cc), ZimbraAccount.AccountB().EmailAddress, "Verify the Cc matches");
      ZAssert.assertEquals(actual.zGetMailProperty(Field.To), app.zGetActiveAccount().EmailAddress, "Verify the To matches");
      ZAssert.assertStringContains(actual.zGetMailProperty(Field.Body), bodyHTML, "Verify the body matches");
      ZAssert.assertStringDoesNotContain( actual.zGetMailProperty(Field.Body), "<span", "Ensure that the body doesn't contain <span from phone zimlet");
   }
}
