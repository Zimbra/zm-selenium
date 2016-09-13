/*
 * ***** BEGIN LICENSE BLOCK *****
 *
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
 *
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.framework.util;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class SendEmail
{
   public static void main(String [] args)
   {
      
	  // Sender's email ID needs to be mentioned
	  // String from = ConfigProperties.getConfigProperties().getString("emailFrom");
	  String from = "pnq-lab@zimbra.com";
	      
	  // Recipient's email ID needs to be mentioned.
      String to = ConfigProperties.getConfigProperties().getString("emailTo");

      // Assuming you are sending email from localhost
      String host = "mail.zimbra.com";

      // Get system properties
      Properties properties = System.getProperties();

      // Setup mail server
      properties.setProperty("mail.zimbra.com", host);
      properties.setProperty("mail.transport.protocol", "smtp");
      properties.setProperty("mail.host", "mail.zimbra.com");

      // Get the default Session object.
      Session session = Session.getDefaultInstance(properties);

      try {
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));

         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO,
                                  new InternetAddress(to));
         
         //message.addRecipients(Message.RecipientType.TO, "test@zimbra.com");

         // Set Subject: header field
         message.setSubject(args[0]);

         // Create the message part 
         BodyPart messageBodyPart = new MimeBodyPart();

         // Fill the message
         messageBodyPart.setText(args[1]);
         
         // Create a multipart message
         Multipart multipart = new MimeMultipart();

         // Set text message part
         multipart.addBodyPart(messageBodyPart);

         // Part two is attachment
         messageBodyPart = new MimeBodyPart();
         messageBodyPart.setDisposition(Part.ATTACHMENT);
         messageBodyPart.setHeader("Content-Transfer-Encoding", "base64");
         addAttachment(multipart, args[2]);
         //addAttachment(multipart, args[3]);

         // Send the complete message parts
         message.setContent(multipart);

         // Send message
         Transport.send(message);
         System.out.println("Message sent successfully....");
         
      } catch (MessagingException mex) {
         mex.printStackTrace();
      }
   }
   
   private static void addAttachment(Multipart multipart, String filename) throws MessagingException {
       DataSource source = new FileDataSource(filename);
       BodyPart messageBodyPart = new MimeBodyPart();        
       messageBodyPart.setDataHandler(new DataHandler(source));
       messageBodyPart.setFileName("View result.html");
       multipart.addBodyPart(messageBodyPart);
   }
}