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
	  String from = ZimbraSeleniumProperties.getConfigProperties().getString("emailFrom");
	      
	  // Recipient's email ID needs to be mentioned.
      String to = ZimbraSeleniumProperties.getConfigProperties().getString("emailTo");

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

      try{
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